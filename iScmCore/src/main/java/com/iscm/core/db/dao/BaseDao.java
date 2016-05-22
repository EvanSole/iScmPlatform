package com.iscm.core.db.dao;

import com.iscm.AutoBasePO;
import com.iscm.BasePO;
import com.iscm.NormalBasePO;
import com.iscm.common.constants.AutoIdConstants;
import com.iscm.common.utils.IScmBeanUtil;
import com.iscm.core.db.splitdb.DbShardsUtil;
import com.iscm.core.db.util.AliasToMapResultTransformer;
import com.iscm.core.db.util.PoUtils;
import com.iscm.service.autoid.IAutoIdClient;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.*;
import org.hibernate.InstantiationException;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;


import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public abstract class BaseDao<T extends BasePO> {

    private Class<T> entityClass;
    @Autowired
    public IAutoIdClient autoIdClient;
    @Autowired
    private SessionFactory sessionFactory;

    public void flush(String... splitTable){
        this.getSession(splitTable).flush();
    }
    /**
     * 通过反射获取子类确定的泛型类
     */
    public BaseDao() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class) params[0];
    }

    /******************************************以下为开放方法********************************************/
    /**
     * 根据ID加载PO实例
     *
     * @param id
     * @return 返回相应的持久化PO实例
     */
    public T load(Serializable id, String... splitTable) {
        return (T) getSession(splitTable).load(entityClass, id);
    }

    /**
     * 根据ID获取PO实例
     *
     * @param id
     * @return 返回相应的持久化PO实例
     */
    public T get(Serializable id, String... splitTable) {
        return (T) getSession(splitTable).get(entityClass, id);
    }

    /*
    根据ID获取PO实例
    并使用悲观锁 锁定记录
     */
    public T getForLock(Serializable id, String... splitTable){
        String hql = " from " + entityClass.getSimpleName() + " lockObj where id = :id";
        Map paramsMap = new HashMap<>();
        paramsMap.put("id",id);

        Query query = null;
        query = createQuery(hql, paramsMap, false, splitTable);
        query.setLockMode("lockObj" , LockMode.PESSIMISTIC_WRITE);
        List list = query.list();

        if(list.size() > 0){
            return (T)list.get(0);
        }else{
            return null;
        }

    }


    /**
     * 执行查询,默认执行hql
     *
     * @param hql
     * @param tableAlias 表别名
     * @param obj 查询条件对象 map或object
     * @return 查询结果
     */
    public List findByHqlForLock(String hql,String tableAlias, Object obj, String... splitTable) {
        Query query = null;
        query = createQuery(hql, obj, false, splitTable);
        query.setLockMode(tableAlias, LockMode.PESSIMISTIC_WRITE);

        return query.list();
    }


    /**
     * 执行查询,默认执行hql
     *
     * @param hql
     * @param obj 查询条件对象 map或object
     * @return 查询结果
     */
    public List findByHqlForLock(String hql, Object obj, String... splitTable) {
        Query query = null;
        query = createQuery(hql, obj, false, splitTable);
        query.setLockOptions(LockOptions.UPGRADE);

        return query.list();
    }
    /**
     * 获取PO的所有对象
     *
     * @return
     */
    public List<T> loadAll(String... splitTable) {
        Criteria criteria = getSession(splitTable).createCriteria(entityClass);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }


    public T save(T entity, String... splitTable) throws Exception {
        setEntityId(entity);
        PoUtils.setPoDefaultValue(entity);
        getSession(splitTable).save(entity);
        setAutoEntityId(entity);
        return entity;
    }

    /**
     * 删除PO
     * @param entity
     */
    public void delete(T entity, String... splitTable) {
        getSession(splitTable).delete(entity);
    }

    /**
     * 根据主键id删除PO
     */
    public void delete(long id, String... splitTable) {
        String hql = "delete " + entityClass.getName() + " where id = :id";
        Map param = new HashMap();
        param.put("id", id);
        executeByHql(hql, param, splitTable);
    }

    /**
     * 获得BasePO类
     * @param cls
     * @return
     */
    private Class getBasePoClass(Class cls){
        if(cls == BasePO.class){
            return cls;
        }else{
            return getBasePoClass(cls.getSuperclass());
        }
    }

    private void ONUpdate(T entity) {
        try {
            //根据传入的属性名称构造属性的set方法名

            Method method = getBasePoClass(entity.getClass()).getMethod("setUpdateTime",Long.class);

            if (null != method) {
                method.invoke(entity, new Date().getTime());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 更改PO
     *
     * @param entity
     */
    public void update(T entity, String... splitTable) {
        this.ONUpdate(entity);
        getSession(splitTable).update(entity);
    }

    /**
     * 更改PO
     *
     * @param entity
     */
    public void updateSetNullValue(T entity, String... splitTable) throws Exception{
        PoUtils.setPoDefaultValue(entity);
        this.update(entity);
    }


    /**
     * 保存或更新PO，用于保存临时状态或更新游离状态的PO
     *
     * @param entity
     */
    public T saveOrUpdate(T entity, String... splitTable) throws Exception {
        long id = getEntityId(entity);
        if (id == 0) {
            this.save(entity, splitTable);
        } else {
            T target = (T) get(id, splitTable);
            if (target != null) {
                //BeanUtils.copyProperties(entity, target);
                IScmBeanUtil.copyProperties(entity, target);
                this.ONUpdate(target);
                getSession(splitTable).merge(target);
            } else {
                this.save(entity, splitTable);
            }
        }
        return entity;
    }


    /**
     * 执行查询,默认执行hql
     *
     * @param hql
     * @param obj 查询条件对象 map或object
     * @return 查询结果
     */
    public List findByHql(String hql, Object obj, String... splitTable) {
        Query query = null;
        query = createQuery(hql, obj, false, splitTable);
        return query.list();
    }




    /**
     * 执行查询,默认执行sql
     *
     * @param sql
     * @param obj 查询条件对象 map或object
     * @return 查询结果
     */
    public List findBySql(String sql, Object obj, String... splitTable) {
        Query query = null;
        query = createQuery(sql, obj, true, splitTable);
        return query.list();
    }

    /**
     * 根据hql查询
     *
     * @param hql
     * @param obj
     * @param splitTable
     * @return
     * @throws Exception
     */
    public Map pageQueryByHql(String hql, Object obj, String... splitTable) throws Exception {
        return pagedQuery(hql, obj, false, splitTable);
    }


    /**
     * 根据sql 查询
     *
     * @param sql
     * @param obj
     * @param splitTable
     * @return
     * @throws Exception
     */
    public Map pageQueryBySql(String sql, Object obj, String... splitTable) throws Exception {
        return pagedQuery(sql, obj, true, splitTable);
    }


    /**
     * 分页查询函数，使用hql.
     *
     * @throws Exception
     */
    private Map pagedQuery(String hql, Object obj, boolean nativeFlag, String... splitTable) throws Exception {
        Map retMap = new HashMap();

        int page = 1, pageSize = 15;
        String _gridIdForExport = "";

        if (obj != null) {
            int tempNum = 0;
            tempNum = getPageMessage(obj, "page");
            if (tempNum != 0) {
                page = tempNum;
            }

            tempNum = 0;
            tempNum = getPageMessage(obj, "pageSize");
            if (tempNum != 0) {
                pageSize = tempNum;
            }

        }
        int startIndex = (page - 1) * pageSize;

        // Count查询
        long total = findForRowCount(hql, obj, nativeFlag(nativeFlag), splitTable);


        Query query = createQuery(hql, obj, nativeFlag(nativeFlag), splitTable);
        List list = query.setFirstResult(startIndex).setMaxResults(pageSize).list();


        retMap.put("rows", list);
        retMap.put("total", total);

        return retMap;
    }


    /**
     * 返回第一行第一列(sql查询)
     *
     * @param sql
     * @param obj
     * @return
     */
    public Object executeScalarBySql(String sql, Object obj, String... splitTable) {
        return executeScalar(sql, obj, true, splitTable);
    }


    /**
     * 返回第一行第一列(hql查询)
     *
     * @param hql
     * @param obj
     * @return
     */
    public Object executeScalarByHql(String hql, Object obj, String... splitTable) {
        return executeScalar(hql, obj, false, splitTable);
    }

    /**
     * 返回第一行第一列
     *
     * @param sql
     * @param obj
     * @param nativeFlag
     * @return
     */
    private Object executeScalar(String sql, Object obj, boolean nativeFlag, String... splitTable) {
        Query query = createQuery(sql, obj, nativeFlag, splitTable);
        List list = query.list();
        if (null != list && list.size() > 0) {
            try {
                Object objtemp = list.get(0);
                if (null != objtemp) {
                    if (objtemp instanceof Map) {
                        Map map = (Map) objtemp;
                        return map.values().toArray()[0];
                    }
                    return objtemp;
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 批量执行sql
     *
     * @param sql    sql
     * @param params 参数list
     */
    @Deprecated
    private void batchExecute(final String sql, final List params, String... splitTable) {
        this.getSession(splitTable).doWork(new Work() {
            @Override
            public void execute(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql);

                try {
                    for (int i = 0; i < params.size(); i++) {
                        Object[] obj = (Object[]) params.get(i);
                        for (int j = 0; j < obj.length; j++) {
                            ps.setObject(j + 1, obj[j]);
                        }
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }finally {
                    ps.close();
                }
            }
        });
    }

    /**
     * 批量执行sql
     *
     * @param list sql List
     */
    @Deprecated
    private void executeBatchSql(final List<Map> list, String... splitTable) {
        this.getSession(splitTable).doWork(new Work() {
            @Override
            public void execute(Connection conn) throws SQLException {
                Statement ps = conn.createStatement();

                try {
                    for (int i = 0; i < list.size(); i++) {
                        String sql = (String) list.get(i).get("sql");
                        ps.addBatch(sql);
                    }
                    ps.executeBatch();
                }finally {
                    ps.close();
                }
            }
        });
    }
    /**
     * 包装nativeFlag参数
     *
     * @param nativeFlag
     * @return
     */
    private boolean nativeFlag(boolean... nativeFlag) {
        boolean flag = false;
        if (null != nativeFlag && nativeFlag.length > 0) {
        //更好的写法是if(ArrayUtils.isNotEmpty(nativeFlag),但这是框架级别代码尽量不要依赖第三方工具包
            flag = nativeFlag[0];
        }
        return flag;
    }

    /**
     * 获得对象中的page相关属性
     *
     * @param obj
     * @param proName
     * @return
     */
    private int getPageMessage(Object obj, String proName) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        int num = 0;
        Object numObj;
        if (obj instanceof Map) {
            numObj = ((Map) obj).get(proName);

        } else {
            numObj = BeanUtils.getProperty(obj, proName);

        }

        if (numObj != null) {
            num = Integer.parseInt(numObj.toString());
        }
        return num;
    }

    /**
     * 执行sql 包括更新等操作
     *
     * @param sql
     * @param obj
     */
    public int executeBySql(String sql, Object obj, String... splitTable) {
        Query query = createQuery(sql, obj, true, splitTable);
        return query.executeUpdate();
    }

    /**
     * 执行hql 包括更新等操作
     *
     * @param hql
     * @param obj
     */
    public int executeByHql(String hql, Object obj, String... splitTable) {
        Query query = createQuery(hql, obj, false, splitTable);
        return query.executeUpdate();
    }


    /**
     * 批量执行hql
     * @param hql
     * @param list
     * @param cls
     * @param splitTable
     */
    public void batchExecuteHql(final String hql, final List <List>list, final Class <T> cls, final String... splitTable) {
        this.getSession(splitTable).doWork(new Work() {
            @Override
            public void execute(Connection conn) throws SQLException {
                List<ColumnObject> colList = getColumnObjList(cls);
                String innerSql = hql;
                if (colList == null) {
                    colList = new ArrayList<ColumnObject>();
                }
                if (colList.size() == 0) {
                    try {
                        makeColumnList(colList, cls);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Table table = cls.getAnnotation(Table.class);


                innerSql = convertHql2Sql(innerSql, colList);
                innerSql = innerSql.replace(cls.getSimpleName(), table.name());
                if(splitTable != null && splitTable.length > 0){
                    innerSql = DbShardsUtil.parseSql(innerSql, splitTable[0]);
                }

                PreparedStatement ps = conn.prepareStatement(innerSql);

                try {
                    for (int i = 0; i < list.size(); i++) {
                        List innerList = list.get(i);
                        for (int j = 0; j < innerList.size(); j++) {
                            ps.setObject(j + 1, innerList.get(j));
                        }
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }finally {
                    ps.close();
                }
            }
        });
    }

    /**
     * 创建Query对象. 对于需要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设置.
     * 留意可以连续设置,如下：
     * <pre>
     * dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
     * </pre>
     *
     * @param hql
     * @param paramMap   参数map
     * @param nativeFlag 是否为本地sql
     */
    private Query createQuery(String hql, Object paramMap, boolean nativeFlag, String... splitTable) {
        Query query = null;
        if (nativeFlag(nativeFlag) == true) {
            query = getSession(splitTable).createSQLQuery(hql);
            query.setResultTransformer(AliasToMapResultTransformer.INSTANCE);
        } else {
            query = getSession(splitTable).createQuery(hql);
        }

        if (paramMap != null) {
            if (paramMap instanceof Map) {
                query.setProperties((Map) paramMap);
            } else {
                query.setProperties(paramMap);
            }
        }

        return query;
    }


    private long findForRowCount(final String queryStr, final Object paramMap, boolean nativeFlag, String... splitTable) {
        String countSql = "";
        if (nativeFlag(nativeFlag) == true) {
            countSql = getCountSQL(queryStr);
        } else {
            countSql = getCountHQL(queryStr);
        }
        return findForLong(countSql, paramMap, nativeFlag(nativeFlag), splitTable);
    }

    private String getCountHQL(String hql) {
        String localObject1 = "*";
        hql = hql.replaceAll("\\s+", " ");
        int i = hql.toLowerCase().indexOf("from ");
        int j = hql.toLowerCase().indexOf("distinct");
        int k = hql.toLowerCase().indexOf("select");
        if ((-1 != j) && (i > j) && (j > k)) {
            String localObject2[] = hql.substring(k + 6, i).split(",");
            String str = "";
            for (int m = 0; m < localObject2.length; m++) {
                str = localObject2[m].trim();
                if (!str.startsWith("distinct"))
                    continue;
                localObject1 = str;
                break;
            }
        }
        String localObject2 = "select count(" + localObject1 + ") " + hql.substring(i);
        return localObject2;
    }

    private String getCountSQL(String sql) {
        sql = sql.replaceAll("\\s+", " ");
        String str;

        str = "select count(*) as cnt from ( " + sql + " ) cntTab";

        return str;
    }

    private long findForLong(final String hql, final Object paramMap, boolean nativeFlag, String... splitTable) {
        List list = createQuery(hql, paramMap, nativeFlag(nativeFlag), splitTable).list();
        if (-1 != hql.toLowerCase().indexOf("group by")){
            return list.size();
        }
        if (list != null && list.size() == 1) {
            if (nativeFlag(nativeFlag) == true) {
                return ((BigInteger) ((Map) list.get(0)).get("cnt")).longValue();
            } else {
                return (Long) list.get(0);
            }
        } else if(list!=null){
            return list.size();
        }else {
            return 0l;
        }

    }

    private void setEntityId(T entity) throws Exception {
        if (entity instanceof NormalBasePO) {//只有分库分表的需要setiId
            long id = ((NormalBasePO) entity).getId();
            if (id <= 0) {
                long autoId = getAutoId(entity.getClass().getSimpleName());
                if (autoId <= 0) {
                    throw new Exception("BaseDao.setEntityId has error when entity(" + entity.getClass().getSimpleName() + ") to get autoId!");
                }
                ((NormalBasePO) entity).setId(autoId);
            }
        }
    }

    /**
     *
     * @param entity
     */
    private void setAutoEntityId(T entity) {
        try {
            if (entity instanceof AutoBasePO) {
                Long id = (Long)getSession().getIdentifier(entity);
                ((AutoBasePO) entity).setId(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getAutoId(String entityName) {
        Integer key = AutoIdConstants.getMap().get(entityName);
        if (null != key && key > 0) {
            return autoIdClient.getAutoId(key);
        }
        return 0;
    }

    private long getEntityId(T entity) {
        long id = 0;
        try {
            if (entity instanceof AutoBasePO) {
                id = ((AutoBasePO) entity).getId();
            } else if (entity instanceof NormalBasePO) {
                id = ((NormalBasePO) entity).getId();
            } else {
                Method method = entity.getClass().getMethod("getId");
                id = (long) method.invoke(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    SessionFactory getSessionFactory() {
        return sessionFactory;
    }


    Session getSession(String... splitid) {
        if (null != splitid && splitid.length > 0) {
            DatabaseContextHolder.setCustomerTable(splitid[0]);
        }
        return sessionFactory.getCurrentSession();
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
     * 例如：HELLO_WORLD->HelloWorld
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    private String camelName(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母小写
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel :  camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    private ConcurrentMap<Class,List<ColumnObject>> batchSqlEntityMap = new ConcurrentHashMap<Class,List<ColumnObject>>();


    private String convertHql2Sql(String sql,List<ColumnObject>list){
        for(ColumnObject col:list){
            String ori = col.getClsField();
            String dest = col.getField();
            sql = sql.replace(ori,dest);
        }
        return sql;
    }

    private List<ColumnObject> getColumnObjList(Class cls){
        return batchSqlEntityMap.get(cls);
    }

    private void setColumnObjList(Class cls,List<ColumnObject> list){
        batchSqlEntityMap.putIfAbsent(cls, list);
    }

    private String makefrontSql(List<ColumnObject>list,String tableName,Class cls){
        List <String>fieldList = new ArrayList<String>();
        for(ColumnObject columnObject:list){
            if(columnObject.getField().equals("id")){
                if(columnObject.getIdType().equals("db")){
                    continue;
                }
            }
            fieldList.add(columnObject.getField());
        }

        String fieldSql = fieldList.stream().collect(Collectors.joining(", "));
        return "insert into "+ tableName+" ("+fieldSql+") ";
    }

    private String makeValueSql(List<ColumnObject>list,Map map,Class cls){
        List<String>valueList = new ArrayList<String>();
        for(ColumnObject columnObject:list){
            if(columnObject.getField().equals("id")){
                if(columnObject.getIdType().equals("db")){

                }else{
                    valueList.add(String.valueOf(map.get(columnObject.getClsField())));

                }
                continue;
            }
            Object valueObj = map.get(columnObject.getClsField());
            String value = "";
            if(valueObj == null){
                value = "null";
            }else{
                if(columnObject.getType().equals("1")){
                    value = getSqlVal(String.valueOf(valueObj));
                }else{
                    value += String.valueOf(valueObj);
                }

            }
            valueList.add(value);
        }
        String valueSql = valueList.stream()
                .collect(Collectors.joining(", "));

        return "("+valueSql+")";
    }

    public void executeBatchSqlByEntity(final List <Map>list, final Class <T>cls, String... splitTable){
        this.getSession(splitTable).doWork(new Work() {
            @Override
            public void execute(Connection conn) throws SQLException {
                List <ColumnObject> colList = getColumnObjList(cls);
                if(colList == null){
                    colList = new ArrayList<ColumnObject> ();
                }
                if(colList.size()==0){
                    try {
                        makeColumnList(colList,cls);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Statement ps = conn.createStatement();

                long start = System.currentTimeMillis();
                Table table = cls.getAnnotation(Table.class);
                String sql = makefrontSql(colList,table.name(),cls) +" values";
                for (int i = 0; i < list.size(); i++) {
                    Map map = list.get(i);
                    sql+= makeValueSql(colList,map,cls) + ",";
                }
                sql = sql.substring(0,sql.length()-1);

                try {
                    ps.execute(sql);
                    System.out.println("反射用了 " + String.valueOf(System.currentTimeMillis() - start) + "ms");
                    ps.executeBatch();
                }finally {
                    ps.close();
                }

            }
        });
    }


    public void executeBatchSqlByEntity(final String sql, final List <Map>list, final Class <T>cls, String... splitTable) {

        this.getSession(splitTable).doWork(new Work() {
            @Override
            public void execute(Connection conn) throws SQLException {
                List <ColumnObject> colList = getColumnObjList(cls);
                if(colList == null){
                    colList = new ArrayList<ColumnObject> ();
                }
                if(colList.size()==0){
                    try {
                        makeColumnList(colList,cls);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Statement ps = conn.createStatement();
                ps.clearBatch();
                long start = System.currentTimeMillis();
                Table table = cls.getAnnotation(Table.class);

                try {
                    for (int i = 0; i < list.size(); i++) {

                        try {
                            //                        System.out.println(makeBatchSql(colList,list.get(i)));
                            ps.addBatch(makeBatchSql(colList, list.get(i), table.name(), cls));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("反射用了 " + String.valueOf(System.currentTimeMillis() - start) + "ms");
                    ps.executeBatch();
                }finally {
                    ps.close();
                }

            }
        });
    }

    public String makeBatchSql(List<ColumnObject>list,Map map,String tableName,Class cls) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        List<String> fieldList = new ArrayList<String>();
        List<String> valueList = new ArrayList<String>();

        for(ColumnObject columnObject:list){
            if(columnObject.getField().equals("id")){
                if(columnObject.getIdType().equals("db")){

                }else{
                    fieldList.add(columnObject.getField());
                    valueList.add(String.valueOf(map.get(columnObject.getClsField())));

                }
                continue;
            }
            fieldList.add(columnObject.getField());
            Object valueObj = map.get(columnObject.getClsField());
            String value = "";
            if(valueObj == null){
                value = "null";
            }else{
                if(columnObject.getType().equals("1")){
                    value = getSqlVal(String.valueOf(valueObj));
                }else{
                    value += String.valueOf(valueObj);
                }

            }
            valueList.add(value);
        }


        String fieldSql = fieldList.stream()
                .collect(Collectors.joining(", "));

        String valueSql = valueList.stream()
                .collect(Collectors.joining(", "));

        return "insert into "+ tableName+" ("+fieldSql+") "  + " values("+valueSql+")";
    }

    /**
     * 拼装sql val
     * @param val
     * @return
     */
    public String getSqlVal(String val){
        return "'" + getPreventInjectValue(val) + "'";
    }

    /**
     * 获得防注入的val
     * @param val
     * @return
     */
    public String getPreventInjectValue(String val){
        return val.replaceAll("'","''");
    }

    private void makeColumnList(List list,Class cls) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if(cls == Object.class){
            return;
        }
        Method m[]=cls.getDeclaredMethods();
        for(int i = 0;i < m.length;i++){
            Method m1 = m[i];
            Column col = m1.getAnnotation(Column.class);
            if(col != null){
                ColumnObject columnObject = new ColumnObject();
                columnObject.setField(col.name());
                columnObject.setNullable(col.nullable());
                columnObject.setMethod(m1);

                columnObject.setFieldCls(m1.getReturnType());


                if(col.name().equals("id")){
                    if(cls == NormalBasePO.class){
                        columnObject.setIdType("service");
                    }else{
                        columnObject.setIdType("db");
                    }
                }
                String clsFieldTemp= m1.getName().substring(3);
                clsFieldTemp = (char)(clsFieldTemp.charAt(0) + 32)+clsFieldTemp.substring(1);
                columnObject.setClsField(clsFieldTemp);

                list.add(columnObject);
            }

        }

        if(cls.getSuperclass()!=null){
            makeColumnList(list, cls.getSuperclass());
        }
    }


    class ColumnObject{
        private String field;
        private Method method;
        private String type;
        private Class fieldCls;
        private boolean isNullable;
        private String idType;
        private String clsField;


        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Class getFieldCls() {
            return fieldCls;
        }

        public void setFieldCls(Class fieldCls) {
            this.fieldCls = fieldCls;
            if(fieldCls == String.class){
                this.setType("1");
            }else{
                this.setType("2");
            }
        }

        public boolean isNullable() {
            return isNullable;
        }

        public void setNullable(boolean isNullable) {
            this.isNullable = isNullable;
        }


        public String getIdType() {
            return idType;
        }

        public void setIdType(String idType) {
            this.idType = idType;
        }

        public String getClsField() {
            return clsField;
        }

        public void setClsField(String clsField) {
            this.clsField = clsField;
        }

    }

}

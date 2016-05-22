package com.iscm;

import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@MappedSuperclass
public class BasePO implements Serializable, Cloneable {

    private int page = 0;
    private int pageSize = 15;
    private String sortKey;
    private boolean isAsc = true;
    private String createUser;
    private Long createTime;
    private String updateUser;
    private Long updateTime;

    @Basic
    @Column(name = "create_user", insertable = true, updatable = true, length = 25)
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Basic
    @Column(name = "create_time", insertable = true, updatable = true)
    public Long getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_user", nullable = true, insertable = true, updatable = true, length = 25)
    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Basic
    @Column(name = "update_time", nullable = true, insertable = true, updatable = true)
    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Transient
    public String getInputFiles() {
        return inputFiles;
    }

    public void setInputFiles(String inputFiles) {
        this.inputFiles = inputFiles;
    }

    private String inputFiles;

    private static final Long serialVersionUID = 3967320237495421407L;
    protected transient Logger log = LoggerFactory.getLogger(this.getClass());

    @Transient
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Transient
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Transient
    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    @Transient
    public boolean isAsc() {
        return isAsc;
    }

    public void setAsc(boolean isAsc) {
        this.isAsc = isAsc;
    }


    /**
     * 把字符串中大写字母前加上下划线
     *
     * @param name
     * @return
     */

    private  String getUpper(String name, boolean... nativeFlag) {
        if (null!=nativeFlag && nativeFlag.length>0 && nativeFlag[0]) {
        //更好的写法是if(ArrayUtils.isNotEmpty(nativeFlag),但这是框架级别代码尽量不要依赖第三方工具包
            StringBuffer temp = new StringBuffer();
            for (Integer i = 0; i < name.length(); i++) {
                char tempC = name.charAt(i);
                if (Character.isUpperCase(name.charAt(i))) {
                    temp.append("_");
                }
                temp.append(tempC);
            }
            return temp.toString().toUpperCase();
        }
        return name;
    }

    /**
     * map to bean
     * @param map
     * @param nativeFlag 需要去"-"改变首字母为大写时 ture ；和对象属性名相同时为false 或不传
     */
    public void fromMap(Map<String, Object> map, boolean... nativeFlag) {
        if (map == null || map.isEmpty()) {
            return;
        }
        try {
            for (PropertyDescriptor pd : descriptProperty()) {
                Method method = pd.getWriteMethod();
                if (method != null) {
                    String name = getUpper(pd.getName(), nativeFlag);
                    if(!map.containsKey(name)){
                        name=name.toLowerCase();
                    }
                    if (map.containsKey(name)) {
                        Object value = map.get(name);
                        if (value != null) {
                            Class t = method.getParameterTypes()[0];
                            if (!t.isAssignableFrom(value.getClass())) {
                                try {
                                    value = ConvertUtils.convert(value, t);
                                } catch (Exception e) {
                                    value = null;
                                }
                            }
                        }
                        try {
                            method.invoke(this, value);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("ERROR in convert map {} to {}", map, this.getClass());
            }
            log.error("ERROR in convert map to po", e);
        }
    }
    public Map<String, Object> toMap() {
        return toMap(false);
    }
    public Map<String, Object> toMap(boolean  isAll) {
        Map map = new HashMap();
        try {
            for (PropertyDescriptor pd : descriptProperty(isAll)) {
                Method method = pd.getReadMethod();
                if (method != null) {
                    map.put(pd.getName(), method.invoke(this));
                }
            }
        } catch (Exception e) {
            log.error("ERROR in convert po to map", e);
        }
        return map;
    }

    public void init() {
    }

    public void clear() {
        try {
            for (PropertyDescriptor pd : descriptProperty()) {
                Method method = pd.getWriteMethod();
                if (method != null) {
                    method.invoke(this, (Object) null);
                }
            }
        } catch (Exception e) {
            log.error("ERROR in clear po", e);
        }
    }

    @Override
    public String toString() {
        try {
            List<String> list = new ArrayList();
            for (PropertyDescriptor pd : descriptProperty()) {
                Method method = pd.getReadMethod();
                if (method != null) {
                    list.add(pd.getName() + "=" + method.invoke(this));
                }
            }
            return this.getClass().getName() + ":{" + list.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(",")) +"}";
        } catch (Exception e) {
            log.error("ERROR in convert po to string", e);
            return super.toString();
        }
    }

    @Override
    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (Exception e) {
            log.error("ERROR in clone", e);
            try {
                BasePO po = this.getClass().newInstance();
                po.fromMap(this.toMap());
                obj = po;
            } catch (Exception ex) {
            }
        }
        return obj;
    }
    private List<PropertyDescriptor> descriptProperty() throws Exception {
        return descriptProperty(false);
    }
    private List<PropertyDescriptor> descriptProperty(boolean flag) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
        PropertyDescriptor[] pp = beanInfo.getPropertyDescriptors();
        List<PropertyDescriptor> list = new ArrayList<>();
        if(flag){
            for (PropertyDescriptor pd : pp) {
                if (!pd.getName().equals("serialVersionUID")) {
                    list.add(pd);
                }
            }
        } else {

            for (PropertyDescriptor pd : pp) {
                if (!(pd.getName().equals("page") || pd.getName().equals("pageSize") || pd.getName().equals("serialVersionUID")||pd.getName().equals("currentUser"))) {
                    list.add(pd);
                }
            }
        }
        return list;
    }

}

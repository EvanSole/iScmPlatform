package com.iscm.core.db.service;


import com.iscm.AutoBasePO;
import com.iscm.BasePO;
import com.iscm.NormalBasePO;
import com.iscm.vo.DbShardVO;
import com.iscm.vo.MessageResult;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseService  {

    /**
     * 占位方法 用于解决spring在添加切面的时候需要有一个方法为public的问题
     */
    public void occupyingMehtod() {
    }


    public Map convertPageMapList(Map map) throws Exception{
        List rows = (List) map.get("rows");
        List <Map>retRows = new ArrayList<Map>();

        for(int i = 0;i < rows.size();i++){

            Object []obj = (Object[]) rows.get(i);
            retRows.add(revertObj2Map(obj));
        }

        map.put("rows",retRows);

        return map;
    }

    public Map revertObj2Map(Object []obj) {
        Map rmap = new HashMap();
        Long rootObjId = 0L;

        List rootFieldList = new ArrayList();

        rootFieldList.add("createUser");
        rootFieldList.add("createTime");
        rootFieldList.add("updateUser");
        rootFieldList.add("updateTime");


        Map <String,Object> rootFieldMap = new HashMap();

        for(int i = 0;i < obj.length;i++){
            Object objTemp = obj[i];
            //规则要求根对象在第一位 获取它的id作为整体id
            if(i == 0){
                if(objTemp instanceof AutoBasePO){
                    rootObjId = ((AutoBasePO)objTemp).getId();
                }else if(objTemp instanceof NormalBasePO){
                    rootObjId = ((NormalBasePO)objTemp).getId();
                }

                rootFieldMap = getRootFieldValue(objTemp,rootFieldList);


            }

            if(objTemp != null )
                rmap.putAll(((BasePO) objTemp).toMap());
        }

        rmap.put("id",rootObjId);

        for(Map.Entry<String,Object> entry:rootFieldMap.entrySet()){
            rmap.put(entry.getKey(),entry.getValue());
        }

        return rmap;
    }

    private Map getRootFieldValue(Object obj,List <String>rootFieldList){
        Map retMap = new HashMap();
        for(String str:rootFieldList){
            Field field = null;
            try {
                field = obj.getClass().getDeclaredField(str);

                if(field != null){
                    try {
                        field.setAccessible(true);
                        Object fieldValue = field.get(obj);
                        retMap.put(str,fieldValue);
                    } catch (IllegalAccessException e) {

                    }
                }
            } catch (NoSuchFieldException e) {

            }

        }

        return retMap;
    }

    /**
     * service 返回结果公用方法
     * @param code
     * @param param
     * @return
     */
    public MessageResult getMessageResult(String code,Object ...param){
        return MessageResult.getMessage(code, param);
    }
    /**
     * 进入DAO时-获取分表参数
     * @param dbShardVO
     * @return
     */
    public String getSplitTableKey(DbShardVO dbShardVO) {
        return dbShardVO.getShardTableId();
    }

}

package com.iscm.base.dao;

import com.iscm.core.db.dao.BaseDao;
import com.iscm.po.base.IScmCodeHeaderEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CodeHeaderDao extends BaseDao<IScmCodeHeaderEntity> {


    public IScmCodeHeaderEntity getCodeHeader(String listName) {
        Map map = new HashMap();
        StringBuffer sb = new StringBuffer(" from IScmCodeHeaderEntity t where 1=1 ");
        if (StringUtils.isNotEmpty(listName)){
            sb.append(" and t.listName=:listName");
            map.put("listName", listName);
        }
        return (IScmCodeHeaderEntity) this.executeScalarByHql(sb.toString(), map);
    }

    public List<IScmCodeHeaderEntity> getCodeHeader(Map<String, Object> map) {
        StringBuffer hql = new StringBuffer(" from IScmCodeHeaderEntity t where 1=1 ");
        searchParam(map,hql);
        return this.findByHql(hql.toString(),map);
    }

    private void searchParam(Map map, StringBuffer hql) {
        if(map.containsKey("listName")){
            hql.append(" and t.listName=:listName ");
        }
    }
}

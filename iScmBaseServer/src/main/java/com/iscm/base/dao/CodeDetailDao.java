package com.iscm.base.dao;

import com.iscm.core.db.dao.BaseDao;
import com.iscm.po.base.IScmCodeDetailEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class CodeDetailDao extends BaseDao<IScmCodeDetailEntity> {

    public List<IScmCodeDetailEntity> getCodeDetailList(Long codeId) {
        Map map = new HashMap();
        StringBuffer sb = new StringBuffer(" from IScmCodeDetailEntity t where 1=1 ");
        if (codeId != 0){
            sb.append(" and t.codeId=:codeId");
            map.put("codeId", codeId);
        }
        return this.findByHql(sb.toString(), map);
    }
}

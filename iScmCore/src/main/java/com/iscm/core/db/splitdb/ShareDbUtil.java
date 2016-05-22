package com.iscm.core.db.splitdb;

import com.iscm.em.DbShareField;
import com.iscm.vo.CurrenUserEntity;
import com.iscm.vo.DbShardVO;
import org.apache.commons.lang3.ArrayUtils;

public class ShareDbUtil {

    public static DbShardVO getDbShardVO(CurrenUserEntity user,Long zoneId, DbShareField...source){
        DbShardVO dbShardVO = DbShardVO.getInstance(user);
        dbShardVO.setZoneId(zoneId);
        if(ArrayUtils.isNotEmpty(source)){
            dbShardVO.setSource(source[0]);
            //默认分表属性
            if(source[0] == DbShareField.ORDER || source[0] == DbShareField.REPORT){
                dbShardVO.setShardTableId(user.getTenantId()+"");
            }
        }
        return dbShardVO;
    }

    public static DbShardVO getDbShardVO(CurrenUserEntity user, DbShareField...source){
        DbShardVO dbShardVO = DbShardVO.getInstance(user);
        if(ArrayUtils.isNotEmpty(source)){
            dbShardVO.setSource(source[0]);
            //默认分表属性
            if(source[0] == DbShareField.ORDER || source[0] == DbShareField.REPORT){
                dbShardVO.setShardTableId(user.getTenantId()+"");
            }
        }
        return dbShardVO;
    }
}

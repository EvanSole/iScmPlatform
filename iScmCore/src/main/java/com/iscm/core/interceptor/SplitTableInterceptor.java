package com.iscm.core.interceptor;

import com.iscm.core.db.dao.DatabaseContextHolder;
import com.iscm.core.db.splitdb.DbShardsUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.EmptyInterceptor;

public class SplitTableInterceptor extends EmptyInterceptor {

    @Override
    public String onPrepareStatement(String sql) {
        String splitFlag = DatabaseContextHolder.getCustomerTable();

        if (StringUtils.isEmpty(splitFlag)) {
            return sql;
        }
        return DbShardsUtil.parseSql(sql, splitFlag);
    }

}

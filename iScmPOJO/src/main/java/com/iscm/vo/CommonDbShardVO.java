package com.iscm.vo;


import com.iscm.em.DbShareField;

public class CommonDbShardVO {
    protected String shardDbId;//分库规则id
    protected String shardTableId;//分表规则id
    protected DbShareField source;//默认为order
    private CurrenUserEntity currentUser;

    public String getShardDbId() {
        return shardDbId;
    }

    public void setShardDbId(String shardDbId) {
        this.shardDbId = shardDbId;
    }

    public String getShardTableId() {
        return shardTableId;
    }

    public void setShardTableId(String shardTableId) {
        this.shardTableId = shardTableId;
    }

    public DbShareField getSource() {
        return source;
    }

    public void setSource(DbShareField source) {
        this.source = source;
    }

    public CurrenUserEntity getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrenUserEntity currentUser) {
        this.currentUser = currentUser;
    }
}

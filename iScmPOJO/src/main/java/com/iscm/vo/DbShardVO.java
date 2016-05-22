package com.iscm.vo;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class DbShardVO extends CommonDbShardVO implements Serializable {

    private long zoneId;//当前区域

	public String getShardDbId() {
		return shardDbId;
	}

	@Override
	public String getShardTableId() {
		if (StringUtils.isEmpty(shardTableId)) {
			// 如果分表id没有set，默认使用区域id分表
			long temp = this.getZoneId();
			if (temp > 0) {
				this.setShardTableId(this.getZoneId() + "");
			}else{
                //如果区域id没有set，默认使用用户所属租户id分表
                this.setShardTableId(this.getCurrentUser().getTenantId()+"");
            }
		}
		return shardTableId;
	}

	public void setShardTableId(String shardTableId) {
		this.shardTableId = shardTableId;
	}

	/**
	 * 默认主库
	 * 
	 * @return
	 */
	public static DbShardVO getInstance(CurrenUserEntity userEntity) {
		DbShardVO dbShardVO = new DbShardVO();
		dbShardVO.setCurrentUser(userEntity);
        dbShardVO.setShardDbId(userEntity.getTenantId()+"");
		return dbShardVO;
	}


	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

}

package com.iscm.dto;


import com.iscm.po.main.IScmUserEntity;

public class IScmUserDto extends IScmUserEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8105250201499234978L;

	private String userName;
	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

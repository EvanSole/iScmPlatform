package com.iscm.service.autoid;

public interface IAutoIdClient {

	public int getAutoId(int key);

	public int getAutoId(int key, int count);

	public int getAutoId(int key, int count, int targetTableIndex);
}

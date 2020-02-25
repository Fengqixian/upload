package com.clinbrain.bd.mdm.genid.service;

public interface CounterService {
	
	String getTableId(String tableName) throws Exception;
	
	String getFlowNo(String keyName) throws Exception;
	
	long getLongNo(String keyName) throws Exception;
	
	String batchGetFlowNo(String keyName) throws Exception;

	String generator(String keyType,String keyName) throws Exception;
	
}

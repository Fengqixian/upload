package com.clinbrain.bd.mdm.genid.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.mdm.genid.cache.KeyInfo;
import com.clinbrain.bd.mdm.genid.entity.BaseKeys;
import com.clinbrain.bd.mdm.genid.mapper.KeyInfoMapper;

import java.util.List;

public interface KeyInfoService extends IService<BaseKeys> {
	List<BaseKeys> selectList(BaseKeys baseKeys);
	String createKey(String name) throws Exception;
	
	String createKey(String name, String prefix, String suffix, int length) throws Exception;
	
	long createKeyAsLong(String name) throws Exception;
	
	void putKeyInfo(String key, KeyInfo keyInfo);

	int updateKeyInfoValue(long keyValue, String keyName, Integer version);
}

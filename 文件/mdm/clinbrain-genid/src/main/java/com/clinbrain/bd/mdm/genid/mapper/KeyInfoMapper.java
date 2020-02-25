package com.clinbrain.bd.mdm.genid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clinbrain.bd.mdm.genid.cache.KeyInfo;
import com.clinbrain.bd.mdm.genid.entity.BaseKeys;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface KeyInfoMapper  extends BaseMapper<BaseKeys> {
	int updateKeyInfoValue(@Param("keyValue") long keyValue, @Param("keyName")String keyName, @Param("version") int version);
}

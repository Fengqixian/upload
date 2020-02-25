package com.clinbrain.bd.dataserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author WANGYI
 * @className com.clinbrain.bt.metadata.dao.DataProviderMapper
 * @createdDate 2019/5/9 16:00
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (DataServer)
 */
@Mapper
public interface DataProviderMapper extends BaseMapper{
    String getExecuteSqlWithOutParam(String key);
}

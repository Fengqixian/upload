package com.clinbrain.bd.dataserver.service.impl;

import com.clinbrain.bd.dataserver.dto.DataServer;
import com.clinbrain.bd.dataserver.mapper.DataProviderMapper;
import com.clinbrain.bd.dataserver.mapper.DataServerMapper;
import com.clinbrain.bd.dataserver.service.DataProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
*@author WANGYI
*@createdDate 2019/5/9 15:56
*@description TODO
*@class DataProviderService
*@group bigdata develop group ()
*@params
*@return
*@updatedDate
*@updatedBy
*/
@Service
public class DataProviderServiceImpl implements DataProviderService {
    @Autowired
    private DataProviderMapper dataProviderMapper;
    /**
     * 根据key查询还未注入参数的模板SQL
     * @param key
     * @return
     */
    @Override
    public String getExecuteSqlWithOutParam(String key) {
        return dataProviderMapper.getExecuteSqlWithOutParam(key);
    }
}

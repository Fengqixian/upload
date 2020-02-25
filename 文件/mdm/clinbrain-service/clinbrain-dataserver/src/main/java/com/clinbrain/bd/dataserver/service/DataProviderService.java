package com.clinbrain.bd.dataserver.service;

import com.clinbrain.bd.dataserver.dto.DataServer;

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
public interface DataProviderService {
    /**
     * 根据key查询还未注入参数的模板SQL
     * @param key
     * @return
     */
    String  getExecuteSqlWithOutParam(String key);
}

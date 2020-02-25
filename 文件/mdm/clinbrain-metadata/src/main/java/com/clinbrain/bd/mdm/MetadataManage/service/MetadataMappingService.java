/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.clinbrain.bd.mdm.MetadataManage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataMapping;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataModel;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataProperties;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;

import java.util.List;
import java.util.Map;

/**
  * 元数据映射
  * @param
  * @return 
  * @author yjt
  * @date  2019/6/4 11:29 
 */
public interface MetadataMappingService extends IService<MetadataMapping> {
//    ResultData getMetaModuleTree(String jsonParam);
//    ResultData getModuleAttList(RequestDto<Map<String, Object>> jsonParam);
//    PageAjax getMetaDataTree(RequestDto<Map<String, Object>> jsonParam);
//    PageAjax getMappingInfoList(RequestDto<Map<String, Object>> jsonParam);
//    ResultData saveMappingInfo(RequestDto<Map<String, Object>> jsonParam);
//    ResultData deleteMappingInfo(RequestDto<Map<String, Object>> jsonParam);
//    ResultData auditMappingInfo(RequestDto<Map<String, Object>> jsonParam);
//
//    ResultData getAuditMappingModuleList(RequestDto<Map<String, Object>> jsonParam);
//
//    PageAjax getMappingSourceDataList(RequestDto<Map<String, Object>> jsonParam);
//    PageAjax getMappingTargetDataList(RequestDto<Map<String, Object>> jsonParam);
//
//    String getMetaDataMappingInfoList(String jsonParam);
    boolean saveMappingInfo(String jsonParam);

    boolean deleteMappingInfo(String jsonParam);

    boolean auditMappingInfo(String jsonParam);

    IPage<MetadataValue> getMappingSourceDataList(Page<MetadataProperties> page, String jsonParam);

    List<Map<String, Object>> getMappingTargetDataList(Page<MetadataProperties> page ,String jsonParam);
}

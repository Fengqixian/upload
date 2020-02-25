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
package com.clinbrain.bd.mdm.MetadataManage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataMapping;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataModel;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MetadataMappingMapper extends BaseMapper<MetadataMapping> {
    List<Map<String,Object>> selectMappingInfoList(MetadataMapping metaDataMap);

    void saveMetaDataMappingInfo(@Param("metaDataMap")MetadataMapping metaDataMap);

    void deleteMetaDataMappingInfo(@Param("metaDataMap") MetadataMapping metaDataMap);

    void auditMetaDataMappingInfo(@Param("status") Integer status,@Param("metaDataMap") MetadataMapping metaDataMap,@Param("targetDataList") String targetDataList);

    List<Map<String,Object>> selectMappingTargetModuleList(@Param("status") String status,@Param("sourceModuleID") String sourceModuleID);

    IPage<MetadataValue> getMappingSourceDataList(Page page, @Param("sourceModuleID") String sourceModuleID, @Param("status") String status, @Param("searchValue") String searchValue );

    List<Map<String,Object>> selectMappingTargetDataList(@Param("status") String status,@Param("sourceModuleID") String sourceModuleID,@Param("sourceDataID") String sourceDataID,@Param("targetModuleID") String targetModuleID);

}

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
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataProperties;
import org.apache.ibatis.annotations.Param;

/**
 * 属性
 *
 * @author wangyi
 * @date 2019-05-29 10:05:46
 */
public interface MetadataPropertiesMapper extends BaseMapper<MetadataProperties> {
  /**
    * 属性简单分页查询
    * @param metadataProperties 属性
    * @return
    */
  IPage<MetadataProperties> getMetadataPropertiesPage(Page page, @Param("metadataProperties") MetadataProperties metadataProperties);
  String generateMetadataPropertiesMappingColumnSpecial(@Param("metadataProperties")MetadataProperties metadataProperties);
  String generateMetadataPropertiesMappingColumnNormal(@Param("metadataProperties")MetadataProperties metadataProperties);

  boolean updateMetadataPropertiesMappingColumn(@Param("metadataProperties")MetadataProperties metadataProperties);
  String selectColumnSql(@Param("metadataProperties") MetadataProperties metadataProperties);
}

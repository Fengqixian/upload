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
import com.clinbrain.bd.mdm.MetadataManage.dto.MetadataPropertiesDto;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataProperties;

import java.util.List;

/**
 * 属性
 *
 * @author wangyi
 * @date 2019-05-29 10:05:46
 */
public interface MetadataPropertiesService extends IService<MetadataProperties> {

  /**
   * 属性简单分页查询
   * @param metadataProperties 属性
   * @return
   */
  IPage<MetadataProperties> getMetadataPropertiesPage(Page<MetadataProperties> page, MetadataProperties metadataProperties);


  boolean saveProperties(MetadataProperties metadataProperties);

  boolean saveOrUpdateProperties(List<MetadataProperties> metadataProperties);

  String selectColumnSql(MetadataProperties metadataProperties);

  /**
   * 属性简单分页查询
   *
   * @param metadataProperties 属性
   * @return
   */
  IPage<MetadataPropertiesDto> getMetadataPropertiesAndItemsPage(Page<MetadataProperties> page, MetadataProperties metadataProperties);

  }

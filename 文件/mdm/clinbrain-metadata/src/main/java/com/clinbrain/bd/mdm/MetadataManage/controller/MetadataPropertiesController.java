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
package com.clinbrain.bd.mdm.MetadataManage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.dto.MetadataPropertiesDto;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.common.log.annotation.SysLog;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataProperties;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataPropertiesService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * 属性
 *
 * @author wangyi
 * @date 2019-05-29 10:05:46
 */
@RestController
@AllArgsConstructor
@RequestMapping("/metadataproperties")
public class MetadataPropertiesController {

  private final  MetadataPropertiesService metadataPropertiesService;

  /**
   * 简单分页查询
   * @param page 分页对象
   * @param metadataProperties 属性
   * @return
   */
  @GetMapping("/page")
  public R<IPage<MetadataProperties>> getMetadataPropertiesPage(Page<MetadataProperties> page, MetadataProperties metadataProperties) {
    return  new R<>(metadataPropertiesService.getMetadataPropertiesPage(page,metadataProperties));
  }


  /**
   * 通过id查询单条记录
   * @param id
   * @return R
   */
  @GetMapping("/{id}")
  public R<MetadataProperties> getById(@PathVariable("id") Integer id){
    return new R<>(metadataPropertiesService.getById(id));
  }

  /**
   * 新增记录
   * @param metadataProperties
   * @return R
   */
  /*@SysLog("新增属性")
  @PostMapping
  @PreAuthorize("@pms.hasPermission('MetadataManage_metadataproperties_add')")
  public R save(@RequestBody MetadataProperties metadataProperties){
    metadataProperties.setResourceId(UUID.randomUUID().toString());
    return new R<>(metadataPropertiesService.saveProperties(metadataProperties));
  }*/

  /**
   * 新增记录
   * @param metadataProperties
   * @return R
   */
  @SysLog("新增或者修改/属性")
  @PostMapping
  @PreAuthorize("@pms.hasPermission('MetadataManage_metadataproperties_add')")
  public R saveOrUpdateProperties(@RequestBody List<MetadataProperties> metadataProperties){
    return new R(metadataPropertiesService.saveOrUpdateProperties(metadataProperties));
  }

  /**
   * 修改记录
   * @param metadataProperties
   * @return R
   */
  @SysLog("修改属性")
  @PutMapping
  @PreAuthorize("@pms.hasPermission('MetadataManage_metadataproperties_edit')")
  public R update(@RequestBody MetadataProperties metadataProperties){
    return new R<>(metadataPropertiesService.updateById(metadataProperties));
  }

  /**
   * 通过id删除一条记录
   * @param id
   * @return R
   */
  @SysLog("删除属性")
  @DeleteMapping
  @PreAuthorize("@pms.hasPermission('MetadataManage_metadataproperties_del')")
  public R removeByIds(@RequestBody List<Integer> ids){
    return new R<>(metadataPropertiesService.removeByIds(ids));
  }

  /**
   * 简单带常量分页查询
   * @param page 分页对象
   * @param metadataProperties 属性
   * @return
   */
  @GetMapping("/itemsPage")
  public R<IPage<MetadataPropertiesDto>> getMetadataPropertiesAndItemsPage(Page<MetadataProperties> page, MetadataProperties metadataProperties) {
    return  new R<>(metadataPropertiesService.getMetadataPropertiesAndItemsPage(page,metadataProperties));
  }
}

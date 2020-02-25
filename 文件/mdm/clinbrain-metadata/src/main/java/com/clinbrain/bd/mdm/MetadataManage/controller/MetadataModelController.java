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
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataModel;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataModelService;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.common.log.annotation.SysLog;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


/**
 * 模型
 *
 * @author wangyi
 * @date 2019-05-28 09:56:48
 */
@RestController
@AllArgsConstructor
@RequestMapping("/metadatamodel")
public class MetadataModelController {
  //private UuidGenerator uuidGenerator;
  private final  MetadataModelService metadataModelService;

  /**
   * 简单分页查询
   * @param page 分页对象
   * @param metadataModel 模型
   * @return
   */
  @GetMapping("/page")
  public R<IPage<MetadataModel>> getMetadataModelPage(Page<MetadataModel> page, MetadataModel metadataModel) {
    return  new R<>(metadataModelService.getMetadataModelPage(page,metadataModel));
  }
  /**
   * 返回树形菜单集合
   *
   * @return 树形菜单
   */
  @GetMapping(value = "/tree")
  public R listModelTrees(@RequestParam(value="filter", required=false) String filter ) {
    return new R<>(metadataModelService.listModelTrees(filter));
  }


  /**
   * 通过id查询单条记录
   * @param id
   * @return R
   */
  @GetMapping("/{id}")
  public R<MetadataModel> getById(@PathVariable("id") Integer id){
    return new R<>(metadataModelService.getById(id));
  }

  /**
   * 新增记录
   * @param metadataModel
   * @return R
   */
  @SysLog("新增模型")
  @PostMapping
  @PreAuthorize("@pms.hasPermission('MetadataManage_metadatamodel_add')")
  public R save(@RequestBody MetadataModel metadataModel) throws Exception{
    //新增之前先将模型信息添加到ID生成服务
    metadataModel.setResourceId(UUID.randomUUID().toString());
    if(metadataModel.getParentResourceId()==null) metadataModel.setParentResourceId("");
    return new R<>(metadataModelService.save(metadataModel));
  }

  /**
   * 获取元模型
   * @return R
   */
  @SysLog("获取元模型")
  @GetMapping("/getModelInfo")
  public R<IPage<MetadataModel>> getMetadataModelInfoPage(Page<MetadataModel> page, MetadataModel metadataModel) {
    return  new R<>(metadataModelService.getMetadataModelInfoPage(page,metadataModel));
  }

  /**
   * 修改记录
   * @param metadataModel
   * @return R
   */
  @SysLog("修改模型")
  @PutMapping
  @PreAuthorize("@pms.hasPermission('MetadataManage_metadatamodel_edit')")
  public R update(@RequestBody MetadataModel metadataModel){
    return new R<>(metadataModelService.updateById(metadataModel));
  }

  /**
   * 通过id删除一条记录
   * @param id
   * @return R
   */
  @SysLog("删除模型")
  @DeleteMapping("/{id}")
  @PreAuthorize("@pms.hasPermission('MetadataManage_metadatamodel_del')")
  public R removeById(@PathVariable Integer id){
    return new R<>(metadataModelService.removeById(id));
  }

}

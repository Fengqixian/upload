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
import com.clinbrain.bd.mdm.MetadataManage.dto.HeaderRelation;
import com.clinbrain.bd.mdm.MetadataManage.dto.ModelResourceTree;
import com.clinbrain.bd.mdm.MetadataManage.dto.ModelTree;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.bd.mdm.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;
import java.util.Map;

/**
 * 元数据
 *
 * @author wangyi
 * @date 2019-05-29 14:41:40
 */
public interface MetadataValueService extends IService<MetadataValue> {

  /**
   * 元数据简单分页查询
   * @param metadataValue 元数据
   * @return
   */
  IPage<MetadataValue> getMetadataValuePage(Page<MetadataValue> page, MetadataValue metadataValue);


  List<ModelResourceTree> listModelResourceTree();
  List<ModelResourceTree> listRoleModelResourceTree(Integer roleId);
  List<ModelResourceTree> listModelResourceTreeByModelId(String modelId);

  /**
   * 根据UUid获取子节点
   *
   * @param parentUuid
   * @return
   */
  List<ModelResourceTree> listModelParentResourceTree(String parentUuid);

  List<ModelResourceTree> listModelResourceLazyTree(String parentUuid, String queryString);

  /**
   * 根据英文名忽略大小写查找根模型元数据
   * @param metadataValue
   * @return
   * @author yjt
   * @date  2019/7/9 9:24
   */
  List<MetadataValue> getMetaDataListByName(MetadataValue metadataValue);

  List selectMetaValueByDynSql(String sql, MetadataValue metadataValue);

  List selectDistinctMetadata(MetadataValue metadataValue);

  MetadataValue selectDbByCloumn(MetadataValue metadataValue);//获取column节点的父的父（库名）

  boolean createHeader(MetadataValue metadataValue,String dbResourceId);

  boolean createHeaderRelation(List<String> resourceIds,List<HeaderRelation> list);

  R autoAdd();

  List<MetadataValue> getMetadataValueByIds(String inColumnString);

  R createMetaDataByElementItem(List<MetadataValue> list, String tbName, String dbResourceId);//根据元素项产生技术元数据
  List<MetadataValue> selectNotInElement(String columnModelId,String tbResouceId,String elementModelId,String dbResouceId);
  R autoCreateElementItem(String dbResourceId);
  R getElementByResouceId(String resourceId);
  List<Map> getConnection(String resourceId);


  /**
   * 获取权限分页
   * @param page 分页信息
   * @param roleId 权限标识
   * @return
   */
  List<ModelResourceTree> listRoleModelResourcePage(Integer roleId, List<String> resourceIds);
}

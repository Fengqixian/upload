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
package com.clinbrain.bd.mdm.MetadataManage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.mdm.MetadataManage.dto.ModelTree;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataModel;
import com.clinbrain.bd.mdm.MetadataManage.mapper.MetadataModelMapper;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataModelService;
import com.clinbrain.bd.mdm.admin.api.dto.TreeNode;
import com.clinbrain.bd.mdm.admin.api.vo.TreeUtil;
import com.clinbrain.bd.mdm.common.security.util.SecurityUtils;
import com.google.common.base.Joiner;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 模型
 *
 * @author wangyi
 * @date 2019-05-28 09:56:48
 */
@Service("metadataModelService")
public class MetadataModelServiceImpl extends ServiceImpl<MetadataModelMapper, MetadataModel> implements MetadataModelService {

  /**
   * 模型简单分页查询
   * @param metadataModel 模型
   * @return
   */
  @Override
  public IPage<MetadataModel> getMetadataModelPage(Page<MetadataModel> page, MetadataModel metadataModel){
      List<Integer> listRole = new ArrayList<>();
      if(!SecurityUtils.getUser().getUsername().equals("admin")){
          listRole = SecurityUtils.getRoles();
          if(listRole.size()==0){
              return null;
          }
          return baseMapper.getMetadataModelPage(page,metadataModel,listRole);
      }
      return baseMapper.getMetadataModelPage(page,metadataModel,listRole);
  }
    /**
     * 模型简单分页查询
     * @param metadataModel 模型
     * @return
     */
    @Override
    public IPage<MetadataModel> getMetadataModelInfoPage(Page<MetadataModel> page, MetadataModel metadataModel){
        List<Integer> listRole = new ArrayList<>();
        if(!SecurityUtils.getUser().getUsername().equals("admin")){
            listRole = SecurityUtils.getRoles();
            if(listRole.size()==0){
                return null;
            }
            return baseMapper.getMetadataModelInfoPage(page,metadataModel,listRole);
        }
        return baseMapper.getMetadataModelInfoPage(page,metadataModel,listRole);
    }
    @Override
    public List<ModelTree> listModelTrees(String filter) {
        List<Integer> listRole = new ArrayList<>();
        if(!SecurityUtils.getUser().getUsername().equals("admin")){
            listRole = SecurityUtils.getRoles();
            if(listRole.size()==0){
                return null;
            }
            return buildByLoop(this.baseMapper.listModelByRoleIds(listRole,filter));
        }
        return buildByLoop(this.baseMapper.listModelByRoleIds(listRole,filter));
    }

    private List<ModelTree> buildByLoop(List<ModelTree> treeNodes) {
        List<ModelTree> trees = new ArrayList<>();
        for (ModelTree treeNode : treeNodes) {
            if (StringUtils.isBlank(treeNode.getParentUuid())) {
                trees.add(treeNode);
            }
            for (ModelTree it : treeNodes) {
                if (StringUtils.equals(it.getParentUuid() , treeNode.getUuid())) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(new ArrayList<>());
                    }
                    treeNode.add(it);
                }
            }
        }
        return trees;
    }
}

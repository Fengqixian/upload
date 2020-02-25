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
import com.clinbrain.bd.mdm.MetadataManage.dto.ModelTree;
import com.clinbrain.bd.mdm.MetadataManage.dto.RolePermissionDto;
import com.clinbrain.bd.mdm.MetadataManage.dto.RoleResourcePermissionDto;
import com.clinbrain.bd.mdm.MetadataManage.entity.ModelResourceTreeEntity;
import com.clinbrain.bd.mdm.MetadataManage.entity.RoleResource;
import com.clinbrain.bd.mdm.MetadataManage.entity.RoleResourcePermission;
import com.clinbrain.bd.mdm.common.core.util.R;

import java.util.List;

/**
 * 资源权限
 *
 * @author wangyi
 * @date 2019-06-04 15:48:56
 */
public interface RoleResourcePermissionService extends IService<RoleResourcePermission> {

    /**
     * 资源权限简单分页查询
     *
     * @param roleResourcePermission 资源权限1
     * @return
     */
    IPage<RoleResourcePermission> getRoleResourcePermissionPage(Page<RoleResourcePermission> page, RoleResourcePermission roleResourcePermission);

    List<RoleResourcePermission> getRoleResourcePermissionList(RoleResourcePermissionDto roleResourcePermission, List<String> parentResourceIds);


    R updateByRoleId(String roleId, List<ModelResourceTreeEntity> roleResourcePermission, List<String> resourceIds);

    List<RoleResourcePermission> listRoleResourcePermissionByRoleIds();

    String getRescoucePermissionParamValue(Integer userId);


    /**
     * 添加权限 0 技术 1:业务 2：项目
     *
     * @param permissionDto 角色条件对象
     * @return
     */
    R savePermission(RolePermissionDto permissionDto);


    /**
     * 创建更新权限
     * @param resources 权限集合
     * @return
     */
    R saveOrUpdateRole(RoleResourcePermissionDto resources);

    /**
     * 取消权限
     * @param resources
     * @return
     */
    R cancelRole(RoleResourcePermissionDto resources);

    /**
     * 根据角色id查询权限
     * @param roleId 角色id
     * @return
     */
    R selectRoleResourceList(Integer roleId);
}

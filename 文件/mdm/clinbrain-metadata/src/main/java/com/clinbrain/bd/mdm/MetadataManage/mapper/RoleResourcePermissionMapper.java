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
import com.clinbrain.bd.mdm.MetadataManage.dto.RoleResourcePermissionDto;
import com.clinbrain.bd.mdm.MetadataManage.entity.RoleResourcePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 资源权限
 *
 * @author wangyi
 * @date 2019-06-04 15:48:56
 */
public interface RoleResourcePermissionMapper extends BaseMapper<RoleResourcePermission> {
    /**
     * 资源权限简单分页查询
     *
     * @param roleResourcePermission 资源权限
     * @return
     */
    IPage<RoleResourcePermission> getRoleResourcePermissionPage(Page page, @Param("roleResourcePermission") RoleResourcePermission roleResourcePermission, @Param("roleIds") List roleIds);

    List<RoleResourcePermission> getRoleResourcePermissionList(@Param("roleResourcePermission") RoleResourcePermissionDto roleResourcePermission, @Param("parentResourceIds") List parentResourceIds);

    List<RoleResourcePermission> getRoleResourcePermissionByRoleIds(@Param("roleIds") List roleIds);

    IPage<RoleResourcePermission> getResourcePermissionTableByUserId(Page page, @Param("userId") Integer userId);

    IPage<RoleResourcePermission> getResourcePermissionColumnByUserId(Page page, @Param("userId") Integer userId, @Param("tableId") String tableId);

    List<Map<String, Object>> getMetadataValueInfo(@Param("resourceId") String resourceId);
}

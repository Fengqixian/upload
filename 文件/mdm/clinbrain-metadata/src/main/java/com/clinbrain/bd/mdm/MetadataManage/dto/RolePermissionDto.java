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
package com.clinbrain.bd.mdm.MetadataManage.dto;


import com.clinbrain.bd.mdm.MetadataManage.entity.RoleResourcePermission;
import lombok.Data;

import java.util.List;

/**
 * 资源权限接收对象
 *
 * @author lianglele
 * @date 2019-06-04 15:48:56
 */
@Data
public class RolePermissionDto {
    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    private String roleId;
    /**
     * 编辑类型 0：部分 1：全选 2：不选 3 父节点全选子节点部分选中 4：父节点部分 子节点全选 5：父节点全选 子节点不选
     */
    private Integer type;
    /**
     * 视图类型 0：技术 1：业务 2：项目
     */
    private Integer projectType;

    /**
     * 模型的id
     */
    private String modelResourceId;

    /**
     * 父亲节点
     */
    private List<RoleResourcePermission> permissionList;
    /**
     * 儿子节点
     */
    private List<RoleResourcePermission> subPermissionList;


}

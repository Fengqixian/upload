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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.businessView.service.MetaDataElementService;
import com.clinbrain.bd.mdm.MetadataManage.businessView.service.MetaElementColumnRefService;
import com.clinbrain.bd.mdm.MetadataManage.dto.ModelResourceTree;
import com.clinbrain.bd.mdm.MetadataManage.dto.RolePermissionDto;
import com.clinbrain.bd.mdm.MetadataManage.dto.RoleResourcePermissionDto;
import com.clinbrain.bd.mdm.MetadataManage.entity.ModelResourceTreeEntity;
import com.clinbrain.bd.mdm.MetadataManage.entity.RoleResource;
import com.clinbrain.bd.mdm.MetadataManage.entity.RoleResourcePermission;
import com.clinbrain.bd.mdm.MetadataManage.service.RoleResourcePermissionService;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.service.TechnologyViewService;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.common.log.annotation.SysLog;
import com.clinbrain.bd.mdm.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 资源权限
 *
 * @author wangyi
 * @date 2019-06-04 15:48:56
 */
@RestController
@AllArgsConstructor
@RequestMapping("/roleresourcepermission")
public class RoleResourcePermissionController {

    private final RoleResourcePermissionService roleResourcePermissionService;

    /**
     * 简单分页查询
     *
     * @param page                   分页对象
     * @param roleResourcePermission 资源权限
     * @return
     */
    @GetMapping("/page")
    public R<IPage<RoleResourcePermission>> getRoleResourcePermissionPage(Page<RoleResourcePermission> page, RoleResourcePermission roleResourcePermission) {
        return new R<>(roleResourcePermissionService.getRoleResourcePermissionPage(page, roleResourcePermission));
    }

    /**
     * 查询角色对应权限
     *
     * @param roleId 角色id
     * @return
     */
    @GetMapping("/list")
    public R<List<RoleResourcePermission>> getRoleResourcePermissionList(Integer roleId) {
//        return new R<>(roleResourcePermissionService.getRoleResourcePermissionList(roleResourcePermission, roleResourcePermission.getParentResourceIds()));
        return roleResourcePermissionService.selectRoleResourceList(roleId);
    }


    /**
     * 通过id查询单条记录
     *
     * @param id
     * @return R
     */
    @GetMapping("/{id}")
    public R<RoleResourcePermission> getById(@PathVariable("id") Integer id) {
        return new R<>(roleResourcePermissionService.getById(id));
    }

    /**
     * 新增记录
     *
     * @param roleResourcePermission
     * @return R
     */
    @SysLog("新增资源权限")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('MetadataManage_roleresourcepermission_add')")
    public R save(@RequestBody RoleResourcePermission roleResourcePermission) {
        if (!SecurityUtils.getUser().getUsername().equals("admin")) { // 超级管理员
            List<RoleResourcePermission> roleResourcePermissionList =
                    roleResourcePermissionService.listRoleResourcePermissionByRoleIds();//当前用户 所有的资源
            for (RoleResourcePermission rp : roleResourcePermissionList) {
                boolean isAdd = rp.getPermissionCreate();
                if (!isAdd) {
                    return new R<>().setMsg("没有新增权限");
                }
            }
        }
        return new R<>(roleResourcePermissionService.save(roleResourcePermission));
    }

    /**
     * 修改记录
     *
     * @param permissionDto
     * @return R
     */
    @SysLog("修改资源权限")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('MetadataManage_roleresourcepermission_edit')")
    @Transactional
    public R update(@RequestBody RolePermissionDto permissionDto) {
//        if (!SecurityUtils.getUser().getUsername().equals("admin")) {
//            List<RoleResourcePermission> roleResourcePermissionList = roleResourcePermissionService.listRoleResourcePermissionByRoleIds();
//            for (RoleResourcePermission rp : roleResourcePermissionList) {
//                boolean isUpdate = rp.getPermissionUpdate();
//                if (!isUpdate) {
//                    return new R<>().setMsg("没有修改权限");
//                }
//            }
//        }
//        return new R<>(roleResourcePermissionService.updateByRoleId(roleId, roleResourcePermission, resourceIds));
        //        先删除
//        List<String> pres = roleResourcePermissionService.list(Wrappers.<RoleResourcePermission>query().lambda().eq(RoleResourcePermission::getRoleId, roleId).eq(RoleResourcePermission::getModelResourceId, modelResourceId)).stream().map(r -> r.getResourceId()).collect(Collectors.toList());
//        if (pres != null && pres.size() > 0) {
//            roleResourcePermissionService.remove(Wrappers.<RoleResourcePermission>query().lambda().eq(RoleResourcePermission::getRoleId, roleId).in(RoleResourcePermission::getParentResourceId, pres));
//        }
//        roleResourcePermissionService.remove(Wrappers.<RoleResourcePermission>query().lambda().eq(RoleResourcePermission::getRoleId, roleId).eq(RoleResourcePermission::getModelResourceId, modelResourceId));

//        if (type == 0 && roleResourcePermission != null) {
//            return new R<>(roleResourcePermissionService.saveBatch(roleResourcePermission));
//
//        } else if (type == 1) {
        return roleResourcePermissionService.savePermission(permissionDto);
//        }
    }

    /**
     * 修改记录
     *
     * @return R
     */
    @SysLog("修改资源权限")
    @PutMapping("/saveOrUpdateRole")
    @PreAuthorize("@pms.hasPermission('MetadataManage_roleresourcepermission_edit')")
    public R saveOrUpdateRole(@RequestBody RoleResourcePermissionDto resources) {
        return roleResourcePermissionService.saveOrUpdateRole(resources);
    }

    /**
     * 取消权限记录
     *
     * @return R
     */
    @SysLog("取消资源权限")
    @PutMapping("/cancel")
    public R cancelRole(@RequestBody RoleResourcePermissionDto resources) {
        return roleResourcePermissionService.cancelRole(resources);
    }

    /**
     * 通过id删除一条记录
     *
     * @param id
     * @return R
     */
    @SysLog("删除资源权限")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('MetadataManage_roleresourcepermission_del')")
    public R removeById(@PathVariable Integer id) {
        if (!SecurityUtils.getUser().getUsername().equals("admin")) {
            List<RoleResourcePermission> roleResourcePermissionList = roleResourcePermissionService.listRoleResourcePermissionByRoleIds();
            for (RoleResourcePermission roleResourcePermission : roleResourcePermissionList) {
                boolean isDel = roleResourcePermission.getPermissionDelete();
                if (!isDel) {
                    return new R<>().setMsg("没有删除权限");
                }
            }
        }
        return new R<>(roleResourcePermissionService.removeById(id));

    }


    /**
     * 新增记录
     *
     * @param permissionDto 权限对象
     * @return R
     */
    @SysLog("新增资源权限")
    @PostMapping("/saveList")
    @PreAuthorize("@pms.hasPermission('MetadataManage_roleresourcepermission_add')")
    public R saveList(@RequestBody RolePermissionDto permissionDto) {
//    if(!SecurityUtils.getUser().getUsername().equals("admin")){ // 超级管理员
//      List<RoleResourcePermission> roleResourcePermissionList =
//              roleResourcePermissionService.listRoleResourcePermissionByRoleIds();//当前用户 所有的资源
//      for(RoleResourcePermission rp:roleResourcePermissionList){
//        boolean isAdd = rp.getPermissionCreate();
//        if(!isAdd){
//          return new R<>().setMsg("没有新增权限");
//        }
//      }
//    }
//        if (type == 0 && roleResourcePermission != null && roleResourcePermission.size() > 0) {
//            return new R<>(roleResourcePermissionService.saveBatch(roleResourcePermission));
//        } else if (type == 1 && roleResourcePermission != null && roleResourcePermission.size() > 0) {
        return roleResourcePermissionService.savePermission(permissionDto);
//        }
//        return new R();
    }


}

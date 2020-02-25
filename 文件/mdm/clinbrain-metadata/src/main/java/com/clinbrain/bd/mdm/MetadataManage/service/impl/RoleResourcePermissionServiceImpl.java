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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.DataElement;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.ElementColumnRef;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.MetadataElement;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.MetadataSetCategory;
import com.clinbrain.bd.mdm.MetadataManage.businessView.mapper.DataElementMapper;
import com.clinbrain.bd.mdm.MetadataManage.businessView.service.MetaDataElementService;
import com.clinbrain.bd.mdm.MetadataManage.businessView.service.MetaElementColumnRefService;
import com.clinbrain.bd.mdm.MetadataManage.businessView.service.MetadataCategoryService;
import com.clinbrain.bd.mdm.MetadataManage.dto.ModelTree;
import com.clinbrain.bd.mdm.MetadataManage.dto.RolePermissionDto;
import com.clinbrain.bd.mdm.MetadataManage.dto.RoleResourcePermissionDto;
import com.clinbrain.bd.mdm.MetadataManage.entity.ModelResourceTreeEntity;
import com.clinbrain.bd.mdm.MetadataManage.entity.RoleResource;
import com.clinbrain.bd.mdm.MetadataManage.entity.RoleResourcePermission;
import com.clinbrain.bd.mdm.MetadataManage.mapper.RoleResourceMapper;
import com.clinbrain.bd.mdm.MetadataManage.mapper.RoleResourcePermissionMapper;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectDataSetCategory;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectElementColumnRef;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectViewTree;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.mapper.ProjectDataMapper;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.service.ProjectDataService;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.service.ProjectViewService;
import com.clinbrain.bd.mdm.MetadataManage.service.RoleResourcePermissionService;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Column;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Table;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.mapper.TechnologyColumnMapper;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.mapper.TechnologyDatabaseMapper;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.mapper.TechnologyTableMapper;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.mapper.TechnologyViewTreeMapper;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.service.TechnologyViewService;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.common.security.util.SecurityUtils;
import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

/**
 * 资源权限
 *
 * @author wangyi
 * @date 2019-06-04 15:48:56
 */
@Service("roleResourcePermissionService")
@Transactional
@AllArgsConstructor
@Slf4j
public class RoleResourcePermissionServiceImpl extends ServiceImpl<RoleResourcePermissionMapper, RoleResourcePermission> implements RoleResourcePermissionService {

    //技术
    private final TechnologyColumnMapper technologyColumnMapper;
    private final TechnologyTableMapper technologyTableMapper;
    //业务
    private final MetadataCategoryService metadataCategoryService;
    private final MetaElementColumnRefService metaElementColumnRefService;
    private final MetaDataElementService metaDataElementService;

    //项目
    private final ProjectViewService projectViewService;
    private final ProjectDataMapper projectDataMapper;

    /**
     * 权限表
     */
    private final RoleResourceMapper roleResourceMapper;

    /**
     * 资源权限简单分页查询
     *
     * @param roleResourcePermission 资源权限
     * @return
     */
    @Override
    public IPage<RoleResourcePermission> getRoleResourcePermissionPage(Page<RoleResourcePermission> page, RoleResourcePermission roleResourcePermission) {
        List<Integer> listRole = new ArrayList<>();
        if (!SecurityUtils.getUser().getUsername().equals("admin")) {
            listRole = SecurityUtils.getRoles();
            if (listRole.size() == 0) {
                return null;
            } else {
                return baseMapper.getRoleResourcePermissionPage(page, roleResourcePermission, listRole);
            }
        }
        return baseMapper.getRoleResourcePermissionPage(page, roleResourcePermission, listRole);
    }

    /**
     * 资源权限简单分页查询
     *
     * @param roleResourcePermission 资源权限
     * @return
     */
    @Override
    public List<RoleResourcePermission> getRoleResourcePermissionList(RoleResourcePermissionDto roleResourcePermission, List<String> parentResourceIds) {

        return baseMapper.getRoleResourcePermissionList(roleResourcePermission, parentResourceIds);
    }

    @Override
    @Transactional
    public R updateByRoleId(String roleId, List<ModelResourceTreeEntity> roleResourcePermission, List<String> resourceIds) {
        /*在更新之前根据roleId 全部删除*/
        RoleResourcePermission permission = new RoleResourcePermission();
        permission.setRoleId(roleId);

        //根据两个条件删除对象
        if (resourceIds != null && resourceIds.size() > 0) {
            for (String resId : resourceIds) {
                permission.setResourceId(resId);
                Wrapper<RoleResourcePermission> wrapper = Wrappers.update(permission);
                baseMapper.delete(wrapper);
            }
        }


        int limitSize = 500;
        List<ModelResourceTreeEntity> tempRoleResourcePermission = new ArrayList<>();

        if (roleResourcePermission.size() <= limitSize) {
            return updateByRoleIdBatch(roleId, roleResourcePermission);
        } else {
            for (int i = 0; i < roleResourcePermission.size(); i++) {
                tempRoleResourcePermission.add(roleResourcePermission.get(i));
                if (tempRoleResourcePermission.size() == limitSize || i == roleResourcePermission.size() - 1) {
                    updateByRoleIdBatch(roleId, tempRoleResourcePermission);
                    tempRoleResourcePermission.clear();
                }
            }
        }
        return new R();
    }

    @Override
    public List<RoleResourcePermission> listRoleResourcePermissionByRoleIds() {
        List<Integer> listRole = new ArrayList<>();
        if (!SecurityUtils.getUser().getUsername().equals("admin")) {
            listRole = SecurityUtils.getRoles();
            if (listRole.size() == 0) {
                return null;
            } else {
                return baseMapper.getRoleResourcePermissionByRoleIds(listRole);
            }
        }
        return baseMapper.getRoleResourcePermissionByRoleIds(listRole);
    }

    @Override
    public String getRescoucePermissionParamValue(Integer userId) {
        Page<RoleResourcePermission> page = new Page<>();
        //获取该用户所有表权限
        IPage<RoleResourcePermission> permissionTableList = baseMapper.getResourcePermissionTableByUserId(page, userId);
        if (permissionTableList == null || permissionTableList.getRecords().size() < 1) {
            return "";
        }
        Map<String, Object> resultMap = new HashMap<>();
        for (RoleResourcePermission p : permissionTableList.getRecords()) {
            String resourceId = p.getResourceId();

            //根据表的resourceId获取表详情
            List<Map<String, Object>> valueInfo = baseMapper.getMetadataValueInfo(resourceId);
            if (valueInfo == null || valueInfo.size() < 1) {
                continue;
            }
            //获取表名称
            String tableName = valueInfo.get(0).get("name_en").toString();

            //获取表所属的所有列权限
            IPage<RoleResourcePermission> permissionColumnList = baseMapper.getResourcePermissionColumnByUserId(page, userId, resourceId);
            if (permissionColumnList == null || permissionColumnList.getRecords().size() < 1) {
                continue;
            }

            //循环某个表的所有权限
            String customConditions = "";
            //自定义条件
            if (customConditions.equalsIgnoreCase("")) {
                customConditions = p.getCustomConditions();
            }

            List<String> andfilters = new ArrayList<>();
            List<String> orfilters = new ArrayList<>();
            List<String> cancelMaskList = new ArrayList<>();
            //每个表的权限所有列
            for (RoleResourcePermission per : permissionColumnList.getRecords()) {
                if (!per.getPermissionType().equalsIgnoreCase("INS")) {
                    continue;
                }
                //只查询配置的具体某些数据的访问权限（表所属所有字段的访问权限
                String rangeId = per.getRangeId();
                String rangeValues = per.getRangeValues();

                valueInfo = baseMapper.getMetadataValueInfo(per.getResourceId());
                if (valueInfo == null || valueInfo.size() < 1) {
                    continue;
                }

                //英文名
                String modelName = valueInfo.get(0).get("name_en").toString();
                String[] rangeValueList = rangeValues.split(",");
                if (rangeValueList == null || rangeValueList.length < 1) {
                    continue;
                }
                String ranges = "";
                if (rangeValues != null && !rangeValues.equalsIgnoreCase("")) {
                    for (String rangeValue : rangeValueList) {
                        if (ranges.equalsIgnoreCase("")) {
                            ranges = "'" + rangeValue + "'";
                        } else {
                            ranges += ",'" + rangeValue + "'";
                        }
                    }
                    orfilters.add(modelName + " in (" + ranges + ")");
                }

                if (per.getCancelDataMasking() != null && per.getCancelDataMasking() == true) {
                    cancelMaskList.add(modelName);
                }
            }

            //解析自定义条件，获取所有的and条件和or条件字段
            if (!customConditions.equalsIgnoreCase("")) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) JSONArray.parse(customConditions);
                for (Map<String, Object> con : list) {
                    String condition = con.get("condition").toString();
                    String conModelName = con.get("modelName").toString();
                    String conValue = con.get("value").toString();
                    if (con.get("connection").toString().equalsIgnoreCase("and")) {
                        andfilters.add(conModelName + condition + conValue);
                    } else if (con.get("connection").toString().equalsIgnoreCase("or")) {
                        orfilters.add(conModelName + condition + conValue);
                    }
                }
            }

            Map<String, Object> filters = new HashMap<>();
            filters.put("andfilters", andfilters);
            filters.put("orfilters", orfilters);
            filters.put("cancelMasking", cancelMaskList);
            resultMap.put(tableName, filters);
        }
        String result = JSON.toJSONString(resultMap);
        return JSON.toJSONString(resultMap);
    }

    private R updateByRoleIdBatch(String roleId, List<ModelResourceTreeEntity> roleResourcePermission) {
        /*转化为RoleResourcePermission 批量保存*/
        LocalDateTime time = LocalDateTime.now();
        List<RoleResourcePermission> resourcePermissions = roleResourcePermission.stream().map(per -> {
            RoleResourcePermission resourcePermission = new RoleResourcePermission();
            resourcePermission.setModelResourceId(per.getModelId());
            resourcePermission.setParentResourceId(per.getParentResourceId());
            resourcePermission.setResourceId(per.getResourceId());
            resourcePermission.setRoleId(roleId);
            resourcePermission.setPermissionType(per.getType());
            resourcePermission.setRangeId(per.getRangeId());
            resourcePermission.setRangeValues(per.getRangeValues());
            if (per.getCancelDataMasking() != null) {
                resourcePermission.setCancelDataMasking(per.getCancelDataMasking().equalsIgnoreCase("1") ? true : false);
            }
            if (per.getCustomConditions() != null) {
                resourcePermission.setCustomConditions(per.getCustomConditions());
            }
            if (per.getPermissionCreate() != null) {
                resourcePermission.setPermissionCreate(per.getPermissionCreate().equalsIgnoreCase("1") ? true : false);
            }
            if (per.getPermissionDelete() != null) {
                resourcePermission.setPermissionDelete(per.getPermissionDelete().equalsIgnoreCase("1") ? true : false);
            }
            if (per.getPermissionUpdate() != null) {
                resourcePermission.setPermissionUpdate(per.getPermissionUpdate().equalsIgnoreCase("1") ? true : false);
            }
            resourcePermission.setCreateTime(time);
            resourcePermission.setUpdateTime(time);

            return resourcePermission;
        }).collect(Collectors.toList());
        saveBatch(resourcePermissions);
        return new R();
    }


    @Override
    @Transactional
    public R savePermission(RolePermissionDto permissionDto) {

        try {

            if (permissionDto.getProjectType() == 0) {
                this.saveTechnology(permissionDto);
            } else if (permissionDto.getProjectType() == 1) {
                this.saveElement(permissionDto);
            } else if (permissionDto.getProjectType() == 2) {
                this.saveProject(permissionDto);
            }
            return new R("添加成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new R("添加失败").setCode(1);
        }
    }


    /**
     * 创建字段权限
     *
     * @param permissionDto 角色对象
     * @return
     */
    private void saveTechnology(RolePermissionDto permissionDto) {

        //先根据roid和modelid删除原来的权限
        this.remove(Wrappers.<RoleResourcePermission>query().lambda().eq(RoleResourcePermission::getRoleId, permissionDto).eq(RoleResourcePermission::getModelResourceId, permissionDto.getModelResourceId()));
        technologyColumnMapper.delete(Wrappers.<Column>query().lambda().eq(Column::getTableId, technologyTableMapper.selectOne(Wrappers.<Table>query().lambda()
                .eq(Table::getResourceId, permissionDto.getModelResourceId()))));

        List<RoleResourcePermission> resourcePermissions = permissionDto.getPermissionList();
        if (permissionDto.getType() == 1) {
            resourcePermissions = technologyColumnMapper.selectList(Wrappers.<Column>query().lambda().eq(Column::getTableId, technologyTableMapper.selectOne(Wrappers.<Table>query().lambda()
                    .eq(Table::getResourceId, permissionDto.getModelResourceId())))).stream().map(c -> {
                RoleResourcePermission permission = new RoleResourcePermission();
                permission.setRoleId(permissionDto.getRoleId());
                permission.setModelResourceId(c.getResourceId());
                permission.setResourceId(c.getResourceId());
                return permission;
            }).collect(Collectors.toList());
        }
        //创建
        if (resourcePermissions != null && resourcePermissions.size() > 0) {
            this.saveBatch(resourcePermissions);
        }

    }

    /**
     * 创建业务权限
     *
     * @param permissionDto 角色id
     * @return
     */
    private void saveElement(RolePermissionDto permissionDto) {
        MetadataSetCategory category = metadataCategoryService.getOne(Wrappers.<MetadataSetCategory>query().lambda().eq(MetadataSetCategory::getResourceId, permissionDto.getModelResourceId()));
        Page page = new Page();
        page.setSize(-1);
        if ("1".equals(category.getCategory())) {
            category.setCategory("category");
        } else if ("2".equals(category.getCategory())) {
            category.setCategory("dataset");
        }
        List<RoleResourcePermission> resourcePermissions = null;
        //先根据roid和modelid删除原来的权限
        this.deleteRole(permissionDto.getRoleId(), permissionDto.getModelResourceId());

        List<DataElement> elementIds = new ArrayList<>();

        if (permissionDto.getType() == 1 || permissionDto.getType() == 3) {
            IPage<MetadataElement> pageList = metadataCategoryService.getPageMetaDataElement(page, category, null);
            if (pageList != null && pageList.getRecords() != null && pageList.getRecords().size() > 0) {
                resourcePermissions = pageList.getRecords().stream().map(e -> {
                    RoleResourcePermission permission = new RoleResourcePermission();
                    permission.setRoleId(permissionDto.getRoleId());
                    permission.setModelResourceId(permissionDto.getModelResourceId());
                    permission.setResourceId(e.getResourceId());
                    return permission;
                }).collect(Collectors.toList());
            }

            if (permissionDto.getType() == 3) {
                this.saveBatch(permissionDto.getSubPermissionList());
            } else {
                elementIds = pageList.getRecords().stream().map(r -> {
                    DataElement dataElement = new DataElement();
                    dataElement.setId(r.getId());
                    dataElement.setResourceId(r.getResourceId());
                    return dataElement;
                }).collect(Collectors.toList());
            }
            //父节点部分选择子节点全选
        } else if (permissionDto.getType() == 4) {
            List<DataElement> elements = metaDataElementService.list(Wrappers.<DataElement>query().lambda().in(DataElement::getResourceId, permissionDto.getPermissionList().stream().map(p -> p.getResourceId()).collect(Collectors.toList())));
            elementIds = elements;
            resourcePermissions = permissionDto.getPermissionList();
            //子节点部分选择
        } else if (permissionDto.getType() == 0) {
            this.saveBatch(permissionDto.getSubPermissionList());
            resourcePermissions = permissionDto.getPermissionList();
        } else if (permissionDto.getType() == 6) {
            resourcePermissions = permissionDto.getPermissionList();
            List<DataElement> elements = metaDataElementService.list(Wrappers.<DataElement>query().lambda().in(DataElement::getResourceId, permissionDto.getPermissionList().stream().map(p -> p.getResourceId()).collect(Collectors.toList())));
            elementIds = elements;
        }


        if (elementIds != null && elementIds.size() > 0) {
            List<RoleResourcePermission> finalResourcePermissions = this.getColumns(permissionDto.getRoleId(), elementIds);
            if (finalResourcePermissions != null && finalResourcePermissions.size() > 0)
                this.saveBatch(finalResourcePermissions);
        }

        if (resourcePermissions != null && resourcePermissions.size() > 0) {
            this.saveBatch(resourcePermissions);
        }
    }


    /**
     * 创建项目权限
     *
     * @param permissionDto 角色对象
     * @return
     */
    private void saveProject(RolePermissionDto permissionDto) {
        List<ProjectDataSetCategory> categorys = projectViewService.selectProjectCategory(permissionDto.getModelResourceId());
        ProjectDataSetCategory category = new ProjectDataSetCategory();
        if (categorys != null && categorys.size() > 0) {
            category = categorys.get(0);
        }
        Page page = new Page();
        page.setSize(-1);
        if ("1".equals(category.getCategory())) {
            category.setCategory("category");
        } else if ("2".equals(category.getCategory())) {
            category.setCategory("dataset");
        }
        List<RoleResourcePermission> resourcePermissions = null;
        //先根据roid和modelid删除原来的权限
        this.deleteRole(permissionDto.getRoleId(), permissionDto.getModelResourceId());

        List<DataElement> elementIds = new ArrayList<>();

        ProjectViewTree tree = new ProjectViewTree();
        tree.setId(category.getId());
        if (permissionDto.getType() == 1 || permissionDto.getType() == 3) {
            R<IPage<DataElement>> pageList = projectViewService.getProjectDataElement(page, tree, null);
            if (pageList != null && pageList.getData().getRecords() != null && pageList.getData().getRecords().size() > 0) {
                resourcePermissions = pageList.getData().getRecords().stream().map(e -> {
                    RoleResourcePermission permission = new RoleResourcePermission();
                    permission.setRoleId(permissionDto.getRoleId());
                    permission.setModelResourceId(permissionDto.getModelResourceId());
                    permission.setResourceId(e.getResourceId());
                    return permission;
                }).collect(Collectors.toList());
//                resourcePermissions.addAll(this.getColumns(roleId, pageList.getData().getRecords()));
            }
            if (permissionDto.getType() == 3) {
                this.saveBatch(permissionDto.getSubPermissionList());
            } else {
                elementIds = pageList.getData().getRecords();
            }
            //父节点部分选择子节点全选
        } else if (permissionDto.getType() == 4) {
            List<DataElement> elements = metaDataElementService.list(Wrappers.<DataElement>query().lambda().in(DataElement::getResourceId, permissionDto.getPermissionList().stream().map(p -> p.getResourceId()).collect(Collectors.toList())));
            elementIds = elements;
            //子节点部分选择
        } else if (permissionDto.getType() == 0) {
            this.saveBatch(permissionDto.getSubPermissionList());
            resourcePermissions = permissionDto.getPermissionList();
        } else if (permissionDto.getType() == 6) {
            resourcePermissions = permissionDto.getPermissionList();
            List<DataElement> elements = metaDataElementService.list(Wrappers.<DataElement>query().lambda().in(DataElement::getResourceId, permissionDto.getPermissionList().stream().map(p -> p.getResourceId()).collect(Collectors.toList())));
            elementIds = elements;
        }

        List<RoleResourcePermission> subPermissionList = this.getColumns(permissionDto.getRoleId(), elementIds);
        if (subPermissionList != null && subPermissionList.size() > 0) {
            this.saveBatch(subPermissionList);
        }

        if (resourcePermissions != null && resourcePermissions.size() > 0) {
            this.saveBatch(resourcePermissions);
        }
    }


    private List<RoleResourcePermission> getColumns(String roleId, List<DataElement> elements) {
        List<RoleResourcePermission> resourcePermissions = new ArrayList<>();
        elements.stream().forEach(r -> {
            List<Integer> columnids = metaElementColumnRefService.list(Wrappers.<ElementColumnRef>query().lambda().eq(ElementColumnRef::getElementId, r.getId())).stream().map(c -> c.getColumnId()).collect(Collectors.toList());
            resourcePermissions.addAll(technologyColumnMapper.selectList(Wrappers.<Column>query().lambda().in(Column::getId, columnids)).stream().map(c -> {
                RoleResourcePermission permission = new RoleResourcePermission();
                permission.setRoleId(roleId);
                permission.setModelResourceId(technologyTableMapper.selectById(c.getTableId()).getResourceId());
                permission.setParentResourceId(r.getResourceId());
                permission.setResourceId(c.getResourceId());
                return permission;
            }).collect(Collectors.toList()));
        });
        return resourcePermissions;
    }


    /**
     * 删除相关权限
     *
     * @param roleId          删除对应权限id
     * @param modelResourceId 删除对应权限 模型id
     */
    private void deleteRole(String roleId, String modelResourceId) {
        //        先删除
        List<RoleResourcePermission> permissionList = this.list(Wrappers.<RoleResourcePermission>query().lambda().eq(RoleResourcePermission::getRoleId, roleId).eq(RoleResourcePermission::getModelResourceId, modelResourceId));

        if (permissionList != null && permissionList.size() > 0) {

            List<String> pres = permissionList.stream().map(r -> r.getResourceId()).collect(Collectors.toList());
            if (pres != null && pres.size() > 0) {
                this.remove(Wrappers.<RoleResourcePermission>query().lambda().eq(RoleResourcePermission::getRoleId, roleId).in(RoleResourcePermission::getParentResourceId, pres));
            }
            this.remove(Wrappers.<RoleResourcePermission>query().lambda().eq(RoleResourcePermission::getRoleId, roleId).eq(RoleResourcePermission::getModelResourceId, modelResourceId));

            for (RoleResourcePermission permission : permissionList) {
                this.deleteRole(roleId, permission.getModelResourceId());
            }
        }

    }


    /**
     * 创建权限
     *
     * @param resources 权限
     * @return
     */
    @Override
    @Transactional
    public R saveOrUpdateRole(RoleResourcePermissionDto resources) {
        if (resources != null && resources.getParentResourceIds() != null && resources.getParentResourceIds().size() > 0) {
            resources.getParentResourceIds().stream().forEach(r -> {
                roleResourceMapper.delete(Wrappers.<RoleResource>query().lambda().eq(RoleResource::getRoleId, resources.getRoleId()).eq(RoleResource::getResourceId, r));
                RoleResource roleResource = new RoleResource();
                roleResource.setRoleId(resources.getRoleId());
                roleResource.setRoleType(resources.getProjectType());
                roleResource.setResourceId(r);
                roleResourceMapper.insert(roleResource);
            });
        }
        return new R();
    }

    /**
     * 创建权限
     *
     * @param resources 权限
     * @return
     */
    @Override
    public R cancelRole(RoleResourcePermissionDto resources) {
        if (resources != null && resources.getParentResourceIds() != null && resources.getParentResourceIds().size() > 0) {
            resources.getParentResourceIds().stream().forEach(r -> {
                roleResourceMapper.delete(Wrappers.<RoleResource>query().lambda().eq(RoleResource::getRoleId, resources.getRoleId()).eq(RoleResource::getResourceId, r));
            });
        } else {
            roleResourceMapper.delete(Wrappers.<RoleResource>query().lambda().eq(RoleResource::getRoleId, resources.getRoleId()));
        }
        return new R();
    }


    @Override
    public R selectRoleResourceList(Integer roleId) {
        return new R(roleResourceMapper.selectList(Wrappers.<RoleResource>query().lambda().eq(RoleResource::getRoleId, roleId)));
    }

}

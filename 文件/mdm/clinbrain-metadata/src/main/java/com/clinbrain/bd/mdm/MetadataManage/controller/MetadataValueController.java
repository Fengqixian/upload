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
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.dto.HeaderRelation;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataPropertiesService;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataReferenceService;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataValueService;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.common.log.annotation.SysLog;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * 元数据
 *
 * @author wangyi
 * @date 2019-05-29 14:41:40
 */
@RestController
@AllArgsConstructor
@RequestMapping("/metadatavalue")
public class MetadataValueController {
    //@Autowired
    //private UuidGenerator uuidGenerator;
    private final MetadataValueService metadataValueService;

    private final MetadataReferenceService metadataReferenceService;
    private MetadataPropertiesService metadataPropertiesService;

    /**
     * 简单分页查询
     *
     * @param page          分页对象
     * @param metadataValue 元数据
     * @return
     */
    @GetMapping("/page")
    public R<IPage<MetadataValue>> getMetadataValuePage(Page<MetadataValue> page, MetadataValue metadataValue) {
        return new R<>(metadataValueService.getMetadataValuePage(page, metadataValue));
    }

    /**
     * 返回树形菜单集合
     * 所有数据的树形菜单集合
     *
     * @return 树形菜单
     */
    @GetMapping(value = "/tree")
    public R listModelResourceTree() {
        return new R<>(metadataValueService.listModelResourceTree());
    }

    /**
     * 根据父节点返回子节点
     * 懒加载树结构使用
     *
     * @return 树形菜单
     */
    @GetMapping(value = "/tree/lazyTree")
    public R listModelResourceLazyTree(@RequestParam(required = false) String parentUuid, @RequestParam(required = false) String queryString) {
        return new R<>(metadataValueService.listModelResourceLazyTree(parentUuid, queryString));
    }

    /**
     * 返回某一角色的树形菜单
     * 全部数据
     *
     * @return 树形菜单
     */
    @GetMapping(value = "/tree/roleTree/{roleId}")
    public R listModelResourceTree(@PathVariable("roleId") Integer roleId) {
        return new R<>(metadataValueService.listRoleModelResourceTree(roleId));
    }

    /**
     * 返回某一模型下的数据
     * 树形结构的数据
     *
     * @return 树形菜单
     */
    @GetMapping(value = "/tree/modelTree/{modelId}")
    public R listModelResourceTree(@PathVariable("modelId") String modelId) {
        return new R<>(metadataValueService.listModelResourceTreeByModelId(modelId));
    }


    /**
     * 通过id查询单条记录
     *
     * @param id
     * @return R
     */
    @GetMapping("/{id}")
    public R<MetadataValue> getById(@PathVariable("id") Integer id) {
        return new R<>(metadataValueService.getById(id));
    }

    @GetMapping("/getConnection")
    public List<Map> getConnection(@RequestParam("resourceId") String resourceId) {
        return metadataValueService.getConnection(resourceId);
    }

    /**
     * 新增记录
     *
     * @param metadataValue
     * @return R
     */
    @SysLog("新增元数据")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('MetadataManage_metadatavalue_add')")
    public R save(@RequestBody MetadataValue metadataValue) {
        metadataValue.setResourceId(UUID.randomUUID().toString());
        if (metadataValue.getParentId() == null) metadataValue.setParentId("");
        return new R<>(metadataReferenceService.saveMetaDataValueInfo(metadataValue));
    }

    /**
     * 新增记录
     *
     * @param metadataValues
     * @return R
     */
    @SysLog("批量新增元数据")
    @PutMapping("saveBath")
    @PreAuthorize("@pms.hasPermission('MetadataManage_metadatavalue_add')")
    public R saveBath(@RequestBody List<MetadataValue> metadataValues) {
        boolean result = true;
        for (MetadataValue metadataValue : metadataValues) {
            if (result) {
                metadataValue.setResourceId(UUID.randomUUID().toString());
                if (metadataValue.getParentId() == null) metadataValue.setParentId("");
                result = metadataReferenceService.saveMetaDataValueInfo(metadataValue);
            }
        }
        return new R<>(result);
    }

    /**
     * 修改记录
     *
     * @param metadataValue
     * @return R
     */
    @SysLog("修改元数据")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('MetadataManage_metadatavalue_edit')")
    public R update(@RequestBody MetadataValue metadataValue) {
        return new R<>(metadataValueService.updateById(metadataValue));
    }

    /**
     * 修改记录
     *
     * @param metadataValues
     * @return R
     */
    @SysLog("新增或修改元数据")
    @PutMapping("saveOrUpdateBatch")
    @PreAuthorize("@pms.hasPermission('MetadataManage_metadatavalue_edit')")
    public R saveOrUpdateBatch(@RequestBody List<MetadataValue> metadataValues) {
        return new R<>(metadataValueService.saveOrUpdateBatch(metadataValues));
    }

    /**
     * 通过id删除一条记录
     *
     * @param id
     * @return R
     */
    @SysLog("删除元数据")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('MetadataManage_metadatavalue_del')")
    public R removeById(@PathVariable Integer id) {
        return new R<>(metadataValueService.removeById(id));
    }


    /**
     * 懒加载树形结构
     * 停止使用
     *
     * @return 树形菜单
     */
    @Deprecated
    @GetMapping(value = "/listModelParentResourceTree")
    public R listModelParentResourceTree(String parentUuid) {
        return new R<>(metadataValueService.listModelParentResourceTree(parentUuid));
    }

    /**
     * 根据英文名查询根模型元数据
     *
     * @param metadataValue 要查询的元数据
     * @return R
     */
    @SysLog("查询元数据")
    @PutMapping("checkMetadata")
    @PreAuthorize("@pms.hasPermission('MetadataManage_metadatavalue_edit')")
    public R getMetaDataListByName(@RequestBody MetadataValue metadataValue) {
        return new R<>(metadataValueService.getMetaDataListByName(metadataValue));
    }

    /**
     * 审批元数据
     *
     * @param metadataValues 审核的元数据
     * @return R
     */
    @SysLog("审批元数据")
    @PutMapping("auditMetadata")
    @PreAuthorize("@pms.hasPermission('MetadataManage_metadatavalue_edit')")
    public R auditMetaData(@RequestBody List<MetadataValue> metadataValues) {
        if (1 == metadataValues.get(0).getStatus()) {
            //通过
            return new R<>(metadataValueService.updateBatchById(metadataValues));
        } else if (2 == metadataValues.get(0).getStatus()) {
            //拒绝
            List<Integer> ids = new ArrayList<>();
            for (MetadataValue v : metadataValues) {
                ids.add(v.getId());
            }
            return new R<>(metadataValueService.removeByIds(ids));
        } else {
            return new R<>(false);
        }
    }

    @SysLog("新增数据项header")
    @GetMapping("createHeader")
    public R createHeader(@RequestParam(required = true, value = "nameCn") String nameCn, @RequestParam(required = true, value = "dbResourceId") String dbResourceId) {
        MetadataValue metadataValue = new MetadataValue();
        metadataValue.setNameCn(nameCn);
        metadataValue.setModelType("element_item");
        metadataValue.setParentId(dbResourceId);
        if (metadataValueService.getOne(Wrappers.query(metadataValue)) != null) {
            return new R<>(false, "同一个库中元素项不能重复");
        }
        return new R<>(metadataValueService.createHeader(metadataValue, dbResourceId));
    }

    @SysLog("新增数据项与header关系")
    @PostMapping("createHeaderRelation")
    public R createHeaderRelation(@RequestBody Map<String, Object> params) {
        List<String> resourceIds = (List) params.get("resourceIds");
        List<HeaderRelation> list = BeanUtils.mapsToBeans((List) params.get("list"), HeaderRelation.class);
        if (list.size() == 0) {
            return new R<>(false);
        }
        return new R<>(metadataValueService.createHeaderRelation(resourceIds, list));
    }

    @SysLog("编辑元素项与header关系")
    @GetMapping("editHeaderRelation")
    public R editHeaderRelation(@RequestParam String resourceId) {
        return metadataValueService.getElementByResouceId(resourceId);
    }

    @SysLog("根据元素项生成技术元数据")
    @PostMapping("createMetaDataByElementItem")
    public R createMetaDataByElementItem(@RequestBody() Map<String, Object> params) {
        List<MetadataValue> list = BeanUtils.mapsToBeans((List) params.get("list"), MetadataValue.class);
        String tbName = (String) params.get("tbName");
        String dbResourceId = (String) params.get("dbResourceId");
        return metadataValueService.createMetaDataByElementItem(list, tbName, dbResourceId);
    }

    @SysLog("生成元素项")
    @GetMapping("createElementItem")
    public R createElementItem(@RequestParam(required = true, value = "dbResouceId") String dbResouceId) {
        metadataValueService.autoCreateElementItem(dbResouceId);
        return new R<>();
    }

    /**
     * 返回某一角色下实例的信息
     * 全部数据
     *
     * @return 树形菜单
     */
    @GetMapping(value = "/rolePage")
    public R listModelResourcePage(Integer roleId, String resourceIds) {
        List ids = Arrays.asList(resourceIds.replaceAll("\"","").split(","));
        return new R<>(metadataValueService.listRoleModelResourcePage(roleId, ids));
    }
}

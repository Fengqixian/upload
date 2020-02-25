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
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataReference;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataReferenceInfo;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataConnectService;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataReferenceService;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataValueService;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.common.log.annotation.SysLog;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 元数据
 *
 * @author yjt
 * @date 2019-05-29 14:41:40
 */
@RestController
@AllArgsConstructor
@RequestMapping("/metadataReference")
public class MetadataReferenceController {

    private final MetadataValueService metadataValueService;

    private final MetadataReferenceService metadataReferenceService;

    private final MetadataConnectService metadataConnectService;

    /**
     * 简单分页查询
     *
     * @param page          分页对象
     * @param metadataValue 映射关系
     * @return
     */
    @GetMapping("/page")
    public R<IPage<MetadataReference>> getMetadataReferencePage(Page<MetadataReference> page, MetadataValue metadataValue) {
        //获取一条数据的映射关系
        MetadataReference metadataReference = new MetadataReference();
        metadataReference.setSourceId(metadataValue.getResourceId());
        return new R<>(metadataReferenceService.getMetadataReferencePage(page, metadataReference));
    }

    /**
     * 简单分页查询
     *
     * @param page 分页对象
     * @return
     */
    @GetMapping("/getReferenceList")
    public R<List<Map<String, Object>>> getMetaDataReferenceList(Page<MetadataReferenceInfo> page, MetadataReference reference) {
        String sourceParentId = reference.getSourceParentId();
        String targetParentId = reference.getTargetParentId();
        return new R<>(metadataReferenceService.getMetaDataReferenceList(page, sourceParentId, targetParentId));
    }

    /**
     * 简单分页查询(获取子数据的映射关系，例如获取一个表下的所有列的关系)
     *
     * @param page          分页对象
     * @param metadataValue 映射关系
     * @return
     */
    @GetMapping("getChildPage")
    public R<IPage<MetadataReference>> getMetadataReferenceChildPage(Page<MetadataReference> page, MetadataValue metadataValue) {
        //获取一条数据所有子数据的映射关系
        return new R<>(metadataReferenceService.getMetadataReferenceChildPage(page, metadataValue));
    }

    /**
     * 新增映射
     *
     * @param metadataReference
     * @return R
     */
    @SysLog("新增映射关系")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('MetadataManage_metadatavalue_add')")
    public R save(@RequestBody MetadataReference metadataReference) {
        return new R<>(metadataReferenceService.save(metadataReference));
    }

    /**
     * 批量新增映射
     *
     * @param metadataReferences
     * @return R
     */
    @SysLog("新增映射关系")
    @PutMapping("saveBatch")
    @PreAuthorize("@pms.hasPermission('MetadataManage_metadatavalue_add')")
    public R saveBatch(@RequestBody List<MetadataReference> metadataReferences) {
        return new R<>(metadataReferenceService.saveBatch(metadataReferences));
    }

    /**
     * 批量删除映射
     *
     * @param metadataReferences
     * @return R
     */
    @SysLog("删除映射")
    @PutMapping("removedReferences")
    @PreAuthorize("@pms.hasPermission('MetadataManage_metadatavalue_del')")
    public R removedReferenceBatch(@RequestBody List<MetadataReference> metadataReferences) {
        boolean result = true;
        for (MetadataReference metadataReference : metadataReferences) {
            if (result) {
                result = metadataReferenceService.deleteReference(metadataReference);
            }
        }
        return new R<>(result);
    }

    /**
     * 删除一条映射
     *
     * @param metadataReference
     * @return R
     */
    @SysLog("删除映射")
    @PutMapping("removedReference")
    @PreAuthorize("@pms.hasPermission('MetadataManage_metadatavalue_del')")
    public R removedReference(@RequestBody MetadataReference metadataReference) {
        return new R<>(metadataReferenceService.deleteReference(metadataReference));
    }

    /**
     * 返回某个元数据的血缘分析结果
     */
    @GetMapping(value = "/connect/{resourceId}")
    public R getMetadataItemConnect(@PathVariable("resourceId") String resourceId){
        return new R<>(metadataConnectService.getMetadataItemConnect(resourceId));
    }
}

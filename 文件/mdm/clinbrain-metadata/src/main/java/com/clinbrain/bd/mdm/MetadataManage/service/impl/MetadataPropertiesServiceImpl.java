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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.mdm.MetadataManage.dto.MetadataPropertiesDto;
import com.clinbrain.bd.mdm.MetadataManage.entity.DictItems;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataProperties;
import com.clinbrain.bd.mdm.MetadataManage.mapper.MetadataPropertiesMapper;
import com.clinbrain.bd.mdm.MetadataManage.service.DictItemService;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataPropertiesService;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 属性
 *
 * @author wangyi
 * @date 2019-05-29 10:05:46
 */
@Service("metadataPropertiesService")
public class MetadataPropertiesServiceImpl extends ServiceImpl<MetadataPropertiesMapper, MetadataProperties> implements MetadataPropertiesService {

    @Autowired
    private DictItemService dictItemService;

    /**
     * 属性简单分页查询
     *
     * @param metadataProperties 属性
     * @return
     */
    @Override
    public IPage<MetadataProperties> getMetadataPropertiesPage(Page<MetadataProperties> page, MetadataProperties metadataProperties) {
      /*List<Integer> listRole = SecurityUtils.getRoles();
      String roleIds = Joiner.on(",").join(listRole);*/
        return baseMapper.getMetadataPropertiesPage(page, metadataProperties);
    }

    /**
     * @return
     * @author WANGYI
     * @createdDate 2019/5/29 11:13
     * @description TODO 添加属性的同时自动映射列
     * @class MetadataPropertiesServiceImpl
     * @group bigdata develop group ()
     * @params
     * @updatedDate
     * @updatedBy
     */
    @SneakyThrows
    @Override
    public boolean saveProperties(MetadataProperties metadataProperties) {
        //先保存
        save(metadataProperties);
        //如果dataType  dataLength 为空未指定数据长度的默认50
        String mappingColumn = null;
        if ("datetime".equalsIgnoreCase(metadataProperties.getDataType())) {
            mappingColumn = baseMapper.generateMetadataPropertiesMappingColumnSpecial(metadataProperties);
        } else if (metadataProperties.getDataLength() == null) {
            metadataProperties.setDataLength(50);
            if ("text".equalsIgnoreCase(metadataProperties.getDataType())) {
                metadataProperties.setDataLength(6000);
            }
            mappingColumn = baseMapper.generateMetadataPropertiesMappingColumnNormal(metadataProperties);
        } else {
            mappingColumn = baseMapper.generateMetadataPropertiesMappingColumnNormal(metadataProperties);
        }
        if (mappingColumn == null) {
            throw new Exception("模型属性个数已经超过系统设定，请联系管理员");
        }
        metadataProperties.setMappingColumn(mappingColumn);
        mappingColumn = com.baomidou.mybatisplus.core.toolkit.StringUtils.underlineToCamel(mappingColumn);
        metadataProperties.setMappingField(mappingColumn);

        return baseMapper.updateMetadataPropertiesMappingColumn(metadataProperties);
    }

    @Override
    public boolean saveOrUpdateProperties(List<MetadataProperties> metadataProperties) {
        if (metadataProperties == null || metadataProperties.isEmpty()) return false;
        //新增
        List<MetadataProperties> updts = metadataProperties.stream()
                .filter(g -> StringUtils.isNotBlank(g.getNameEn()))
                .map(e -> {
                    if (e.getId() == null || e.getId() == 0) {
                        e.setResourceId(UUID.randomUUID().toString());
                        saveProperties(e);
                        e.setId(null);
                    }
                    return e;
                })
                .filter(e -> e.getId() != null && e.getId() != 0)
                .collect(Collectors.toList());
        //修改
        if (updts != null && !updts.isEmpty()) {
            updateBatchById(updts);
        }
        return true;
    }

    @Override
    public String selectColumnSql(MetadataProperties metadataProperties) {
        return this.baseMapper.selectColumnSql(metadataProperties);
    }


    /**
     * 属性简单分页查询
     *
     * @param metadataProperties 属性
     * @return
     */
    @Override
    public IPage<MetadataPropertiesDto> getMetadataPropertiesAndItemsPage(Page<MetadataProperties> page, MetadataProperties metadataProperties) {
        IPage pages = baseMapper.getMetadataPropertiesPage(page, metadataProperties);
        if (pages != null && pages.getRecords() != null && pages.getRecords().size() > 0) {
            List<MetadataPropertiesDto> dtoList = new ArrayList<>();
            pages.getRecords().stream().forEach(i -> {
                MetadataPropertiesDto dto = new MetadataPropertiesDto();
                BeanUtil.copyProperties(i, dto);
                if (StrUtil.isNotEmpty(dto.getShowType()) && !"input".equalsIgnoreCase(dto.getShowType()) && !"datetime".equalsIgnoreCase(dto.getShowType())) {
                    dto.setItemsList(dictItemService.getSelectItemsValues(dto.getNameEn()));
                }
                dtoList.add(dto);
            });
            pages.setRecords(dtoList);
        }
        return pages;
    }
}

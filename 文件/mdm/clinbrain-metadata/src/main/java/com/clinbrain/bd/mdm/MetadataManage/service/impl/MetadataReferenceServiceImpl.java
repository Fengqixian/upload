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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataModel;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataReference;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataReferenceInfo;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.bd.mdm.MetadataManage.mapper.MetadataReferenceMapper;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataModelService;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataReferenceService;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataValueService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 元数据关联关系
 *
 * @author
 */
@Service("metadataReferenceService")
public class MetadataReferenceServiceImpl extends ServiceImpl<MetadataReferenceMapper, MetadataReference> implements MetadataReferenceService {

    @Autowired
    private MetadataValueService metadataValueService;

    @Autowired
    private MetadataModelService metadataModelService;

    /**
     * 导入元数据,区分是否已存在
     *
     * @param metadataValue
     * @return
     * @author yjt
     * @date 2019/7/8 16:17
     */
    @Override
    public boolean saveMetaDataValueInfo(MetadataValue metadataValue) {
        try {
            //当前数据已导入过，则不再导入
            List<MetadataValue> checkList = metadataValueService.getMetaDataListByName(metadataValue);
            if (checkList != null && checkList.size() > 0) {
                return true;
            }
            return metadataValueService.save(metadataValue);

            //20190724  版本更改，不再使用根模型
            /*
            //查询根模型下是否存在，根据英文名忽略大小写进行查找
            //根模型下不存在时，同步导入一份到根模型，根模型数据为待审批状态
            //根模型下存在时，根模型不导入
            MetadataValue checkValue = new MetadataValue();
            if (metadataValue.getNameEn() != null && !"".equalsIgnoreCase(metadataValue.getNameEn().trim())) {
                checkValue.setNameEn(metadataValue.getNameEn());
            }
            checkValue.setIsStandard(true);
            checkValue.setModelType(metadataValue.getModelType());
            List<MetadataValue> list = metadataValueService.getMetaDataListByName(checkValue);

            //当前导入的数据是否根模型数据
            boolean isStandard = metadataValue.getIsStandard() != null ? metadataValue.getIsStandard() : false;//modelPage.getRecords().get(0).getIsStandard();
            if (isStandard) {
                metadataValue.setParentId("");
            }

            //状态待审批
            metadataValue.setStatus(0);
            if (list == null || list.size() < 1) {
                //不存在
                if (!isStandard) {
                    //非根模型，需要向根模型中同步插入一份，并且为待审批
                    MetadataValue rootValue = getMetaDataValueFromModelValue(metadataValue);
                    if (rootValue != null) {
                        metadataValueService.save(rootValue);
                    }
                }
            } else {
                //存在
                if (isStandard) {
                    return true;
                }
            }
            return metadataValueService.save(metadataValue);
            */
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public IPage<MetadataReference> getMetadataReferencePage(Page<MetadataReference> page, MetadataReference metadataReference) {
        return baseMapper.getMetadataReferencePage(page, metadataReference);
    }

    @Override
    public List<Map<String, Object>> getMetaDataReferenceList(Page page, String sourceParentId, String targetParentId) {
        return baseMapper.getMetaDataReferenceList(page, sourceParentId, targetParentId);
    }

    @Override
    public IPage<MetadataReference> getMetadataReferenceChildPage(Page<MetadataReference> page, MetadataValue metadataValue) {
        MetadataValue value = new MetadataValue();
        value.setParentId(metadataValue.getParentId());
        Page<MetadataValue> pagevalue = new Page<>();
        pagevalue.setSize(-1);
        IPage<MetadataValue> valueList = metadataValueService.getMetadataValuePage(pagevalue, value);
        String ids = "";
        for (MetadataValue v : valueList.getRecords()) {
            if ("".equalsIgnoreCase(ids)) {
                ids = "'" + v.getResourceId() + "'";
            } else {
                ids += ",'" + v.getResourceId() + "'";
            }
        }
        return baseMapper.getMetadataReferenceChildPage(page, ids);
    }


    @Override
    public boolean save(MetadataReference metadataReference) {
        baseMapper.insert(metadataReference);
        return true;
    }

    @Override
    public boolean deleteReference(MetadataReference metadataReference) {
        baseMapper.deleteReference(metadataReference);
        return true;
    }

    /**
     * 根据导入的模型数据获取一个根模型数据
     *
     * @param metadatavalue 导入的模型数据（非根模型数据）
     * @return
     * @author yjt
     * @date 2019/7/9 15:24
     */
    private MetadataValue getMetaDataValueFromModelValue(MetadataValue metadatavalue) {
        try {
            MetadataValue value = new MetadataValue();
            BeanUtils.copyProperties(metadatavalue, value);
            MetadataModel model = new MetadataModel();
            model.setModelType(value.getModelType());
            model.setIsStandard(true);

            Page<MetadataModel> page = new Page<>();
            page.setSize(-1);
            //根据模型类型获取同类型的根模型
            IPage<MetadataModel> modelPage = metadataModelService.getMetadataModelPage(page, model);
            if (modelPage == null || modelPage.getRecords().size() < 1) {
                return null;
            }
            //根模型
            MetadataModel rootModel = modelPage.getRecords().get(0);
            value.setStatus(0);
            value.setResourceId(UUID.randomUUID().toString());
            value.setModelId(rootModel.getResourceId());
            value.setModelType(rootModel.getModelType());
            value.setIsStandard(true);
            value.setParentId("");

            return value;
        } catch (Exception e) {
            return null;
        }
    }
}

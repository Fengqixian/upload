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
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataReference;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataReferenceInfo;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.bd.mdm.common.core.util.R;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 元数据Excel导入
 */
public interface MetadataReferenceService extends IService<MetadataReference> {

    boolean saveMetaDataValueInfo(MetadataValue metadataValue);

    IPage<MetadataReference> getMetadataReferencePage(Page<MetadataReference> page, MetadataReference metadataReference);

    List<Map<String, Object>> getMetaDataReferenceList(Page page, String sourceParentId, String targetParentId);

    IPage<MetadataReference> getMetadataReferenceChildPage(Page<MetadataReference> page, MetadataValue metadataValue);

    //boolean save(MetadataReference metadataReference);

    boolean deleteReference(MetadataReference metadataReference);
}

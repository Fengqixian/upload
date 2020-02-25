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
import com.clinbrain.bd.mdm.MetadataManage.dto.ModelResourceTree;
import com.clinbrain.bd.mdm.MetadataManage.entity.ElementNode;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.bd.mdm.MetadataManage.entity.ModelResourceTreeEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Map;

/**
 * 元数据
 *
 * @author wangyi
 * @date 2019-05-29 14:41:40
 */
public interface MetadataValueMapper extends BaseMapper<MetadataValue> {
    /**
     * 元数据简单分页查询
     *
     * @param metadataValue 元数据
     * @return
     */
    IPage<MetadataValue> getMetadataValuePage(Page page, @Param("metadataValue") MetadataValue metadataValue, @Param("roleIds") List roleIds);

    List<ModelResourceTree> listRoleModelResourceTree(@Param("roleId") Integer roleId);

    List<ModelResourceTree> listModelResourceTree(@Param("roleIds") List roleIds);

    List<ModelResourceTree> listModelParentResourceTree(@Param("parentUuid") String parentUuid);

    List<ModelResourceTree> getModelResourceTreeByLoopQuery(@Param("parentUuid") String parentUuid);

    List<ModelResourceTree> listModelResourceLazyTree(@Param("parentUuid") String parentUuid);

    List<ModelResourceTree> listModelResourceByUuid(@Param("uuid") String uuid);

    List<ModelResourceTree> queryModelResourceLazyTree(@Param("queryString") String queryString);

    List<ModelResourceTree> listModelResourceTreeByModelId(@Param("roleIds") List<Integer> listRole, @Param("modelId") String modelId);

    List<MetadataValue> getMetadataValueListByName(@Param("metadataValue") MetadataValue metadataValue);

    List<Map> selectMetaValueByDynSql(@Param("sql") String sql, @Param("metadataValue") MetadataValue metadataValue);
    List<MetadataValue> selectDistinctMetadata(@Param("metadataValue") MetadataValue metadataValue);
    MetadataValue selectDbByCloumn(@Param("metadataValue") MetadataValue metadataValue);

    List<MetadataValue> getMetadataValueByIds(@Param("ids") String inColumnString);

    List<MetadataValue> selectNotInElement(@Param("columnModelId") String columnModelId,
                                          @Param("tbResouceId") String tbResouceId,
                                          @Param("elementModelId") String elementModelId,
                                          @Param("dbResouceId") String dbResouceId);
    void callCreateElementItem(@Param("paramMap") Map paramMap);
    List<ElementNode> getAllItem();

    List<ModelResourceTree> listRoleModelResourcePage(@Param("roleId") Integer roleId, @Param("resourceIds") List<String> resourceId);
}

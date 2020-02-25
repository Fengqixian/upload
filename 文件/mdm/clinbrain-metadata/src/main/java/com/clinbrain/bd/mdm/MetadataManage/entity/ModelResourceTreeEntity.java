package com.clinbrain.bd.mdm.MetadataManage.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.mdm.MetadataManage.entity.ModelResourceTree
 * @createdDate 2019/5/31 14:15
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "ModelResourceTreeEntity")
public class ModelResourceTreeEntity {
    @ApiModelProperty(value = "节点ID")
    Integer id;
    private String uuid;
    private String parentUuid;
    private String nameCn;
    private String nameEn;
    private String resourceId;
    private String modelId;
    private String parentResourceId;
    private String type;
    private Integer status;
    private String rangeId;
    private String rangeValues;
    private String cancelDataMasking;
    private String customConditions;
    private String modelType;
    private String permissionCreate;
    private String permissionDelete;
    private String permissionUpdate;

}

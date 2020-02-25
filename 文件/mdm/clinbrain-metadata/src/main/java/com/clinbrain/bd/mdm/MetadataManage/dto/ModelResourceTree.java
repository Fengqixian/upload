package com.clinbrain.bd.mdm.MetadataManage.dto;

import com.clinbrain.bd.mdm.admin.api.dto.TreeNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.mdm.MetadataManage.dto.ModelTree
 * @createdDate 2019/5/29 15:39
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ModelResourceTree extends TreeNode {
    String nameCn;
    String nameEn;
    String resourceId;
    String modelId;
    String parentResourceId;
    String type;
    Integer status;
    String rangeId;
    String rangeValues;
    String cancelDataMasking;
    String customConditions;
    String modelType;
    String permissionCreate;
    String permissionUpdate;
    String permissionDelete;
    Boolean isStandard;
}

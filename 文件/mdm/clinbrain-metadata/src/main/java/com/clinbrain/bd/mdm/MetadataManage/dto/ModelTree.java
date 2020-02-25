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
public class ModelTree extends TreeNode {
    String nameCn;
    String nameEn;
    String status;
    String resourceId;
    String description;
    String parentResourceId;
    String isRange;
    String rangeId;
    String modelType;
    /**
     * 是否是标准模型
     * @date  2019/6/12 10:37
     */
    Boolean isStandard;
}

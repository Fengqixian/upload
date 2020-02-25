package com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo;

import lombok.Data;

/**
 * @author lianglele
 * @date 2019-11-11 15:21
 */
@Data
public class EtlSelect {
    private String workflowTokenCode;

    private String sourceTableAliasName;
    private String sourceColumnName;
    private String sourceColumnExpressionDefault;
    private String sourceColumnExpressionCustomized;
    private String targetColumnAliasName;
    private Integer isEnable = 1;
    private Integer isDefault = 1;
    private long createdAt;
    private long updatedAt;

}

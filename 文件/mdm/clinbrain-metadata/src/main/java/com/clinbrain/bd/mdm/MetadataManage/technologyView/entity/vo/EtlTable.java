package com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo;

import lombok.Data;

/**
 * @author lianglele
 * @date 2019-11-11 15:21
 */
@Data
public class EtlTable {
    private Integer id;
    private String workflowTokenCode;
    private Integer isPrimaryTable;
    private String joinType;
    private String joinOnCurrentColumnName;
    private String joinOnRightTableAliasName;
    private String joinOnRightTableColumnName;
    private String joinOnExpression;
    private Integer isEnable = 1;
    private Integer isDefault = 1;
    private String incrementalColumn;
    private long createdAt;
    private long updatedAt;
    private String dbName;
    private String tableExpression;
    private String tableAliasName;
}

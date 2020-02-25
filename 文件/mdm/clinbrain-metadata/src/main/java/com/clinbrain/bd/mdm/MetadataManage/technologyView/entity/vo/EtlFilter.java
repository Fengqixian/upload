package com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo;

import lombok.Data;

/**
 * @author lianglele
 * @date 2019-11-11 15:21
 */
@Data
public class EtlFilter {
    private String workflowTokenCode;

    private String commonFilterExpression;
    private String commonFilterExpressionCustomized;
    private String incrementalFilterExpression;
    private String incrementalFilterExpressionCustomized;
    private String rangeFilterExpression;
    private String rangeFilterExpressionCustomized;
    private Integer isEnable = 1;
    private Integer isDefault = 1;
    private long createdAt;
    private long updatedAt;

}

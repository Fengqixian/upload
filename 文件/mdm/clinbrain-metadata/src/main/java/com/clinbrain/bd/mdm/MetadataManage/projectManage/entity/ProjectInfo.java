package com.clinbrain.bd.mdm.MetadataManage.projectManage.entity;

import lombok.Data;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectInfo
 * @createdDate 2019/10/25 16:19
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
@Data
public class ProjectInfo {
    private Integer projectId;
    private Integer conditionId;
    private Integer refId;
    private String condition;
    private Boolean isKeyValue;
    private Integer elementId;
    private String elementName;
    private Integer columnId;
    private String columnName;
    private Integer tableId;
    private String tableName;
    private String tableAlias;
    private Boolean isCaseWhen;
}

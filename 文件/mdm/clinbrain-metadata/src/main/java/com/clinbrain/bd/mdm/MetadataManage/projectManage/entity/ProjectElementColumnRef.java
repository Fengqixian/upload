package com.clinbrain.bd.mdm.MetadataManage.projectManage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mdm_project_element_column_condition")
public class ProjectElementColumnRef extends Model<ProjectElementColumnRef> {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer datasetId;
    private Integer refId;
    private String condition;
    private boolean isKeyValue;

    private Integer tableId;
    private String tableNameCn;
    private String tableNameEn;
    private Integer columnId;
    private String columnNameCn;
    private String columnNameEn;
    private Integer elementId;

}


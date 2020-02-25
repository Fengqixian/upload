package com.clinbrain.bd.mdm.MetadataManage.projectManage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mdm_project_element_ref")
public class ProjectElementRef extends Model<ProjectElementRef> {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer datasetId;
    private Integer elementId;
}


package com.clinbrain.bd.mdm.MetadataManage.projectManage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mdm_project_category_ref")
public class ProjectDataSetRef extends Model<ProjectDataSetRef> {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer categoryId;
    private Integer datasetId;
}


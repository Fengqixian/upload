package com.clinbrain.bd.mdm.MetadataManage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mdm_resource_reference")
public class MetadataReference extends Model<MetadataReference> {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;
    private String sourceId;
    private String targetId;
    private String sourceParentId;
    private String targetParentId;
    private String refType;
}
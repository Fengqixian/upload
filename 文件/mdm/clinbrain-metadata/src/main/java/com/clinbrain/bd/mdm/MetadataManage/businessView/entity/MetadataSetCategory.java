package com.clinbrain.bd.mdm.MetadataManage.businessView.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mdm_dataset_category")
public class MetadataSetCategory extends Model<MetadataSetCategory> {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;
    private Integer parentId;
    private String nameEn;
    private String nameCn;
    private String resourceId;
    private String resourceCode;
    private String sourceId;
    private String category;
    private String description;
    private String createUser;
    private LocalDateTime createTime;
}


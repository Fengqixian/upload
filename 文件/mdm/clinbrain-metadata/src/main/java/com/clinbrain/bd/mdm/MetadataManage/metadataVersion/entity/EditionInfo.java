package com.clinbrain.bd.mdm.MetadataManage.metadataVersion.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mdm_edition_info")
public class EditionInfo extends Model<EditionInfo> {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;
    private String versionCode;
    private String description;
    private String userAccount;
    private String userId;
    private Date createDateTime;
}

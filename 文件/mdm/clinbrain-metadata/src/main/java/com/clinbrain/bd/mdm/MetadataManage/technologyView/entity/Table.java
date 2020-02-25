package com.clinbrain.bd.mdm.MetadataManage.technologyView.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Table
 * @createdDate 2019/10/13 11:37
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
@Data
@TableName("mdm_technology_table")
public class Table {
    @TableId
    private Integer id;
    private Integer databaseId;
    private String resourceId;
    private String resourceCode;
    private String nameEn;
    private String nameCn;
    private String remark;
}

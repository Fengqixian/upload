package com.clinbrain.bd.mdm.MetadataManage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dict_info")
public class DictInfo extends Model<DictInfo> {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;
    private String dictName;
    private String sqlCommand;
    private String dictType;
    private Integer showOrder;
    private String description;
}
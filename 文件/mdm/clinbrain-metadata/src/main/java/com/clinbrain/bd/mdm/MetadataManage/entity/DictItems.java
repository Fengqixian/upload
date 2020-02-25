package com.clinbrain.bd.mdm.MetadataManage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dict_items")
public class DictItems  extends Model<DictItems> {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;
    private Integer dictId;
    private String itemsName;
    private String itemsCode;
    private Integer showOrder;
    private String description;
}
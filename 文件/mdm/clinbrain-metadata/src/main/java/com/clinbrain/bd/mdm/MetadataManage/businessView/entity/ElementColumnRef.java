package com.clinbrain.bd.mdm.MetadataManage.businessView.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mdm_element_column_ref")
public class ElementColumnRef extends Model<ElementColumnRef> {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;
    private Integer elementId;
    private Integer columnId;

}

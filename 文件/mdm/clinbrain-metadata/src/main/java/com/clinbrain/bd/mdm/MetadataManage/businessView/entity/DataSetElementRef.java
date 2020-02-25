package com.clinbrain.bd.mdm.MetadataManage.businessView.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mdm_dataset_element_ref")
public class DataSetElementRef extends Model<DataSetElementRef> {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer datasetId;
    private Integer elementId;

}

package com.clinbrain.bd.mdm.MetadataManage.businessView.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectElementColumnRef;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.mdm.MetadataManage.businessView.entity.DataElement
 * @createdDate 2019/10/22 15:34
 * @description 数据元实体（保持和表一致，用于新增；如需增加额外属性时使用MetadataElement）
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
@Data
@TableName("mdm_data_element")
public class DataElement {
    @TableId
    private Integer id;
    private String resourceId;
    private String resourceCode;
    private String sourceId;
    private String category;
    private String nameEn;
    private String nameCn;
    private String definition;
    private String dataType;
    private String showFormat;
    private String allowableValue;
    private String allowableType;
    private String allowableCode;
}

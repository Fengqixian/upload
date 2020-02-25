package com.clinbrain.bd.mdm.MetadataManage.businessView.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectElementColumnRef;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mdm_data_element")
public class MetadataElement extends Model<com.clinbrain.bd.mdm.MetadataManage.businessView.entity.MetadataElement> {
    private static final long serialVersionUID = 1L;
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

    private Map<String,Object> rangeList = new HashMap<>();
    private List<ProjectElementColumnRef> options = new ArrayList<>();
    private Integer refId;
}

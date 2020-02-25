package com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo;

import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Column;
import lombok.Data;

import java.util.List;

@Data
public class TableVo {
    private Integer id;
    private Integer databaseId;
    private String resourceId;
    private String resourceCode;
    private String nameEn;
    private String nameCn;
    private String remark;
    private boolean changeType = false;
    private List<Column> changeColList;
}

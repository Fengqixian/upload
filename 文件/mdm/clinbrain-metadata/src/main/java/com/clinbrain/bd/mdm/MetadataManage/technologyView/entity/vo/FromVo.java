package com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo;

import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Column;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Table;
import lombok.Data;

import java.util.List;


@Data
public class FromVo {
    /**
     * gp,hive,mysql,sqlserver
     */
    private String type;
    /**
     * 数据库标识
     */
    private Integer databaseId;
    /**
     * 数据库标识
     */
    private Integer tableId;
    /**
     * 表
     */
    private String tableName;
    /**
     * 数据库名
     */
    private String dbName;

    /**
     * 1、全量导入表 2:全量导入字段 3：增量创建表 4：增量创建字段
     */
    private Integer importType;

    private List<TableVo> tableList;

    private List<TableVo> changeList;

}

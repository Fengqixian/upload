package com.clinbrain.bd.mdm.MetadataManage.technologyView.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author LIANGLELE
 * @className com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.EtlSource
 * @createdDate 2019/11/13 11:37
 * @description TODO
 * @e-mail LIANGLELE@clinbrain.com
 * @group bigdata develop group (mdm)
 */
@Data
@TableName("mdm_technology_etl_source")
public class EtlSource {
    @TableId
    private Integer id;
    /**
     *ETL   ID
     */
    private Integer etlId;
    /**
     * 源数据库
     */
    private String sourceDb;
    /**
     * 源数据库ID
     */
    private Integer sourceDbId;
    /**
     * 源表
     */
    private String sourceTable;
    /**
     * 源表ID
     */
    private Integer sourceTableId;
}

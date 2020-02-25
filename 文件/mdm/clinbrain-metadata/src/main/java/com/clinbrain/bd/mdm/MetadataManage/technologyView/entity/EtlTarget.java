package com.clinbrain.bd.mdm.MetadataManage.technologyView.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author LIANGLELE
 * @className com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.TargetEtl
 * @createdDate 2019/11/13 11:37
 * @description TODO
 * @e-mail LIANGLELE@clinbrain.com
 * @group bigdata develop group (mdm)
 */
@Data
@TableName("mdm_technology_etl")
public class EtlTarget {
    @TableId
    private Integer id;
    /**
     * 目标数据库ID
     */
    private Integer targetDbId;
    /**
     * 目标数据库
     */
    private String targetDb;
    /**
     * 目标表
     */
    private String targetTable;
    /**
     * 目标表id
     */
    private Integer targetTableId;
    /**
     * 执行sql
     */
    private String executorSql;
}

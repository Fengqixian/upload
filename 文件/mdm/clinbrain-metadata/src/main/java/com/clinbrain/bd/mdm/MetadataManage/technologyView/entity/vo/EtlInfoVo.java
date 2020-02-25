package com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo;

import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Database;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.EtlDetail;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.EtlSource;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.EtlTarget;
import lombok.Data;
import sun.rmi.runtime.Log;

import java.util.List;

/**
 * @author LIANGLELE
 * @className com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.EtlDetail
 * @createdDate 2019/11/13 11:37
 * @description TODO
 * @e-mail LIANGLELE@clinbrain.com
 * @group bigdata develop group (mdm)
 */
@Data
public class EtlInfoVo {

    /**
     * 任务编号，新建留空
     */
    private String moduleCode;
    /**
     * 任务名称
     */
    private String moduleName;
    /**
     * 任务名称
     */
    private String moduleDescription;
    /**
     * 任务名称
     */
    private String connectionCode;

    private String targetSchema;
    /**
     * 任务名称
     */
    private String targetTable;

    private Integer modulePriority = 1;

    /**
     * 任务类型，无实际意义，使用DIM就可以
     */
    private String moduleCategory = "DIM";

    /**
     * 增量0 / 区间1 ， 配合fullWhileMonths > 1 就是区间 =0 就是全量
     */
    private Integer etlType = 1;

    /**
     * 启用
     */
    private Integer enabled = 1;

    private Integer fullWhileMonths = 1;

    /**
     * 区间时间开始
     */
    private Long rangeStartDate;

    /**
     * 区间时间结束
     */
    private Long rangeEndDate;

    private String topicId;

    /**
     * 任务所属JOB
     */
    private Integer jobId;

    /**
     * 医院代码
     */
    private String hospitalName;

    private Long createdAt;

    private Long updatedAt;

    private List<Object> workflows;


//    private EtlSource etlSource;
//
//    private EtlTarget etlTarget;
//
//    private List<EtlDetail> etlDetailList;
//
//    private Database sourceDatabase;
//
//    private Database targetDatabase;
//
//    private EtlSource etlSource;
//
//    private EtlTarget etlTarget;
//
//    private List<EtlDetail> etlDetailList;
//
//    private Database sourceDatabase;
//
//    private Database targetDatabase;

}

package com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo;

import lombok.Data;

/**
 * @author lianglele
 * @date 2019-11-11 15:10
 */
@Data
public class EtlExtraData {

    /**
     * 来源
     */
    private EtlConnection connection;

    /**
     * 目标
     */
    private EtlConnection targetConnection;


    private EtlSql sql;
    private String fullSql;
    private EtlType reader;
    private EtlType writer;

}

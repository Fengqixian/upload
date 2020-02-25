package com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @author lianglele
 * @date 2019-11-11 15:21
 */
@Data
public class EtlSql {
    private List<EtlSelect> select;
    private String where;
    private String incrementalWhere;
    private String rangeWhere;
    private EtlFilter filter;
    private EtlFrom from;
}

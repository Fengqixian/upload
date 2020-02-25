package com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @author lianglele
 * @date 2019-11-11 15:21
 */
@Data
public class EtlFrom {
    private EtlTable primaryTable;
    private List<EtlTable> joinTables;

}

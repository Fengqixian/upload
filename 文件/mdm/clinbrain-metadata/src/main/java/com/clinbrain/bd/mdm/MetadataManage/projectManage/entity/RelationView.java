package com.clinbrain.bd.mdm.MetadataManage.projectManage.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LIANGLELE
 * @className com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.RelationView
 * @createdDate 2019/10/13 11:43
 * @description TODO
 * @e-mail LIANGLELE@clinbrain.com
 * @group bigdata develop group (mdm)
 */
@Data
public class RelationView {
    private Integer tableId;
    private String tableNameEn;
    private String tableNameCn;

    private String resourceId;
    private Integer columnId;
    private String columnResourceId;
    private String columnNameEn;
    private String columnNameCn;
}

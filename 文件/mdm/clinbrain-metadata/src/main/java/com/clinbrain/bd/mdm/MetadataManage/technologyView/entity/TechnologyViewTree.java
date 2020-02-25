package com.clinbrain.bd.mdm.MetadataManage.technologyView.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.TechnologyViewTree
 * @createdDate 2019/10/13 11:43
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
@Data
public class TechnologyViewTree {
    private Integer databaseId;
    private String databaseResid;
    private String databaseNameEn;
    private String databaseNameCn;
    private String connectIp;
    private String connectHost;
    private String connectUser;
    private String connectPassword;
    private String databaseType;

    private Integer tableId;
    private String tableResid;
    private String tableNameEn;
    private String tableNameCn;


    private Integer columnId;
    private String columnResid;
    private String columnNameEn;
    private String columnNameCn;

    private Integer id;
    private Integer parentId;
    private String resourceId;
    private String nameEn;
    private String nameCn;
    private String nodeType;
    private String eleResourceId;
    private List<TechnologyViewTree> children = new ArrayList<>();
}

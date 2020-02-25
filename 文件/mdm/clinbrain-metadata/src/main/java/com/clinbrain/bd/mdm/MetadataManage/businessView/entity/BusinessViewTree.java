package com.clinbrain.bd.mdm.MetadataManage.businessView.entity;

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
public class BusinessViewTree {
    private Integer datasetId;
    private String datasetResid;
    private String datasetNameEn;
    private String datasetNameCn;


    private Integer id;
    private Integer parentId;
    private String resourceId;
    private String nameEn;
    private String nameCn;
    private String nodeType;
    private String subResourceId;
    private List<BusinessViewTree> children = new ArrayList<>();
}

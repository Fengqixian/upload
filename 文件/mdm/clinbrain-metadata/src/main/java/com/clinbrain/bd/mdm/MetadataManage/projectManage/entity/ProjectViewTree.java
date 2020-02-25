package com.clinbrain.bd.mdm.MetadataManage.projectManage.entity;

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
public class ProjectViewTree {
    private Integer projectId;
    private String projectResid;
    private String projectNameEn;
    private String projectNameCn;


    private Integer id;
    private Integer parentId;
    private String resourceId;
    private String nameEn;
    private String nameCn;
    private String nodeType;
    private List<ProjectViewTree> children = new ArrayList<>();
}

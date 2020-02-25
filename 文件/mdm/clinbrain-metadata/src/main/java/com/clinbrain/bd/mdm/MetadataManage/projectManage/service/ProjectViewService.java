package com.clinbrain.bd.mdm.MetadataManage.projectManage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.DataElement;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectDataSetCategory;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectViewTree;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.RelationView;
import com.clinbrain.bd.mdm.common.core.util.R;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ProjectViewService {

    R getProjectViewTree();

    R getProjectViewTree(String roleId);

    R<IPage<DataElement>> getProjectDataElement(Page<DataElement> page, ProjectViewTree tree, String queryString);

    R<IPage<DataElement>> getProjectDataElement(Page<DataElement> page, ProjectViewTree tree, String queryString, String roleId);

    void getProjectDataElementWithData(HttpServletResponse response, Integer projectId, List<Integer> ids) throws Exception;

    R<List<RelationView>> selectRelationView(Integer elementId);

    R<List<RelationView>> selectProjectColumn(Integer datasetId, Integer elementId);

    R<List<RelationView>> selectProjectColumn(Integer datasetId, Integer elementId, String roleId);

    List<ProjectDataSetCategory> selectProjectCategory(String resourceId);
}

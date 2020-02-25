package com.clinbrain.bd.mdm.MetadataManage.projectManage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectDataSetCategory;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectDataSetRef;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectElementColumnRef;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectElementRef;
import com.clinbrain.bd.mdm.common.core.util.R;

import java.util.List;

public interface ProjectDataService extends IService<ProjectDataSetCategory> {
    boolean saveProjectDataSetRef(List<ProjectDataSetRef> projectDataSetRefList);

    boolean saveProjectElementRef(List<ProjectElementRef> projectElementRefList);

    boolean deleteCategoryProjectRef(Integer id);

    boolean deleteElementRef(Integer datasetId);

    boolean deleteProjectElementColumnRef(Integer projectId);

    boolean saveProjectElementColumnRef(List<ProjectElementColumnRef> projectElementColumnRefList);

    R getElementColumnRefInfo(List<Integer> ids);

    R getProjectElementInfo(Integer id);

    R getElementList(String queryString, String type);
}

package com.clinbrain.bd.mdm.MetadataManage.projectManage.mapper;

import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectDataSetCategory;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectViewTree;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.RelationView;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * 业务数据视图
 *
 * @author yjt
 * @return
 * @date 2019/10/12 18:24
 */
public interface ProjectViewMapper {
    List<ProjectViewTree> getProjectViewTreeList();

    List<ProjectViewTree> getProjectViewTreeListByRoleId(@Param("roleId") String roleId);

    List<RelationView> selectRelationView(@Param("elementId") Integer elementId);

    List<RelationView> selectProjectColumn(@Param("datasetId") Integer datasetId, @Param("elementId") Integer elementId);

    List<RelationView> selectProjectColumnByRoleId(@Param("datasetId") Integer datasetId, @Param("elementId") Integer elementId, @Param("roleId") String roleId);

    List<ProjectDataSetCategory> selectProjectCategory(@Param("resourceId") String resourceId);
}

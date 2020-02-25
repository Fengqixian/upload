package com.clinbrain.bd.mdm.MetadataManage.projectManage.mapper;

import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  业务数据视图
 * @return
 * @author yjt
 * @date  2019/10/12 18:24
 */
public interface ProjectInfoMapper {
    List<ProjectInfo> getProjectInfoList(@Param("projectId")Integer projectId, @Param("ids") List<Integer> ids);
}

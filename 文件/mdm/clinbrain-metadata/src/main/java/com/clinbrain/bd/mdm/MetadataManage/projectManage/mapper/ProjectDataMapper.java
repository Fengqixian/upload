package com.clinbrain.bd.mdm.MetadataManage.projectManage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.DataElement;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.MetadataElement;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.*;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
  * 工程视图编辑接口
  * @return
  * @author yjt
  * @date  2019/11/12 14:07
 */
public interface ProjectDataMapper extends BaseMapper<ProjectDataSetCategory> {

    /**
      * 保存分类和工程关系
      * @param projectDataSetRefList
      * @return
      * @author yjt
      * @date  2019/11/13 16:58
     */
    void saveProjectDataSetRef(@Param("projectDataSetRefList") List<ProjectDataSetRef> projectDataSetRefList);

    /**
      * 保存工程和数据元关系
      * @param projectElementRefList
      * @return
      * @author yjt
      * @date  2019/11/13 16:59
     */
    boolean saveProjectElementRef(@Param("projectElementRefList")List<ProjectElementRef> projectElementRefList);

    /**
      * 删除分类和工程关系
      * @param id
      * @return
      * @author yjt
      * @date  2019/11/14 10:09
     */
    boolean deleteCategoryProjectRef(@Param("id")Integer id);

    /**
      * 删除工程和数据元关系
      * @param datasetId
      * @return
      * @author yjt
      * @date  2019/11/13 16:59
     */
    boolean deleteElementRef(@Param("datasetId") Integer datasetId);

    /**
      * 删除工程所属数据元和对应列的关系
      * @param datasetId
      * @return
      * @author yjt
      * @date  2019/11/13 17:00
     */
    boolean deleteProjectElementColumnRef(@Param("datasetId") Integer datasetId);

    /**
      * 保存工程所属数据元和对应列关系
      * @param projectElementColumnRefList
      * @return
      * @author yjt
      * @date  2019/11/13 17:00
     */
    boolean saveProjectElementColumnRef(@Param("projectElementColumnRefList") List<ProjectElementColumnRef> projectElementColumnRefList);

    /**
      * 获取数据元的所有列关系
      * @param ids 数据元集合
      * @return
      * @author yjt
      * @date  2019/11/13 17:01
     */
    List<ProjectElementColumnRef> getElementColumnRefInfo(@Param("ids") List<Integer> ids);

    /**
      * 获取工程的所属数据元
      * @param id  工程id
      * @return
      * @author yjt
      * @date  2019/11/13 16:57
     */
    List<MetadataElement> getProjectElementList(@Param("id") Integer id);

    /**
      * 获取工程所属数据元的所有关联列
      * @param id 工程id
      * @return
      * @author yjt
      * @date  2019/11/13 16:57
     */
    List<ProjectElementColumnRef> getElementColumnList(@Param("id") Integer id);

    /**
      * 获取工程每个所属数据元选择的对应列
      * @param id
      * @return
      * @author yjt
      * @date  2019/11/13 16:58
     */
    List<ProjectElementColumnRef> getProjectElementColumnDetail(@Param("id") Integer id);

    List<MetadataElement> getElementList(@Param("queryString") String queryString, @Param("elementId") Integer elementId);
    List<MetadataElement> getElementListByDataSet(@Param("queryString") String queryString);
}

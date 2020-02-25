package com.clinbrain.bd.mdm.MetadataManage.businessView.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.*;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 *  业务数据视图
 * @return
 * @author yjt
 * @date  2019/10/12 18:24
 */
public interface MetadataSetCategoryMapper extends BaseMapper<MetadataSetCategory> {
    IPage<MetadataSetCategory> getChildCategoryList(Page page, @Param("categoryId") Integer categoryId, @Param("queryString") String queryString);
    IPage<MetadataElement> getPageMetaDataElement(Page page, @Param("ids") List<Integer> ids,@Param("queryString") String queryString);
    List<MetadataSetCategory> getCategoryInfo(@Param("categoryId") Integer categoryId,@Param("resourceId") String resourceId);

    List<BusinessViewTree> getBusinessViewTreeList();
    List<MetadataElement> getElementList(@Param("queryString") String queryString,@Param("elementId") Integer elementId);
    List<MetadataElement> getElementListByDataSet(@Param("queryString") String queryString);

    void saveCatgoryRef(@Param("categoryDataSetRefList") List<CategoryDataSetRef> categoryDataSetRefList);
    void saveDataSetRef(@Param("dataSetElementRefList")List<DataSetElementRef> dataSetElementRefList);

    void deleteElementRef(@Param("datasetId") Integer datasetId);

    List<BusinessViewTree> getBusinessViewTreeListByRoleId(@Param("roleId") String roleId);

    IPage<MetadataElement> getPageMetaDataElementByRoleId(Page<MetadataElement> page, @Param("ids")List<Integer> ids, @Param("queryString")String queryString,@Param("roleId") String roleId);

    List<BusinessViewTree> getCategotyLazzyTree(@Param("nodeId") Integer nodeId,@Param("queryString") String queryString);

    List<BusinessViewTree> getElementListLazzyTree(@Param("nodeId") Integer nodeId,@Param("queryString") String queryString);

    void deleteCategoryDataSetRef(@Param("id") Integer id);
}

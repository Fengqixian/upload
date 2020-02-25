package com.clinbrain.bd.mdm.MetadataManage.businessView.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.*;
import com.clinbrain.bd.mdm.common.core.util.R;

import java.util.List;

public interface MetadataCategoryService extends IService<MetadataSetCategory> {

    R getMetadataSetCategoryTree();
    R getMetadataSetCategoryTree(String roleId);
    IPage<MetadataSetCategory> getChildCategoryList(Page<MetadataSetCategory> page, MetadataSetCategory metadataSetCategory, String queryString);

    IPage<MetadataElement> getPageMetaDataElement(Page<MetadataElement> page, MetadataSetCategory metadataSetCategory, String queryString);

    R getCategoryInfo(Integer categoryId);

    R getCategoryInfo(MetadataSetCategory category);

    R getElementList(String queryString, String type);

    boolean saveCategoryRef(List<CategoryDataSetRef> categoryDataSetRefList);

    boolean saveDataSetRef(List<DataSetElementRef> dataSetElementRefList);

    boolean deleteElementRef(Integer datasetId);

    R getElementInfo(Page<ElementRange> page, MetadataElement element,String url);

    R getElementRangeList(Page<ElementRange> page,MetadataElement element,String url);

    IPage<MetadataElement>  getPageMetaDataElement(Page<MetadataElement> page, MetadataSetCategory metadataSetCategory, String queryString, String client);

    R getMetaCategoryLazzyTree(BusinessViewTree treeNode,String queryString);

    boolean deleteCategoryDataSetRef(Integer id);
}

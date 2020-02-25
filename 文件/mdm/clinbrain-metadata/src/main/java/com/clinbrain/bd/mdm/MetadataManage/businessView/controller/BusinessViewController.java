package com.clinbrain.bd.mdm.MetadataManage.businessView.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.*;
import com.clinbrain.bd.mdm.MetadataManage.businessView.service.MetaDataElementService;
import com.clinbrain.bd.mdm.MetadataManage.businessView.service.MetaElementColumnRefService;
import com.clinbrain.bd.mdm.MetadataManage.businessView.service.MetadataCategoryService;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.TechnologyViewTree;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.service.TechnologyViewService;
import com.clinbrain.bd.mdm.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 业务视图
 */
@RestController
@RequestMapping("/businessView")
@CrossOrigin
public class BusinessViewController {

    @Autowired
    private MetadataCategoryService metadataCategoryService;
    @Autowired
    private MetaDataElementService metaDataElementService;
    @Autowired
    private MetaElementColumnRefService metaElementColumnRefService;
    @Autowired
    private TechnologyViewService technologyViewService;

    @Value("${webService.mdmUrl}")
    private String mdmUrl;

    @GetMapping("/tree")
    public R getBusinessViewTree() throws Exception {
        return metadataCategoryService.getMetadataSetCategoryTree();
    }

    @GetMapping("/public/category")
    public R getBusinessViewTree(String client) throws Exception {
        return metadataCategoryService.getMetadataSetCategoryTree(client);
    }

    @GetMapping("/childCategory")
    public R<IPage<MetadataSetCategory>> getChildCategoryList(Page<MetadataSetCategory> page, MetadataSetCategory metadataSetCategory, String queryString) {
        return new R<>(metadataCategoryService.getChildCategoryList(page, metadataSetCategory, queryString));
    }

    @GetMapping("/elementList")
    public R<IPage<MetadataElement>> getMetaDataElement(Page<MetadataElement> page, MetadataSetCategory metadataSetCategory, String queryString) {
        return new R<>(metadataCategoryService.getPageMetaDataElement(page, metadataSetCategory, queryString));
    }

    @GetMapping("/public/categoryDetail")
    public R<IPage<MetadataElement>> getMetaDataElement(Page<MetadataElement> page, MetadataSetCategory metadataSetCategory, String queryString, String client) {
        return new R<>(metadataCategoryService.getPageMetaDataElement(page, metadataSetCategory, queryString, client));
    }

    @GetMapping("/categoryInfo/{categoryId}")
    public R listModelResourceTree(@PathVariable("categoryId") Integer categoryId) {
        return metadataCategoryService.getCategoryInfo(categoryId);
    }

    @GetMapping("/getElementList")
    public R getElementList(String queryString, String type) {
        return metadataCategoryService.getElementList(queryString, type);
    }

    @GetMapping("/elementInfo")
    public R getElementInfo(Page<ElementRange> page, MetadataElement element) {
        return metadataCategoryService.getElementInfo(page, element, mdmUrl);
    }

    @GetMapping("/elementRangeInfo")
    public R getElementRangeList(Page<ElementRange> page, MetadataElement element) {
        return metadataCategoryService.getElementRangeList(page, element, mdmUrl);
    }

    /**
     * 新增或编辑分类/数据集
     *
     * @param metadataSetCategory
     * @return R
     */
    @PutMapping("saveOrUpdate")
    public R saveOrUpdateBatch(@RequestBody MetadataSetCategory metadataSetCategory) {
        return new R(metadataCategoryService.saveOrUpdate(metadataSetCategory));
    }

    /**
     * 新增或编辑分类与数据集的关系
     *
     * @param categoryDataSetRefList
     * @return
     * @author yjt
     * @date 2019/10/15 11:08
     */
    @PutMapping("saveCategoryRef")
    public R saveCategoryRef(@RequestBody List<CategoryDataSetRef> categoryDataSetRefList) {
        return new R<>(metadataCategoryService.saveCategoryRef(categoryDataSetRefList));
    }

    /**
     * 新增和编辑数据集与数据源关系
     *
     * @param dataSetElementRefList
     * @return
     * @author yjt
     * @date 2019/10/15 11:14
     */
    @PutMapping("saveDataSetRef")
    public R saveDataSetRef(@RequestBody List<DataSetElementRef> dataSetElementRefList) {
        return new R<>(metadataCategoryService.saveDataSetRef(dataSetElementRefList));
    }

    /**
     * 保存数据集并同步保存关系
     *
     * @param param
     * @return
     * @author yjt
     * @date 2019/10/15 17:58
     */
    @PutMapping("saveDataSetAndRef")
    public R saveDataSetAndRef(@RequestBody Map<String, Object> param) {
        try {

            boolean result = true;
            boolean editFlag = false;
            if (param.get("categoryId") == null) {
                return new R<>(1, "未获取到分类ID", null);
            }
            Integer categoryId = Integer.parseInt(param.get("categoryId").toString());
            MetadataSetCategory dataset = JSON.parseObject(JSON.toJSONString(param.get("dataset")), MetadataSetCategory.class);
            List<Integer> elementList = JSONArray.parseArray(param.get("elementList").toString(), Integer.class);

            Integer datasetId = dataset.getId();
            //有id表示是更新数据集
            if (datasetId != null) {
                editFlag = true;
            }
            //保存数据集
            result = metadataCategoryService.saveOrUpdate(dataset);

            //保存分类和数据集关系
            if (result && !editFlag) {
                //保存后的数据集ID
                datasetId = dataset.getId();
                CategoryDataSetRef categoryDataSetRef = new CategoryDataSetRef();
                categoryDataSetRef.setCategoryId(categoryId);
                categoryDataSetRef.setDatasetId(datasetId);
                List<CategoryDataSetRef> categoryDataSetRefList = new ArrayList<>();
                categoryDataSetRefList.add(categoryDataSetRef);
                result = metadataCategoryService.saveCategoryRef(categoryDataSetRefList);
            }

            //保存数据集与数据元关系
            if (result) {
                //更新前先删除关系
                if (editFlag) {
                    result = metadataCategoryService.deleteElementRef(datasetId);
                }

                if (result) {
                    List<DataSetElementRef> dataSetElementRefList = new ArrayList<>();
                    if (elementList != null && elementList.size() > 0) {
                        for (Integer elementId : elementList) {
                            DataSetElementRef ref = new DataSetElementRef();
                            ref.setDatasetId(datasetId);
                            ref.setElementId(elementId);
                            dataSetElementRefList.add(ref);
                        }
                        result = metadataCategoryService.saveDataSetRef(dataSetElementRefList);
                    }
                }
            }

            return new R<>(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new R<>(1, "保存数据集异常", null);
        }
    }

    @GetMapping("/getCategoryLazzyTree")
    public R getMetaCategoryLazzyTree(BusinessViewTree treeNode, String queryString) {
        return metadataCategoryService.getMetaCategoryLazzyTree(treeNode, queryString);
    }

    @PutMapping("/deleteCategory")
    public R deleteMetaCategory(@RequestBody MetadataSetCategory metadataSetCategory) {
        //删除分类、数据集
        boolean result = metadataCategoryService.removeById(metadataSetCategory.getId());

        if (!result) {
            return new R(result);
        }
        //删除分类/数据集关系、删除数据集/数据元关系
        if ("category".equalsIgnoreCase(metadataSetCategory.getCategory())) {
            result = metadataCategoryService.deleteCategoryDataSetRef(metadataSetCategory.getId());
        } else if ("dataset".equalsIgnoreCase(metadataSetCategory.getCategory())) {
            result = metadataCategoryService.deleteElementRef(metadataSetCategory.getId());
        }
        return new R(result);
    }

    /**
     * 保存或更新数据元和数据元关联字段
     *
     * @param param
     * @return
     * @author yjt
     * @date 2019/12/4 9:37
     */
    @PutMapping("/saveElement")
    public R saveOrUpdateElement(@RequestBody Map<String, Object> param) {
        try {
            //数据元参数
            DataElement element = JSON.parseObject(JSON.toJSONString(param.get("element")), DataElement.class);
            if (element.getResourceId() == null || "".equalsIgnoreCase(element.getResourceId())) {
                element.setResourceId(UUID.randomUUID().toString());
            }

            boolean editFlag = false;
            if (element.getId() != null) {
                editFlag = true;
            }
            //保存数据元
            metaDataElementService.saveOrUpdate(element);

            //新增时保存数据元和数据集关系
            if (!editFlag) {
                List<DataSetElementRef> dataSetElementRefList = new ArrayList<>();
                Integer datasetId = Integer.parseInt(param.get("datasetId").toString());
                DataSetElementRef eleRef = new DataSetElementRef();
                eleRef.setDatasetId(datasetId);
                eleRef.setElementId(element.getId());
                dataSetElementRefList.add(eleRef);
                metadataCategoryService.saveDataSetRef(dataSetElementRefList);
            }

            Integer elementId = element.getId();
            //数据元与列关系
            String refStr = JSONArray.toJSONString(param.get("refList"));
            List<ElementColumnRef> refList = JSON.parseArray(refStr, ElementColumnRef.class);
            if (refList == null || refList.size() < 1) {
                //删除现有关系
                Map<String, Object> map = new HashMap<>();
                map.put("element_id", element.getId());
                metaElementColumnRefService.removeByMap(map);
                return new R(true);
            }

            refList.stream().forEach(e -> e.setElementId(elementId));
            if (!editFlag) {
                //新增的数据元，直接保存关系
                metaElementColumnRefService.saveOrUpdateBatch(refList);
            } else {
                //编辑数据元,保存列关系
                ElementColumnRef ref = new ElementColumnRef();
                ref.setElementId(elementId);
                Wrapper wrapper = Wrappers.query(ref);
                //已经存在的关系
                List<ElementColumnRef> allRef = metaElementColumnRefService.list(wrapper);

                List<Integer> ids = new ArrayList<>();

                for (ElementColumnRef f : allRef) {
                    boolean hasFlag = false;
                    //过滤出已有的和新保存的重合项,新保存集合中不存在的关系删除
                    for (ElementColumnRef r : refList) {
                        if (r.getColumnId().equals(f.getColumnId())) {
                            hasFlag = true;
                            break;
                        }
                    }
                    if (!hasFlag) {
                        ids.add(f.getId());
                    }
                }

                //去除已经存在的关系集合，只保存新添加的
                List<ElementColumnRef> saveRef = new ArrayList<>();
                for (ElementColumnRef r1 : refList) {
                    boolean flag = false;
                    for (ElementColumnRef f1 : allRef) {
                        if (f1.getColumnId().equals(r1.getColumnId())) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        saveRef.add(r1);
                    }
                }

                if (ids != null && ids.size() > 0) {
                    metaElementColumnRefService.removeByIds(ids);
                }
                metaElementColumnRefService.saveOrUpdateBatch(saveRef);
            }

            return new R(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new R(false);
        }
    }

    /**
     * 获取字段集合
     *
     * @param page        分页参数
     * @param queryString 模糊查询(查询列的中英文名)
     * @param elementId   数据元id
     * @return
     * @author yjt
     * @date 2019/12/4 9:45
     */
    @GetMapping("/columnList")
    public R<IPage<TechnologyViewTree>> getTechnologyColumnList(Page<TechnologyViewTree> page, String queryString, Integer elementId) {
        List<Integer> elementids = new ArrayList<>();
        elementids.add(elementId);
        return technologyViewService.getTechnologyColumnList(page, queryString, elementids);
    }
}

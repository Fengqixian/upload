package com.clinbrain.bd.mdm.MetadataManage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.ElementRange;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.MetadataElement;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.MetadataSetCategory;
import com.clinbrain.bd.mdm.MetadataManage.businessView.service.MetadataCategoryService;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageMetaValue;
import com.clinbrain.bd.mdm.MetadataManage.service.MetaDataLineageService;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.service.TechnologyViewService;
import com.clinbrain.bd.mdm.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 数据血缘 (根据技术视图、业务视图分析)
 */
@RestController
@AllArgsConstructor
@RequestMapping("/metaDataLineage")
public class MetaDataLineageController {
    @Autowired
    private MetaDataLineageService metaDataLineageService;

    @Autowired
    private MetadataCategoryService metadataCategoryService;
    @Autowired
    private TechnologyViewService technologyViewService;

    /**
     * 数据血缘分析
     *
     * @param lineageMetaValue
     * @return
     */
    @ResponseBody
    @GetMapping("getDataLineage")
    public R getDataLineage(LineageMetaValue lineageMetaValue) {

        if(lineageMetaValue.getVersion()!=null&&"HX".equalsIgnoreCase(lineageMetaValue.getVersion())){
            return metaDataLineageService.getDataLineageHX(lineageMetaValue);
        }else{
            return metaDataLineageService.getDataLineage(lineageMetaValue);
        }
    }

    /**
     * 获取数据血缘节点详情
     *
     * @param lineageMetaValue
     * @return
     */
    @ResponseBody
    @GetMapping("getDataLineageNodeInfo")
    public R getDataLineageNodeInfo(LineageMetaValue lineageMetaValue) {
        try {
            if (lineageMetaValue == null || lineageMetaValue.getDataType() == null || lineageMetaValue.getResourceId() == null)
                return null;
            switch (lineageMetaValue.getDataType()) {
                case "database":
                case "table":
                case "column":
                    return technologyViewService.getTechnologyViewNodeInfo(lineageMetaValue.getResourceId(), lineageMetaValue.getDataType());
                case "element_item":
                    MetadataElement element = new MetadataElement();
                    element.setResourceId(lineageMetaValue.getResourceId());
                    return metadataCategoryService.getElementInfo(null, element, "");
                case "element_set":
                    MetadataSetCategory category = new MetadataSetCategory();
                    category.setResourceId(lineageMetaValue.getResourceId());
                    return metadataCategoryService.getCategoryInfo(category);
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取ETL的字段映射详情
     * @param lineageMetaValue
     * @author yjt
     * @date  2019/11/4 15:22
     */
    @ResponseBody
    @GetMapping("getDataLineageETLDetail")
    public R getDataLineageETLDetail(LineageMetaValue lineageMetaValue){
        if(lineageMetaValue.getVersion()!=null&&"HX".equalsIgnoreCase(lineageMetaValue.getVersion())){
            return metaDataLineageService.getDataLineageETLDetailHX(lineageMetaValue);
        }else{
            return metaDataLineageService.getDataLineageETLDetail(lineageMetaValue);
        }
    }

}

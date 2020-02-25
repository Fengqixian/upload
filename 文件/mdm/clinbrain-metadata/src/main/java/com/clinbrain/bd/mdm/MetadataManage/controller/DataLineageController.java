package com.clinbrain.bd.mdm.MetadataManage.controller;

import com.clinbrain.bd.mdm.MetadataManage.entity.ModelResourceTreeEntity;
import com.clinbrain.bd.mdm.MetadataManage.service.DataLineageService;
import com.clinbrain.bd.mdm.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 数据血缘关系分析
 */
@RestController
@Api(value = "血缘管理")
@RequestMapping("/dataLineage")
public class DataLineageController {
    int count = 6;
    //@Autowired
    //private UuidGenerator uuidGenerator;
    @Autowired
    private DataLineageService dataLineageService;
    /**
     * 点击元数据出入如下参数
     * {"parentId":"1769","moduleInstanceId":"2306","treeType":"instance"}
     * @param jsonParam
     * @return
     */
    @ResponseBody
    @ApiOperation(response = R.class,value = "查询血缘关系",notes = "根据id查询")
    @PostMapping("getDataLineage")
    public R getDataLineage(@RequestBody  ModelResourceTreeEntity treeEntity) throws Exception{
        return dataLineageService.getDataLineage(treeEntity);
    }
    /**
     * 点击元数据出入如下参数
     * resourceId
     * @param jsonParam
     * @return
     */
    /*@ResponseBody
    @PostMapping("getDataLineage/{resourceId}")
    public R getDataLineage(@PathVariable String resourceId) throws Exception{
        return dataLineageService.getDataLineage(resourceId);
    }*/
    /**
     * 点击元数据出入如下参数
     * resourceId
     * @param jsonParam
     * @return
     */
    @ResponseBody
    @GetMapping("getDataLineage/{resourceId}")
    public R getDataLineage(@PathVariable String resourceId) throws Exception{
        return dataLineageService.getDataLineage(resourceId);
    }
}

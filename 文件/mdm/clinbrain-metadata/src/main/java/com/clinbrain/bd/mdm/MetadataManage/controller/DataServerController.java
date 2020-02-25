package com.clinbrain.bd.mdm.MetadataManage.controller;

import com.clinbrain.bd.mdm.MetadataManage.dto.DataService;
import com.clinbrain.bd.mdm.MetadataManage.entity.ModelResourceTreeEntity;
import com.clinbrain.bd.mdm.MetadataManage.service.DataLineageService;
import com.clinbrain.bd.mdm.MetadataManage.service.DataServiceService;
import com.clinbrain.bd.mdm.MetadataManage.util.uuid.UuidGenerator;
import com.clinbrain.bd.mdm.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 数据血缘关系分析
 */
@RestController
@Api(value = "数据服务")
@RequestMapping("/dataServer")
public class DataServerController {
    @Autowired
    private DataServiceService service;
    @ResponseBody
    @ApiOperation(response = R.class,value = "请求服务",notes = "请求指定服务")
    @PostMapping("/{projectName}/{modelName}/{serviceName}/{version}")
    public String getDataLineage(@RequestBody(required = false) Map<String,Object> paramaMap
            ,@PathVariable String projectName
            ,@PathVariable String modelName
            ,@PathVariable String serviceName
            ,@PathVariable(required = false) String version) throws Exception{
        return service.service(projectName,modelName,serviceName,version,paramaMap);
    }
}

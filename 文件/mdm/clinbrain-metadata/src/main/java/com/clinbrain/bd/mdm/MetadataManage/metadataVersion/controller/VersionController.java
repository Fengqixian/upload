package com.clinbrain.bd.mdm.MetadataManage.metadataVersion.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.DataElement;
import com.clinbrain.bd.mdm.MetadataManage.metadataVersion.entity.EditionInfo;
import com.clinbrain.bd.mdm.MetadataManage.metadataVersion.service.VersionControlService;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Column;
import com.clinbrain.bd.mdm.common.core.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 版本控制
 */
@RestController
@RequestMapping("/version")
@CrossOrigin
public class VersionController {
    @Autowired
    private VersionControlService service;

    /**
      * 发布版本
      * @param editionInfo  版本信息
      * @return 
      * @author yjt
      * @date  2019/12/23 10:08 
     */
    @PostMapping("/release")
    public R ReleaseNewVersion(EditionInfo editionInfo){
        return service.createNewVersion(editionInfo);
    }

    /**
      * 回滚版本
      * @param versionId  回滚的版本号,回滚master版本时传值0
      * @return 
      * @author yjt
      * @date  2019/12/23 10:09 
     */
    @PostMapping("/rollback/{versionId}")
    public R RollBackEdition(@PathVariable("versionId") Integer versionId){
        if(versionId==null||versionId.equals(0)){
            return service.rollBackMaster();
        }
        return service.rollBackEdition(versionId);
    }

    /**
      * 获取版本信息集合
      * @param page 分页参数
      * @param queryString  模糊查询
      * @return 
      * @author yjt
      * @date  2019/12/23 10:09 
     */
    @GetMapping("/versionList")
    public R<IPage<EditionInfo>> getVersionList(Page<EditionInfo> page,String queryString){
        return new R<>(service.getVersionList(page,queryString));
    }

    /**
      * 获取列的版本信息
      * @param page
      * @param versionId
      * @param queryString
      * @return
      * @author yjt
      * @date  2019/12/24 16:20
     */
    @GetMapping("/columnInfo")
    public R<IPage<Column>> getColumnInfo(Page<Column> page,Integer versionId,Integer tableId,String queryString){
        return new  R<>(service.getColumnInfo(page,versionId,tableId,queryString));
    }

    /**
      * 获取数据集的数据元信息
      * @return 
      * @author yjt
      * @date  2019/12/24 16:32 
     */
    @GetMapping("/elementInfo")
    public R<IPage<DataElement>> getElementInfo(Page<DataElement> page,Integer versionId,Integer datasetId, String queryString){
        return new R<>(service.getElementInfo(page,versionId,datasetId,queryString));
    }
}

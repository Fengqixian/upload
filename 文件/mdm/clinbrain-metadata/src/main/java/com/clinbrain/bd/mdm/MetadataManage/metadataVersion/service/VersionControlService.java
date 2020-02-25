package com.clinbrain.bd.mdm.MetadataManage.metadataVersion.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.businessView.entity.DataElement;
import com.clinbrain.bd.mdm.MetadataManage.metadataVersion.entity.EditionInfo;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Column;
import com.clinbrain.bd.mdm.common.core.util.R;

public interface VersionControlService {

    /**
      *  发布新版本
      * @return
      * @author yjt
      * @date  2019/12/20 15:00
     */
    R createNewVersion(EditionInfo editionInfo);

    /**
      * 回滚Master版本  
      * @return 
      * @author yjt
      * @date  2019/12/23 9:48 
     */
    R rollBackMaster();

    /**
      * 回滚指定版本
      * @param versionId  
      * @return 
      * @author yjt
      * @date  2019/12/23 9:49 
     */
    R rollBackEdition(Integer versionId);

    /**
     * 获取版本信息集合
     * @param page 分页参数
     * @param queryString  模糊查询
     * @return
     * @author yjt
     * @date  2019/12/23 10:09
     */
    IPage<EditionInfo> getVersionList(Page<EditionInfo> page,String queryString);

    IPage<Column> getColumnInfo(Page<Column> page,Integer versionId,Integer tableId,String queryString);

    IPage<DataElement> getElementInfo(Page<DataElement> page, Integer versionId, Integer datasetId,String queryString);

}

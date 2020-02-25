package com.clinbrain.bd.mdm.MetadataManage.service;

import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.bd.mdm.MetadataManage.entity.ModelResourceTreeEntity;
import com.clinbrain.bd.mdm.common.core.util.R;

import java.util.List;

public interface IndexService {
    R getIndex(String resourceId) throws Exception;


    /**
     * 获取表中的数量
     * @throws Exception 错误信息
     */
    void getIndexCount() throws Exception;

}

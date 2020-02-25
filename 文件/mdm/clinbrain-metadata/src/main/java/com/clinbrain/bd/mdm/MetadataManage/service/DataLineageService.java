package com.clinbrain.bd.mdm.MetadataManage.service;

import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.bd.mdm.MetadataManage.entity.ModelResourceTreeEntity;
import com.clinbrain.bd.mdm.common.core.util.R;

import java.util.List;

public interface DataLineageService {

    R getDataLineage(ModelResourceTreeEntity jsonParam) throws Exception;
    R getDataLineage(String resourceId) throws Exception;

    List<MetadataValue> getMetadataValueByIds(String inColumnString);

    List<MetadataValue> getColumnSourceItems(MetadataValue metadataValue);

    List<MetadataValue> getColumnTransferEtl(MetadataValue metadataValue);

    List<MetadataValue> getParentItems(MetadataValue metadataValue);
}

package com.clinbrain.bd.mdm.MetadataManage.service;

import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.bd.mdm.MetadataManage.entity.ModelResourceTreeEntity;
import com.clinbrain.bd.mdm.common.core.util.R;

import java.util.List;
import java.util.Map;

public interface DataServiceService {

    String service(String projectName, String modelName, String serviceName, String version,Map<String,Object> paramaMap) throws Exception;
}

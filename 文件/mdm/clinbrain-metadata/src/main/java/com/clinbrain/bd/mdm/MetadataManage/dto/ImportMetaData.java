package com.clinbrain.bd.mdm.MetadataManage.dto;

import lombok.Data;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/6/20 13:59
 * @Description:
 */
@Data
public class ImportMetaData {
    String tableName;
    String modelId;
    String parentModelId;
    String resourceId;
    String metaDataType;
    String metaDataId;
    String parentMetaDataId;
    String databaseModelId;
    String databaseResourceId;
}

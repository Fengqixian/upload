package com.clinbrain.bd.mdm.MetadataManage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageNode;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.parser.common.entity.Column;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DataLineageMapper extends BaseMapper {
    LineageNode transferColumn2MetadataColumn(Column c);

    LineageNode transferColumn2MetadataIndex(Column c);

    List<LineageNode> getLineageTableNode(Set<String> tableList);
    List<MetadataValue> getColumnSourceItems(MetadataValue metadataValue);
    List<MetadataValue> getColumnTransferEtl(MetadataValue metadataValue);

    List<Map<String,String>> getTableInDatabaseIndex(@Param("modelType") String modelType);

    List<MetadataValue> getParentItems(MetadataValue metadataValue);
}
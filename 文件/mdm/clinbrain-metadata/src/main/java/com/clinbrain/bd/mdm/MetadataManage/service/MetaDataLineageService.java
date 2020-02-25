package com.clinbrain.bd.mdm.MetadataManage.service;

import com.clinbrain.bd.mdm.MetadataManage.entity.LineageMetaValue;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageNode;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectDataSetCategory;
import com.clinbrain.bd.mdm.common.core.util.R;

import java.util.List;

public interface MetaDataLineageService {

    R getDataLineage(LineageMetaValue lineageValue);
    R getDataLineageHX(LineageMetaValue lineageMetaValue);

    List<LineageMetaValue> getColumnSourceItems(LineageMetaValue lineageMetaValue);

    List<LineageMetaValue> getColumnSourceTables(LineageMetaValue lineageMetaValue);

    List<LineageMetaValue> getItemSourceColumns(LineageMetaValue lineageMetaValue);

    List<LineageMetaValue> getItemDataSets(LineageMetaValue lineageMetaValue);

    List<LineageMetaValue> getTableEtlSourceTables(LineageMetaValue lineageMetaValue);

    R getDataLineageETLDetail(LineageMetaValue lineageMetaValue);
    R getDataLineageETLDetailHX(LineageMetaValue lineageMetaValue);

    //根据id获取工程信息
    ProjectDataSetCategory getProjectNodeInfo(Integer id);
    //根据工程id和数据元id获取该数据元配置的来源字段
    LineageMetaValue getElementRefColumnInfo(Integer datasetId, Integer elementId);

    List<LineageMetaValue> getColumnEtlSourceColumns(List<LineageMetaValue> sourceColumns);

    //获取项目视图数据元关联的数据集
    List<LineageMetaValue>  getElementProjectList(Integer id);
}

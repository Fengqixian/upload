package com.clinbrain.bd.mdm.MetadataManage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageMetaValue;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageNode;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.parser.common.entity.Column;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MetaDataLineageMapper extends BaseMapper {
    LineageNode transferColumn2MetadataColumn(Column c);

    LineageNode transferColumn2MetadataIndex(Column c);

    //获取列生成的数据元
    List<LineageMetaValue> getColumnSourceItems(@Param("id") Integer id);

    //获取字段所在的表
    List<LineageMetaValue> getColumnSourceTables(@Param("id") Integer id);

    //获取数据元生成的列
    List<LineageMetaValue> getItemSourceColumns(@Param("id") Integer id);

    //获取与数据元有关的数据集
    List<LineageMetaValue> getItemDataSets(@Param("id") Integer id);

    //获取表的ETL关系表
    List<LineageMetaValue> getTableEtlSourceTables(@Param("id") Integer id);

    List<Map<String,Object>> getTableInDatabaseIndex();

    List<Map<String,Object>> getDataLineageETLDetail(@Param("targetTableResourceId") String targetTableResourceId);

    //获取字段的来源字段
    List<LineageMetaValue> getColumnEtlSourceColumns(@Param("columnIds") String columnIds);

    //分步获取etl表
    //1.根据target_table_id或target_table获取etl
    List<Map<String,Object>> getTableETLInfo(@Param("lineage") LineageMetaValue lineage);

    //2.根据etl_id获取etl的source表
    List<Map<String,Object>> getTableETLSource(@Param("ids") List<Integer> ids);

    //从etl_detail表直接获取etl详情，用于目标表或来源表在technology_table里不存在时
    List<Map<String,Object>> getDataLineageETLDetailDirect(@Param("targetTableName") String targetTableName,@Param("etlId") Integer etlId);

    List<LineageMetaValue>  getElementProjectList(@Param("id") Integer id);
}
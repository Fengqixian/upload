package com.clinbrain.bd.mdm.MetadataManage.technologyView.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Column;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Table;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.TechnologyViewTree;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TechnologyViewTreeMapper extends BaseMapper<TechnologyViewTree> {
    List<TechnologyViewTree> treeList();

    IPage<Column> getTableColumnList(Page page, @Param("tableId") Integer tableId, @Param("queryString") String queryString);

    IPage<TechnologyViewTree> getTechnologyColumnList(Page page, @Param("queryString") String queryString, @Param("elementIds") List<Integer> elementIds);

    List<Table> getDataTableList(@Param("tableName") String tableName);

    List<Column> getDataColumnList(@Param("tableName") String tableName);

    List<Table> getDataGPTableList(@Param("tableName") String tableName);

    List<Column> getDataGPColumnList(@Param("tableName") String tableName);

    List<Table> getDataHiveTableList(@Param("dbName") String dbName);

    List<Column> getDataHiveColumnList(@Param("dbName") String dbName, @Param("tableName") String tableName);
}
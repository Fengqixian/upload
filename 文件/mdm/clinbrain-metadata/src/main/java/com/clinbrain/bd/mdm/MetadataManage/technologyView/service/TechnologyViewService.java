package com.clinbrain.bd.mdm.MetadataManage.technologyView.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Column;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Database;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Table;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.TechnologyViewTree;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo.FromVo;
import com.clinbrain.bd.mdm.common.core.util.R;

import java.util.List;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.mdm.MetadataManage.service.TechnologyViewService
 * @createdDate 2019/10/13 11:25
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
public interface TechnologyViewService {
    R getTechnologyViewTree();

    R getTableInfoByTreeNode(Page<Column> page, TechnologyViewTree tree, String queryString);

    R getTableListByTreeNode(Page<Table> page, TechnologyViewTree tree);

    R getDatabase(Page<Database> page, Database database);

    R getColumn(Page<Table> page, Table table);

    R getTechnologyViewNodeInfo(String resourceId, String type);

    R getTechnologyColumnList(Page<TechnologyViewTree> page, String queryString, List<Integer> elementIds);

    /**
     * 获取数据库下表结构信息
     *
     * @param fromVo 不为空的话查询当前表信息
     * @return
     */
    R getDataTableList(FromVo fromVo);

    /**
     * 获取数据库下表字段结构信息
     *
     * @param fromVo 查询当前表信息
     * @return
     */
    R getTableColumnList(FromVo fromVo);

    /**
     * 导入数据表结构
     *
     * @param fromVo 参数
     * @return R 成功失败信息
     */
    R importDataBase(FromVo fromVo);

    /**
     * 创建数据库
     *
     * @param database
     * @return
     */
    R saveDataBase(Database database);
    /**
     * 修改数据库
     *
     * @param database
     * @return
     */
    R updateDataBase(Database database);

    /**
     * 创建数据表
     *
     * @param tables
     * @return
     */
    R saveDataTable(List<Table> tables);

    /**
     * 创建数据表
     *
     * @param tables
     * @return
     */
    R updateDataTable(List<Table> tables);

    /**
     * 创建数据字段
     *
     * @param columns
     * @return
     */
    R saveDataColumn(List<Column> columns);

    /**
     * 修改数据字段
     *
     * @param columns
     * @return
     */
    R updateDataColumn(List<Column> columns);


}

package com.clinbrain.bd.mdm.MetadataManage.technologyView.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Column;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Database;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Table;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.TechnologyViewTree;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo.FromVo;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo.TableVo;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.mapper.TechnologyColumnMapper;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.mapper.TechnologyDatabaseMapper;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.mapper.TechnologyTableMapper;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.mapper.TechnologyViewTreeMapper;
import com.clinbrain.bd.mdm.MetadataManage.util.cost.MetaModuleConst;
import com.clinbrain.bd.mdm.MetadataManage.util.uuid.UuidUtil;
import com.clinbrain.bd.mdm.admin.api.vo.TreeUtil;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.common.datasource.config.DynamicDataSourceConfig;
import com.clinbrain.bd.mdm.common.datasource.support.DynamicDataSourceContextHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.mdm.MetadataManage.service.impl.TechnologyViewServiceImpl
 * @createdDate 2019/10/13 11:26
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
@AllArgsConstructor
@Getter
@Service
public class TechnologyViewServiceImpl implements TechnologyViewService {
    private TechnologyViewTreeMapper technologyViewTreeMapper;
    private TechnologyColumnMapper technologyColumnMapper;
    private TechnologyTableMapper technologyTableMapper;
    private TechnologyDatabaseMapper technologyDatabaseMapper;
    private final DynamicDataSourceConfig dynamicDataSourceConfig;

    @Override
    public R getTechnologyViewTree() {
        List<TechnologyViewTree> tree = new ArrayList<>();
        List<TechnologyViewTree> treeList = technologyViewTreeMapper.treeList();
        /*查询库  表  列  组装成树形结构*/
        Integer dbId = null;
        TechnologyViewTree node = null;
        for (TechnologyViewTree t : treeList) {
            if (t.getDatabaseId() != dbId) {
                dbId = t.getDatabaseId();
                node = new TechnologyViewTree();
                /*数据库*/
                node.setId(t.getDatabaseId());
                node.setResourceId(t.getDatabaseResid());
                node.setNameCn(t.getDatabaseNameCn());
                node.setNameEn(t.getDatabaseNameEn());
                node.setNodeType(MetaModuleConst.DATABASE_MODULE_TYPE);
                node.setConnectIp(t.getConnectIp());
                node.setConnectHost(t.getConnectHost());
                node.setConnectUser(t.getConnectUser());
                node.setConnectPassword(t.getConnectPassword());
                node.setDatabaseType(t.getDatabaseType());
                tree.add(node);
            }
            //表
            t.setId(t.getTableId());
            t.setParentId(t.getDatabaseId());
            t.setNameEn(t.getTableNameEn());
            t.setNameCn(t.getTableNameCn());
            t.setResourceId(t.getTableResid());
            t.setNodeType(MetaModuleConst.TABLE_MODULE_TYPE);
            /*表装入库*/
            node.getChildren().add(t);
        }
        return new R(tree);
    }

    @Override
    public R getTableInfoByTreeNode(Page<Column> page, TechnologyViewTree tree, String queryString) {
        IPage<Column> columnList = technologyViewTreeMapper.getTableColumnList(page, tree.getId(), queryString);
        return new R(columnList);
    }

    @Override
    public R getTableListByTreeNode(Page<Table> page, TechnologyViewTree tree) {
        Table table = new Table();
        table.setDatabaseId(tree.getId());
        Wrapper wrapper = Wrappers.query(table);
        return new R(technologyTableMapper.selectPage(page, wrapper));
    }

    @Override
    public R getDatabase(Page<Database> page, Database database) {
        return new R(technologyDatabaseMapper.selectPage(page, Wrappers.query(database)));
    }

    @Override
    public R getColumn(Page<Table> page, Table table) {
        return new R(technologyTableMapper.selectPage(page, Wrappers.query(table)));
    }

    @Override
    public R getTechnologyViewNodeInfo(String resourceId, String type) {
        try {
            switch (type) {
                case "database":
                    Database database = new Database();
                    database.setResourceId(resourceId);
                    Wrapper wrapperDatabase = Wrappers.query(database);
                    return new R(technologyDatabaseMapper.selectOne(wrapperDatabase));
                case "table":
                    Table table = new Table();
                    table.setResourceId(resourceId);
                    Wrapper wrapperTable = Wrappers.query(table);
                    return new R(technologyTableMapper.selectOne(wrapperTable));
                case "column":
                    Column column = new Column();
                    column.setResourceId(resourceId);
                    Wrapper wrapperColumn = Wrappers.query(column);
                    return new R(technologyColumnMapper.selectOne(wrapperColumn));
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public R getTechnologyColumnList(Page<TechnologyViewTree> page, String queryString, List<Integer> elementIds) {
        return new R(technologyViewTreeMapper.getTechnologyColumnList(page, queryString, elementIds));
    }


    @Override
    public R getDataTableList(FromVo fromVo) {
        try {
            return new R(this.selectDataTableList(fromVo));
        } catch (Exception e) {
            return new R().setCode(1);
        }
    }

    private List<TableVo> selectDataTableList(FromVo fromVo) {
        DynamicDataSourceContextHolder.setDataSourceType(fromVo.getDatabaseId());
        List<Table> tableList = new ArrayList<>();
        try {
            if ("gp".equals(fromVo.getType())) {
                tableList = technologyViewTreeMapper.getDataGPTableList(fromVo.getTableName());
            } else if ("hive".equals(fromVo.getType())) {
                tableList = technologyViewTreeMapper.getDataHiveTableList(fromVo.getDbName());
            } else {
                tableList = technologyViewTreeMapper.getDataTableList(fromVo.getTableName());
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());

        } finally {
            DynamicDataSourceContextHolder.clearDataSourceType();
        }


        List<TableVo> tables = new ArrayList<>();
        //查询库是否导入过
        Integer count = technologyTableMapper.selectCount(Wrappers.<Table>query().lambda().eq(Table::getDatabaseId, fromVo.getDatabaseId()));
        //比较表下的字段和导入字段是否有变更
        if (tableList != null && tableList.size() > 0 && count > 0) {
            List<TableVo> finalTables = tables;
            tableList.stream().forEach(t -> {
                fromVo.setTableName(t.getNameEn());
                Table table = technologyTableMapper.selectOne(Wrappers.<Table>query().lambda().eq(Table::getNameEn, t.getNameEn()).eq(Table::getDatabaseId, fromVo.getDatabaseId()));
                TableVo tableVo = new TableVo();
                BeanUtil.copyProperties(t, tableVo);
                if (table != null) {
                    tableVo.setId(table.getId());
                    List<Column> oldColList = technologyColumnMapper.selectList(Wrappers.<Column>query().lambda().eq(Column::getTableId, table.getId()));

                    fromVo.setTableName(t.getNameEn());
                    List<Column> columnList = selectTableColumnList(fromVo);
                    //判断下字段数量即可
                    if (oldColList != null && columnList != null && oldColList.size() < columnList.size()) {
                        tableVo.setChangeType(true);
                        List<Column> changeColList = new ArrayList<>();
                        columnList.stream().forEach(o -> {
                            if (oldColList.stream().anyMatch(col -> col.getNameEn().equalsIgnoreCase(o.getNameEn())) == false) {
                                changeColList.add(o);
                            }
                        });
                        tableVo.setChangeColList(changeColList);
                        finalTables.add(tableVo);
                    }

                } else {
                    finalTables.add(tableVo);
                }
            });
        } else {
            tables = tableList.stream().map(t -> {
                TableVo tableVo = new TableVo();
                BeanUtil.copyProperties(t, tableVo);
                return tableVo;
            }).collect(Collectors.toList());
        }

        return tables;
    }

    @Override
    public R getTableColumnList(FromVo fromVo) {
        return new R(this.selectTableColumnList(fromVo));
    }

    private List<Column> selectTableColumnList(FromVo fromVo) {
        DynamicDataSourceContextHolder.setDataSourceType(fromVo.getDatabaseId());
        List<Column> columnList = new ArrayList<>();
        try {

            if ("gp".equals(fromVo.getType())) {
                columnList = technologyViewTreeMapper.getDataGPColumnList(fromVo.getTableName());
            } else if ("hive".equals(fromVo.getType())) {
                columnList = technologyViewTreeMapper.getDataHiveColumnList(fromVo.getDbName(), fromVo.getTableName());
            } else {
                columnList = technologyViewTreeMapper.getDataColumnList(fromVo.getTableName());
            }
        } catch (Exception e) {
        } finally {
            DynamicDataSourceContextHolder.clearDataSourceType();
        }

        return columnList;
    }

    /**
     * 创建数据库
     *
     * @param database
     * @return
     */
    public R saveDataBase(Database database) {
        database.setResourceId(UUID.randomUUID().toString());
        technologyDatabaseMapper.insert(database);
        return new R(dynamicDataSourceConfig.reload());
    }

    /**
     * 修改数据库
     *
     * @param database
     * @return
     */
    public R updateDataBase(Database database) {
        technologyDatabaseMapper.updateById(database);
        return new R(dynamicDataSourceConfig.reload());
    }

    /**
     * 创建数据表
     *
     * @param tables
     * @return
     */
    public R saveDataTable(List<Table> tables) {
        if (tables != null && tables.size() > 0) {
            tables.stream().forEach(t -> {
                t.setResourceId(UUID.randomUUID().toString());
                technologyTableMapper.insert(t);
            });
        }
        return new R();
    }

    /**
     * 创建数据表
     *
     * @param tables
     * @return
     */
    public R updateDataTable(List<Table> tables) {
        if (tables != null && tables.size() > 0) {
            tables.stream().forEach(t -> technologyTableMapper.updateById(t));
        }
        return new R();
    }

    /**
     * 创建数据字段
     *
     * @param columns
     * @return
     */
    public R saveDataColumn(List<Column> columns) {
        if (columns != null && columns.size() > 0) {
            columns.stream().forEach(c -> {
                c.setResourceId(UUID.randomUUID().toString());
                technologyColumnMapper.insert(c);
            });
        }
        return new R();
    }

    /**
     * 修改数据字段
     *
     * @param columns
     * @return
     */
    public R updateDataColumn(List<Column> columns) {
        if (columns != null && columns.size() > 0) {
            columns.stream().forEach(c -> technologyColumnMapper.updateById(c));
        }
        return new R();
    }


    @Override
//    @Transactional
    public R importDataBase(FromVo fromVo) {

        if (fromVo.getTableList() != null && fromVo.getTableList().size() > 0) {
            saveToTable(fromVo, fromVo.getTableList());
        }

        if (fromVo.getChangeList() != null && fromVo.getChangeList().size() > 0) {
            fromVo.getChangeList().stream().forEach(t -> {
                List<Column> oldColList = technologyColumnMapper.selectList(Wrappers.<Column>query().lambda().eq(Column::getTableId, t.getId()));
                fromVo.setTableName(t.getNameEn());
                List<Column> columnList = selectTableColumnList(fromVo);
                if (columnList != null && columnList.size() > 0) {
                    if (oldColList != null && oldColList.size() > 0) {
                        columnList.stream().forEach(c -> {
                            if (oldColList.stream().anyMatch(o -> o.getNameEn().equalsIgnoreCase(c.getNameEn())) == false) {
                                c.setResourceId(UUID.randomUUID().toString());
                                c.setTableId(t.getId());
                                technologyColumnMapper.insert(c);
                            }
                        });
                    } else {
                        columnList.stream().forEach(c -> c.setTableId(t.getId()));
                        this.saveDataColumn(columnList);
                    }
                }
            });

        }

//        if (fromVo.getImportType() == 1) { //全量数据表
//            //根据数据库id，类型，名称查询数据库下的所有表
//            List<TableVo> tableList = selectDataTableList(fromVo);
//            saveToTable(fromVo, tableList);
//            //查询表下的所有字段
//        } else if (fromVo.getImportType() == 2) { //全量字段
//            Table table = technologyTableMapper.selectById(fromVo.getTableId());
//            List<Column> columnList = selectTableColumnList(fromVo);
//            saveTableColumnList(table, columnList);
//        } else if (fromVo.getImportType() == 3) { //增量创建表
//            List<TableVo> tableList = fromVo.getTableList();
//            saveToTable(fromVo, tableList);
//        } else if (fromVo.getImportType() == 4) { //增量创建字段
//            Table table = technologyTableMapper.selectById(fromVo.getTableId());
//            //查询已有字段
//
//            List<Column> columnList = fromVo.getColumnList();
//            saveTableColumnList(table, columnList);
//        }
        return new R();
    }


    /**
     * 导入表和字段信息
     *
     * @param fromVo    参数
     * @param tableList 表集和
     */
    private void saveToTable(FromVo fromVo, List<TableVo> tableList) {
        if (tableList != null && tableList.size() > 0) {
            tableList.stream().forEach(t -> {
                t.setDatabaseId(fromVo.getDatabaseId());
                t.setResourceId(UUID.randomUUID().toString());
                Table table = new Table();
                BeanUtil.copyProperties(t, table);
                technologyTableMapper.insert(table);
                //查询表下的字段
                fromVo.setTableName(t.getNameEn());
                List<Column> columnList = selectTableColumnList(fromVo);
                saveTableColumnList(table, columnList);
            });
        }
    }

    /**
     * 新增表下的字段
     *
     * @param table   表
     * @param columns 字段集和
     */
    private void saveTableColumnList(Table table, List<Column> columns) {
        if (columns != null && columns.size() > 0) {
            columns.stream().forEach(c -> {
                c.setResourceId(UUID.randomUUID().toString());
                c.setTableId(table.getId());
                technologyColumnMapper.insert(c);
            });

        }
    }
}

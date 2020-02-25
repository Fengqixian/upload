package com.clinbrain.bd.mdm.MetadataManage.util.lineageUtil;

import com.clinbrain.bd.mdm.MetadataManage.entity.LineageMetaValue;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageNode;
import com.clinbrain.bd.mdm.MetadataManage.util.cost.MetaModuleConst;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

public class ColumnLineageParserExecutorHX extends AbstractLineageParserExecutor<LineageMetaValue> {
    public final static LineageMetaValue SHUTDOWN_COMMAND = new LineageMetaValue();

    @Override
    LineageMetaValue accept() {
        try {
            return enviroment.lineageColumns.take();
        } catch (InterruptedException e) {
            LOGGER.error("获取任务发生错误。", e);
        }
        return null;
    }

    /**
     * column解析逻辑(单向分析)
     * 找到由它生成的元素  放入items
     * 找到它是由哪个元素生成的 放入items
     * 找到他的所属表，根据所属表找到他的ETL  放入etls
     *
     * @param lineageValue
     * @throws Exception
     */
    @Override
    void doParse(LineageMetaValue lineageValue) throws Exception {
        if (!MetaModuleConst.COLUMN_MODULE_TYPE.equalsIgnoreCase(lineageValue.getDataType())) return;
        enviroment.parsedObjects.add(lineageValue.getResourceId());

        //enviroment.getNode().add(transferMetadataValue2Node(lineageValue));

        //找到与字段有关的数据元
//        List<LineageMetaValue> lineageItems = enviroment.metaDataLineageService.getColumnSourceItems(lineageValue);
//        lineageItems.stream().forEach(o ->{
//            //添加link from  o --> metadatavalue
//            if(!enviroment.parsedObjects.contains(o.getResourceId())){
//                enviroment.lineageItems.add(o);
//            }
//        });

        List<LineageMetaValue> lineageTables = new ArrayList<>();
        try {
            //找到与字段有关的表
            lineageTables = enviroment.metaDataLineageService.getColumnSourceTables(lineageValue);
            lineageTables.stream().forEach(o -> {

                LineageNode tableNode = transferMetadataValue2Node(o);
                if (!enviroment.getNode().contains(tableNode)) {
                    tableNode.setLevel("0");
                    tableNode.setChildren(transferMetadataValue2Node(lineageValue));
                    enviroment.getNode().add(tableNode);
                }else{
                    for(LineageNode n : enviroment.getNode()){
                        if(n.equals(tableNode)){
                            n.setLevel("0");
                            n.setChildren(transferMetadataValue2Node(lineageValue));
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        //找到字段相关表的ETL关系
        List<LineageMetaValue> etlTables = new ArrayList<>();
        List<LineageMetaValue> sourceColumns = new ArrayList<>();
        sourceColumns.add(lineageValue);
        lineageTables.stream().forEach(o -> {
            //循环字段相关的每一个表
            loopEtlTables(o, etlTables,sourceColumns,0);
        });
    }

    /**
     * 解析表的ETL关系
     *
     * @param targetTable 目标表
     * @param tables
     * @return
     * @author yjt
     * @date 2019/10/25 15:03
     */
    private void loopEtlTables(LineageMetaValue targetTable, List<LineageMetaValue> tables,List<LineageMetaValue> sourceColumns,Integer level) {
        try {
            List<LineageMetaValue> lineageEtlTables = enviroment.metaDataLineageService.getTableEtlSourceTables(targetTable);

            if (lineageEtlTables == null || lineageEtlTables.size() < 1)
                return;
            enviroment.getLink().addAll(createETLLinks(lineageEtlTables, targetTable));
            ++level;

            //从ETL详情表中查找，该字段来源于哪些字段
            List<LineageMetaValue> sourceEtlColumns = enviroment.metaDataLineageService.getColumnEtlSourceColumns(sourceColumns);

            for (LineageMetaValue table : lineageEtlTables) {
                LineageNode tableNode = transferMetadataValue2Node(table);
                tableNode.setLevel(level.toString());
                //从所有来源表中找到属于当前表的
                List<LineageMetaValue> columns = new ArrayList<>();
                for(LineageMetaValue column : sourceEtlColumns){
                    if(column.getTableId()!=null&&column.getTableId().equals(table.getId())){
                        columns.add(column);
                        tableNode.setChildren(transferMetadataValue2Node(column));
                    }
                }

                if (!enviroment.getNode().contains(tableNode)) {
                    enviroment.getNode().add(tableNode);
                }
                if (!tables.contains(table)) {
                    tables.add(table);
                }
                loopEtlTables(table, tables,columns,level);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<LineageNode> getEtlSourceColumns(LineageMetaValue targetColumn){
        return null;
    }

    @Override
    boolean isEndNode(LineageMetaValue lineageValue) {
        return lineageValue == SHUTDOWN_COMMAND;
    }

    @Override
    protected LinkedTransferQueue<LineageMetaValue> getTaskQueue() {
        return enviroment.lineageColumns;
    }}

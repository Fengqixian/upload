package com.clinbrain.bd.mdm.MetadataManage.util.lineageUtil;

import com.clinbrain.bd.mdm.MetadataManage.entity.LineageMetaValue;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageNode;
import com.clinbrain.bd.mdm.MetadataManage.util.cost.MetaModuleConst;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

public class ColumnLineageParserExecutor  extends AbstractLineageParserExecutor<LineageMetaValue> {
    public final static LineageMetaValue SHUTDOWN_COMMAND = new LineageMetaValue();
    @Override
    LineageMetaValue accept() {
        try {
            return enviroment.lineageColumns.take();
        } catch (InterruptedException e) {
            LOGGER.error("获取任务发生错误。",e);
        }
        return null;
    }

    /**
     * column解析逻辑(单向分析)
     * 找到由它生成的元素  放入items
     * 找到它是由哪个元素生成的 放入items
     * 找到他的所属表，根据所属表找到他的ETL  放入etls
     * @param lineageValue
     * @throws Exception
     */
    @Override
    void doParse(LineageMetaValue lineageValue) throws Exception {
        if(!MetaModuleConst.COLUMN_MODULE_TYPE.equalsIgnoreCase(lineageValue.getDataType())) return;
        enviroment.parsedObjects.add(lineageValue.getResourceId());
        enviroment.getNode().add(transferMetadataValue2Node(lineageValue));

        //找到与字段有关的数据元
//        List<LineageMetaValue> lineageValues = enviroment.metaDataLineageService.getColumnSourceItems(lineageValue);
//        enviroment.getLink().addAll(createLinks(lineageValues,lineageValue));
//        lineageValues.stream().forEach(o ->{
//            //添加link from  o --> metadatavalue
//            if(!enviroment.parsedObjects.contains(o.getResourceId())){
//                enviroment.lineageItems.add(o);
//            }
//        });

        List<LineageMetaValue> lineageValues = new ArrayList<>();
        try {
            //找到与字段有关的表
            lineageValues = enviroment.metaDataLineageService.getColumnSourceTables(lineageValue);
            enviroment.getLink().addAll(createLinks(lineageValues, lineageValue));
            lineageValues.stream().forEach(o -> {

                lineageValue.setParentResourceId(o.getResourceId());
                LineageNode tableNode = transferMetadataValue2Node(o);
                if (!enviroment.getNode().contains(tableNode)) {
                    enviroment.getNode().add(tableNode);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        //找到字段相关表的ETL关系
        List<LineageMetaValue> etlTables = new ArrayList<>();
        lineageValues.stream().forEach(o ->{
            //循环字段相关的每一个表
            loopEtlTables(o,etlTables);
        });
    }

    /**
      * 解析表的ETL关系
      * @param etlTable
      * @param tables  
      * @return 
      * @author yjt
      * @date  2019/10/25 15:03 
     */
    private void loopEtlTables(LineageMetaValue etlTable, List<LineageMetaValue> tables) {
        try{
            List<LineageMetaValue> lineageEtlTables = enviroment.metaDataLineageService.getTableEtlSourceTables(etlTable);

            if(lineageEtlTables==null||lineageEtlTables.size()<1)
                return;
            enviroment.getLink().addAll(createETLLinks(lineageEtlTables,etlTable));
            for(LineageMetaValue table : lineageEtlTables){
                LineageNode tableNode = transferMetadataValue2Node(table);
                if(!enviroment.getNode().contains(tableNode)){
                    enviroment.getNode().add(tableNode);
                }
                if(!tables.contains(table)){
                    tables.add(table);
                }
                loopEtlTables(table,tables);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    boolean isEndNode(LineageMetaValue lineageValue) {
        return lineageValue==SHUTDOWN_COMMAND;
    }

    @Override
    protected LinkedTransferQueue<LineageMetaValue> getTaskQueue() {
        return enviroment.lineageColumns;
    }
}

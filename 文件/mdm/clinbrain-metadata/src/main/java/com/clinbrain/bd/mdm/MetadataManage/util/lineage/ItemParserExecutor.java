package com.clinbrain.bd.mdm.MetadataManage.util.lineage;

import com.clinbrain.bd.mdm.MetadataManage.entity.LineageNode;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.bd.mdm.MetadataManage.util.cost.MetaModuleConst;

import java.util.List;
import java.util.concurrent.LinkedTransferQueue;
import java.util.stream.Collectors;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.mdm.MetadataManage.util.lineage.EtlParserExecutor
 * @createdDate 2019/7/22 14:43
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
public class ItemParserExecutor extends AbstractParserExecutor<MetadataValue> {
    public final static MetadataValue SHUTDOWN_COMMAND = new MetadataValue();
    @Override
    MetadataValue accept() {
        try {
            return enviroment.items.take();
        } catch (InterruptedException e) {
            LOGGER.error("获取任务发生错误。",e);
        }
        return null;
    }

    /**
     * 元素项解析逻辑
     * 找到生成它的字段
     * 找到由它生成的字段
     * @param metadataValue
     * @throws Exception
     */
    @Override
    void doParse(MetadataValue metadataValue) throws Exception {
        if(!MetaModuleConst.ITEM_MODULE_TYPE.equalsIgnoreCase(metadataValue.getModelType())&&!MetaModuleConst.SET_MODULE_TYPE.equalsIgnoreCase(metadataValue.getModelType())) return;
        enviroment.parsedObjects.add(metadataValue.getResourceId());
        enviroment.getNode().add(transferMetadataValue2Node(metadataValue));
        //TODO 找到生成它的字段 就是关系表中 target_id 为自己的
        metadataValue.setModelType(MetaModuleConst.REF_COLUMN_ITEM_TYPE);
        List<MetadataValue> metadataValues = enviroment.dataLineageService.getColumnSourceItems(metadataValue);
        metadataValue.setModelType(MetaModuleConst.ITEM_MODULE_TYPE);
        enviroment.getLink().addAll(createLinks(metadataValues,metadataValue));
        metadataValues.stream().forEach(o ->{
            if(!enviroment.parsedObjects.contains(o.getResourceId())){
                enviroment.columns.add(o);
            }
        });

        //TODO 找到它的上级 就是关系表中 source_id 为自己的  REF_ITEM_ITEM_TYPE
        metadataValue.setModelType(MetaModuleConst.REF_ITEM_ITEM_TYPE);
        metadataValues = enviroment.dataLineageService.getParentItems(metadataValue);
        metadataValue.setModelType(MetaModuleConst.ITEM_MODULE_TYPE);
        /*必须是没有解析过的才进行link添加*/
        metadataValues.stream().filter(e ->!enviroment.parsedObjects.contains(e.getResourceId()))
                .forEach(e ->{
                    enviroment.getLink().add(createLinks(metadataValue,e));
                    enviroment.items.add(e);
                });
        //TODO 找到它的下级 就是关系表中 target_id 为自己的  REF_ITEM_ITEM_TYPE
        metadataValue.setModelType(MetaModuleConst.REF_ITEM_ITEM_TYPE);
        metadataValues = enviroment.dataLineageService.getColumnSourceItems(metadataValue);
        metadataValue.setModelType(MetaModuleConst.ITEM_MODULE_TYPE);
        /*必须是没有解析过的才进行link添加*/
        metadataValues.stream().filter(e ->!enviroment.parsedObjects.contains(e.getResourceId()))
                .forEach(e ->{
                    enviroment.getLink().add(createLinks(e,metadataValue));
                    enviroment.items.add(e);
                });
        //TODO 找到由它生成的字段 （暂时不用找）
        /*if(!enviroment.parsedObjects.contains("")){
            enviroment.etls.transfer(null);
        }*/
    }

    @Override
    boolean isEndNode(MetadataValue metadataValue) {
        return metadataValue==SHUTDOWN_COMMAND;
    }

    @Override
    protected LinkedTransferQueue<MetadataValue> getTaskQueue() {
        return enviroment.items;
    }
}

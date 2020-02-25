package com.clinbrain.bd.mdm.MetadataManage.util.lineage;

import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.bd.mdm.MetadataManage.util.cost.MetaModuleConst;

import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.mdm.MetadataManage.util.lineage.EtlParserExecutor
 * @createdDate 2019/7/22 14:43
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
public class ColumnParserExecutor extends AbstractParserExecutor<MetadataValue> {
    public final static MetadataValue SHUTDOWN_COMMAND = new MetadataValue();
    @Override
    MetadataValue accept() {
        try {
            return enviroment.columns.take();
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
     * @param metadataValue
     * @throws Exception
     */
    @Override
    void doParse(MetadataValue metadataValue) throws Exception {
        if(!MetaModuleConst.COLUMN_MODULE_TYPE.equalsIgnoreCase(metadataValue.getModelType())) return;
        enviroment.parsedObjects.add(metadataValue.getResourceId());
        enviroment.getNode().add(transferMetadataValue2Node(metadataValue));
        //TODO  找到由它生成的元素  放入items(已经解析过的直接跳过)
        /*metadataValue.setModelType(MetaModuleConst.REF_COLUMN_ITEM_TYPE);
        List<MetadataValue> metadataValues = enviroment.dataLineageService.getParentItems(metadataValue);
        metadataValue.setModelType(MetaModuleConst.COLUMN_MODULE_TYPE);
        enviroment.getLink().addAll(createLinks(metadataValue,metadataValues));
        metadataValues.stream().forEach(o ->{
            if(!enviroment.parsedObjects.contains(o.getResourceId())){
                enviroment.items.add(o);
            }
        });*/

        //TODO  找到它是由哪个元素生成的 放入items(已经解析过的直接跳过) 即关系表中target_id 为自己的ref_type 为 ITEM_COLUMN
        metadataValue.setModelType(MetaModuleConst.REF_ITEM_COLUMN_TYPE);
        List<MetadataValue>  metadataValues = enviroment.dataLineageService.getColumnSourceItems(metadataValue);
        metadataValue.setModelType(MetaModuleConst.COLUMN_MODULE_TYPE);
        enviroment.getLink().addAll(createLinks(metadataValues,metadataValue));
        metadataValues.stream().forEach(o ->{
            //添加link from  o --> metadatavalue
            if(!enviroment.parsedObjects.contains(o.getResourceId())){
                enviroment.items.add(o);
            }
        });

        //TODO  找到他的所属表，根据所属表找到他的ETL  放入etls(已经解析过的直接跳过) 即value表中etl的output为自己的parentid
        metadataValues = enviroment.dataLineageService.getColumnTransferEtl(metadataValue);
        metadataValues.stream().forEach(o ->{
            if(!enviroment.parsedObjects.contains(o.getResourceId())){
                enviroment.etls.add(o);
            }
        });
    }

    @Override
    boolean isEndNode(MetadataValue metadataValue) {
        return metadataValue==SHUTDOWN_COMMAND;
    }

    @Override
    protected LinkedTransferQueue<MetadataValue> getTaskQueue() {
        return enviroment.columns;
    }
}

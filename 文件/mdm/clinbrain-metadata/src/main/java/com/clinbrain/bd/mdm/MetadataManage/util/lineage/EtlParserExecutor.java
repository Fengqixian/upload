package com.clinbrain.bd.mdm.MetadataManage.util.lineage;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageLink;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataModel;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataProperties;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.bd.mdm.MetadataManage.util.cost.MetaModuleConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedTransferQueue;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.mdm.MetadataManage.util.lineage.EtlParserExecutor
 * @createdDate 2019/7/22 14:43
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
public class EtlParserExecutor extends AbstractParserExecutor<MetadataValue> {
    /*固定属性定义*/
    public static final String ETL_INPUTS = "inputs";
    public static final String ETL_OUTPUT = "output";
    public static final String ETL_EXECUTE_SQL = "execute_sql";
    public static final String ETL_SQL_TYPE = "sql_type";
    public static final String ETL_SOURCE_DATABASE = "source_database";
    public static final String ETL_TARGET_DATABASE = "target_database";
    public static final String ETL_COLUMN_MAPPING = "column_mapping";

    public String etlInputs = "";
    public String etlOutput = "";
    public String etlExecuteSql = "";
    public String etlSqlType = "";
    public String etlSourceDatabase = "";
    public String etlTargetDatabase = "";
    public String etlColumnMapping = "";


    public final static MetadataValue SHUTDOWN_COMMAND = new MetadataValue();
    //模型信息
    private MetadataModel metadataModel ;
    private List<MetadataProperties> properties ;
    @Override
    MetadataValue accept() {
        try {
            return enviroment.etls.take();
        } catch (InterruptedException e) {
            LOGGER.error("获取任务发生错误。",e);
        }
        return null;
    }

    /**
     * ETL解析逻辑
     * 取出ETL的源，目标，sql
     * 解析sql中的字段对应关系
     * @param metadataValue
     * @throws Exception
     */
    @Override
    void doParse(MetadataValue metadataValue) throws Exception {
        if(!MetaModuleConst.ETL_MODULE_TYPE.equalsIgnoreCase(metadataValue.getModelType())) return;
        enviroment.parsedObjects.add(metadataValue.getResourceId());
        if(metadataModel == null){
            metadataModel = enviroment.dataLineageService.getMetadataModel(metadataValue);
            properties = enviroment.dataLineageService.getMetadataProperties(metadataValue);
            properties.stream().forEach(e -> {
                if(ETL_EXECUTE_SQL.equalsIgnoreCase(e.getNameEn())){
                    etlExecuteSql = e.getMappingField();
                }
                if(ETL_INPUTS.equalsIgnoreCase(e.getNameEn())){
                    etlInputs = e.getMappingField();
                }
                if(ETL_OUTPUT.equalsIgnoreCase(e.getNameEn())){
                    etlOutput = e.getMappingField();
                }
                if(ETL_SOURCE_DATABASE.equalsIgnoreCase(e.getNameEn())){
                    etlSourceDatabase = e.getMappingField();
                }
                if(ETL_SQL_TYPE.equalsIgnoreCase(e.getNameEn())){
                    etlSqlType = e.getMappingField();
                }
                if(ETL_TARGET_DATABASE.equalsIgnoreCase(e.getNameEn())){
                    etlTargetDatabase = e.getMappingField();
                }
                if(ETL_COLUMN_MAPPING.equalsIgnoreCase(e.getNameEn())){
                    etlColumnMapping = e.getMappingField();
                }
            });
        }
        //取出源，目标源目标全部存储uuid 找出该etl对应的字段对应关系，ETL已经解析到位
        Map<String,Object> metadataValueMap = BeanUtil.beanToMap(metadataValue,false,true);
        String mappings = (String) metadataValueMap.get(etlColumnMapping);
        String[] mapping = StringUtils.split(mappings,"|");
        String[] inColumns = StringUtils.split(mapping[1],",");
        String[] outColumns = StringUtils.split(mapping[0],",");
        //产生连线,加入环境(只加入相关的)
        for(int i= 0;i<outColumns.length;i++){
            LineageLink lineageLink = new LineageLink();
            lineageLink.setComment(metadataValue.getNameEn());
            lineageLink.setFrom(inColumns[i]);
            lineageLink.setTo(outColumns[i]);
            lineageLink.setType(MetaModuleConst.ETL_MODULE_TYPE);
            enviroment.getLink().add(lineageLink);
        }
        //找到输入字段之后将输入字段查询出来生成字段解析任务
        String  inColumnString= StringUtils.join(inColumns,",");
        //一次性查出
        List<MetadataValue> metadataValues = enviroment.dataLineageService.getMetadataValueByIds(inColumnString);
        //添加连线，连线类型为etl
        //enviroment.getLink().addAll(createLinks(metadataValue,metadataValues));
        if(!metadataValues.isEmpty()){
            metadataValues.stream().forEach(e ->{
                if(!enviroment.parsedObjects.contains(e.getResourceId()))
                    enviroment.columns.add(e);
            });
        }
    }

    @Override
    boolean isEndNode(MetadataValue metadataValue) {
        return metadataValue==SHUTDOWN_COMMAND;
    }

    @Override
    protected LinkedTransferQueue<MetadataValue> getTaskQueue() {
        return enviroment.etls;
    }
}

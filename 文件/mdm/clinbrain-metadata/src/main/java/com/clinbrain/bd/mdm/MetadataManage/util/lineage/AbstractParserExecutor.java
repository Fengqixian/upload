package com.clinbrain.bd.mdm.MetadataManage.util.lineage;

import com.clinbrain.bd.mdm.MetadataManage.entity.LineageLink;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageNode;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.bd.mdm.MetadataManage.util.cost.MetaModuleConst;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;
import java.util.stream.Collectors;

/**
 * 解析流程
 * *启动一个线程开始
 * 1.检查环境是否有待解析任务（更新为同步阻塞队列，无需检查）
 * 2.申请一个对象来解析
 *
 * @param <T>
 */
public abstract class AbstractParserExecutor<T> extends Thread{
    protected final static Logger LOGGER = LoggerFactory.getLogger(AbstractParserExecutor.class);
    /**
     * 解析器的运行环境
     */
    protected volatile ParseEnviroment enviroment;
    /**
     * 解析器当前解析对象
     */
    protected T parsingObject;

    public void init(ParseEnviroment enviroment,String name){
        this.setName(name);
        this.enviroment = enviroment;
    }
    protected boolean hasTask(){
        return true;
    }
    /**
     * 从环境中申请一个对象来解析
     * @return
     */
    abstract T accept();

    /**
     * 解析
     */
    abstract void doParse(T t) throws Exception;

    /**
     * 开启线程开始解析流程
     * 每一轮循环首先申请对象 如果为空，报告一个等待
     */
    @Override
    public void run() {
        while (true){
            //监控点--
            T t =null;
            enviroment.parserWaitingCount.incrementAndGet();
            try{
                t =accept();//会阻塞
            }finally {
                enviroment.parserWaitingCount.decrementAndGet();
            }
            /*拿到对象就更改监控*/
            if(isEndNode(t)){
                /*还要在放回去*/
                getTaskQueue().add(t);
                break;
            }
            parsingObject = t;
            try {
                doParse(t);
            } catch (Exception e) {
                LOGGER.error("解析对象发生错误。。",e);
            }
            doParseEnd();
        }
        LOGGER.info("解析任务结束。");
    }

    abstract boolean isEndNode(T t);

    /**
     * 解析完成后的函数
     * @return
     */
    protected T doParseEnd(){
        return parsingObject;
    }

    /**
     * 添加顶点
     */
    protected void addNode(){

    }

    /**
     * 添加边
     */
    protected void addLink(){

    }
    protected  LineageNode transferMetadataValue2Node(MetadataValue metadataValue) throws Exception{
        LineageNode lineageNode = new LineageNode();
        lineageNode.setId(metadataValue.getResourceId());
        lineageNode.setName(metadataValue.getNameCn());
        if(metadataValue.getNameCn() == null){
            lineageNode.setName(metadataValue.getNameEn());
        }
        lineageNode.setType(metadataValue.getModelType());
        lineageNode.setModuleId(metadataValue.getModelId());
        lineageNode.setParentId(metadataValue.getParentId());
        return lineageNode;
    }
    /*如果是to column 就要改为to表*/
    protected List<LineageLink> createLinks(MetadataValue from,List<MetadataValue> tos){
        return tos.stream().map(o ->{
            LineageLink lineageLink = new LineageLink();
            lineageLink.setFrom(from.getResourceId());
            lineageLink.setTo(o.getResourceId());
            lineageLink.setType(from.getModelType()+"_"+o.getModelType());
            return lineageLink;
        }).collect(Collectors.toList());

    }
/*
    protected List<LineageLink> createLinks(MetadataValue from,List<MetadataValue> tos){
        List<LineageLink> tableToColumn = new ArrayList<>();
        List<LineageLink> list = tos.stream().map(o ->{
            LineageLink lineageLink = new LineageLink();
            lineageLink.setFrom(from.getResourceId());
            lineageLink.setTo(o.getResourceId());
            lineageLink.setType(from.getModelType()+"_"+o.getModelType());
            if(MetaModuleConst.COLUMN_MODULE_TYPE.equalsIgnoreCase(o.getModelType())&&!MetaModuleConst.COLUMN_MODULE_TYPE.equalsIgnoreCase(from.getModelType())){
                lineageLink.setTo(o.getParentId());
                lineageLink.setType(from.getModelType()+"_"+MetaModuleConst.TABLE_MODULE_ID);
                */
/*添加表到列*//*

                LineageLink lineageLink1 = new LineageLink();
                lineageLink1.setFrom(o.getParentId());
                lineageLink1.setTo(o.getResourceId());
                lineageLink1.setType(MetaModuleConst.TABLE_MODULE_TYPE+"_"+o.getModelType());
                tableToColumn.add(lineageLink1);
            }
            return lineageLink;
        }).collect(Collectors.toList());
        tableToColumn.addAll(list);
        return tableToColumn;
    }
*/
    /*如果是只要是to column 就要改为to表*/
    protected List<LineageLink> createLinks(List<MetadataValue> froms,MetadataValue to){
        return froms.stream().map(o ->{
            LineageLink lineageLink = new LineageLink();
            lineageLink.setFrom(o.getResourceId());
            lineageLink.setTo(to.getResourceId());
            lineageLink.setType(o.getModelType()+"_"+to.getModelType());
            return lineageLink;
        }).collect(Collectors.toList());
    }
/*
    protected List<LineageLink> createLinks(List<MetadataValue> froms,MetadataValue to){
        List<LineageLink> tableToColumn = new ArrayList<>();
        List<LineageLink> list = froms.stream().map(o ->{
            LineageLink lineageLink = new LineageLink();
            lineageLink.setFrom(o.getResourceId());
            lineageLink.setTo(to.getResourceId());
            lineageLink.setType(o.getModelType()+"_"+to.getModelType());
            if(MetaModuleConst.COLUMN_MODULE_TYPE.equalsIgnoreCase(to.getModelType())&&!MetaModuleConst.COLUMN_MODULE_TYPE.equalsIgnoreCase(o.getModelType())){
                lineageLink.setFrom(o.getParentId());
                lineageLink.setType(MetaModuleConst.TABLE_MODULE_ID+"_"+to.getModelType());
                */
/*添加表到列*//*

                LineageLink lineageLink1 = new LineageLink();
                lineageLink1.setFrom(o.getResourceId());
                lineageLink1.setTo(o.getParentId());
                lineageLink1.setType(o.getModelType()+"_"+ MetaModuleConst.TABLE_MODULE_TYPE);
                tableToColumn.add(lineageLink1);
            }
            return lineageLink;
        }).collect(Collectors.toList());
        tableToColumn.addAll(list);
        return tableToColumn;
    }
*/
    protected LineageLink createLinks(MetadataValue from,MetadataValue to){
        LineageLink lineageLink = new LineageLink();
        lineageLink.setFrom(from.getResourceId());
        lineageLink.setTo(to.getResourceId());
        lineageLink.setType(from.getModelType()+"_"+to.getModelType());
        return lineageLink;
    }
    protected abstract LinkedTransferQueue<T> getTaskQueue();
}

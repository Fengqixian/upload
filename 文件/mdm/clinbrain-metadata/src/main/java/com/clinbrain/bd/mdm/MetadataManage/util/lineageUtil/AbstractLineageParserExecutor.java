package com.clinbrain.bd.mdm.MetadataManage.util.lineageUtil;

import com.clinbrain.bd.mdm.MetadataManage.entity.LineageLink;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageMetaValue;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageNode;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectDataSetCategory;
import com.clinbrain.bd.mdm.MetadataManage.util.lineageUtil.AbstractLineageParserExecutor;
import com.clinbrain.bd.mdm.MetadataManage.util.lineageUtil.ParseLineageEnviroment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.LinkedTransferQueue;
import java.util.stream.Collectors;

public abstract class AbstractLineageParserExecutor <T> extends Thread {
    protected final static Logger LOGGER = LoggerFactory.getLogger(AbstractLineageParserExecutor.class);
    /**
     * 解析器的运行环境
     */
    protected volatile ParseLineageEnviroment enviroment;
    /**
     * 解析器当前解析对象
     */
    protected T parsingObject;

    public void init(ParseLineageEnviroment enviroment,String name){
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
    protected LineageNode transferMetadataValue2Node(LineageMetaValue lineageValue) {
        LineageNode lineageNode = new LineageNode();
        lineageNode.setId(lineageValue.getResourceId());
        lineageNode.setName(lineageValue.getNameCn());
        if(lineageValue.getNameCn() == null){
            lineageNode.setName(lineageValue.getNameEn());
        }

        if(lineageValue.getIsHighlight()!=null&&lineageValue.getIsHighlight()){
            lineageNode.setIsHighlight(true);
        }

        if(lineageValue.getDbName()!=null){
            lineageNode.setDbName(lineageValue.getDbName());
        }else{
            //如果没有所属库，将唯一标识赋值进去，避免后续根据库名分组报错
            lineageNode.setDbName(lineageValue.getResourceId());
        }

        lineageNode.setNameEn(lineageValue.getNameEn());
        lineageNode.setType(lineageValue.getDataType());
        return lineageNode;
    }

    protected  LineageNode transferProjectValue2Node(ProjectDataSetCategory projectInfo){
        if(projectInfo==null)return null;
        LineageNode lineageNode = new LineageNode();
        lineageNode.setId(projectInfo.getResourceId());
        lineageNode.setName(projectInfo.getNameCn());
        if(projectInfo.getNameCn() == null){
            lineageNode.setName(projectInfo.getNameEn());
        }
        lineageNode.setType("project");

        return lineageNode;
    }

    /*如果是to column 就要改为to表*/
    protected List<LineageLink> createLinks(LineageMetaValue from, List<LineageMetaValue> tos){
        return tos.stream().map(o ->{
            LineageLink lineageLink = new LineageLink();
            lineageLink.setFrom(from.getResourceId());
            lineageLink.setTo(o.getResourceId());
            lineageLink.setType(from.getDataType()+"_"+o.getDataType());
            return lineageLink;
        }).collect(Collectors.toList());

    }
    /*如果是只要是to column 就要改为to表*/
    protected List<LineageLink> createLinks(List<LineageMetaValue> froms,LineageMetaValue to){
        return froms.stream().map(o ->{
            LineageLink lineageLink = new LineageLink();
            lineageLink.setFrom(o.getResourceId());
            lineageLink.setTo(to.getResourceId());
            lineageLink.setType(o.getDataType()+"_"+to.getDataType());
            return lineageLink;
        }).collect(Collectors.toList());
    }

    protected List<LineageLink> createETLLinks(List<LineageMetaValue> froms,LineageMetaValue to){
        return froms.stream().map(o ->{
            LineageLink lineageLink = new LineageLink();
            lineageLink.setFrom(o.getResourceId());
            lineageLink.setTo(to.getResourceId());
            lineageLink.setType("ETL");
            if(o.getEtlId()!=null){
                lineageLink.setEtlId(o.getEtlId());
            }
            return lineageLink;
        }).collect(Collectors.toList());
    }

    protected LineageLink createLinks(LineageMetaValue from, LineageMetaValue to){
        LineageLink lineageLink = new LineageLink();
        lineageLink.setFrom(from.getResourceId());
        lineageLink.setTo(to.getResourceId());
        lineageLink.setType(from.getDataType()+"_"+to.getDataType());
        return lineageLink;
    }

    protected List<LineageLink> createLinks(List<LineageNode> froms,LineageNode to){
        return froms.stream().map(o ->{
            LineageLink lineageLink = new LineageLink();
            lineageLink.setFrom(o.getId());
            lineageLink.setTo(to.getId());
            lineageLink.setType(o.getType()+"_"+to.getType());
            return lineageLink;
        }).collect(Collectors.toList());
    }
    protected abstract LinkedTransferQueue<T> getTaskQueue();
}

package com.clinbrain.bd.mdm.MetadataManage.util.lineageUtil;

import com.clinbrain.bd.mdm.MetadataManage.entity.LineageMetaValue;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageNode;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectDataSetCategory;
import com.clinbrain.bd.mdm.MetadataManage.util.cost.MetaModuleConst;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

public class ItemLineageParserExecutorHX extends AbstractLineageParserExecutor<LineageMetaValue> {
    public final static LineageMetaValue SHUTDOWN_COMMAND = new LineageMetaValue();

    @Override
    LineageMetaValue accept() {
        try {
            return enviroment.lineageItems.take();
        } catch (InterruptedException e) {
            LOGGER.error("获取任务发生错误。", e);
        }
        return null;
    }

    /**
     * 元素项解析逻辑
     * 找到与数据元有关系的字段
     * 找到与数据元有关系的数据集
     *
     * @param lineageValue
     * @throws Exception
     */
    @Override
    void doParse(LineageMetaValue lineageValue) throws Exception {
        if (!MetaModuleConst.ITEM_MODULE_TYPE.equalsIgnoreCase(lineageValue.getDataType()) && !MetaModuleConst.SET_MODULE_TYPE.equalsIgnoreCase(lineageValue.getDataType()))
            return;
        enviroment.parsedObjects.add(lineageValue.getResourceId());
        enviroment.getNode().add(transferMetadataValue2Node(lineageValue));
        if("business".equalsIgnoreCase(lineageValue.getViewType())){
            //找到与数据元有关的数据集
            List<LineageMetaValue> lineageValues = enviroment.metaDataLineageService.getItemDataSets(lineageValue);
            /*必须是没有解析过的才进行link添加*/
            lineageValues.stream().filter(e -> !enviroment.parsedObjects.contains(e.getResourceId()))
                    .forEach(e -> {
                        //数据元与数据集的连线不展示
                        //enviroment.getLink().add(createLinks(lineageValue, e));
                        LineageNode itemSetNode = transferMetadataValue2Node(e);
                        if (!enviroment.getNode().contains(itemSetNode)) {
                            enviroment.getNode().add(itemSetNode);
                        }
                    });

            //找到与数据元有关的字段
            lineageValues = enviroment.metaDataLineageService.getItemSourceColumns(lineageValue);

            //enviroment.getLink().addAll(createLinks(lineageValues,lineageValue));
            lineageValues.stream().forEach(o -> {

                //生成据元的直接数字段高亮
                o.setIsHighlight(true);
                //放入字段类进行解析
                if (!enviroment.parsedObjects.contains(o.getResourceId())) {
                    enviroment.lineageColumns.add(o);
                }
            });
        }else if("project".equalsIgnoreCase(lineageValue.getViewType())){
            //工程视图时，查询出工程信息和该数据元在工程中选择的字段
            //查询该数据元所关联的所有工程（数据集）
            List<LineageMetaValue> projects = enviroment.metaDataLineageService.getElementProjectList(lineageValue.getId());
            if(projects!=null){
                projects.stream().filter(e -> !enviroment.parsedObjects.contains(e.getResourceId()))
                        .forEach(e -> {
                            LineageNode projectNode = transferMetadataValue2Node(e);
                            if (!enviroment.getNode().contains(projectNode)) {
                                enviroment.getNode().add(projectNode);
                            }
                        });
            }

            //查出工程节点信息
            ProjectDataSetCategory projectInfo = enviroment.metaDataLineageService.getProjectNodeInfo(lineageValue.getProjectId());
            LineageNode projectNode = transferProjectValue2Node(projectInfo);
            if (!enviroment.getNode().contains(projectNode)&&projectNode!=null) {
                //工程节点
                enviroment.getNode().add(projectNode);
                //添加数据元到工程的连线
                List<LineageNode> nodes = new ArrayList<>();
                nodes.add(transferMetadataValue2Node(lineageValue));
                enviroment.getLink().addAll(createLinks(nodes,projectNode));
            }

            //获取该数据元在此工程内设置的关联字段
            LineageMetaValue refColumn = enviroment.metaDataLineageService.getElementRefColumnInfo(projectInfo.getId(),lineageValue.getId());
            List<LineageMetaValue> columnValues = new ArrayList<>();
            columnValues.add(refColumn);
            enviroment.getLink().addAll(createLinks(columnValues,lineageValue));
            enviroment.lineageColumns.add(refColumn);
        }
    }

    @Override
    boolean isEndNode(LineageMetaValue lineageValue) {
        return lineageValue == SHUTDOWN_COMMAND;
    }

    @Override
    protected LinkedTransferQueue<LineageMetaValue> getTaskQueue() {
        return enviroment.lineageItems;
    }
}

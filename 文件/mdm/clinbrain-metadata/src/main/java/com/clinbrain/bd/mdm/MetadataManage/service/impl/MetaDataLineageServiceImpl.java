package com.clinbrain.bd.mdm.MetadataManage.service.impl;

import com.clinbrain.bd.mdm.MetadataManage.entity.LineageLink;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageMetaValue;
import com.clinbrain.bd.mdm.MetadataManage.entity.LineageNode;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.bd.mdm.MetadataManage.mapper.MetaDataLineageMapper;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.ProjectDataSetCategory;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.entity.RelationView;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.mapper.ProjectDataMapper;
import com.clinbrain.bd.mdm.MetadataManage.projectManage.mapper.ProjectViewMapper;
import com.clinbrain.bd.mdm.MetadataManage.service.MetaDataLineageService;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Table;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.mapper.TechnologyColumnMapper;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.mapper.TechnologyTableMapper;
import com.clinbrain.bd.mdm.MetadataManage.util.cost.MetaModuleConst;
import com.clinbrain.bd.mdm.MetadataManage.util.lineageUtil.*;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.parser.common.entity.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Service
public class MetaDataLineageServiceImpl implements MetaDataLineageService {

    private MetaDataLineageMapper metaDataLineageMapper;

    private ProjectDataMapper projectDataMapper;
    private ProjectViewMapper projectViewMapper;

    private TechnologyTableMapper technologyTableMapper;
    private TechnologyColumnMapper technologyColumnMapper;

    @Override
    public R getDataLineage(LineageMetaValue lineageValue){
        try{
            ParseLineageEnviroment enviroment = new ParseLineageEnviroment(this);

            //根据判断的结果放入相应的任务队列中
            if(MetaModuleConst.COLUMN_MODULE_TYPE.equalsIgnoreCase(lineageValue.getDataType())){
                //字段
                enviroment.getLineageColumns().add(lineageValue);
            }else if(MetaModuleConst.ITEM_MODULE_TYPE.equalsIgnoreCase(lineageValue.getDataType())){
                //数据元
                enviroment.getLineageItems().add(lineageValue);
            }

            //创建解析任务执行器
            enviroment.create(ColumnLineageParserExecutor.class);
            enviroment.create(ColumnLineageParserExecutor.class);
            enviroment.create(ColumnLineageParserExecutor.class);
            enviroment.create(ColumnLineageParserExecutor.class);
            enviroment.create(ColumnLineageParserExecutor.class);
            enviroment.create(ItemLineageParserExecutor.class);
            enviroment.create(ItemLineageParserExecutor.class);
            enviroment.create(ItemLineageParserExecutor.class);
            enviroment.create(ItemLineageParserExecutor.class);
            enviroment.create(ItemLineageParserExecutor.class);
            //启动  开始解析任务
            enviroment.enviromentStart();


            //获取解析结果
            Map<String,Object> resultMap =  new HashMap<String, Object>();
            Set<LineageNode> nodeDataArray = new HashSet<LineageNode>();
            Set<LineageLink> linkDataArray = new HashSet<LineageLink>();
            nodeDataArray.addAll(enviroment.getNode());
            linkDataArray.addAll(enviroment.getLink());
            List<String> tableNodeIds = new ArrayList<>();
            /*将link中无用的去除*/
            List<String> nodeIds = nodeDataArray.stream().map(e ->{
                return e.getId();
            }).collect(Collectors.toList());
            /*from和to的Id集合*/
            List<String> fromToIds = new ArrayList<>();
            /*from和to都存在的link*/
            List<LineageLink> filteredLinks = linkDataArray.stream()
                    .filter(e -> nodeIds.contains(e.getFrom())&&nodeIds.contains(e.getTo()))
                    .map(e ->{
                        fromToIds.add(e.getFrom());
                        fromToIds.add(e.getTo());
                        return e;
                    }).collect(Collectors.toList());
            /*过滤node 在fromToIds中不存在的*/
            List<String> filteredNodeIds = new ArrayList<>();
            /*需要给线条添加颜色每一个数据库颜色不一样（表，1）,查询每一个表的数据库序号*/
            List<Map<String,Object>> tableInDb = metaDataLineageMapper.getTableInDatabaseIndex();
            String firstDb = "";
            int index = 0;
            Map<String,Integer> tableIndexMap = new HashMap<>();
            Map<String,Integer> itemIndexMap = new HashMap<>();
            for(Map<String,Object> map:tableInDb){
                String databaseId = map.get("databaseId")!=null?map.get("databaseId").toString():"";
                String tableId = map.get("tableId")!=null?map.get("tableId").toString():"";
                if(!firstDb.equalsIgnoreCase(databaseId)) {
                    firstDb = databaseId;
                    index++;
                    itemIndexMap.put(databaseId,index);
                }
                tableIndexMap.put(tableId,index);
            }

            /*过滤后的node*/
            List<LineageNode> filteredNodes = nodeDataArray.stream()
                    .filter(e -> fromToIds.contains(e.getId())||lineageValue.getResourceId().equalsIgnoreCase(e.getId()))
                    .map(e -> {
                        filteredNodeIds.add(e.getId());
                        //字段设置颜色
                        if(MetaModuleConst.COLUMN_MODULE_TYPE.equalsIgnoreCase(e.getType())){
                            e.setColor(tableIndexMap.get(e.getParentId()));
                        }else{
                            e.setColor(itemIndexMap.get(e.getParentId()));
                        }
                        return e;
                    })
                    .collect(Collectors.toList());

            filteredLinks.stream().forEach(e ->{
                e.setSource(filteredNodeIds.indexOf(e.getFrom()));
                e.setTarget(filteredNodeIds.indexOf(e.getTo()));
            });

            Map<String,Object> map = setETLNodeAndLink(filteredNodes,filteredLinks);
            resultMap.put("nodeDataArray",filteredNodes);
            resultMap.put("linkDataArray",filteredLinks);
            return new R(map);

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public R getDataLineageHX(LineageMetaValue lineageValue){
        try {
            ParseLineageEnviroment enviroment = new ParseLineageEnviroment(this);

            //根据判断的结果放入相应的任务队列中
            if(MetaModuleConst.COLUMN_MODULE_TYPE.equalsIgnoreCase(lineageValue.getDataType())){
                //字段
                enviroment.getLineageColumns().add(lineageValue);
            }else if(MetaModuleConst.ITEM_MODULE_TYPE.equalsIgnoreCase(lineageValue.getDataType())){
                //数据元
                enviroment.getLineageItems().add(lineageValue);
            }

            //创建解析任务执行器
            enviroment.create(ColumnLineageParserExecutorHX.class);
            enviroment.create(ColumnLineageParserExecutorHX.class);
            enviroment.create(ColumnLineageParserExecutorHX.class);
            enviroment.create(ColumnLineageParserExecutorHX.class);
            enviroment.create(ColumnLineageParserExecutorHX.class);
            enviroment.create(ItemLineageParserExecutorHX.class);
            enviroment.create(ItemLineageParserExecutorHX.class);
            enviroment.create(ItemLineageParserExecutorHX.class);
            enviroment.create(ItemLineageParserExecutorHX.class);
            enviroment.create(ItemLineageParserExecutorHX.class);
            //启动  开始解析任务
            enviroment.enviromentStart();

            //获取解析结果
            Map<String, Object> resultMap = new HashMap<String, Object>();
            Set<LineageNode> nodeDataArray = new HashSet<LineageNode>();
            Set<LineageLink> linkDataArray = new HashSet<LineageLink>();
            nodeDataArray.addAll(enviroment.getNode());
            linkDataArray.addAll(enviroment.getLink());
            List<String> tableNodeIds = new ArrayList<>();
            /*将link中无用的去除*/
            List<String> nodeIds = nodeDataArray.stream().map(e -> {
                return e.getId();
            }).collect(Collectors.toList());
            /*from和to的Id集合*/
            List<String> fromToIds = new ArrayList<>();
            /*from和to都存在的link*/
            List<LineageLink> filteredLinks = linkDataArray.stream()
                    .filter(e -> nodeIds.contains(e.getFrom()) && nodeIds.contains(e.getTo()))
                    .map(e -> {
                        fromToIds.add(e.getFrom());
                        fromToIds.add(e.getTo());
                        return e;
                    }).collect(Collectors.toList());
            /*过滤node 在fromToIds中不存在的*/
            List<String> filteredNodeIds = new ArrayList<>();
            /*需要给线条添加颜色每一个数据库颜色不一样（表，1）,查询每一个表的数据库序号*/
            List<Map<String, Object>> tableInDb = metaDataLineageMapper.getTableInDatabaseIndex();
            String firstDb = "";
            int index = 0;
            Map<String, Integer> tableIndexMap = new HashMap<>();
            Map<String, Integer> itemIndexMap = new HashMap<>();
            for (Map<String, Object> map : tableInDb) {
                String databaseId = map.get("databaseId") != null ? map.get("databaseId").toString() : "";
                String tableId = map.get("tableId") != null ? map.get("tableId").toString() : "";
                if (!firstDb.equalsIgnoreCase(databaseId)) {
                    firstDb = databaseId;
                    index++;
                    itemIndexMap.put(databaseId, index);
                }
                tableIndexMap.put(tableId, index);
            }

            /*过滤后的node*/
            List<LineageNode> filteredNodes = nodeDataArray.stream()
                    // .filter(e -> fromToIds.contains(e.getId())||lineageValue.getResourceId().equalsIgnoreCase(e.getId()))
                    .map(e -> {
                        filteredNodeIds.add(e.getId());
                        //字段设置颜色
                        if (MetaModuleConst.COLUMN_MODULE_TYPE.equalsIgnoreCase(e.getType())) {
                            e.setColor(tableIndexMap.get(e.getParentId()));
                        } else {
                            e.setColor(itemIndexMap.get(e.getParentId()));
                        }
                        return e;
                    })
                    .collect(Collectors.toList());

            filteredLinks.stream().forEach(e -> {
                e.setSource(filteredNodeIds.indexOf(e.getFrom()));
                e.setTarget(filteredNodeIds.indexOf(e.getTo()));
            });

            //Map<String, Object> map = setETLNodeAndLink(filteredNodes, filteredLinks);

            List<LineageNode> elementSetArray = filteredNodes.stream()
                    .filter(e -> e.getType().equalsIgnoreCase(MetaModuleConst.SET_MODULE_TYPE)).collect(Collectors.toList());

            List<LineageNode> elementItemArray = filteredNodes.stream()
                    .filter(e -> e.getType().equalsIgnoreCase(MetaModuleConst.ITEM_MODULE_TYPE)).collect(Collectors.toList());

            filteredNodes = filteredNodes.stream()
                    .filter(e ->!e.getType().equalsIgnoreCase(MetaModuleConst.PROJECT_MODULE_TYPE)&&  !e.getType().equalsIgnoreCase(MetaModuleConst.SET_MODULE_TYPE)&&!e.getType().equalsIgnoreCase(MetaModuleConst.ITEM_MODULE_TYPE)).collect(Collectors.toList());

            Map<String, List<LineageNode>> nodeList = filteredNodes.stream().collect(Collectors.groupingBy(LineageNode::getLevel));
            Map<String,Object> nodeDataList = new HashMap<>();
            for (Map.Entry<String, List<LineageNode>> m : nodeList.entrySet()) {
                Map<String,Object> temp = new HashMap<>();
                temp.put("children",m.getValue());
                nodeDataList.put(m.getKey(),temp);
            }

            resultMap.put("nodeDataArray", nodeDataList);
            resultMap.put("elementItemArray",elementItemArray);
            resultMap.put("elementSetArray",elementSetArray);
            resultMap.put("linkDataArray", filteredLinks);
            return new R(resultMap);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public R getDataLineageETLDetail(LineageMetaValue lineageMetaValue) {
        try {
            Map<String, Object> resultMap = new HashMap<>();
            List<LineageNode> targetTable = new ArrayList<>();
            List<LineageNode> sourceTable = new ArrayList<>();

            List<LineageLink> links = new ArrayList<>();
            List<LineageNode> nodes = new ArrayList<>();
            List<Map<String, Object>> etlDetailList = getETLDetailInfo(lineageMetaValue);
            String sourceTableFlag = "";

            LineageNode targetTableNode = new LineageNode();
            LineageNode sourceTableNode = new LineageNode();

            if(etlDetailList==null){
                return null;
            }

            int index = 0;
            for (Map<String, Object> detail : etlDetailList) {
                //String targetTableId = detail.get("target_table_id") != null ? detail.get("target_table_id").toString() : "";
                String targetTableName = detail.get("target_tableName") != null ? detail.get("target_tableName").toString() : "";
                //String targetColumnId = detail.get("target_column_id") != null ? detail.get("target_column_id").toString() : "";
                String targetColumnName = detail.get("target_columnName") != null ? detail.get("target_columnName").toString() : "";
                String targetTableResourceId = detail.get("target_table_resource_id") != null ? detail.get("target_table_resource_id").toString() : "";
                String targetColumnResourceId = detail.get("target_column_resource_id") != null ? detail.get("target_column_resource_id").toString() : "";

                //String sourceTableId = detail.get("source_table_id") != null ? detail.get("source_table_id").toString() : "";
                String sourceTableName = detail.get("source_tableName") != null ? detail.get("source_tableName").toString() : "";
                //String sourceColumnId = detail.get("source_column_id") != null ? detail.get("source_column_id").toString() : "";
                String sourceColumnName = detail.get("source_columnName") != null ? detail.get("source_columnName").toString() : "";
                String sourceTableResourceId = detail.get("source_table_resource_id") != null ? detail.get("source_table_resource_id").toString() : "";
                String sourceColumnResourceId = detail.get("source_column_resource_id") != null ? detail.get("source_column_resource_id").toString() : "";

                //构造targetTable信息
                if (index == 0) {
                    targetTableNode.setId(targetTableResourceId);
                    targetTableNode.setName(targetTableName);
                    targetTableNode.setType("table");

                    nodes.add(targetTableNode);
                }

                //构造targetColumn信息
                LineageNode targetColumnNode = new LineageNode();
                targetColumnNode.setId(targetColumnResourceId);
                targetColumnNode.setName(targetColumnName);
                targetColumnNode.setType("column");

                //已经有相同的目标字段，不再添加同样的字段,拿到重复的点id,赋给link
                boolean hasColumn = false;
                String repeatColumnId = targetColumnResourceId;
                for(LineageNode c : targetTableNode.getChildren()){
                    if(c.getName().equalsIgnoreCase(targetColumnName)){
                        hasColumn = true;
                        repeatColumnId = c.getId();
                        break;
                    }
                }
                if(!hasColumn){
                    nodes.add(targetColumnNode);
                }

                //构造targetTable和targetColumn的Link
                LineageLink link = new LineageLink();
                link.setFrom(targetTableNode.getId());
                link.setTo(targetColumnNode.getId());
                link.setType("table_column");
                links.add(link);

                if(!"".equalsIgnoreCase(sourceColumnName)){
                    //构造sourceColumn信息
                    LineageNode sourceColumnNode = new LineageNode();
                    sourceColumnNode.setId(sourceColumnResourceId);
                    sourceColumnNode.setName(sourceColumnName);
                    sourceColumnNode.setType("column");

                    nodes.add(sourceColumnNode);
                    //如果sourceTable改变，则是另一个source表
                    if ("".equalsIgnoreCase(sourceTableFlag) || !sourceTableFlag.equalsIgnoreCase(sourceTableName)) {
                        sourceTableFlag = sourceTableName;

                        if (sourceTableNode.getId() != null && !"".equalsIgnoreCase(sourceTableNode.getId())) {
                            sourceTable.add(sourceTableNode);
                        }
                        //构造sourceTable信息
                        sourceTableNode = new LineageNode();
                        sourceTableNode.setId(sourceTableResourceId);
                        sourceTableNode.setName(sourceTableName);
                        sourceTableNode.setType("table");
                        nodes.add(sourceTableNode);
                    }

                    //构造sourceTable和sourceColumn的Link
                    link = new LineageLink();
                    link.setFrom(sourceTableNode.getId());
                    link.setTo(sourceColumnNode.getId());
                    link.setType("table_column");
                    links.add(link);

                    //构造targetColumn和sourceColumn的link
                    link = new LineageLink();
                    link.setFrom(sourceColumnResourceId);
                    link.setTo(repeatColumnId);
                    link.setType("column_column");
                    links.add(link);
                }

                index++;
            }

            List<String> nodeIds = new ArrayList<>();
            nodes.stream().forEach(n->{
                nodeIds.add(n.getId());
            });
            links.stream().forEach(e ->{
                e.setSource(nodeIds.indexOf(e.getFrom()));
                e.setTarget(nodeIds.indexOf(e.getTo()));
            });

            resultMap.put("nodeDataArray",nodes);
            resultMap.put("linkDataArray",links);

            return new R(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public R getDataLineageETLDetailHX(LineageMetaValue lineageMetaValue) {
        try {
            Map<String, Object> resultMap = new HashMap<>();
            List<LineageNode> targetTable = new ArrayList<>();
            List<LineageNode> sourceTable = new ArrayList<>();
            List<LineageLink> links = new ArrayList<>();
            List<Map<String, Object>> etlDetailList = getETLDetailInfo(lineageMetaValue);

            String sourceTableFlag = "";

            LineageNode targetTableNode = new LineageNode();
            LineageNode sourceTableNode = new LineageNode();

            if(etlDetailList==null){
                return null;
            }

            int index = 0;
            for (Map<String, Object> detail : etlDetailList) {
                //String targetTableId = detail.get("target_table_id") != null ? detail.get("target_table_id").toString() : "";
                String targetTableName = detail.get("target_tableName") != null ? detail.get("target_tableName").toString() : "";
                //String targetColumnId = detail.get("target_column_id") != null ? detail.get("target_column_id").toString() : "";
                String targetColumnName = detail.get("target_columnName") != null ? detail.get("target_columnName").toString() : "";
                String targetTableResourceId = detail.get("target_table_resource_id") != null ? detail.get("target_table_resource_id").toString() : "";
                String targetColumnResourceId = detail.get("target_column_resource_id") != null ? detail.get("target_column_resource_id").toString() : "";

                //String sourceTableId = detail.get("source_table_id") != null ? detail.get("source_table_id").toString() : "";
                String sourceTableName = detail.get("source_tableName") != null ? detail.get("source_tableName").toString() : "";
                //String sourceColumnId = detail.get("source_column_id") != null ? detail.get("source_column_id").toString() : "";
                String sourceColumnName = detail.get("source_columnName") != null ? detail.get("source_columnName").toString() : "";
                String sourceTableResourceId = detail.get("source_table_resource_id") != null ? detail.get("source_table_resource_id").toString() : "";
                String sourceColumnResourceId = detail.get("source_column_resource_id") != null ? detail.get("source_column_resource_id").toString() : "";

                //构造targetTable信息
                if (index == 0) {
                    targetTableNode.setId(targetTableResourceId);
                    targetTableNode.setName(targetTableName);
                    targetTableNode.setType("table");
                }

                //构造targetColumn信息
                LineageNode targetColumnNode = new LineageNode();
                targetColumnNode.setId(targetColumnResourceId);
                targetColumnNode.setName(targetColumnName);
                targetColumnNode.setType("column");

                //已经有相同的目标字段，不再添加同样的字段,拿到重复的点id,赋给link
                boolean hasColumn = false;
                String repeatColumnId = targetColumnResourceId;
                for(LineageNode c : targetTableNode.getChildren()){
                    if(c.getName().equalsIgnoreCase(targetColumnName)){
                        hasColumn = true;
                        repeatColumnId = c.getId();
                        break;
                    }
                }
                if(!hasColumn){
                    targetTableNode.setChildren(targetColumnNode);
                }

                if(!"".equalsIgnoreCase(sourceColumnName.trim())){
                    //构造sourceColumn信息
                    LineageNode sourceColumnNode = new LineageNode();
                    sourceColumnNode.setId(sourceColumnResourceId);
                    sourceColumnNode.setName(sourceColumnName);
                    sourceColumnNode.setType("column");
                    //如果sourceTable改变，则是另一个source表
                    if ("".equalsIgnoreCase(sourceTableFlag) || !sourceTableFlag.equalsIgnoreCase(sourceTableName)) {
                        sourceTableFlag = sourceTableName;

                        if (sourceTableNode.getId() != null && !"".equalsIgnoreCase(sourceTableNode.getId())) {
                            sourceTable.add(sourceTableNode);
                        }
                        //构造sourceTable信息
                        sourceTableNode = new LineageNode();
                        sourceTableNode.setId(sourceTableResourceId);
                        sourceTableNode.setName(sourceTableName);
                        sourceTableNode.setType("table");
                    }
                    sourceTableNode.setChildren(sourceColumnNode);

                    //构造link
                    LineageLink link = new LineageLink();
                    link.setFrom(sourceColumnResourceId);
                    link.setTo(repeatColumnId);
                    link.setType("column_column");
                    links.add(link);
                }

                if (index == etlDetailList.size() - 1 && !sourceTable.contains(sourceTableNode)) {
                    sourceTable.add(sourceTableNode);
                }
                index++;
            }
            targetTable.add(targetTableNode);

            resultMap.put("targetDataArray", targetTable);
            resultMap.put("sourceDataArray", sourceTable);
            resultMap.put("linkDataArray", links);
            return new R(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
      * 获取ETL详情
      * @param lineageMetaValue  
      * @return 
      * @author yjt
      * @date  2019/11/18 17:32 
     */
    private List<Map<String,Object>> getETLDetailInfo(LineageMetaValue lineageMetaValue){
        try{
            List<Map<String, Object>> etlDetailList = new ArrayList<>();
            if("".equalsIgnoreCase(lineageMetaValue.getNameEn())||lineageMetaValue.getNameEn()==null){
                return null;
            }
            //根据resourceid查找不到表，则表示该表为etl虚拟关系
            List<Map<String,Object>> resultList = new ArrayList<>();
            resultList = metaDataLineageMapper.getDataLineageETLDetailDirect(lineageMetaValue.getNameEn(),lineageMetaValue.getEtlId());
            if(resultList==null||resultList.size()<1){
                return null;
            }

            String changeSourceFlag="";
            String sourceTableId = "";
            for(Map<String,Object> map : resultList){
                Map<String,Object> detail = new HashMap<>();
                String targetTableName = map.get("target_table_name")!=null?map.get("target_table_name").toString():"";
                String targetColumnName = map.get("target_column_name") != null ? map.get("target_column_name").toString() : "";
                if(map.get("target_table_id")!=null){
                    Integer tId =Integer.parseInt(map.get("target_table_id").toString());
                    Table t = technologyTableMapper.selectById(tId);
                    if(t!=null){
                        targetTableName = t.getNameCn()!=null&&!"".equalsIgnoreCase(t.getNameCn())?t.getNameCn():t.getNameEn();
                    }
                }
                if(map.get("target_column_id")!=null){
                    Integer columnId = Integer.parseInt(map.get("target_column_id").toString());
                    com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Column c= technologyColumnMapper.selectById(columnId);
                    if(c!=null){
                        targetColumnName = c.getNameCn()!=null&&!"".equalsIgnoreCase(c.getNameCn())?c.getNameCn():c.getNameEn();
                    }
                }

                detail.put("target_tableName", targetTableName);
                detail.put("target_columnName", targetColumnName);
                detail.put("target_table_resource_id", UUID.randomUUID().toString());
                detail.put("target_column_resource_id", UUID.randomUUID().toString());

                String sourceTableName = map.get("source_table_name")!=null?map.get("source_table_name").toString():"";
                String sourceColumnName = map.get("source_column_name") != null ? map.get("source_column_name").toString() : "";
                if(map.get("source_table_id")!=null){
                    Integer tId =Integer.parseInt(map.get("source_table_id").toString());
                    Table t = technologyTableMapper.selectById(tId);
                    if(t!=null){
                        sourceTableName = t.getNameCn()!=null&&!"".equalsIgnoreCase(t.getNameCn())?t.getNameCn():t.getNameEn();
                    }
                }

                if(map.get("source_column_id")!=null){
                    Integer columnId = Integer.parseInt(map.get("source_column_id").toString());
                    com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Column c= technologyColumnMapper.selectById(columnId);
                    if(c!=null){
                        sourceColumnName = c.getNameCn()!=null&&!"".equalsIgnoreCase(c.getNameCn())?c.getNameCn():c.getNameEn();
                    }
                }

                if ("".equalsIgnoreCase(changeSourceFlag) || !changeSourceFlag.equalsIgnoreCase(sourceTableName)) {
                    //切换来源表
                    changeSourceFlag = sourceTableName;
                    sourceTableId = UUID.randomUUID().toString();
                }

                detail.put("source_tableName",changeSourceFlag);
                detail.put("source_columnName",sourceColumnName);
                detail.put("source_table_resource_id",sourceTableId);
                detail.put("source_column_resource_id",UUID.randomUUID().toString());
                etlDetailList.add(detail);
            }

            return etlDetailList;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<MetadataValue> getMetadataValueByIds(String inColumnString) {
        //return metadataValueService.getMetadataValueByIds(inColumnString);
        return null;
    }

    private Map<String,Object> setETLNodeAndLink(List<LineageNode> filteredNodes,List<LineageLink> filteredLinks){
        Map<String,Object> map = new HashMap<>();

        //ET类型的连线
        List<LineageLink> etlLinkList =  filteredLinks.stream().filter(link->"ETL".equalsIgnoreCase(link.getType())).collect(Collectors.toList());
        if(etlLinkList==null||etlLinkList.size()<1){
            map.put("nodeDataArray",filteredNodes);
            map.put("linkDataArray",filteredLinks);
            return map;
        }

        List<LineageLink> linkList = filteredLinks.stream().filter(link->!"ETL".equalsIgnoreCase(link.getType())).collect(Collectors.toList());
        etlLinkList.stream().forEach(l->{
            String uuid = UUID.randomUUID().toString();
            LineageNode node = new LineageNode();
            node.setId(uuid);
            node.setType("ETL");
            node.setName("ETL");
            node.setEtlTargetId(l.getTo());

            filteredNodes.stream().forEach(n->{
                if(n.getId()!=null&&l.getTo()!=null&&n.getId().equalsIgnoreCase(l.getTo())){
                    node.setNameEn(n.getNameEn());
                }
            });
            node.setEtlTargetId(l.getTo());
            if(l.getEtlId()!=null){
                node.setEtlId(l.getEtlId());
            }
            filteredNodes.add(node);

            LineageLink link = new LineageLink();
            link.setFrom(l.getFrom());
            link.setTo(uuid);
            link.setType("ETL");
            link.setSource(l.getSource());
            link.setTarget(filteredNodes.size()-1);
            linkList.add(link);

            link = new LineageLink();
            link.setFrom(uuid);
            link.setTo(l.getTo());
            link.setType("ETL");
            link.setSource(filteredNodes.size()-1);
            link.setTarget(l.getTarget());
            linkList.add(link);
        });

        map.put("nodeDataArray",filteredNodes);
        map.put("linkDataArray",linkList);
        return map;
    }


    /**
      * 根据columnid找elementid
      * @param
      * @return 
      * @author yjt
      * @date  2019/10/25 13:42 
     */
    @Override
    public List<LineageMetaValue> getColumnSourceItems(LineageMetaValue lineageMetaValue) {
        return metaDataLineageMapper.getColumnSourceItems(lineageMetaValue.getId());
    }

    @Override
    public List<LineageMetaValue> getColumnSourceTables(LineageMetaValue lineageMetaValue){
        return metaDataLineageMapper.getColumnSourceTables(lineageMetaValue.getId());
    }

    @Override
    public List<LineageMetaValue> getItemSourceColumns(LineageMetaValue lineageMetaValue){
        return metaDataLineageMapper.getItemSourceColumns(lineageMetaValue.getId());
    }

    @Override
    public List<LineageMetaValue> getItemDataSets(LineageMetaValue lineageMetaValue){
        return metaDataLineageMapper.getItemDataSets(lineageMetaValue.getId());
    }

    @Override
    public List<LineageMetaValue> getTableEtlSourceTables(LineageMetaValue lineageMetaValue){
        //获取一个表的ETL来源表集合
        //区分etl的来源表是否在技术视图数据中存在
        List<Map<String,Object>> etlList = metaDataLineageMapper.getTableETLInfo(lineageMetaValue);

        if(etlList==null||etlList.size()<1){
            return null;
        }
        if(etlList.get(0).get("id")==null){
            return null;
        }

        //获取etl的source表
        List<Integer> ids = new ArrayList<>();
        for(Map<String,Object> e :etlList){
            ids.add(Integer.parseInt(e.get("id").toString()));
        }
        List<Map<String,Object>> etlSource = metaDataLineageMapper.getTableETLSource(ids);

        if(etlSource==null||etlSource.size()<1){
            return null;
        }

        List<LineageMetaValue> list = new ArrayList<>();
        for (Map<String,Object> map : etlSource){
            LineageMetaValue value = new LineageMetaValue();
            value.setDataType("table");
            value.setEtlId(Integer.parseInt(map.get("etl_id").toString()));
            if(map.get("source_table_id")==null||"".equalsIgnoreCase(map.get("source_table_id").toString())){
                //source_table_id为空，技术数据中不存在，不再关联查询中文名
                value.setDbName("");
                value.setNameEn(map.get("source_table")!=null?map.get("source_table").toString():"");
                value.setNameCn(map.get("source_table")!=null?map.get("source_table").toString():"");
                value.setResourceId(UUID.randomUUID().toString());
            }else{
                Integer sourceTableId = Integer.parseInt(map.get("source_table_id").toString());
                Table sourceTable = technologyTableMapper.selectById(sourceTableId);
                if(sourceTable!=null){
                    value.setNameCn(sourceTable.getNameCn()!=null?sourceTable.getNameCn():sourceTable.getNameEn());
                    value.setNameEn(sourceTable.getNameEn());
                    value.setResourceId(sourceTable.getResourceId());
                    value.setId(sourceTableId);
                }else{
                    value.setDbName("");
                    value.setNameEn(map.get("source_table")!=null?map.get("source_table").toString():"");
                    value.setNameCn(map.get("source_table")!=null?map.get("source_table").toString():"");
                    value.setResourceId(UUID.randomUUID().toString());
                }
            }
            list.add(value);
        }
        return list;
        //return metaDataLineageMapper.getTableEtlSourceTables(lineageMetaValue.getId());
    }

    @Override
    public ProjectDataSetCategory getProjectNodeInfo(Integer id){
        return projectDataMapper.selectById(id);
    }

    @Override
    public LineageMetaValue getElementRefColumnInfo(Integer datasetId,Integer elementId){
        try{
            List<RelationView> list = projectViewMapper.selectProjectColumn(datasetId,elementId);
            if(list==null||list.size()<1){
                return null;
            }

            LineageMetaValue value = new LineageMetaValue();
            value.setId(list.get(0).getColumnId());
            value.setNameCn(list.get(0).getColumnNameCn()!=null&&!"".equalsIgnoreCase(list.get(0).getColumnNameCn())?list.get(0).getColumnNameCn():list.get(0).getColumnNameEn());
            value.setNameEn(list.get(0).getColumnNameEn());
            value.setResourceId(list.get(0).getColumnResourceId());
            value.setDataType("column");
            return value;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<LineageMetaValue> getColumnEtlSourceColumns(List<LineageMetaValue> sourceColumns) {
        String ids = "";
        for (int i = 0; i < sourceColumns.size(); i++) {
            if (i == 0) {
                ids = sourceColumns.get(0).getId() + "";
            } else {
                ids += "," + sourceColumns.get(0).getId() + "";
            }
        }
        if ("".equalsIgnoreCase(ids)) {
            return new ArrayList<LineageMetaValue>();
        }
        return metaDataLineageMapper.getColumnEtlSourceColumns(ids);
    }

    //将解析出来的列转化为元数据系统中的列
    public LineageNode transferColumn2MetadataColumn(Column c) {
        //如果是普通的,执行sql转化为元数据
        if(!c.getSourceTable().isEmpty()){
            return metaDataLineageMapper.transferColumn2MetadataColumn(c);
        }else{//如果是表达式的特殊处理 指标运算的，转化为指标节点
            return metaDataLineageMapper.transferColumn2MetadataIndex(c);
        }
    }

    @Override
    public List<LineageMetaValue>  getElementProjectList(Integer id){
        try{
            return metaDataLineageMapper.getElementProjectList(id);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

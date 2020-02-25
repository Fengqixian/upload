package com.clinbrain.bd.mdm.MetadataManage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.clinbrain.bd.mdm.MetadataManage.entity.*;
import com.clinbrain.bd.mdm.MetadataManage.mapper.DataLineageMapper;
import com.clinbrain.bd.mdm.MetadataManage.service.DataLineageService;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataModelService;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataPropertiesService;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataValueService;
import com.clinbrain.bd.mdm.MetadataManage.util.cost.MetaModuleConst;
import com.clinbrain.bd.mdm.MetadataManage.util.lineage.*;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.parser.common.entity.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 元数据管理服务
 */
@AllArgsConstructor
@Getter
@Service
public class DataLineageServiceImpl implements DataLineageService {
    private DataLineageMapper dataLineageMapper ;
    private MetadataValueService metadataValueService;
    private MetadataPropertiesService metadataPropertiesService;
    private MetadataModelService metadataModelService;
    private final static Logger LOGGER = LoggerFactory.getLogger(DataLineageServiceImpl.class);
    /**
     * @createBy wy
     * @createTime 2019-01-04
     * 1. 根据参数查询数据
     * 2. 根据不同模型取不同的东西出来解析
     * 3. 根据解析的结果来处理（过滤无关内容）
     * 4. 整理数据返回
     * 5. 扩展内容代谢
     * @param metaJson
     * @return
     */
    public R getDataLineage(ModelResourceTreeEntity treeEntity) throws Exception{
        if(treeEntity.getResourceId()==null) return new R();
        //查询出tree节点的数据
        MetadataValue metadataValue = new MetadataValue();
        metadataValue.setId(treeEntity.getId());
        metadataValue.setResourceId(treeEntity.getResourceId());
        metadataValue.setParentId(treeEntity.getParentResourceId());
        MetadataValue value = getMetadataValue(metadataValue);
        //查询出对象，创建环境
        ParseAbstractEnviroment enviroment = new ParseAbstractEnviroment(this);
        //将指标放入指标任务队列
        enviroment.getIndexQueue().add(value);
        //创建分析器
        enviroment.create(SqlParser.class);
        enviroment.create(IndexParser.class);
        enviroment.create(TableParser.class);
        //启动开始解析
        enviroment.enviromentStart();

        //获取解析结果
        Map<String,Object> resultMap =  new HashMap<String, Object>();
        Set<LineageNode> nodeDataArray = new HashSet<LineageNode>();
        Set<LineageLink> linkDataArray = new HashSet<LineageLink>();
        nodeDataArray.addAll(enviroment.getNode());
        linkDataArray.addAll(enviroment.getLink());
        resultMap.put("nodeDataArray",nodeDataArray);
        resultMap.put("linkDataArray",linkDataArray);
        return new R(resultMap);
    }

    @Override
    public R getDataLineage(String resourceId) throws Exception {
        MetadataValue metadataValue = new MetadataValue();
        metadataValue.setResourceId(resourceId);
        metadataValue = getMetadataValue(metadataValue);
        //做一些基本的判断 column ？ item ？然后创建解析环境
        ParseEnviroment enviroment = new ParseEnviroment(this);
        //根据判断的结果放入相应的任务队列中
        if(MetaModuleConst.COLUMN_MODULE_TYPE.equalsIgnoreCase(metadataValue.getModelType())){
            /*找到由它生成的item*/
            metadataValue.setModelType(MetaModuleConst.REF_COLUMN_ITEM_TYPE);
            List<MetadataValue> items = getParentItems(metadataValue);
            metadataValue.setModelType(MetaModuleConst.COLUMN_MODULE_TYPE);
            enviroment.getItems().addAll(items);
        }else if(MetaModuleConst.ITEM_MODULE_TYPE.equalsIgnoreCase(metadataValue.getModelType())){
            enviroment.getItems().add(metadataValue);
        }else if(MetaModuleConst.SET_MODULE_TYPE.equalsIgnoreCase((metadataValue.getModelType()))){
            //元素项集合也按照元素项处理
            enviroment.getItems().add(metadataValue);
        }else{
            return null;
        }
        //创建解析任务执行器
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(ColumnParserExecutor.class);
        enviroment.create(EtlParserExecutor.class);
        enviroment.create(EtlParserExecutor.class);
        enviroment.create(ItemParserExecutor.class);
        enviroment.create(ItemParserExecutor.class);
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
        List<Map<String,String>> tableInDb = dataLineageMapper.getTableInDatabaseIndex(MetaModuleConst.TABLE_MODULE_TYPE);
        String firstDb = "";
        int index = 0;
        Map<String,Integer> tableIndexMap = new HashMap<>();
        Map<String,Integer> itemIndexMap = new HashMap<>();
        for(Map<String,String> map:tableInDb){
            if(!firstDb.equalsIgnoreCase(map.get("databaseId"))) {
                firstDb = map.get("databaseId");
                index++;
                itemIndexMap.put(map.get("databaseId"),index);
            }
            tableIndexMap.put(map.get("tableId"),index);
        }

        /*过滤后的node*/
        List<LineageNode> filteredNodes = nodeDataArray.stream()
                .filter(e -> fromToIds.contains(e.getId())||resourceId.equalsIgnoreCase(e.getId()))
                .map(e -> {
                    filteredNodeIds.add(e.getId());
                    //字段设置颜色
                    if(MetaModuleConst.COLUMN_MODULE_TYPE.equalsIgnoreCase(e.getType())){
                        tableNodeIds.add(e.getParentId());
                        /*添加link column —>table*/
                        LineageLink lineageLink = new LineageLink();
                        lineageLink.setFrom(e.getId());
                        lineageLink.setTo(e.getParentId());
                        lineageLink.setType(MetaModuleConst.COLUMN_MODULE_TYPE+"_"+MetaModuleConst.TABLE_MODULE_TYPE);
                        filteredLinks.add(lineageLink);

                        e.setColor(tableIndexMap.get(e.getParentId()));
                    }else{
                        e.setColor(itemIndexMap.get(e.getParentId()));
                    }
                    return e;
                })
                .collect(Collectors.toList());

        /*查找所有的表，构造节点*/
        List<MetadataValue> metadataValues = getMetadataValueByIds(StringUtils.join(tableNodeIds,","));
        metadataValues.stream().forEach(e ->{
            LineageNode node = new LineageNode();
            node.setId(e.getResourceId());
            node.setParentId(e.getParentId());
            node.setModuleId(e.getModelId());
            node.setType(e.getModelType());
            node.setName(e.getNameCn()==null?e.getNameEn():e.getNameCn());
            node.setColor(tableIndexMap.get(e.getResourceId()));
            filteredNodes.add(node);
            filteredNodeIds.add(node.getId());
        });
        filteredLinks.stream().forEach(e ->{
            e.setSource(filteredNodeIds.indexOf(e.getFrom()));
            e.setTarget(filteredNodeIds.indexOf(e.getTo()));
        });

        Map<String,Object> map = setETLNodeAndLink(filteredNodes,filteredLinks);
        resultMap.put("nodeDataArray",filteredNodes);
        resultMap.put("linkDataArray",filteredLinks);
        return new R(map);
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

    @Override
    public List<MetadataValue> getMetadataValueByIds(String inColumnString) {
        return metadataValueService.getMetadataValueByIds(inColumnString);
    }

    @Override
    public List<MetadataValue> getColumnSourceItems(MetadataValue metadataValue) {
        return dataLineageMapper.getColumnSourceItems(metadataValue);
    }

    @Override
    public List<MetadataValue> getColumnTransferEtl(MetadataValue metadataValue) {
        //需要查询出所有的属性
        MetadataValue value = new MetadataValue();
        value.setModelId(MetaModuleConst.ETL_MODULE_ID);
        List<MetadataProperties> properties = getMetadataProperties(value);
        properties.stream().forEach(o ->{
            if(EtlParserExecutor.ETL_OUTPUT.equalsIgnoreCase(o.getNameEn())){
                BeanUtil.setFieldValue(value,o.getMappingField(),metadataValue.getParentId());
            }
        });
        return metadataValueService.list(Wrappers.query(value));
    }

    @Override
    public List<MetadataValue> getParentItems(MetadataValue metadataValue) {
        return dataLineageMapper.getParentItems(metadataValue);
    }

    //根据条件获取数据的详细信息
    public MetadataValue getMetadataValue(MetadataValue metadataValue){
        Wrapper<MetadataValue> wrapper = Wrappers.query(metadataValue);
        return metadataValueService.getOne(wrapper);
    }
    //根据模型resourceId 获取模型的属性信息
    public List<MetadataProperties> getMetadataProperties(MetadataValue metadataValue){
        MetadataProperties properties = new MetadataProperties();
        properties.setModelResourceId(metadataValue.getModelId());
        Wrapper<MetadataProperties> wrapper = Wrappers.query(properties);
        return metadataPropertiesService.list(wrapper);
    }
    //根据模型resourceId 获取模型的信息
    public MetadataModel getMetadataModel(MetadataValue metadataValue){
        MetadataModel model = new MetadataModel();
        model.setResourceId(metadataValue.getModelId());
        Wrapper<MetadataModel> wrapper = Wrappers.query(model);
        return metadataModelService.getOne(wrapper);
    }
    //将解析出来的列转化为元数据系统中的列
    public LineageNode transferColumn2MetadataColumn(Column c) {
        //如果是普通的,执行sql转化为元数据
        if(!c.getSourceTable().isEmpty()){
            return dataLineageMapper.transferColumn2MetadataColumn(c);
        }else{//如果是表达式的特殊处理 指标运算的，转化为指标节点
            return dataLineageMapper.transferColumn2MetadataIndex(c);
        }
    }

    public List<LineageNode> getLineageTableNode(Set<String> columntables) {
        return  dataLineageMapper.getLineageTableNode(columntables);
    }
}

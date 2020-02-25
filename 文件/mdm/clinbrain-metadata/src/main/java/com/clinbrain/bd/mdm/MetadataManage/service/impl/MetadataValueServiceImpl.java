/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.clinbrain.bd.mdm.MetadataManage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.mdm.MetadataManage.dto.HeaderRelation;
import com.clinbrain.bd.mdm.MetadataManage.dto.ModelResourceTree;
import com.clinbrain.bd.mdm.MetadataManage.entity.*;
import com.clinbrain.bd.mdm.MetadataManage.mapper.MetadataValueMapper;
import com.clinbrain.bd.mdm.MetadataManage.service.DataLineageService;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataPropertiesService;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataReferenceService;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataValueService;
import com.clinbrain.bd.mdm.MetadataManage.util.cost.MetaModuleConst;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.common.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * 元数据
 *
 * @author wangyi
 * @date 2019-05-29 14:41:40
 */
@Service("metadataValueService")
@Slf4j
public class MetadataValueServiceImpl extends ServiceImpl<MetadataValueMapper, MetadataValue> implements MetadataValueService {

    /*@Autowired
    private MetadataPropertiesServiceImpl metadataPropertiesService;*/
    /*@Autowired
    private ElementItemRelationServiceImpl elementItemRelationService;*/
    @Autowired
    private Environment env;
    @Autowired
    private MetadataReferenceServiceImpl referenceService;
    private List<ElementNode> nodeList = new ArrayList<ElementNode>();
    @Autowired
    private MetadataPropertiesService propertiesService;

    /**
     * 元数据简单分页查询
     *
     * @param metadataValue 元数据
     * @return
     */
    @Override
    public IPage<MetadataValue> getMetadataValuePage(Page<MetadataValue> page, MetadataValue metadataValue) {
        List<Integer> listRole = new ArrayList<>();
        if (!SecurityUtils.getUser().getUsername().equals("admin")) {
            listRole = SecurityUtils.getRoles();
            if (listRole.size() == 0) {
                return null;
            }
            return baseMapper.getMetadataValuePage(page, metadataValue, listRole);
        }
        return baseMapper.getMetadataValuePage(page, metadataValue, listRole);
    }

    @Override
    public List<ModelResourceTree> listModelResourceTree() {
        List<ModelResourceTree> treeList = listModelResourceTree(Wrappers.emptyWrapper());
        return buildByLoop(treeList);
    }

    @Override
    public List<ModelResourceTree> listRoleModelResourceTree(Integer roleId) {
        return getModelResourceTree(baseMapper.listRoleModelResourceTree(roleId));
    }

    @Override
    public List<ModelResourceTree> listModelResourceTreeByModelId(String modelId) {
        List<ModelResourceTree> list = null;
        List<Integer> listRole = new ArrayList<>();
        if (!SecurityUtils.getUser().getUsername().equals("admin")) {
            listRole = SecurityUtils.getRoles();
            if (listRole.size() == 0) {
                return null;
            }
            list = baseMapper.listModelResourceTree(listRole);
        } else {
            list = baseMapper.listModelResourceTree(listRole);
        }
        List collect = list.stream()
                .filter(e -> e.getModelId().equalsIgnoreCase(modelId))
                .collect(Collectors.toList());
        return getModelResourceTree(collect);
    }

    private List<ModelResourceTree> listModelResourceTree(QueryWrapper<ModelResourceTree> treeQueryWrapper) {
        List<Integer> listRole = new ArrayList<>();
        if (!SecurityUtils.getUser().getUsername().equals("admin")) {
            listRole = SecurityUtils.getRoles();
            if (listRole.size() == 0) {
                return null;
            }
            return baseMapper.listModelResourceTree(listRole);
        }
        return baseMapper.listModelResourceTree(listRole);
    }

    private List<ModelResourceTree> getModelResourceTree(List<ModelResourceTree> modelResourceTreeEntities) {
        List<ModelResourceTree> treeList = modelResourceTreeEntities.stream()
                .filter(model -> !model.getUuid().equals(model.getParentUuid()))
                .map(model -> {
                    ModelResourceTree node = new ModelResourceTree();
                    BeanUtils.copyProperties(model, node);
                    return node;
                }).collect(Collectors.toList());
        return buildByLoop(treeList);
    }

    private List<ModelResourceTree> buildByLoop(List<ModelResourceTree> treeNodes) {
        List<ModelResourceTree> trees = new ArrayList<>();
        boolean matched = false;
        for (ModelResourceTree treeNode : treeNodes) {
            if (StringUtils.isBlank(treeNode.getParentUuid())) {
                trees.add(treeNode);
            }
            matched = false;
            for (ModelResourceTree it : treeNodes) {
                if (StringUtils.equals(it.getParentUuid(), treeNode.getUuid())) {
                    matched = true;
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(new ArrayList<>());
                    }
                    treeNode.add(it);
                } else {
                    if (matched) break;
                }
            }
        }
        return trees;
    }

    /*不推荐使用*/
    @Deprecated
    @Override
    public List<ModelResourceTree> listModelParentResourceTree(String parentUuid) {
        return baseMapper.listModelParentResourceTree(parentUuid).stream()
                .filter(model -> !model.getUuid().equals(model.getParentUuid()))
                .map(model -> {
                    ModelResourceTree node = new ModelResourceTree();
                    BeanUtils.copyProperties(model, node);
                    return node;
                }).collect(Collectors.toList());
    }

    @Override
    public List<ModelResourceTree> listModelResourceLazyTree(String parentUuid, String queryString) {
        List<ModelResourceTree> treeList = null;
        if (org.apache.commons.lang3.StringUtils.isBlank(parentUuid)) parentUuid = "";
        if (org.apache.commons.lang3.StringUtils.isNotBlank(queryString)) {
            treeList = baseMapper.queryModelResourceLazyTree(queryString);
            parentNodeList = new ArrayList<>();
            return listModelResourceLazy(treeList);
            //return buildByLoop(treeList);
            //return baseMapper.queryModelResourceLazyTree(queryString);
        } else {
            return baseMapper.listModelResourceLazyTree(parentUuid);
        }
    }

    List<ModelResourceTree> parentNodeList = new Vector<>();

    /**
      * 获取模糊搜索树状结构数据
      * @param list  扁平化的模糊搜索结果
      * @return  组装好的父子关系结果
      * @author yjt
      * @date  2019/9/11 14:31 
     */
    public List<ModelResourceTree> listModelResourceLazy(List<ModelResourceTree> list) {
        List<ModelResourceTree> resultList = new ArrayList<>();
        List<ModelResourceTree> allList = baseMapper.listModelResourceByUuid("");

        for (ModelResourceTree m : list) {
            resultList.add(m);
            getParentNode(m, allList);
        }

        for (ModelResourceTree p : parentNodeList) {
            if (!resultList.contains(p)) {
                resultList.add(p);
            }
        }
        resultList = getTreeModelResourceData(resultList,"");
        resultList = resultList.stream().filter(res -> res.getModelId() != null && res.getNameEn() != null).collect(Collectors.toList());
        return resultList;
    }

    /**
      * 获取查询结果的所有父节点
      * @param node
      * @param allList  
      * @return 
      * @author yjt
      * @date  2019/9/11 14:27 
     */
    private synchronized  void getParentNode(ModelResourceTree node,List<ModelResourceTree> allList) {
        if (node.getParentUuid() == null || "".equalsIgnoreCase(node.getParentUuid())) {
            return;
        }
        //添加父节点
        ModelResourceTree parentInfo =new ModelResourceTree();
        for(ModelResourceTree m : allList){
            if(m.getUuid()!=null&&node.getParentUuid()!=null&&m.getUuid().equalsIgnoreCase(node.getParentUuid())){
                parentInfo = m;
                break;
            }
        }
        if (!parentNodeList.contains(parentInfo)) {
            parentNodeList.add(parentInfo);
        }
        getParentNode(parentInfo,allList);
    }

    /**
      * 组装父子结构数据
      * @param resultList
      * @param parentId  
      * @return 
      * @author yjt
      * @date  2019/9/11 13:48 
     */
    private List<ModelResourceTree> getTreeModelResourceData(List<ModelResourceTree>resultList,String parentId){
        List<ModelResourceTree> list = new ArrayList<>();
        List<ModelResourceTree> childList = new ArrayList<>();

        for (ModelResourceTree m : resultList) {
            if((m.getParentUuid()==null ||"".equalsIgnoreCase(m.getParentUuid()))&&"".equalsIgnoreCase(parentId)){
                childList.add(m);
            }else if(m.getParentUuid()!=null&&m.getParentUuid().equalsIgnoreCase(parentId)){
                childList.add(m);
            }
        }

        for (ModelResourceTree m : childList) {
            List<ModelResourceTree> childListTemp = new ArrayList<>();
            String id = m.getUuid();
            childListTemp = getTreeModelResourceData(resultList,id);
            for(ModelResourceTree it : childListTemp ){
                if (it.getChildren() == null) {
                    it.setChildren(new ArrayList<>());
                }
                m.add(it);
            }
            list.add(m);
        }
        return list;
    }

    /**
     * 根据英文名忽略大小写查找元数据
     *
     * @param metadataValue
     * @return
     * @author yjt
     * @date 2019/7/9 9:24
     */
    @Override
    public List<MetadataValue> getMetaDataListByName(MetadataValue metadataValue) {
        return baseMapper.getMetadataValueListByName(metadataValue);
    }

    @Override
    public List selectMetaValueByDynSql(String sql, MetadataValue metadataValue) {
        return baseMapper.selectMetaValueByDynSql(sql, metadataValue);
    }

    @Override
    public List selectDistinctMetadata(MetadataValue metadataValue) {
        return baseMapper.selectDistinctMetadata(metadataValue);
    }

    @Override
    public MetadataValue selectDbByCloumn(MetadataValue metadataValue) {
        return baseMapper.selectDbByCloumn(metadataValue);
    }

    /**
     * @Author：ligen
     * @Date: Created: 2019-07-17 14:05
     * @Description: 自动生成元素项  1个元素项  n个元素项关系
     **/
    @Transactional
    @Override
    public R autoAdd() {
        long begin = System.currentTimeMillis();
        System.out.println("开始：" + LocalDateTime.now());
        String columnModelId = env.getProperty("metavalue.column.modelId");
        String elementModelId = env.getProperty("metavalue.elementItem.modelId");
        String dbModelId = env.getProperty("metavalue.database.modelId");
        MetadataValue dbMetadata = new MetadataValue();
        dbMetadata.setModelId(dbModelId);
        dbMetadata.setModelType("database");
        //dbMetadata.setResourceId("5228179c-8c4d-4a2c-81ad-adf7db67ea5f"); //测试 一个DB
        //dbMetadata.setResourceId("44sgydd0-c1w3-4e43-b0y8-8f97frvklbe"); //测试 一个DB
        List<MetadataValue> saveElemetList = new ArrayList<>();
        List<MetadataReference> saveRefList = new ArrayList<>();
        List<MetadataValue> dbList = this.baseMapper.selectList(Wrappers.<MetadataValue>query(dbMetadata));
        for (MetadataValue db : dbList) {
            MetadataValue tbMetadata = new MetadataValue();
            tbMetadata.setParentId(db.getResourceId());
            tbMetadata.setModelType("table");
            List<MetadataValue> tbList = this.baseMapper.selectList(Wrappers.<MetadataValue>query(tbMetadata));
            for (MetadataValue tb : tbList) {
                MetadataValue columnMetadata = new MetadataValue();
                columnMetadata.setModelType("column");
                columnMetadata.setModelId(columnModelId);
                columnMetadata.setIsStandard(false);
                columnMetadata.setParentId(tb.getResourceId());
                List<MetadataValue> columnList = this.selectDistinctMetadata(columnMetadata);
                //List<MetadataValue> diffList = selectNotInElement(columnModelId,tb.getResourceId(),elementModelId,db.getResourceId());
                for (MetadataValue m : columnList) {
                    String resourceId = UUID.randomUUID().toString();
                    MetadataValue element = new MetadataValue();
                    element.setNameEn(m.getNameEn());
                    element.setModelId(elementModelId);
                    element.setResourceId(resourceId);
                    element.setModelType("element_item");
                    element.setCreateTime(null);
                    element.setUpdateTime(null);
                    element.setParentId(db.getResourceId());
                    element.setStatus(1);
                    element.setIsStandard(false);

                    MetadataValue metadataValue1 = new MetadataValue();
                    metadataValue1.setNameEn(m.getNameEn());
                    metadataValue1.setModelId(elementModelId);
                    metadataValue1.setParentId(db.getResourceId());
                    List<MetadataValue> ele = this.baseMapper.selectList(Wrappers.<MetadataValue>query(metadataValue1));//找元素项有没有
                    if (ele.size() == 0) {
                        //saveElemetList.add(element);
                        this.baseMapper.insert(element);
                        // 处理关系
                        MetadataReference reference = new MetadataReference();
                        reference.setSourceId(m.getResourceId());
                        reference.setSourceParentId(m.getParentId());
                        reference.setTargetId(element.getResourceId());
                        reference.setTargetParentId(element.getParentId());
                        reference.setRefType(MetaModuleConst.REF_COLUMN_ITEM_TYPE);
                        if (referenceService.getBaseMapper().selectCount(Wrappers.<MetadataReference>query(reference)) == 0) {
                            referenceService.save(reference);
                            //saveRefList.add(reference);
                        }
                    } else {
                        MetadataReference reference = new MetadataReference();
                        reference.setSourceId(m.getResourceId());
                        reference.setSourceParentId(m.getParentId());
                        reference.setTargetId(ele.get(0).getResourceId());
                        reference.setTargetParentId(ele.get(0).getParentId());
                        reference.setRefType(MetaModuleConst.REF_COLUMN_ITEM_TYPE);
                        if (referenceService.getBaseMapper().selectCount(Wrappers.<MetadataReference>query(reference)) == 0) {
                            referenceService.save(reference);
                            //saveRefList.add(reference);
                        }
                    }
                }

            }
        }
        //this.saveBatch(saveElemetList);
        //referenceService.saveBatch(saveRefList);
        System.out.println("结束：" + LocalDateTime.now());
        System.out.println("耗时：" + (System.currentTimeMillis() - begin));

        return new R<>(true, "生成成功");
    }

    @Override
    public R autoCreateElementItem(String dbResourceId) {
        Map map = new HashMap();
        map.put("dbResourceId", dbResourceId);
        this.baseMapper.callCreateElementItem(map);
        return new R<>();
    }


    /**
     * @Author：ligen
     * @Date: Created:2019-07-19 14:01
     * @Description: 根据元素项 生成技术元数据
     * parent_id = "所属表"
     * 字段的model_id = "54d0c89e-b7d8-4776-94cc-260354235c24"
     **/
    @Transactional
    @Override
    public R createMetaDataByElementItem(List<MetadataValue> list, String tbName, String dbResourceId) {
        String columnModelId = env.getProperty("metavalue.column.modelId");
        String tableModelId = env.getProperty("metavalue.table.modelId");
        MetadataValue db = new MetadataValue();
        db.setResourceId(dbResourceId);
        db = this.baseMapper.selectOne(Wrappers.<MetadataValue>query(db));//获取DB
        if (db == null) return new R<>(false, "dbResourceId为：" + dbResourceId + "数据库元数据不存在");//排除脏数据
        if (list.size() == 0) return new R<>(false, "元素项不能为空");
        MetadataValue tbMetadata = new MetadataValue();
        tbMetadata.setNameEn(tbName);
        tbMetadata.setModelType("table");
        tbMetadata.setModelId(tableModelId);
        tbMetadata.setParentId(dbResourceId);
        if (this.baseMapper.selectCount(Wrappers.<MetadataValue>query(tbMetadata)) > 0) {
            return new R<>(false, "该库下已经存在表" + tbName);
        }
        String resoucrId = UUID.randomUUID().toString();
        tbMetadata.setResourceId(resoucrId);
        tbMetadata.setIsStandard(false);
        tbMetadata.setStatus(1);
        this.save(tbMetadata);//保存表
        for (MetadataValue metadataValue : list) {//元素项集合
            MetadataValue column = new MetadataValue();
            String columnResourceId = UUID.randomUUID().toString();
            column.setNameEn(metadataValue.getNameEn());
            column.setNameCn(metadataValue.getNameCn());
            column.setResourceId(columnResourceId);
            column.setModelType("column");
            column.setModelId(columnModelId);
            column.setParentId(tbMetadata.getResourceId());
            column.setIsStandard(false);
            column.setStatus(1);
            this.save(column);
            //存储字段关系
            MetadataReference columnRelation = new MetadataReference();
            columnRelation.setSourceId(metadataValue.getResourceId());
            columnRelation.setTargetId(column.getResourceId());
            if (referenceService.getBaseMapper().selectCount(Wrappers.<MetadataReference>query(columnRelation)) == 0) {
                columnRelation.setSourceParentId(metadataValue.getParentId());
                columnRelation.setTargetParentId(column.getParentId());
                columnRelation.setRefType(MetaModuleConst.REF_ITEM_COLUMN_TYPE);//item-column
                referenceService.save(columnRelation);
            }
        }
        autoCreateElementItem(dbResourceId);
        return new R<>(true);
    }

    /**
     * @Author：ligenT
     * @Date: Created:2019-07-22 13:30T
     * @Description: 创建header
     **/
    @Override
    public boolean createHeader(MetadataValue metadataValue, String dbResourceId) {
        String elementSetModelId = env.getProperty("metavalue.elementSet.modelId");
        metadataValue.setResourceId(UUID.randomUUID().toString());
        metadataValue.setModelId(elementSetModelId);
        metadataValue.setModelType("element_set");
        metadataValue.setCreateTime(LocalDateTime.now());
        metadataValue.setIsStandard(false);
        metadataValue.setStatus(1);
        metadataValue.setParentId(dbResourceId);
        return this.save(metadataValue);
    }

    /**
     * @Author：ligen
     * @Date: Created:2019-07-22 13:30
     * @Description: 创建前端拖拉元素项与头产生的关系
     * @Param resourceIds:拖拉进去的resourceId集合
     * @Param list:新产生的结构的集合
     **/
    @Transactional
    @Override
    public boolean createHeaderRelation(List<String> resourceIds, List<HeaderRelation> list) {
        // region 注释。。。
        /*for(HeaderRelation headerRelation:list){
            Integer id = headerRelation.getToId();
            String fromId = headerRelation.getFromResourceId();
            String toId = headerRelation.getToResourceId();
            MetadataValue metadataValue = new MetadataValue();
            //metadataValue.setResourceId(toId);
            metadataValue.setParentId(fromId);
            metadataValue.setId(id);
            this.updateById(metadataValue);
        }*/
        // endregion
        nodeList = new ArrayList<ElementNode>();
        List<ElementNode> all = this.getBaseMapper().getAllItem();
        List<ElementNode> nodes = new ArrayList<>();
        for (String resourceId : resourceIds) {
            nodeList = new ArrayList<ElementNode>();
            nodes.addAll(getChildNodes(all, resourceId));
        }
        int cout = 0;
        for (ElementNode e : nodes) {
            MetadataReference r = new MetadataReference();
            r.setSourceId(e.getSourceId());
            r.setTargetId(e.getTargetId());
            cout += referenceService.getBaseMapper().delete(Wrappers.<MetadataReference>query(r));
        }
        log.info("删除reference关系：" + cout + "条");
        log.info("删除的节点：" + nodes);
        List<MetadataReference> references = new ArrayList<>();
        for (HeaderRelation relation : list) {
            MetadataReference reference = new MetadataReference();
            reference.setSourceId(relation.getFromResourceId());
            reference.setSourceParentId(relation.getFromParentId());
            reference.setTargetId(relation.getToResourceId());
            reference.setTargetParentId(relation.getToParentId());
            reference.setRefType(MetaModuleConst.REF_ITEM_ITEM_TYPE);
            references.add(reference);
        }
        return referenceService.saveBatch(references);
    }

    @Override
    public R getElementByResouceId(String resourceId) {
        //resourceId = "f11c0e38-b1a0-11e9-b276-1866daf4c034";
        nodeList = new ArrayList<>();
        List<ElementNode> all = this.getBaseMapper().getAllItem();
        List<ElementNode> nodes = getChildNodes(all, resourceId);
        List<LineageNode> lineNode = new ArrayList<>();
        List<LineageLink> lineLink = new ArrayList<>();
        for (ElementNode node : nodes) {
            LineageNode lineageNode = new LineageNode();
            lineageNode.setId(node.getId());
            lineageNode.setName(node.getName());
            lineageNode.setModuleId(node.getModelId());
            lineageNode.setType(node.getModelType());
            lineageNode.setParentId(node.getParentId());
            lineNode.add(lineageNode);
            LineageLink lineageLink = new LineageLink();
            lineageLink.setFrom(node.getSourceId());
            lineageLink.setTo(node.getTargetId());
            lineLink.add(lineageLink);
        }
        MetadataValue root = new MetadataValue();
        root.setResourceId(resourceId);
        root = this.getBaseMapper().selectOne(Wrappers.query(root));
        LineageNode lineageNode = new LineageNode();
        lineageNode.setId(root.getResourceId());
        lineageNode.setName(root.getNameCn() != null ? root.getNameCn() : root.getNameEn());
        lineageNode.setModuleId(root.getModelId());
        lineageNode.setType(root.getModelType());
        lineageNode.setParentId(root.getParentId());
        lineNode.add(lineageNode);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("nodeDataArray", lineNode);
        resultMap.put("linkDataArray", lineLink);
        return new R<>(resultMap);
    }

    @Override
    public List<Map> getConnection(String resourceId) {
        String dbModelId = env.getProperty("metavalue.database.modelId");
        MetadataProperties properties = new MetadataProperties();
        properties.setModelResourceId(dbModelId);
        String sql = propertiesService.selectColumnSql(properties);
        MetadataValue metadataValue = new MetadataValue();
        metadataValue.setResourceId(resourceId);
        return this.baseMapper.selectMetaValueByDynSql(sql, metadataValue);
    }

    public List<ElementNode> getChildNodes(List<ElementNode> allNodes, String parentId) {
        for (ElementNode node : allNodes) {
            // 判断是否存在子节点
            if (node.getTargetId().equals(parentId)) {
                // 递归遍历下一级
                getChildNodes(allNodes, node.getSourceId());
                nodeList.add(node);
            }
        }
        System.out.println("nodeList=" + nodeList);
        return nodeList;
    }

    @Override
    public List<MetadataValue> getMetadataValueByIds(String inColumnString) {
        return baseMapper.getMetadataValueByIds(inColumnString);
    }

    @Override
    public List<MetadataValue> selectNotInElement(String columnModelId, String tbResouceId, String elementModelId, String dbResouceId) {
        return this.baseMapper.selectNotInElement(columnModelId, tbResouceId, elementModelId, dbResouceId);
    }


    @Override
    public List<ModelResourceTree> listRoleModelResourcePage(Integer roleId, List<String> resourceIds) {
        return baseMapper.listRoleModelResourcePage(roleId, resourceIds);
    }
}

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

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataReference;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataReferenceInfo;
import com.clinbrain.bd.mdm.MetadataManage.mapper.MetadataReferenceMapper;
import com.clinbrain.bd.mdm.MetadataManage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 元数据关系分析
 *
 * @author
 */
@Service("metadataConnectService")
public class MetadataConnectServiceImpl extends ServiceImpl<MetadataReferenceMapper, MetadataReference> implements MetadataConnectService {

    @Autowired
    private MetadataValueService metadataValueService;

    @Autowired
    private MetadataModelService metadataModelService;

    @Autowired
    private MetadataReferenceService metadataReferenceService;

    //元素项关联关系集合
    private List<Map<String, Object>> linkList = new ArrayList<>();
    private List<String> nodeIdList = new ArrayList<>();
    private List<Map<String, Object>> nodeList = new ArrayList<>();

    private List<String> columnList = new ArrayList<>();

    /**
     * 获取元数据之间的关联关系
     * @author yjt
     * @date 2019/7/19 16:55
     */
    @Override
    public Map<String, Object> getMetadataItemConnect(String resourceId) {
        try {
            linkList = new ArrayList<>();
            nodeList = new ArrayList<>();
            nodeIdList = new ArrayList<>();

            //resourceId = "5a39aa27-00f2-4f57-849d-92d327620810";

            //根据source递归获取target，即往下查找
            getMetadataConnectTargetList(resourceId);

            //根据target递归获取source，即往上溯源
            getMetadataConnectSourceList(resourceId);

            if (nodeList.size() < 1 || linkList.size() < 1) {
                return null;
            }

            /*
            if (nodeIdList != null && nodeIdList.size() > 0) {
                int limitSize = 500;
                if (nodeIdList.size() <= limitSize) {
                    nodeList = getMatadataNodeList(nodeIdList);
                } else {
                    List<String> tempList = new ArrayList<>();
                    for (int i = 0; i < nodeIdList.size(); i++) {
                        tempList.add(nodeIdList.get(i));
                        if (nodeIdList.size() == limitSize || i == nodeIdList.size() - 1) {
                            List<Map<String, Object>> tempNodeList = getMatadataNodeList(tempList);
                            nodeList.addAll(tempNodeList);
                            tempList.clear();
                        }
                    }
                }
            }*/

            //对nodeList分组  暂时不分组，离散展示
            nodeList = getGroupLinkAndNodeList(linkList, nodeList);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("linkDataArray", linkList);
            resultMap.put("nodeDataArray", nodeList);
            String result = JSON.toJSONString(resultMap);
            return resultMap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取下级关系和节点集合
     *
     * @param sourceId
     * @return
     * @author yjt
     * @date 2019/7/19 16:55
     */
    private void getMetadataConnectTargetList(String sourceId) {
        try {
            Page<MetadataReferenceInfo> page = new Page<>();
            page.setSize(-1);
            List<Map<String, Object>> list = baseMapper.getMetaDataConnectListByID(page, sourceId, "");
            for (Map<String, Object> map : list) {
                getNodeAndLinkList(map);

                String id = map.get("targetId").toString();
                getMetadataConnectTargetList(id);
            }

            /*
            //不从视图中查询，从表中获取
            list = baseMapper.getMetaDataConnectListFromConnect(sourceId,"");
            for(Map<String,Object> map : list){
                getNodeAndLinkArray(map);

                String id = map.get("targetId").toString();
                getMetadataConnectTargetList(id);
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * 获取上级关系和节点集合
     *
     * @param targetId
     * @return
     * @author yjt
     * @date 2019/7/22 13:39
     */
    private void getMetadataConnectSourceList(String targetId) {
        try {
            Page<MetadataReferenceInfo> page = new Page<>();
            page.setSize(-1);
            List<Map<String, Object>> list = baseMapper.getMetaDataConnectListByID(page, "", targetId);
            for (Map<String, Object> map : list) {
                getNodeAndLinkList(map);

                String id = map.get("sourceId").toString();
                getMetadataConnectSourceList(id);
            }

            /*
            //不从视图中查询
            list = baseMapper.getMetaDataConnectListFromConnect("",targetId);
            for(Map<String,Object> map : list){
                getNodeAndLinkArray(map);

                String id = map.get("targetId").toString();
                getMetadataConnectSourceList(id);
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * 从一条关系数据中获取link和node信息
     *
     * @param map
     * @return
     * @author yjt
     * @date 2019/7/22 13:21
     */
    private void getNodeAndLinkList(Map<String, Object> map) {
        try {
            Map<String, Object> connect = new HashMap<>();
            String sourceid = map.get("sourceId").toString();
            String targetid = map.get("targetId").toString();
            connect.put("from", sourceid);
            connect.put("to", targetid);
            if (!linkList.contains(connect)) {
                linkList.add(connect);
            }

            //获取link集合，节点集合
            if (!nodeIdList.contains(sourceid)) {
                Map<String, Object> nodeMap = new HashMap<>();
                nodeIdList.add(sourceid);
                nodeMap.put("id", sourceid);
                String sourceNameEn = map.get("sourceNameEn") != null ? map.get("sourceNameEn").toString() : "";
                String sourceNameCn = map.get("sourceNameCn") != null ? map.get("sourceNameCn").toString() : "";
                String sourceModelType = map.get("sourceModelType") != null ? map.get("sourceModelType").toString() : "";
                String sourceParentEn = map.get("sourceParentEn") != null ? map.get("sourceParentEn").toString() : "";
                String sourceParentCn = map.get("sourceParentCn") != null ? map.get("sourceParentCn").toString() : "";
                String sourceParentId = map.get("sourceParentId") != null ? map.get("sourceParentId").toString() : "";
                nodeMap.put("name", "".equalsIgnoreCase(sourceNameCn) ? sourceNameEn : sourceNameCn);
                nodeMap.put("parentName", "".equalsIgnoreCase(sourceParentCn) ? sourceParentEn : sourceParentCn);
                nodeMap.put("modelType", sourceModelType);
                nodeMap.put("parentId", sourceParentId);
                nodeList.add(nodeMap);

                if("column".equalsIgnoreCase(sourceModelType)){
                    columnList.add(sourceNameEn);
                }
            }
            if (!nodeIdList.contains(targetid)) {
                Map<String, Object> nodeMap = new HashMap<>();
                nodeIdList.add(targetid);
                nodeMap.put("id", targetid);
                String targetNameEn = map.get("targetNameEn") != null ? map.get("targetNameEn").toString() : "";
                String targetNameCn = map.get("targetNameCn") != null ? map.get("targetNameCn").toString() : "";
                String targetModelType = map.get("targetModelType") != null ? map.get("targetModelType").toString() : "";
                String targetParentEn = map.get("targetParentEn") != null ? map.get("targetParentEn").toString() : "";
                String targetParentCn = map.get("targetParentCn") != null ? map.get("targetParentCn").toString() : "";
                String targetParentId = map.get("targetParentId") != null ? map.get("targetParentId").toString() : "";

                nodeMap.put("name", "".equalsIgnoreCase(targetNameCn) ? targetNameEn : targetNameCn);
                nodeMap.put("parentName", "".equalsIgnoreCase(targetParentCn) ? targetParentEn : targetParentCn);
                nodeMap.put("modelType", targetModelType);
                nodeMap.put("parentId", targetParentId);
                nodeList.add(nodeMap);

                if("column".equalsIgnoreCase(targetModelType)){
                    columnList.add(targetNameEn);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    private void getNodeAndLinkArray(Map<String, Object> map) {
        try {
            Map<String, Object> connect = new HashMap<>();
            String sourceid = map.get("sourceId").toString();
            String targetid = map.get("targetId").toString();
            connect.put("from", sourceid);
            connect.put("to", targetid);
            if (!linkList.contains(connect)) {
                linkList.add(connect);
            }

            //获取link集合，节点集合
            Map<String, Object> nodeMap = new HashMap<>();
            if (!nodeIdList.contains(sourceid)) {
                nodeIdList.add(sourceid);
                nodeMap = getNodeInfo(sourceid);
                nodeList.add(nodeMap);
            }

            if (!nodeIdList.contains(targetid)) {
                nodeIdList.add(targetid);
                nodeMap = getNodeInfo(targetid);
                nodeList.add(nodeMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * 获取一条数据的信息和父数据信息
     *
     * @param resourceId
     * @return
     * @author yjt
     * @date 2019/7/22 15:44
     */
    private Map<String, Object> getNodeInfo(String resourceId) {
        Map<String, Object> nodeMap = new HashMap<>();
        nodeMap.put("id", resourceId);
        List<Map<String, Object>> list = baseMapper.getMetadataInfoWithParentInfoByID(resourceId);

        if (list != null && list.size() > 0) {
            String nameEn = list.get(0).get("name_en") != null ? list.get(0).get("name_en").toString() : "";
            String nameCn = list.get(0).get("name_cn") != null ? list.get(0).get("name_cn").toString() : "";
            String modelType = list.get(0).get("model_type") != null ? list.get(0).get("model_type").toString() : "";

            String parentNameEn = list.get(0).get("parent_name_en") != null ? list.get(0).get("parent_name_en").toString() : "";
            String parentNameCn = list.get(0).get("parent_name_cn") != null ? list.get(0).get("parent_name_cn").toString() : "";
            String parentModelType = list.get(0).get("parent_model_type") != null ? list.get(0).get("parent_model_type").toString() : "";

            String parentId = list.get(0).get("parent_id") != null ? list.get(0).get("parent_id").toString() : "";
            nodeMap.put("name", "".equalsIgnoreCase(nameCn) ? nameEn : nameCn);
            nodeMap.put("parentName", "".equalsIgnoreCase(parentNameCn) ? parentNameEn : parentNameCn);
            nodeMap.put("modelType", modelType);
            nodeMap.put("parentModelType", parentModelType);
            nodeMap.put("parentId", parentId);
        }
        return nodeMap;
    }

    /**
     * 将node和link分组
     *
     * @return
     * @author yjt
     * @date 2019/7/22 18:28
     */
    private List<Map<String, Object>> getGroupLinkAndNodeList(List<Map<String, Object>> linkList, List<Map<String, Object>> nodeList) {
        if (nodeList == null || nodeList.size() < 1) {
            return null;
        }
        List<String> groupIdList = new ArrayList<>();
        for (Map<String, Object> map : nodeList) {
            String parentId = map.get("parentId") != null ? map.get("parentId").toString() : "'";
            if (!groupIdList.contains(parentId) && !"".equalsIgnoreCase(parentId)) {
                groupIdList.add(parentId);
            }
        }

        List<Map<String, Object>> groupList = new ArrayList<>();
        //如果parentid都为空，即groupIdList没有值，则全部为离散节点
        if (groupIdList == null || groupIdList.size() < 1) {
            for (Map<String, Object> m : nodeList) {
                Map<String, Object> groupMap = new HashMap<>();
                List<Map<String, Object>> tempList = new ArrayList<>();
                tempList.add(m);
                groupMap.put("nodeArray", tempList);
                groupMap.put("groupName", "");
                groupList.add(groupMap);
            }
        } else {
            for (Map<String,Object> m : nodeList) {
                String parentId = m.get("parentId") != null ? m.get("parentId").toString() : "'";
                if ("".equalsIgnoreCase(parentId)) {
                    List<Map<String, Object>> tempList1 = new ArrayList<>();
                    Map<String, Object> groupMap1 = new HashMap<>();
                    tempList1.add(m);
                    groupMap1.put("nodeArray", tempList1);
                    groupMap1.put("groupName", "");
                    groupList.add(groupMap1);
                    continue;
                }
            }

            for (String id : groupIdList) {
                Map<String, Object> groupMap = new HashMap<>();
                List<Map<String, Object>> tempList = new ArrayList<>();
                String parentName = "";
                for(Map<String,Object> m : nodeList){
                    String parentId = m.get("parentId") != null ? m.get("parentId").toString() : "'";
                    if (parentId.equalsIgnoreCase(id) && !tempList.contains(m)) {
                        parentName = m.get("parentName") != null ? m.get("parentName").toString() : "'";
                        tempList.add(m);
                    }
                }
                groupMap.put("nodeArray", tempList);
                groupMap.put("groupName", parentName);
                groupList.add(groupMap);
            }
        }
        return groupList;
    }

    /**
     * 获取数据信息
     *
     * @param sourceIdList
     * @return
     * @author yjt
     * @date 2019/7/19 16:56
     */
    private List<Map<String, Object>> getMatadataNodeList(List<String> sourceIdList) {
        return baseMapper.getMetaDataConnectListByIDList(sourceIdList);
    }

    /**
      * 根据column，从etl解析出关联节点和连接
      * @param nodeList  
      * @return 
      * @author yjt
      * @date  2019/7/23 14:38 
     */
    private List<Map<String,Object>> getETLNodeAndLinkList(List<String> nodeList){
        return null;
    }
}

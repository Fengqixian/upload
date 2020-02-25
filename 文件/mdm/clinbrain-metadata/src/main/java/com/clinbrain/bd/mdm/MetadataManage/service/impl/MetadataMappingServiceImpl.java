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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataMapping;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataModel;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataProperties;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.bd.mdm.MetadataManage.mapper.MetadataMappingMapper;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataMappingService;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataPropertiesService;
import com.clinbrain.bd.mdm.common.security.util.SecurityUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 元数据映射service
 *
 * @param
 * @author yjt
 * @return
 * @date 2019/6/4 16:54
 */
@Service("metadataMappingService")
public class MetadataMappingServiceImpl extends ServiceImpl<MetadataMappingMapper, MetadataMapping> implements MetadataMappingService {

    /**
      * 保存元数据映射
      * @param jsonParam  
      * @return 
      * @author yjt
      * @date  2019/6/5 10:45 
     */
    public boolean saveMappingInfo(String jsonParam) {
        try {
            List<Map<String, Object>> mappingList = (List<Map<String, Object>>) JSONArray.parse(jsonParam);
            if (mappingList == null || mappingList.size() < 1) {
                return false;
            }

            Map<String, Object> mappingInfo = mappingList.get(0);
            String mapType = mappingInfo.get("mapType") != null ? mappingInfo.get("mapType").toString() : "1";
            JSONObject sourceModule = JSONObject.parseObject(JSON.toJSONString(mappingInfo.get("sourceModule")));
            JSONArray sourceDataList = JSONArray.parseArray(JSON.toJSONString(mappingInfo.get("sourceData")));

            JSONObject targetModule = JSONObject.parseObject(JSON.toJSONString(mappingInfo.get("targetModule")));
            JSONArray targetDataList = JSONArray.parseArray(JSON.toJSONString(mappingInfo.get("targetData")));

            String sourceModuleID = sourceModule.getString("row_id") != null ? sourceModule.getString("row_id").toString() : "";
            JSONObject sourceData = sourceDataList.getJSONObject(0);
            String sourceDataID = sourceData.getString("row_id") != null ? sourceData.getString("row_id") : "";

            String targetModuleID = targetModule.get("row_id") != null ? targetModule.get("row_id").toString() : "";

            for (int i = 0; i < targetDataList.size(); i++) {
                JSONObject targetData = targetDataList.getJSONObject(i);
                String targetDataID = targetData.getString("row_id") != null ? targetData.getString("row_id") : "";

                MetadataMapping metaDataMap = new MetadataMapping();
                metaDataMap.setSourcemoduleid(sourceModuleID);
                metaDataMap.setSourcemetadataid(sourceDataID);
                metaDataMap.setTargetmoduleid(targetModuleID);
                metaDataMap.setTargetmetadataid(targetDataID);

                List<Map<String, Object>> hadDataList = baseMapper.selectMappingInfoList(metaDataMap);
                if (hadDataList != null && hadDataList.size() > 0) {
                    continue;
                }

                metaDataMap.setId(UUID.randomUUID().toString());
                metaDataMap.setStatus(0);
                metaDataMap.setCreateuser("testUserID");
                metaDataMap.setMaptype(Integer.parseInt(mapType));

                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                metaDataMap.setCreatedatetime(date.format(new Date()));
                baseMapper.saveMetaDataMappingInfo(metaDataMap);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
      * 删除元数据映射
      * @param jsonParam  
      * @return 
      * @author yjt
      * @date  2019/6/5 10:46 
     */
    public boolean deleteMappingInfo(String jsonParam) {
        try {
            List<Map<String, Object>> mappingList = (List<Map<String, Object>>) JSONArray.parse(jsonParam);
            if (mappingList == null || mappingList.size() < 1) {
                return true;
            }

            String sourceModuleID = mappingList.get(0).get("sourceModuleId") != null ? mappingList.get(0).get("sourceModuleId").toString() : "";
            String sourceDataId = mappingList.get(0).get("sourceDataId") != null ? mappingList.get(0).get("sourceDataId").toString() : "";

            String targetData = JSON.toJSONString(mappingList.get(0).get("targetData"));
            JSONArray targetDataList = JSONArray.parseArray(targetData);
            if (targetDataList == null || targetDataList.size() < 1) {
                return true;
            }
            for (int i = 0; i < targetDataList.size(); i++) {
                JSONObject target = targetDataList.getJSONObject(i);
                String targetModuleID = target.getString("targetModuleId");
                String targetDataID = JSON.toJSONString(target.get("targetDataId"));

                if (targetDataID == null || "".equalsIgnoreCase(targetDataID)) {
                    continue;
                }

                JSONArray targetList = JSONArray.parseArray(targetDataID);
                if (targetList == null || targetList.size() < 1) {
                    continue;
                }
                for (int m = 0; m < targetList.size(); m++) {
                    String id = targetList.get(m).toString();
                    MetadataMapping metadataMap = new MetadataMapping();
                    metadataMap.setSourcemoduleid(sourceModuleID);
                    metadataMap.setSourcemetadataid(sourceDataId);
                    metadataMap.setTargetmoduleid(targetModuleID);
                    metadataMap.setTargetmetadataid(id);
                    baseMapper.deleteMetaDataMappingInfo(metadataMap);
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
      * 审核元数据映射
      * @param jsonParam  
      * @return 
      * @author yjt
      * @date  2019/6/5 10:46 
     */
    public boolean auditMappingInfo(String jsonParam) {
        try {
            List<Map<String, Object>> param = (List<Map<String, Object>>) JSONArray.parse(jsonParam);
            Map<String, Object> auditDataInfo = param.get(0);

            if (auditDataInfo == null || auditDataInfo.size() < 1) {
                return true;
            }
            String sourceModuleID = auditDataInfo.get("sourceModuleId") != null ? auditDataInfo.get("sourceModuleId").toString() : "";
            String sourceDataId = auditDataInfo.get("sourceDataId") != null ? auditDataInfo.get("sourceDataId").toString() : "";
            String status = auditDataInfo.get("status") != null ? auditDataInfo.get("status").toString() : "";

            String targetData = JSON.toJSONString(auditDataInfo.get("targetData"));
            JSONArray targetDataList = JSONArray.parseArray(targetData);
            if (targetDataList == null || targetDataList.size() < 1) {
                return true;
            }

            for (int i = 0; i < targetDataList.size(); i++) {
                JSONObject target = targetDataList.getJSONObject(i);
                String targetModuleID = target.getString("targetModuleId");
                String targetDataID = JSON.toJSONString(target.get("targetDataId"));

                if (targetDataID == null || "".equalsIgnoreCase(targetDataID)) {
                    continue;
                }

                JSONArray targetList = JSONArray.parseArray(targetDataID);
                if (targetList == null || targetList.size() < 1) {
                    continue;
                }
                String ids = "";
                for (int m = 0; m < targetList.size(); m++) {
                    String id = targetList.get(m).toString();
                    if (!id.equalsIgnoreCase("")) {
                        id = "'" + id + "'";
                    }
                    if (ids.equalsIgnoreCase("")) {
                        ids = id;
                    } else {
                        ids += "," + id;
                    }
                }
                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String auditUser = "testAuditUser";
                String auditDataTime = date.format(new Date());
                MetadataMapping metadataMap = new MetadataMapping();
                metadataMap.setSourcemoduleid(sourceModuleID);
                metadataMap.setSourcemetadataid(sourceDataId);
                metadataMap.setAudituser(auditUser);
                metadataMap.setAuditdatetime(auditDataTime);
                baseMapper.auditMetaDataMappingInfo(Integer.parseInt(status), metadataMap, ids);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
      * 获取元数据映射源
      * @param page
      * @param jsonParam  
      * @return 
      * @author yjt
      * @date  2019/6/5 10:47 
     */
    public  IPage<MetadataValue> getMappingSourceDataList(Page<MetadataProperties> page, String jsonParam){
        try{
            List<Map<String, Object>> param = (List<Map<String, Object>>) JSONArray.parse(jsonParam);
            Map<String, Object> paramMap = param.get(0);
            String status = paramMap.get("status") != null ? paramMap.get("status").toString() : "";
            String sourceModuleID = paramMap.get("sourceModuleId") != null ? paramMap.get("sourceModuleId").toString() : "";
            String searchValue = paramMap.get("searchValue") != null ? paramMap.get("searchValue").toString() : "";
            return baseMapper.getMappingSourceDataList(page,sourceModuleID, status,searchValue);

        }catch(Exception e){
            return null;
        }
    }

    /**
      * 获取映射的目标数据集
      * @param page
      * @param jsonParam  
      * @return 
      * @author yjt
      * @date  2019/6/5 10:48 
     */
    public List<Map<String, Object>> getMappingTargetDataList(Page<MetadataProperties> page ,String jsonParam){
        try{
            List<Map<String, Object>> param = (List<Map<String, Object>>) JSONArray.parse(jsonParam);
            Map<String, Object> paramMap = param.get(0);
            String status = paramMap.get("status") != null ? paramMap.get("status").toString() : "";
            String sourceModuleID = paramMap.get("sourceModuleId") != null ? paramMap.get("sourceModuleId").toString() : "";
            String sourceDataD = paramMap.get("sourceDataId") != null ? paramMap.get("sourceDataId").toString() : "";

            //获取该元数据映射的目标元模型id集合
            List<Map<String, Object>> targetModuleList = baseMapper.selectMappingTargetModuleList(status, sourceModuleID);
            if (targetModuleList == null || targetModuleList.size() < 1) {
                return null;
            }

            List<Map<String, Object>> result = new ArrayList<>();
            for (Map<String, Object> target : targetModuleList) {
                if (target == null) {
                    continue;
                }
                String targetModuleID = target.get("targetmoduleid") != null ? target.get("targetmoduleid").toString() : "";
                Map<String, Object> map = new HashMap<>();
                List<Map<String, Object>> dataList = baseMapper.selectMappingTargetDataList(status, sourceModuleID, sourceDataD, targetModuleID);
                map.put("targetModuleID", targetModuleID);
                map.put("dataList", dataList);
                result.add(map);
            }
            return result;

        }catch(Exception e){
            return null;
        }
    }
}

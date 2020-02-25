package com.clinbrain.bd.mdm.MetadataManage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.mdm.MetadataManage.entity.*;
import com.clinbrain.bd.mdm.MetadataManage.mapper.ElementItemMapper;
import com.clinbrain.bd.mdm.MetadataManage.service.*;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.common.security.util.SecurityUtils;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.Wrapper;
import java.util.*;

/**
 * @Author：ligen
 * @Date: Created:13:50  2019/7/16
 * @Description: ElementItemService实现类
 **/
@Slf4j
@Service
public class ElementItemServiceImpl extends ServiceImpl<ElementItemMapper,ElementItemInfo> implements ElementItemService {
    @Autowired
    private ElementItemRelationService elementItemRelationService;
    @Autowired
    private MetadataValueService metadataValueService;
    @Autowired
    private MetadataPropertiesService metadataPropertiesService;
    @Autowired
    private MetadataReferenceService metadataReferenceService;

    @Override
    public IPage<ElementItemInfo> getPageData(Page<ElementItemInfo> page, ElementItemInfo elementItemInfo) {
        List<Integer> listRole = new ArrayList<>();
        if(!SecurityUtils.getUser().getUsername().equals("admin")){
            listRole = SecurityUtils.getRoles();
            if(listRole.size()==0){
                return null;
            }
            return baseMapper.selectPageData(page,elementItemInfo,listRole);
        }
        return baseMapper.selectPageData(page,elementItemInfo,listRole);
    }

    @Override
    public List<ElementItemInfo> getListByName(String elementName) {
        ElementItemInfo elementItemInfo = new ElementItemInfo();
        elementItemInfo.setElementName(elementName);
        return baseMapper.selectList((Wrappers.<ElementItemInfo>query(elementItemInfo)));
    }
    @Override
    public int add(ElementItemInfo entity) {
        entity.setResourceId(UUID.randomUUID().toString());
        entity.setCreateTime(new Date());
        if(entity.getParentResourceId()==null) entity.setParentResourceId("");
        if(this.getListByName(entity.getElementName()).size()>0){//存在
            return 0;
        }
        return baseMapper.insert(entity);
    }


    /**
     * @Author：ligen
     * @Date: Created: 2019-07-17 14:05
     * @Description: 自动生成元素项  1个元素项  n个元素项关系
     **/
    @Transactional
    public void autoAdd(String model_id){
        /*model_id = "d64ce09e-4e71-40d7-b8e5-8b7bdcdefdee";
        MetadataProperties properties = new MetadataProperties();
        properties.setModelResourceId(model_id);
        String columnSql = metadataPropertiesService.selectColumnSql(properties);
        MetadataValue metadataValue = new MetadataValue();
        metadataValue.setModelId(model_id);
        metadataValue.setIsStandard(true);
        *//**
         * 1.获取元素项（去重）
         * 2.插入元素想
         * 3.获取元素项关系
         * 4.插入元素项关系
         *//*
        List<Map<String,Object>> eleItemList = metadataValueService.selectDistinctMetadata(columnSql,metadataValue);
        for(Map<String,Object> m:eleItemList){
            ElementItemInfo info = new ElementItemInfo();
            info.setElementName(m.get("d_name")+"");
            info.setElementRemarks(m.get("d_remarks")+"");
            info.setElementDatatype(m.get("d_datatype")+"");
            info.setElementLength(!(m.get("d_length")+"").equals(null) && !(m.get("d_length")+"").equals("")?Integer.parseInt(m.get("d_length")+""):null);
            info.setResourceId(UUID.randomUUID().toString());
            info.setCreateTime(new Date());
            if(this.baseMapper.selectCount(Wrappers.<ElementItemInfo>query().lambda().eq(ElementItemInfo::getElementName,m.get("d_name")+""))==0){
                this.baseMapper.insert(info);
                metadataValue.setNameEn(m.get("d_name")+"");
                List referenceList =  metadataValueService.selectMetaValueByDynSql(columnSql,metadataValue);
                for(int i=0;i<referenceList.size();i++){
                    Map<String,Object> map = (Map)referenceList.get(i);
                    String source_id = map.get("resource_id")+"";
                    ElementItemRelation reference = new ElementItemRelation();
                    reference.setSourceId(source_id);
                    reference.setTargetId(info.getResourceId());
                    if(elementItemRelationService.getBaseMapper().selectCount(Wrappers.<ElementItemRelation>query(reference))==0){
                        elementItemRelationService.save(reference);
                    }

                }
            }
        }*/


    }

    /**
     * @Author：ligen
     * @Date: Created:2019-07-19 14:01
     * @Description: 根据元素项生成技术元数据
     * parent_id = "所属表"
     * 字段的model_id = "54d0c89e-b7d8-4776-94cc-260354235c24"
     **/
    public boolean createMetaData(List<ElementItemInfo> list,MetadataValue metadataValue){

        return true;
    }
    public static void main(String[] args) {
        Map<String,String> map1 = new HashMap<>();
        map1.put("name","name");
        map1.put("datatype","varchar");
        map1.put("datalength","100");
        Map<String,String> map2 = new HashMap<>();
        map2.put("name","name");
        map2.put("datatype","int");
        map2.put("datalength","200");
        List<Map<String,String>> list = new ArrayList<>();
        list.add(map1);
        list.add(map2);
        System.out.println(list);
        Map<String,String> resultMap = new HashMap<>();
        List<Map<String,String>> resultList = new ArrayList<>();
        for(Map<String,String> m:list){
            for(String s:m.keySet()){
                if(s.equals("datatype")){
                    if("varchar".equals(resultMap.get(s))){
                        resultMap.put(s,"varchar");
                    }else{
                        resultMap.put(s,m.get(s));
                    }
                }else if(s.equals("datalength")){
                    if(resultMap.get(s)!=null){
                        if(Integer.parseInt(resultMap.get(s))<Integer.parseInt(m.get(s))){
                            resultMap.put(s,m.get(s));
                        }else{
                            resultMap.put(s,m.get(s));
                        }
                    }else{
                        resultMap.put(s,m.get(s));
                    }
                }else {
                    resultMap.put(s,m.get(s));
                }
            }
        }
        resultList.add(resultMap);
        System.out.println(resultList);
        System.out.println(!"".equals(null) && !"".equals("") ? Integer.parseInt(""):null);
    }
}

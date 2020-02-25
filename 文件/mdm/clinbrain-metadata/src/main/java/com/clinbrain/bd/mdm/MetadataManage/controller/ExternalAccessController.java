package com.clinbrain.bd.mdm.MetadataManage.controller;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.clinbrain.bd.mdm.MetadataManage.dto.ExternalAccessTlabel;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataModel;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataProperties;
import com.clinbrain.bd.mdm.MetadataManage.entity.MetadataValue;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataModelService;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataPropertiesService;
import com.clinbrain.bd.mdm.MetadataManage.service.MetadataValueService;
import com.clinbrain.bd.mdm.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.mdm.MetadataManage.controller.ExternalAccessController
 * @createdDate 2019/9/11 16:57
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
@RestController
@RequestMapping("/externalAccess")
public class ExternalAccessController {
    @Autowired
    private MetadataValueService metadataValueService;
    @Autowired
    private MetadataPropertiesService metadataPropertiesService;
    @Autowired
    private MetadataModelService metadataModelService;

//    @Value("${metavalue.tlabel.modelId}")
    private String tlabelModelId;
    @PostMapping(value = "/tlabel")
    public R externalAccessTlabel(@RequestBody ExternalAccessTlabel tlabel) {
        /*将tlabel转化为Map*/
        Map<String,Object> tlabelMap = BeanUtil.beanToMap(tlabel);
        /*获取模型*/
        MetadataModel model = new MetadataModel();
        model.setResourceId(tlabelModelId);
        model = metadataModelService.getOne(Wrappers.query(model));
        /*查询标签模型的属性*/
        MetadataProperties properties = new MetadataProperties();
        properties.setModelResourceId(model.getResourceId());
        List<MetadataProperties> propertiesList = metadataPropertiesService.list(Wrappers.query(properties));
        /*给MetadataValue设置属性值*/
        MetadataValue metadataValue = new MetadataValue();

        String parentIdProp = null;
        for(MetadataProperties prop:propertiesList){
            if("tlabelPoolid".equalsIgnoreCase(prop.getNameEn())){
                parentIdProp = prop.getMappingField();
            }
            BeanUtil.setFieldValue(metadataValue,prop.getMappingField(),tlabelMap.get(prop.getNameEn()));
        }

        metadataValue.setModelType(model.getModelType());
        metadataValue.setModelId(model.getResourceId());
        metadataValue.setResourceId(UUID.randomUUID().toString());

        metadataValue.setParentId("");
        if(tlabel.getTlabelParentid()!=null){
            //查询上级资源的真实resourceId  modelId  parentId
            MetadataValue value = new MetadataValue();
            BeanUtil.setFieldValue(value,parentIdProp,tlabel.getTlabelParentid());
            value.setModelId(tlabelModelId);
            value = metadataValueService.getOne(Wrappers.query(value));
            if(value !=null||value.getResourceId()!=null){
                metadataValue.setParentId(value.getResourceId());
            }
        }
        metadataValue.setNameCn(tlabel.getTlabelName());
        metadataValue.setStatus(tlabel.getTlabelStatus());
        metadataValueService.save(metadataValue);
        return new R<>();
    }
}

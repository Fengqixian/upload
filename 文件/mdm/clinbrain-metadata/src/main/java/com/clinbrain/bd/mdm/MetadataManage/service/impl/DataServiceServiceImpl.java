package com.clinbrain.bd.mdm.MetadataManage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.clinbrain.bd.mdm.MetadataManage.dto.DataService;
import com.clinbrain.bd.mdm.MetadataManage.entity.*;
import com.clinbrain.bd.mdm.MetadataManage.mapper.DataLineageMapper;
import com.clinbrain.bd.mdm.MetadataManage.mapper.DataServiceMapper;
import com.clinbrain.bd.mdm.MetadataManage.service.*;
import com.clinbrain.bd.mdm.MetadataManage.util.cost.MetaModuleConst;
import com.clinbrain.bd.mdm.MetadataManage.util.lineage.*;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.parser.common.entity.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 元数据管理服务
 */
@AllArgsConstructor
@Getter
@Service
public class DataServiceServiceImpl implements DataServiceService {
    private DataServiceMapper dataServiceMapper ;
    private final static Logger LOGGER = LoggerFactory.getLogger(DataServiceServiceImpl.class);
    @Override
    public String service(String projectName, String modelName, String serviceName, String version,Map<String,Object> paramMap) throws Exception{
        DataService dataService = dataServiceMapper.selectOneByCondition(projectName, modelName, serviceName, version);
        if("HTTP".equalsIgnoreCase(dataService.getServiceType())){
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpRequestBase http = null;
            if("GET".equalsIgnoreCase(dataService.getRequestType())){
                http = new HttpGet(dataService.getServiceUrl());
            }else if("POST".equalsIgnoreCase(dataService.getRequestType())){
                http = new HttpPost(dataService.getServiceUrl());
                if(paramMap!=null&&!paramMap.isEmpty()){
                    StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(paramMap));
                    ((HttpPost) http).setEntity(stringEntity);
                }
            }else if("DELETE".equalsIgnoreCase(dataService.getRequestType())){
                http = new HttpDelete(dataService.getServiceUrl());
            }
            CloseableHttpResponse response = httpClient.execute(http);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                String responseStr = EntityUtils.toString(responseEntity,StandardCharsets.UTF_8);
                return responseStr;
            }
        }/*else if("WSD".equalsIgnoreCase(dataService.getServiceType())){
        }else if("TCP".equalsIgnoreCase(dataService.getServiceType())){
        }*/
        return null;
    }
}

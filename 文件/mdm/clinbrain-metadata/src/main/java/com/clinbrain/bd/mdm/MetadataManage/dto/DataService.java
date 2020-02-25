package com.clinbrain.bd.mdm.MetadataManage.dto;

import lombok.Data;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.mdm.MetadataManage.entity.DataService
 * @createdDate 2019/9/23 15:07
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
@Data
public class DataService {
    private Integer projectId;
    private String projectResId;
    private Integer serviceId;
    private String serviceResId;
    private String projectCname;
    private String projectEname;
    private String modelCode;
    private String version;
    private String serviceCname;
    private String serviceEname;
    private Integer status;
    private String serviceType;
    private String serviceUrl;
    private String requestType;
    private String returnType;
    private String docUrl;
}

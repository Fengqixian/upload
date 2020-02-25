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
package com.clinbrain.bd.mdm.MetadataManage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
  * 元数据映射
  * @param
  * @return 
  * @author yjt
  * @date  2019/6/4 11:39 
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mdm_metadatamaping")
public class MetadataMapping extends Model<MetadataMapping> {
private static final long serialVersionUID = 1L;
    private String id;

    private String sourcemoduleid;

    private String sourcemetadataid;

    private String sourcemetadataname;

    private String sourcemetadataname_cn;

    private String targetmoduleid;

    private String targetmetadataid;

    private String targetmetadataname;

    private String targetmetadataname_cn;

    private Integer maptype;

    private Integer status;

    private String createuser;

    private String createdatetime;

    private String audituser;
    
    private String auditdatetime;

    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getSourcemoduleid() {
        return sourcemoduleid;
    }

    public void setSourcemoduleid(String sourcemoduleid) {
        this.sourcemoduleid = sourcemoduleid;
    }

    public String getSourcemetadataid(){
        return this.sourcemetadataid;
    }
    public void setSourcemetadataid(String sourcemetadataid) {
        this.sourcemetadataid = sourcemetadataid;
    }

    public String getSourcemetadataname() {
        return sourcemetadataname;
    }

    public void setSourcemetadataname(String sourcemetadataname) {
        this.sourcemetadataname = sourcemetadataname;
    }

    public String getSourcemetadataname_cn() {
        return sourcemetadataname_cn;
    }

    public void setSourcemetadataname_cn(String sourcemetadataname_cn) {
        this.sourcemetadataname_cn = sourcemetadataname_cn;
    }

    public String getTargetmoduleid() {
        return targetmoduleid;
    }

    public void setTargetmoduleid(String targetmoduleid) {
        this.targetmoduleid = targetmoduleid;
    }

    public String getTargetmetadataid() {
        return targetmetadataid;
    }

    public void setTargetmetadataid(String targetmetadataid) {
        this.targetmetadataid = targetmetadataid;
    }

    public String getTargetmetadataname() {
        return targetmetadataname;
    }

    public void setTargetmetadataname(String targetmetadataname) {
        this.targetmetadataname = targetmetadataname;
    }

    public String getTargetmetadataname_cn() {
        return targetmetadataname_cn;
    }

    public void setTargetmetadataname_cn(String targetmetadataname_cn) {
        this.targetmetadataname_cn = targetmetadataname_cn;
    }

    public Integer getMaptype() {
        return maptype;
    }

    public void setMaptype(Integer maptype) {
        this.maptype = maptype;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public String getCreatedatetime() {
        return createdatetime;
    }

    public void setCreatedatetime(String createdatetime) {
        this.createdatetime = createdatetime;
    }

    public String getAudituser() {
        return audituser;
    }

    public void setAudituser(String audituser) {
        this.audituser = audituser;
    }

    public String getAuditdatetime() {
        return auditdatetime;
    }

    public void setAuditdatetime(String auditdatetime) {
        this.auditdatetime = auditdatetime;
    }
  
}

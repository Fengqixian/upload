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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

/**
 * 元数据
 *
 * @author wangyi
 * @date 2019-05-29 14:41:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mdm_metadata_value")
public class MetadataValue extends Model<MetadataValue> {
private static final long serialVersionUID = 1L;

    /**
   * 自增ID
   */
    @TableId
    private Integer id;
    /**
   * 资源标识
   */
    private String resourceId;
    /**
   * 父ID
   */
    private String parentId;
    /**
   * 模型ID
   */
    private String modelId;
    /**
   * 中文名称
   */
    private String nameEn;
    /**
   * 英文名称
   */
    private String nameCn;
    /**
   * 模型类型
   */
    private String modelType;
    /**
   * 值域ID
   */
    private String rangeId;
    /**
   * 状态
   */
    private Integer status;
    /**
   * 创建时间
   */
    private LocalDateTime createTime;
    /**
   * 更新时间
   */
    private LocalDateTime updateTime;

    /**
      * 是否是根模型下的数据
     */
    private Boolean  isStandard;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc001;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc002;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc003;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc004;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc005;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc006;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc007;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc008;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc009;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc010;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc011;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc012;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc013;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc014;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc015;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc016;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc017;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc018;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc019;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc020;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc021;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc022;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc023;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc024;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc025;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc026;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc027;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc028;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc029;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc030;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc031;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc032;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc033;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc034;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc035;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc036;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc037;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc038;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc039;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc040;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc041;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc042;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc043;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc044;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc045;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc046;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc047;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc048;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc049;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc050;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc051;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc052;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc053;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc054;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc055;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc056;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc057;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc058;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc059;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc060;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc061;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc062;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc063;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc064;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc065;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc066;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc067;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc068;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc069;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc070;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc071;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc072;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc073;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc074;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc075;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc076;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc077;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc078;
    /**
   * 50长度扩展字段
   */
    private String exVarcharL50Vc079;
    /**
   * 50长度扩展字段
   */
    //private String exVarcharL50Vc080;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc001;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc002;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc003;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc004;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc005;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc006;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc007;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc008;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc009;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc010;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc011;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc012;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc013;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc014;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc015;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc016;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc017;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc018;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc019;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc020;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc021;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc022;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc023;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc024;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc025;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc026;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc027;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc028;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc029;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc030;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc031;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc032;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc033;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc034;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc035;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc036;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc037;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc038;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc039;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc040;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc041;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc042;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc043;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc044;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc045;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc046;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc047;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc048;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc049;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc050;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc051;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc052;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc053;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc054;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc055;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc056;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc057;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc058;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc059;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc060;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc061;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc062;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc063;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc064;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc065;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc066;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc067;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc068;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc069;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc070;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc071;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc072;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc073;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc074;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc075;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc076;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc077;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc078;
    /**
   * 100长度扩展字段
   */
    private String exVarcharL100Vc079;
    /**
   * 100长度扩展字段
   */
    //private String exVarcharL100Vc080;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt001;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt002;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt003;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt004;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt005;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt006;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt007;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt008;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt009;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt010;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt011;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt012;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt013;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt014;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt015;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt016;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt017;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt018;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt019;
    /**
   * 时间扩展字段
   */
    private LocalDateTime exDatetimeDt020;
    /**
   * 大文本扩展字段
   */
    private String exTextT001;
    /**
   * 大文本扩展字段
   */
    private String exTextT002;
    /**
   * 大文本扩展字段
   */
    private String exTextT003;
    /**
   * 大文本扩展字段
   */
    private String exTextT004;
    /**
   * 大文本扩展字段
   */
    private String exTextT005;
    /**
   * 大文本扩展字段
   */
    private String exTextT006;
    /**
   * 大文本扩展字段
   */
    private String exTextT007;
    /**
   * 大文本扩展字段
   */
    private String exTextT008;
    /**
   * 大文本扩展字段
   */
    private String exTextT009;
    /**
   * 大文本扩展字段
   */
    private String exTextT010;
    /**
   * 大文本扩展字段
   */
    private String exTextT011;
    /**
   * 大文本扩展字段
   */
    private String exTextT012;
    /**
   * 大文本扩展字段
   */
    private String exTextT013;
    /**
   * 大文本扩展字段
   */
    private String exTextT014;
    /**
   * 大文本扩展字段
   */
    private String exTextT015;
    /**
   * 大文本扩展字段
   */
    private String exTextT016;
    /**
   * 大文本扩展字段
   */
    private String exTextT017;
    /**
   * 大文本扩展字段
   */
    private String exTextT018;
    /**
   * 大文本扩展字段
   */
    private String exTextT019;
    /**
   * 大文本扩展字段
   */
    private String exTextT020;
    /**
   * 大文本扩展字段
   */
    private String exTextT021;
    /**
   * 大文本扩展字段
   */
    private String exTextT022;
    /**
   * 大文本扩展字段
   */
    private String exTextT023;
    /**
   * 大文本扩展字段
   */
    private String exTextT024;
    /**
   * 大文本扩展字段
   */
    private String exTextT025;
    /**
   * 大文本扩展字段
   */
    private String exTextT026;
    /**
   * 大文本扩展字段
   */
    private String exTextT027;
    /**
   * 大文本扩展字段
   */
    private String exTextT028;
    /**
   * 大文本扩展字段
   */
    private String exTextT029;
  
}


package com.clinbrain.bd.mdm.strategy.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 规则列表
 */
@Data
public class RuleInfoDto {
	private static final long serialVersionUID = 1L;
	/**
	 * 标识
	 */
	private Integer id;
	/**
	 * 规则名称
	 */
	private String ruleName;
	/**
	 * 规则类型 0：脱敏 1:标签
	 */
	private String ruleType;
	/**
	 * 规则详情标识
	 */
	private int ruleId;

	/**
	 * 备注
	 */
	private String remarks;

	/**
	 * 状态 0：启用 1：停用
	 */
	private Integer status;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;


	//脱敏规则存放
	private String ruleExpression;

	//脱敏替换值
	private String sign;

	/**
	 * REG:正则， AES, DES, MD5
	 */
	private String encryptionType;


	//=============================================
	/**
	 * 组装字段表达式
	 */
	private String fields;

	/**
	 * 组装表表达式
	 */
	private String tables;

	/**
	 * 组装条件表达式
	 */
	private String conditions;

	/**
	 * sql语句
	 */
	private String sqlInfo;

	/**
	 *  0：组装类型 1:sql类型
	 */
	private Integer type;


}

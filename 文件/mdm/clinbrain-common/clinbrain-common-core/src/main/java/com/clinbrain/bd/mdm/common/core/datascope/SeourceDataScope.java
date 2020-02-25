
package com.clinbrain.bd.mdm.common.core.datascope;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.List;

/**
 * @author lianglele
 * @date 2019/2/1
 * 数据权限查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SeourceDataScope extends HashMap {
	/**
	 * 限制范围的字段名称
	 */
	private String scopeName = "roleId";

	/**
	 * 具体的数据范围
	 */
	private List<Integer> roleIds;

	/**
	 * 是否只查询自己有权限看的
	 */
	private Boolean isOnly = false;
}

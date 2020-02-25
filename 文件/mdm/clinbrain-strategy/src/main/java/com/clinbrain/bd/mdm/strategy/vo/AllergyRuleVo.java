/*
 *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  <p>
 *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 * https://www.gnu.org/licenses/lgpl.html
 *  <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clinbrain.bd.mdm.strategy.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 规则列表
 */
@Data
public class AllergyRuleVo implements Serializable {
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
    private String ruleId;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 状态 1：启用 2：停用
     */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 规则表达式
     */
    private String ruleExpression;

    /**
     * 替换值
     */
    private String sign;

    private String strategyId;

    /**
     * 加密方式
     */
    private String encryptionType;

    /**
     * 秘钥
     */
    public String secretKey;
}

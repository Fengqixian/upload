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

package com.clinbrain.bd.mdm.strategy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 规则列表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("rule_info")
public class RuleInfo extends Model<RuleInfo> {
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
    private Integer ruleType = -1;
    /**
     * 规则详情标识
     */
    private Integer ruleId;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 状态 1：启用 2：停用
     */
    private Integer status;

    /**
     * REG:正则， AES, DES, MD5
     */
    private String encryptionType;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


    private String resourceId;
}

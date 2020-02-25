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
 * 脱敏规则详情
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("allergy_rule")
public class AllergyRule extends Model<AllergyRule> {
    private static final long serialVersionUID = 1L;
    /**
     * 标识
     */
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 规则表达式
     */
    private String ruleExpression;

    /**
     * 备注
     */
    private String remarks;
    /**
     * 状态
     */
    private Integer status;


    /**
     * 替换值
     */
    private String sign;

}

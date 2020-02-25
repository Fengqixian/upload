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

import java.util.List;

/**
 * sql组装规则详情表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assemble_rule")
public class AssembleRule extends Model<AssembleRule> {
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
     * 类型
     */
    private Integer type;
    /**
     * 字段

     * "columns":{
     * "fields":[
     * {
     * "field":"字段A",
     * "table":"表A",
     * "alias":"别名A"
     * },
     * {
     * "field":"字段B",
     * "table":"表B",
     * "alias":"别名B"
     * }
     * ],
     * "relation":[{
     * "from":"A字段",
     * "to":"B字段",
     * "relat":"+,-,*,/,count,avg"
     * }]
     * }
     */
    private String fields;
    /**
     * 表
     * "from":[
     * {
     * "table":"表A",
     * "alias":"别名A",
     * "join":"左连接"
     * },
     * {
     * "table":"表B",
     * "alias":"别名B"
     * }
     * ],
     */
    private String tables;
    /**
     * 条件
     * "where":[
     * {
     * "filed":"字段A",
     * "oper":"等于",
     * "value":"${替代标签}"
     * }
     * ]
     */
    private String conditions;

    /**
     * SQL
     */
    private String sqlInfo;

    /**
     * 备注
     */
    private String remarks;
}

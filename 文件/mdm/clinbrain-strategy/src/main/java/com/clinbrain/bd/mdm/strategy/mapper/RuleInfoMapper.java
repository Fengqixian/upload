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

package com.clinbrain.bd.mdm.strategy.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clinbrain.bd.mdm.strategy.entity.RuleInfo;
import com.clinbrain.bd.mdm.strategy.vo.AllergyRuleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 代码生成器
 *
 * @author lengleng
 * @date 2019/2/1
 */
public interface RuleInfoMapper  extends BaseMapper<RuleInfo> {


    /**
     * 根据策略用户标识查询对应规则
     * @param userId 策略信息
     * @return
     */
    List<RuleInfo> listStrategyUserByRule(@Param("userId") String userId);

    /**
     * 根据策略标识查询对应规则
     * @param strategyId 策略信息
     * @return
     */
    List<RuleInfo> listStrategyByRule(@Param("strategyId") String strategyId);

    /**
     * 根据角色和应用标识查询用户
     * @param roleId 角色
     * @param systemId 应用
     * @return 规则
     */
    List<AllergyRuleVo> listRuleBySystemRule(@Param("roleId") String roleId, @Param("systemId") String systemId);


}

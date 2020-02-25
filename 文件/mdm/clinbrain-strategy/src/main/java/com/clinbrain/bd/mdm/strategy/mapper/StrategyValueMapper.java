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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.strategy.entity.StrategyValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 代码生成器
 *
 * @author lengleng
 * @date 2019/2/1
 */
public interface StrategyValueMapper extends BaseMapper<StrategyValue> {

    /**
     * 获取策略对应用户
     * @param ruleId 策略标识
     * @return
     */
    List<StrategyValue> listRuleByValue(@Param("ruleId") String ruleId, @Param("strategyId") String strategyId);

    /**
     * 根据策略id删除策略值
     * @param strategyId 策略id
     */
    void deleteStrategyValueByStrategyId(@Param("strategyId") String strategyId);


    /**
     * 获取策略对应用户
     * @param ruleId 策略标识
     * @return
     */
    IPage<StrategyValue> listRuleByValuePage(Page page, @Param("ruleId") String ruleId, @Param("strategyId") String strategyId);


}

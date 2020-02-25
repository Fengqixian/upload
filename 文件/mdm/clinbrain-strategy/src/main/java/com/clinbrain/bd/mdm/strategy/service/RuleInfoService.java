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

package com.clinbrain.bd.mdm.strategy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.clinbrain.bd.mdm.strategy.dto.RuleInfoDto;
import com.clinbrain.bd.mdm.strategy.entity.RuleInfo;
import com.clinbrain.bd.mdm.strategy.vo.AllergyRuleVo;
import com.clinbrain.bd.mdm.strategy.vo.RuleInfoVo;

import java.util.List;


/**
 * @author 乐乐
 * @date 2019/2/1
 */
public interface RuleInfoService extends IService<RuleInfo>{

	/**
	 * 分页查询
	 *
	 * @param RuleInfo 表名
	 * @return
	 */
	IPage<RuleInfo> queryPage(Page page, RuleInfo strategyInfo);


	/**
	 * 根据策略标识查询对应规则
	 * @param strategyId 策略信息
	 * @return
	 */
	List<RuleInfo> listStrategyByRule(String strategyId);

	/**
	 * 根据策略用户标识查询对应规则
	 * @param listStrategyUserByRule 策略信息
	 * @return
	 */
	List<RuleInfo> listStrategyUserByRule(String userId);

	/**
	 * 创建规则
	 * @param ruleInfo 规则信息
	 */
	Boolean saveTypeRuleInfo(RuleInfoDto ruleInfo);


	/**
	 * 修改规则
	 * @param ruleInfoDto
	 * @return
	 */
	Boolean updateTypeRuleInfo(RuleInfoDto ruleInfoDto);

	/**
	 * 根据标识删除所有状态
	 * @param id 规则标识
	 * @return
	 */
	Boolean deleteRuleById(String id);

	/**
	 * 根据规则对象查询规则详情
	 * @param ruleInfo 规则信息
	 * @return 规则详情
	 */
	RuleInfoDto getTypeRule(RuleInfo ruleInfo);

	List<AllergyRuleVo> listRuleBySystemRule(String roleId, String systemId);
}

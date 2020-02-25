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

import com.baomidou.mybatisplus.extension.service.IService;
import com.clinbrain.bd.mdm.strategy.entity.StrategyUser;
import com.clinbrain.bd.mdm.strategy.vo.StrategyUserVo;

import java.util.List;


/**
 * @author lengleng
 * @date 2019/2/1
 */
public interface StrategyUserService extends IService<StrategyUser>{
	/**
	 * 获取策略对应用户
	 * @param strategyId 策略标识
	 * @return
	 */
	List<StrategyUser> listStrategyByUser(String strategyId);
}

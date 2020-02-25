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
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.strategy.dto.StrategyInfoDto;
import com.clinbrain.bd.mdm.strategy.entity.StrategyInfo;
import com.clinbrain.bd.mdm.strategy.vo.StrategyLabelVO;
import com.clinbrain.bd.mdm.strategy.vo.StrategyVO;

import java.util.List;
import java.util.Map;


/**
 * @author lengleng
 * @date 2019/2/1
 */
public interface StrategyInfoService extends IService<StrategyInfo> {

    /**
     * 分页查询
     *
     * @param strategyInfo 表名
     * @return
     */
    IPage<StrategyInfo> queryPage(Page page, StrategyInfo strategyInfo);

    /**
     * 获取策略信息
     *
     * @param strInfo 策略信息
     * @return
     */
    IPage<StrategyVO> selectStrategyVo(Page page, StrategyInfoDto strInfo);


    /**
     * 获取策略信息
     *
     * @param strInfo 策略信息
     * @return
     */
    IPage<StrategyLabelVO> selectStrategyLabelVo(Page page, StrategyInfoDto strInfo);

    /**
     * 解析json创建策略
     *
     * @param jsonString 策略信息
     */
    Boolean saveStrategy(StrategyVO strategyVO);

    /**
     * 修改策略
     *
     * @param strategyVO 策略对象
     * @return
     */
    Boolean updateStrategy(StrategyVO strategyVO);


    /**
     * 解析json创建策略标签
     *
     * @param strategyLabelVO 策略信息
     */
    Boolean saveStrategyLabel(StrategyLabelVO strategyLabelVO);

    /**
     * 修改标签策略
     *
     * @param strategyLabelVO 策略对象
     * @return
     */
    Boolean updateStrategyLabel(StrategyLabelVO strategyLabelVO);


    Object getRegDataList(Object data, String roleId, String systemId, boolean type);

    /**
     * 获取标签规则sql
     * @param sourceId 标签标识
     * @param valueMap 替换值
     * @return
     */
    R getLabelSql(String sourceId, Map<String, Object> valueMap);
}

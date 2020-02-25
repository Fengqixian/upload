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
import com.clinbrain.bd.mdm.strategy.dto.StrategyInfoDto;
import com.clinbrain.bd.mdm.strategy.entity.StrategyInfo;
import com.clinbrain.bd.mdm.strategy.vo.StrategyLabelVO;
import com.clinbrain.bd.mdm.strategy.vo.StrategyVO;
import org.apache.ibatis.annotations.Param;


/**
 * 代码生成器
 *
 * @author lengleng
 * @date 2019/2/1
 */
public interface StrategyInfoMapper extends BaseMapper<StrategyInfo> {

    /**
     *  获取策略信息
     * @param strInfo 策略信息
     * @return
     */
    IPage<StrategyVO> selectStrategyVo(Page page,  @Param("strInfo") StrategyInfoDto strInfo);

    /**
     *  获取标签策略信息
     * @param strInfo 策略信息
     * @return
     */
    IPage<StrategyLabelVO> selectStrategyLabelVo(Page page, @Param("strInfo") StrategyInfoDto strInfo);
}

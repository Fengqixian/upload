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

package com.clinbrain.bd.mdm.strategy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.strategy.service.StrategyValueService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 策略
 *
 * @author lengleng
 * @date 2019/2/1
 */
@RestController
@AllArgsConstructor
@RequestMapping("/strategyValue")
public class StrategyValueController {
    private final StrategyValueService strategyValueService;

    /**
     * 列表
     *
     * @param strategyId 参数集
     * @return 数据库表
     */
    @GetMapping("/page")
    public R<IPage> list(Page page, String ruleId, String strategyId) {
        return new R<>(strategyValueService.queryPage(page, ruleId, strategyId));
    }

}

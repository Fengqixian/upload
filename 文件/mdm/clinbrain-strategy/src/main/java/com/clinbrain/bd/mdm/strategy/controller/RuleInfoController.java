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
import com.clinbrain.bd.mdm.common.log.annotation.SysLog;
import com.clinbrain.bd.mdm.strategy.dto.RegDto;
import com.clinbrain.bd.mdm.strategy.dto.RuleInfoDto;
import com.clinbrain.bd.mdm.strategy.entity.AssembleRule;
import com.clinbrain.bd.mdm.strategy.entity.RuleInfo;
import com.clinbrain.bd.mdm.strategy.service.RuleInfoService;
import com.clinbrain.bd.mdm.strategy.util.PackagingUtil;
import com.clinbrain.bd.mdm.strategy.util.ReplaceUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 规则
 *
 * @author lengleng
 * @date 2019/2/1
 */
@RestController
@AllArgsConstructor
@RequestMapping("/ruleInfo")
public class RuleInfoController {
    private final RuleInfoService ruleInfoService;


    /**
     * 列表
     *
     * @param ruleInfo 参数集
     * @return 数据库表
     */
    @GetMapping("/page")
    public R<IPage> list(Page page, RuleInfo ruleInfo) {
        return new R<>(ruleInfoService.queryPage(page, ruleInfo));
    }

    /**
     * 详情
     *
     * @param ruleInfo 参数集
     * @return 数据库表
     */
    @GetMapping("/getTypeRule")
    public R getTypeRule(RuleInfo ruleInfo) {
        return new R<>(ruleInfoService.getTypeRule(ruleInfo));
    }

    /**
     * @param ruleInfo
     * @return
     */
    @SysLog("添加规则信息")
    @PostMapping("/save")
    public R save(@RequestBody RuleInfoDto ruleInfo) {
        return new R<>(ruleInfoService.saveTypeRuleInfo(ruleInfo));
    }


    @SysLog("修改规则信息")
    @PutMapping("/update")
    public R update(@RequestBody RuleInfoDto ruleInfo) {
        return new R<>(ruleInfoService.updateTypeRuleInfo(ruleInfo));
    }


    @SysLog("删除规则信息")
    @DeleteMapping("/{id}")
    public R delete(@PathVariable String id) {
        return new R<>(ruleInfoService.deleteRuleById(id));
    }

    /**
     * 验证脱敏规则
     *
     * @param regValue   验证值
     * @param regDtoList 过敏规则
     * @return
     */
    @GetMapping("/getRuleValue")
    public R getAllRule(List<Map<String, Object>> regValue, List<RegDto> regDtoList) {
        return new R<>(ReplaceUtil.dataListToRegList(regValue, regDtoList));
    }

    /**
     * 解析组装规则
     *
     * @param assembleRule 组装规则对象
     * @param map          验证值
     * @return sql
     */
    @GetMapping("/getAssRuleValue")
    public R getAssRule(AssembleRule assembleRule, Map<String, Object> map) {
        return new R<>(PackagingUtil.getDataToSql(assembleRule.getFields(), assembleRule.getTables(), assembleRule.getConditions(), map));
    }

}

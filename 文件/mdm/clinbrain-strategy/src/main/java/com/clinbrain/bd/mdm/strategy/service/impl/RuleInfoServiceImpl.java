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

package com.clinbrain.bd.mdm.strategy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.mdm.strategy.dto.RuleInfoDto;
import com.clinbrain.bd.mdm.strategy.entity.AllergyRule;
import com.clinbrain.bd.mdm.strategy.entity.AssembleRule;
import com.clinbrain.bd.mdm.strategy.entity.RuleInfo;
import com.clinbrain.bd.mdm.strategy.mapper.RuleInfoMapper;
import com.clinbrain.bd.mdm.strategy.service.AllergyRuleService;
import com.clinbrain.bd.mdm.strategy.service.AssembleRuleService;
import com.clinbrain.bd.mdm.strategy.service.RuleInfoService;
import com.clinbrain.bd.mdm.strategy.util.Base64Util;
import com.clinbrain.bd.mdm.strategy.util.PackagingUtil;
import com.clinbrain.bd.mdm.strategy.vo.AllergyRuleVo;
import com.clinbrain.bd.mdm.strategy.vo.RuleInfoVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


/**
 * 规则
 *
 * @author lengleng
 * @date 2019/2/1
 */
@Slf4j
@Service
@AllArgsConstructor
public class RuleInfoServiceImpl extends ServiceImpl<RuleInfoMapper, RuleInfo> implements RuleInfoService {

    /**
     * 脱敏规则
     */
    private final AllergyRuleService allergyRuleService;

    /**
     * 组装规则
     */
    private final AssembleRuleService assembleRuleService;

    /**
     * 分页查询表
     *
     * @param ruleInfo 查询条件
     * @return
     */
    @Override
    public IPage<RuleInfo> queryPage(Page page, RuleInfo ruleInfo) {
        Wrapper var = null;
        if (ruleInfo != null) {
            if (StrUtil.isNotEmpty(ruleInfo.getRuleName()) && ruleInfo.getStatus() != null && ruleInfo.getStatus() > 0) {
                var = Wrappers.<RuleInfo>query().lambda()
                        .like(RuleInfo::getRuleName, ruleInfo.getRuleName()).eq(RuleInfo::getStatus, ruleInfo.getStatus());
            } else if (StrUtil.isNotEmpty(ruleInfo.getRuleName())) {
                var = Wrappers.<RuleInfo>query().lambda()
                        .like(RuleInfo::getRuleName, ruleInfo.getRuleName());
            } else if (ruleInfo.getStatus() != null && ruleInfo.getStatus() > 0) {
                var = Wrappers.<RuleInfo>query().lambda().eq(RuleInfo::getStatus, ruleInfo.getStatus());
            }
            if (ruleInfo.getRuleType() > -1) {
                var = Wrappers.<RuleInfo>query().lambda().eq(RuleInfo::getRuleType, ruleInfo.getRuleType());
            }

        }
        return baseMapper.selectPage(page, var);
    }


    /**
     * 根据规则对象查询规则详情
     *
     * @param ruleInfo 规则信息
     * @return 规则详情
     */
    @Override
    public RuleInfoDto getTypeRule(RuleInfo ruleInfo) {
        RuleInfoDto infoDto = new RuleInfoDto();
        if (ruleInfo != null && ruleInfo.getId() > 0) {
            ruleInfo = baseMapper.selectById(ruleInfo.getId());
            BeanUtil.copyProperties(ruleInfo, infoDto);

            if (ruleInfo == null) {
                return null;
            }
            if (ruleInfo.getRuleType() == 0) {
                AllergyRule aRule = allergyRuleService.getById(ruleInfo.getRuleId());
                if ("REG".equalsIgnoreCase(infoDto.getEncryptionType()) && StrUtil.isNotEmpty(aRule.getRuleExpression())) {
                    aRule.setRuleExpression(Base64Util.Base64ToDataDec(aRule.getRuleExpression()));
                }
                BeanUtil.copyProperties(aRule, infoDto);
                infoDto.setRuleId(aRule.getId());
                infoDto.setId(ruleInfo.getId());
            } else if (ruleInfo.getRuleType() == 1) {
                AssembleRule sRule = assembleRuleService.getById(ruleInfo.getRuleId());
                BeanUtil.copyProperties(sRule, infoDto);
                infoDto.setRuleId(sRule.getId());
                infoDto.setId(ruleInfo.getId());
            }
        }
        return infoDto;
    }


    /**
     * 根据策略用户标识查询对应规则
     *
     * @param userId 策略信息
     * @return
     */
    @Override
    public List<RuleInfo> listStrategyUserByRule(String userId) {
        return baseMapper.listStrategyUserByRule(userId);
    }

    /**
     * 根据策略标识查询对应规则
     *
     * @param strategyId 策略信息
     * @return
     */
    @Override
    public List<RuleInfo> listStrategyByRule(String strategyId) {
        return baseMapper.listStrategyByRule(strategyId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean saveTypeRuleInfo(RuleInfoDto ruleInfo) {
        try {
            if (ruleInfo != null) {
                RuleInfo info = new RuleInfo();
                BeanUtil.copyProperties(ruleInfo, info);

                //创建脱敏规则
                if (info.getRuleType() == 0) {
                    AllergyRule aRule = new AllergyRule();
                    BeanUtil.copyProperties(ruleInfo, aRule);
                    aRule.setName(info.getRuleName());
                    if (StrUtil.isNotEmpty(aRule.getRuleExpression()) && "Reg".equalsIgnoreCase(info.getEncryptionType())) {
                        aRule.setRuleExpression(Base64Util.DataToBase64Enc(aRule.getRuleExpression()));
                    }
//                    aRule.setId(UUID.randomUUID().toString());
                    allergyRuleService.save(aRule);
                    info.setRuleId(aRule.getId());
                    //组装规则
                } else if (info.getRuleType() == 1) {
                    AssembleRule eRule = new AssembleRule();
                    BeanUtil.copyProperties(ruleInfo, eRule);
//                    eRule.setId(UUID.randomUUID().toString());
                    eRule.setName(info.getRuleName());
                    if (ruleInfo.getType() != 1) {
                        String sql = PackagingUtil.getDataToSql(eRule.getFields(), eRule.getTables(), eRule.getConditions(), null);
                        eRule.setSqlInfo(sql);
                    }
                    assembleRuleService.save(eRule);
                    info.setRuleId(eRule.getId());
                }

//                info.setId(UUID.randomUUID().toString());
                info.setCreateTime(LocalDateTime.now());
                info.setResourceId(UUID.randomUUID().toString());
                //创建
                this.save(info);
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateTypeRuleInfo(RuleInfoDto ruleInfoDto) {
        try {
            if (ruleInfoDto != null) {
                RuleInfo info = new RuleInfo();
                BeanUtil.copyProperties(ruleInfoDto, info);

                //创建脱敏规则
                if (info.getRuleType() == 0) {
                    AllergyRule aRule = new AllergyRule();
                    BeanUtil.copyProperties(ruleInfoDto, aRule);
                    aRule.setId(info.getRuleId());
                    if (StrUtil.isNotEmpty(aRule.getRuleExpression()) && "Reg".equalsIgnoreCase(info.getEncryptionType())) {
                        aRule.setRuleExpression(Base64Util.DataToBase64Enc(aRule.getRuleExpression()));
                    }
                    allergyRuleService.updateById(aRule);
                    //组装规则
                } else if (info.getRuleType() == 1) {
                    AssembleRule eRule = new AssembleRule();
                    BeanUtil.copyProperties(ruleInfoDto, eRule);
                    eRule.setId(info.getRuleId());
                    if (ruleInfoDto.getType() != 1) {
                        String sql = PackagingUtil.getDataToSql(eRule.getFields(), eRule.getTables(), eRule.getConditions(), null);
                        eRule.setSqlInfo(sql);
                    }

                    assembleRuleService.updateById(eRule);
                }
                info.setUpdateTime(LocalDateTime.now());
                //创建
                this.updateById(info);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deleteRuleById(String id) {
        try {
            RuleInfo info = this.getById(id);
            if (info != null) {
                //创建脱敏规则
                if (info.getRuleType() == 0) {
                    allergyRuleService.removeById(info.getRuleId());
                    //组装规则
                } else if (info.getRuleType() == 1) {
                    assembleRuleService.removeById(info.getRuleId());
                }
                //删除
                this.removeById(info.getId());
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public List<AllergyRuleVo> listRuleBySystemRule(String roleId, String systemId) {
        return baseMapper.listRuleBySystemRule(roleId, systemId);
    }
}

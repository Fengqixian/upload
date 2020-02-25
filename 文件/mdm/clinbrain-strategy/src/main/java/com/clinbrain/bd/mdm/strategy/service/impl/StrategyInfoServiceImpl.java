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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.strategy.dto.StrategyInfoDto;
import com.clinbrain.bd.mdm.strategy.entity.*;
import com.clinbrain.bd.mdm.strategy.mapper.StrategyInfoMapper;
import com.clinbrain.bd.mdm.strategy.service.*;
import com.clinbrain.bd.mdm.strategy.util.PackagingUtil;
import com.clinbrain.bd.mdm.strategy.util.ReplaceUtil;
import com.clinbrain.bd.mdm.strategy.vo.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 代码生成器
 *
 * @author lengleng
 * @date 2019/2/1
 */
@Slf4j
@Service
@AllArgsConstructor
public class StrategyInfoServiceImpl extends ServiceImpl<StrategyInfoMapper, StrategyInfo> implements StrategyInfoService {

    private final RuleInfoService ruleInfoService;

    private final StrategyUserService strategyUserService;


    private final StrategyValueService strategyValueService;

    private final StrategyOrRuleService strategyOrRuleService;
    private final AssembleRuleService assembleRuleService;

    /**
     * 分页查询表
     *
     * @param strategyInfo 查询条件
     * @return
     */
    @Override
    public IPage<StrategyInfo> queryPage(Page page, StrategyInfo strategyInfo) {
        Wrapper var = null;
        if (strategyInfo != null && StrUtil.isNotEmpty(strategyInfo.getName())) {
            var = Wrappers.<StrategyInfo>query().lambda()
                    .eq(StrategyInfo::getType, 2).like(StrategyInfo::getName, strategyInfo.getName());
        } else {
            var = Wrappers.<StrategyInfo>query().lambda().eq(StrategyInfo::getType, 2);
        }
        return baseMapper.selectPage(page, var);
    }

    /**
     * 获取策略信息
     *
     * @param strInfo 策略信息
     * @return
     */
    @Override
    public IPage<StrategyVO> selectStrategyVo(Page page, StrategyInfoDto strInfo) {
        IPage<StrategyVO> pageData = baseMapper.selectStrategyVo(page, strInfo);
//        List<StrategyVO> strategyList = new ArrayList<>();
        if (pageData != null && pageData.getRecords() != null) {
            pageData.getRecords().stream().forEach(s -> {
                s.setPageType(strInfo.getPageType());
//                StrategyVO strategyVO = new StrategyVO();
//                BeanUtil.copyProperties(s, strategyVO);
                List<StrategyUserVo> userVoList = this.getUserVoList(s);
                s.setChildren(userVoList);
//                strategyList.add(strategyVO);
            });
        }
        return pageData;
    }


    /**
     * 获取策略信息
     *
     * @param strInfo 策略信息
     * @return
     */
    @Override
    public IPage<StrategyLabelVO> selectStrategyLabelVo(Page page, StrategyInfoDto strInfo) {
        IPage<StrategyLabelVO> pageData = baseMapper.selectStrategyLabelVo(page, strInfo);
        if (pageData != null && pageData.getRecords() != null) {
            pageData.getRecords().stream().forEach(s -> {
                List<RuleInfoVo> RuleVoList = this.getUserByRule(null, s.getId() + "", s.getName(), strInfo.getPageType());
                s.setChildren(RuleVoList);
            });
        }
        return pageData;
    }

    /**
     * 根据策略查询用户
     *
     * @return
     */
    private List<StrategyUserVo> getUserVoList(StrategyVO strategyVO) {
        //查询用户
        List<StrategyUser> userList = strategyUserService.listStrategyByUser(strategyVO.getId() + "");
        List<StrategyUserVo> userVoList = new ArrayList<>();
        if (userList != null && userList.size() > 0) {
            userList.stream().forEach(u -> {
                StrategyUserVo userVo = new StrategyUserVo();
                BeanUtil.copyProperties(u, userVo);
                //根据用户查询规则
                List<RuleInfoVo> ruleInfoList = this.getUserByRule(userVo, strategyVO.getId() + "", strategyVO.getName(), strategyVO.getPageType());
                userVo.setChildren(ruleInfoList);
                userVoList.add(userVo);
            });

//            // 用户层
//            strategyVO.setChildren(userVoList);
        }

        return userVoList;
    }

    /**
     * 根据用户标识获取规则
     *
     * @param userVo
     * @return
     */
    private List<RuleInfoVo> getUserByRule(StrategyUserVo userVo, String strategyId, String strategyName, Integer pageType) {

        //规则
        List<RuleInfo> ruleInfoList = null;
        if (userVo == null || userVo.getId() <= 0) {
            ruleInfoList = ruleInfoService.listStrategyByRule(strategyId);
        } else {
            ruleInfoList = ruleInfoService.listStrategyUserByRule(userVo.getId() + "");
        }

        List<RuleInfoVo> ruleInfoVoList = new ArrayList<>();
        if (ruleInfoList != null && ruleInfoList.size() > 0) {
            ruleInfoList.stream().forEach(r -> {
                RuleInfoVo infoVo = new RuleInfoVo();
                BeanUtil.copyProperties(r, infoVo);

                //查询值
                List<StrategyValue> valueList = null;
                if (pageType != null && pageType == 1) {
                    valueList = this.getRuleByValue(infoVo, strategyId);
                } else {
                    valueList = this.getRuleByValuePage(infoVo, strategyId, strategyName);
                }

                if ("0".equals(infoVo.getRuleType())) {
                    infoVo.setNodeType("desensitization");
                } else {
                    infoVo.setNodeType("sign");
                }
                infoVo.setChildren(valueList);
                // 对应值
                ruleInfoVoList.add(infoVo);
            });

//            userVo.setChildren(ruleInfoVoList);
        }
        return ruleInfoVoList;
    }

    /**
     * 根据规则查询对应值
     *
     * @param infoVo 规则信息
     * @return
     */
    private List<StrategyValue> getRuleByValue(RuleInfoVo infoVo, String strategyId) {
        return strategyValueService.listRuleByValue(infoVo.getId() + "", strategyId);
    }


    /**
     * 根据规则查询对应值 分页
     *
     * @param infoVo 规则信息
     * @return
     */
    private List<StrategyValue> getRuleByValuePage(RuleInfoVo infoVo, String strategyId, String strategyName) {
        Page page = new Page();
        page.setSize(10);
        IPage<StrategyValue> dataPages = strategyValueService.queryPage(page, infoVo.getId() + "", strategyId);
        if (dataPages != null && dataPages.getRecords().size() > 0) {
            StrategyValue value = new StrategyValue();
            value.setColumnNameCn("更多...");
            value.setColumnNameEn(strategyName);
            value.setId(infoVo.getId());
            value.setStrategicCond(strategyId);
            value.setNodeType("more");
            dataPages.getRecords().add(value);
            return dataPages.getRecords();
        }
        return null;
    }


    /**
     * 解析前台的json然后创建策略
     *
     * @param strategyVO json字符串分4层
     */
    @Transactional
    @Override
    public Boolean saveStrategy(StrategyVO strategyVO) {

        try {
            /**
             * 转换对象
             */
            StrategyVO obj = strategyVO;
//                    JSON.parseObject(jsonString, StrategyVO.class);


            //策略
//        StrategyInfo sInfo = BeanUtils.mapToBean(obj, StrategyInfo.class);

//        //获取子节点
//        JSONArray array = JSONArray.parseArray(obj.getChildren());
//
//        List<Map<String, Object>> tList = JSON.
//        //转换用户
//        List<StrategyUser>  sUser = BeanUtils.mapsToBeans((List<Map<String,Object>>) array, StrategyUser.class);

            if (obj != null) {

                //获取策略
                StrategyInfo sInfo = new StrategyInfo();
                BeanUtil.copyProperties(obj, sInfo);
                sInfo.setCreateTime(LocalDateTime.now());
                this.save(sInfo);

                StrategyOrRule strategyOrRule = new StrategyOrRule();
                strategyOrRule.setStrategyId(sInfo.getId() + "");


                //用户vo
                List<StrategyUserVo> userVoList = obj.getChildren();
                if (userVoList != null && userVoList.size() > 0) {
                    userVoList.stream().forEach(uv -> {
                        //用户
                        StrategyUser user = new StrategyUser();
                        BeanUtil.copyProperties(uv, user);
                        user.setResourceId(UUID.randomUUID().toString());
                        strategyUserService.save(user);
                        //得到规则
                        List<RuleInfoVo> ruleVoList = uv.getChildren();

                        strategyOrRule.setUserId(user.getId() + "");

                        if (ruleVoList != null && ruleVoList.size() > 0) {
                            ruleVoList.forEach(rv -> {
                                strategyOrRule.setRuleId(rv.getId() + "");
                                /**
                                 * 策略对应值
                                 */
                                if (rv.getChildren() != null && rv.getChildren().size() > 0) {
                                    strategyValueService.saveBatch(rv.getChildren());

                                    //创建关系
                                    rv.getChildren().forEach(vl -> {
                                        strategyOrRule.setStrValueId(vl.getId() + "");
                                        strategyOrRuleService.save(strategyOrRule);
                                    });

                                }
                            });
                        }
                    });

//
//                    //记录规则标识
//                    List<Integer> ruleId = new ArrayList<>();
//                    obj.getChildren().stream().forEach(u -> {
//                        List<RuleInfo> ruleInfoList = new ArrayList<>();
//                        BeanUtil.copyProperties(u.getChildren(), ruleInfoList);
//                        if (ruleInfoList != null && ruleInfoList.size() > 0) {
//                            u.getChildren().stream().filter(v -> v.getChildren() != null && v.getChildren().size() > 0).forEach(v -> {
//                                strategyValueService.saveBatch(v.getChildren());
//                            });
//
//                            ruleInfoService.saveBatch(ruleInfoList);
//                        }
//
//                    });


                }


            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }


    /**
     * 解析前台的json然后创建策略
     *
     * @param strategyVO json字符串分4层
     */
    @Transactional
    @Override
    public Boolean updateStrategy(StrategyVO strategyVO) {

        try {
            /**
             * 转换对象
             */
            StrategyVO obj = strategyVO;
//                    JSON.parseObject(jsonString, StrategyVO.class);

            if (obj != null) {

                //获取策略
                StrategyInfo sInfo = new StrategyInfo();
                BeanUtil.copyProperties(obj, sInfo);
                sInfo.setUpdateTime(LocalDateTime.now());
                this.updateById(sInfo);

                StrategyOrRule strategyOrRule = new StrategyOrRule();
                strategyOrRule.setStrategyId(sInfo.getId() + "");

                //先删除策略值
                strategyValueService.deleteStrategyValueByStrategyId(strategyOrRule.getStrategyId());
                //删除策略关系
                strategyOrRuleService.remove(Wrappers.<StrategyOrRule>query().lambda().eq(StrategyOrRule::getStrategyId, sInfo.getId()));


                //用户vo
                List<StrategyUserVo> userVoList = obj.getChildren();
                if (userVoList != null && userVoList.size() > 0) {
                    userVoList.stream().forEach(uv -> {
                        //用户
                        StrategyUser user = new StrategyUser();
                        BeanUtil.copyProperties(uv, user);
                        if (strategyUserService.getById(user.getId()) == null) {
                            user.setResourceId(UUID.randomUUID().toString());
                            strategyUserService.save(user);
                        } else {
                            strategyUserService.updateById(user);
                        }

                        //得到规则
                        List<RuleInfoVo> ruleVoList = uv.getChildren();

                        strategyOrRule.setUserId(user.getId() + "");

                        if (ruleVoList != null && ruleVoList.size() > 0) {
                            ruleVoList.forEach(rv -> {
                                strategyOrRule.setRuleId(rv.getId() + "");
                                /**
                                 * 策略对应值
                                 */
                                if (rv.getChildren() != null && rv.getChildren().size() > 0) {
                                    strategyValueService.saveBatch(rv.getChildren());
                                    //创建关系
                                    rv.getChildren().forEach(vl -> {
                                        strategyOrRule.setStrValueId(vl.getId() + "");
                                        strategyOrRuleService.save(strategyOrRule);
                                    });

                                }
                            });
                        }
                    });


                }


            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }

    }


    /**
     * 解析前台的json然后创建标签策略
     *
     * @param strategyLableVO json字符串分4层
     */
    @Transactional
    @Override
    public Boolean saveStrategyLabel(StrategyLabelVO strategyLableVO) {

        try {
            /**
             * 转换对象
             */
            StrategyLabelVO obj = strategyLableVO;
            if (obj != null) {

                //获取策略
                StrategyInfo sInfo = new StrategyInfo();
                BeanUtil.copyProperties(obj, sInfo);
                sInfo.setCreateTime(LocalDateTime.now());
                this.save(sInfo);

                StrategyOrRule strategyOrRule = new StrategyOrRule();
                strategyOrRule.setStrategyId(sInfo.getId() + "");

                //得到规则
                List<RuleInfoVo> ruleVoList = obj.getChildren();

                if (ruleVoList != null && ruleVoList.size() > 0) {
                    ruleVoList.forEach(rv -> {
                        strategyOrRule.setRuleId(rv.getId() + "");
                        /**
                         * 策略对应值
                         */
                        if (rv.getChildren() != null && rv.getChildren().size() > 0) {
                            strategyValueService.saveBatch(rv.getChildren());

                            //创建关系
                            rv.getChildren().forEach(vl -> {
                                strategyOrRule.setStrValueId(vl.getId() + "");
                                strategyOrRuleService.save(strategyOrRule);
                            });

                        }
                    });
                }


            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }


    /**
     * 解析前台的json然后创建策略
     *
     * @param strategyLableVO json字符串分4层
     */
    @Transactional
    @Override
    public Boolean updateStrategyLabel(StrategyLabelVO strategyLableVO) {

        try {
            /**
             * 转换对象
             */
            StrategyLabelVO obj = strategyLableVO;

            if (obj != null) {

                //获取策略
                StrategyInfo sInfo = new StrategyInfo();
                BeanUtil.copyProperties(obj, sInfo);
                sInfo.setUpdateTime(LocalDateTime.now());
                this.updateById(sInfo);

                StrategyOrRule strategyOrRule = new StrategyOrRule();
                strategyOrRule.setStrategyId(sInfo.getId() + "");

                //先删除策略值
                strategyValueService.deleteStrategyValueByStrategyId(strategyOrRule.getStrategyId());
                //删除策略关系
                strategyOrRuleService.remove(Wrappers.<StrategyOrRule>query().lambda().eq(StrategyOrRule::getStrategyId, sInfo.getId()));


                //得到规则
                List<RuleInfoVo> ruleVoList = obj.getChildren();

                if (ruleVoList != null && ruleVoList.size() > 0) {
                    ruleVoList.forEach(rv -> {
                        strategyOrRule.setRuleId(rv.getId() + "");
                        /**
                         * 策略对应值
                         */
                        if (rv.getChildren() != null && rv.getChildren().size() > 0) {
                            strategyValueService.saveBatch(rv.getChildren());
                            //创建关系
                            rv.getChildren().forEach(vl -> {
                                strategyOrRule.setStrValueId(vl.getId() + "");
                                strategyOrRuleService.save(strategyOrRule);
                            });

                        }
                    });
                }


            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }

    }


    /**
     * 数据加密脱敏
     *
     * @param data     数据
     * @param roleId   角色id
     * @param systemId 系统id
     * @return 脱敏后数据
     */
    @Override
    public Object getRegDataList(Object data, String roleId, String systemId, boolean type) {
        try {
            //首先根据角色id查询角色策略
            List<AllergyRuleVo> ruleVoList = ruleInfoService.listRuleBySystemRule(roleId, systemId);
            if (ruleVoList != null && ruleVoList.size() > 0) {
                ruleVoList.forEach(r -> {
                    //查询规则对应字段
                    List<StrategyValue> valueList = strategyValueService.listRuleByValue(r.getId() + "", r.getStrategyId());

                    if (valueList != null && valueList.size() > 0) {
                        String reg = "";
                        try {
                            if (StrUtil.isNotEmpty(r.getRuleExpression())) {
                                reg = new String(Base64.getDecoder().decode(r.getRuleExpression()), "utf-8");
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        //只获取需要的信息
                        List<String> fieldNames = valueList.stream().filter(v -> StrUtil.isNotEmpty(v.getColumnNameEn())).map(StrategyValue::getColumnNameEn).collect(Collectors.toList());
//                                .forEach(v -> {
//                            fieldNames.add(v.getColumnNameEn());
//
//                        });
                        String sign = r.getSign();
                        //秘钥只能绑定用户
                        if (!"REG".equalsIgnoreCase(r.getEncryptionType()) && StrUtil.isNotEmpty(r.getSecretKey())) {
                            sign = r.getSecretKey();
                        }

                        if (data instanceof List) {
                            if (type) {
                                ReplaceUtil.dataListToEncode((List<Map<String, Object>>) data, fieldNames, reg, sign, r.getEncryptionType());
                            } else {
                                ReplaceUtil.dataListToDncode((List<Map<String, Object>>) data, fieldNames, reg, sign, r.getEncryptionType());
                            }
                            System.out.println("是List");
                        } else if (data instanceof Map) {
                            if (type) {
                                ReplaceUtil.dataMapToEncode((Map) data, fieldNames, reg, r.getSign(), r.getEncryptionType());
                            } else {
                                ReplaceUtil.dataMapToDncode((Map) data, fieldNames, reg, r.getSign(), r.getEncryptionType());
                            }
                            System.out.println("是map");
                        }
                    }
                });
            }
            return data;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }


    /**
     * 根据标签id获取标签策略
     *
     * @param sourceId 标签标识
     * @return
     */
    @Override
    public R getLabelSql(String sourceId, Map<String, Object> valueMap) {
        try {
            AssembleRule aRule = assembleRuleService.selectStrategyLabel(sourceId);
            if (aRule != null) {
                if (aRule.getType() != 1) {
                    String sql = PackagingUtil.getDataToSql(aRule.getFields(), aRule.getTables(), aRule.getConditions(), valueMap);
                    aRule.setSqlInfo(sql);
                }

                aRule.setFields("");
                aRule.setTables("");
                aRule.setConditions("");
                return new R(aRule);
            } else {
                return new R(null, "无匹配信息。").setCode(1);
            }
        } catch (Exception e) {
            return new R(e);
        }
    }
}

package com.clinbrain.bd.mdm.strategy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Auther: 梁乐乐 规则策略关系表
 * @Date: 2019/9/17 11:33
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("strategy_or_rule")
public class StrategyOrRule extends Model<StrategyOrRule> {
    private static final long serialVersionUID = 1L;
    /**
     * 标识
     */
    private Integer id;
    /**
     * 规则标识
     */
    private String ruleId;

    /**
     * 策略标识
     */
    private String strategyId;

    /**
     * 关联用户标识
     */
    private String userId;

    /**
     * 策略值标识
     */
    private String strValueId;
}

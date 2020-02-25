package com.clinbrain.bd.mdm.strategy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/9/17 11:38
 * @Description: 策略信息表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("strategy_info")
public class StrategyInfo extends Model<StrategyInfo> {
    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 策略名称
     */
    private String name;
    /**
     * 策略备注
     */
    private String remarks;
    /**
     * 策略状态
     */
    private Integer status;

    /**
     *
     */
    private LocalDateTime createTime;

    /**
     *
     */
    private LocalDateTime updateTime;

    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 策略类型
     */
    private Integer type;
}

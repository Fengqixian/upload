package com.clinbrain.bd.mdm.strategy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/9/17 11:38
 * @Description: 策略信息值表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("strategy_value")
public class StrategyValue extends Model<StrategyValue> {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 策略对应表中文
     */
    private String tableNameCn;
    /**
     * 策略对应英文字段
     */
    private String columnNameEn;
    /**
     * 字段中文
     */
    private String columnNameCn;
    /**
     * 策略对应表英文
     */
    private String tableNameEn;
    /**
     *  策略条件
     */
    private String strategicCond;
    /**
     *  策略值
     */
    private String strategicValue;

    /**
     * 节点类型
     */
    private String nodeType;
    /**
     * 来源标识
     */
    private String sourceId;
}

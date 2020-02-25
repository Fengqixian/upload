

package com.clinbrain.bd.mdm.strategy.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 策略
 *
 * @author lianglele
 * @date 2019/9/23
 */
@Data
public class StrategyLabelVO implements Serializable {

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
    private String status;

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
     * 类型
     */
    private Integer type;
    /**
     * 策略对应规则
     */
    List<RuleInfoVo> children;
}

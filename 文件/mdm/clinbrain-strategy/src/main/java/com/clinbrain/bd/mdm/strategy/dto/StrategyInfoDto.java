package com.clinbrain.bd.mdm.strategy.dto;

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
public class StrategyInfoDto {
    private static final long serialVersionUID = 1L;

    private String id;
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

    private String userName;

    private Integer type;


    private Integer pageType;
}

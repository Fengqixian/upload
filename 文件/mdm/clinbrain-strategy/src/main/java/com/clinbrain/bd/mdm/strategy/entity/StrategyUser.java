package com.clinbrain.bd.mdm.strategy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/9/17 13:14
 * @Description: 策略对应用户角色表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("strategy_user")
public class StrategyUser extends Model<StrategyUser> {
    private static final long serialVersionUID = 1L;
    private Integer id;

    /**
     * 用户名称
      * @return
     */
    private String userName;

    /**
     * 用户标识
     * @return
     */
    private String userId;
    /**
     * 系统标识
     */
    private String systemId;

    /**
     * 系统名称
      * @return
     */
   private String systemName;

    /**
     * 1:针对用户 2.针对角色 3.其它
     */
   private Integer type;

    /**
     * 节点类型
     */
    private String nodeType;

    private String resourceId;

    /**
     * 秘钥
     */
    private String secretKey;
}

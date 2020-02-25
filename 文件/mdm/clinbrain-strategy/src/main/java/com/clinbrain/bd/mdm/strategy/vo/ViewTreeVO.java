

package com.clinbrain.bd.mdm.strategy.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 技术元数据树结构
 *
 * @author lianglele
 * @date 2019/9/23
 */
@Data
public class ViewTreeVO implements Serializable {

    private String uuid;

    private Integer id;
    /**
     * 中文名称
     */
    private String nameCn;

    /**
     * 英文
     */
    private String nameEn;

    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 数据库标识标识
     */
    private Integer databaseId;

    /**
     * 数据表标识
     */
    private Integer tableId;

    /**
     * 用户
     */
    List<ViewTreeVO> children;
}

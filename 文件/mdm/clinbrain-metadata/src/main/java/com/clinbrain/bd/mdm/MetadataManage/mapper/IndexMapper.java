package com.clinbrain.bd.mdm.MetadataManage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IndexMapper extends BaseMapper {
    List<Map<String, Object>> getKeyValueData();

    List<Map<String, Object>> getPieDataMap();

    List<Map<String, Object>> getTreeMapDataMap();

    /**
     * 查询需要统计的表
     *
     * @return
     */
    List<Map<String, Object>> getIndexTableList(@Param("dataId") Integer dataId);

    /**
     * 根据ID修改表中的统计数量
     *
     * @param tableCount 数据量
     * @param id         唯一标识
     * @return 大于0修改成功
     */
    Integer updateTableIdByCount(@Param("tableCount") String tableCount, @Param("id") Integer id);

    /**
     * 根据ID修改首页统计数量
     *
     * @param dataCount 数据量
     * @param id         唯一标识
     * @return 大于0修改成功
     */
    Integer updateKeyValueData(@Param("dataCount") String dataCount, @Param("id") Integer id);
}
package com.clinbrain.bd.mdm.common.datasource.config;

import com.clinbrain.bd.mdm.common.datasource.support.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author lianglele
 * @date 2019-12-26 9:35
 * 动态数据源类
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * 指定路由Key
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
}

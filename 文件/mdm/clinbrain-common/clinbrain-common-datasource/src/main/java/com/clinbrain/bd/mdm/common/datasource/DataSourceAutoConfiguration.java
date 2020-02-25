package com.clinbrain.bd.mdm.common.datasource;

import com.clinbrain.bd.mdm.common.datasource.config.DruidDataSourceProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author lianglele
 * @date 2019-12-26 9:32
 * 自动配置类
 */
@AllArgsConstructor
@EnableConfigurationProperties({DruidDataSourceProperties.class})
public class DataSourceAutoConfiguration {
}

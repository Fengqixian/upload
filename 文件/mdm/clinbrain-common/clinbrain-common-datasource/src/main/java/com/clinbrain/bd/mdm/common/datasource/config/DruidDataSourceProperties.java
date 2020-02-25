package com.clinbrain.bd.mdm.common.datasource.config;

import lombok.Data;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lianglele
 * @date 2019-12-26 9:33
 * 参考DruidDataSourceWrapper
 */
@Primary
@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class DruidDataSourceProperties {
    private String type;
    private String driverClassName;
    private String username;
    private String password;
    private String url;

}

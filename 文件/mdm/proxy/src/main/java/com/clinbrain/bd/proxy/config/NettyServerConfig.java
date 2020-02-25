package com.clinbrain.bd.proxy.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取yml配置文件中的信息
 * Created by 叶云轩 on 2017/10/31 - 18:38
 * Concat tdg_yyx@foxmail.com
 */
@Component
@ConfigurationProperties(prefix = "server")
@Getter
@Setter
public class NettyServerConfig {
    private int port;//对外端口
    private int maxConnection;//最大连接数
    private long maxWatting;//最大空闲等待时间
}
package com.clinbrain.bd.proxy.config;

import com.clinbrain.bd.proxy.consts.DabaseTpye;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 读取yml配置文件中的信息
 * Created by 叶云轩 on 2017/10/31 - 18:38
 * Concat tdg_yyx@foxmail.com
 */
@Component
@ConfigurationProperties(prefix = "netty.clients")
@Getter
@Setter
public class NettyProxyConfig {
    private DabaseTpye type;
    private Map<String,NettyClientConfig> databases = new HashMap<>();
    public NettyClientConfig get(String key){
        return databases.get(key);
    }
    public void setType(String type){
        this.type = DabaseTpye.valueOf(type);
    }
}
package com.clinbrain.bd.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.proxy.BootstrapApplication
 * @createdDate 2019/8/22 15:32
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (proxy)
 */
@SpringCloudApplication
public class BootstrapApplication{
    public static void main(String[] args) {
        SpringApplication.run(BootstrapApplication.class, args);
    }
}

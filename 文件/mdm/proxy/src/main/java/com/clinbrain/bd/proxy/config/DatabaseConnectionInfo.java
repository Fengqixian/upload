package com.clinbrain.bd.proxy.config;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WANGYI
 * @className com.clinbrain.bd.proxy.config.DatabaseConnectionInfo
 * @createdDate 2019/8/29 17:37
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (mdm)
 */
@Getter
@Setter
public class DatabaseConnectionInfo {
    private Map<String,String> params = new HashMap<>();
    private String database;
    private String username;
    private String password;
}

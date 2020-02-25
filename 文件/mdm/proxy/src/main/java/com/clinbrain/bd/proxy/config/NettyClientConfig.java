package com.clinbrain.bd.proxy.config;

import com.clinbrain.bd.mdm.common.core.util.DataJDBCUtil;
import com.clinbrain.bd.proxy.consts.DabaseTpye;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class NettyClientConfig {

    private String dbName;
    private String dbAlias;
    private int port;
    private String host;
    private String username;
    private String password;
}
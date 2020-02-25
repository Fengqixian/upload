package com.clinbrain.bd.mdm.common.core.util;

import lombok.Data;

/**
 * @author WANGYI
 * @className com.clinbrain.bt.metadata.entity.ConnectionParam
 * @createdDate 2019/5/14 10:44
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (DataServer)
 */
@Data
public class ConnectionParam {
    private DbTypeEnum dbType;
    private String port;
    private String username;
    private String password;
    private String ipAddr;
    private String database;
}

package com.clinbrain.bd.dataserver.entity;

/**
 * @author WANGYI
 * @className com.clinbrain.bt.metadata.entity.ConnectionParam
 * @createdDate 2019/5/14 10:44
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (DataServer)
 */
public class ConnectionParam {
    private DbTypeEnum dbType;
    private String port;
    private String username;
    private String password;
    private String ipAddr;
    private String database;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public DbTypeEnum getDbType() {
        return dbType;
    }

    public void setDbType(DbTypeEnum dbType) {
        this.dbType = dbType;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }
}

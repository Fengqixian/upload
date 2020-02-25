package com.clinbrain.bd.mdm.common.core.factory;

import com.clinbrain.bd.mdm.common.core.util.ConnectionParam;
import com.clinbrain.bd.mdm.common.core.util.DbTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author WANGYI
 * @className com.clinbrain.bt.metadata.common.factory.ConnectionFactory
 * @createdDate 2019/5/14 10:41
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (DataServer)
 */
public class ConnectionFactory {
    private final static Logger LOGGER = LoggerFactory.getLogger(ConnectionFactory.class);
    private ConnectionFactory(){
        for(DbTypeEnum typ:DbTypeEnum.values()){
            try {
                Class.forName(typ.getDriverClass());
            } catch (ClassNotFoundException e) {
                LOGGER.warn("找不到可能会被使用的驱动:"+typ.getDriverClass());
            }
        }
    }
    private static final ConnectionFactory connectionFactory = new ConnectionFactory();
    public static ConnectionFactory getConnectionFactory(){
        return connectionFactory;
    }

    public Connection createConnection(ConnectionParam param) throws Exception {
        Connection connection = null;
        switch (param.getDbType()){
            case MYSQL:
                System.out.println(DriverManager.getDrivers());
                connection = DriverManager.getConnection("jdbc:mysql://"+param.getIpAddr()+":"+param.getPort()+"/"+param.getDatabase()+"?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC",param.getUsername(),param.getPassword());
                break;
            case GREENPLUM:
                connection = DriverManager.getConnection("jdbc:pivotal:greenplum://"+param.getIpAddr()+":"+param.getPort()+";DatabaseName="+param.getDatabase(),param.getUsername(),param.getPassword());
                break;
            case SQLSERVER:
                connection = DriverManager.getConnection("jdbc:sqlserver://"+param.getIpAddr()+":"+param.getPort()+";DatabaseName="+param.getDatabase(),param.getUsername(),param.getPassword());
                break;
            case ORACLE:
                connection = DriverManager.getConnection("jdbc:oracle:thin:@//"+param.getIpAddr()+":"+param.getPort()+";DatabaseName="+param.getDatabase(),param.getUsername(),param.getPassword());
                break;
            default:
                throw new Exception("不支持的数据库类型");
        }

        return connection;
    }
}

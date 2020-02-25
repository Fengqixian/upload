package com.clinbrain.bd.dataserver.dto;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("data_server")
public class DataServer {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;

    /**
     * 服务名称 例如 getServerListData
     */
    private String serverName;

    /**
     * 服务描述 例如 获取所有服务信息
     */
    private String serverDesc;

    /**
     * 服务中文名 获取所有服务信息
     */
    private String serverNameCn;

    /**
     * 返回数据类型  XML JSON
     */
    private String returnType;

    /**
     * 执行SQL语句
     */
    private String querySql;

    /**
     * 数据库连接ID
     */
    private String datasourceId;

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取服务名称 例如 getServerListData
     *
     * @return server_name - 服务名称 例如 getServerListData
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * 设置服务名称 例如 getServerListData
     *
     * @param serverName 服务名称 例如 getServerListData
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * 获取服务描述 例如 获取所有服务信息
     *
     * @return server_desc - 服务描述 例如 获取所有服务信息
     */
    public String getServerDesc() {
        return serverDesc;
    }

    /**
     * 设置服务描述 例如 获取所有服务信息
     *
     * @param serverDesc 服务描述 例如 获取所有服务信息
     */
    public void setServerDesc(String serverDesc) {
        this.serverDesc = serverDesc;
    }

    /**
     * 获取服务中文名 获取所有服务信息
     *
     * @return server_name_cn - 服务中文名 获取所有服务信息
     */
    public String getServerNameCn() {
        return serverNameCn;
    }

    /**
     * 设置服务中文名 获取所有服务信息
     *
     * @param serverNameCn 服务中文名 获取所有服务信息
     */
    public void setServerNameCn(String serverNameCn) {
        this.serverNameCn = serverNameCn;
    }

    /**
     * 获取返回数据类型  XML JSON
     *
     * @return return_type - 返回数据类型  XML JSON
     */
    public String getReturnType() {
        return returnType;
    }

    /**
     * 设置返回数据类型  XML JSON
     *
     * @param returnType 返回数据类型  XML JSON
     */
    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    /**
     * 获取执行SQL语句
     *
     * @return query_sql - 执行SQL语句
     */
    public String getQuerySql() {
        return querySql;
    }

    /**
     * 设置执行SQL语句
     *
     * @param querySql 执行SQL语句
     */
    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }
}
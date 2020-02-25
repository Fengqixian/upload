package com.clinbrain.bd.mdm.MetadataManage.entity;

/**
 * 血缘分析用
 */
public class LineageSql {
    private LineageNode rootNode;
    private String sql;

    public LineageNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(LineageNode rootNode) {
        this.rootNode = rootNode;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}

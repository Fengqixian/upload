package com.clinbrain.bd.mdm.MetadataManage.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * 血缘分析用
 */
public class LineageTable {
    private LineageNode tableNode;
    private Set<LineageNode> columns = new HashSet<LineageNode>();

    public LineageNode getTableNode() {
        return tableNode;
    }

    public void setTableNode(LineageNode tableNode) {
        this.tableNode = tableNode;
    }

    public Set<LineageNode> getColumns() {
        return columns;
    }

    public void setColumns(Set<LineageNode> columns) {
        this.columns = columns;
    }
}

package com.clinbrain.bd.mdm.MetadataManage.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LineageNode {
    private String id;
    private String name;
    private String nameEn;
    private String parentId;
    private String moduleId;
    private String type="";
    private Boolean isGroup;
    private String comment;
    private Integer color;

    private Boolean isHighlight;
    private String dbName;
    private String level;
    private String etlTargetId;
    private Integer etlId;
    private List<LineageNode> children = new ArrayList<>();

    public LineageNode() {}
    public LineageNode(String id, String name, String parentId, String moduleId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.moduleId = moduleId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(Boolean isGroup) {
        this.isGroup = isGroup;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Boolean getIsHighlight(){
        return this.isHighlight;
    }

    public void setIsHighlight(Boolean isHighlight){
        this.isHighlight = isHighlight;
    }

    public String getDbName(){
        return this.dbName;
    }

    public void setDbName(String dbName){
        this.dbName = dbName;
    }
    public String getLevel(){
        return this.level;
    }

    public void setLevel(String level){
        this.level = level;
    }

    public String getEtlTargetId(){
        return this.etlTargetId;
    }

    public void setEtlTargetId(String etlTargetId){
        this.etlTargetId = etlTargetId;
    }

    public Integer getEtlId(){
        return this.etlId;
    }

    public void setEtlId(Integer etlId){
        this.etlId = etlId;
    }

    public List<LineageNode> getChildren(){
        return this.children;
    }

    public void setChildren(LineageNode node){
        this.children.add(node);
    }

    public String getNameEn(){
        return this.nameEn;
    }
    public void setNameEn(String nameEn){
        this.nameEn = nameEn;
    }
//重写

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineageNode)) return false;
        LineageNode that = (LineageNode) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parentId, moduleId, type, isGroup, comment);
    }
}

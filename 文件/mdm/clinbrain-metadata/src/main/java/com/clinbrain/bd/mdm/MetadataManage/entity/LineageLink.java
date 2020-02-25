package com.clinbrain.bd.mdm.MetadataManage.entity;

import java.util.Objects;

public class LineageLink {
    private String from ;
    private String to;
    private String comment;
    private String type="";
    private String color;
    private Integer source;
    private Integer target;
    private Integer etlId;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getEtlId(){
        return this.etlId;
    }

    public void setEtlId(Integer etlId){
        this.etlId=etlId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineageLink)) return false;
        LineageLink link = (LineageLink) o;
        return Objects.equals(from, link.from) &&
                Objects.equals(to, link.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }
}

package com.clinical.tongxin.entity;

/**
 * Created by Administrator on 2016/10/22 0022.
 */
public class CaseListEntity {
    private String rId;
    private String time;
    private String project;
    private String doctor;
    private String agency;
    private String sort_id;

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getSort_id() {
        return sort_id;
    }

    public void setSort_id(String sort_id) {
        this.sort_id = sort_id;
    }
}

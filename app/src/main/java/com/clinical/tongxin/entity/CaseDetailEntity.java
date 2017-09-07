package com.clinical.tongxin.entity;

import com.clinical.tongxin.util.Utils;

import java.util.List;

/**
 * Created by Administrator on 2016/10/22 0022.
 */
public class CaseDetailEntity {
    private String rId;
    private String time;
    private String project;
    private String doctor;
    private String agency;
    private String details;
    private String condition;
    private String measures;
    private String inspectionData;
    private List<ImageURLEntity> picList;
    public String getrId() {
        return Utils.getMyString(rId, "");
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public String getTime() {
        return Utils.getMyString(time, "");
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProject() {
        return Utils.getMyString(project, "");
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getDoctor() {
        return Utils.getMyString(doctor, "");
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getAgency() {
        return Utils.getMyString(agency, "");
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getDetails() {
        return Utils.getMyString(details, "");
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCondition() {
        return Utils.getMyString(condition, "");
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getMeasures() {
        return Utils.getMyString(measures, "");
    }

    public void setMeasures(String measures) {
        this.measures = measures;
    }

    public String getInspectionData() {
        return Utils.getMyString(inspectionData, "");
    }

    public void setInspectionData(String inspectionData) {
        this.inspectionData = inspectionData;
    }

    public List<ImageURLEntity> getPicList() {
        return picList;
    }

    public void setPicList(List<ImageURLEntity> picList) {
        this.picList = picList;
    }
}

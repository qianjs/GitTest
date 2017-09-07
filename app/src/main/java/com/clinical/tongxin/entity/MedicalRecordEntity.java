package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lzj667 on 2016/9/1.
 * 病历列表
 */
public class MedicalRecordEntity {
    @SerializedName("time")
    private String date;
    @SerializedName("doctor")
    private String person;
    @SerializedName("agency")
    private String agency;
    @SerializedName("rId")
    private String medicalId;
    private String project;
    private String webUrl;

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(String medicalId) {
        this.medicalId = medicalId;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

}

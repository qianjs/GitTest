package com.clinical.tongxin.entity;

import java.util.List;

/**
 * Created by apple on 16/11/2.
 */

public class DemandDetailEntity {
    private String dId;
    private String date;
    private String endDate;
    private String content;
    private String project;
    private  List<DemandPricesEntity> docList;
    private List<ImageURLEntity> picList;

    public String getdId() {
        return dId;
    }

    public void setdId(String dId) {
        this.dId = dId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }


    public List<ImageURLEntity> getPicList() {
        return picList;
    }

    public void setPicList(List<ImageURLEntity> picList) {
        this.picList = picList;
    }

    public List<DemandPricesEntity> getDocList() {
        return docList;
    }

    public void setDocList(List<DemandPricesEntity> docList) {
        this.docList = docList;
    }
}

package com.clinical.tongxin.entity;

import com.clinical.tongxin.util.Utils;

/**
 * Created by Administrator on 2016/9/2 0002.
 */
public class DemandEntity {
    private String dId;
    private String date;
    private String endDate;
    private String content;
    private String detailUrl;
    private String offerUrl;
    private String sort_id;
    private String project;
    private String demandState;
    private String stateName;

    public String getdId() {
        return Utils.getMyString(dId, "");
    }

    public void setdId(String dId) {
        this.dId = dId;
    }

    public String getDate() {
        return Utils.getMyString(date, "");
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEndDate() {
        return Utils.getMyString(endDate, "");
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getContent() {
        return Utils.getMyString(content, "");
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDetailUrl() {
        return Utils.getMyString(detailUrl, "");
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getOfferUrl() {
        return Utils.getMyString(offerUrl, "");
    }

    public void setOfferUrl(String offerUrl) {
        this.offerUrl = offerUrl;
    }

    public String getSort_id() {
        return Utils.getMyString(sort_id, "");
    }

    public void setSort_id(String sort_id) {
        this.sort_id = sort_id;
    }

    public String getProject() {
        return Utils.getMyString(project, "");
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getDemandState() {
        return Utils.getMyString(demandState, "");
    }

    public void setDemandState(String demandState) {
        this.demandState = demandState;
    }

    public String getStateName() {
        return Utils.getMyString(stateName, "");
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}

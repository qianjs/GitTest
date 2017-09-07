package com.clinical.tongxin.entity;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class AuctionEntity {
    private String aId;
    private String project;
    private String price;
    private String residualTime;
    private String releaseDoctor;
    private String sort_id;
    private String state;

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getResidualTime() {
        return residualTime;
    }

    public void setResidualTime(String residualTime) {
        this.residualTime = residualTime;
    }

    public String getReleaseDoctor() {
        return releaseDoctor;
    }

    public void setReleaseDoctor(String releaseDoctor) {
        this.releaseDoctor = releaseDoctor;
    }

    public String getSort_id() {
        return sort_id;
    }

    public void setSort_id(String sort_id) {
        this.sort_id = sort_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

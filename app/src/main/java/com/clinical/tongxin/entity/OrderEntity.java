package com.clinical.tongxin.entity;

import com.clinical.tongxin.util.Utils;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class OrderEntity {
    private String oId;
    private String doctor;
    private String oDate;
    private String project;
    private String prices;
    private String sort_id;
    private String state;
    private String path;
    private String stateName;
    private String AdvancedCharge;
    private String ChangePrice;

    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getoDate() {
        return oDate;
    }

    public void setoDate(String oDate) {
        this.oDate = oDate;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getAdvancedCharge() {
        return Utils.getMyString(AdvancedCharge, "");
    }

    public void setAdvancedCharge(String advancedCharge) {
        AdvancedCharge = advancedCharge;
    }

    public String getChangePrice() {
        return Utils.getMyString(ChangePrice, "");
    }

    public void setChangePrice(String changePrice) {
        ChangePrice = changePrice;
    }
}

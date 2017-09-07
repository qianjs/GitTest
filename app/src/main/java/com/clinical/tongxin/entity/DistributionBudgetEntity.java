package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/1/4 0004.
 */

public class DistributionBudgetEntity {
    @SerializedName("DirectorGroupId")
    private String DirectorBudgetId;
    private String PMName;
    private String ExpectAmount;
    private String UsingAmount;
    private String UsedAmount;
    private String UnUsedAmount;
    private String sortId;

    public String getDirectorBudgetId() {
        return DirectorBudgetId;
    }

    public void setDirectorBudgetId(String directorBudgetId) {
        DirectorBudgetId = directorBudgetId;
    }

    public String getPMName() {
        return PMName;
    }

    public void setPMName(String PMName) {
        this.PMName = PMName;
    }

    public String getExpectAmount() {
        return ExpectAmount;
    }

    public void setExpectAmount(String expectAmount) {
        ExpectAmount = expectAmount;
    }

    public String getUsingAmount() {
        return UsingAmount;
    }

    public void setUsingAmount(String usingAmount) {
        UsingAmount = usingAmount;
    }

    public String getUsedAmount() {
        return UsedAmount;
    }

    public void setUsedAmount(String usedAmount) {
        UsedAmount = usedAmount;
    }

    public String getUnUsedAmount() {
        return UnUsedAmount;
    }

    public void setUnUsedAmount(String unUsedAmount) {
        UnUsedAmount = unUsedAmount;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }
}

package com.clinical.tongxin.entity;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

public class BudgetQueryEntitity {
    private String DirectorBudgetId;
    private String PMName;
    private String UsedAmount;
    private String UnUsedAmount;
    private String AllocationsDate;
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

    public String getAllocationsDate() {
        return AllocationsDate;
    }

    public void setAllocationsDate(String allocationsDate) {
        AllocationsDate = allocationsDate;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }
}

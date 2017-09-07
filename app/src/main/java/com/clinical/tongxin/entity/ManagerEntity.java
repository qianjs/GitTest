package com.clinical.tongxin.entity;

/**
 * Created by Administrator on 2017/1/7 0007.
 */

public class ManagerEntity {
    private String BudgetId;
    private String AllocationsDate;
    private String ExpectAmount;
    private String SortId;
    private String State;

    public String getBudgetId() {
        return BudgetId;
    }

    public void setBudgetId(String budgetId) {
        BudgetId = budgetId;
    }

    public String getAllocationsDate() {
        return AllocationsDate;
    }

    public void setAllocationsDate(String allocationsDate) {
        AllocationsDate = allocationsDate;
    }

    public String getExpectAmount() {
        return ExpectAmount;
    }

    public void setExpectAmount(String expectAmount) {
        ExpectAmount = expectAmount;
    }

    public String getSortId() {
        return SortId;
    }

    public void setSortId(String sortId) {
        SortId = sortId;
    }

    public String getStatus() {
        return State;
    }

    public void setStatus(String status) {
        State = status;
    }
}

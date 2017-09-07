package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 评价 新 实体
 * Created by linchao on 2017/7/14.
 */

public class EvaluateNewEntity {
    @SerializedName("BeingEvaluated")
    private String beingEvaluated;
    @SerializedName("taskReviewItemName")
    private String taskReviewItemName;
    @SerializedName("isTerms")
    private String isTerms;

    public String getBeingEvaluated() {
        return beingEvaluated;
    }

    public void setBeingEvaluated(String beingEvaluated) {
        this.beingEvaluated = beingEvaluated;
    }

    public String getTaskReviewItemName() {
        return taskReviewItemName;
    }

    public void setTaskReviewItemName(String taskReviewItemName) {
        this.taskReviewItemName = taskReviewItemName;
    }

    public String getIsTerms() {
        return isTerms;
    }

    public void setIsTerms(String isTerms) {
        this.isTerms = isTerms;
    }
}

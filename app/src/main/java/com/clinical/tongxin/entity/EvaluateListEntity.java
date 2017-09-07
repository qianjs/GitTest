package com.clinical.tongxin.entity;

/**
 * Created by Administrator on 2017/6/19.
 */

public class EvaluateListEntity {
    private String comment;
    private int count;
    private int isterms;

    public int getIsterms() {
        return isterms;
    }

    public void setIsterms(int isterms) {
        this.isterms = isterms;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

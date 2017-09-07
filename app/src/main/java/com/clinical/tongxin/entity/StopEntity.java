package com.clinical.tongxin.entity;

/**
 * 验收任务详情
 * Created by Administrator on 2016/10/2 0002.
 */
public class StopEntity {
    private int allTaskItem;
    private int pass;
    private int unPass;
    private int unCheck;
    private String BidAmount;

    public int getAllTaskItem() {
        return allTaskItem;
    }

    public void setAllTaskItem(int allTaskItem) {
        this.allTaskItem = allTaskItem;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public int getUnPass() {
        return unPass;
    }

    public void setUnPass(int unPass) {
        this.unPass = unPass;
    }

    public int getUnCheck() {
        return unCheck;
    }

    public void setUnCheck(int unCheck) {
        this.unCheck = unCheck;
    }

    public String getBidAmount() {
        return BidAmount;
    }

    public void setBidAmount(String bidAmount) {
        BidAmount = bidAmount;
    }
}

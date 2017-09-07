package com.clinical.tongxin.entity;

/**
 * 查询任务数量
 * Created by linchao on 2017/7/6.
 */

public class TaskCountEntity {
    private String completed; // 已完成
    private String arbitration; // 仲裁
    private String waitcommit; // 待提交
    private String waitreceive; // 	待接收
    private String bidding; // 	竞价中
    private String waitevaluate; // 待评价
    private String total; // 	所有任务
    private String waitcheck; // 待验收
    private String waitpay; // 待支付
    private String offshelf; // 	已下架
    private String waitapproval; // 待审批
    private String over;// 终止

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getArbitration() {
        return arbitration;
    }

    public void setArbitration(String arbitration) {
        this.arbitration = arbitration;
    }

    public String getWaitcommit() {
        return waitcommit;
    }

    public void setWaitcommit(String waitcommit) {
        this.waitcommit = waitcommit;
    }

    public String getWaitreceive() {
        return waitreceive;
    }

    public void setWaitreceive(String waitreceive) {
        this.waitreceive = waitreceive;
    }

    public String getBidding() {
        return bidding;
    }

    public void setBidding(String bidding) {
        this.bidding = bidding;
    }

    public String getWaitevaluate() {
        return waitevaluate;
    }

    public void setWaitevaluate(String waitevaluate) {
        this.waitevaluate = waitevaluate;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getWaitcheck() {
        return waitcheck;
    }

    public void setWaitcheck(String waitcheck) {
        this.waitcheck = waitcheck;
    }

    public String getWaitpay() {
        return waitpay;
    }

    public void setWaitpay(String waitpay) {
        this.waitpay = waitpay;
    }

    public String getOffshelf() {
        return offshelf;
    }

    public void setOffshelf(String offshelf) {
        this.offshelf = offshelf;
    }

    public String getWaitapproval() {
        return waitapproval;
    }

    public void setWaitapproval(String waitapproval) {
        this.waitapproval = waitapproval;
    }

    public String getOver() {
        return over;
    }

    public void setOver(String over) {
        this.over = over;
    }
}

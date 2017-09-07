package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by linchao on 2017/1/5.
 */

public class TaskDetailsEntity implements Serializable {
    @SerializedName("TypePicUrl")
    private String typePicUrl; 	// 任务类型图片
    @SerializedName("ProjectTypeName")
    private String projectTypeName;	// 项目类型名
    @SerializedName("ProvinceName")
    private String provinceName;// 省份名称
    @SerializedName("CityName")
    private String cityName;	// 城市名称
    @SerializedName("DistrictName")
    private String districtName;	// 区县名称
    @SerializedName("Number")
    private String number;	// 任务编号
    @SerializedName("HXNumber")
    private String hXNumber;	// 发布人环信账号
    @SerializedName("ProjectName")
    private String projectName;	// 项目名称
    @SerializedName("Status")
    private String taskStatusName;// 任务状态名
    @SerializedName("Scale")
    private String scale;// 规模
    @SerializedName("Remark")
    private String remark;	// 备注
    @SerializedName("Amount")
    private String amount;	// 总价
    @SerializedName("FileUrl")
    private String fileUrl;// 上传数据文件地址
    @SerializedName("TaskItem")
    private List<TaskSiteItemEntity> taskItem;
    @SerializedName("CreateOrderTime")
    private String createOrderTime;
    @SerializedName("OrderId")
    private String orderId;
    @SerializedName("PayAccount")
    private String payAccount;
    @SerializedName("ContractorHxNum")
    private String contractorHxNum;
    @SerializedName("Publisher")
    private String publisher;
    @SerializedName("Contractor")
    private String contractor;
    @SerializedName("PublishedType")
    private String publishedType; // 1竞价中 2指定接包

    public String getPublishedType() {
        return publishedType;
    }

    public void setPublishedType(String publishedType) {
        this.publishedType = publishedType;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public String getContractorHxNum() {
        return contractorHxNum;
    }

    public void setContractorHxNum(String contractorHxNum) {
        this.contractorHxNum = contractorHxNum;
    }

    public String getCreateOrderTime() {
        return createOrderTime;
    }

    public void setCreateOrderTime(String createOrderTime) {
        this.createOrderTime = createOrderTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }



    public String getTypePicUrl() {
        return typePicUrl;
    }

    public void setTypePicUrl(String typePicUrl) {
        this.typePicUrl = typePicUrl;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String gethXNumber() {
        return hXNumber;
    }

    public void sethXNumber(String hXNumber) {
        this.hXNumber = hXNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTaskStatusName() {
        return taskStatusName;
    }

    public void setTaskStatusName(String taskStatusName) {
        this.taskStatusName = taskStatusName;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public List<TaskSiteItemEntity> getTaskItem() {
        return taskItem;
    }

    public void setTaskItem(List<TaskSiteItemEntity> taskItem) {
        this.taskItem = taskItem;
    }
}

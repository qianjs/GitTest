package com.clinical.tongxin.entity;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class HotAuctionEntity {
    private String pId;
    private String fixedPrice;
    private String path;
    private String auctionPrice;
    private String aId;
    private String pName;
    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getFixedPrice() {
        return fixedPrice;
    }

    public void setFixedPrice(String fixedPrice) {
        this.fixedPrice = fixedPrice;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAuctionPrice() {
        return auctionPrice;
    }

    public void setAuctionPrice(String auctionPrice) {
        this.auctionPrice = auctionPrice;
    }

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }
}

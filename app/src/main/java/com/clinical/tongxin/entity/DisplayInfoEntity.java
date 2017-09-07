package com.clinical.tongxin.entity;

import com.clinical.tongxin.util.Utils;

/**
 * Created by Administrator on 2016/10/5 0005.
 */
public class DisplayInfoEntity {
    private String pId;
    private String url;
    private String title;
    private String subTitle;
    private String details;

    public String getpId() {
        return Utils.getMyString(pId, "");
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getUrl() {
        return Utils.getMyString(url, "");
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return Utils.getMyString(title, "");
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return Utils.getMyString(subTitle, "");
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDetails() {
        return Utils.getMyString(details, "");
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

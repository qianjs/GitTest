package com.clinical.tongxin.entity;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class ItemFragmententity {
    private String imagePath;
    private String linkAddress;
    private String contentName;
    private String linkType; // text 文本       link 链接
    private String content; // 文本内容

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }
}

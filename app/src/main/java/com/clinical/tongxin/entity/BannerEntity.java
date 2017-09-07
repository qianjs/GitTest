package com.clinical.tongxin.entity;

import com.clinical.tongxin.util.UrlUtils;
import com.clinical.tongxin.util.Utils;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class BannerEntity {
    private String id;
    @SerializedName("ImgUrl")
    private String picUrl;
    @SerializedName("LinkUrl")
    private String webUrl;
    private String name;
    private String content;
    private String type;

    public String getId() {
        return Utils.getMyString(id,"");
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicUrl() {
        return Utils.getMyString(picUrl,"");
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = UrlUtils.BASE_URL+ picUrl;
    }

    public String getWebUrl() {
        return Utils.getMyString(webUrl,"");
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getName() {
        return Utils.getMyString(name,"");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return Utils.getMyString(content,"");
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public String getType() {
//        return Utils.getMyString(type,"");
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }

    public String getType() {
        return Utils.getMyString(type, "");
    }

    public void setType(String type) {
        this.type = type;
    }
}

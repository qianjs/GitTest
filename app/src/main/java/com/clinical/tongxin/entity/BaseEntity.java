package com.clinical.tongxin.entity;

import com.clinical.tongxin.util.Utils;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class BaseEntity {
    private String id;
    private String text;

    public String getId() {
        return Utils.getMyString(id, "");
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return Utils.getMyString(text, "");
    }

    public void setText(String text) {
        this.text = text;
    }
}

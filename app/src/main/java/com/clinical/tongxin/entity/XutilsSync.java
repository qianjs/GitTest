package com.clinical.tongxin.entity;

import com.clinical.tongxin.util.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/25 0025.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class XutilsSync implements Serializable {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

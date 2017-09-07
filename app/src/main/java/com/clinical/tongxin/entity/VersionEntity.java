package com.clinical.tongxin.entity;

import java.io.Serializable;

/**
 * 版本信息实体
 * Created by linchao on 2017/3/23.
 */

public class VersionEntity implements Serializable{

   private String version; // 版本号
    private String size; // 更新包大小
    private String content; // 更新内容提示

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

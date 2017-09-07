package com.clinical.tongxin.entity;

/**
 * 推送消息实体
 * Created by linchao on 2017/6/23.
 */

public class NotifyMessageEntity<T> {
    private String messageType;
    private T content;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}

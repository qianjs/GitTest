package com.clinical.tongxin.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name="AppNoticeModel")
public class AppNoticeModel {
	@Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
	private int id;
	@Column(name = "noticeType")
	private String noticeType; // 消息类型
	@Column(name = "noticeName")
	private String noticeName; // 名称
	@Column(name = "taskId")
	private String taskId; // 任务id
	@Column(name = "status")
	private String taskStatus; // 任务状态
	@Column(name = "noticeContent")
	private String noticeContent; // 提示内容
	@Column(name="uid")
	private String uid; // 登录人id
	@Column(name="msgId")
	private String msgId; // 消息id


	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public String getNoticeName() {
		return noticeName;
	}

	public void setNoticeName(String noticeName) {
		this.noticeName = noticeName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

}

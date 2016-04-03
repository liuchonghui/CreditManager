package com.sina.request;

import com.sina.engine.base.request.model.RequestModel;

public class AccountInfoRequestModel extends RequestModel {

	private static final long serialVersionUID = 1L;

	public AccountInfoRequestModel(String domainName, String phpName) {
		super(domainName, phpName);
	}

	private String action;
	private String uid;

	private String guid;
	private String gtoken;
	private String deadline;
	private String type;
	private String from;

	private String taskId;
	private String syncReseaon;

	private String task_id;
	private String news_id;

	private String additionInfo;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getSyncReseaon() {
		return syncReseaon;
	}

	public void setSyncReseaon(String syncReseaon) {
		this.syncReseaon = syncReseaon;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getNews_id() {
		return news_id;
	}

	public void setNews_id(String news_id) {
		this.news_id = news_id;
	}

	public String getAdditionInfo() {
		return additionInfo;
	}

	public void setAdditionInfo(String additionInfo) {
		this.additionInfo = additionInfo;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getGtoken() {
		return gtoken;
	}

	public void setGtoken(String gtoken) {
		this.gtoken = gtoken;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

}
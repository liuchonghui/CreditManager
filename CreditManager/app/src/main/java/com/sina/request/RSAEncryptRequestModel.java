package com.sina.request;

import com.sina.engine.base.request.model.RequestModel;

public class RSAEncryptRequestModel extends RequestModel {

	private static final long serialVersionUID = 1L;
	private String token;

	private String guid;
	private String gtoken;
	private String deadline;
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public RSAEncryptRequestModel(String domainName, String phpName) {
		super(domainName, phpName);
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
}
package com.sina.request;

import java.util.List;
import java.util.Map;

/**
 * RAS加密API
 * 
 * @author kangshaozhe
 * 
 */
public class RSAEncryptModel extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final int SUCCESS = 0;
	public static final int PARAM_INVALIDATE = 1;
	public static final int TOKEN_INVALIDATE = 2;
	public static final int UNKNOWN_ERROR = 99;

	private boolean result;
	private String data;
	private int err;
	private List<Map<String, CookieModel>>multi_domain;
	
	public List<Map<String, CookieModel>> getMulti_domain() {
		return multi_domain;
	}
	public void setMulti_domain(List<Map<String, CookieModel>> multi_domain) {
		this.multi_domain = multi_domain;
	}

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getErr() {
		return err;
	}

	public void setErr(int err) {
		this.err = err;
	}

}

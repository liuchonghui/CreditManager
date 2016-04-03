package com.sina.engine.base.request.model;

import java.io.Serializable;
/**
 * 请求数据模型基类
 * @author kangshaozhe
 *
 */

public class RequestModel implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String domainName= "";//域名
	
	private String phpName = "";//php方法名
	
	public RequestModel(String domainName,String phpName){
		setDomainName(domainName);
		setPhpName(phpName);
	}
	
	
	public void setDomainName(String domainName){
		this.domainName = domainName;
	}
	
	public String getDomainName(){
		return domainName;
	}
	
	public void setPhpName(String phpName){
		this.phpName = phpName;
	}
	
	public String getPhpName(){
		return phpName;
	}
	
}

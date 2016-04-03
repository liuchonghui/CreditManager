package com.sina.engine.base.request.model;

import java.io.Serializable;

public class MemoryModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long creatTime;//缓存创建时间 毫秒
	
	private int lifeTime;//缓存过期时间 秒
	
	private Object memoryModel;//缓存数据
	
	private String result="";
	
	private String message="";
	
	public void setMessage(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
	
	public void setResult(String result){
		this.result = result;
	}
	
	public String getResult(){
		return result;
	}
	
	public void setCreatTime(long creatTime){
		this.creatTime = creatTime;
	}
	
	public long getCreatTime(){
		return creatTime;
	}
	
	public void setLifeTime(int lifeTime){
		this.lifeTime = lifeTime;
	}
	
	public int getLifeTime(){
		return lifeTime;
	}
	
	public void setMemoryModel(Object memoryModel){
		this.memoryModel = memoryModel;
	}
	
	public Object getMemoryModel(){
		return memoryModel;
	}
	
	
}

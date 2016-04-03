package com.sina.engine.base.request.options;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.sina.engine.base.enums.HttpMethodEnum;
import com.sina.engine.base.enums.HttpTypeEnum;
import com.sina.engine.base.enums.ReturnDataClassTypeEnum;
import com.sina.engine.base.manager.EngineManager;
import com.sina.engine.base.request.client.JsonFetcher;

/**
 * 请求数据设置
 * @author kangshaozhe
 *
 */
public class RequestOptions{

	private HttpMethodEnum httpRequestMethod = null;
	private HttpTypeEnum httpRequestType;
	private ReturnDataClassTypeEnum returnDataClassTypeEnum;
	private Class<? extends Object> returnModelClass;
	private boolean isMainThread;
	private int memoryLifeTime = 0;// 缓存过期时间(秒)
	private boolean isSaveMemory = false;
	private boolean isSaveDb = false;
	private List<BasicNameValuePair> uploadFileList = new ArrayList<BasicNameValuePair>();	
	/**链接是否超时**/
	private boolean isConnectedTimeOut;
	/**链接超时时的提示**/
	private String timeOutString = null;

	private JsonFetcher fetcher;

	public RequestOptions(){
		
	}
	
	public static RequestOptions getDefaultOptions(){
		RequestOptions requestOptions = new RequestOptions()
		        .setHttpRequestType(HttpTypeEnum.get)
		        .setHttpRequestMethod(HttpMethodEnum.HTTP)
				.setReturnDataClassTypeEnum(ReturnDataClassTypeEnum.object)
				.setReturnModelClass(Object.class)
				.setMemoryLifeTime(0)
				.setIsSaveMemory(false)
				.setIsSaveDb(false)
		        .setIsMainThread(true)
		        .setHttpRequestMethod(EngineManager.getInstance().getEngineConfig().
		        		getHttpMethodForAll())
				.setJsonFetcher(EngineManager.getInstance().getEngineConfig()
						.getJsonFetcherFactory().newJsonFetcher());
		return requestOptions;
	}
	
	public RequestOptions setReturnModelClass(Class<? extends Object> returnModelClass){
		this.returnModelClass = returnModelClass;
		return this;
	}
	
	public Class<? extends Object> getReturnModelClass(){
		return returnModelClass;
	}
	
	public RequestOptions setReturnDataClassTypeEnum(ReturnDataClassTypeEnum returnDataClassTypeEnum){
		this.returnDataClassTypeEnum = returnDataClassTypeEnum;
		if(returnDataClassTypeEnum == null){
			this.returnDataClassTypeEnum = ReturnDataClassTypeEnum.object;
		}
		return this;
	}
	
	public ReturnDataClassTypeEnum getReturnDataClassTypeEnum(){
		return returnDataClassTypeEnum;
	}
	
	
	public RequestOptions setMemoryLifeTime(int memoryLifeTime){
		this.memoryLifeTime = memoryLifeTime;
		return this;
	}
	
	public int getMemoryLifeTime(){
		return memoryLifeTime;
	}
	
	public RequestOptions setHttpRequestMethod(HttpMethodEnum httpRequestMethod){
		this.httpRequestMethod = httpRequestMethod;
		if(this.httpRequestMethod == null){
			this.httpRequestMethod = HttpMethodEnum.HTTP;
		}
		return this;
	}
	
	public HttpMethodEnum getHttpRequestMethod(){
		return this.httpRequestMethod == null ? 
				EngineManager.getInstance().getEngineConfig().getHttpMethodForAll() :
					this.httpRequestMethod;
	}
	
	public RequestOptions setHttpRequestType(HttpTypeEnum httpRequestType){
		this.httpRequestType = httpRequestType;
		if(httpRequestType == null){
			this.httpRequestType = HttpTypeEnum.get;
		}
		return this;
	}
	
	public HttpTypeEnum getHttpRequestType(){
		return httpRequestType;
	}
	
	public RequestOptions setIsMainThread(boolean isMainThread){
		this.isMainThread = isMainThread;
		return this;
	}

	public boolean getIsMainThread(){
		return isMainThread;
	}
	
	public RequestOptions setIsSaveMemory(boolean isSaveMemory){
		this.isSaveMemory = isSaveMemory;
		return this;
	}

	public boolean getIsSaveMemory(){
		return isSaveMemory;
	}
	
	public RequestOptions setIsSaveDb(boolean isSaveDb){
		this.isSaveDb = isSaveDb;
		return this;
	}

	public boolean getIsSaveDb(){
		return isSaveDb;
	}
	
	public void addUploadFiles(List<BasicNameValuePair> list){
		this.uploadFileList.clear();
		this.uploadFileList.addAll(list);
	}

	public List<BasicNameValuePair> getUploadFileList() {
		return uploadFileList;
	}
	
	public boolean isConnectedTimeOut() {
		return isConnectedTimeOut;
	}


	public void setConnectedTimeOut(boolean isConnectedTimeOut) {
		this.isConnectedTimeOut = isConnectedTimeOut;
	}


	public String getTimeOutString() {
		return timeOutString;
	}


	public RequestOptions setTimeOutString(String timeOutString) {
		this.timeOutString = timeOutString;
		return this;
	}

	public RequestOptions setJsonFetcher(JsonFetcher fetcher) {
		this.fetcher = fetcher;
		return this;
	}

	public JsonFetcher getJsonFetcher() {
		if (this.fetcher == null) {
			this.fetcher = EngineManager.getInstance().getEngineConfig()
					.getJsonFetcherFactory().newJsonFetcher();
		}
		return this.fetcher;
	}
}
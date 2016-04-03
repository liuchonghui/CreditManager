package com.sina.engine.base.config;

import com.sina.engine.base.enums.HttpMethodEnum;
import com.sina.engine.base.manager.EngineManager;
import com.sina.engine.base.request.client.DefaultJsonFetcherFactory;
import com.sina.engine.base.request.client.JsonFetcherFactory;

/**
 * 整个引擎框架的设置
 * @author kangshaozhe
 *
 */
public class EngineConfig {
    private String signKey = "";
	private String partner_id = "";
	private String deviceId;
	private boolean isEncrypt = false;//是否加密
	private boolean ignoreAction = false;//是否忽略action字段
	/**链接超时时的提示**/
	private String timeOutString = null;

	private JsonFetcherFactory fetcherFactory = null;

	private String cid = "";//channel_id
	private String reviewState = "";// review_state
	private int cerResourceId; // cer 文件 raw id
	private HttpMethodEnum httpMethodForAll = HttpMethodEnum.HTTP;
	private boolean enableTimeOffset = false;

    public static EngineConfig getDefaultConfig(){
    	EngineConfig config = new EngineConfig();
    	config.setPartner_id("");
    	config.setSignKey("");
    	config.setIsEncrypt(false);
    	config.setHttpMethodForAll(HttpMethodEnum.HTTP);
		config.setJsonFetcherFactory(new DefaultJsonFetcherFactory());
		config.setTimeOffsetEnable(false);
		config.setCid("");
		config.setReviewState("");
    	return config;
    }
    
    public EngineConfig setPartner_id(String partner_id){
		this.partner_id = partner_id;
		return this;
	}

	public String getPartner_id(){
		return partner_id;
	}

	public EngineConfig setDebug(boolean isDebug){
		EngineManager.DEBUG = isDebug;
		return this;
	}

	public EngineConfig setCid(String cid){
		this.cid = cid;
		return this;
	}

	public String getCid(){
		return this.cid;
	}
	
	public String getReviewState() {
		return reviewState;
	}

	public void setReviewState(String reviewState) {
		this.reviewState = reviewState;
	}

	public EngineConfig setIgnoreAction(boolean ignore) {
    	this.ignoreAction = ignore;
    	return this;
    }
	
	public boolean isIgnoreAction() {
		return this.ignoreAction;
	}
	
	public EngineConfig setSignKey(String signKey){
		this.signKey = signKey;
		return this;
	}
	
	public String getSignKey(){
		return signKey;
	}
	
	public EngineConfig setIsEncrypt(boolean isEncrypt){
		this.isEncrypt = isEncrypt;
		return this;
	}
	
	public boolean getIsEncrypt(){
		return isEncrypt;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public EngineConfig setDeviceId(String deviceId) {
		this.deviceId = deviceId;
		return this;
	}

	public String getTimeOutString() {
		return timeOutString;
	}

	public EngineConfig setTimeOutString(String timeOutString) {
		this.timeOutString = timeOutString;
		return this;
	}
	
	public EngineConfig setJsonFetcherFactory(JsonFetcherFactory fetcherFactory) {
		this.fetcherFactory = fetcherFactory;
		return this;
	}

	public JsonFetcherFactory getJsonFetcherFactory() {
		if (this.fetcherFactory == null) {
			this.fetcherFactory = new DefaultJsonFetcherFactory();
		}
		return this.fetcherFactory;
	}

	public int getCerResourceId() {
		return cerResourceId;
	}

	public EngineConfig setCerResourceId(int cerResourceId) {
		this.cerResourceId = cerResourceId;
		return this;
	}

	public JsonFetcherFactory getFetcherFactory() {
		return fetcherFactory;
	}

	public EngineConfig setFetcherFactory(JsonFetcherFactory fetcherFactory) {
		this.fetcherFactory = fetcherFactory;
		return this;
	}

	public HttpMethodEnum getHttpMethodForAll() {
		return httpMethodForAll;
	}

	public EngineConfig setHttpMethodForAll(HttpMethodEnum httpMethodForAll) {
		this.httpMethodForAll = httpMethodForAll;
		if (null == httpMethodForAll) {
			this.httpMethodForAll = HttpMethodEnum.HTTP;
		}
		return this;
	}
	
	public EngineConfig setTimeOffsetEnable(boolean enable) {
		this.enableTimeOffset = enable;
		return this;
	}
	
	public boolean enableTimeOffset() {
		return this.enableTimeOffset;
	}
}

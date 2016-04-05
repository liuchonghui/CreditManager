package com.sina.engine.base.request.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.TextUtils;

import com.sina.engine.base.config.EngineConfig;
import com.sina.engine.base.manager.EngineManager;
import com.sina.engine.base.request.model.RequestModel;
import com.sina.engine.base.request.model.TaskModel;
import com.sina.engine.base.request.options.RequestOptions;
import com.sina.engine.base.utils.Constant;
import com.sina.engine.base.utils.LogUtils;
import com.sina.ep.EpUtils;

/**
 * HTTP请求返回字符串
 * @author kangshaozhe
 *
 */
public class HttpRequestUtils {
	
	public static long timeOffset = 0L;
	
	public static String getJsonStr(TaskModel taskModel){
		RequestOptions requestOptions = taskModel.getRequestOptions();
		RequestModel requestModel = taskModel.getRequestModel();
		String jsonStr = "";
		try{
			
			if(requestModel == null){
				return jsonStr;
			}
			
			String domainName = requestModel.getDomainName();
			
			String phpName = requestModel.getPhpName();
			
			LinkedHashMap<String, Object> listMap= Utils.getSelfRequestMap(requestModel);
			
		    LinkedHashMap<String, Object> pubMap = getPublicMap(taskModel);
		    
		    listMap.putAll(pubMap);
		    
		    String parameter = "";
		    
		    String sign = "";
			
		    EngineConfig config = EngineManager.getInstance().getEngineConfig();
		    boolean ignoreAction = config.isIgnoreAction();
		    
			for(String key : listMap.keySet()){					
				parameter=parameter+key+"="+URLEncoder.encode(String.valueOf(listMap.get(key)),"UTF-8")+"&";
				if (ignoreAction) {
					if (!key.equals(Constant.FILTER_PARTNER_ID)) {
						sign = sign + listMap.get(key);
					}
				} else {
					if (!key.equals(Constant.FILTER_ACTION)
							&& !key.equals(Constant.FILTER_PARTNER_ID)) {
						sign = sign + listMap.get(key);
					}
				}
			}
			sign = getSign(sign,taskModel);
			parameter = parameter+"sign"+"="+URLEncoder.encode(sign,"UTF-8");
//					+"&"+Constant.FILTER_PARTNER_ID+"="
//					+URLEncoder.encode(Constant.FILTER_PARTNER_ID_VALUE,"UTF-8");
			String resultUrl = domainName + phpName;
			if(!TextUtils.isEmpty(parameter)&& !TextUtils.isEmpty(phpName)){
				resultUrl = resultUrl + "?" + parameter;
			}
			LogUtils.d(Constant.ENGINE_REQUEST_LOG_TAG, "请求url="+resultUrl);
			taskModel.setRequestUrl(resultUrl);
			switch(requestOptions.getHttpRequestType()){
			   case get:
				   if (!EngineManager.DEBUG) {
					   jsonStr = HttpUtils.getHttpJsonStr(resultUrl,taskModel);
				   } else {
					   jsonStr = DebugHttpUtils.getHttpJsonStr(resultUrl,taskModel);
				   }
			   break;
			   case post:
					List<BasicNameValuePair> values = new ArrayList<BasicNameValuePair>();
			    	Iterator<?> iter = listMap.entrySet().iterator(); 
					while (iter.hasNext()) { 
					    Entry<?, ?> entry = (Entry<?, ?>) iter.next(); 
					    String key = (String)entry.getKey(); 
					    String val = String.valueOf(entry.getValue()); 
					    values.add(new BasicNameValuePair(key,val));
					} 
					 jsonStr = HttpUtils.postHttpJsonStr(resultUrl,values,taskModel);
			   break;
			   case upload:
				   List<BasicNameValuePair> values2 = new ArrayList<BasicNameValuePair>();
			    	Iterator<?> iter2 = listMap.entrySet().iterator(); 
					while (iter2.hasNext()) { 
					    Entry<?, ?> entry = (Entry<?, ?>) iter2.next(); 
					    String key = (String)entry.getKey(); 
					    String val = String.valueOf(entry.getValue()); 
					    values2.add(new BasicNameValuePair(key,val));
					} 
					jsonStr = HttpUtils.postUploadHttpJsonStr(resultUrl, requestOptions.getUploadFileList(), values2,taskModel);
				   break;
			  default:
				break;
			}
		}
		catch(Exception e){
           e.printStackTrace();
		}
		LogUtils.d(Constant.ENGINE_REQUEST_LOG_TAG, "返回json="+jsonStr);
		return jsonStr;
	}
	
	
	
	
    /**
	 * 获得请求url的公共部分
	 * @return
	 */
	public static LinkedHashMap<String, Object> getPublicMap(TaskModel taskModel){
		LinkedHashMap<String, Object> publicMap = new LinkedHashMap<String, Object>();
		try{
			Context con = EngineManager.getInstance().getContext();
			PackageInfo pi=con.getPackageManager().getPackageInfo(con.getPackageName(), 0);
			String v_name = taskModel.getRequestOptions().getVersionName();
			if (v_name == null || v_name.length() == 0) {
				v_name = con!=null?pi.versionName:"";
			}
			String v_code = taskModel.getRequestOptions().getVersionCode();
			if (v_code == null || v_code.length() == 0) {
				v_code = con!=null?String.valueOf(pi.versionCode):"";
			}
			String platform = "android";
			String sys_version = android.os.Build.VERSION.RELEASE;
//			Properties configPro = Utils.getConfigProperties(con);
//			String cid = configPro.getProperty("cid", "");
			String cid = EngineManager.getInstance().getEngineConfig().getCid();
			if (cid == null || cid.length() == 0) {
				cid = taskModel.getRequestOptions().getCid();
			}
			String channel_id = con!=null?cid:"";
			Long currentTime = System.currentTimeMillis();
			
			if (EngineManager.getInstance().getEngineConfig().enableTimeOffset()) {
				// timeOffset需要以毫秒为单位
				currentTime = currentTime + timeOffset;
			}
			String timestampStr = String.valueOf(currentTime);
			
			String partner_id = EngineManager.getInstance().getEngineConfig().getPartner_id();
			if (partner_id == null || partner_id.length() == 0) {
				partner_id = taskModel.getRequestOptions().getPartner_id();
			}
			String deviceId = EngineManager.getInstance().getEngineConfig().getDeviceId();
			publicMap.put("timestamp", timestampStr);
			publicMap.put("version", v_name);
			publicMap.put("version_code", v_code);
			publicMap.put("platform", platform);
			publicMap.put("sys_version", sys_version);
			publicMap.put("cid", channel_id);
			
			String review_state = EngineManager.getInstance().getEngineConfig().getReviewState();
			if (review_state != null && review_state.length() > 0) {
				publicMap.put("review_state", review_state); 
			}
			
			if(!TextUtils.isEmpty(partner_id)){
				publicMap.put(Constant.FILTER_PARTNER_ID, partner_id);
			}
			if (!TextUtils.isEmpty(deviceId)) {
				publicMap.put(Constant.FILTER_DEVICE_ID, deviceId);
			}
		}
		catch(Exception e){
			
		}
		return publicMap;
	}
	
	 
	 
	 private static String  getSign(String sign,TaskModel taskModel){
		EngineConfig config = EngineManager.getInstance().getEngineConfig();
		String signkey = config.getSignKey();
		
		if(TextUtils.isEmpty(signkey)){
			signkey = "";
		}
		String getSign = sign+signkey;
		
		LogUtils.d(Constant.ENGINE_REQUEST_LOG_TAG, "sign ="+getSign);
		getSign = MD5Utils.encode(getSign);
		if(config.getIsEncrypt()){
			getSign = EpUtils.getEpStr(EngineManager.getInstance().getContext(), taskModel, sign);
		}
		
		LogUtils.d(Constant.ENGINE_REQUEST_LOG_TAG, "md5 sign="+getSign);
		return getSign;
	 }
	
}

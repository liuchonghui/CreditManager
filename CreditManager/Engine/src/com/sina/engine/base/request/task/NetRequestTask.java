package com.sina.engine.base.request.task;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.sina.engine.base.enums.ReturnDataClassTypeEnum;
import com.sina.engine.base.enums.TaskStatus;
import com.sina.engine.base.manager.EngineManager;
import com.sina.engine.base.request.interfaces.DbLogicInterface;
import com.sina.engine.base.request.listener.TaskListener;
import com.sina.engine.base.request.model.TaskModel;
import com.sina.engine.base.request.options.RequestOptions;
import com.sina.engine.base.request.utils.HttpRequestUtils;
import com.sina.engine.base.utils.Constant;
import com.sina.engine.base.utils.LogUtils;

/**
 * 网路数据请求任务
 * @author kangshaozhe
 *
 */
public class NetRequestTask extends BaseRequestTask{

	public NetRequestTask(TaskModel taskModel, TaskListener taskListener,DbLogicInterface dbLogicObserver) { 
		super(taskModel, taskListener,dbLogicObserver);
		// TODO Auto-generated constructor stub
		this.dbLogicObserver = dbLogicObserver;
	}
	
	

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		taskStatus = TaskStatus.run;
		if(taskModel == null||taskModel.getRequestModel() == null){
			return;
		}
		try{
			LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
					   " 网络任务启动"+" key="+taskModel.getMapKey());
			parserlogic();
			saveData();
		}
		catch(Exception e){
           e.printStackTrace();
           LogUtils.e(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
    			   " 网络任务出现错误"+" key="+taskModel.getMapKey());
		}
		LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
				   " 网络任务完成并通知任务分配器"+" key="+taskModel.getMapKey());
		taskCallBack();
		LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
				   " 网络任务通知任务分配器完成"+" key="+taskModel.getMapKey());
		taskStatus = TaskStatus.stop;
	}
	
	
	public void parserlogic() throws Exception{
		String domainName = taskModel.getRequestModel().getDomainName();
		
		if(TextUtils.isEmpty(domainName)){
			LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
					   " 域名为空 网络任务中断"+" key="+taskModel.getMapKey());
			return;
		}
		
		String jsonStr = HttpRequestUtils.getJsonStr(taskModel);
		
	    JSONObject js = new JSONObject(jsonStr);
	    
	    if(!js.isNull("result")){
	    	String result = js.getString("result");
	    	taskModel.setResult(result);
	    }
	    else {
	    	LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
	 			   " 网络任务 result为空"+" key="+taskModel.getMapKey());
	    }
		    
	    if(!js.isNull("message")){
	    	String message = js.getString("message");
	    	taskModel.setMessage(message);
	    }
	    else {
	    	LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
	 			   " 网络任务 message为空"+" key="+taskModel.getMapKey());
	    }
	    
	    if (EngineManager.getInstance().getEngineConfig().enableTimeOffset()) {
	    	if(!js.isNull("time_offset")) {
	    		long time_offset = js.getLong("time_offset");
	    		HttpRequestUtils.timeOffset = HttpRequestUtils.timeOffset + time_offset;
	    	}
	    }
	    
	    Object returnModel = null;   
	    RequestOptions requestOptions = getTaskModel().getRequestOptions();
		ReturnDataClassTypeEnum returnDataClassTypeEnum = requestOptions.getReturnDataClassTypeEnum();
	    if(!js.isNull("data")){
	    	if(taskModel.getRequestOptions().getReturnModelClass() != null){
	    	    if(ReturnDataClassTypeEnum.list == returnDataClassTypeEnum){
	    	    	JSONArray dataArray = js.getJSONArray("data");
	    	    	returnModel = JSON.parseArray(dataArray.toString(), 
	    	    			taskModel.getRequestOptions().getReturnModelClass());
	    	    }
	    	    else if (ReturnDataClassTypeEnum.object == returnDataClassTypeEnum) {
			    	JSONObject dataObject= js.getJSONObject("data");
			 	    returnModel = JSON.parseObject(dataObject.toString(), 
			 	    		taskModel.getRequestOptions().getReturnModelClass());
	    	    }
	    	    else if (ReturnDataClassTypeEnum.generic == returnDataClassTypeEnum) {
			 	    returnModel = JSON.parseObject(js.toString(), 
			 	    		taskModel.getRequestOptions().getReturnModelClass());
	    	    }
		 	    
	    	}
	    	LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
		 			   " 数据解析完成 ReturnModel="+taskModel.getReturnModel()+" key="+taskModel.getMapKey());
	    }
	    else {
	    	if(taskModel.getRequestOptions().getReturnModelClass() != null){
	    		if (ReturnDataClassTypeEnum.generic == returnDataClassTypeEnum) {
			 	    returnModel = JSON.parseObject(js.toString(), 
			 	    		taskModel.getRequestOptions().getReturnModelClass());
	    	    }
	    	}
	    	LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
	 			   " 网络任务 data为空"+" key="+taskModel.getMapKey());
	    }
	    taskModel.setReturnModel(returnModel);
	}
	/**
	 * 储存数据
	 */
	private void saveData() {
		if(taskModel.getRequestOptions().getIsSaveDb()){
			  if(dbLogicObserver != null){
				  LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
						   " 网络任务 返回数据储存到数据库中开始"+" key="+taskModel.getMapKey());
				  dbLogicObserver.saveDb(taskModel);
				  LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
						   " 网络任务 返回数据储存到数据库中完成"+" key="+taskModel.getMapKey());
			  }
		  }
		
		  if(taskModel.getRequestOptions().getIsSaveMemory()){
			  LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
					   " 网络任务 返回数据添加到内存缓存中开始"+" key="+taskModel.getMapKey());
			  EngineManager.getInstance().getRequestDataTaskManager().memoryTaskManager.addMemoryMap(taskModel);
			  LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
					   " 网络任务 返回数据添加到内存缓存中完成"+" key="+taskModel.getMapKey());
		  }
		  
	}
}

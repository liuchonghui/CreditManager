package com.sina.engine.base.request.model;

import java.io.Serializable;
import java.util.LinkedList;

//import com.sina.engine.base.config.EngineConfig;
import com.sina.engine.base.enums.TaskTypeEnum;
import com.sina.engine.base.manager.EngineManager;
import com.sina.engine.base.request.options.RequestOptions;
import com.sina.engine.base.request.options.ReturnInfo;

/**
 * 任务数据模型
 * @author kangshaozhe
 *
 */
public class TaskModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**任务顺序**/
	private LinkedList<TaskTypeEnum> taskOrder = new LinkedList<TaskTypeEnum>();
	/**返回数据对象**/
	private Object returnModel;
	/**请求数据对象**/
	private RequestModel requestModel;

	private String message = "",result = "";
	/**请求设置**/
	private RequestOptions requestOptions;
	/**返回结果信息**/
	private ReturnInfo returnInfo;
	/**任务对应的key值**/
	private String mapKey = "";
	
	/**扩展key值**/
	private String expandKey = "";
	/**请求url**/
	private String requestUrl = "";
	/**任务标签**/
	private String tag = "";
	
	private boolean isRefresh;
	
	private int page;
	
	private boolean isAuToRefresh;//当本次任务流程完成是否自动下拉强刷
	
	public TaskModel(RequestOptions requestOptions,RequestModel requestModel){
		setRequestModel(requestModel);
		setRequestOptions(requestOptions);
	}
	
	
	public void setRequestUrl(String requestUrl){
		this.requestUrl = requestUrl;
	}
	
	public String getRequestUrl(){
		return requestUrl;
	}
	
	public void setIsAuToRefresh(boolean isAuToRefresh){
		this.isAuToRefresh = isAuToRefresh;
	}
	
	public boolean getIsAuToRefresh(){
		return isAuToRefresh;
	}
	
	public void setTaskOrders(LinkedList<TaskTypeEnum> taskOrder){
		this.taskOrder.clear();
		this.taskOrder.addAll(taskOrder);
	}
	
	public TaskModel addTaskOrder(TaskTypeEnum order){
		taskOrder.add(order);
		return this;
	}
	
	public void removeCurOrder(){
		if(taskOrder.size() == 0){
			return;
		}
		taskOrder.remove(0);
	}
	
	public boolean isTaskRun(){
		if(taskOrder.size() == 0){
			return false;
		}
		return true;
	}
	
	public TaskTypeEnum getCurTaskType(){
		if(taskOrder.size() == 0){
			return null;
		}
		return taskOrder.get(0);
	}
	
	public void clearTaskOrder(){
		taskOrder.clear();
	}
	
	public void setIsRefresh(boolean isRefresh){
		this.isRefresh = isRefresh;
	}
	
	public boolean getIsRefresh(){
		return isRefresh;
	}
	
	public void setReturnInfo(ReturnInfo returnInfo){
		this.returnInfo = returnInfo;
		if(returnInfo == null){
			this.returnInfo = new ReturnInfo();
		}
	}
	
	public ReturnInfo getReturnInfo(){
		return returnInfo;
	}
	
	public void setRequestOptions(RequestOptions requestOptions){
		this.requestOptions = requestOptions;
		
		if(requestOptions == null){
			this.requestOptions = RequestOptions.getDefaultOptions();
		}
		
		if(requestOptions.getTimeOutString()==null){
			this.requestOptions.setTimeOutString(EngineManager.getInstance().getEngineConfig().getTimeOutString());
		}
	}
	
	public RequestOptions getRequestOptions(){
		return requestOptions;
	}
	
	public void setRequestModel(RequestModel requestModel){
		this.requestModel = requestModel;
	}
	
	public RequestModel getRequestModel(){
		return requestModel;
	}
	
	public void setReturnModel(Object returnModel){
		this.returnModel = returnModel;
	}
	
	public Object getReturnModel(){
		return returnModel;
	}
	
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

	
	public void setMapKey(String mapKey){
		this.mapKey = mapKey;
	}
	
	public String getMapKey(){
		return mapKey;
	}
	
	public void setExpandKey(String expandKey){
		this.expandKey = expandKey;
	}
	
	public String getExpandKey(){
		return expandKey;
	}
	
	public void setTag(String tag){
		this.tag = tag;
	}
	
	public String getTag(){
		return tag;
	}
	
	public void setPage(int page){
		this.page = page;
	}
	
	public int getPage(){
		return page;
	}



	
}

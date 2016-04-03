package com.sina.engine.base.request.distribute;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

//import android.app.Activity;
//import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.sina.engine.base.enums.TaskTypeEnum;
import com.sina.engine.base.request.interfaces.DbLogicInterface;
import com.sina.engine.base.request.listener.RequestDataListener;
import com.sina.engine.base.request.listener.TaskListener;
import com.sina.engine.base.request.manager.DbTaskManager;
import com.sina.engine.base.request.manager.MemoryTaskManager;
import com.sina.engine.base.request.manager.NetTaskManager;
import com.sina.engine.base.request.model.TaskModel;
import com.sina.engine.base.request.options.ReturnInfo;
import com.sina.engine.base.utils.Constant;
import com.sina.engine.base.utils.LogUtils;
/**
 * 请求任务分配
 * @author kangshaozhe
 *
 */
public class TaskDistribute implements TaskListener{
	private NetTaskManager netTaskManager;
	private DbTaskManager dbTaskManager;
	private MemoryTaskManager memoryTaskManager;
	public ConcurrentHashMap<String,List<RequestDataListener>> observerMap = 
			new ConcurrentHashMap<String,List<RequestDataListener>>();
	
	public TaskDistribute(NetTaskManager netTaskManager,DbTaskManager dbTaskManager, 
			MemoryTaskManager memoryTaskManager){
		this.netTaskManager = netTaskManager;
		this.dbTaskManager = dbTaskManager;
		this.memoryTaskManager = memoryTaskManager;
    }
	
	/**
	 * 增加任务观察者
	 * @param taskKey
	 * @param observer
	 */
	public boolean addObserver(String taskKey,RequestDataListener observer){
	   if(!TextUtils.isEmpty(taskKey)){
		   LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "开始添加任务观察者 key="+taskKey);
		   List<RequestDataListener> observers = observerMap.get(taskKey);
		   if(observers != null){
			   boolean isHave = false;
			   for(RequestDataListener _observer:observers){
				   if(_observer == observer){
					   isHave = true;
					   break;
				   }
			    }
			   if(!isHave){
			      observers.add(observer);
			      LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务流程已存在 添加观察者 key="+taskKey);
			   }
			  
		   }
		   else {
			   List<RequestDataListener> addObservers = new ArrayList<RequestDataListener>();
			   addObservers.add(observer);
			   observerMap.put(taskKey, addObservers);
			   LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务流程不存在 创建观察者 key="+taskKey);
			   return true;
		   }
	   }
	   else {
		   LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务流程key为空 无法创建观察者");
	   }
	   
	   return false;
	}

	
  /**
   * 创建任务
   * @param taskModel
   * @param observer
   * @param taskLogicObserver
   */
  public void creatTask(TaskModel taskModel,RequestDataListener observer,DbLogicInterface taskLogicObserver){
	  boolean isAdd = addObserver(taskModel.getMapKey(),observer);
	  if(isAdd){
	     distribute(taskModel,taskLogicObserver);
	  }
	  
  }
	
	
 /**
  * 分配任务
  * @param task
  * @param observer
  */
   private void distribute(TaskModel taskModel,DbLogicInterface dbLogicObserver){
	  if(taskModel == null){
		   return;
	  }
	  LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "开始分配任务 key="+taskModel.getMapKey());
	  TaskTypeEnum curTaskType = taskModel.getCurTaskType();
	  LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "当前任务类型:"+curTaskType+" key="+taskModel.getMapKey());
	  if(curTaskType == null){
		  LogUtils.e(Constant.ENGINE_REQUEST_LOG_TAG, "当前任务类型不存在 无法分配任务"+" key="+taskModel.getMapKey());
		  return;
	  } 
	 
	  switch (curTaskType) {
		case getMemory:	
			memoryTaskManager.creatTask(taskModel, this ,dbLogicObserver);
			break;
		case getNet:
			netTaskManager.creatTask(taskModel,this,dbLogicObserver);
			break;
		case getDb:
			dbTaskManager.creatTask(taskModel,this, dbLogicObserver);
			break;
		default:
			break;
		}
	 
   }
   
   /**
    * 观察者回调
    * @param taskKey
    * @param taskModel
    */
   private void observersCallBack(String taskKey,TaskModel taskModel){
	   if(TextUtils.isEmpty(taskKey)){
		   return;
	   }
	   List<RequestDataListener> callbackObservers = observerMap.get(taskKey);
	   if(callbackObservers != null){
		   for(RequestDataListener observer:callbackObservers){
//			   if(observer != null&&isCallBack(observer)){
			   if(observer != null){
				   observer.resultCallBack(taskModel);
			   }
		   }
	   }
	  
   }
   
   /**
    * 任务完成回调后重新分配
    */
    @Override
	public void taskCallBack(TaskModel taskModel,DbLogicInterface taskLogicObserver) {
		// TODO Auto-generated method stub
    	 TaskTypeEnum curTaskType = taskModel.getCurTaskType();
    	 LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+curTaskType+" 通知适配器 key="+taskModel.getMapKey());
    	 if(curTaskType == null){
    		 LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "当前任务流程结束 key="+taskModel.getMapKey());
    		 return;
    	 }
    	 try{
	    	 ReturnInfo returnInfo = new ReturnInfo();
	    	 returnInfo.setTaskTypeEnum(curTaskType);
	    	 taskModel.setReturnInfo(returnInfo);
	    	 LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+curTaskType+" 创建返回信息成功 key="+taskModel.getMapKey());
	    	 taskModel.removeCurOrder();
	    	 LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+curTaskType+" 移除当前任务,并通知接口 key="+taskModel.getMapKey());
	    	 observersCallBack(taskModel.getMapKey(),taskModel);
	    	 LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+curTaskType+" 通知接口完成 key="+taskModel.getMapKey());
    	 }
    	 catch(Exception e){
    		 e.printStackTrace();
    		 LogUtils.e(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+curTaskType+" 任务回调出现异常 key="+taskModel.getMapKey());
    	 }
    	 finally{
    		 taskModel.setReturnModel(null);
	    	 if(taskModel.isTaskRun()){
	    		 LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+curTaskType+" 任务流程未结束 重新分配任务" +
	    		 		" key="+taskModel.getMapKey());
			    distribute(taskModel,taskLogicObserver);
	    	 }
	    	 else {
	    		 LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+curTaskType+" 任务流程结束" +
		    		 		" key="+taskModel.getMapKey());
	    		 clearObserver(taskModel.getMapKey());
	    		 observerMap.remove(taskModel.getMapKey());
	    		 LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+curTaskType+" 任务流程结束销毁观察者完成" +
		    		 		" key="+taskModel.getMapKey());
	    	 }
    	 }
    	
	}
    
    /**
     * 判断是否要回调
     * @param observer
     * @return
     */
//    private boolean isCallBack(RequestDataListener observer){
//    	try{
//			if(observer instanceof Fragment){
//				Fragment callBackFragment = (Fragment)observer;
//				if(callBackFragment.isDetached() || callBackFragment.getActivity() == null || 
//						callBackFragment.getActivity().isFinishing()){
//					return false;
//				}
//			}
//			else if(observer instanceof Activity){
//				Activity callBackActivity = (Activity)observer;
//				if(callBackActivity == null || callBackActivity.isFinishing()){
//					return false;
//				}
//			}
//			
//    	}
//    	catch(Exception e){
//    		
//    	}
//    	return true;
//    }
    
    /**
     * 清除观察者
     */
    private void clearObserver(String mapKey){
    	List<RequestDataListener> observerList= observerMap.get(mapKey);
    	if(observerList != null){
    		List<RequestDataListener> removeList = new ArrayList<RequestDataListener>();
    		for(RequestDataListener observer:observerList){
    			boolean isObserverLife = observerIsLife(mapKey,observer);
    			if(!isObserverLife){
    				LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "清除观察者 该观察者在任务列表中不存在，添加到删除集合中" +
    			                  " key="+mapKey);
    				removeList.add(observer);
    			}
    			else {
    				LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "清除观察者 该观察者在任务列表中存在，不能销毁" +
			                  " key="+mapKey);
    			}
    		}
    		
    		observerList.removeAll(removeList);
    		for(RequestDataListener removeOberver:removeList){
    			if(removeOberver != null){
    			   removeOberver = null;
    			}
    		}
    		removeList.clear();
    		System.gc();
    	}
    	else {
    		 LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "清除观察者 该任务流程不存在" +" key="+mapKey);
    	}
    }
    /**
     * 判断观察者对象在任务map中是否存在
     * @param observer
     * @return
     */
    @SuppressWarnings("unchecked")
	private boolean observerIsLife(String key,RequestDataListener observer){
    	Iterator<?> iter = observerMap.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Entry<?, ?> entry = (Entry<?, ?>) iter.next(); 
		    String mapKey = (String)entry.getKey();
		    List<RequestDataListener> mapList = (List<RequestDataListener>)entry.getValue();
		    if (!key.equals(mapKey)&&mapList!=null)
			{
		    	for(RequestDataListener mapObserver:mapList){
		    		if(mapObserver == observer){
		    			return true;
		    		}
		    	}
			}
		} 
    	return false;
    }
    
    /**
     * 销毁
     */
    public void destory(){
    	observerMap.clear();
    }

}

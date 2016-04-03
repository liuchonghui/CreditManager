package com.sina.engine.base.request.manager;

import android.text.TextUtils;

import com.sina.engine.base.request.distribute.TaskDistribute;
import com.sina.engine.base.request.interfaces.DbLogicInterface;
import com.sina.engine.base.request.listener.RequestDataListener;
import com.sina.engine.base.request.model.RequestModel;
import com.sina.engine.base.request.model.TaskModel;
import com.sina.engine.base.request.utils.Utils;
import com.sina.engine.base.utils.Constant;
import com.sina.engine.base.utils.LogUtils;
/**
 * 请求数据管理器类
 * @author kangshaozhe
 *
 */
public class RequestDataTaskManager {
	
	
	public TaskDistribute taskDistribute;
	
	public NetTaskManager netTaskManager;
	public DbTaskManager dbTaskManager;
	public MemoryTaskManager memoryTaskManager;
	
	public RequestDataTaskManager(){
		netTaskManager = new NetTaskManager();
		memoryTaskManager = new MemoryTaskManager();
		dbTaskManager = new DbTaskManager();
		
		taskDistribute = new TaskDistribute(netTaskManager, dbTaskManager, memoryTaskManager);
    }
	
	
   /**
    * 启动任务
    * @param taskModel
    * @param observer
    */
   public void startTask(TaskModel taskModel,RequestDataListener observer,DbLogicInterface taskLogicObserver){
	   if(taskModel == null){
		   LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务模型 taskModel为null");
		   return;
	   }
	   if(TextUtils.isEmpty(taskModel.getMapKey())){
		   String mapKey = createTaskKey(taskModel);
		   taskModel.setMapKey(mapKey);
	   }
	   LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务流程key创建成功，key="+taskModel.getMapKey());
	   LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务流程开始创建");
	   taskDistribute.creatTask(taskModel, observer ,taskLogicObserver);
   }
   
   
   /**
    * 注册观察者
    * @param requestModel
    * @param observer
    */
   public void registerObserver(RequestModel requestModel,RequestDataListener observer){
	   String mapKey = Utils.creatMapKey(requestModel);
	   taskDistribute.addObserver(mapKey, observer);
   }
   /**
    * 创建key
    * @param taskModel
    * @return
    */
   public String createTaskKey(TaskModel taskModel){
	   if(taskModel == null){
		   return "";
	   }
	   String mapKey = "";
	   if(taskModel.getRequestModel() != null){
	      mapKey = Utils.creatMapKey(taskModel.getRequestModel());
	   }
	   mapKey = mapKey + taskModel.getExpandKey();
	   if(TextUtils.isEmpty(mapKey)){
		   mapKey = String.valueOf(System.currentTimeMillis());
	   }
	   taskModel.setMapKey(mapKey);
	   return mapKey;
   }
  

   /**
    * 销毁
    */
   public void destory(){
	   netTaskManager.destory();
	   dbTaskManager.destory();
	   memoryTaskManager.destory();
	   taskDistribute.destory();
   }
}

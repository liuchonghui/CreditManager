package com.sina.engine.base.request.task;

import com.sina.engine.base.manager.EngineManager;
import com.sina.engine.base.request.interfaces.DbLogicInterface;
import com.sina.engine.base.request.listener.TaskListener;
import com.sina.engine.base.request.model.MemoryModel;
import com.sina.engine.base.request.model.TaskModel;
import com.sina.engine.base.request.utils.Utils;
import com.sina.engine.base.utils.Constant;
import com.sina.engine.base.utils.LogUtils;

/**
 * 缓存任务
 * @author kangshaozhe
 *
 */
public class MemoryRequestTask extends BaseRequestTask{
	public MemoryRequestTask(TaskModel taskModel,TaskListener taskListener,DbLogicInterface dbLogicObserver) { 
		super(taskModel, taskListener,dbLogicObserver);
		// TODO Auto-generated constructor stub
	}
	
 
	public void run() {
		// TODO Auto-generated method stub
		try{
			LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
					   " 缓存任务启动"+" key="+taskModel.getMapKey());
			 MemoryModel memoryData = EngineManager.getInstance().getRequestDataTaskManager().
					 memoryTaskManager.getMemoryData(taskModel.getMapKey());
			  if(memoryData != null){
				  MemoryModel copyMemory = Utils.cloneTo(memoryData);
				  taskModel.setReturnModel(copyMemory.getMemoryModel());
				  taskModel.setResult(copyMemory.getResult());
				  taskModel.setMessage(copyMemory.getMessage());
			  }
			
		}
		catch(Exception e){
			e.printStackTrace();
			LogUtils.e(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
					   " 缓存任务出现错误"+" key="+taskModel.getMapKey());
		}
		LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
				   " 缓存任务完成并通知任务分配器"+" key="+taskModel.getMapKey());
		taskCallBack();
		LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
				   " 缓存任务通知任务分配器完成"+" key="+taskModel.getMapKey());
	}
	
    
}

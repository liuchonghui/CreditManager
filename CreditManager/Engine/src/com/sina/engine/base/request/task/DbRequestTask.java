package com.sina.engine.base.request.task;

import com.sina.engine.base.enums.TaskTypeEnum;
import com.sina.engine.base.request.interfaces.DbLogicInterface;
import com.sina.engine.base.request.listener.TaskListener;
import com.sina.engine.base.request.model.TaskModel;
import com.sina.engine.base.utils.Constant;
import com.sina.engine.base.utils.LogUtils;
/**
 * 数据库任务
 * @author kangshaozhe
 *
 */

public class DbRequestTask extends BaseRequestTask{
     
	public DbRequestTask(TaskModel taskModel, TaskListener taskListener,DbLogicInterface dbLogicObserver) { 
		super(taskModel, taskListener,dbLogicObserver);
		// TODO Auto-generated constructor stub
	}
	
	
 
	public void run() {
		// TODO Auto-generated method stub
		try{
			LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
					   " 获取数据库数据任务启动"+" key="+taskModel.getMapKey());
			if(dbLogicObserver != null){
				TaskTypeEnum taskType = getTaskModel().getCurTaskType();
				if(taskType != null){
					switch(taskType){
						case getDb:
							dbLogicObserver.getDb(taskModel);
							LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
									   " 获取数据库数据任务完成"+
							" key="+taskModel.getMapKey());
						break;
					    default:
						break;
					}
				}
			}
			else {
				LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
						   " 获取数据库数据任务 实现接口为空"+" key="+taskModel.getMapKey());
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			LogUtils.e(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
					   " 获取数据库数据任务出现错误"+" key="+taskModel.getMapKey());
		}
		LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
				   " 获取数据库数据任务完成并通知任务分配器"+" key="+taskModel.getMapKey());
		taskCallBack();
		LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
				   " 获取数据库数据任务通知任务分配器完成"+" key="+taskModel.getMapKey());
	}

    
}

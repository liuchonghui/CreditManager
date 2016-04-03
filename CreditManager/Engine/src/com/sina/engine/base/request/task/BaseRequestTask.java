package com.sina.engine.base.request.task;

import com.sina.engine.base.enums.TaskStatus;
import com.sina.engine.base.manager.EngineManager;
import com.sina.engine.base.request.interfaces.DbLogicInterface;
import com.sina.engine.base.request.listener.TaskListener;
import com.sina.engine.base.request.model.TaskModel;

import android.widget.Toast;

public class BaseRequestTask implements Runnable{
	protected TaskModel taskModel;
	protected TaskListener taskListener;
	public TaskStatus taskStatus = TaskStatus.wait;
	protected DbLogicInterface dbLogicObserver;
	
	public BaseRequestTask(TaskModel taskModel,TaskListener taskListener,DbLogicInterface dbLogicObserver){
		this.taskModel = taskModel;
		setTaskListener(taskListener);
		setDbLogicObserver(dbLogicObserver);
	}
	
	public void setDbLogicObserver(DbLogicInterface dbLogicObserver){
		this.dbLogicObserver = dbLogicObserver;
	}
	
	public void setTaskListener(TaskListener taskListener){
		this.taskListener = taskListener;
	}
	
	public TaskModel getTaskModel(){
		return taskModel;
	}
	
	public DbLogicInterface getDbLogicObserver(){
		return dbLogicObserver;
	}
	
	
	/**
	 * 任务完成回调
	 */
	public void taskCallBack(){
		if(taskListener != null){
			if(taskModel.getRequestOptions().getIsMainThread()){
			   EngineManager.getInstance().getJsonHandlerManager().sendRunnable(new Runnable() {
				
				  @Override
				  public void run() {
					// 请求超时，且超时提示字符串不为null是，弹toast提示
					  if(taskModel.getRequestOptions().isConnectedTimeOut()&&taskModel.getRequestOptions().getTimeOutString()!=null){
						  Toast.makeText(EngineManager.getInstance().getContext(), taskModel.getRequestOptions().getTimeOutString(),Toast.LENGTH_SHORT).show();
					  }
					  taskListener.taskCallBack(taskModel,dbLogicObserver);
				  }
			   });
		    }
		    else {
		    	 taskListener.taskCallBack(taskModel,dbLogicObserver);
		    }
			
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

     
    
}

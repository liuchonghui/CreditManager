package com.sina.engine.base.request.manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.sina.engine.base.request.interfaces.DbLogicInterface;
import com.sina.engine.base.request.listener.TaskListener;
import com.sina.engine.base.request.model.TaskModel;
import com.sina.engine.base.request.task.DbRequestTask;
import com.sina.engine.base.utils.Constant;
import com.sina.engine.base.utils.LogUtils;
/**
 * 数据库数据请求管理类
 * @author kangshaozhe
 *
 */
public class DbTaskManager {
	private  int maxThreadCount = 10;
	private BlockingQueue<Runnable> downloadQueue = new LinkedBlockingQueue<Runnable>();
	private ExecutorService executorService;
	
	public DbTaskManager(){
		executorService = new ThreadPoolExecutor(this.maxThreadCount,
				maxThreadCount + 1, 1, TimeUnit.SECONDS,
				downloadQueue);
	}
 /**
  * 创建请求任务
  * @param task
  */
   public void creatTask(TaskModel taskModel,TaskListener taskListener, DbLogicInterface taskLogicObserver){
	   if(taskModel == null){
		   return;
	   }
	   LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
			   " 开始创建获取数据库数据任务"+" key="+taskModel.getMapKey());
	   if(executorService==null||executorService.isShutdown()){
		   executorService = new ThreadPoolExecutor(this.maxThreadCount,
					maxThreadCount + 1, 1, TimeUnit.SECONDS,
					downloadQueue);
	   }
	   DbRequestTask dbRequestTask = new DbRequestTask(taskModel, taskListener,taskLogicObserver);
	   LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
			   " 获取数据库数据任务创建成功"+" key="+taskModel.getMapKey());

	   executorService.submit(dbRequestTask);
   }
   
   

   /**
    * 销毁
    */
   public void destory(){
	   clearAllDownloadTask();
	   executorService.shutdown();
   }

   
   /**
	 * 清除所有下载任务
	 */
	public void clearAllDownloadTask(){
		if(downloadQueue != null){
			downloadQueue.clear();
		}
	}

}

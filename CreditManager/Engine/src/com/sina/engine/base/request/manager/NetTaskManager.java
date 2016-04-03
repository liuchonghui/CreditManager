package com.sina.engine.base.request.manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.sina.engine.base.request.interfaces.DbLogicInterface;
import com.sina.engine.base.request.listener.TaskListener;
import com.sina.engine.base.request.model.TaskModel;
import com.sina.engine.base.request.task.NetRequestTask;
import com.sina.engine.base.utils.Constant;
import com.sina.engine.base.utils.LogUtils;
/**
 * 网络数据请求管理类
 * @author kangshaozhe
 *
 */
public class NetTaskManager {
	private  int maxThreadCount = 10;
	private BlockingQueue<Runnable> downloadQueue = new LinkedBlockingQueue<Runnable>();
	private ExecutorService executorService;
	
	public NetTaskManager(){
		executorService = new ThreadPoolExecutor(this.maxThreadCount,
				maxThreadCount + 1, 1, TimeUnit.SECONDS,
				downloadQueue);
	}
 /**
  * 添加json请求任务
  * @param task
  */
   public void creatTask(TaskModel taskModel,TaskListener taskListener,
		   DbLogicInterface taskLogicObserver){
	   if(taskModel == null){
		   return;
	   }
	   LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
			   " 开始创建网络任务"+" key="+taskModel.getMapKey());
	   if(executorService == null||executorService.isShutdown()){
		   executorService = new ThreadPoolExecutor(this.maxThreadCount,
					maxThreadCount + 1, 1, TimeUnit.SECONDS,
					downloadQueue);
	   }
	   NetRequestTask netRequestTask = new NetRequestTask(taskModel,taskListener,taskLogicObserver);
	   executorService.submit(netRequestTask);
	   LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
			   " 网络任务创建成功"+" key="+taskModel.getMapKey());
   }
   
   

   /**
    * 销毁
    */
   public void destory(){
	   clearAllDownloadTask();
	   executorService.shutdownNow();
	   
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

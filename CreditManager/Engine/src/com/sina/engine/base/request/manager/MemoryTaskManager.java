package com.sina.engine.base.request.manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.text.TextUtils;

import com.sina.engine.base.request.interfaces.DbLogicInterface;
import com.sina.engine.base.request.listener.TaskListener;
import com.sina.engine.base.request.model.MemoryModel;
import com.sina.engine.base.request.model.TaskModel;
import com.sina.engine.base.request.task.MemoryRequestTask;
import com.sina.engine.base.request.utils.Utils;
import com.sina.engine.base.utils.Constant;
import com.sina.engine.base.utils.LogUtils;
/**
 * 内存数据请求管理类
 * @author kangshaozhe
 *
 */
public class MemoryTaskManager {
	private  int maxThreadCount = 10;
	public ConcurrentHashMap<String,MemoryModel> memoryMap = new ConcurrentHashMap<String,MemoryModel>();
	private BlockingQueue<Runnable> downloadQueue = new LinkedBlockingQueue<Runnable>();
	private ExecutorService executorService;
	public MemoryTaskManager(){
		executorService = new ThreadPoolExecutor(this.maxThreadCount,
				maxThreadCount + 1, 1, TimeUnit.SECONDS,
				downloadQueue);
	}
    
    /**
     * 缓存任务
     * @param task
     */
      public void creatTask(TaskModel taskModel,TaskListener taskListener,
	         DbLogicInterface taskLogicObserver){
	   	   if(taskModel == null){
	   		   return;
	   	   }
	       LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
				   " 开始创建获取缓存任务"+" key="+taskModel.getMapKey());
	   	   if(executorService==null||executorService.isShutdown()){
	   		 executorService = new ThreadPoolExecutor(this.maxThreadCount,
					maxThreadCount + 1, 1, TimeUnit.SECONDS,
					downloadQueue);
		   }
	       MemoryRequestTask memoryRequestTask = new MemoryRequestTask(taskModel,taskListener,taskLogicObserver);
		   executorService.submit(memoryRequestTask);
		   LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "任务"+taskModel.getCurTaskType()+
				   " 获取缓存任务创建成功"+" key="+taskModel.getMapKey());
      }
	/**
	 * 获取缓存数据
	 * @param mapKey
	 * @return
	 */
	public MemoryModel getMemoryData(String mapKey){
		if(TextUtils.isEmpty(mapKey)){
			return null;
		}
		MemoryModel memoryModel = memoryMap.get(mapKey);
	    if(memoryModel != null){
	    	//判断是否过期
	    	long creatTime = memoryModel.getCreatTime();
	    	long lifeTime = memoryModel.getLifeTime()*1000;
	    	long curTime = System.currentTimeMillis();
		    if(lifeTime>0&&curTime - creatTime >=lifeTime){
			   removeMemory(mapKey);
			   return null;
		    }
		    
		   return memoryModel;
	    }
		return null;
	}
	
	public void removeMemory(String mapKey){
		if(TextUtils.isEmpty(mapKey)){
			return;
		}
		 memoryMap.remove(mapKey);
	}
	
	/**
	 * 添加数据缓存
	 * @param taskModel
	 */
	public void addMemoryMap(TaskModel taskModel){
		if(taskModel != null && !TextUtils.isEmpty(taskModel.getMapKey())){
			LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "数据添加到内存缓存中启动"+" key="+taskModel.getMapKey());
			MemoryModel memoryModel = new MemoryModel();
			memoryModel.setCreatTime(System.currentTimeMillis());
			memoryModel.setLifeTime(taskModel.getRequestOptions().getMemoryLifeTime());
			memoryModel.setMessage(taskModel.getMessage());
			memoryModel.setResult(taskModel.getResult());
			try {
				Object copyModel = Utils.cloneTo(taskModel.getReturnModel());
				memoryModel.setMemoryModel(copyModel);
				memoryMap.put(taskModel.getMapKey(), memoryModel);
				LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "数据成功添加到内存缓存中"+" key="+taskModel.getMapKey());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LogUtils.i(Constant.ENGINE_REQUEST_LOG_TAG, "数据添加到内存缓存中出现异常"+" key="+taskModel.getMapKey());
			}
			
		}
	}
	
	public void addMemoryMap(String mapKey,MemoryModel memoryModel){
		if(memoryModel != null||TextUtils.isEmpty(mapKey)){
			try {
				memoryModel.setMemoryModel(memoryModel);
				memoryMap.put(mapKey, memoryModel);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
   

   /**
    * 销毁
    */
   public void destory(){
	   memoryMap.clear();
	   if(downloadQueue != null){
			downloadQueue.clear();
		}
	   executorService.shutdown();
   }
   
}

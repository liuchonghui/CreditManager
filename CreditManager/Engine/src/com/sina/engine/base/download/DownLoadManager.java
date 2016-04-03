package com.sina.engine.base.download;

import java.io.File;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;
/**
 * 下载管理器 不是单例模式
 * @author administrator
 *
 */
public class DownLoadManager {
	public ConcurrentHashMap<String,DownLoadTask> requestMap = new ConcurrentHashMap<String,DownLoadTask>();
	/**
	 * 同时运行线程最大值
	 * */
	public int maxThreadCount = 1;
	/** 线程运行Service */
	public ExecutorService downloadExecutor;

	/** 下载队列 capacity队列长度，超过capacity的 */
	public BlockingQueue<Runnable> downloadQueue = new LinkedBlockingQueue<Runnable>();
	
	public Context context;
	
	public boolean denyDownLoadImg;
	
	public DownLoadListener downLoadListener;
	public DownLoadManager(Context context,int maxThreadCount){
		this.maxThreadCount = maxThreadCount;
		downloadExecutor = new ThreadPoolExecutor(this.maxThreadCount,
				maxThreadCount + 1, 1, TimeUnit.SECONDS,
				downloadQueue);
		this.context = context;
	}
	
	public void setDenyDownLoadImg(boolean denyDownLoadImg){
		this.denyDownLoadImg = denyDownLoadImg;
	}
	
	public boolean getDenyDownLoadImg(){
		return denyDownLoadImg;
	}
	
	public File getImageDownLoadFile(Context context){
		File file = new File(DownLoadFile.getFileCachePath(context,DownLoadConstant.DOWNLOAD_IMAGE_DIR),"");
		return file;
	}
	
	public void setDownLoadListener(DownLoadListener downLoadListener){
		this.downLoadListener = downLoadListener;
	}
	
	public DownLoadListener getDownLoadListener(){
		return downLoadListener;
	}
	
	
	public boolean isRequestMap(String url){
		if(requestMap.containsKey(url)){
			return true;
		}
		return false;
	}
	
	public DownloadItem getMyApkItem(String url,String version,String appName,int iconId){
		DownloadItem item = new DownloadItem();
		item.setUrl(url);
		item.setFileName(DownLoadUtils.getMD5(url+version));
		item.setSuffixName(DownLoadConstant.DOWNLOAD_APK_SUFFIXNAME);
		item.setChildDirs(DownLoadConstant.DOWNLOAD_APK_DIR);
		item.setAppName(appName);
		item.setIconId(iconId);
		return item;
	}
	
	public void addMyApkDownLoadTask(String url,String version,String appName,int iconId){
		requestMap.remove(url);
		DownLoadTask newTask = requestMap.get(url);
		if(newTask == null){
			DownloadItem item = getMyApkItem(url,version,appName,iconId);
			newTask = new MyApkDownLoadTask(context,this,item);
			Future<Integer> future = downloadExecutor.submit(newTask,0);
			newTask.setFuture(future);
			requestMap.put(item.getUrl(), newTask);
		}
		
	}
	
	public DownloadItem getImageItem(String url){
		DownloadItem item = new DownloadItem();
		item.setUrl(url);
		item.setFileName(DownLoadUtils.getMD5(url));
		item.setSuffixName(DownLoadConstant.DOWNLOAD_IMAGE_SUFFIXNAME);
		item.setChildDirs(DownLoadConstant.DOWNLOAD_IMAGE_DIR);
		return item;
	}
	
	public void addImageDownLoadTask(String url){
		requestMap.remove(url);
		DownLoadTask newTask = requestMap.get(url);
		if(newTask == null){
			DownloadItem item = getImageItem(url);
			newTask = new ImageDownLoadTask(context,this,item);
			Future<Integer> future = downloadExecutor.submit(newTask,0);
			newTask.setFuture(future);
			requestMap.put(item.getUrl(), newTask);
		}
		
	}
	
	
	
	/**
	 * 增加下载任务
	 * @param observer
	 * @param item
	 */
	public void addDownLoadTask(DownloadItem item){
		DownLoadTask newTask = requestMap.get(item.getUrl());
		if(newTask == null){
			newTask = new DownLoadTask(context,this,item);
			Future<Integer> future = downloadExecutor.submit(newTask,0);
			newTask.setFuture(future);
			requestMap.put(item.getUrl(), newTask);
		}
		
	}
	
	/**
	 * 停止某个下载任务
	 * @param url
	 */
	public void stopDownLoadTask(String url){
		DownLoadTask newTask = requestMap.get(url);
		if(newTask != null){
			newTask.setInterrept(true);
			removeTask(url);
			Future<Integer> future = newTask.getFuture();
			if(future != null){
				future.cancel(true);
			}
		}
	}
	
	public void removeTask(String url){
		if(requestMap != null){
			requestMap.remove(url);
		}
	}
	
	/**
	 * 清除所有下载任务
	 */
	public void clearAllDownloadTask(){
		if(requestMap != null){
			Iterator<?> iter = requestMap.entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Entry<?, ?> entry = (Entry<?, ?>) iter.next(); 
			    DownLoadTask newTask = (DownLoadTask)entry.getValue();
			    if (newTask!=null)
				{
			    	newTask.setInterrept(true);
			    	Future<Integer> f = newTask.getFuture();
			    	if(f != null){
					   f.cancel(true);
			    	}
				}
			} 
			requestMap.clear();
		}
		if(downloadQueue != null){
			downloadQueue.clear();
		}
//		if(downloadExecutor != null){
//		   downloadExecutor.shutdown();
//		}
	}
}

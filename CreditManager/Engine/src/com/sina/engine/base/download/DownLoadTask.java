package com.sina.engine.base.download;

import java.util.concurrent.Future;

import android.content.Context;

import com.sina.engine.base.manager.EngineManager;


public class DownLoadTask implements Runnable{
//	"http://dl.games.sina.com.cn/game/app/sinacard.apk"
	public DownLoadManager downLoadManager;
	
	/**下载数据对象**/
	public DownloadItem downLoadItem;
	
	public Context myContext;
	
	/**future对象**/
	public Future<Integer> future;
	
	/**停止下载标志位**/
	public boolean isInterrept;
	
	public boolean isQueue = true;
	public DownLoadTask(Context myContext,DownLoadManager downLoadManager,
			DownloadItem downLoadItem){
		this.downLoadItem = downLoadItem;
		this.myContext = myContext.getApplicationContext();
		this.downLoadManager = downLoadManager;
	}
	
	public void setFuture(Future<Integer> future){
		this.future = future;
	}
	
	public Future<Integer> getFuture(){
		return this.future;
	}
	
	public void setInterrept(boolean isInterrept){
		this.isInterrept = isInterrept;
	}
	
	public int httpDownLoad(){
		
		return 0;
	
}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		 httpDownLoad();
	}
	/**
	 * handler调用
	 */
	public void taskCallBack(int type){
		if(downLoadManager.getDownLoadListener() != null){
			final int callBackType = type;
			EngineManager.getInstance().getJsonHandlerManager().sendRunnable(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					 downLoadManager.getDownLoadListener().downLoadCallBack(downLoadItem, callBackType);
				}
			});
		}
	}
}

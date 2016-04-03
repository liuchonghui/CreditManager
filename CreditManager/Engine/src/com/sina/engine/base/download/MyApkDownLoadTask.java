package com.sina.engine.base.download;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class MyApkDownLoadTask extends DownLoadTask{
	/** 通知栏管理器 */
	private NotificationManager notificationManager;
	/** 通知id的基数 */
	private static final int notificationBase = 3015001;
	/** 通知id的增量 */
	private static final AtomicInteger notificationCount = new AtomicInteger(1);
	private Notification notification;
	/** 通知id */
	private int notificationId;
	
	private int requsetTimes;
	
	private String appName = "";
	
	public MyApkDownLoadTask(Context myContext,
			DownLoadManager downLoadManager, DownloadItem downLoadItem) {
		super(myContext, downLoadManager, downLoadItem);
		// TODO Auto-generated constructor stub
		notificationManager = (NotificationManager) myContext.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification(downLoadItem.getIconId(),
				"开始下载", System.currentTimeMillis());
		notification.defaults = Notification.DEFAULT_LIGHTS;
		notificationId = notificationBase
				+ notificationCount.getAndIncrement();
		// 重置notificationCount，避免无限增长
		if (notificationCount.get() > 10000) {
			notificationCount.set(1);
		}
		appName = downLoadItem.getAppName()==null?"":downLoadItem.getAppName();
	}

	public int httpDownLoad(){
		isQueue = false;
		String url = downLoadItem.getUrl();
		String fileName = downLoadItem.getFileName();
		String childDir = downLoadItem.getChildDir();
		String suffixName = downLoadItem.getSuffixName();
		
		//删除缓存文件
		String cachPath = null;
		File cachFile =  new File(DownLoadFile.getFileCachePath(myContext,childDir),
				fileName +DownLoadConstant.DOWNLOAD_CACHE_SUFFIXNAME);
		if (cachFile != null && cachFile.exists()) {
			cachPath = cachFile.getAbsolutePath();
		}
		if(!TextUtils.isEmpty(cachPath)){
		   DownLoadFile.deteleFile(cachPath);
		}
		while(requsetTimes>=0&&requsetTimes<3){
			long totalSize = DownLoadFile.getDownLoadFileSize(url);
			if (totalSize < 1000L) {
				Log.d("DLF", "!totalSize = " + totalSize + ", < 1000, retry!");
				try {
					Thread.sleep(1000L);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				requsetTimes++;
				continue;
			}
			String path = DownLoadFile.isFileExist(myContext, childDir, fileName, suffixName, totalSize);
			//如果有此文件返回
			if(!TextUtils.isEmpty(path)){
				downLoadItem.setLocalPath(path);
				taskCallBack(DownLoadConstant.DOWNLOAD_STATUS_FILE_EXIST);
				if(!TextUtils.isEmpty(downLoadItem.getLocalPath())){
					   DownLoadUtils.installApkForPath(myContext.getApplicationContext(), downLoadItem.getLocalPath());
			    }
				return 0;
			}
			long completeSize = DownLoadFile.getlocalCacheFileSize(myContext, childDir, 
					fileName, DownLoadConstant.DOWNLOAD_CACHE_SUFFIXNAME);
			Log.d("DLF", "totalSize=" + totalSize + ", completeSize=" + completeSize);
			InputStream inputStream = null;
			BufferedOutputStream bout = null;
			FileOutputStream fo = null;
			if(TextUtils.isEmpty(url)){
				return 0;
			}
			taskCallBack(DownLoadConstant.DOWNLOAD_STATUS_START);
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, DownLoadConstant.DOWNLOAD_HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, DownLoadConstant.DOWNLOAD_HTTP_TIMEOUT);
			
			HttpClient httpClient = new DefaultHttpClient(params);
			HttpGet httpGet = new HttpGet(url);
			try {
				//设置下载的数据位置XX字节到XX字节
	            Header header_size = new BasicHeader("Range", "bytes=" + completeSize + "-"  
	                    + totalSize);  
	            httpGet.addHeader(header_size); 
				HttpResponse response = httpClient.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				if (statusLine != null && statusLine.getStatusCode() == 206||statusLine.getStatusCode() == 200){
					HttpEntity entity = response.getEntity();
					inputStream = entity.getContent();
					File theTempFile = new File(DownLoadFile.getFileCachePath(myContext,childDir),
							fileName + DownLoadConstant.DOWNLOAD_CACHE_SUFFIXNAME);
					fo = new FileOutputStream(theTempFile,true);
					bout = new BufferedOutputStream(fo,4096*4); 
					int length = -1;
					byte[] buffer = new byte[4096];
					int origin_percent = 0;
					while ((length = inputStream.read(buffer)) != -1) {
						if (Thread.interrupted()||isInterrept) {
							taskCallBack(DownLoadConstant.DOWNLOAD_STATUS_DOWNLOAD_PAUSE);
							downLoadManager.removeTask(downLoadItem.getUrl());
							return 0;
						}
						bout.write(buffer,0,length);
						completeSize += length;
						int progress = (int) (((double) (completeSize) / totalSize) * 100);
						if (progress - origin_percent >= 1) {
							origin_percent = progress;
							downLoadItem.setProgress(progress);
							taskCallBack(DownLoadConstant.DOWNLOAD_STATUS_DOWNLOADING);
							try {
								PendingIntent contentIntent = PendingIntent.getActivity(myContext.getApplicationContext(), 0, new Intent(), 0);
								notification.setLatestEventInfo(myContext.getApplicationContext(),
										appName, "已完成" + progress + "%",contentIntent);
								notificationManager.notify(notificationId, notification);
							} catch (Exception e) {
								e.printStackTrace();
								if (e != null && e.getMessage() != null) {
									Log.d("DLF", e.getMessage());
								}
							}
						}
						
					}
					path = DownLoadFile.renameDownloadFile(myContext, childDir, fileName, suffixName);
					if (DownLoadFile.isFileExist(myContext, childDir, fileName, suffixName, totalSize) != null) {
						requsetTimes = -1;
						downLoadItem.setLocalPath(path);
						taskCallBack(DownLoadConstant.DOWNLOAD_STATUS_COMPLETE);
						downLoadManager.removeTask(downLoadItem.getUrl());
						try {
							notificationManager.cancel(notificationId);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if(!TextUtils.isEmpty(downLoadItem.getLocalPath())){
							DownLoadUtils.installApkForPath(myContext.getApplicationContext(), downLoadItem.getLocalPath());
						}
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				if (e != null && e.getMessage() != null) {
					Log.d("DLF", e.getMessage());
				}
			}finally{
				requsetTimes++;
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				inputStream = null;
				
				if (bout != null)
				{
					try {
						bout.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if(fo != null){
					try {
						fo.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(8000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	    return 0;
     }
}

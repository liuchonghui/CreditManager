package com.sina.engine.base.download;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

import android.content.Context;
import android.text.TextUtils;

import com.sina.engine.base.manager.EngineManager;

public class ImageDownLoadTask extends DownLoadTask{
	private int requsetTimes;
	public ImageDownLoadTask(Context myContext,
			DownLoadManager downLoadManager, DownloadItem downLoadItem) {
		super(myContext, downLoadManager, downLoadItem);
		// TODO Auto-generated constructor stub
	}

	public int httpDownLoad(){
		isQueue = false;
		String url = downLoadItem.getUrl();
		String fileName = downLoadItem.getFileName();
		String childDir = downLoadItem.getChildDir();
		String suffixName = downLoadItem.getSuffixName();
		//删除缓存文件
		File cachFile =  new File(DownLoadFile.getFileCachePath(myContext,childDir),
				fileName +DownLoadConstant.DOWNLOAD_CACHE_SUFFIXNAME);
		String cachPath = cachFile.getPath();
		if(!TextUtils.isEmpty(cachPath)){
		   DownLoadFile.deteleFile(cachPath);
		}

		while(requsetTimes>=0&&requsetTimes<3){
			long totalSize = DownLoadFile.getDownLoadFileSize(url);
			String path = DownLoadFile.isFileExist(myContext, childDir, fileName, suffixName, totalSize);
			//如果有此文件返回
			if(!TextUtils.isEmpty(path)){
				downLoadItem.setLocalPath(path);
				taskCallBack(DownLoadConstant.DOWNLOAD_STATUS_FILE_EXIST);
				return 0;
			}
			if(EngineManager.getInstance().getImageDownLoadManager().getDenyDownLoadImg()){
				taskCallBack(DownLoadConstant.DOWNLOAD_STATUS_FAIL);
				return 0;
			}
			long completeSize = DownLoadFile.getlocalCacheFileSize(myContext, childDir, 
					fileName, DownLoadConstant.DOWNLOAD_CACHE_SUFFIXNAME);
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
						}
						
					}
					path = DownLoadFile.renameDownloadFile(myContext, childDir, fileName, suffixName);
					requsetTimes = -1;
					downLoadItem.setLocalPath(path);
					taskCallBack(DownLoadConstant.DOWNLOAD_STATUS_COMPLETE);
					downLoadManager.removeTask(downLoadItem.getUrl());
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(requsetTimes >= 2){
					taskCallBack(DownLoadConstant.DOWNLOAD_STATUS_FAIL);
					return 0;
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
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		 }
	    return 0;
     }
}

package com.sina.engine.base.download;

import java.io.Serializable;

/**
 * 下载数据模型 定义下载信息
 * @author administrator
 *
 */
public class DownloadItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**应用id标识 如果是视频下载为视频ID*/
	private String id;
	/** 下载文件下载地址 */
	private String url;
	/** 下载文件程序名 */
	private String appName;
	/** 下载文件文件名 */
	private String fileName;
	/** 下载文件包名 */
	private String packageName;
	/**下载文件本地地址*/
	private String localPath;
	/**下载文件本地缓存地址*/
	private String localCachePath;
	/**下载进度**/
	private int progress;
	/**子目录**/
	private String childDir;
	/**后缀名**/
	private String suffixName;
	
	/**下载状态**/
	private int downLoadStatus;
	
	/**文件总大小**/
	private long totalFileSize;
	
	private long currentFileSize;
	
	private int iconId;
	
	public void setIconId(int iconId){
		this.iconId = iconId;
	}
	
	public int getIconId(){
		return iconId;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getId(){
		return id;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getUrl(){
		return url;
	}
	
	public void setAppName(String appName){
		this.appName = appName;
	}
	
	public String getAppName(){
		return appName;
	}
	
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	
	public String getFileName(){
		return fileName;
	}
	
	public void setPackageName(String packageName){
		this.packageName = packageName;
	}
	
	public String getPackageName(){
		return packageName;
	}
	
	
	public void setLocalPath(String localPath){
		this.localPath = localPath;
	}
	
	public String getLocalPath(){
		return localPath;
	}
	
	public void setLocalCachePath(String localCachePath){
		this.localCachePath = localCachePath;
	}
	
	public String getLocalCachePath(){
		return localCachePath;
	}
	
	public void setProgress(int progress){
		this.progress = progress;
	}
	
	public int getProgress(){
		return progress;
	}
	
	public void setChildDirs(String childDir){
		this.childDir = childDir;
	}
	
	public String getChildDir(){
		return childDir;
	}
	
	public void setSuffixName(String suffixName){
		this.suffixName = suffixName;
	}
	
	public String getSuffixName(){
		return suffixName;
	}
	
	public void setDownLoadStatus(int downLoadStatus){
		this.downLoadStatus = downLoadStatus;
	}
	
	public int getDownLoadStatus(){
		return downLoadStatus;
	}
	
	public void setTotalFileSize(long totalFileSize){
		this.totalFileSize = totalFileSize;
	}
	
	public long getTotalFileSize(){
		return totalFileSize;
	}
	
	public void setCurrentFileSize(long currentFileSize){
		this.currentFileSize = currentFileSize;
	}
	
	public long getCurrentFileSize(){
		return currentFileSize;
	}
	
}

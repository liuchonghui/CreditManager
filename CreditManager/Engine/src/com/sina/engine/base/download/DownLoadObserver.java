package com.sina.engine.base.download;

/**
 * 下载文件观察者类
 * @author administrator
 *
 */
public abstract class DownLoadObserver {
	/**
	 * 下载回调方法
	 * @param item
	 * @param downLoadStatus
	 */
	 public abstract void downLoadCallBack(DownloadItem item,int downLoadStatus);
}

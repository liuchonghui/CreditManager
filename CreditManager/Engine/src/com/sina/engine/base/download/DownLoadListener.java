package com.sina.engine.base.download;

public interface DownLoadListener {
	/**
	 * 下载回调方法
	 * @param item
	 * @param downLoadStatus
	 */
	 public void downLoadCallBack(DownloadItem item,int downLoadStatus);
}

package com.sina.engine.base.download;

public class DownLoadConstant {
	/** http请求超时时长 15秒 */
	public static final int DOWNLOAD_HTTP_TIMEOUT = 5000;
	
	/**下载状态**/
	public static final int DOWNLOAD_STATUS_UNDOWNLOAD = 0;
	public static final int DOWNLOAD_STATUS_COMPLETE = 100001;
	public static final int DOWNLOAD_STATUS_DOWNLOADING = 100002;
	public static final int DOWNLOAD_STATUS_FILE_EXIST = 100003;
	public static final int DOWNLOAD_STATUS_DOWNLOAD_PAUSE = 100004;
	public static final int DOWNLOAD_STATUS_START= 100005;
	public static final int DOWNLOAD_STATUS_FAIL= 100006;
	public static final int DOWNLOAD_STATUS_DOWNLOAD_WAIT = 100007;
	
	/**下载文件APK储存目录**/
	public static final String DOWNLOAD_APK_DIR = "/DownLoadFile/Apk";
	/**下载文件视频储存目录**/
	public static final String DOWNLOAD_VIDEO_DIR = "/DownLoadFile/Video";
	/**下载图片储存目录**/
	public static final String DOWNLOAD_IMAGE_DIR = "/DownLoadFile/Image";
	/**下载缓存的后缀名**/
	public static final String DOWNLOAD_CACHE_SUFFIXNAME = ".temp";
	/**apk包下载的后缀名**/
	public static final String DOWNLOAD_APK_SUFFIXNAME = ".apk";
	/**视频下载的后缀名**/
	public static final String DOWNLOAD_VIDEO_SUFFIXNAME = ".mp4";
	/**视频图片的后缀名**/
	public static final String DOWNLOAD_IMAGE_SUFFIXNAME = ".jpg";
	
}

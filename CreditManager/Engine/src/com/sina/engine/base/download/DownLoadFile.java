package com.sina.engine.base.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

/**
 * 下载文件管理
 * @author kangshaozhe
 *
 */
public class DownLoadFile {
	/**
	 * 获取本地文件地址
	 * @param context
	 * @param childDir 子目录
	 * @return
	 */
	   public static File getFileCachePath(Context context,String childDir){
		   return getSubCacheDirectory(context, childDir);
//		  File cacheDir = null;
//		// 判断 SDCard 是否存在
//		  if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//			  if (Build.VERSION.SDK_INT >= 8) // API Level 8
//				{
//					Method method = null;
//					try {
//						method = context.getClass()
//								.getMethod("getExternalCacheDir");
//					} catch (SecurityException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					} catch (NoSuchMethodException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					if (method != null) {
//						try {
//							String cacheDirPath = method.invoke(context,
//									new Object[] {}).toString();
//							cacheDir = new File(cacheDirPath+ childDir);
//						} catch (IllegalArgumentException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (IllegalAccessException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (InvocationTargetException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (Exception e) {
//							// e.printStackTrace();
//						}
//					}
//				} else {
//					cacheDir = new File(Environment.getExternalStorageDirectory(),
//							"/Android/data/" + context.getPackageName() + childDir);
//					
//				}
//			  if (cacheDir!=null && !cacheDir.exists()) {
//				  cacheDir.mkdirs();
//			  }
//			  
//			  if (cacheDir == null || !cacheDir.exists()
//						|| !cacheDir.isDirectory()) {
//				  //使用内部存储空间
//				  cacheDir = context.getCacheDir();
//			  }
//			  
//			  
//			  
//		  }
//		  return cacheDir;
	   }
	   
	   public static String getLoaclVideoPath(Context context,String url){
		   File file = new File(getFileCachePath(context,DownLoadConstant.DOWNLOAD_VIDEO_DIR),
				   DownLoadUtils.getMD5(url) + DownLoadConstant.DOWNLOAD_VIDEO_SUFFIXNAME);
		   String loaclPath = null;
		   if(file.exists()){
			   loaclPath = file.getPath();
		   }
		   return loaclPath;
	   }
	   
	   /**
	    *  获取要下载文件的大小
	    * @param url
	    * @return
	    */
	   public static long getDownLoadFileSize(String url){
		   HttpParams params = new BasicHttpParams();
		   HttpConnectionParams.setConnectionTimeout(params, DownLoadConstant.DOWNLOAD_HTTP_TIMEOUT);
		   HttpConnectionParams.setSoTimeout(params, DownLoadConstant.DOWNLOAD_HTTP_TIMEOUT);
		   HttpResponse response_test = null;  
		   HttpClient client_test = new DefaultHttpClient(params); 
		   HttpGet request_test = new HttpGet(url); 
		   long fileSize = 0;
           try {
				response_test = client_test.execute(request_test);
				 //获取需要下载文件的大小  
		        fileSize = response_test.getEntity().getContentLength();  
			} catch(Exception e) {
				
			}
	          
	       return fileSize;
	   }
	/**
	 * 获取本地缓存文件大小
	 * @param context
	 * @param childDir
	 * @param fileName
	 * @param suffixName
	 * @return
	 */
	   public static long getlocalCacheFileSize(Context context,String childDir,String fileName,String suffixName){
		   long fileSize = 0;
		   File dir =  getFileCachePath(context,childDir);
		   File dF = new File(dir,fileName+suffixName); 
		   FileInputStream fis = null; 
		   try {
			    fis = new FileInputStream(dF);
			    fileSize = fis.available();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		      catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   finally{
				if(fis != null){
					try {
						fis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		   
		   return fileSize;
	   }
	   
	   public static long getlocalCacheFileSize(Context context,String cachPath){
		   long fileSize = 0;
		   File dF = new File(cachPath); 
		   FileInputStream fis = null; 
		   try {
			    fis = new FileInputStream(dF);
			    fileSize = fis.available();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		      catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   finally{
				if(fis != null){
					try {
						fis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		   
		   return fileSize;
	   }
	  /**
	   * 文件包下载成功后，把文件改名，去掉.temp后缀
	   * @param context
	   * @param childDir 子目录
	   * @param fileName 
	   * @param suffixName 后缀名
	   * @return
	   */
		public static String renameDownloadFile(Context context,String childDir,
				String fileName,String suffixName) {
			if (!TextUtils.isEmpty(fileName)) {
				File filePath = getFileCachePath(context,childDir);
				File cacheFile = new File(filePath,fileName + 
						DownLoadConstant.DOWNLOAD_CACHE_SUFFIXNAME);
				if (cacheFile != null && cacheFile.exists()) {
					File newFile = new File(filePath, fileName+suffixName);
					if (newFile != null && cacheFile.renameTo(newFile)) {
						
						return newFile.getAbsolutePath();
					}
				}
			}
			return null;
		}
		
		public static String renameDownloadFile(Context context,String path,String renamePath) {
			if (!TextUtils.isEmpty(path)) {
				File cacheFile = new File(path);
				if (cacheFile.exists()) {
					File newFile = new File(renamePath);
					if (newFile != null && cacheFile.renameTo(newFile)) {
						
						return newFile.getAbsolutePath();
					}
				}
			}
			return null;
		}
	  /**
	   * 判断文件是否存在
	   * @param context
	   * @param childDir
	   * @param fileName
	   * @param suffixName
	   * @return
	   */
	  public static String isFileExist(Context context,String childDir,
				String fileName,String suffixName){
		  File file = new File(getFileCachePath(context,childDir),fileName +suffixName);
			if (file.exists()) {
				return file.getPath();
			} 
		  return null;
	  }
	  
	  public static String isFileExist(Context context,String childDir,
				String fileName,String suffixName, long targetSize){
		  File file = new File(getFileCachePath(context,childDir),fileName +suffixName);
			if (file != null && file.exists()) {
				long fileSize = file.length();
				Log.d("DLF", "[apk]" + fileName + suffixName + "[download]"  + fileSize + "[target]" + targetSize);
				if (targetSize != fileSize) {
					return null;
				}
				return file.getPath();
			} 
		  return null;
	  }
	  
	  public static String isFileExist(Context context,String path){
		  File file = new File(path);
			if (file.exists()) {
				return file.getPath();
			} 
		  return null;
	  }
	  
	  public static String getFilePath(Context context,String childDir,
				String fileName,String suffixName){
		  File file = new File(getFileCachePath(context,childDir),fileName +suffixName);
			if (file.exists()) {
				return file.getPath();
			} 
			return file.getPath();
	  }
	  
	  /**
	   * 删除指定路径文件
	   * @param path
	   */
	  public static void deteleFile(String path){
		  if(TextUtils.isEmpty(path)){
			  return;
		  }
		  File file = new File(path);
		  file.delete();
	  }
	  /**
	   * 删除目录文件夹下所有文件
	   * @param file
	   */
	  public static void DeleteAllFile(Context context,String suffixname) {  
		  File file = getFileCachePath(context,DownLoadConstant.DOWNLOAD_VIDEO_DIR);
		  File temp=null;
		  File[] filelist= file.listFiles();
		  if(filelist.length == 0){
			  return;
		  }
		  for(int i=0;i<filelist.length;i++){
			  temp=filelist[i];
			  if(temp.getName().endsWith(suffixname)){
			    temp.delete();
			  }
		  }
	    }  
	  
	public static File getCacheDirectory(Context context) {
		File appCacheDir = null;
		if (android.os.Environment.MEDIA_MOUNTED.equals(
				Environment.getExternalStorageState())) {
			appCacheDir = getExternalCacheDir(context);
		}
		if (appCacheDir != null) {
			Log.w("DLF", "[ok]got appCacheDir.getAbsolutePath=" + appCacheDir.getAbsolutePath());
			return appCacheDir;
		}
		if (appCacheDir == null) {
			appCacheDir = getAppCacheDir(context);
		}
		if (appCacheDir != null) {
			Log.w("DLF", "[ok]regot appCacheDir.getAbsolutePath=" + appCacheDir.getAbsolutePath());
			return appCacheDir;
		}
		if (appCacheDir == null) {
			appCacheDir = getFilsDir(context);
		}
		if (appCacheDir != null) {
			Log.w("DLF", "[ok]reregot appCacheDir.getAbsolutePath=" + appCacheDir.getAbsolutePath());
			return appCacheDir;
		}
		Log.w("DLF", "[fail]failure to get appCacheDir!");
		return appCacheDir;
	}
	
	public static File getExternalCacheDir(Context context) {
		File dataDir = new File(new File(
				Environment.getExternalStorageDirectory(), "Android"), "data");
		File appCacheDir = new File(
				new File(dataDir, context.getPackageName()), "cache");
		if (!appCacheDir.exists()) {
			if (!appCacheDir.mkdirs()) {
				Log.w("DLF", "Unable to create external cache directory");
				Log.w("DLF", "appCacheDir.getPath=" + appCacheDir.getPath());
				Log.w("DLF", "dataDir.getPath=" + dataDir.getPath());
				return null;
			}
			try {
				new File(appCacheDir, ".nomedia").createNewFile();
			} catch (Exception e) {
				Log.i("DLF",
						"Can't create \".nomedia\" file in application external cache directory");
			}
		}
		return appCacheDir;
	}
	
	public static File getAppCacheDir(Context context) {
		File appCacheDir = context.getCacheDir();
		if (!appCacheDir.exists()) {
			if (!appCacheDir.mkdirs()) {
				Log.w("DLF", "Unable to create app cache directory");
				Log.w("DLF", "appCacheDir.getPath=" + appCacheDir.getPath());
				return null;
			}
			try {
				new File(appCacheDir, ".nomedia").createNewFile();
			} catch (Exception e) {
				Log.i("DLF",
						"Can't create \".nomedia\" file in application cache directory");
			}
		}
		return appCacheDir;
	}
	
	public static File getFilsDir(Context context) {
		File appCacheDir = context.getFilesDir();
		if (!appCacheDir.exists()) {
			if (!appCacheDir.mkdirs()) {
				Log.w("DLF", "Unable to create app files directory");
				Log.w("DLF", "appCacheDir.getPath=" + appCacheDir.getPath());
				return null;
			}
			try {
				new File(appCacheDir, ".nomedia").createNewFile();
			} catch (Exception e) {
				Log.i("DLF",
						"Can't create \".nomedia\" file in application fils directory");
			}
		}
		return appCacheDir;
	}
	
	public static File getSubCacheDirectory(Context context, String childDir) {
		File cacheDir = getCacheDirectory(context);
		if (cacheDir != null && cacheDir.exists()) {
			String childDirPath = cacheDir.getAbsolutePath() + childDir;
			File subCacheDir = new File(childDirPath);
			if (!subCacheDir.exists()) {
				if (!subCacheDir.mkdirs()) {
					Log.w("DLF", "[fail]Unable to create external subcache "
							+ childDir + " directory");
					subCacheDir = cacheDir;
				}
			}
			return subCacheDir;
		}
		return cacheDir;
	}
}

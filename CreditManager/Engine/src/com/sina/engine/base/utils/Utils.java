package com.sina.engine.base.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class Utils {
	  /**
	   * 数据库data下路径
	   * @param context
	   * @return
	   */
	  public static File getFileDbPath(Context context){
		  File cacheDir = null;
		  try{
			  cacheDir = new File(context.getApplicationContext().getFilesDir(),"/db4o");
		  }
		  catch(Exception e){
			  
		  }
	
		  return cacheDir;
	  }
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
	   
	   public static File getCacheDirectory(Context context) {
			File appCacheDir = null;
			if (android.os.Environment.MEDIA_MOUNTED.equals(
					Environment.getExternalStorageState())) {
				appCacheDir = getExternalCacheDir(context);
			}
			if (appCacheDir != null) {
				Log.w("Utils", "[ok]got appCacheDir.getAbsolutePath=" + appCacheDir.getAbsolutePath());
				return appCacheDir;
			}
			if (appCacheDir == null) {
				appCacheDir = getAppCacheDir(context);
			}
			if (appCacheDir != null) {
				Log.w("Utils", "[ok]regot appCacheDir.getAbsolutePath=" + appCacheDir.getAbsolutePath());
				return appCacheDir;
			}
			if (appCacheDir == null) {
				appCacheDir = getFilsDir(context);
			}
			if (appCacheDir != null) {
				Log.w("Utils", "[ok]reregot appCacheDir.getAbsolutePath=" + appCacheDir.getAbsolutePath());
				return appCacheDir;
			}
			Log.w("Utils", "[fail]failure to get appCacheDir!");
			return appCacheDir;
		}
		
		public static File getExternalCacheDir(Context context) {
			File dataDir = new File(new File(
					Environment.getExternalStorageDirectory(), "Android"), "data");
			File appCacheDir = new File(
					new File(dataDir, context.getPackageName()), "cache");
			if (!appCacheDir.exists()) {
				if (!appCacheDir.mkdirs()) {
					Log.w("Utils", "Unable to create external cache directory");
					Log.w("Utils", "appCacheDir.getPath=" + appCacheDir.getPath());
					Log.w("Utils", "dataDir.getPath=" + dataDir.getPath());
					return null;
				}
				try {
					new File(appCacheDir, ".nomedia").createNewFile();
				} catch (Exception e) {
					Log.i("Utils",
							"Can't create \".nomedia\" file in application external cache directory");
				}
			}
			return appCacheDir;
		}
		
		public static File getAppCacheDir(Context context) {
			File appCacheDir = context.getCacheDir();
			if (!appCacheDir.exists()) {
				if (!appCacheDir.mkdirs()) {
					Log.w("Utils", "Unable to create app cache directory");
					Log.w("Utils", "appCacheDir.getPath=" + appCacheDir.getPath());
					return null;
				}
				try {
					new File(appCacheDir, ".nomedia").createNewFile();
				} catch (Exception e) {
					Log.i("Utils",
							"Can't create \".nomedia\" file in application cache directory");
				}
			}
			return appCacheDir;
		}
		
		public static File getFilsDir(Context context) {
			File appCacheDir = context.getFilesDir();
			if (!appCacheDir.exists()) {
				if (!appCacheDir.mkdirs()) {
					Log.w("Utils", "Unable to create app files directory");
					Log.w("Utils", "appCacheDir.getPath=" + appCacheDir.getPath());
					return null;
				}
				try {
					new File(appCacheDir, ".nomedia").createNewFile();
				} catch (Exception e) {
					Log.i("Utils",
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
						Log.w("Utils", "Unable to create external subcache "
								+ childDir + " directory");
						subCacheDir = cacheDir;
					}
				}
				return subCacheDir;
			}
			return cacheDir;
		}
		
		public static File getExternalDir(Context context) {
			File dataDir = new File(new File(
					Environment.getExternalStorageDirectory(), "Android"), "data");
			File externalDir = new File(dataDir, context.getPackageName());
			if (!externalDir.exists()) {
				if (!externalDir.mkdirs()) {
					Log.w("Utils", "Unable to create external directory");
					Log.w("Utils", "externalDir.getPath=" + externalDir.getPath());
					return null;
				}
				try {
					new File(externalDir, ".nomedia").createNewFile();
				} catch (Exception e) {
					Log.i("Utils",
							"Can't create \".nomedia\" file in application external directory");
				}
			}
			return externalDir;
		}
}

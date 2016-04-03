package com.sina.engine.base.db4o;

import java.io.File;

import android.text.TextUtils;

public class Db4oUitls {
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
}

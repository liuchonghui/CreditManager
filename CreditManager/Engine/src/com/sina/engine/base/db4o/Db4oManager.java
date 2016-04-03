package com.sina.engine.base.db4o;

import java.util.LinkedHashMap;

import android.content.Context;

/**
 * db4o数据库管理器
 * @author kangshaozhe
 *
 */
public class Db4oManager {
//     public static Object lock = new Object();
	 public static LinkedHashMap<String, Object> locks = new LinkedHashMap<String, Object>(0, 0.75f, true);
     
     public static Context context;
     
     public static void init(Context context){
    	 Db4oManager.context = context;
     }
}

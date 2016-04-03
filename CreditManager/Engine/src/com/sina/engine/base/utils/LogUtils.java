package com.sina.engine.base.utils;

import android.util.Log;

import com.sina.engine.base.manager.EngineManager;

/**
 * Log工具类
 * 
 * 封装系统Log工具类，便于统一Log的输出和关闭
 * 
 * @author com.renren
 * 
 */

public class LogUtils {
	public static boolean LOG = EngineManager.DEBUG;

	public static int v(String msg) {
		return v("", msg);
	}

	public static int d(String msg) {
		return d("", msg);
	}

	public static int i(String msg) {
		return i("", msg);
	}

	public static int w(String msg) {
		return w("", msg);
	}

	public static int e(String msg) {
		return e("", msg);
	}

	public static int e(Exception exception) {
		StackTraceElement[] ste = exception.getStackTrace();
		StringBuilder sb = new StringBuilder();
		try {
			String name = exception.getClass().getName();
			String message = exception.getMessage();
			String content = (message != null) ? (name + ":" + message) : name;
			sb.append(content + "\n");
			for (StackTraceElement s : ste) {
				sb.append(s.toString() + "\n");
			}
		} catch (Exception e) {
		}
		return w("", sb.toString());
	}
	
	public static int v(String tag, String msg) {
		if(!LOG){
			return 0;
		}
		return Log.v(tag, getTracePrefix("v") + msg);
	}

	public static int d(String tag, String msg) {
		if(!LOG){
			return 0;
		}
		return Log.d(tag, getTracePrefix("d") + tag + " " + msg);
	}

	public static int i(String tag, String msg) {
		if(!LOG){
			return 0;
		}
		return Log.i(tag, getTracePrefix("i") + tag + " " + msg);
	}

	public static int w(String tag, String msg) {
		if(!LOG){
			return 0;
		}
		return Log.w(tag, getTracePrefix("w") + tag + " " + msg);
	}

	public static int e(String tag, String msg) {
		if(!LOG){
			return 0;
		}
		return Log.e(tag, getTracePrefix("e") + tag + " " + msg);
	}

	public static int v(String tag, String msg, Throwable tr) {
		if(!LOG){
			return 0;
		}
		return Log.v(tag, tag + ": " + msg, tr);
	}

	public static int d(String tag, String msg, Throwable tr) {
		if(!LOG){
			return 0;
		}
		return Log.d(tag, tag + ": " + msg, tr);
	}

	public static int i(String tag, String msg, Throwable tr) {
		if(!LOG){
			return 0;
		}
		return Log.i(tag, tag + ": " + msg, tr);
	}

	public static int w(String tag, String msg, Throwable tr) {
		if(!LOG){
			return 0;
		}
		return Log.w(tag, tag + ": " + msg, tr);
	}

	public static int e(String tag, String msg, Throwable tr) {
		if(!LOG){
			return 0;
		}
		return Log.e(tag, tag + ": " + msg, tr);
	}

	private static String getTracePrefix(String logLevel) {
		StackTraceElement[] sts = new Throwable().getStackTrace();
		StackTraceElement st = null;
		for (int i = 0; i < sts.length; i++) {
			if (sts[i].getMethodName().equalsIgnoreCase(logLevel)
					&& i + 2 < sts.length) {

				if (sts[i + 1].getMethodName().equalsIgnoreCase(logLevel)) {
					st = sts[i + 2];
					break;
				} else {
					st = sts[i + 1];
					break;
				}
			}
		}
		if (st == null) {
			return "";
		}

		String clsName = st.getClassName();
		if (clsName.contains("$")) {
			clsName = clsName.substring(clsName.lastIndexOf(".") + 1,
					clsName.indexOf("$"));
		} else {
			clsName = clsName.substring(clsName.lastIndexOf(".") + 1);
		}
		return clsName + "-> " + st.getMethodName() + "():";
	}
	
	public static void sysOut(String outStr){
		if(!LOG){
			return;
		}
		System.out.println(outStr);
	}
}

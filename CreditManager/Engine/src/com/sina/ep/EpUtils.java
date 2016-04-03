package com.sina.ep;

import android.content.Context;

public class EpUtils {
	static {
		System.loadLibrary("GetEp");
	}
	
	/**
	 * 加密字段
	 * @param ctx
	 * @param str
	 * @return
	 */
	public static native String getEpStr(Context ctx, String str);
	
	/**
	 * Debug字段
	 * @param ctx
	 * @param str
	 * @return
	 */
	public static native String getHexStr(Context ctx, String str);
}

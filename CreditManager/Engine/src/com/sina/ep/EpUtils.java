package com.sina.ep;

import android.content.Context;

import com.sina.engine.base.request.utils.MD5Utils;

public class EpUtils {
//	static {
//		System.loadLibrary("GetEp");
//	}
	
	/**
	 * 加密字段
	 * @param ctx
	 * @param str
	 * @return
	 */
//	public static native String getEpStr(Context ctx, String str);
	public static String getEpStr(Context ctx, String str) {
		String sn = "1020981062"; // [新浪游戏][1020981062][手游攻略][-1526612315]
		String contentChar = str + sn;
		String keyStr = "0fe1ed9affcdd242451d9883f506a3dd";// 新浪游戏
		keyStr = keyStr.substring(2, 22);
		System.out.println("cut key=" + keyStr);
		String head = contentChar.substring(0, 2);
		String tail = contentChar.substring(2, contentChar.length());
		String szText = head + keyStr + tail;
		System.out.println("finish str=" + szText);
		String encryptStr = MD5Utils.encode(szText);
		System.out.println("encryptStr=" + encryptStr);
		return encryptStr;
	}
	/**
	 * Debug字段
	 * @param ctx
	 * @param str
	 * @return
	 */
//	public static native String getHexStr(Context ctx, String str);
}

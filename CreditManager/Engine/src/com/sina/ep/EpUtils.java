package com.sina.ep;

import android.content.Context;

import com.sina.engine.base.request.model.TaskModel;
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
	public static String getEpStr(Context ctx, TaskModel taskModel,String str) {
		String sn = taskModel.getRequestOptions().getSn();
		if (sn == null) {
			sn = "1020984062";
		}
		String contentChar = str + sn;
		String keyStr = taskModel.getRequestOptions().getKeyStr();
		if (keyStr == null || keyStr.length() == 0) {
			keyStr = "0fe1ed9affcdd242451d9884f506a3dd";
		}
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

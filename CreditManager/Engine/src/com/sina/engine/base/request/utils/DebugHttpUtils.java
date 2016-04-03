package com.sina.engine.base.request.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.sina.engine.base.manager.EngineManager;
import com.sina.engine.base.request.model.TaskModel;

public class DebugHttpUtils extends HttpUtils {

	public static String getHttpJsonStr(String getUrl,TaskModel taskModel) {
		if (getUrl.startsWith("file:///android_asset/")) {
			int slashIndex = getUrl.indexOf("android_asset") + 13;
			try {
				String filePath = getUrl.substring(slashIndex + 1);
				InputStream is = EngineManager.getInstance().getContext()
						.getAssets().open(filePath);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int i = -1;
				while ((i = is.read()) != -1) {
					baos.write(i);
				}
				return new String(baos.toString().getBytes(), "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		return HttpUtils.getHttpJsonStr(getUrl,taskModel);
	}
}

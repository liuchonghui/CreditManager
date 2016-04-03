package com.sina.engine.base.request.utils;

import com.sina.engine.base.request.model.TaskModel;

import org.apache.http.message.BasicNameValuePair;

import java.util.List;

/**
 * http请求工具类
 *
 * @author kangshaozhe
 *
 */
public class HttpUtils {

	/**
	 * get请求获取json字符串数据
	 *
	 * @param getUrl
	 * @return
	 */
	public static String getHttpJsonStr(String getUrl,TaskModel taskModel) {
		String jsonStr = null;

		jsonStr = taskModel.getRequestOptions().getJsonFetcher()
				.get(getUrl, taskModel);

		return jsonStr;
	}

	/**
	 * 表单发送POST请求
	 *
	 * @param postUrl
	 * @return
	 */
	public static String postHttpJsonStr(String postUrl,
			List<BasicNameValuePair> values,TaskModel taskModel) {
		String returnData = null;

		returnData = taskModel.getRequestOptions().getJsonFetcher()
				.post(postUrl, values, taskModel);

		return returnData;
	}

	/**
	 * 表单上传文件or文字请求
	 *
	 * @param postUrl
	 * @return
	 */
	public static String postUploadHttpJsonStr(String postUrl,
			List<BasicNameValuePair> filelist, List<BasicNameValuePair> values, TaskModel taskModel) {
		String returnData = null;

		returnData = taskModel.getRequestOptions().getJsonFetcher()
				.post(postUrl, filelist, values, taskModel);

		return returnData;
	}

}

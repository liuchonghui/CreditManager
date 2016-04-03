package com.sina.engine.base.request.client;

import com.sina.engine.base.request.model.TaskModel;

import org.apache.http.message.BasicNameValuePair;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author liu_chonghui
 *
 */
public interface JsonFetcher extends Serializable {

    /**
     * get请求
     * @param url
     * @param taskModel
     * @return
     */
    String get(String url, TaskModel taskModel);

    /**
     * post提交
     * @param postUrl
     * @param values
     * @param taskModel
     * @return
     */
    String post(String postUrl,
                List<BasicNameValuePair> values, TaskModel taskModel);

    /**
     * post表单提交（上传文件）
     * @param postUrl
     * @param filelist
     * @param values
     * @param taskModel
     * @return
     */
    String post(String postUrl, List<BasicNameValuePair> filelist,
                List<BasicNameValuePair> values, TaskModel taskModel);
}

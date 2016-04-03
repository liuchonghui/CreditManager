package com.sina.engine.base.request.listener;

import com.sina.engine.base.request.model.TaskModel;

/**
 * 任务完成回调监听
 * @author kangshaozhe
 *
 */
public interface RequestDataListener {
	 public  void resultCallBack(TaskModel taskModel);

}

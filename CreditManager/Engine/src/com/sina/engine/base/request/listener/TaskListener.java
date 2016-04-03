package com.sina.engine.base.request.listener;

import com.sina.engine.base.request.interfaces.DbLogicInterface;
import com.sina.engine.base.request.model.TaskModel;
/**
 * 任务完成监听
 * @author kangshaozhe
 *
 */
public interface TaskListener {
	 public void taskCallBack(TaskModel taskModel,DbLogicInterface taskLogicObserver);
}

package com.sina.engine.base.request.options;

import com.sina.engine.base.enums.TaskTypeEnum;


public class ReturnInfo{

	
	private TaskTypeEnum taskTypeEnum;
	
	
	public ReturnInfo(){
		
	}
	
	
	public void setTaskTypeEnum(TaskTypeEnum taskTypeEnum){
		this.taskTypeEnum = taskTypeEnum;
		
	}
	
	public TaskTypeEnum getTaskTypeEnum(){
		return taskTypeEnum;
	}
	
}

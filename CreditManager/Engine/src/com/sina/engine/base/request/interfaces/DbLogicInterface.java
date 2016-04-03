package com.sina.engine.base.request.interfaces;

import com.sina.engine.base.request.model.TaskModel;



/**
 * 数据库操作逻辑实现的抽象类
 * @author kangshaozhe
 *
 */

public abstract  class DbLogicInterface{
	
    
    public abstract void saveDb(TaskModel taskModel);
    

    public abstract void getDb(TaskModel taskModel);
    

}

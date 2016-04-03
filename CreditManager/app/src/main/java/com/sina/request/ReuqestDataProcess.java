 package com.sina.request;

import com.sina.engine.base.enums.TaskTypeEnum;
import com.sina.engine.base.manager.EngineManager;
import com.sina.engine.base.request.interfaces.DbLogicInterface;
import com.sina.engine.base.request.listener.RequestDataListener;
import com.sina.engine.base.request.manager.MemoryTaskManager;
import com.sina.engine.base.request.model.MemoryModel;
import com.sina.engine.base.request.model.RequestModel;
import com.sina.engine.base.request.model.TaskModel;
import com.sina.engine.base.request.options.RequestOptions;
import com.sina.engine.base.request.utils.Utils;

 /**
  * 任务请求数据的二次封装
  * @author kangshaozhe
  *
  */
 public class ReuqestDataProcess {
     /**
      * 数据请求
      * @param isRefresh
      * @param page
      * @param requestDataListener
      * @param dbLogicInterface
      */
    public static void requestData(boolean isRefresh,int page,RequestModel requestModel,RequestOptions requestOptions,
            RequestDataListener requestDataListener,DbLogicInterface dbLogicInterface){
        //无网络提示
        if(!NetWorkUtil.isNetworkAvailable(EngineManager.getInstance().getContext())){
            new NoNetToastDialog(EngineManager.getInstance().getContext()).setWaitTitle(
                     "哎呦~您的网络有问题，\n请查看网络设置").showMe();
         }
         TaskModel taskModel = creatTaskModel(isRefresh,page,requestModel,requestOptions);
         EngineManager.getInstance().getRequestDataTaskManager().startTask(taskModel, requestDataListener,dbLogicInterface);
    }

    /**
     * 后台请求专用
     * @param isRefresh
     * @param requestModel
     * @param requestOptions
     * @param requestDataListener
     * @param dbLogicInterface
     */
     public static void requestData(boolean isRefresh,
             RequestModel requestModel, RequestOptions requestOptions,
             RequestDataListener requestDataListener,
             DbLogicInterface dbLogicInterface) {
         TaskModel taskModel = creatTaskModel(isRefresh,Integer.MAX_VALUE,requestModel,requestOptions);
         EngineManager.getInstance().getRequestDataTaskManager().startTask(taskModel, requestDataListener,dbLogicInterface);
     }

    /**
     * 创建taskmodel
     * @param isRefresh
     * @param page
     * @param requestModel
     * @param requestOptions
     * @return
     */
    private static TaskModel creatTaskModel(boolean isRefresh,int page,
            RequestModel requestModel,RequestOptions requestOptions){
         TaskModel taskModel = new TaskModel(requestOptions, requestModel);
         String mapKey = EngineManager.getInstance().getRequestDataTaskManager().createTaskKey(taskModel);
         taskModel.setMapKey(mapKey);
         taskModel.setIsRefresh(isRefresh);
         taskModel.setPage(page);
 //		taskModel.setSignKey(RequestConstant.SIGN_KEY_VALUE);
         if(isRefresh){
             taskModel.addTaskOrder(TaskTypeEnum.getNet);
         }
         else {
             MemoryTaskManager memoryTaskManager =
                     EngineManager.getInstance().getRequestDataTaskManager().memoryTaskManager;
             if(memoryTaskManager.getMemoryData(taskModel.getMapKey()) != null){//如果缓存有数据
                 taskModel.addTaskOrder(TaskTypeEnum.getMemory);
             }
             else {
                 if(page == 1){
                     taskModel.addTaskOrder(TaskTypeEnum.getDb);
                     taskModel.setIsAuToRefresh(true);
 //			        .addTaskOrder(TaskTypeEnum.getNet);
                 }
                 else {
                     if(NetWorkUtil.isNetworkAvailable(EngineManager.getInstance().getContext())){
                         taskModel.addTaskOrder(TaskTypeEnum.getNet);
                     }
                     else {
                         taskModel.addTaskOrder(TaskTypeEnum.getDb);
                     }
                 }
             }
         }
         return taskModel;
    }

    /**
     * 移除内存
     * @param requestModel
     */
    public static void removeMemoryData(RequestModel requestModel){
        String mapKey = Utils.creatMapKey(requestModel);
        EngineManager.getInstance().getRequestDataTaskManager().memoryTaskManager.removeMemory(mapKey);
    }

   /**
    * 获取缓存数据
    * @param requestModel
    * @return
    */
 @SuppressWarnings("unchecked")
 public static <T> T getMemoryData(RequestModel requestModel){
        T returnModel = null;
        try{
            String mapKey = Utils.creatMapKey(requestModel);
            MemoryModel memoryModel= EngineManager.getInstance().getRequestDataTaskManager()
                    .memoryTaskManager.getMemoryData(mapKey);
            if(memoryModel == null){
                return null;
            }
            returnModel = (T)memoryModel.getMemoryModel();
        }
        catch(Exception e){

        }
        return returnModel;
    }
  /**
   * 注册观察者
   * @param requestModel
   * @param observer
   */
    public static void registerObserver(RequestModel requestModel,RequestDataListener observer){
        EngineManager.getInstance().getRequestDataTaskManager().registerObserver(requestModel, observer);
    }
 }

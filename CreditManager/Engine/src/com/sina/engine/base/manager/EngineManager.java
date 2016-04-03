package com.sina.engine.base.manager;

import android.content.Context;

import com.sina.engine.base.config.EngineConfig;
import com.sina.engine.base.db4o.Db4oManager;
import com.sina.engine.base.download.DownLoadManager;
import com.sina.engine.base.request.manager.RequestDataTaskManager;



public class EngineManager {
	public  static boolean DEBUG = true;
	private static EngineManager engine;
	private RequestDataTaskManager requestTaskManager;
	private JsonHandlerManager jsonHandlerManager;
	private DownLoadManager imageDownLoadManager;
	private DownLoadManager apkDownLoadManager;
	private EngineConfig engineConfig;
//	public static String PartnerId = null;
	
	private static Context context;
	
	public EngineManager(){
		requestTaskManager = new RequestDataTaskManager();
		jsonHandlerManager = new JsonHandlerManager(context);
		imageDownLoadManager = new DownLoadManager(context, 10);
		apkDownLoadManager = new DownLoadManager(context, 1);
		Db4oManager.init(context);
	}
	
	
	public static void init(Context con){
		context =  con.getApplicationContext();
		getInstance();
//		initDebug();
//		initPartnerId();
	}
	
	public void initConfig(EngineConfig config){
		if(config == null){
			engineConfig = EngineConfig.getDefaultConfig();
		}
		else {
			engineConfig = config;
		}
	}
	
//	private static void initDebug(){
//		Properties configPro = Utils.getConfigProperties(context);
//		String debug = configPro.getProperty("debug","");
//		DEBUG = true;
//		if(!TextUtils.isEmpty(debug)){
//			if(debug.equals("true")){
//				DEBUG = true;
//			}
//			else if(debug.equals("false")){
//				DEBUG = false;
//			}
//		}
//	}
	
//	private static void initPartnerId() {
//		Properties configPro = Utils.getConfigProperties(context);
//		String pid = configPro.getProperty("partner_id","");
//		if(!TextUtils.isEmpty(pid)){
//			PartnerId = pid;
//		}
//	}
	
	public Context getContext(){
		return context;
	}
	
	public EngineConfig getEngineConfig(){
		return engineConfig;
	}
	
	public RequestDataTaskManager getRequestDataTaskManager(){
		return requestTaskManager;
	}
	
	public JsonHandlerManager getJsonHandlerManager(){
		return jsonHandlerManager;
	}
	
	public DownLoadManager getImageDownLoadManager(){
		return imageDownLoadManager;
	}
	
	public DownLoadManager getApkDownLoadManager(){
		return apkDownLoadManager;
	}

	
	/**
	 * 单例模式
	 * @return
	 */
	public static EngineManager getInstance() {
		if (engine == null) {
			synchronized (EngineManager.class) {
				if (engine == null) {
					engine = new EngineManager();
				}
			}
		}
		return engine;
	}
	
	public void destory(){
		jsonHandlerManager.destoryHandler();
		requestTaskManager.destory();
	}
}

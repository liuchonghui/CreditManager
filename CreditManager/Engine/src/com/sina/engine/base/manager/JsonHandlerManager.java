
package com.sina.engine.base.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

/**
 * json获取解析加入handler主线程
 * @author kangshaozhe
 *
 */
public class JsonHandlerManager implements Callback{
//   private static JsonHandlerManager instance;
   private Handler handler;
   private Context context;
   
   public JsonHandlerManager(Context context){
	   this.context = context;
	   handler = new Handler(context.getApplicationContext().getMainLooper(), this);
   }

/**
 * 发送handler 请求
 * @param msg
 * @param obj
 */
   public void sendHandlerMsg(int msg,Object obj){
 
	   if(handler != null){
	       Message message = new Message();
		
		   message.what = msg;
		   
		   message.obj = obj;
		
		   handler.sendMessage(message);
	   }
    }
   
   public void sendRunnable(Runnable runnable){
	   if(handler == null){
		   handler = new Handler(context.getApplicationContext().getMainLooper(), this);
	   }
	   if(handler != null&&runnable != null){
		   handler.post(runnable);
	   }
   }
   
   /**
    * 销毁handler管理器
    */
   public void destoryHandler(){
	   if(handler != null){
		   handler.removeCallbacks(null);
		   handler = null;
	   }
   }

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		int message = msg.what;
		 switch(message){
		     
		 }
		return false;
	}
}

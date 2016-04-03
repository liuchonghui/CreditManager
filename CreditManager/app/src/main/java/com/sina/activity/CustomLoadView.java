package com.sina.activity;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sina.sinagame.credit.R;


public class CustomLoadView {
	private LayoutInflater inflater;
	private Context context;
	public static final int LOAD_ING = 0;
	public static final int LOAD_FAIL = 1;
	public static final int LOAD_SUCESS = 2;
	public static final int LOAD_NODATA = 3;
	public static final int LOAD_KONG = 4;
	private View mainView;
	private View failLayout;
	private View loadingView;
	private ViewGroup loadView;
	private View nodataView;
	private ImageView loadingImageView;
	
    /**handler变量**/
    
    private static final int HANDLER_LOAD_FAIL = 123455;
    private static final int HANDLER_LOAD_SUCESS = 123456;
    private static final int HANDLER_LOAD_LOADING = 123457;
    private static final int HANDLER_LOAD_NODATA = 123458;
    private static final int HANDLER_LOAD_KONG  = 123459;
    private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case HANDLER_LOAD_FAIL:
					mainView.setVisibility(View.VISIBLE);
					failLayout.setVisibility(View.VISIBLE);
					loadingView.setVisibility(View.GONE);
					nodataView.setVisibility(View.GONE);
					stopLoadingAnimation();
				break;
				case HANDLER_LOAD_SUCESS:
					mainView.setVisibility(View.GONE);
					stopLoadingAnimation();
				break;
				case HANDLER_LOAD_LOADING:
					mainView.setVisibility(View.VISIBLE);
					failLayout.setVisibility(View.GONE);
					loadingView.setVisibility(View.VISIBLE);
					startLoadingAnimation();
					nodataView.setVisibility(View.GONE);
				break;
				case HANDLER_LOAD_NODATA:
					mainView.setVisibility(View.VISIBLE);
					failLayout.setVisibility(View.GONE);
					loadingView.setVisibility(View.GONE);
					nodataView.setVisibility(View.VISIBLE);
					stopLoadingAnimation();
				break;
				case HANDLER_LOAD_KONG:
					mainView.setVisibility(View.GONE);
					stopLoadingAnimation();
				break;
			}
		}
	};
	public CustomLoadView(Context context){
		this.context = context;
		inflater = LayoutInflater.from(this.context);
	}
	
	public ViewGroup creatView(){
		loadView = (ViewGroup) inflater.inflate(R.layout.load_layout, null);
		loadView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		failLayout = loadView.findViewById(R.id.custom_load_fail_button);
		mainView = loadView.findViewById(R.id.custom_load_main_layout);
		loadingView = loadView.findViewById(R.id.custom_load_progress);
		nodataView = loadView.findViewById(R.id.custom_load_nodata);
		loadingImageView = (ImageView)loadView.findViewById(R.id.custom_load_image_view);
		return loadView;
	}
	
	public View creatView(ViewGroup vg,OnClickListener l){
		loadView = creatView();
		failLayout.setOnClickListener(l);
		ViewGroup.LayoutParams loadParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT);
		   
		vg.addView(loadView, loadParams);
		return loadView;
	}
	
	public void replaceNodataIcon(int iconResouseId){
		if(loadView == null||iconResouseId<=0){
			return;
		}
		ImageView icon = (ImageView)loadView.findViewById(R.id.custom_load_nodata_icon);
		icon.setImageResource(iconResouseId);
	}
	
	public void replaceNodataContet(int textResouseId){
		if(loadView == null||textResouseId<=0){
			return;
		}
		TextView content = (TextView)loadView.findViewById(R.id.custom_load_nodata_content);
		content.setText(textResouseId);
	}
	
	public void replaceNodataSubContet(int textResouseId){
		if(loadView == null||textResouseId<=0){
			return;
		}
		TextView content = (TextView)loadView.findViewById(R.id.custom_load_nodata_subcontent);
		content.setText(textResouseId);
		content.setVisibility(View.VISIBLE);
	}
	
	public void replaceNodataView(View replaceView) {
		if(loadView == null || nodataView == null){
			return;
		}
		int count = loadView.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = loadView.getChildAt(i);
			if (child != null && child == nodataView) {
				loadView.removeViewAt(i);
				nodataView = replaceView;
				loadView.addView(replaceView, i);
			}
		}
	}
	
	
	public void flushLoadView(int state){
		switch(state){
		  case LOAD_ING:
			  sendHandlerMsg(HANDLER_LOAD_LOADING);
		  break;
		  case LOAD_FAIL:
			  sendHandlerMsg(HANDLER_LOAD_FAIL);
		  break;
		  case LOAD_SUCESS:
			  sendHandlerMsg(HANDLER_LOAD_SUCESS);
		  break;
		  case LOAD_NODATA:
			  sendHandlerMsg(HANDLER_LOAD_NODATA);
		  break;
		  case LOAD_KONG:
			  sendHandlerMsg(HANDLER_LOAD_KONG);
		  break;
		}
	}
	
	/**
	  * 发送handler 请求
	  * @param msg
	  */
	 public void sendHandlerMsg(int msg){
 	 
 	    Message message = new Message();
		
		message.what = msg;
		
		handler.sendMessage(message);
	    	 
	 }
	 
	 public void changeMainBack(int colorResouse){
		 if(mainView != null){
			 mainView.setBackgroundResource(colorResouse);
		 }
	 }
	 
	 private void startLoadingAnimation(){
		 if(loadingImageView == null){
			 return;
		 }
		 try{
		    AnimationDrawable animationDrawable = (AnimationDrawable)loadingImageView.getDrawable();
		    if(animationDrawable != null){
		    	animationDrawable.start();
		    }
		 }
		 catch(Exception e){
			 
		 }
	 }
	 
	 private void stopLoadingAnimation(){
		 if(loadingImageView == null){
			 return;
		 }
		 try{
		    AnimationDrawable animationDrawable = (AnimationDrawable)loadingImageView.getDrawable(); 
		    if(animationDrawable != null){
		    	animationDrawable.stop();
		    }
		 }
		 catch(Exception e){
			 
		 }
	 }
}

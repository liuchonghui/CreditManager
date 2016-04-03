package com.sina.activity;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.request.ViewUtils;
import com.sina.sinagame.credit.R;


/**
 * 自定义toast
 * @author kangshaozhe
 *
 */
public class CustomToastDialog {
    private Context context;
    private View view;
    private LayoutInflater inflater;
    private Toast toast;
    
	public CustomToastDialog(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(this.context);
		// TODO Auto-generated constructor stub
		 view = inflater.inflate(R.layout.toast_dialog, null);
		 toast = Toast.makeText(context,"", Toast.LENGTH_SHORT);
		 toast.setGravity(Gravity.BOTTOM, 0, ViewUtils.dp2px(context, 65));;
	     toast.setView(view);
		 
	}
	
	 
	 public CustomToastDialog setWaitTitle(int titleId,int iconId){
		 if(view == null){
			 return this;
		 }
		 if(titleId > 0){
			 TextView titleView = (TextView)view.findViewById(R.id.wait_dialog_toast_title);
		     titleView.setText(titleId);
		 }
		 ImageView iconView = (ImageView)view.findViewById(R.id.wait_dialog_toast_icon);
	     if(iconId > 0){
		     iconView = (ImageView)view.findViewById(R.id.wait_dialog_toast_icon);
		     iconView.setImageResource(iconId);
		     iconView.setVisibility(View.VISIBLE);
	     }else{
	    	 iconView.setVisibility(View.GONE);
	     }
	     return this;
	 }
	 
	public CustomToastDialog setWaitTitle(int titleId) {
		return setWaitTitle(titleId, -1);
	}
	
	public CustomToastDialog setWaitTitle(String title){
		 if(view == null){
			 return this;
		 }
		 if(title != null && title.length() > 0){
			 TextView titleView = (TextView)view.findViewById(R.id.wait_dialog_toast_title);
		     titleView.setText(title);
		 }
		 ImageView iconView = (ImageView)view.findViewById(R.id.wait_dialog_toast_icon);
		 iconView.setVisibility(View.GONE);
	     return this;
	 }
	 
	 /**
	  * 显示
	  */
	 public void showMe(){
		if (toast != null) {
			toast.show();
		}
	 }
}

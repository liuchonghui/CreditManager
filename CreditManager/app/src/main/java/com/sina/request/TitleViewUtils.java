package com.sina.request;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sina.sinagame.credit.R;

public class TitleViewUtils {
	public static void addRightTitleLayout(Context context,View titleLayout,int rightLayoutResouseId){
		if(titleLayout == null||rightLayoutResouseId<=0){
			return ;
		}
		View rightView = LayoutInflater.from(context).inflate(rightLayoutResouseId, null);
		if(rightView == null){
			return;
		}
		FrameLayout rightLayout = (FrameLayout)titleLayout.findViewById(R.id.title_right_layout);
		rightLayout.addView(rightView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, 
				FrameLayout.LayoutParams.WRAP_CONTENT));
	}
	
	public static void replaceCenterLayout(Context context,View titleLayout,int centerLayoutResouseId){
		if(titleLayout == null||centerLayoutResouseId<=0){
			return ;
		}
		View centerView = LayoutInflater.from(context).inflate(centerLayoutResouseId, null);
		if(centerView == null){
			return;
		}
		TextView titleView = (TextView)titleLayout.findViewById(R.id.title_content);
		FrameLayout cetnterLayout = (FrameLayout)titleLayout.findViewById(R.id.title_center_layout);
		cetnterLayout.removeView(titleView);
		cetnterLayout.addView(centerView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, 
				FrameLayout.LayoutParams.WRAP_CONTENT));
	}
	
	public static void replaceLeftLayout(Context context,View titleLayout,int centerLayoutResouseId){
		if(titleLayout == null||centerLayoutResouseId<=0){
			return ;
		}
		View leftView = LayoutInflater.from(context).inflate(centerLayoutResouseId, null);
		if(leftView == null){
			return;
		}
		ImageView returnView = (ImageView)titleLayout.findViewById(R.id.title_turn_return);
		FrameLayout leftLayout = (FrameLayout)titleLayout.findViewById(R.id.title_left_layout);
		leftLayout.removeView(returnView);
		leftLayout.addView(leftView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, 
				FrameLayout.LayoutParams.WRAP_CONTENT));
	}
	
	public static void setTitle(View titleLayout,int titleResouseId){
		if(titleLayout == null||titleResouseId<=0){
			return ;
		}
		TextView titleView = (TextView)titleLayout.findViewById(R.id.title_content);
		if(titleView == null){
			return;
		}
		
		titleView.setText(titleResouseId);
	}
	
	public static void setTitle(View titleLayout, String title){
		if(titleLayout==null||TextUtils.isEmpty(title)){
			return ;
		}
		TextView titleView = (TextView)titleLayout.findViewById(R.id.title_content);
		if(titleView == null){
			return;
		}
		titleView.setText(title);
	}
	
	public static void setTitleColor(View titleLayout, int colorid){
		if(titleLayout == null){
			return ;
		}
		TextView titleView = (TextView)titleLayout.findViewById(R.id.title_content);
		if(titleView == null){
			return;
		}
		titleView.setTextColor(colorid);
	}
	
	public static void setBackGround(View titleLayout, int colorResouseId){
		if(titleLayout == null||colorResouseId<=0){
			return ;
		}
		View titleView = titleLayout.findViewById(R.id.title_layout);
		titleView.setBackgroundResource(colorResouseId);
	}
	
	public static void setReturnSrc(View titleLayout, int resouseId){
		if(titleLayout == null||resouseId<=0){
			return ;
		}
		ImageView returnImageView = (ImageView) titleLayout.findViewById(R.id.title_turn_return);
		returnImageView.setImageResource(resouseId);
	}
	
	public static void setReturnListener(View titleLayout, OnClickListener listener){
		if(titleLayout == null){
			return ;
		}
		ImageView returnImageView = (ImageView) titleLayout.findViewById(R.id.title_turn_return);
		returnImageView.setOnClickListener(listener);
	}
	
	public static void setHideReturn(View titleLayout){
		ImageView returnView = (ImageView)titleLayout.findViewById(R.id.title_turn_return);
		returnView.setVisibility(View.INVISIBLE);
	}
	
	public static void setHideBorder(View titleLayout){
		View returnView = titleLayout.findViewById(R.id.news_detail_title_line);
		returnView.setVisibility(View.GONE);
	}
}

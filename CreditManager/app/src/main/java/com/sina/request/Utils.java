package com.sina.request;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore.Images;
import android.text.ClipboardManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.webkit.WebView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
	/**
	 * 获取现在时间
	 * @return
	 */
	public static String getDate() {
	    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.setTimeZone(Calendar.getInstance().getTimeZone());   
        String date = formatter.format(new Date()); 
        return date;
	}
	
	/**
	 * 获取现在时间
	 * @return
	 */
	public static String getAllDate() {
	    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(Calendar.getInstance().getTimeZone());   
        String date = formatter.format(new Date()); 
        return date;
	}
	
	/**
	 * 时间戳转换成时间
	 * @param time
	 * @return
	 */
	public static String allTimeTransDate(Long time){
		long curr_time = System.currentTimeMillis();
		Date date = new Date(time);
		Date curr_date = new Date(curr_time);
		
		int year = date.getYear();
		int curr_year = curr_date.getYear();
		DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String sd = sdf.format(time);  
		return sd;
	}
	
	/**
	 * 时间戳转换成时间
	 * @param time
	 * @return
	 */
	public static String timeTransDate(Long time){
		long curr_time = System.currentTimeMillis();
		Date date = new Date(time);
		Date curr_date = new Date(curr_time);
		
		int year = date.getYear();
		int curr_year = curr_date.getYear();
		DateFormat sdf=new SimpleDateFormat("MM-dd");
		if(year < curr_year){
			sdf=new SimpleDateFormat("yyyy-MM-dd");  
		}
		String sd = sdf.format(time);  
		return sd;
	}
	
	/**
	 * 日期转换成时间戳
	 * @param date
	 * @return
	 */
	public static long DateTransTime(String date){
		DateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		 
		Date time = null;
		try {
			time = format.parse(date);
			return time.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	/**
	 * 新闻列表日期转换方法
	 * @return
	 */
	public static String getNewsDate(long date) {
		String turnResult = "";
		long subTime = System.currentTimeMillis() - date;
		if(subTime<0){
			turnResult = timeTransDate(date);
		}
		else {
			if(subTime < 1000*60){//一分钟内
				turnResult=subTime/(1000)+"秒前";
			}
			else if(subTime < 1000*60*60){//一小时内
				turnResult=subTime/(1000*60)+"分钟前";
			}
			else if(subTime <1000*60*60*24){//一天内
				turnResult=subTime/(1000*60*60)+"小时前";
			}
			else{
				turnResult = timeTransDate(date);
			}
		}
		return turnResult;
	}
	
	public static String getNewsDate(String date){
		return getNewsDate(DateTransTime(date));
	}
	
	/**
	 *  转换成视频的时长
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String timeTransVideoTimes(Long time, boolean showZeroHour){
		DateFormat sdf = null;
		long hourTime = 1000*60*60;
		if(time >= hourTime){
			sdf=new SimpleDateFormat("hh:mm:ss");  
		}
		else{
			if(!showZeroHour){
			    sdf=new SimpleDateFormat("mm:ss");  
			}
			else{
				sdf=new SimpleDateFormat("HH:mm:ss");  
			}
		}
		TimeZone tz = TimeZone.getTimeZone("Asia/Beijing");  
		sdf.setTimeZone(tz); 
		String sd = sdf.format(time);  
		
		return sd;
	}
	
	public static String timeParseVideoTimes(Long time) {
		boolean hasHour = time / 3600000L  > 0;
		String hour = String.valueOf(time / 3600000);
		hour = hour.length() == 1 ? "0" + hour : hour;
		time = time % 3600000;
		String minute = String.valueOf(time / 60000);
		minute = minute.length() == 1 ? "0" + minute : minute;
		time = time % 60000;
		String second = String.valueOf(time / 1000);
		second = second.length() == 1 ? "0" + second : second;
		if (hasHour) {
			return hour + ":" + minute + ":" + second;
		}
		return minute + ":" + second;
	}
	
	public static String getIp(Context con) {   
	      WifiManager wifiManager = (WifiManager) con.getSystemService(Context.WIFI_SERVICE);   
	      WifiInfo wifiInfo = wifiManager.getConnectionInfo();   
	      int ipAddress = wifiInfo.getIpAddress();   
	         
	      // 格式化IP address，例如：格式化前：1828825280，格式化后：192.168.1.109   
	      String ip = String.format("%d.%d.%d.%d",   
	              (ipAddress & 0xff),   
	              (ipAddress >> 8 & 0xff),   
	              (ipAddress >> 16 & 0xff),   
	              (ipAddress >> 24 & 0xff));   
	      return ip;   
	         
	  }
	/**
	 * 跳转到浏览器
	 * @param url
	 */
	public static void goToBrowser(Context con,String url)
	{
		Intent in = new Intent();
		in.setAction("android.intent.action.VIEW");
		Uri url_content = Uri.parse(url);
		in.setData(url_content);
//		in.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
		con.startActivity(in);

	}
	/**
	 * px 转 dp
	 * @param context
	 * @param pxValue
	 * @return
	 */
	 public static int px2dip(Context context, float pxValue){ 
         final float scale = context.getResources().getDisplayMetrics().density; 
         return (int)(pxValue / scale + 0.5f); 
     } 
	 /**
	  * px转sp
	  * @param context
	  * @param pxValue
	  * @return
	  */
	 public static int px2sp(Context context, float pxValue) {  
	        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
	        return (int) (pxValue / fontScale + 0.5f);  
	 }  
	 
	/** 
     * 将dip或dp值转换为px值，保证尺寸大小不变 
     *  
     * @param dipValue 
     *            （DisplayMetrics类中属性density）
     * @return 
     */  
    public static int dp2px(Context context, float dipValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dipValue * scale + 0.5f);  
    }  
	 
    
    /**
     * 将sp值转换为px值，保证文字大小不变
     * 
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int sp2px(Context context, float spValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    } 
	 /**
	  * 获取微博的appkey
	  * @return
	  */
//	 public static String getAppKey(Context con){
//		 String app_key = null;
//		 try {
//			InputStream open = con.getResources().getAssets().open("MyShareSDK.xml");
//			XmlPullParser mXmlPull = Xml.newPullParser();
//			mXmlPull.setInput(open, "UTF-8");
//			int eventCode = mXmlPull.getEventType();
//
//			while (eventCode != XmlPullParser.END_DOCUMENT) {
//
//				switch (eventCode) {
//				case XmlPullParser.START_DOCUMENT: // 文档开始事件
//					break;
//
//				case XmlPullParser.START_TAG:// 元素开始.
//					String name = mXmlPull.getName();
//					if (name.equalsIgnoreCase("SinaWeibo")) {
//						app_key = mXmlPull.getAttributeValue(null, "AppKey");
//					}
//					break;
//
//				case XmlPullParser.END_TAG: // 元素结束,
//
//					break;
//				default:
//					break;
//				}
//				eventCode = mXmlPull.next();// 进入到一下一个元素.
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 return app_key;
//	 }
	 /**
	  * 获取当前版本号
	  * @param context
	  * @return
	  */
	 public static int getVersionCode(Context context)//获取版本号(内部识别号)  
	 {  
	     try {  
	         PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);  
	         return pi.versionCode;  
	     } catch (NameNotFoundException e) {  
	         // TODO Auto-generated catch block  
	         e.printStackTrace();  
	         return 0;  
	     }  
	 }
	 
	 /**
	  * 获取当前版本名称
	  * @param context
	  * @return
	  */
	 public static String getVersion(Context context)//获取版本号  
	    {  
	        try {  
	            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);  
	            return pi.versionName;  
	        } catch (NameNotFoundException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	            return "";  
	        }  
	    }  
	/**
	 * 安装某个apk
	 * @param con
	 * @param filePath
	 */
	public static void installApkForPath(Context con,String filePath){
		if(!TextUtils.isEmpty(filePath)&&con!=null){
			Intent intent1 = new Intent(Intent.ACTION_VIEW);
			File file = new File(filePath);
			if(file != null){
				intent1.setDataAndType(Uri.fromFile(file),
	                    "application/vnd.android.package-archive");
				intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				con.startActivity(intent1);
			}
		}
	}
	/**
	 * 获得网络状态信息
	 * @param context
	 * @return
	 */
	 public static NetworkInfo getActiveNetwork(Context context){
	        if (context == null)
	            return null;
	        ConnectivityManager mConnMgr = (ConnectivityManager) context
	                .getSystemService(Context.CONNECTIVITY_SERVICE);
	        if (mConnMgr == null)
	            return null;
	        NetworkInfo aActiveInfo = mConnMgr.getActiveNetworkInfo(); // 获取活动网络连接信息
	        return aActiveInfo;
	    }
	 /**
	  * 当前API等级是否比指定api等级大
	  * @return
	  */
	 public static boolean isMoreSdkLv(int lv){
		 int version = Integer.valueOf(Build.VERSION.SDK);
		 if(version > lv){
			 return true;
		 }
		 return false;
	 }

	 public static Bitmap scaleBitmapImg(Bitmap img,int scale_width,int scale_height){
	    Matrix matrix = new Matrix();
	    float scaleWidth = ((float) scale_width / img.getWidth());
	    float scaleHeight = ((float) scale_height / img.getHeight());
	    float scale = scaleWidth>=scaleHeight?scaleWidth:scaleHeight;
	    matrix.postScale(scale, scale);
	    Bitmap newbmp = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(),
				matrix, true);
	    return newbmp;
	 }


	/**
	 * 获得application数据标签的值
	 * @param context
	 * @param name
	 * @return
	 */
	public static String getAppLicationMetaData(Context context,String name){
		ApplicationInfo appInfo = null;
		String metaDataStr = null;
		Context appContext = context.getApplicationContext();
		try {
			appInfo = appContext.getPackageManager().getApplicationInfo(appContext.getPackageName(),PackageManager.GET_META_DATA);
			if(appInfo != null&&appInfo.metaData!=null){
			   metaDataStr = String.valueOf(appInfo.metaData.get(name));
			}

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return metaDataStr;
	}
	/**
	 * 屏幕像素宽高
	 * @param context
	 * @return
	 */
	public static int[] getScreenWH(Context context){
		int[] wh = new int[2];
		DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
		wh[0] = metrics.widthPixels;
		wh[1] = metrics.heightPixels;
		return wh;
	}

	/**
     * 获得SD卡总大小
     *
     * @return
     */
	public static long getSDTotalSize(Context con) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
//        return Formatter.formatFileSize(con, blockSize * totalBlocks);
        return blockSize * totalBlocks;
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static long getSDAvailableSize(Context con) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
//        return Formatter.formatFileSize(con, blockSize * availableBlocks);
        return blockSize * availableBlocks;
    }

    /**
     * 获得机身内存总大小
     *
     * @return
     */
    public static String getRomTotalSize(Context con) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(con, blockSize * totalBlocks);
    }

    /**
     * 获得机身可用内存
     *
     * @return
     */
    public static String getRomAvailableSize(Context con) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(con, blockSize * availableBlocks);
    }

    /**
     * 赋值到剪贴板
     * @param context
     * @param clipText
     */
    public static void clipText(Context context,String clipText){
    	// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(clipText);
    }

   /**
    * 改变textview中局部文字颜色
    * @param text
    * @param start
    * @param end
    * @param color
    * @return
    */
    public static SpannableStringBuilder changeTextPartColor(String text,int start,int end,int color){
    	if(text == null){
    		return new SpannableStringBuilder("");
    	}
    	SpannableStringBuilder style=new SpannableStringBuilder(text);
//    	//设置指定位置的背景颜色
//    	style.setSpan(new BackgroundColorSpan(Color.RED),0,4,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    	//设置指定位置的文字颜色
    	style.setSpan(new ForegroundColorSpan(color),start,end,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

    	return style;

    }

    /**
     * drawable转bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                               drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }

   /**
    * 图片模糊
    * @param sentBitmap
    * @param radius
    * @return
    */
    public static Bitmap fastblur(Bitmap sentBitmap, int radius) {

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }


	/**
	 * 图片模糊
	 * @param bitmap
	 * @param radius
	 */
	public static void blur(Bitmap bitmap, int radius) {


		if (radius < 1) {
			return;
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}

		bitmap.setPixels(pix, 0, w, 0, 0, w, h);

	}





    /**
     * webview执行js方法
     * @param webview
     * @param content
     */
    @SuppressLint("NewApi")
	public static void webViewJs(WebView webview,String content){
    	try{
	    	if(Build.VERSION.SDK_INT < 19){//4.4版本之前
	    		content = "javascript:"+content;
	    		webview.loadUrl(content);
	    	}
	    	else {
	    		webview.evaluateJavascript(content, null);
	    	}
    	}
    	catch(Exception e){

    	}
    }
    /**
     * 包名
	  * @param context
	  * @return
	  */
	public static String getPackageName(Context context)
    {
       try {
           PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
           return pi.packageName;
       } catch (Exception e) {
           e.printStackTrace();
           return "";
       }
    }

    public static String getWebViewBaseUrl(Context con){
    	String baseUrl = "";
    	String packageName = getPackageName(con);
    	if (packageName == null || packageName.length() == 0) {
    		packageName = "com.sina.sinagame";
    	}
    	baseUrl = "sinagame://"+packageName+"/";
    	return baseUrl;
    }


//    public static Properties  getConfigProperties(Context con){
//    	Properties properties = new Properties();
//    	try {
//    		InputStream is = con.getAssets().open("config.properties");
//	    	properties.load(is);
//	    	return properties;
//    	} catch (Exception e) {
//    	   e.printStackTrace();
//    	}
//    	return null;
//    }

    /**
	 * 获取刷新时间
	 * @param date
	 * @return
	 */
	public static String getRrefreshTime(long date) {
		String turnResult = "";
		long subTime = System.currentTimeMillis() - date;
		if(date<=0){
			turnResult="今天:"+getHSDate(subTime);
		}
		else {
			if(subTime <= 0){
				turnResult = timeTransDate(date);
			}
			else if(subTime <  10*1000 ){
				turnResult = "刚刚";
			}
			else if(subTime < 1000*60){//一分钟内
				turnResult=subTime/(1000)+"秒前";
			}
			else if(subTime < 1000*60*60){//一小时内
				turnResult=subTime/(1000*60)+"分钟前";
			}
			else if(subTime <1000*60*60*24){//一天内
				turnResult=subTime/(1000*60*60)+"小时前";
			}
			else{
				turnResult = timeTransDate(subTime);
			}
		}
		return turnResult;
	}
	public static String getHSDate(long time){
		DateFormat sdf=new SimpleDateFormat("HH:mm");
		String sd = sdf.format(time);
		return sd;
	}
	public static void saveImgToGallery(Context context,String imagePath,String imageName){

		ContentValues values = new ContentValues();

		values.put(Images.Media.TITLE, imageName);
		values.put(Images.Media.DESCRIPTION, "sinagame_image");
		values.put(Images.Media.DISPLAY_NAME, imageName);
//		values.put(Images.Media.DATE_TAKEN, dateTaken);
		values.put(Images.Media.MIME_TYPE, "image/png");
//		values.put(Images.Media.ORIENTATION, degree[0]);
		values.put(Images.Media.DATA, imagePath);
//		values.put(Images.Media.SIZE, size);

		ContentResolver localContentResolver = context.getContentResolver();

		Uri localUri = Images.Media.EXTERNAL_CONTENT_URI;

		localContentResolver.insert(localUri, values);
	}
	
	public static boolean fileExists(String filePath) {  
        File file = new File(filePath);  
        return file.exists();  
    }  
	
	public static boolean fileCopy(String newsDir,String oldFilePath,String newFilePath) throws IOException {  
		if(TextUtils.isEmpty(newsDir)||TextUtils.isEmpty(oldFilePath)||TextUtils.isEmpty(newFilePath)){
			return false;
		}
        //如果原文件不存在  
        if(!fileExists(oldFilePath)){  
            return false;  
        }  
        //如果要复制的文件存在
        if(fileExists(newFilePath)){  
            return false;  
        } 
        File newsFile = new File(newsDir);  
        if(!newsFile.exists()){
        	newsFile.mkdirs();
		 }
        //获得原文件流  
        FileInputStream inputStream = new FileInputStream(oldFilePath);  
        byte[] data = new byte[1024];  
        //输出流  
        FileOutputStream outputStream =new FileOutputStream(newFilePath);  
        //开始处理流  
        while (inputStream.read(data) != -1) {  
            outputStream.write(data);  
        }  
        inputStream.close();  
        outputStream.close();   
        return true;
    }

}

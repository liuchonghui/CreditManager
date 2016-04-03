package com.sina.engine.base.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.sina.engine.base.utils.Utils;
/**
 * 下载工具类
 * @author kangshaozhe
 *
 */
public class DownLoadUtils {
	/**
	 * 获得md5加密
	 * @param content
	 * @return
	 */
	public static String getMD5(String content) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(content.getBytes());
			return getHashString(digest);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	private static String getHashString(MessageDigest digest) {
		StringBuilder builder = new StringBuilder();
		for (byte b : digest.digest()) {
			builder.append(Integer.toHexString((b >> 4) & 0xf));
			builder.append(Integer.toHexString(b & 0xf));
		}
		return builder.toString();
	}
	
	
	/**
	 * 判断本地是否有视频
	 * @param url
	 * @return
	 */
	public static String getlocalPath(Context context,String url) {
		String localPath = null;
		String childDir = DownLoadConstant.DOWNLOAD_VIDEO_DIR;
		String suffixName = DownLoadConstant.DOWNLOAD_VIDEO_SUFFIXNAME;
		String fileName = DownLoadUtils.getMD5(url);
		localPath = DownLoadFile.isFileExist(
				context.getApplicationContext(), childDir, fileName,
				suffixName);
		return localPath;

	}
	
	/**
	 * 安装某个apk
	 * 
	 * @param con
	 * @param filePath
	 */
	@SuppressLint("NewApi")
	public static void installApkForPath(final Context con, final String filePath) {
		Log.d("DLF", "installApkForPath=" + filePath);
		if (!TextUtils.isEmpty(filePath) && con != null) {
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					File file = new File(filePath);
					if (file.exists()) {
						boolean available = false;
						Uri uriFromFile =  Uri.fromFile(file);
						String filesDir = "/data/data/" + con.getPackageName();
						Log.d("DLF", "FilesDir=" + filesDir);
						if (filePath.contains(filesDir)) {
							Log.d("DLF", "apk在data/data区");
							File externalFile = null;
							try {
								String filename = file.getName();
								File externalDir = Utils.getExternalDir(con);
								if (externalDir != null) {
									Log.d("DLF", "--start file channel copy");
									externalFile = new File(externalDir,
											filename);
									externalFile.createNewFile();
									fileChannelCopy(file, externalFile);
									Log.d("DLF", "--finish file channel copy");
								} else {
									Log.d("DLF", "fail to get ExternalDir");
									File sdcard = Environment
											.getExternalStorageDirectory();
									externalFile = new File(sdcard, filename);
									Log.d("DLF", "--start file channel copy");
									if (externalFile != null) {
										externalFile.createNewFile();
										fileChannelCopy(file, externalFile);
										Log.d("DLF", "--finish file channel copy");
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
								externalFile = null;
							}
							if (externalFile != null && externalFile.exists()) {
								uriFromFile = Uri.fromFile(externalFile);
								available = true;
							}
						} else {
							available = true;
						}
						Log.d("DLF", "uriFromFile=" + uriFromFile.toString());
						if (available) {
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(uriFromFile,
									"application/vnd.android.package-archive");
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							con.startActivity(intent);
						}
					}
				}
			});
		}
	}
	
	public static void fileChannelCopy(File from, File to) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(from);
			fo = new FileOutputStream(to);
			in = fi.getChannel();
			out = fo.getChannel();
			in.transferTo(0, in.size(), out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

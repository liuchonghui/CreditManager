package com.sina.request;

import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.android.overlay.RunningEnvironment;
import com.sina.engine.base.enums.HttpTypeEnum;
import com.sina.engine.base.enums.ReturnDataClassTypeEnum;
import com.sina.engine.base.request.listener.RequestDataListener;
import com.sina.engine.base.request.model.TaskModel;
import com.sina.engine.base.request.options.RequestOptions;
import com.sina.sinagame.credit.OnCookieSetCompleteListener;
import com.sina.sinagame.credit.OnH5GamesReceivedListener;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("serial")
public class UserCookieManager implements Serializable {

	protected static UserCookieManager instance;
	protected AccountInfo info;

	static {
		instance = new UserCookieManager();
		RunningEnvironment.getInstance().addManager(instance);
	}

	public static UserCookieManager getInstance() {
		return instance;
	}

	protected UserCookieManager() {
		info = new AccountInfo();
	}

	protected void requestTokenAndSetCookie() {
		removeAllCookie();
		requestData();
	}

	/**
	 * 自定义token字符串
	 * 
	 * @param tokenStr
	 * @param expireIn
	 * @return
	 */
	protected StringBuffer getCookieStr(String tokenStr, Date expireIn) {
		StringBuffer sb = new StringBuffer();
		StringBuffer didBuffer = new StringBuffer();
		didBuffer.append("imei=")
				.append(DeviceUtils.getIMEI(
						RunningEnvironment.getInstance().getApplicationContext()))
				.append("&mac=").append(DeviceUtils.getMac(
				RunningEnvironment.getInstance().getApplicationContext()))
				.append("&os=").append("android")
				.append(DeviceUtils.getVersionRelease()).append("&model=")
				.append(DeviceUtils.getDeviceModel());
		StringBuffer sinfoBuffer = new StringBuffer();
		sinfoBuffer.append("from=").append(URLEncoder.encode("game"))
				.append("&").append("token=")
				.append(URLEncoder.encode(tokenStr)).append("&").append("did=")
				.append(URLEncoder.encode(didBuffer.toString()));
		sb.append("").append("sinfo=")
				.append(URLEncoder.encode(sinfoBuffer.toString())).append("; ")
				.append("Expires=").append(expireIn.toGMTString()).append("; ")
				.append("Domain=").append(COOKIE_DOMAIN)
				.append("; ").append("Path=/; ").append("Secure; ")
				.append("HttpOnly");

		return sb;
	}

//	/**
//	 * 同步webview和httpclient cookie共享
//	 *
//	 * @param context
//	 * @param url
//	 * @param stringBuffer
//	 */
//	public static void synCookies(Activity context, String url,
//			StringBuffer stringBuffer) {
//		if (context == null || url == null || stringBuffer == null)
//			return;
//		CookieSyncManager.createInstance(context);
//		CookieManager cookieManager = CookieManager.getInstance();
//		cookieManager.setAcceptCookie(true);
//		cookieManager.removeSessionCookie();// 移除
//		cookieManager.setCookie(url, stringBuffer.toString());// cookies是在HttpClient中获得的cookie
//		CookieSyncManager.getInstance().sync();
//	}
	/**
	 * 同步webview和httpclient cookie共享
	 *
	 * @param url
	 * @param stringBuffer
	 */
	protected void synCookies(String url, StringBuffer stringBuffer) {
		if (url == null || stringBuffer == null) {
			return;
		}
		try {
			CookieSyncManager.createInstance(
					RunningEnvironment.getInstance().getApplicationContext());
			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.setAcceptCookie(true);
			// cookieManager.removeSessionCookie();// 移除
			if (Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 == Build.VERSION.SDK_INT) {
				if (url.startsWith(".")) {
					url = url.replaceFirst(".", "*.*");
				}
			}
			cookieManager.setCookie(url, stringBuffer.toString());// cookies是在HttpClient中获得的cookie
			CookieSyncManager.getInstance().sync();
		} catch (Exception e) {
		}
	}

	protected void syncCookiesForList(List<Map<String, CookieModel>> list) {
		for (Map<String, CookieModel> mapData : list) {
			Set set = mapData.keySet();
//			AccountItem item = AccountManager.getInstance()
//					.getCurrentAccountItem();
			for (Iterator iter = set.iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				CookieModel cookieModel = (CookieModel) mapData.get(key);
				StringBuffer sb = new StringBuffer();
				sb.append(cookieModel.getValue());
				if (TextUtils.isEmpty(cookieModel.getExpires())) {
					sb.append(";Expires=").append(
							new Date(info.getExpiresin()).toGMTString());
				}
				else {
					sb.append(";Expires=").append(cookieModel.getExpires());
				}
				if (cookieModel.getSecure()) {
					sb.append(";").append("Secure");
				}
				if (cookieModel.getHttponly()) {
					sb.append(";").append("HttpOnly");
				}
				synCookies(key, sb);
			}
		}
	}

	protected void syncCookiesForList(String domainStr,
									  List<CookieModel> cookieList) {
		for (CookieModel cookieModel : cookieList) {
			StringBuffer sb = new StringBuffer();
			sb.append(cookieModel.getName());
			sb.append("=");
			sb.append(cookieModel.getValue());
			sb.append(";");
			synCookies(domainStr, sb);
		}
	}

	public static String COOKIE_DOMAIN = ".weibo.cn";
	public static String RSA_DOAMIN = "http://jifen.sina.com.cn/api/";
	public static String RSA_PHPNAME = "app_token_encrypt";

	/**
	 * 请求列表数据
	 */
	protected void requestData() {

		RequestOptions requestOptions = new RequestOptions()
				.setHttpRequestType(HttpTypeEnum.post).setIsMainThread(true)
				.setIsSaveMemory(false).setIsSaveDb(false)
				.setReturnDataClassTypeEnum(ReturnDataClassTypeEnum.generic)
				.setReturnModelClass(RSAEncryptModel.class);

		RSAEncryptRequestModel requestModel = new RSAEncryptRequestModel(
				RSA_DOAMIN, RSA_PHPNAME);
		if (info != null && info.getAccessToken() != null) {
			requestModel.setToken(info.getAccessToken()); // token取微博账号
		}
		requestModel.setGuid(info.getGuid());
		requestModel.setGtoken(info.getGtoken());
		requestModel.setDeadline(info.getDeadline());

		ReuqestDataProcess.requestData(true, Integer.MAX_VALUE, requestModel, requestOptions,
				new RequestDataListener() {

					@Override
					public void resultCallBack(TaskModel taskModel) {
						if (taskModel.getReturnModel() != null) {
							RSAEncryptModel model = (RSAEncryptModel) taskModel
									.getReturnModel();
							if (model != null && model.getResult()
									&& model.getData() != null) {
								Date exprisin = new Date(info.getExpiresin());
								StringBuffer sb = getCookieStr(model.getData(), exprisin);
								synCookies(COOKIE_DOMAIN, sb);
								List<Map<String, CookieModel>> domainArray = model
										.getMulti_domain();
								if (domainArray != null
										&& domainArray.size() > 0) {
									// do cookie for domainArray
									syncCookiesForList(domainArray);
								}
								// requestWebDetail();
								// contentWebView
								// .loadUrl("http://jifen.sina.com.cn/h5/app_inner_test");
								notifyCookieSetComplete(info.getGuid());
							}
						}
					}
				}, null);
	}
//	/**
//	 * 请求列表数据
//	 */
//	protected void requestData() {
//
//		RequestOptions requestOptions = new RequestOptions()
//				.setHttpRequestType(HttpTypeEnum.post).setIsMainThread(false)
//				.setIsSaveMemory(false).setIsSaveDb(false)
//				.setMemoryLifeTime(RequestConstant.MEMORY_LIST_LIFETIME)
//				.setReturnDataClassTypeEnum(ReturnDataClassTypeEnum.object)
//				.setReturnModelClass(Map.class);
//
//		RSAEncryptRequestModel requestModel = new RSAEncryptRequestModel(
//				RequestConstant.RSA_DOAMIN, RequestConstant.RSA_PHPNAME);
//		if (UserManager.getInstance().isLogin()) {
//			AccountItem accountItem = UserManager.getInstance().getPlatformAccountItem(
//					PlatformType.SinaWeibo);
//			if (accountItem != null && accountItem.getResource() != null) {
//				requestModel.setToken(accountItem.getResource());
//			}
//			String token = UserManager.getInstance().getCurrentGtoken();
//			requestModel.setGtoken(token);
//			String deadLine = UserManager.getInstance().getCurrentDeadLine();
//			requestModel.setDeadline(deadLine);
//			String uid = UserManager.getInstance().getCurrentGuid();
//			requestModel.setGuid(uid);
//		}
//
//		ReuqestDataProcess.requestData(true, 0, requestModel, requestOptions,
//				new RequestDataListener() {
//
//					@Override
//					public void resultCallBack(TaskModel taskModel) {
//						if (taskModel.getReturnModel() != null) {
//							Map<String, List<JSONObject>> model = (HashMap<String, List<JSONObject>>) taskModel
//									.getReturnModel();
//							if (model != null) {
//								for (Map.Entry<String, List<JSONObject>> entry : model
//										.entrySet()) {
//									String cookieDomain = entry.getKey();
//									List<JSONObject> jsonList = entry
//											.getValue();
//									List<CookieModel> list = new ArrayList<CookieModel>();
//									for (JSONObject object : jsonList) {
//										CookieModel cookieModel = new CookieModel();
//										cookieModel.setName((String) object
//												.get("name"));
//										cookieModel.setValue((String) object
//												.get("value"));
//										cookieModel.setExpires((String) object
//												.get("expires"));// "1473142816");
//										list.add(cookieModel);
//									}
//									syncCookiesForList(cookieDomain, list// 10.210.228.41
//									);
//								}
//							}
//						}
//					}
//				}, null);
//	}

//	public static void removeAllCookie(Activity context) {
//		CookieSyncManager.createInstance(context);
//		CookieManager cookieManager = CookieManager.getInstance();
//		cookieManager.removeAllCookie();
//		CookieSyncManager.getInstance().sync();
//	}
	protected void removeAllCookie() {
		try {
			CookieSyncManager.createInstance(
					RunningEnvironment.getInstance().getApplicationContext());
			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.removeAllCookie();
			CookieSyncManager.getInstance().sync();
		} catch (Exception e) {
		}
	}

	public void setUserCookie(AccountInfo info) {
		this.info = info;
		requestTokenAndSetCookie();
	}

	protected void notifyCookieSetComplete(final String guid) {
		RunningEnvironment.getInstance().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				for (OnCookieSetCompleteListener listener : RunningEnvironment
						.getInstance().getUIListeners(
								OnCookieSetCompleteListener.class)) {
					listener.onCookieSetComplete(guid);
				}
			}
		});
	}

}

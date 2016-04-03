package com.sina.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.overlay.ApplicationUncaughtHandler;
import com.sina.custom.view.ProgressWebView;
import com.sina.custom.view.ProgressWebView.ChromeClientListener;
import com.sina.request.DeviceUtils;
import com.sina.request.Recommendation;
import com.sina.request.TitleViewUtils;
import com.sina.request.ViewUtils;
import com.sina.sinagame.credit.R;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author liu_chonghui
 * 
 */
public final class WebBrowserFragment extends BaseFragment implements
		OnClickListener {

	protected int getPageLayout() {
		return R.layout.web_browser;
	}

	// protected WebBrowserPopupAttacher popupAttacher;
	private TextView exitView;
	private String titleStr;
//	private OneKeyShare myOneKeyShare;
	protected View titleMainLayout;

	protected RelativeLayout mainLayout;
	protected ProgressWebView contentWebView;
	protected WebSettings webSettings;
	private String htmlStr;

	protected void initView(View view) {
		View btnRet = view.findViewById(R.id.title_turn_return);
		if (btnRet != null) {
			btnRet.setOnClickListener(this);
		}
		title = (TextView) view.findViewById(R.id.title_content);
		if (title != null) {
			title.setText(getIntentTitle(getActivity().getIntent()));
		}
		setUrl(getIntentUrl(getActivity().getIntent()));
		hostUrl = url;
		initWebView(view);

		titleMainLayout = view.findViewById(R.id.title_layout);
		TitleViewUtils.addRightTitleLayout(getActivity(), titleMainLayout,
				R.layout.web_browser_fragment_title_right);
		Button btnMenu = (Button) view.findViewById(R.id.btn_menu);
		if (btnMenu != null) {
			btnMenu.setOnClickListener(this);
		}
		exitView = (TextView) view.findViewById(R.id.web_browser_exit);
		if (exitView != null) {
			exitView.setOnClickListener(this);
		}
		title.setMaxWidth(ViewUtils.dp2px(getActivity(), 160));
		title.setSingleLine(true);
		title.setEllipsize(TruncateAt.END);
		if (title != null) {
			titleStr = getIntentTitle(getActivity().getIntent());
			title.setText(titleStr);
		}

		initContent();
	}

	@Override
	public void onClick(View view) {
		final int id = view.getId();
		if (R.id.title_turn_return == id) {
			returnLogic();
		} else if (R.id.custom_load_fail_button == id) {
			refeshLogic();
		} else if (R.id.btn_menu == id) {
			// TODO 点击菜单
			initShare();
			shareLogic();
		} else if (R.id.web_browser_exit == id) {
			// TODO 点击“关闭”
			getActivity().finish();
			getActivity().overridePendingTransition(R.anim.push_still,
					R.anim.push_right_out);
		}
	}

	public boolean holdGoBack() {
//		if (myOneKeyShare != null && myOneKeyShare.isShow()) {
//			return true;
//		}

		if (contentWebView != null) {
			return contentWebView.canGoBack();
		}
		return false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean flag = false;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (contentWebView != null && contentWebView.canGoBack()) {
				contentWebView.goBack();
				if (queue.size() > 0) {
					queue.removeLast();
				}
				flag = true;
			}
//			if (myOneKeyShare != null && myOneKeyShare.isShow()) {
//				myOneKeyShare.close();
//				flag = true;
//			}
		}
		return flag;
	}

	/**
	 * 分享操作
	 */
	// public void shareAction() {
	// // TODO 点击分享
	// if (TextUtils.isEmpty(titleStr)) {
	// return;
	// }
	// initShare();
	// shareLogic();
	// }

	protected void shareLogic() {
//		String appname = getActivity().getString(R.string.app_name);
//		titleStr = titleStr == null || titleStr.length() == 0 ? appname
//				: titleStr;
//		String sAgeFormat = getResources().getString(
//				R.string.web_browser_share_content);
//		String content = String.format(sAgeFormat, titleStr);
//		// Bitmap img = ViewUtils.getScreenShot(getActivity());
//		// if(img!=null)
//		// img = ViewUtils.scaleBitmapImg(img, 300, 300);
//		Drawable iconDrawable = getActivity().getResources().getDrawable(
//				R.drawable.sinagame_icon);
//		BitmapDrawable iconBitmap = (BitmapDrawable) iconDrawable;
//		Bitmap bitmap = iconBitmap.getBitmap();
//		ShareSelectModel item = new ShareSelectModel();
//		item.setTitle(titleStr);
//		item.setContent(content);
//		item.setWeb_url(url);
//		item.setImgUrl(CommonConstant.appIconUrl);
//		item.setImage(bitmap);
//		myOneKeyShare.setShareContent(item);
//		myOneKeyShare.show(getActivity(), url);
	}

	protected void initShare() {
//		myOneKeyShare = ShareManager.getInstance()
//				.getWebBrowserPageOneKeyShare(getActivity(), null);
//		myOneKeyShare.setOnShareActionListener(new ShareActionAdapter() {
//			@Override
//			public void onShareSuccess(PlatformType type) {
//				// TODO 默认浏览器分享成功
//				String share_channel = TalkingDataManager.getShareChannel(type);
//
//				Map<String, String> map = new HashMap<String, String>();
//				map.put(Constant.TALKING_SHARE_SOURCE_CHANNEL_KEY,
//						share_channel);
//				if (getActivity() == null || getActivity().isFinishing())
//					return;
//				TalkingDataManager.TDevent(getActivity()
//						.getApplicationContext(),
//						Constant.TALKING_SHARE_SOURCE,
//						Constant.TALKING_SHARE_SOURCE_BROWSER, null);
//
//				TalkingDataManager.TDevent(getActivity()
//						.getApplicationContext(),
//						Constant.TALKING_SHARE_CHANNE, share_channel, null);
//			}
//		});
//
//		myOneKeyShare.setOnClickListener(ShareMethod.FONT,
//				new OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						showPopTextSize();
//						if (myOneKeyShare != null) {
//							myOneKeyShare.close();
//						}
//					}
//				});
	}

	protected int current_textszie;
//	TextSizeChangeAttacher textSizeAttacher;
	
	protected void showPopTextSize() {
//		if (textSizeAttacher == null) {
//			textSizeAttacher = new TextSizeChangeAttacher(getActivity()) {
//				@Override
//				protected void onTextSizeChanged(RadioGroup group, int checkedId) {
//					if (checkedId == R.id.button_font_low) {
//						current_textszie = 0;
//					} else if (checkedId == R.id.button_font_medium) {
//						current_textszie = 1;
//					} else if (checkedId == R.id.button_font_high) {
//						current_textszie = 2;
//					} else if (checkedId == R.id.button_font_exhigh) {
//						current_textszie = 3;
//					} else {
//						return;
//					}
//					PreferencesUtils.writeInt(getActivity(),
//							Constant.SETTING_TEXT_SIZE,
//							Constant.WEBVIEW_TEXT_SIZE_KEY, current_textszie);
//				}
//			};
//		}
//		showTextsizePop();
	}

	public void showTextsizePop() {
//		if (textSizeAttacher != null && !textSizeAttacher.isShowing()) {
//			textSizeAttacher.show();
//		}
	}

	public void closeTextsizePop() {
//		if (textSizeAttacher != null && textSizeAttacher.isShowing()) {
//			textSizeAttacher.closePop();
//		}
	}

	protected WebView content;
	protected String hostUrl;
	protected String url;
	protected LinkedList<String> queue = new LinkedList<String>();
	protected TextView title;
	protected Recommendation recommendation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ApplicationUncaughtHandler(
				getActivity()));
		initController();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (!isViewNull()) {
			return mView;
		}
//		removeAllCookie();
		mView = inflater.inflate(getPageLayout(), container, false);
		initView(mView);
//		AccountItem item = AccountManager.getInstance().getCurrentAccountItem();
//		if (AuthorizeManager.getInstance().isAuthorized()
//				&& item.getExpiresin() != null
//				&& new Date().getTime() < item.getExpiresin().getTime()) {
//			removeAllExpiredCookie();
//			CookieManager cookieManager = CookieManager.getInstance();
//			String CookieStr = cookieManager
//					.getCookie(RequestConstant.COOKIE_DOMAIN);
//			if (TextUtils.isEmpty(CookieStr)) {
//				requestData();
//			}
//			// else {
//			// contentWebView
//			// .loadUrl("http://jifen.sina.com.cn/h5/app_inner_test");
//			// }
//		} else {
//			removeAllCookie();
//		}
		return mView;
	}

	protected void initController() {
		getActivity().overridePendingTransition(R.anim.push_left_in,
				R.anim.push_still);
	}

	protected void prepareView(View view) {

	}

	protected void initContent() {
		if (checkWebRecommendation(getActivity().getIntent())) {
			refreshWebRecommendation();
		}
		requestWebDetail();
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (checkWebRecommendation(intent)) {
			refreshWebRecommendation();
		} else {
			setUrl(getIntentUrl(intent));
			hostUrl = url;
			if (title != null) {
				title.setText(getIntentTitle(intent));
			}
		}
		refeshLogic();
	}

	@Override
	public void onResume() {
		super.onResume();
//		if (recommendation != null) {
//			PushManager.getInstance().removeRecommendation(recommendation);
//		}
	}

	public static final String UA_STR = "%1$s SinaGameApp/%2$s";

	protected void initWebView(View view) {
		// mPullRefreshWebView = (PullToRefreshWebView) view
		// .findViewById(R.id.web_detail_webview);
		// mPullRefreshWebView.setMode(Mode.DISABLED);
		// mPullRefreshWebView.getLoadingLayoutProxy().setLastUpdatedLabel("");
		// mPullRefreshWebView.getLoadingLayoutProxy().setPullLabel("");
		// mPullRefreshWebView.getLoadingLayoutProxy().setRefreshingLabel("");
		// mPullRefreshWebView.getLoadingLayoutProxy().setReleaseLabel("");
		// mPullRefreshWebView.setOnPullEventListener(mPullEventListener);
		// mPullRefreshWebView.setOnRefreshListener(mRefreshListener2);
		// contentWebView = mPullRefreshWebView.getRefreshableView();

		contentWebView = (ProgressWebView) view
				.findViewById(R.id.web_detail_webview);
		contentWebView.setmChromeClientListener(new ChromeClientListener() {

			@Override
			public void onReceivedTitle(WebView view, String title) {
				titleStr = title;
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {

			}
		});

		contentWebView.requestFocus();
		// contentWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		webSettings = contentWebView.getSettings();
		// 拼接ua串 sinaAPP/版本号
		String uaStr = webSettings.getUserAgentString();
		String uaStrFormat = String.format(UA_STR, uaStr,
				DeviceUtils.getVersionRelease());
		webSettings.setUserAgentString(uaStrFormat);
		// settings.setSupportZoom(true);
		contentWebView.getSettings().setJavaScriptEnabled(true);
		contentWebView.getSettings().setDefaultTextEncodingName("UTF-8");
		String strPadding = getIntentPadding(getActivity().getIntent());
		int padding = 0;
		try {
			padding = Integer.valueOf(strPadding);
		} catch (Exception e) {
			padding = 0;
		}
		if (padding > 0) {
			// mPullRefreshWebView.setPadding(padding, 0, padding, 0);
			// mPullRefreshWebView.setVerticalScrollBarEnabled(true);
		}
		// settings.setDefaultTextEncodingName("GBK");
		// contentWebView.getSettings().setLayoutAlgorithm(
		// LayoutAlgorithm.SINGLE_COLUMN);
		// settings.setUseWideViewPort(true);
		// settings.setLoadWithOverviewMode(true);
		// settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		// webSettings.setBlockNetworkImage(true);

		// int screenDensity = getResources().getDisplayMetrics().densityDpi;
		// WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
		// if (screenDensity == DisplayMetrics.DENSITY_LOW) {
		// zoomDensity = WebSettings.ZoomDensity.CLOSE;
		// } else if (screenDensity == DisplayMetrics.DENSITY_MEDIUM) {
		// zoomDensity = WebSettings.ZoomDensity.MEDIUM;
		// } else if (screenDensity >= DisplayMetrics.DENSITY_HIGH) {
		// zoomDensity = WebSettings.ZoomDensity.FAR;
		// }
		// webSettings.setSupportZoom(true);
		// webSettings.setDefaultZoom(zoomDensity);
		// setLocalTextSizeData();
		// contentWebView.setInitialScale(39);
		contentWebView.getSettings().setBuiltInZoomControls(true);
		contentWebView.getSettings().setUseWideViewPort(true);
		contentWebView.getSettings().setLoadWithOverviewMode(true);
		// contentWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
		// contentWebView.getSettings().setBlockNetworkImage(true);
		contentWebView.getSettings().setCacheMode(WebSettings.LOAD_NORMAL);

		contentWebView.setWebViewClient(new MyWebViewClient());

		contentWebView.setDownloadListener(new MyWebViewDownLoadListener());

		// contentWebView.setWebChromeClient(new MyChromeClient());

		mainLayout = (RelativeLayout) view
				.findViewById(R.id.detail_main_layout);
		//
		// loadLayout = new CustomLoadView(getActivity());
		//
		// loadLayout.creatView(mainLayout, this);
		//
		// loadLayout.flushLoadView(CustomLoadView.LOAD_ING);
		String filename = getActivity().getIntent().getStringExtra("filename");
		String insertStr = getActivity().getIntent().getStringExtra("data");
//		if (!TextUtils.isEmpty(filename)) {
//			htmlStr = Utils.getHtmlMouldFromAssets(filename);
//			if (!TextUtils.isEmpty(insertStr))
//				htmlStr = htmlStr.replace("xxxx", insertStr);
//		}
	}

	private class MyWebViewDownLoadListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			getActivity().startActivity(intent);
			// if (loadLayout != null) {
			// loadLayout.flushLoadView(CustomLoadView.LOAD_SUCESS);
			// }
		}

	}

	public Recommendation getRecommendation() {
		return recommendation;
	}

	protected String getIntentTitle(Intent intent) {
		String title = getActivity().getString(R.string.app_name);
		String intentContent = intent.getStringExtra("title");
		if (intentContent != null) {
			title = intentContent;
		}
		return title;
	}

	protected String getIntentUrl(Intent intent) {
		String url = "";
		String intentContent = intent.getStringExtra("url");
		if (intentContent != null && intentContent.length() > 0) {
			url = intentContent;
		}
		return url;
	}

	protected String getIntentPadding(Intent intent) {
		String padding = "0";
		String intentContent = intent.getStringExtra("padding");
		if (intentContent != null && intentContent.length() > 0) {
			padding = intentContent;
		}
		return padding;
	}

	protected boolean checkWebRecommendation(Intent intent) {
//		String action = intent.getAction();
//		String typeName = RoyaltyIntentBuilder.getRoyalTypeName(intent);
//		String identify = RoyaltyIntentBuilder.getRoyalIdentify(intent);
//		if (action == null || action.length() == 0) {
//			recommendation = null;
//		} else if (action.contains(PushAssistant.ACTION_WEBRECOMMENDATION)) {
//			recommendation = PushManager.getInstance().getRecommendation(
//					typeName, identify);
//			if (recommendation == null) {
//				Log.d("PUSHLOG", "NewsDetail->WebRecommendation == null");
//				getActivity().finish();
//				// // 在二层界面发生崩溃
//				// recommendation = new WebRecommendation(account, user);
//				// recommendation.url = account;
//				// recommendation.messageId = user;
//				// recommendation.channelId = "abcd123456";
//				return false;
//			} else {
//				Log.d("PUSHLOG", "NewsDetail->WebRecommendation:"
//						+ recommendation.getUuid() + ", " + recommendation.url);
//			}
//			// TODO 新浪游戏中，所有的web页推送跳转都算“活动详情页”跳转
//			TalkingDataManager.TDevent(getActivity().getApplicationContext(),
//					Constant.TALKING_ACTIVITY_LIST_CLICK,
//					Constant.TALKING_ACTIVITY_CLICK_PUSH, null);
//			return true;
//		} else {
//			recommendation = null;
//		}

		return false;
	}

	protected void refreshWebRecommendation() {
//		if (recommendation == null || recommendation.url == null
//				|| recommendation.url.length() == 0) {
//			return;
//		}
//
//		setUrl(recommendation.url);
//		hostUrl = url;
	}

	// protected PullToRefreshWebView mPullRefreshWebView;
	// protected CustomLoadView loadLayout;

	protected void refeshLogic() {
		alreadyFailed = false;

		// mPullRefreshWebView.onRefreshComplete();

		// loadLayout.flushLoadView(CustomLoadView.LOAD_ING);

		contentWebView.loadDataWithBaseURL(null, null, "text/html", "utf-8",
				null);
		contentWebView.setVisibility(View.GONE);
		
		requestWebDetail();
	}

//	class RequestResult extends AjaxCallBack<Object> {
//		@Override
//		public void onSuccess(Object t) {
//			if (getActivity() != null && getActivity().isFinishing()) {
//				return;
//			}
//			String str = null;
//			if (t != null) {
//				str = t.toString();
//			}
//			if (str != null && str.length() > 0) {
//				contentWebView.loadDataWithBaseURL(null, str, "text/html",
//						"utf-8", null);
//
//				// loadLayout.flushLoadView(CustomLoadView.LOAD_SUCESS);
//				contentWebView.setVisibility(View.VISIBLE);
//			} else {
//				// loadLayout.flushLoadView(CustomLoadView.LOAD_FAIL);
//			}
//			// mPullRefreshWebView.onRefreshComplete();
//		}
//
//		@Override
//		public void onFailure(Throwable t, int errorNo, String strMsg) {
//			// TODO
//			// loadLayout.flushLoadView(CustomLoadView.LOAD_FAIL);
//			// mPullRefreshWebView.onRefreshComplete();
//		}
//	}

	private void requestWebDetail() {
		// TODO
		alreadyFailed = false;
		Log.d("WEB", "WebView.loadUrl:" + url);
		if (TextUtils.isEmpty(htmlStr)) {
			contentWebView.loadUrl(url);
		}
		else{
			contentWebView.loadDataWithBaseURL(null, htmlStr, "text/html", "utf-8", null);
		}
	}

	// OnRefreshListener2<WebView> mRefreshListener2 = new
	// OnRefreshListener2<WebView>() {
	//
	// @Override
	// public void onPullDownToRefresh(PullToRefreshBase<WebView> refreshView) {
	// mPullRefreshWebView.onRefreshComplete();
	// }
	//
	// @Override
	// public void onPullUpToRefresh(PullToRefreshBase<WebView> refreshView) {
	// mPullRefreshWebView.onRefreshComplete();
	// }
	//
	// };

	// OnPullEventListener<WebView> mPullEventListener = new
	// OnPullEventListener<WebView>() {
	//
	// @Override
	// public void onPullEvent(PullToRefreshBase<WebView> refreshView,
	// State state, Mode direction) {
	//
	// if (direction == Mode.PULL_FROM_START) {
	// mPullRefreshWebView.getLoadingLayoutProxy().setPullLabel("");
	// mPullRefreshWebView.getLoadingLayoutProxy().setReleaseLabel("");
	// mPullRefreshWebView.getLoadingLayoutProxy()
	// .setLastUpdatedLabel("");
	// } else if (direction == Mode.PULL_FROM_END) {
	// String downTurn = getActivity().getResources().getString(
	// R.string.pull_to_refresh_news_detail_down);
	// mPullRefreshWebView.getLoadingLayoutProxy().setPullLabel("");
	// mPullRefreshWebView.getLoadingLayoutProxy().setReleaseLabel("");
	// mPullRefreshWebView.getLoadingLayoutProxy()
	// .setLastUpdatedLabel("");
	// }
	//
	// }
	// };

	void setUrl(String url) {
		this.url = url;
		queue.addLast(url);
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			Log.d("WEB", "errorCode:" + errorCode + ", desc:" + description
					+ ", url:" + failingUrl);
			alreadyFailed = true;
			contentWebView.setVisibility(View.GONE);
			// loadLayout.flushLoadView(CustomLoadView.LOAD_FAIL);
			// mPullRefreshWebView.onRefreshComplete();
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d("WEB", "shouldOverrideUrlLoading:" + url);
			setUrl(url);
			view.loadUrl(url);
			// refeshLogic();
			// CookieManager cookieManager = CookieManager.getInstance();
			// cookieManager.setAcceptCookie(true);
			// String CookieStr =
			// cookieManager.getCookie(RequestConstant.COOKIE_DOMAIN);
			// Log.e("sunzn", "Cookies = " + CookieStr);

			return false;
		}

		// public void onPageFinished(WebView view, String url) {
		// CookieManager cookieManager = CookieManager.getInstance();
		// cookieManager.setAcceptCookie(true);
		// String CookieStr =
		// cookieManager.getCookie(RequestConstant.COOKIE_DOMAIN);
		// Log.e("sunzn", "Cookies = " + CookieStr);
		// super.onPageFinished(view, url);
		// }
	}

	boolean alreadyFailed = false;

	// private class MyChromeClient extends WebChromeClient {
	//
	// @Override
	// public void onProgressChanged(WebView view, int newProgress) {
	// super.onProgressChanged(view, newProgress);
	// Log.d("WEB", "Progress:" + newProgress);
	// if (alreadyFailed) {
	// return;
	// }
	//
	// if (newProgress <= 15) {
	// // setWebViewLoading(view);
	// }
	// if (newProgress > 90) {
	// Log.d("WEB", "Progress> 90:" + newProgress);
	// loadLayout.flushLoadView(CustomLoadView.LOAD_SUCESS);
	// contentWebView.setVisibility(View.VISIBLE);
	// mPullRefreshWebView.onRefreshComplete();
	// }
	// }
	//
	// @Override
	// public void onReceivedTitle(WebView view, String title) {
	// super.onReceivedTitle(view, title);
	// titleStr = title;
	// }
	// }

	public void setLocalTextSizeData() {
//		int text_size_value = PreferencesUtils.getInt(getActivity(),
//				Constant.SETTING_TEXT_SIZE, Constant.WEBVIEW_TEXT_SIZE_KEY,
//				Constant.WEBVIEW_TEXT_SIZE_VALUE);
//		if (text_size_value <= Constant.WEBVIEW_TEXT_SIZE_LIST.length - 1) {
//			webSettings
//					.setTextSize(Constant.WEBVIEW_TEXT_SIZE_LIST[text_size_value]);
//		}
	}

	private void returnLogic() {
		if (!holdGoBack()) {
			getActivity().finish();
			getActivity().overridePendingTransition(R.anim.push_still,
					R.anim.push_right_out);
		} else {
			contentWebView.goBack();
		}
	}
}

package com.sina.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.sina.sinagame.credit.R;

public class ProgressWebView extends WebView {

    private ProgressBar progressbar;
    private ChromeClientListener mChromeClientListener;
    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 3, 0, 0));
        progressbar.setProgressDrawable(context.getResources().getDrawable(R.drawable.bar_color));
        addView(progressbar);
        //        setWebViewClient(new WebViewClient(){});
        setWebChromeClient(new WebChromeClient());
    }
    
	public ChromeClientListener getmChromeClientListener() {
		return mChromeClientListener;
	}

	public void setmChromeClientListener(ChromeClientListener mChromeClientListener) {
		this.mChromeClientListener = mChromeClientListener;
	}



	public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            if(mChromeClientListener!=null)
            	mChromeClientListener.onProgressChanged(view, newProgress);
            super.onProgressChanged(view, newProgress);
        }

        @Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			if(mChromeClientListener!=null)
				mChromeClientListener.onReceivedTitle(view, title);
		}
    }
    
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
    
    public interface ChromeClientListener{
    	public abstract void onProgressChanged(WebView view, int newProgress);
    	public abstract void onReceivedTitle(WebView view, String title);
    }
}

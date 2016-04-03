package com.sina.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.sina.sinagame.credit.R;

public class WebViewActivity extends Activity {

    String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            url = getIntent().getStringExtra("url");
        }
        setContentView(R.layout.main);
        // Instanciation du WebView...
        WebView wvSite = (WebView) findViewById(R.id.webview);
        //...on active JavaScript...
        WebSettings webSettings = wvSite.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //...et on charge la page
        wvSite.loadUrl(url);
    }

}
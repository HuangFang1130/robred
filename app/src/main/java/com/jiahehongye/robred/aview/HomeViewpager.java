package com.jiahehongye.robred.aview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jiahehongye.robred.R;

public class HomeViewpager extends AppCompatActivity {
	private WebView webView;
	private String helpUrl;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.pager_home_viewpager);
	
		initData();
	}

	private void initData() {

		Intent intent = getIntent();
		helpUrl =intent.getStringExtra("url");
		Log.e("传递过来的地址==",helpUrl);
		webView = (WebView) findViewById(R.id.viewpager_webView);
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);// 设置支持javascript脚本
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setSupportZoom(true); // 支持缩放
		settings.setBuiltInZoomControls(true);// 设置显示缩放按钮
		settings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
		settings.setLoadWithOverviewMode(true);
		
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}
		});
		
		webView.loadUrl(helpUrl);

	}
}

package com.jiahehongye.robred.oneyuan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.R.id;

public class OneyuanRulesActivity extends BaseActivity {
	
	private WebView webView;

	private RelativeLayout mBack;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		applyKitKatTranslucency();
		mTintManager.setStatusBarTintResource(R.color.home_state_color);
		setContentView(R.layout.activity_oneyuan_rules);

		mBack = (RelativeLayout) findViewById(id.oneyuanrules_rl_back);
		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		webView = (WebView) findViewById(id.web_oneyuan_rules);
		
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
				return true;
			}
		});
		
		webView.loadUrl(Constant.ONEYUANRULES);
		
	}
}

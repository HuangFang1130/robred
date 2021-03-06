package com.jiahehongye.robred.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;

public class WebActivity extends BaseActivity {

    private TextView web_title;
    private RelativeLayout web_back;
    private WebView web_all;
    private String url;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_web);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            url = bundle.getString("URL");
            title = bundle.getString("title");
        }

        web_all = (WebView) findViewById(R.id.web_all);
        web_title = (TextView) findViewById(R.id.web_title);
        web_back = (RelativeLayout) findViewById(R.id.web_back);

        if (!TextUtils.isEmpty(title)){
            web_title.setText(title);
        }

        web_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        WebSettings settings = web_all.getSettings();
        settings.setJavaScriptEnabled(true);
        web_all.loadUrl(url);
        web_all.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }
        });
    }
}

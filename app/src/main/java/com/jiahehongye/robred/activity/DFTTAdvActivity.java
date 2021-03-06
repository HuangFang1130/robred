package com.jiahehongye.robred.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.utils.LogUtil;
import com.songheng.newsapisdk.sdk.apiservice.DFTTStatisticsApiService;
import com.songheng.newsapisdk.sdk.apiservice.listener.DfttApiServiceCallBack;

import java.util.ArrayList;

import okhttp3.Response;

/**
 * Created by huangjunhui on 2017/5/23.9:46
 */
public class DFTTAdvActivity extends BaseActivity implements View.OnClickListener {
    private String type;
    private String title;
    private String url;
    private String isdownload;
    private int isdsp;
    private String reporturl;
    private ArrayList<String> clickrep;
    private ArrayList<String> showrep;
    private String position;
    private String reqeustPage;
    private String reqeusturl;
    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dfttadv);
        WebView mDFTTWebView = (WebView) findViewById(R.id.dftt_adv_webview);

        id = getIntent().getStringExtra("id");
        url = getIntent().getStringExtra("url");
        position = getIntent().getStringExtra("position");
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        isdownload = getIntent().getStringExtra("isdownload");
        isdsp = getIntent().getIntExtra("isdsp", 0);
        reporturl = getIntent().getStringExtra("reporturl");
        clickrep = getIntent().getStringArrayListExtra("clickrep");
        showrep = getIntent().getStringArrayListExtra("showrep");

        ImageView mIvBack = (ImageView) findViewById(R.id.application_iv_back);
        TextView mTvTitle = (TextView) findViewById(R.id.application_tv_title);

        mIvBack.setOnClickListener(this);
        mTvTitle.setText(title);
        mDFTTWebView.getSettings().setJavaScriptEnabled(true);
        mDFTTWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                    showMyDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

//                    animDialog.dismiss();

            }
        });
//        mDFTTWebView.loadDataWithBaseURL(url, html, "text/html", "utf-8", null);
//        mDFTTWebView.loadDataWithBaseURL(url,null,);
//        mDFTTWebView.loadData(this.encode(url), "text/html", "utf-8");
        mDFTTWebView.loadUrl(url);


        requestServer();

    }

    //上传日志  点击的时候
    private void requestServer() {
        reqeusturl= url;
        reqeustPage = "2";

        if (isdsp == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(reporturl).append("\t");
            if (clickrep != null) {
                for (int i = 0; i < clickrep.size(); i++) {
                    stringBuilder.append(clickrep.get(i)).append("\t");
                }
            }
            if (showrep != null) {
                for (int i = 0; i < showrep.size(); i++) {
                    stringBuilder.append(showrep.get(i));
                    if (i != showrep.size() - 1) {
                        stringBuilder.append("\t");
                    }
                }

            }
            //联盟广告
            DFTTStatisticsApiService.pageUnionAdsOperate(reporturl,id, type, null, url, position, null, stringBuilder.toString(), reqeustPage, new DfttApiServiceCallBack() {
                @Override
                public void onSuccess(String s) {
                    LogUtil.LogShitou("广告页点击上报success：",s.toString());

                }

                @Override
                public void onError(String s, String s1, Response response, Exception e) {
                     LogUtil.LogShitou("广告页点击上报error：",e.toString());

                }
            });
        } else {
            //dsp广告
            DFTTStatisticsApiService.pageDSPAdsClick(url, url, id, new DfttApiServiceCallBack() {
                @Override
                public void onSuccess(String s) {
                    LogUtil.LogShitou("广告页点击上报DSP  :success：",s.toString());

                }

                @Override
                public void onError(String s, String s1, Response response, Exception e) {
//                    LogUtil.LogShitou("广告页：",s.toString()+"  "+s1.toString());
                    LogUtil.LogShitou("广告页点击上报DSP  :error：",e.toString());


                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.application_iv_back:
                finish();
                break;
        }
    }

    final String digits = "0123456789ABCDEF";

    public String encode(String s)

    {
        // Guess a bit bigger for encoded form
        StringBuilder buf = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
                    || (ch >= '0' && ch <= '9') || ".-*_".indexOf(ch) > -1) { //$NON-NLS-1$
                buf.append(ch);
            } else {
                byte[] bytes = new String(new char[]{ch}).getBytes();
                for (int j = 0; j < bytes.length; j++) {
                    buf.append('%');
                    buf.append(digits.charAt((bytes[j] & 0xf0) >> 4));
                    buf.append(digits.charAt(bytes[j] & 0xf));
                }
            }
        }
        return buf.toString();
    }
}

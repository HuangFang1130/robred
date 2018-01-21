package com.jiahehongye.robred.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AboutUsActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mBack;
    private RelativeLayout welcome;
    private RelativeLayout service;
    private PersistentCookieStore persistentCookieStore;
    private Call call;
    private String helpUrl;
    private static final int GET_ALL = 0000;
    private static final int GET_PAGE = 0001;
    private String productPage;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_ALL:
                    String s = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(s);
                        if (object.getString("result").equals("success")) {
                            JSONObject data = new JSONObject(object.getString("data"));
                            helpUrl = data.getString("serviceContractUrl");

                            service.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent1 = new Intent(getApplicationContext(),WebActivity.class);
                                    intent1.putExtra("title","服务协议");
                                    intent1.putExtra("URL", helpUrl);
                                    startActivity(intent1);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case GET_PAGE:
                    String a = (String) msg.obj;

                    try {
                        JSONObject object = new JSONObject(a);
                        if (object.getString("result").equals("success")) {
                            JSONObject data = new JSONObject(object.getString("data"));
                            productPage = data.getString("productPage");

                            chanpinjieshao.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent2 = new Intent(getApplicationContext(),WebActivity.class);
                                    intent2.putExtra("title","产品介绍");
                                    intent2.putExtra("URL", productPage);
                                    startActivity(intent2);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };
    private RelativeLayout contant_us;
    private RelativeLayout chanpinjieshao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_about_us);

        mBack = (RelativeLayout) findViewById(R.id.aboutus_rl_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        welcome = (RelativeLayout) findViewById(R.id.welcome);
        contant_us = (RelativeLayout) findViewById(R.id.contant_us);
        service = (RelativeLayout) findViewById(R.id.service);
        chanpinjieshao = (RelativeLayout) findViewById(R.id.chanpinjieshao);

        welcome.setOnClickListener(this);
        contant_us.setOnClickListener(this);

        initData();
        getData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.welcome:
                startActivity(new Intent(this,SettingWelcome.class));
                break;
            case R.id.service:
                break;


            case R.id.contant_us:
                Intent intent = new Intent(getApplicationContext(),WebActivity.class);
                intent.putExtra("title","联系我们");
                intent.putExtra("URL", Constant.URL+"/res/wap/contactus.html");
                startActivity(intent);
                break;

        }
    }

    private void initData() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getApplicationContext());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.GETHELPPAGE)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                AboutUsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AboutUsActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                Message msg = handler.obtainMessage();
                msg.what = GET_ALL;
                msg.obj = result;
                handler.sendMessage(msg);


            }
        });
    }
    private void getData() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getApplicationContext());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.GETPRODUCT)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                AboutUsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AboutUsActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                Message msg = handler.obtainMessage();
                msg.what = GET_PAGE;
                msg.obj = result;
                handler.sendMessage(msg);


            }
        });
    }
}

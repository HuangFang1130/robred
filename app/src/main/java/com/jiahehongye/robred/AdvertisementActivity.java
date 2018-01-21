package com.jiahehongye.robred;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobads.SplashAd;
import com.baidu.mobads.SplashAdListener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jiahehongye.robred.activity.SplashActivity;
import com.jiahehongye.robred.bean.LoginResult;
import com.jiahehongye.robred.biz.model.WelcomAdvertisingResponse;
import com.jiahehongye.robred.utils.LogUtil;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by huangjunhui on 2017/1/19.18:11
 */
public class AdvertisementActivity extends BaseActivity implements View.OnClickListener {

    private static final int TIMER = 201;
    private static final int LOGIN_OK = 202;
    private int timer = 4;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMER:
                    timer--;
                    if (timer > 0) {
                        startTimer();     // send message
                    } else {
                        tvSecond.setText(String.format(getString(R.string.ad_second), timer));
                        goMain();
                    }
                    break;
                case LOGIN_OK:
                    String result = (String) msg.obj;
                    LoginResult loginResult = new Gson().fromJson(result, LoginResult.class);
                    SPUtils.put(UIUtils.getContext(), Constant.IS_LOGIN, true);
                    AppHelper.getInstance().loginHuanXin();
                    break;
                case 203:
                    loadedAD((WelcomAdvertisingResponse) msg.obj);
                    break;
                case 204:
//                    timer = 1;
//                    startTimer();

                    requesSplashAdv();
                    break;
            }
        }
    };

    private ImageView mIvAvertisement;
    private TextView tvSecond;
    private RelativeLayout mRlParent;
    private FrameLayout flSkip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_advertisement);
        mIvAvertisement = (ImageView) findViewById(R.id.avertisement_iv);
        mRlParent = (RelativeLayout) findViewById(R.id.adsRl);
        tvSecond = (TextView) findViewById(R.id.avertisement_tv);
//        Glide.with(UIUtils.getContext()).load(Constant.ADVERTISEMENT_IMG).diskCacheStrategy(DiskCacheStrategy.NONE )//禁用磁盘缓存
//                .skipMemoryCache(true) //跳过内存缓存
//                .into(mIvAvertisement);
//        handler.postDelayed(loadAD, 1000);
        requestDate();


        AppHelper.getInstance().loginMySelefServer();
        flSkip = (FrameLayout) findViewById(R.id.layout_skip);

        flSkip.setOnClickListener(this);
    }

    private void requesSplashAdv() {
        mRlParent.setVisibility(View.VISIBLE);
        mIvAvertisement.setVisibility(View.GONE);
        // the observer of AD
        SplashAdListener listener = new SplashAdListener() {


            @Override
            public void onAdDismissed() {
                Log.i("RSplashActivity", "onAdDismissed");
//                jumpWhenCanClick(); // 跳转至您的应用主界面
            }

            @Override
            public void onAdFailed(String arg0) {
                Log.i("RSplashActivity", "onAdFailed");
//                jump();
                requesSplashAdv();


            }

            @Override
            public void onAdPresent() {
                Log.i("RSplashActivity", "onAdPresent");
                startTimer();
                tvSecond.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClick() {
                Log.i("RSplashActivity", "onAdClick");
                // 设置开屏可接受点击时，该回调可用
            }
        };
        String adPlaceId = "4510084"; // 重要：请填上您的广告位ID，代码位错误会导致无法请求到广告
        new SplashAd(this, mRlParent, listener, adPlaceId, true);

    }

//    private void login() {
//        String mobile = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_MOBILE, "");
//        String password = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_PASSWORD, "");
//        if (mobile.equals("") || password.equals("")) {
//            SPUtils.put(UIUtils.getContext(), Constant.IS_LOGIN, false);
//        } else {
//            String decrypt = DesUtil.decrypt(password);
//            login(mobile, decrypt);
//            AppHelper.getInstance().registerId(BaseApplication.registrationId);
//        }
//
//    }
//
//    private void login(String mobile, String decrypt) {
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(getApplicationContext());
//        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
//        builder.cookieJar(cookieJarImpl);
//        OkHttpClient client = builder.build();
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("mobile", mobile);
//            jsonObject.put("password", decrypt);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
//        Request request = new Request.Builder()
//                .url(Constant.LOGIN_URL)
//                .post(body)
//                .build();
//
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                AdvertisementActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(AdvertisementActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
//                        SPUtils.put(UIUtils.getContext(), Constant.IS_LOGIN, false);
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String result = response.body().string();
//                Message msg = handler.obtainMessage();
//                msg.what = LOGIN_OK;
//                msg.obj = result;
//                handler.sendMessage(msg);
//            }
//        });
//
//    }

//    private Runnable loadAD = new Runnable() {
//        @Override
//        public void run() {
//            requestDate();
//        }
//    };

    /**
     * 获取开屏页广告
     */
    private void requestDate() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient client = builder.build();

        Request request = new Request.Builder()
                .url(Constant.ADVERTISEMENT_URL)
                .get()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) { //不处理
                handler.sendEmptyMessage(204);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
//                Intent intent = new Intent(AdvertisementActivity.this, SplashActivity.class);
//                startActivity(intent);
//                finish();
                if (response.isSuccessful()) {

                    LogUtil.LogShitou("splash: ", result);
                    final WelcomAdvertisingResponse info = new Gson().fromJson(result, WelcomAdvertisingResponse.class);
                    if ("success".equals(info.getResult())) {
                        Message msg = handler.obtainMessage(203);
                        msg.obj = info;
                        handler.sendMessage(msg);
                    } else { //不处理
                        handler.sendEmptyMessage(204);
                    }
                } else { //不处理
                    handler.sendEmptyMessage(204);
                }
            }
        });
    }

    //网络图片广告请求完毕
    private void loadedAD(WelcomAdvertisingResponse info) {

        mIvAvertisement.setVisibility(View.VISIBLE);
        mRlParent.setVisibility(View.GONE);
        String adIsBanner = info.getData().getAdIsBanner();
        String adIsVertImg = info.getData().getAdIsVertImg();
        SPUtils.put(UIUtils.getContext(), Constant.IS_ADV, adIsBanner);
        SPUtils.put(UIUtils.getContext(), Constant.IS_ADV_BANNER_RED_DETAIL, adIsVertImg);

        if (info.getData().getAdImgAddr() == null) {
            requesSplashAdv();
            return;
        }
        if (info.getData().getAdImgAddr().equals("")) {
            requesSplashAdv();
        } else {
            Glide.with(UIUtils.getContext())
                    .load(info.getData().getAdImgAddr())
//                .diskCacheStrategy( DiskCacheStrategy.NONE )//禁用磁盘缓存
                    .skipMemoryCache(true) //跳过内存缓存
                    .crossFade()
                    .centerCrop()
                    .into(mIvAvertisement);
            startTimer();
            tvSecond.setVisibility(View.VISIBLE);
        }

    }

    private void startTimer() {
        tvSecond.setText(String.format(getString(R.string.ad_second), timer));
        Message message = handler.obtainMessage(TIMER);     // Message
        handler.sendMessageDelayed(message, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Message message = handler.obtainMessage(TIMER);     // Message
//        handler.sendMessageDelayed(message, 1000);

        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    public void onClick(View v) {
        handler.removeMessages(TIMER);
        goMain();
    }

    private void goMain() {
        flSkip.setOnClickListener(null);
        Intent intent = new Intent(AdvertisementActivity.this, SplashActivity.class);
        startActivity(intent);
        finish();
    }
}

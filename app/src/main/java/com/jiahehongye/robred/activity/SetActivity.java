package com.jiahehongye.robred.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.FuncSettingsBean;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.utils.DataCleanManager;
import com.jiahehongye.robred.utils.NetworkAvailable;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.UISwitchButton;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SetActivity extends BaseActivity implements View.OnClickListener {

    private static final int LOGOUT_SUCCESS = 201;
    private RelativeLayout mBack;
    private LinearLayout account_safe;
    private LinearLayout help_center;
    private LinearLayout about_us;
    private LinearLayout suggestion;
    private TextView cachesize;
    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private static final String TAG = "JPush";
    private LinearLayout clean_;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGOUT_SUCCESS:
                    UIUtils.exit();
                    boolean b = new PersistentCookieStore(UIUtils.getContext()).removeAll();
                    SPUtils.clear(UIUtils.getContext());
                    SPUtils.put(UIUtils.getContext(), Constant.LOGOUT, true);
                    SPUtils.put(UIUtils.getContext(), Constant.IS_LOGIN, false);
                    SPUtils.put(UIUtils.getContext(),Constant.FIRST_COMEIN,false);
                    SPUtils.put(UIUtils.getContext(), Constant.FIRST_COMEIN_PERSONAL, false);
                    Intent intent = new Intent(SetActivity.this, LoginActivity.class);
                    intent.putExtra(Constant.EXIT_SUCCESS,true);
                    startActivity(intent);
                    break;
                case 1:
                    String dataa = (String) msg.obj;
                    FuncSettingsBean funcSettingsBean = new Gson().fromJson(dataa, FuncSettingsBean.class);
                    if (funcSettingsBean.getResult().equals("success")) {
                        if (funcSettingsBean.getData().getFuncSettings().size() != 0) {
                            String funcCode = funcSettingsBean.getData().getFuncSettings().get(0).getFuncCode();
                            boolean funcSwitch = funcSettingsBean.getData().getFuncSettings().get(0).getFuncSwitch();
                            if (funcCode.equals("f_send_push")) {
                                if (funcSwitch) {
                                    switch2.setChecked(true);
                                } else {
                                    switch2.setChecked(false);
                                }
                            } else {
                                change(1, "f_send_push");
                            }
                        }
                    }
                    break;
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    Log.d(TAG, "Set tags in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;
            }

        }
    };
    private UISwitchButton switch2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_set);

        init();
        getCacheSize();
        searchData();

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                String funcCode = "f_send_push";
                if (isChecked) {
                    //设置Jpush的标签为可接受
                    LinkedHashSet<String> set = new LinkedHashSet<String>();
                    set.add("f_on_push");
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, set, mTagsCallback);
                    int funcSwitch = 1;
                    change(funcSwitch, funcCode);
                } else {
                    //设置Jpush的标签为不可接受
                    LinkedHashSet<String> set = new LinkedHashSet<String>();
                    set.add("f_off_push");
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, set, mTagsCallback);
                    int funcSwitch = 0;
                    change(funcSwitch, funcCode);
                }

            }
        });
    }

    private void searchData() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(SetActivity.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {
            json.put("funcCode", "f_send_push");

        } catch (Exception e) {

            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.GETFUNCSETTING)
                .post(body)
                .build();


        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SetActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SetActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.obj = result;
                handler.sendMessage(msg);

            }
        });


    }


    protected void change(int funcSwitch, String funcCode) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(SetActivity.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {
            json.put("funcSwitch", funcSwitch);
            json.put("funcCode", funcCode);

        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.SETFUNCSETTING)
                .post(body)
                .build();


        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SetActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SetActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                Log.e("设置过后", result);

            }
        });
    }


    private void cleanCache() {

        cachesize.setText("清空图片缓存(0.0Byte)");
        DataCleanManager.cleanDatabases(UIUtils.getContext());
        DataCleanManager.cleanExternalCache(UIUtils.getContext());
        DataCleanManager.cleanInternalCache(UIUtils.getContext());

        Toast.makeText(getApplicationContext(), "清理成功", Toast.LENGTH_SHORT).show();

    }

    private void getCacheSize() {
        // 获取缓存目录大小。

        File cacheDir = getCacheDir();

        String cacheSize2 = null;
        try {
            cacheSize2 = DataCleanManager.getCacheSize(cacheDir);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cachesize.setText("清空图片缓存(" + cacheSize2 + ")");
        // 如果这里需要格式化就格式化
    }

    private void init() {
        switch2 = (com.jiahehongye.robred.view.UISwitchButton) findViewById(R.id.switch2);
        mBack = (RelativeLayout) findViewById(R.id.setting_rl_back);
        account_safe = (LinearLayout) findViewById(R.id.setting_ll_account_safe);
        help_center = (LinearLayout) findViewById(R.id.setting_ll_help_center);
        about_us = (LinearLayout) findViewById(R.id.setting_ll_aboutus);
        suggestion = (LinearLayout) findViewById(R.id.setting_ll_suggestion);
        cachesize = (TextView) findViewById(R.id.cachesize);
        clean_ = (LinearLayout) findViewById(R.id.clean_);
        LinearLayout log_out = (LinearLayout) findViewById(R.id.setting_ll_logout);

        mBack.setOnClickListener(this);
        account_safe.setOnClickListener(this);
        help_center.setOnClickListener(this);
        about_us.setOnClickListener(this);
        suggestion.setOnClickListener(this);
        log_out.setOnClickListener(this);
        clean_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanCache();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_rl_back:
                finish();
                break;

            case R.id.setting_ll_help_center:
                startActivity(new Intent(this, HelpCenterActivity.class));
                break;

            case R.id.setting_ll_account_safe:
                startActivity(new Intent(this, AccountSafeActivity.class));
                break;

            case R.id.setting_ll_aboutus:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;

            case R.id.setting_ll_suggestion:
                startActivity(new Intent(this, FeedBackActivity.class));
                break;

            case R.id.setting_ll_logout:
                clearAndLogout();
                break;
        }
    }

    private void clearAndLogout() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(SetActivity.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.SETTING_LOGOUT)
                .post(body)
                .build();


        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SetActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SetActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = LOGOUT_SUCCESS;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }


    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (NetworkAvailable.isNetworkAvailable(getApplicationContext())) {
                        handler.sendMessageDelayed(handler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

//            RegisterFragment.showToast(logs, getApplicationContext());
        }

    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (NetworkAvailable.isNetworkAvailable(getApplicationContext())) {
                        handler.sendMessageDelayed(handler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

//            RegisterFragment.showToast(logs, getApplication());
        }

    };
}

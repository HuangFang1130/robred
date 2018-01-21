package com.jiahehongye.robred.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.jiahehongye.robred.AppHelper;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.MainActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.application.BaseApplication;
import com.jiahehongye.robred.bean.LoginResult;
import com.jiahehongye.robred.bean.ThridLoginResult;
import com.jiahehongye.robred.bean.ThridOpenIdResult;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.utils.DesUtil;
import com.jiahehongye.robred.utils.LogUtil;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.ThreadManager;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.MyProgressDialog;
import com.songheng.newsapisdk.sdk.global.DFTTSdk;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by huangjunhui on 2016/12/9.9:50
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {


    private static final int RESULT_SUCCESS_QQ = 203;
    private static final int RESULT_SUCCESS_WECHAT = 204;
    private static final int RESULT_SUCCESS = 201;
    private static final int SEND_SUCCESS = 202;
    private static final int GET_PERSONAL_INFORMATION_OK = 205;

    private String openid;
    private String password;
    private String encryptPassword;
    private UMShareAPI shareApi;
    private String mediaType;
    private String userName;
    private String headAvatar;
    private String screen_name;

    private static final String TAG = "LoginActivity";
    private Button mBtnRegister;//注册按钮
    private Button mBtnLogin;  //登录按钮
    private EditText mEtPhone;//用户名
    private EditText mEtPassword;//用户密码
    private PersistentCookieStore persistentCookieStore;
    private MyProgressDialog animDialog;
    private Call call;
    private static final int LOGIN_OK = 0000;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_OK://登录成功
                    animDialog.dismiss();
                    String login = (String) msg.obj;

                    LogUtil.LogShitou("登录返回信息：",login.toString());
                    LoginResult loginResult = new Gson().fromJson(login, LoginResult.class);
                    if (loginResult.getResult().equals(Constant.SUCCESS)) {
                        saveInfor(loginResult);
                        AppHelper.getInstance().registerId(BaseApplication.registrationId);
                        SPUtils.put(UIUtils.getContext(),Constant.IS_LOGIN,true);
                        SPUtils.put(UIUtils.getContext(),Constant.IS_LOGIN,true);
                        loginChat();
                    } else {
                        animDialog.dismiss();
                        Toast.makeText(UIUtils.getContext(), "登录失败！", Toast.LENGTH_SHORT).show();
                    }

                    break;

                case RESULT_SUCCESS://微博登录成功返回
                    Map<String, Object> result3 = (Map<String, Object>) msg.obj;
                    headAvatar = result3.get("iconurl").toString();
                    screen_name = result3.get("name").toString();
                    openid = result3.get("uid").toString();
                    LogUtil.LogShitou(headAvatar + ",  " + screen_name + ", " + openid);
                    initLoad(openid, headAvatar, screen_name, mediaType);

                    break;

                case SEND_SUCCESS://发送成功
                    String result = (String) msg.obj;
                    Gson gson = new Gson();
                    ThridOpenIdResult openIdBean = gson.fromJson(result, ThridOpenIdResult.class);

                    if (openIdBean.getResult().equals("fail")) { // 打开注册消息
                        Intent intent = new Intent(LoginActivity.this, ThridLoginActivity.class);
                        intent.putExtra("headpicc", headAvatar);
                        intent.putExtra("name", screen_name);
                        intent.putExtra("openId", openid);
                        intent.putExtra("mediaType", mediaType);
                        startActivity(intent);
                        finish();
                    } else if (openIdBean.getResult().equals("success")) {//获取个人信息
                        getPersonalInfor();
                    }
                    break;

                case RESULT_SUCCESS_QQ:

                    Map<String, Object> result2 = (Map<String, Object>) msg.obj;
                    headAvatar = result2.get("iconurl").toString();
                    screen_name = result2.get("name").toString();
                    openid = result2.get("uid").toString();
                    initLoad(openid, headAvatar, screen_name, mediaType);
                    break;

                case RESULT_SUCCESS_WECHAT:

                    Map<String, Object> result1 = (Map<String, Object>) msg.obj;
                    headAvatar = result1.get("iconurl").toString();
                    screen_name = result1.get("name").toString();
                    openid = result1.get("openid").toString();
                    initLoad(openid, headAvatar, screen_name, mediaType);
                    break;

                case GET_PERSONAL_INFORMATION_OK:
                    String Infodata = (String) msg.obj;
                    ThridLoginResult thridLoginResult = new Gson().fromJson(Infodata, ThridLoginResult.class);
                    if ("success".equals(thridLoginResult.getResult())) {
                        saveInfor(thridLoginResult);
                        SPUtils.put(UIUtils.getContext(),Constant.IS_LOGIN,true);
                        AppHelper.getInstance().registerId(BaseApplication.registrationId);
                        loginChat();
                    } else {

                    }

                    break;
            }
        }
    };
    private boolean exitSuccess;

    private void saveInfor(ThridLoginResult thridLoginResult) {
        String id = thridLoginResult.getData().getId(); //唯一标示
        String memberRank = thridLoginResult.getData().getMemberRank();//会员等级
        String memberType = thridLoginResult.getData().getMemberType();//会员类型
        String mobile = thridLoginResult.getData().getMobile();//会员手机
        String redIntegral = thridLoginResult.getData().getRedIntegral();//积分

        userName = mobile;
        SPUtils.put(UIUtils.getContext(), Constant.LOGIN_ID, id);
        DFTTSdk.getInstance().setUserId(id);
        SPUtils.put(UIUtils.getContext(), Constant.LOGIN_MEMBERRANK, memberRank);
        SPUtils.put(UIUtils.getContext(), Constant.LOGIN_MEMBERTYPE, memberType);
        SPUtils.put(UIUtils.getContext(), Constant.LOGIN_MOBILE, mobile);
        SPUtils.put(UIUtils.getContext(), Constant.LOGIN_REDINTEGRAL, redIntegral);

    }

    /**
     * 从cookie里面取出用户的信息
     */
    private void getPersonalInfor() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getApplicationContext());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();
        JSONObject json = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.GET_PERSONAL_INFORMATION)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = GET_PERSONAL_INFORMATION_OK;
                msg.obj = result;
                handler.sendMessage(msg);
                LogUtil.LogShitou(result);
            }
        });
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        setContentView(R.layout.activity_login);
        initView();
        shareApi = UIUtils.getShareApi();
        exitSuccess = getIntent().getBooleanExtra(Constant.EXIT_SUCCESS, false);
    }




    /**
     * 初始化view
     */
    private void initView() {
        mBtnLogin = (Button) findViewById(R.id.login_bt_login);
        mBtnRegister = (Button) findViewById(R.id.login_bt_register);
        mEtPhone = (EditText) findViewById(R.id.login_et_phone);
        mEtPassword = (EditText) findViewById(R.id.login_et_password);
        ImageView mIvClose = (ImageView) findViewById(R.id.login_iv_close);
        TextView mTvForgetPassword = (TextView) findViewById(R.id.login_tv_forget_password);
//        RadioGroup mRgThirdLogin = (RadioGroup) findViewById(R.id.login_rg_root);
        RadioButton mRbTencent = (RadioButton) findViewById(R.id.login_rb_tencent);
        RadioButton mRbWeChat = (RadioButton) findViewById(R.id.login_rb_wechat);
        RadioButton mRbWeiBo = (RadioButton) findViewById(R.id.login_rb_weibo);


        boolean isLogOut = (boolean) SPUtils.get(UIUtils.getContext(), Constant.LOGOUT, false);
        if(!isLogOut){
            String mobile = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_MOBILE, "");
            String password = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_PASSWORD, "");
            mEtPhone.setText(mobile);
            mEtPassword.setText(password);
        }


        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        mTvForgetPassword.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mRbTencent.setOnClickListener(this);
        mRbWeChat.setOnClickListener(this);
        mRbWeiBo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_bt_login://登录
                login();
                break;

            case R.id.login_bt_register://注册
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                registerIntent.putExtra("isThird", false);
                startActivity(registerIntent);
                break;

            case R.id.login_tv_forget_password://忘记密码
                Intent forgetIntent = new Intent(this, ForgetPasswordActivity.class);
                startActivity(forgetIntent);
                break;

            case R.id.login_iv_close:
                finish();
                break;

            case R.id.login_rb_tencent://腾讯qq
                mediaType = "2";
                shareApi.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                        if (data != null) {
                            StringBuilder sb = new StringBuilder();
                            Set<String> keySet = data.keySet();
                            for (String key : keySet) {
                                Object object = data.get(key);
                                sb.append(key + "=" + data.get(key));
                                System.out.println(key + "+++++++" + object + "\r\n");
                            }

                            Message msg = new Message();
                            msg.what = RESULT_SUCCESS_QQ;
                            msg.obj = data;
                            handler.sendMessage(msg);

                        }

                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                        Toast.makeText(LoginActivity.this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform, int action) {
                        Toast.makeText(LoginActivity.this, "取消了", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.login_rb_wechat://微信
                mediaType = "1";
                shareApi.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                        if (data != null) {
                            StringBuilder sb = new StringBuilder();
//                            Toast.makeText(LoginActivity.this, "微信返会有数据", Toast.LENGTH_SHORT).show();
                            Set<String> keySet = data.keySet();
                            for (String key : keySet) {
                                Object object = data.get(key);
                                sb.append(key + "=" + data.get(key));
                                LogUtil.LogShitou("key",sb.toString());

                            }

                            LogUtil.LogShitou("微信登录返回：",sb.toString());
                            Message msg = new Message();
                            msg.what = RESULT_SUCCESS_WECHAT;
                            msg.obj = data;
                            handler.sendMessage(msg);

                        }else {
//                            Toast.makeText(LoginActivity.this, "微信返回null", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                        Toast.makeText(LoginActivity.this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform, int action) {
                        Toast.makeText(LoginActivity.this, "取消了", Toast.LENGTH_LONG).show();
                    }
                });

                break;
            case R.id.login_rb_weibo://微博
                mediaType = "3";
                shareApi.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.SINA, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                        if (data != null) {
                            StringBuilder sb = new StringBuilder();
                            Set<String> keySet = data.keySet();
                            for (String key : keySet) {
                                Object object = data.get(key);
                                sb.append(key + "=" + data.get(key));
                                System.out.println(key + "+++++++" + object + "\r\n");
                            }

                            Message msg = new Message();
                            msg.what = RESULT_SUCCESS;
                            msg.obj = data;
                            handler.sendMessage(msg);

                        }

                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                        Toast.makeText(LoginActivity.this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform, int action) {
                        Toast.makeText(LoginActivity.this, "取消了", Toast.LENGTH_LONG).show();
                    }
                });

                break;

        }
    }


    /**
     * 登录操作
     */
    private void login() {
        userName = mEtPhone.getText().toString().trim();
        password = mEtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "请输入用户名！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请输入密码！", Toast.LENGTH_SHORT).show();
            return;
        }

        showMyDialog();

        encryptPassword = DesUtil.encrypt(password);
        LogUtil.LogShitou(encryptPassword);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getApplicationContext());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", userName);
            jsonObject.put("password", encryptPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.LOGIN_URL)
                .post(body)
                .build();

        Log.e("登陆请求：", "mobile=" + userName + ",password=" + encryptPassword);

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        if (animDialog != null) {
                            animDialog.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = LOGIN_OK;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });

    }

    /**
     * 显示对话框
     */
    public void showMyDialog() {
        animDialog = new MyProgressDialog(this, "玩命加载中...", R.drawable.loading);
        animDialog.show();
        animDialog.setCancelable(true);
        animDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (call.isExecuted()) {
                    call.cancel();
                }
            }
        });
    }

    //登录环信
    private void loginChat() {
        ThreadManager.getLongPool().execute(new Runnable() {
            @Override
            public void run() {
                if (EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().getCurrentUser().equals(userName)) {
                    EMClient.getInstance().chatManager().loadAllConversations();
                    Log.d("main", "isLoggedInBefore登录聊天服务器成功！");
                    if(exitSuccess){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    finish();
                    return;
                }

                EMClient.getInstance().login(userName, "admin123", new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        Log.d("main", "登录聊天服务器成功！");
                        if (animDialog != null) {
                            animDialog.dismiss();
                        }
                        if(exitSuccess){
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        finish();
                        return;
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(final int code, final String message) {
                        Log.d("main", "登录聊天服务器失败！");
                        AppHelper.getInstance().logoutHuanXin();
                        if (animDialog != null) {
                            animDialog.dismiss();
                        }
                        if(exitSuccess){
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    }
                });
            }
        });
    }

    /**
     * 保存用户信息
     *
     * @param loginResult
     */
    private void saveInfor(LoginResult loginResult) {
        String id = loginResult.getData().getId(); //唯一标示
        String memberRank = loginResult.getData().getMemberRank();//会员等级
        String memberType = loginResult.getData().getMemberType();//会员类型
        String mobile = loginResult.getData().getMobile();//会员手机
        String redIntegral = loginResult.getData().getRedIntegral();//积分

        SPUtils.put(UIUtils.getContext(), Constant.LOGIN_ID, id);
        SPUtils.put(UIUtils.getContext(), Constant.LOGIN_MEMBERRANK, memberRank);
        SPUtils.put(UIUtils.getContext(), Constant.LOGIN_MEMBERTYPE, memberType);
        SPUtils.put(UIUtils.getContext(), Constant.LOGIN_MOBILE, mobile);
        SPUtils.put(UIUtils.getContext(), Constant.LOGIN_REDINTEGRAL, redIntegral);

        SPUtils.put(UIUtils.getContext(), Constant.LOGIN_PASSWORD, password);



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    /**
     * 发送openid给服务器
     *
     * @param openId
     * @param headpicc
     * @param screen_name
     * @param mediaType
     */
    public void initLoad(String openId, String headpicc, String screen_name, String mediaType) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getApplicationContext());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {
            json.put("openId", openId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.SEND_OPENID)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                Message msg = handler.obtainMessage();
                msg.what = SEND_SUCCESS;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
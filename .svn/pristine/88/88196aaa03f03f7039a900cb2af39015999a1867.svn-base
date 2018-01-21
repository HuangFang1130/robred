package com.jiahehongye.robred.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.ThridOpenIdResult;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.utils.DesUtil;
import com.jiahehongye.robred.utils.LogUtil;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.MyProgressDialog;
import com.jiahehongye.robred.view.TimeButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by huangjunhui on 2016/12/9.11:19
 */
public class RegisterReceiverVerificyActivity extends BaseActivity implements View.OnClickListener {

    private static final int REGISTER_SUCCESS = 200;
    private static final int REGISTER_GENEROL_SUCCESS = 202;
    private EditText mEtVerificyCode;
    private EditText mEtVerificyPassword;
    private TimeButton mTvTimer;
    private TextView mTvPhone;
    private String phone;
    private String mediaType;
    private String openId;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REGISTER_SUCCESS://第三方注册
                    String result = (String) msg.obj;

                    Gson gson = new Gson();
                    ThridOpenIdResult thridOpenIdBean = gson.fromJson(result, ThridOpenIdResult.class);

                    if (thridOpenIdBean.getResult().equals("success")) {//注册成功去登录
                        Intent intent = new Intent(RegisterReceiverVerificyActivity.this, LoginActivity.class);
                        SPUtils.put(UIUtils.getContext(), Constant.LOGIN_MOBILE, phone);
                        SPUtils.put(UIUtils.getContext(), Constant.LOGIN_PASSWORD, password);
                        startActivity(intent);

                    } else if (thridOpenIdBean.getResult().equals("fail")) {
                        Toast.makeText(RegisterReceiverVerificyActivity.this, thridOpenIdBean.getErrorMsg(), Toast.LENGTH_SHORT).show();
                    }
                    if(animDialog!=null){
                        animDialog.dismiss();
                    }

                    break;

                case REGISTER_GENEROL_SUCCESS://普通注册
                    String generolResult = (String) msg.obj;
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(generolResult);
                        String data = jsonObject.getString("result");
                        if (data.equals("success")) {//注册成功去登录
                            Toast.makeText(RegisterReceiverVerificyActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterReceiverVerificyActivity.this, LoginActivity.class);
                            SPUtils.put(UIUtils.getContext(), Constant.LOGIN_MOBILE, phone);
                            SPUtils.put(UIUtils.getContext(), Constant.LOGIN_PASSWORD, password);
                            startActivity(intent);

                        } else if (data.equals("fail")) {
                            String errMsg = jsonObject.getString("errorMsg");
                            Toast.makeText(RegisterReceiverVerificyActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                        }
                        if(animDialog!=null){
                            animDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

            }
        }
    };
    private String password;
    private boolean isThird;
    private MyProgressDialog animDialog;
    private Call call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        setContentView(R.layout.activity_register_receiver_verificy);
        Intent intent = getIntent();
        isThird = intent.getBooleanExtra("isThird", false);
        phone = intent.getStringExtra("phone");
        if (isThird) {
            mediaType = intent.getStringExtra("mediaType");
            openId = intent.getStringExtra("openId");
        }


        initView();
    }

    /**
     * 获取验证码
     * -->向服务器发送短信验证码
     *
     * @param phone
     */
    private void requestCode(String phone) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(getApplicationContext());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {
            json.put("mobile", phone);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.MOBILECODE_URL)
                .post(body)
                .build();


        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RegisterReceiverVerificyActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterReceiverVerificyActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    /**
     * 初始化操作
     */
    private void initView() {
        ImageView mIvBack = (ImageView) findViewById(R.id.register_verificy_iv_colse);
        mEtVerificyCode = (EditText) findViewById(R.id.register_verificy_et_code);
        mEtVerificyPassword = (EditText) findViewById(R.id.register_verificy_et_password);
        mTvTimer = (TimeButton) findViewById(R.id.register_verificy_tv_timer);//倒计时
        Button mBtnRegister = (Button) findViewById(R.id.register_verificy_btn_register);
        mTvPhone = (TextView) findViewById(R.id.register_verificy_tv_phone);

        mTvPhone.setText("+86  " + phone);
        mIvBack.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        mTvTimer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_verificy_iv_colse:
                finish();
                break;
            case R.id.register_verificy_btn_register://注册
                register();
                break;

            case R.id.register_verificy_tv_timer://倒计时开始并且发送验证码
                requestCode(phone);
                break;
        }
    }

    /**
     * 注册的方法
     */
    private void register() {
        password = mEtVerificyPassword.getText().toString().trim();
        if(!isPassword(password)){
            Toast.makeText(RegisterReceiverVerificyActivity.this, "密码格式不正确！", Toast.LENGTH_SHORT).show();
            return;
        }

        String encryptPassword = DesUtil.encrypt(password);//对密码进行加密
        String code = mEtVerificyCode.getText().toString().trim();
        showMyDialog();
        if (isThird) {
            registerToServerWithThird(phone, encryptPassword, code);
        } else {
            registerToServerWithGenerol(phone, encryptPassword, code);
        }

    }

    /**
     * 显示对话框
     */
    public void showMyDialog() {
        animDialog = new MyProgressDialog(RegisterReceiverVerificyActivity.this, "玩命加载中...", R.drawable.loading);
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
    private void registerToServerWithGenerol(String phone, String password, String code) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(getApplicationContext());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {
            json.put("mobile", phone);
            json.put("password", password);
            json.put("memberType", 0+"");
            json.put("checkCode", code);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.REGISTER_URL)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RegisterReceiverVerificyActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterReceiverVerificyActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        if(animDialog!=null){
                            animDialog.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = REGISTER_GENEROL_SUCCESS;
                msg.obj = result;
                handler.sendMessage(msg);
                LogUtil.LogShitou(result.toString());
            }
        });
    }

    /**
     * 第三方登录的时候的注册方法
     *
     * @param tel1
     * @param pwd1
     * @param code
     */
    private void registerToServerWithThird(final String tel1, final String pwd1, String code) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(getApplicationContext());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {
            json.put("mobile", tel1);
            json.put("password", pwd1);
            json.put("checkCode", code);
            json.put("memberType", 0 + "");
            json.put("mediaType", mediaType);
            json.put("openId", openId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.BIND_OPENID_TOSERVER)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RegisterReceiverVerificyActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterReceiverVerificyActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        animDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = REGISTER_SUCCESS;
                msg.obj = result;
                handler.sendMessage(msg);
                LogUtil.LogShitou(result.toString());
            }
        });

    }
}

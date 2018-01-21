package com.jiahehongye.robred.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
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

public class ChangePhoneOkActivity extends BaseActivity implements View.OnClickListener {

    private PersistentCookieStore persistentCookieStore;
    private Call call;
    private static final int GET_ALL = 0000;
    private static final int GET_RESULT = 0001;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_ALL:
                    String login = (String) msg.obj;
                    Log.e("ckcode",login);
                    try {
                        JSONObject object = new JSONObject(login);
                        if (object.getString("result").equals("success")){
                            Toast.makeText(ChangePhoneOkActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ChangePhoneOkActivity.this, "网络繁忙", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case GET_RESULT:
                    String result = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getString("result").equals("success")){
                            Toast.makeText(ChangePhoneOkActivity.this, "更改成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(ChangePhoneOkActivity.this, "网络繁忙", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    private String mobile = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.white);
        setContentView(R.layout.activity_change_phone_ok);

        initView();
    }

    private RelativeLayout mBack;
    private RelativeLayout mChangeCity;
    private TextView city_number,city_name;
    private EditText mPhoneNumber,mCode;
    private TimeButton mGetCode;
    private Button mOk;
    private void initView() {
        mBack = (RelativeLayout) findViewById(R.id.changephoneok_rl_back);
        mChangeCity = (RelativeLayout) findViewById(R.id.changephoneok_rl_city);
        city_number = (TextView) findViewById(R.id.changephoneok_tv_city_number);
        city_name = (TextView) findViewById(R.id.changephoneok_tv_city_name);
        mPhoneNumber = (EditText) findViewById(R.id.changephoneok_et_phonenumber);
        mCode = (EditText) findViewById(R.id.changephoneok_et_code);
        mGetCode = (TimeButton) findViewById(R.id.changephoneok_tv_getcode);
        mOk = (Button) findViewById(R.id.changephoneok_btn_ok);

        mBack.setOnClickListener(this);
        mChangeCity.setOnClickListener(this);
        mGetCode.setOnClickListener(this);
        mOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.changephoneok_rl_back:
                finish();
                break;
            case R.id.changephoneok_rl_city:
                break;
            case R.id.changephoneok_tv_getcode:
                mobile = mPhoneNumber.getText().toString();
                if (!BaseActivity.isMobileNO(mobile)){
                    Toast.makeText(ChangePhoneOkActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    mGetCode.setTextAfter("s").setLenght(0 * 1000);
                    return;
                }
                if (mobile.length()!=11){
                    Toast.makeText(ChangePhoneOkActivity.this, "请输入11位手机号", Toast.LENGTH_SHORT).show();
                    mGetCode.setTextAfter("s").setLenght(0 * 1000);
                    return;
                }
                mGetCode.setTextAfter("s").setLenght(60 * 1000);
                rCode(mobile);
                break;
            case R.id.changephoneok_btn_ok:
                mobile = mPhoneNumber.getText().toString();
                if (!BaseActivity.isMobileNO(mobile)){
                    Toast.makeText(ChangePhoneOkActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mobile.length()!=11){
                    Toast.makeText(ChangePhoneOkActivity.this, "请输入11位手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mCode.getText().toString().trim())){
                    Toast.makeText(ChangePhoneOkActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                getResult();
                break;
        }
    }

    private void getResult() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(ChangePhoneOkActivity.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();


        try {
            json.put("newMobile", mobile);
            json.put("checkCode", mCode.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.MODIFYMOBILENUMBER)
                .post(body)
                .build();



        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ChangePhoneOkActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChangePhoneOkActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                Message msg = handler.obtainMessage();
                msg.what = GET_RESULT;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }


    protected void rCode(String mobile) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(ChangePhoneOkActivity.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();


        try {
            json.put("mobile", mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.MOBILECODE_URL)
                .post(body)
                .build();



        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ChangePhoneOkActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChangePhoneOkActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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
}

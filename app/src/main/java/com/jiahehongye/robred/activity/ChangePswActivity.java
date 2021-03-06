package com.jiahehongye.robred.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.utils.DesUtil;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangePswActivity extends BaseActivity implements View.OnClickListener {

    private PersistentCookieStore persistentCookieStore;
    private Call call;
    private static final int GET_ALL = 0000;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_ALL:
                    String login = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(login);
                        if (object.getString("result").equals("success")){
                            Toast.makeText(ChangePswActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(ChangePswActivity.this, object.getString("errorMsg").toString(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };
    private String mobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_change_psw);

        initView();
        mobile = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_MOBILE,"");
    }

    private RelativeLayout mBack;
    private EditText et_old,et_new,et_renew;
    private RelativeLayout mForgetPsw;
    private Button mOk;
    private void initView() {
        mBack = (RelativeLayout) findViewById(R.id.changepsw_rl_back);
        et_old = (EditText) findViewById(R.id.changepsw_et_old);
        et_new = (EditText) findViewById(R.id.changepsw_et_new);
        et_renew = (EditText) findViewById(R.id.changepsw_et_renew);
        mForgetPsw = (RelativeLayout) findViewById(R.id.changepsw_rl_forgetpsw);
        mOk = (Button) findViewById(R.id.changepsw_btn_ok);

        mBack.setOnClickListener(this);
        mForgetPsw.setOnClickListener(this);
        mOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.changepsw_rl_back:
                finish();
                break;
            case R.id.changepsw_rl_forgetpsw:
                startActivity(new Intent(this,ForgetPasswordActivity.class));
                break;
            case R.id.changepsw_btn_ok:
                if (TextUtils.isEmpty(et_old.getText())){
                    Toast.makeText(ChangePswActivity.this, "请输入当前密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(et_new.getText())){
                    Toast.makeText(ChangePswActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(et_renew.getText())){
                    Toast.makeText(ChangePswActivity.this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!et_new.getText().toString().equals(et_renew.getText().toString())){
                    Toast.makeText(ChangePswActivity.this, "两次新密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!BaseActivity.isPassword(et_new.getText().toString())){
                    Toast.makeText(ChangePswActivity.this, "请输入6-18位的英文和数字组合", Toast.LENGTH_SHORT).show();
                    return;
                }
                String oldDES = DesUtil.encrypt(et_old.getText().toString());
                String newDES = DesUtil.encrypt(et_new.getText().toString());
                initData(oldDES,newDES);
                break;
        }
    }

    private void initData(String oldDES,String newDES) {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(ChangePswActivity.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();


        try {
            json.put("mobile", mobile);
            json.put("password", oldDES);
            json.put("newPassword",newDES);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.MODIFYPASSWORD_URL)
                .post(body)
                .build();



        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ChangePswActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChangePswActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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

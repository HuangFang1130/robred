package com.jiahehongye.robred.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangeNickName extends BaseActivity {

    private String name;
    private EditText nickname;
    private TextView changenickname_ok;
    private ImageView changenickname_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_change_nick_name);
        changenickname_back = (ImageView) findViewById(R.id.changenickname_back);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            name = bundle.getString("nickname");
        }

        changenickname_ok = (TextView) findViewById(R.id.changenickname_ok);
        nickname = (EditText) findViewById(R.id.nickname);

        nickname.setText(name);

        changenickname_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata();
            }
        });
        changenickname_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    /**
     *
     */
    private PersistentCookieStore persistentCookieStore;
    private Call call;

    private void getdata() {
        if (TextUtils.isEmpty(nickname.getText())){
            Toast.makeText(ChangeNickName.this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(ChangeNickName.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();



        RequestBody formBody = new FormBody.Builder()
                .add("nickName", nickname.getText().toString()).build();

        Request request = new Request.Builder()
//                .addHeader("Content-Type", "application/json;charset=utf-8")
                .url(Constant.CHANGE_MEMBER_INFO)

                .post(formBody)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ChangeNickName.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChangeNickName.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject object = new JSONObject(result);
                    String s = object.getString("result");
                    if (s.equals("success")){
                        ChangeNickName.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChangeNickName.this, "修改成功", Toast.LENGTH_SHORT).show();
                                hideSoftKeyboard();
                                finish();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}

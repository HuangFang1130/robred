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

public class ChangeGexing extends BaseActivity {

    private String gexing;
    private TextView ok;
    private EditText edit;
    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_change_gexing);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            gexing = bundle.getString("gexing");
        }

        ok = (TextView) findViewById(R.id.changegexing_ok);
        edit = (EditText) findViewById(R.id.gexing);
        back = (ImageView) findViewById(R.id.changegexing_back);

        edit.setText(gexing);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata();
            }
        });
    }

    /**
     *
     */
    private PersistentCookieStore persistentCookieStore;
    private Call call;

    private void getdata() {
        if (TextUtils.isEmpty(edit.getText())){
            Toast.makeText(ChangeGexing.this, "请输入个性签名", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(ChangeGexing.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();



        RequestBody formBody = new FormBody.Builder()
                .add("personalDescription", edit.getText().toString()).build();

        Request request = new Request.Builder()
                .url(Constant.CHANGE_MEMBER_INFO)

                .post(formBody)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ChangeGexing.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChangeGexing.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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
                        ChangeGexing.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChangeGexing.this, "修改成功", Toast.LENGTH_SHORT).show();
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

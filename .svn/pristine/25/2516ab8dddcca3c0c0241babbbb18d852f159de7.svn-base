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

public class ChangeJob extends BaseActivity {

    private String job;
    private TextView ok;
    private EditText edit;
    private ImageView changenickjob_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_change_job);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            job = bundle.getString("job");
        }

        ok = (TextView) findViewById(R.id.changejob_ok);
        edit = (EditText) findViewById(R.id.job);

        edit.setText(job);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata();
            }
        });
        changenickjob_back = (ImageView) findViewById(R.id.changenickjob_back);
        changenickjob_back.setOnClickListener(new View.OnClickListener() {
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
        if (TextUtils.isEmpty(edit.getText())){
            Toast.makeText(ChangeJob.this, "请输入职业", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(ChangeJob.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();



        RequestBody formBody = new FormBody.Builder()
                .add("profession", edit.getText().toString()).build();

        Request request = new Request.Builder()
//                .addHeader("Content-Type", "application/json;charset=utf-8")
                .url(Constant.CHANGE_MEMBER_INFO)

                .post(formBody)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ChangeJob.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChangeJob.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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
                        ChangeJob.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChangeJob.this, "修改成功", Toast.LENGTH_SHORT).show();
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

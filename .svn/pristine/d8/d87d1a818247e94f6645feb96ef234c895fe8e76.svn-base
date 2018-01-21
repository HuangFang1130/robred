package com.jiahehongye.robred.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

/**
 * Created by huangjunhui on 2016/12/5.15:00
 */
public class ContanctReportActivty extends BaseActivity implements View.OnClickListener {

    private RelativeLayout r1,r2,r3,r4,r5,r6,r7,r8;
    private ImageView c1,c2,c3,c4,c5,c6,c7,c8;
    private String TYPE;
    private ImageView report_iv_back;
    private String id;
    private Button report_btn_submit;
    private Call call;
    private PersistentCookieStore persistentCookieStore;
    private String reason;
    private EditText report_et_problem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_contanct_report);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            TYPE  = bundle.getString("type");
            id = bundle.getString("id");
        }
        init();

    }

    private void init() {
        report_et_problem = (EditText) findViewById(R.id.report_et_problem);
        report_btn_submit = (Button) findViewById(R.id.report_btn_submit);
        report_iv_back = (ImageView) findViewById(R.id.report_iv_back);
        r1 = (RelativeLayout) findViewById(R.id.jubao1);
        r2 = (RelativeLayout) findViewById(R.id.jubao2);
        r3 = (RelativeLayout) findViewById(R.id.jubao3);
        r4 = (RelativeLayout) findViewById(R.id.jubao4);
        r5 = (RelativeLayout) findViewById(R.id.jubao5);
        r6 = (RelativeLayout) findViewById(R.id.jubao6);
        r7 = (RelativeLayout) findViewById(R.id.jubao7);
        r8 = (RelativeLayout) findViewById(R.id.jubao8);
        c1 = (ImageView) findViewById(R.id.jubaocheck1);
        c2 = (ImageView) findViewById(R.id.jubaocheck2);
        c3 = (ImageView) findViewById(R.id.jubaocheck3);
        c4 = (ImageView) findViewById(R.id.jubaocheck4);
        c5 = (ImageView) findViewById(R.id.jubaocheck5);
        c6 = (ImageView) findViewById(R.id.jubaocheck6);
        c7 = (ImageView) findViewById(R.id.jubaocheck7);
        c8 = (ImageView) findViewById(R.id.jubaocheck8);

        r1.setOnClickListener(this);
        r2.setOnClickListener(this);
        r3.setOnClickListener(this);
        r4.setOnClickListener(this);
        r5.setOnClickListener(this);
        r6.setOnClickListener(this);
        r7.setOnClickListener(this);
        r8.setOnClickListener(this);
        report_iv_back.setOnClickListener(this);
        report_btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.report_btn_submit:
                JUBAO();
                break;
            case R.id.report_iv_back:
                finish();
                break;

            case R.id.jubao1:
                reason = getString(R.string.report_tv_sex);
                c1.setSelected(true);
                c2.setSelected(false);
                c3.setSelected(false);
                c4.setSelected(false);
                c5.setSelected(false);
                c6.setSelected(false);
                c7.setSelected(false);
                c8.setSelected(false);
                break;
            case R.id.jubao2:
                reason = getString(R.string.report_tv_infor);
                c1.setSelected(false);
                c2.setSelected(true);
                c3.setSelected(false);
                c4.setSelected(false);
                c5.setSelected(false);
                c6.setSelected(false);
                c7.setSelected(false);
                c8.setSelected(false);
                break;
            case R.id.jubao3:
                reason = getString(R.string.report_tv_minzhu);
                c1.setSelected(false);
                c2.setSelected(false);
                c3.setSelected(true);
                c4.setSelected(false);
                c5.setSelected(false);
                c6.setSelected(false);
                c7.setSelected(false);
                c8.setSelected(false);
                break;
            case R.id.jubao4:
                reason = getString(R.string.report_tv_guanggao);
                c1.setSelected(false);
                c2.setSelected(false);
                c3.setSelected(false);
                c4.setSelected(true);
                c5.setSelected(false);
                c6.setSelected(false);
                c7.setSelected(false);
                c8.setSelected(false);
                break;
            case R.id.jubao5:
                reason = getString(R.string.report_tv_yaoyan);
                c1.setSelected(false);
                c2.setSelected(false);
                c3.setSelected(false);
                c4.setSelected(false);
                c5.setSelected(true);
                c6.setSelected(false);
                c7.setSelected(false);
                c8.setSelected(false);
                break;
            case R.id.jubao6:
                reason = getString(R.string.report_tv_gongji);
                c1.setSelected(false);
                c2.setSelected(false);
                c3.setSelected(false);
                c4.setSelected(false);
                c5.setSelected(false);
                c6.setSelected(true);
                c7.setSelected(false);
                c8.setSelected(false);
                break;
            case R.id.jubao7:
                reason = getString(R.string.report_tv_lie);
                c1.setSelected(false);
                c2.setSelected(false);
                c3.setSelected(false);
                c4.setSelected(false);
                c5.setSelected(false);
                c6.setSelected(false);
                c7.setSelected(true);
                c8.setSelected(false);
                break;
            case R.id.jubao8:
                reason = getString(R.string.report_tv_lungune);
                c1.setSelected(false);
                c2.setSelected(false);
                c3.setSelected(false);
                c4.setSelected(false);
                c5.setSelected(false);
                c6.setSelected(false);
                c7.setSelected(false);
                c8.setSelected(true);
                break;
        }
    }

    private void JUBAO() {
        if (TextUtils.isEmpty(reason)){
            Toast.makeText(ContanctReportActivty.this, "请选择类型", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getApplicationContext());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();
        if (TYPE.equals("1")){

            try {
                jsonObject.put("type",TYPE);
                jsonObject.put("reason",reason);
                jsonObject.put("commentId",id);
                jsonObject.put("description",report_et_problem.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (TYPE.equals("2")){

            try {
                jsonObject.put("type",TYPE);
                jsonObject.put("reason",reason);
                jsonObject.put("informMemId",id);
                jsonObject.put("description",report_et_problem.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.JUBAO)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ContanctReportActivty.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ContanctReportActivty.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject object = new JSONObject(result);

                    String data = object.getString("data");
                    if (data.equals("举报成功")){
                        ContanctReportActivty.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ContanctReportActivty.this, "举报成功", Toast.LENGTH_SHORT).show();
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

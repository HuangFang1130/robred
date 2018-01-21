package com.jiahehongye.robred.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.utils.LogUtil;
import com.jiahehongye.robred.utils.TUtils;
import com.jiahehongye.robred.view.MyProgressDialog;

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
 * 提现
 */
public class EnchashmentActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mBack;
    private String canuse;
    private EditText zhanghao,name,num;
    private double canusenum;
    private TextView red_tixian;
    private Call call;
    private PersistentCookieStore persistentCookieStore;
    private static final int GET_ALL = 0000;
    private static final int GET_BANK = 0006;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_ALL:
                    String s = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(s);
                        LogUtil.LogShitou("提现结果：",object.toString());
                        if (object.getString("result").equals("success")) {//code : 5025
                            Toast.makeText(EnchashmentActivity.this, "您的提现申请已提交审核", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(EnchashmentActivity.this, object.getString("errorMsg").toString(), Toast.LENGTH_SHORT).show();

                            if(object.getString("errorCode").equals("5025")){
                                showChangeDialogDD();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case GET_BANK:
                    String ss = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(ss);
                        JSONObject data = new JSONObject(object.getString("data"));
                        String bankCard = data.getString("bankCard");
                        String realName = data.getString("realName");
                        zhanghao.setText(bankCard);
                        name.setText(realName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


            }
        }
    };

    private void showChangeDialogDD() {
        new AlertDialog.Builder(this).setTitle("输入名字不匹配是否修改？")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showChangeDialog();
                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    /**
     * 弹出提示框  确认修改支付宝账号
     *
     * @{@"cardType":@"1",@"bankCard":textField0.text,@"realName":textField1.text,@"accountMoney":textField2.text}
     *
     *
     * @{@"cardType":@"1",@"bankCard":textField0.text,@"realName":textField1.text,@"accountMoney":textField2.text,@"type":@"0"}
     */
    private void showChangeDialog() {
        showMyDialog();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(EnchashmentActivity.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cardType","3");//支付宝
            jsonObject.put("bankCard", zhanghao.getText().toString());//卡号
            jsonObject.put("realName",name.getText().toString());//名字
            jsonObject.put("accountMoney",num.getText().toString());//钱
            jsonObject.put("type","0");//钱
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.GET_CHANGE_ACOUNT)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                EnchashmentActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        animDialog.dismiss();
                        Toast.makeText(EnchashmentActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                animDialog.dismiss();
                EnchashmentActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                             JSONObject object = new JSONObject(result);
                            String data = object.getString("data");
                            TUtils.showToast(EnchashmentActivity.this,data.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });


    }

    private String type;
    private String strurl;
    private TextView tixian_shuoming;
    private MyProgressDialog animDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_enchashment);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            canuse = bundle.getString("canuse");
            canusenum = Double.parseDouble(canuse);
            type = bundle.getString("type");
            if (type.equals("1")){// 红包提现
                strurl = Constant.RED_TIXIAN;
            }else if (type.equals("2")){//充值提现
                strurl = Constant.CHONGZHI_TIXIAN;
            }

        }
        tixian_shuoming = (TextView) findViewById(R.id.tixian_shuoming);
        red_tixian = (TextView) findViewById(R.id.red_tixian);
        zhanghao = (EditText) findViewById(R.id.zfb_zhanghao);
        name = (EditText) findViewById(R.id.zfb_name);
        num = (EditText) findViewById(R.id.red_tixiannum);
        num.setHint("可提现金额"+canuse+"元");

        getBank();


        num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(num.getText().toString())){
                    double getnum = Double.parseDouble(num.getText().toString());
                    if(type.equals("1")){
                        if(getnum<30){
                            TUtils.showToast(EnchashmentActivity.this, "提现金额不能低于30元！");
                            red_tixian.setOnClickListener(null);
                            return;
                        }

                    }else {
                        if(getnum<1){
                            TUtils.showToast(EnchashmentActivity.this, "提现金额不能低于1元！");
//                            Toast.makeText(EnchashmentActivity.this, "提现金额不能低于1元！", Toast.LENGTH_SHORT).show();
                            red_tixian.setOnClickListener(null);
                            return;
                        }
                    }
                    if(getnum>canusenum){
                        TUtils.showToast(EnchashmentActivity.this, "超出可提现金额！");
//                        Toast.makeText(EnchashmentActivity.this, "超出可提现金额！", Toast.LENGTH_SHORT).show();
                        red_tixian.setOnClickListener(null);

                    }else {
                        red_tixian.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getdata();
                            }
                        });
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        mBack = (RelativeLayout) findViewById(R.id.enchashment_rl_back);
        mBack.setOnClickListener(this);


        tixian_shuoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),WebActivity.class);
                intent.putExtra("title","提现说明");
                intent.putExtra("URL",Constant.URL+"/wap/redEnveRulesCharge.jhtml");
                startActivity(intent);
            }
        });
    }


    /**
     * 显示对话框
     */
    public void showMyDialog() {
        animDialog = new MyProgressDialog(EnchashmentActivity.this, "玩命加载中...", R.drawable.loading);
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

    /**
     * 获取之前的支付宝账号还有名字
     */
    private void getBank() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(EnchashmentActivity.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.GET_BANK)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                EnchashmentActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EnchashmentActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                LogUtil.LogShitou("getBank: ",result.toString());
                Message msg = handler.obtainMessage();
                msg.what = GET_BANK;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.enchashment_rl_back:
                finish();
                break;
        }
    }


    /**
     *提现
     */
    private void getdata() {

        showMyDialog();
        if (TextUtils.isEmpty(zhanghao.getText().toString())){
            Toast.makeText(EnchashmentActivity.this, "请输入支付宝账号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name.getText().toString())){
            Toast.makeText(EnchashmentActivity.this, "请输入真实姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(EnchashmentActivity.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("cardType","3");//支付宝
            jsonObject.put("bankCard", zhanghao.getText().toString());//卡号
            jsonObject.put("realName",name.getText().toString());//名字
            jsonObject.put("accountMoney",num.getText().toString());//钱
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(strurl)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                EnchashmentActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        animDialog.dismiss();
                        Toast.makeText(EnchashmentActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                animDialog.dismiss();
                String result = response.body().string();

                Message msg = handler.obtainMessage();
                msg.what = GET_ALL;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });

    }
}

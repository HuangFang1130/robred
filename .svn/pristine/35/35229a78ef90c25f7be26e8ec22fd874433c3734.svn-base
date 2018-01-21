package com.jiahehongye.robred.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.view.MyProgressDialog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PayOneyuanActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mBack;

    private static final int GET_USER_BALANCE = 1000;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_USER_BALANCE:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.white);
        setContentView(R.layout.activity_pay_oneyuan);

        initView();

        getBalance();

    }

    /**
     * 获取红包余额和账户余额
     */
    private PersistentCookieStore persistentCookieStore;
    private MyProgressDialog animDialog;
    private Call call;
    private void getBalance() {
        showMyDialog();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        RequestBody body = RequestBody.create(Constant.JSON, "");
        Request request = new Request.Builder()
                .url(Constant.GET_USER_BALANCE)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                PayOneyuanActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PayOneyuanActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        animDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("获取红包余额/账户余额：", result);

                Message msg = handler.obtainMessage();
                msg.what = GET_USER_BALANCE;
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

    private TextView tv_redBalance,tv_rechargeBalance;
    private ImageView iv_redBalance,iv_rechargeBalance,iv_alipay,iv_wxpay;
    private LinearLayout mRedBalance,mRechargeBalance,mAlipay,mWxpay;
    private TextView mOk;
    private void initView() {
        mBack = (RelativeLayout) findViewById(R.id.payoneyuan_rl_back);
        tv_redBalance = (TextView) findViewById(R.id.payoneyuan_tv_red_balance);
        tv_rechargeBalance = (TextView) findViewById(R.id.payoneyuan_tv_recharge_balance);
        iv_redBalance = (ImageView) findViewById(R.id.payoneyuan_iv_red_balance);
        iv_rechargeBalance = (ImageView) findViewById(R.id.payoneyuan_iv_recharge_balance);
        iv_alipay = (ImageView) findViewById(R.id.payoneyuan_iv_alipay);
        iv_wxpay = (ImageView) findViewById(R.id.payoneyuan_iv_wxpay);
        mRedBalance = (LinearLayout) findViewById(R.id.payoneyuan_ll_red_balance);
        mRechargeBalance = (LinearLayout) findViewById(R.id.payoneyuan_ll_recharge_balance);
        mAlipay = (LinearLayout) findViewById(R.id.payoneyuan_ll_alipay);
        mWxpay = (LinearLayout) findViewById(R.id.payoneyuan_ll_wxpay);
        mOk = (TextView) findViewById(R.id.payoneyuan_tv_ok);

        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.payoneyuan_rl_back:
                finish();
                break;
            case R.id.payoneyuan_ll_red_balance:
                setAllBtnHide();
                iv_redBalance.setVisibility(View.VISIBLE);
                break;
            case R.id.payoneyuan_ll_recharge_balance:
                setAllBtnHide();
                iv_rechargeBalance.setVisibility(View.VISIBLE);
                break;
            case R.id.payoneyuan_ll_alipay:
                setAllBtnHide();
                iv_alipay.setVisibility(View.VISIBLE);
                break;
            case R.id.payoneyuan_ll_wxpay:
                setAllBtnHide();
                iv_wxpay.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 所有对勾隐藏
     */
    public void setAllBtnHide(){
        iv_redBalance.setVisibility(View.GONE);
        iv_rechargeBalance.setVisibility(View.GONE);
        iv_alipay.setVisibility(View.GONE);
        iv_wxpay.setVisibility(View.GONE);
    }
}

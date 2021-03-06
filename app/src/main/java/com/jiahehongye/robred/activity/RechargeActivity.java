package com.jiahehongye.robred.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.PayResult;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.view.MyProgressDialog;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

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
 * 充值页面
 */
public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    private Animation show, hide;

    private RelativeLayout mBack;
    private LinearLayout mChooseType;
    private ImageView iv_type;
    private TextView tv_type;
    private EditText mPrice;
    private TextView mCommit;
    private View spaceView;
    private LinearLayout mChooseWindow;
    private RelativeLayout mClose;
    private LinearLayout ll_alipay,ll_wxpay;
    private PersistentCookieStore persistentCookieStore;
    private Call call;
    private MyProgressDialog animDialog;
    private String result;
    private String result1;

    private String appid;
    private String partnerid;
    private String prepayid;
    private String package1;
    private String noncestr;
    private String timestamp;
    private String sign;
    private IWXAPI api;
    // 微信支付
    private static final String APP_ID = "wx8f543a00c79d8221";

    private String requestParamsStr;
    private String accountDetailId;
    private static final int SDK_PAY_FLAG = 3;
    private static final int SDK_CHECK_FLAG = 4;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://先在后台创建订单，
                    // 主线程拿到网络请求到额数据。
                    result = (String) msg.obj;

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("result").equals("success")) {
                            System.out.println("发送的消息在解析了");
                            JSONObject json = jsonObject.getJSONObject("data");
                            requestParamsStr = json.getString("requestParamsStr");///拿到订单信息
                            accountDetailId = json.getString("accountDetailId");

                            System.out.println("获取完了");
                            zhifuPay();//调支付宝



                        } else if (jsonObject.getString("result").equals("fail")) {
                            System.out.println("3");
                            Toast.makeText(RechargeActivity.this, jsonObject.getString("errorMsg"), Toast.LENGTH_SHORT).show();
                            //
                        } else {
                            System.out.println("3");
                            Toast.makeText(RechargeActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {//支付宝成功的返回状态吗是9000
                        Toast.makeText(RechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        // Intent intent = new
                        // Intent(Pay.this,SendDetailActivity.class);
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(RechargeActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(RechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }

                case 2://微信支付
                    // 主线程拿到网络请求到额数据。
                    result1 = (String) msg.obj;
                    System.out.println("1");
                    try {
                        JSONObject jsonObject = new JSONObject(result1);

                        if (jsonObject.getString("result").equals("success")) {
                            System.out.println("2");
                            JSONObject json = jsonObject.getJSONObject("data");
                            requestParamsStr = json.getString("requestParamsStr");
                            accountDetailId = json.getString("accountDetailId");
                            JSONObject json2 = json.getJSONObject("wxParams");

                            appid = json2.getString("appid");
                            partnerid = json2.getString("partnerid");
                            prepayid = json2.getString("prepayid");
                            package1 = json2.getString("package");
                            noncestr = json2.getString("noncestr");
                            timestamp = json2.getString("timestamp");
                            sign = json2.getString("sign");
                            wxPay();
                        } else if (jsonObject.getString("result").equals("fail")) {
                            System.out.println("3");
                            Toast.makeText(RechargeActivity.this, jsonObject.getString("errorMsg"), Toast.LENGTH_SHORT).show();
                            //
                        } else {
                            System.out.println("3");
                            Toast.makeText(RechargeActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    break;
                case SDK_CHECK_FLAG:
                    Toast.makeText(RechargeActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        };

    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.white);
        setContentView(R.layout.activity_recharge);
        // 注册appid
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.registerApp(APP_ID);
        mBack = (RelativeLayout) findViewById(R.id.recharge_rl_back);
        mBack.setOnClickListener(this);

        show = AnimationUtils.loadAnimation(this, R.anim.pop_zhifu_show);
        hide = AnimationUtils.loadAnimation(this, R.anim.pop_zhifu_hide);

        initView();

        mPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        mPrice.setText(s);
                        mPrice.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    mPrice.setText(s);
                    mPrice.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mPrice.setText(s.subSequence(0, 1));
                        mPrice.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void initView() {
        mChooseType = (LinearLayout) findViewById(R.id.recharge_ll_type);
        iv_type = (ImageView) findViewById(R.id.recharge_iv_paytype);
        tv_type = (TextView) findViewById(R.id.recharge_tv_paytype);
        mPrice = (EditText) findViewById(R.id.recharge_et_price);
        mCommit = (TextView) findViewById(R.id.recharge_tv_recharge);
        spaceView = findViewById(R.id.recharge_view);
        mChooseWindow = (LinearLayout) findViewById(R.id.recharge_ll_choose_window);
        mClose = (RelativeLayout) findViewById(R.id.recharge_rl_close);
        ll_alipay = (LinearLayout) findViewById(R.id.recharge_ll_choose_alipay);
        ll_wxpay = (LinearLayout) findViewById(R.id.recharge_ll_choose_wxpay);

        mChooseType.setOnClickListener(this);
        mCommit.setOnClickListener(this);
        spaceView.setOnClickListener(this);
        mClose.setOnClickListener(this);
        ll_alipay.setOnClickListener(this);
        ll_wxpay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.recharge_tv_recharge:
                if (tv_type.getText().equals("支付宝支付")) {

                    String edit = mPrice.getText().toString().trim();
                    if (!TextUtils.isEmpty(edit)) {
                        String sourceType = "3";
                        String pluginId = "alipayAppPlugin";
                        System.out.println("支付宝支付传入数据" + edit + sourceType + pluginId);
                        requestPay(edit, sourceType, pluginId);
                    } else {

                        Toast.makeText(RechargeActivity.this, "请输入金额", Toast.LENGTH_SHORT).show();
                         return;
                    }

                } else if (tv_type.getText().equals("微信支付")) {

                    String edit = mPrice.getText().toString().trim();
                    String sourceType = "2";
                    String pluginId = "wxpayAppPlugin";
                    requestWXPay(edit, sourceType, pluginId);

                } else {
                    Toast.makeText(RechargeActivity.this, "请选择支付账户", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.recharge_rl_back:
                finish();
                break;
            case R.id.recharge_ll_type:
                showChooseWindow();
                break;
            case R.id.recharge_view:
                hideChooseWindow();
                break;
            case R.id.recharge_rl_close:
                hideChooseWindow();
                break;
            case R.id.recharge_ll_choose_alipay:
                iv_type.setImageResource(R.mipmap.icon_alipay);
                tv_type.setText("支付宝支付");
                hideChooseWindow();
                break;
            case R.id.recharge_ll_choose_wxpay:
                iv_type.setImageResource(R.mipmap.icon_wxpay);
                tv_type.setText("微信支付");
                hideChooseWindow();
                break;
        }
    }

    /**
     * chooseWindow显示
     */
    private void showChooseWindow() {
        mChooseWindow.setAnimation(show);
        mChooseWindow.startAnimation(show);
        mChooseWindow.setVisibility(View.VISIBLE);
    }

    /**
     * chooseWindow隐藏
     */
    private void hideChooseWindow() {
        mChooseWindow.setAnimation(hide);
        mChooseWindow.startAnimation(hide);
        mChooseWindow.setVisibility(View.GONE);
    }


    /**
     * 支付宝支付
     * @param edit
     * @param sourceType
     * @param pluginId
     */
    private void requestPay(String edit, String sourceType, String pluginId) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {
            json.put("rechargeAmount", edit);
            json.put("sourceType", sourceType);
            json.put("pluginId", pluginId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.MEMBERRECHARGE)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RechargeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RechargeActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                try {
                    if (result!=null) {
                        Message message = handler.obtainMessage();
                        message.what = 1;
                        message.obj = result;
                        System.out.println("支付宝发送消息了");
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void zhifuPay() {

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {

                PayTask alipay = new PayTask(RechargeActivity.this);
                System.out.println("走了支付宝");
                String result = alipay.pay(requestParamsStr, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();//异步调用

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

    private void requestWXPay(String edit, String sourceType, String pluginId) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {
            json.put("rechargeAmount", edit);
            json.put("sourceType", sourceType);
            json.put("pluginId", pluginId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.MEMBERRECHARGE)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RechargeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RechargeActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                try {
                    if (result!=null) {
                        Message message = handler.obtainMessage();
                        message.what = 2;
                        message.obj = result;
                        System.out.println("支付宝发送消息了");
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 创建一个PayReq
     */
    private void wxPay() {
        // 点击确认时候就调起预支付.
        System.out.println("微信在封装数据了");
        PayReq request = new PayReq();
        request.appId = "wx8f543a00c79d8221";
        request.partnerId = partnerid;
        request.prepayId = prepayid;
        request.packageValue = package1;
        request.nonceStr = noncestr;
        request.timeStamp = timestamp;
        request.sign = sign;
        api.sendReq(request);
        finish();
    }
}

package com.jiahehongye.robred.oneyuan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.R.id;
import com.jiahehongye.robred.activity.PayRedActivity;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.utils.DesUtil;
import com.jiahehongye.robred.utils.TUtils;
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

public class OneyuanPayActivity extends BaseActivity implements OnClickListener {

    private TextView mMoney;
    private Button mOk;
    private static final int GET_PRODUCT_INFOR = 6666;
    private static final int TIJIAO_BACK = 7777;
    private String money;
    private PopupWindow window;
    private PopupWindow window2;
    private String passwordDes;
    private String productId;
    private RelativeLayout oneyuanpay_rl_back;
    private TextView name;

    private int maxNum;
    private String productname;
    private String danjia;
    private double danjia_dou;
    private String mobile;
    private EditText oneyuan_mobile;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_PRODUCT_INFOR:

                    String data1 = (String) msg.obj;
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(data1);
                        String res = jsonObject.getString("result");
                        if (res.equals("success")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            maxNum = Integer.parseInt(data.getString("surplusPeople"));
                            productname = data.getString("name");
                            name.setText(productname);
                            danjia = data.getString("price");
                            danjia_dou = Double.parseDouble(data.getString("price"));
                            mobile = data.getString("mobile");
                            oneyuan_mobile.setText(mobile);

                            xiaoji.setText(danjia);


                        } else {
                            Toast.makeText(UIUtils.getContext(), jsonObject.optString("errorMsg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;

                case TIJIAO_BACK:
                    String tijiao = (String) msg.obj;
                    try {
                        JSONObject jsonObject1 = new JSONObject(tijiao);
                        String res = jsonObject1.getString("result");
                        if (res.equals("success")) {
                            JSONObject data = jsonObject1.getJSONObject("data");
                            String code =data.getString("orderCode");
                            Intent intent = new Intent(OneyuanPayActivity.this, PayRedActivity.class);
                            intent.putExtra("redCode", code);
                            intent.putExtra("type","1");
                            startActivity(intent);
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    private TextView xiaoji;
    private int num = 1;
    private TextView minus;
    private TextView plus;
    private double zongjia_dou;
    private TextView number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_oneyuan_pay);

        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        money = intent.getStringExtra("accoRedEnve");


        mOk = (Button) findViewById(id.btn_oneyuan_pay_ok);
        mOk.setOnClickListener(this);

        oneyuanpay_rl_back = (RelativeLayout) findViewById(id.oneyuanpay_rl_back);
        oneyuanpay_rl_back.setOnClickListener(this);

        name = (TextView) findViewById(id.oneyuan_productname);
        xiaoji = (TextView) findViewById(id.xiaoji);

        minus = (TextView) findViewById(id.detail_minus);
        plus = (TextView) findViewById(id.detail_plus);
        number = (TextView) findViewById(id.detail_number);
        oneyuan_mobile = (EditText) findViewById(id.oneyuan_mobile);

        minus.setOnClickListener(this);
        plus.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {
            json.put("productId", productId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.GET_PRODUCT_INFOR)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OneyuanPayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(OneyuanPayActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String productdata = response.body().string();

                Message msg = handler.obtainMessage();
                msg.what = GET_PRODUCT_INFOR;
                msg.obj = productdata;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case id.btn_oneyuan_pay_ok:
                confirm();
                break;

            case R.id.oneyuanpay_rl_back:
                finish();
                break;


            case id.detail_minus:
                if (num - 1 <= 0) {
                    Toast.makeText(OneyuanPayActivity.this, "不能低于一件哦~", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    num = num - 1;
                    changeView();
                }
                changeView();
                break;


            case id.detail_plus:
                if (num + 1 > maxNum) {
//                    Toast.makeText(OneyuanPayActivity.this, "库存不足", Toast.LENGTH_SHORT).show();
                    TUtils.showToast(OneyuanPayActivity.this,"库存不足");
                    return;
                } else {
                    num = num + 1;
                    changeView();
                }
                break;


            default:
                break;
        }
    }

    private void changeView() {
        number.setText(num + "");
        zongjia_dou = num * danjia_dou;
        xiaoji.setText("¥" + zongjia_dou);
    }

    public void getMemberInfo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.MEMBERINFO)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OneyuanPayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(OneyuanPayActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String Infodata = response.body().string();
                Log.e("会员信息==", Infodata);
                OneyuanPayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Infodata != null) {
                            try {
                                JSONObject json = new JSONObject(Infodata);
                                String result = json.getString("result");
                                if (result.equals("success")) {
                                    JSONObject jsonob = json.getJSONObject("data");
                                    String setPwd = jsonob.getString("txYezfPass");
                                    if (TextUtils.isEmpty(setPwd)) {
                                        backgroundAlpha(0.5f);
                                    } else {
                                        backgroundAlpha(0.5f);
                                    }
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "网络请求失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }


    /**
     * 打开软键盘
     */
    private void openKeyboard(Handler mHandler, int s) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, s);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getWindow().setAttributes(lp);
    }

    // 设置密码
    public void setPsd(String psd) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {
            // 给获取到的密码进行加密
            passwordDes = DesUtil.encrypt(psd);
            json.put("payPassword", passwordDes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.SETPAYPASSWORD)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OneyuanPayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(OneyuanPayActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        window.dismiss();
                        backgroundAlpha(1f);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String psdData = response.body().string();
                Log.e("psdData", psdData);
                OneyuanPayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (psdData != null) {
                            try {
                                JSONObject json = new JSONObject(psdData);
                                String res = json.getString("result");
                                if (res.equals("success")) {
                                    window.dismiss();
                                    backgroundAlpha(1f);
                                    Toast.makeText(getApplicationContext(), "设置密码成功", Toast.LENGTH_SHORT).show();
                                    // if(TextUtils.isEmpty(isSet)){
                                    // SharedPreferencesUtils.putString(PayWayActivity.this,
                                    // "isPwd", passwordDes);
                                    // }
                                    backgroundAlpha(0.5f);
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(OneyuanPayActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }


    // 输入密码
    private PersistentCookieStore persistentCookieStore;
    private Call call;

    public void rePsd(final String psd) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {
            // 给获取到的密码进行加密
            String passwordDes2 = DesUtil.encrypt(psd);
            json.put("payPassword", passwordDes2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.CHECKPAYPASSWORD)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OneyuanPayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(OneyuanPayActivity.this, "支付密码验证网络请求失败", Toast.LENGTH_SHORT).show();
                        window2.dismiss();
                        backgroundAlpha(1f);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String reData = response.body().string();
                Log.e("reData = ", reData);
                OneyuanPayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json2 = new JSONObject(reData);
                            String result = json2.getString("result");
                            if (result.equals("success")) {
                                window2.dismiss();
                                backgroundAlpha(1f);
                            } else {
                                String error = json2.getString("errorMsg");
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                window2.dismiss();
                                backgroundAlpha(1f);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }


    private void confirm() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(OneyuanPayActivity.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {
            json.put("productId", productId);
            json.put("number", num+"");
            json.put("mobile", oneyuan_mobile.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.ONEYUANPAY)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OneyuanPayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(OneyuanPayActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String reData = response.body().string();

                Message msg = handler.obtainMessage();
                msg.what = TIJIAO_BACK;
                msg.obj = reData;
                handler.sendMessage(msg);
//                Log.e("付款结果：", productId + "---" + reData);
//                OneyuanPayActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            JSONObject object = new JSONObject(reData);
//                            if (object.get("result").equals("success")) {
//                                Toast.makeText(OneyuanPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                                finish();
//                                startActivity(new Intent(OneyuanPayActivity.this, MyRobListActivity.class));
//                            } else if (object.get("result").equals("fail")) {
//                                Toast.makeText(OneyuanPayActivity.this, object.getString("errorMsg"),
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//                });

            }
        });
    }

}

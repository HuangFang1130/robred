package com.jiahehongye.robred.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.jiahehongye.robred.utils.DesUtil;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.MyProgressDialog;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PayRedActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mBack;
    public static String WHRER_TO_GO = "3";

    //红包code
    private String redCode;
    // 支付类型
    private String payType;
    // 插件id
    private String pluginId;
    //获取到的整段回调地址
    private String payUrl_cache;

    // 微信
    private IWXAPI msgApi;
    private String wxPartnerId;
    private String wxPrepayId;
    private String wxPackageValue;
    private String wxNonceStr;
    private String wxTimeStamp;
    private String wxSign;
    // 微信支付
    private static final String APP_ID = "wx8f543a00c79d8221";

    private String setPwd = "";//获取到的账户信息支付密码
    private static final int GET_USER_BALANCE = 1000;
    private static final int PAY_RED_WAY_OK = 1001;
    private static final int GET_PAY_PASSWORD = 1002;
    private static final int SETPAYPASSWORD = 1003;
    private static final int CHECKPAYPASSWORD = 1004;
    private static final int BALANCE_PAY_RETURN = 1005;
    private static final int SDK_PAY_FLAG = 3333;
    private static final int DIAMOND = 6666;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case DIAMOND:
                    animDialog.dismiss();
                    String diamond = (String) msg.obj;

                    try {
                        JSONObject jsonObject = new JSONObject(diamond);
                        String result = jsonObject.optString("result");
                        if (result.equals("success")) {
                            JSONObject json2 = jsonObject.getJSONObject("data");
                            payUrl_cache = json2.getString("requestParamsStr");
                            if (payType.equals("1")) {
                                String payInfo = payUrl_cache;
                            }
                            if (payType.equals("2")) {
                                JSONObject json3 = json2.getJSONObject("wxParams");
                                wxPartnerId = json3.getString("partnerid");
                                wxPrepayId = json3.getString("prepayid");
                                wxPackageValue = json3.getString("package");
                                wxNonceStr = json3.getString("noncestr");
                                wxTimeStamp = json3.getString("timestamp");
                                wxSign = json3.getString("sign");
                            }

                            //根据不同支付方式进行请求
                            if (payType.equals("3")) {
                                if (setPwd == null || setPwd.length() == 0) {
                                    showPopwindow();
                                    backgroundAlpha(0.5f);
                                } else {
                                    showPopwindow2();
                                    backgroundAlpha(0.5f);
                                }
                            } else if (payType.equals("1")) {
                                zhifubaoPay();
                            } else if (payType.equals("2")) {
                                wxPay();
                            } else {
                                Toast.makeText(UIUtils.getContext(), jsonObject.optString("errorMsg"), Toast.LENGTH_SHORT).show();//账户余额不足
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case GET_USER_BALANCE://获取账户余额
                    animDialog.dismiss();
                    String data_balance = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(data_balance);
                        JSONObject json = jsonObject.getJSONObject("data");
                        String total = json.getString("accountAmount");
                        tv_balance.setText(total);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case PAY_RED_WAY_OK://确定支付方式
                    animDialog.dismiss();
                    String pay_red_way_ok = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(pay_red_way_ok);
                        String result = jsonObject.optString("result");
                        if (result.equals("success")) {
                            JSONObject json2 = jsonObject.getJSONObject("data");
                            payUrl_cache = json2.getString("requestParamsStr");
                            //根据不同的支付方式设置对应参数
                            if (payType.equals("1")) {
                                String payInfo = payUrl_cache;
                            }
                            if (payType.equals("2")) {
                                JSONObject json3 = json2.getJSONObject("wxParams");
                                wxPartnerId = json3.getString("partnerid");
                                wxPrepayId = json3.getString("prepayid");
                                wxPackageValue = json3.getString("package");
                                wxNonceStr = json3.getString("noncestr");
                                wxTimeStamp = json3.getString("timestamp");
                                wxSign = json3.getString("sign");
                                wxPay();
                            }

                            //根据不同支付方式进行请求
                            if (payType.equals("3")) {
                                if (setPwd == null || setPwd.length() == 0) {
                                    showPopwindow();
                                    backgroundAlpha(0.5f);
                                } else {
                                    showPopwindow2();
                                    backgroundAlpha(0.5f);
                                }
                            } else if (payType.equals("1")) {
                                zhifubaoPay();
                            } else if (payType.equals("2")) {
//
                            }

                        } else {
                            Toast.makeText(UIUtils.getContext(), jsonObject.optString("errorMsg"), Toast.LENGTH_SHORT).show();//账户余额不足
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case GET_PAY_PASSWORD:
//                    animDialog.dismiss();
                    String Indata = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(Indata);
                        String result = json.getString("result");
                        if (result.equals("success")) {
                            JSONObject jsonob = json.getJSONObject("data");
                            setPwd = jsonob.optString("txYezfPass");
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case SETPAYPASSWORD:
                    animDialog.dismiss();
                    String setPsw = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(setPsw);
                        String res = json.getString("result");
                        if (res.equals("success")) {
                            window.dismiss();
                            Toast.makeText(getApplicationContext(), "设置支付密码成功", Toast.LENGTH_SHORT).show();
                            showPopwindow2();
                        } else {
                            Toast.makeText(PayRedActivity.this, json.optString("errorMsg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case CHECKPAYPASSWORD:
                    animDialog.dismiss();
                    String checkPsw = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(checkPsw);
                        String res = json.getString("result");
                        if (res.equals("success")) {
                            String[] arr = payUrl_cache.split("=");
                            final String payUrl = arr[1];
                            Log.e("余额支付请求接口==", payUrl);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    window2.dismiss();
                                    backgroundAlpha(1f);
                                    confirm(payUrl);
                                }
                            }, 1000);

                        } else {
                            Toast.makeText(PayRedActivity.this, json.optString("errorMsg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case BALANCE_PAY_RETURN:
                    animDialog.dismiss();
                    String payReturn = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(payReturn);
                        String res = json.getString("result");
                        if (res.equals("success")) {
                            Toast.makeText(PayRedActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                            if (type.equals("1")){
                                Intent intent = new Intent(PayRedActivity.this, SnatchOrderActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }if (type.equals("3")){
                                finish();
                            }
                            else {
                                Intent intent = new Intent(PayRedActivity.this, GrabRedActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(PayRedActivity.this, json.optString("errorMsg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    Log.e("支付宝返回结果：", resultInfo);
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PayRedActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        if (type.equals("1")){
                            Intent intent = new Intent(PayRedActivity.this, SnatchOrderActivity.class);
                            startActivity(intent);
                            finish();
                        }if (type.equals("3")){
                            finish();
                        }
                        else {
                            Intent intent = new Intent(PayRedActivity.this, GrabRedActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayRedActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PayRedActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    };
    private String type;

    /**
     * 支付宝支付
     */
    private void zhifubaoPay() {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayRedActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payUrl_cache, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 余额支付请求
     */
    public void confirm(String url) {

        showMyDialog();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(PayRedActivity.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                PayRedActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PayRedActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        animDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("余额支付返回：", result);

                Message msg = handler.obtainMessage();
                msg.what = BALANCE_PAY_RETURN;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    private PopupWindow window;
    private PopupWindow window2;

    public void showPopwindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.payway_popwindow, null);
        view.setBackgroundResource(R.drawable.paypassword_rec);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        window = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        // 必须要给调用这个方法，否则点击popWindow以外部分，popWindow不会消失
        // window.setBackgroundDrawable(new BitmapDrawable());
        // 实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDra

        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(PayRedActivity.this.findViewById(R.id.payred_tv_ok), Gravity.TOP, 0, 240);
        // 这里检验popWindow里的button是否可以点击
        Button cancel = (Button) view.findViewById(R.id.btn_payway_cancel);
        Button commit = (Button) view.findViewById(R.id.btn_payway_commit);
        final EditText et_xxx = (EditText) view.findViewById(R.id.et_xxx);
        final TextView one = (TextView) view.findViewById(R.id.one_point);
        final TextView two = (TextView) view.findViewById(R.id.two_point);
        final TextView three = (TextView) view.findViewById(R.id.three_point);
        final TextView four = (TextView) view.findViewById(R.id.four_point);
        final TextView five = (TextView) view.findViewById(R.id.five_point);
        final TextView six = (TextView) view.findViewById(R.id.six_point);
        final List<TextView> list = new ArrayList<TextView>();
        list.add(one);
        list.add(two);
        list.add(three);
        list.add(four);
        list.add(five);
        list.add(six);
        et_xxx.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                int length = s.length();
                one.setVisibility(View.GONE);
                two.setVisibility(View.GONE);
                three.setVisibility(View.GONE);
                four.setVisibility(View.GONE);
                five.setVisibility(View.GONE);
                six.setVisibility(View.GONE);
                for (int i = 0; i < length; i++) {
                    list.get(i).setVisibility(View.VISIBLE);
                }
            }
        });

        openKeyboard(new Handler(), 100);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                backgroundAlpha(1f);
            }

        });

        commit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (et_xxx.getText().length() == 6) {
                    String password = et_xxx.getText().toString();
                    setPsd(password);
                } else {
                    Toast.makeText(PayRedActivity.this, "密码必须是六位", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                System.out.println("popWindow消失");
            }
        });

    }

    public void showPopwindow2() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.payway_popwindow2, null);
        view.setBackgroundResource(R.drawable.paypassword_rec);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        window2 = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window2.setFocusable(true);
        // 必须要给调用这个方法，否则点击popWindow以外部分，popWindow不会消失
        // window.setBackgroundDrawable(new BitmapDrawable());
        // 实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDra

        // 设置popWindow的显示和消失动画
        window2.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window2.showAtLocation(PayRedActivity.this.findViewById(R.id.payred_tv_ok), Gravity.TOP, 0, 240);

        // 这里检验popWindow里的button是否可以点击
        Button cancel = (Button) view.findViewById(R.id.btn_payway_cancel);
        Button commit = (Button) view.findViewById(R.id.btn_payway_commit);
        final EditText et_xxx = (EditText) view.findViewById(R.id.et_xxx);
        final TextView one = (TextView) view.findViewById(R.id.one_point);
        final TextView two = (TextView) view.findViewById(R.id.two_point);
        final TextView three = (TextView) view.findViewById(R.id.three_point);
        final TextView four = (TextView) view.findViewById(R.id.four_point);
        final TextView five = (TextView) view.findViewById(R.id.five_point);
        final TextView six = (TextView) view.findViewById(R.id.six_point);
        final List<TextView> list = new ArrayList<TextView>();
        list.add(one);
        list.add(two);
        list.add(three);
        list.add(four);
        list.add(five);
        list.add(six);
        et_xxx.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                int length = s.length();
                one.setVisibility(View.GONE);
                two.setVisibility(View.GONE);
                three.setVisibility(View.GONE);
                four.setVisibility(View.GONE);
                five.setVisibility(View.GONE);
                six.setVisibility(View.GONE);
                for (int i = 0; i < length; i++) {
                    list.get(i).setVisibility(View.VISIBLE);
                }
            }
        });

        //打开键盘，设置延时时长
        openKeyboard(new Handler(), 100);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window2.dismiss();
                backgroundAlpha(1f);
            }

        });

        commit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (et_xxx.getText().length() == 6) {
                    String password = et_xxx.getText().toString();
                    rePsd(password);

                } else {
                    Toast.makeText(PayRedActivity.this, "密码必须是六位", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // popWindow消失监听方法
        window2.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                System.out.println("popWindow消失");
            }
        });

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
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
     * 设置支付密码
     */
    public void setPsd(String psd) {
        showMyDialog();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(PayRedActivity.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {
            // 给获取到的密码进行加密
            String passwordDes = DesUtil.encrypt(psd);
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
                PayRedActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PayRedActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        animDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("设置支付密码返回：", result);

                Message msg = handler.obtainMessage();
                msg.what = SETPAYPASSWORD;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 验证支付密码
     */
    public void rePsd(String psd) {
        showMyDialog();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(PayRedActivity.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {
            // 给获取到的密码进行加密
            String passwordDes = DesUtil.encrypt(psd);
            json.put("payPassword", passwordDes);
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
                PayRedActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PayRedActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        animDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("验证支付密码返回：", result);

                Message msg = handler.obtainMessage();
                msg.what = CHECKPAYPASSWORD;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 获取个人信息中的支付密码
     */
    private void getMemberInfo() {
//        showMyDialog();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(PayRedActivity.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.MEMBERINFO)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                PayRedActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PayRedActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
//                        animDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("获取个人全部信息返回：", result);

                Message msg = handler.obtainMessage();
                msg.what = GET_PAY_PASSWORD;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.white);
        setContentView(R.layout.activity_pay_red);

        initView();

        msgApi = WXAPIFactory.createWXAPI(this,APP_ID, true);
        // 将该app注册到微信
        msgApi.registerApp("wx8f543a00c79d8221");
        Intent intent = getIntent();
        redCode = intent.getStringExtra("redCode");
        type = intent.getStringExtra("type");

        if(type.equals("1")){
            WHRER_TO_GO = "1";
        }else if (type.equals("3")){
            WHRER_TO_GO = "3";
        }else {
            WHRER_TO_GO = "2";
        }

        getBalance();

        getMemberInfo();

    }

    public void wxPay() {
        PayReq request = new PayReq();
        request.appId = "wx8f543a00c79d8221";
        request.partnerId = wxPartnerId;
        request.prepayId = wxPrepayId;
        request.packageValue = wxPackageValue;
        request.nonceStr = wxNonceStr;
        request.timeStamp = wxTimeStamp;
        request.sign = wxSign;
        msgApi.sendReq(request);
        finish();
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

        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.GET_USER_BALANCE)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                PayRedActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PayRedActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
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

    private TextView tv_balance;
    private ImageView iv_balance, iv_alipay, iv_wxpay;
    private LinearLayout mBalance, mAlipay, mWxpay;
    private TextView mOk;

    private void initView() {
        mBack = (RelativeLayout) findViewById(R.id.payred_rl_back);
        tv_balance = (TextView) findViewById(R.id.payred_tv_balance);
        iv_balance = (ImageView) findViewById(R.id.payred_iv_balance);
        iv_alipay = (ImageView) findViewById(R.id.payred_iv_alipay);
        iv_wxpay = (ImageView) findViewById(R.id.payred_iv_wxpay);
        mBalance = (LinearLayout) findViewById(R.id.payred_ll_balance);
        mAlipay = (LinearLayout) findViewById(R.id.payred_ll_alipay);
        mWxpay = (LinearLayout) findViewById(R.id.payred_ll_wxpay);
        mOk = (TextView) findViewById(R.id.payred_tv_ok);

        mBack.setOnClickListener(this);
        mBalance.setOnClickListener(this);
        mAlipay.setOnClickListener(this);
        mWxpay.setOnClickListener(this);
        mOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.payred_rl_back:
                finish();
                break;
            case R.id.payred_ll_balance:
                setAllBtnHide();
                iv_balance.setVisibility(View.VISIBLE);
                pluginId = "accountPayPlugin";
                payType = "3";
                break;
            case R.id.payred_ll_alipay:
                setAllBtnHide();
                iv_alipay.setVisibility(View.VISIBLE);
                pluginId = "alipayAppPlugin";
                payType = "1";
                break;
            case R.id.payred_ll_wxpay:
                setAllBtnHide();
                iv_wxpay.setVisibility(View.VISIBLE);
                pluginId = "wxpayAppPlugin";
                payType = "2";
                break;
            case R.id.payred_tv_ok:
                if (payType == null) {
                    Toast.makeText(UIUtils.getContext(), "请选择支付方式", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (type.equals("3")) {
                    diamondPay();
                } else {
                    requestPay();
                }
                break;
        }
    }


    public void requestPay() {
        showMyDialog();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {

            json.put("pluginId", pluginId);
            json.put("payType", payType);
            json.put("redEnveCode", redCode);
            if (type.equals("1")) {
                json.put("orderType", "1");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.PAY_RED_WAY_OK)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                PayRedActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PayRedActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        animDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("确定支付方式返回：", result);

                Message msg = handler.obtainMessage();
                msg.what = PAY_RED_WAY_OK;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }


    public void diamondPay() {
        showMyDialog();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {

            json.put("pluginId", pluginId);
            json.put("payType", payType);
            json.put("diamondDetailId", redCode);

        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.BUY_DIAMOND2)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                PayRedActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PayRedActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                        animDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("确定支付方式返回：", result);

                Message msg = handler.obtainMessage();
                msg.what = DIAMOND;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }



    /**
     * 所有对勾隐藏
     */
    public void setAllBtnHide() {
        iv_balance.setVisibility(View.GONE);
        iv_alipay.setVisibility(View.GONE);
        iv_wxpay.setVisibility(View.GONE);
    }
}

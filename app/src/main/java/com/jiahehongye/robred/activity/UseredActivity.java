package com.jiahehongye.robred.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.utils.DesUtil;
import com.jiahehongye.robred.view.MyProgressDialog;

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

public class UseredActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mBack;
    private LinearLayout mEnchashment,mRecharge;
    private MyProgressDialog animDialog;
    private Call call;
    private PersistentCookieStore persistentCookieStore;
    private static final int GET_ALL = 0000;
    private static final int CHECKPAYPASSWORD = 1004;
    private String accoRedEnve = "0";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_ALL:
                    String s = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(s);
                        if (object.getString("result").equals("success")) {
                            JSONObject data = new JSONObject(object.getString("data"));
                            accoRedEnve = data.getString("accoRedEnve");
                            red_canuse.setText("¥"+accoRedEnve);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case CHECKPAYPASSWORD:
                    animDialog.dismiss();
                    String checkPsw =(String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(checkPsw);
                        String res = json.getString("result");
                        if (res.equals("success")) {
                            Intent intent = new Intent(getApplicationContext(),EnchashmentActivity.class);
                            intent.putExtra("canuse",accoRedEnve);
                            intent.putExtra("type","1");
                            startActivity(intent);
                            window2.dismiss();
                        }else {
                            Toast.makeText(UseredActivity.this,json.optString("errorMsg"),Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;

            }
        }
    };
    private TextView red_canuse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_usered);

        mBack = (RelativeLayout) findViewById(R.id.usered_rl_back);
        red_canuse = (TextView) findViewById(R.id.red_canuse);
        mBack.setOnClickListener(this);
        mEnchashment = (LinearLayout) findViewById(R.id.usered_ll_enchashment);
        mRecharge = (LinearLayout) findViewById(R.id.usered_ll_recharge);
        mEnchashment.setOnClickListener(this);
        mRecharge.setOnClickListener(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        getdata();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.usered_rl_back:
                finish();
                break;
            case R.id.usered_ll_enchashment:
                if (Double.parseDouble(accoRedEnve.trim())>=30.00){
                    showPopwindow2();


                }else {
                    Toast.makeText(UseredActivity.this, "红包余额不足30元", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.usered_ll_recharge:
                break;
        }
    }

    private void getdata() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(UseredActivity.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("pageNumber","1");
            jsonObject.put("pageSize", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.RED_YUELIST)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                UseredActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UseredActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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

    private PopupWindow window2;
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
        window2.showAtLocation(UseredActivity.this.findViewById(R.id.usered_ll_enchashment), Gravity.TOP, 0, 240);

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
        list.add(one);list.add(two);list.add(three);list.add(four);list.add(five);list.add(six);
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
                for(int i = 0;i<length;i++){
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
                if(et_xxx.getText().length()==6){
                    String password = et_xxx.getText().toString();
                    rePsd(password);

                }else {
                    Toast.makeText(UseredActivity.this, "密码必须是六位", Toast.LENGTH_SHORT).show();
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
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }


    /**
     * 验证支付密码
     */
    public void rePsd(String psd) {
        showMyDialog();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(UseredActivity.this);
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
                UseredActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UseredActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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
}

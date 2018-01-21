package com.jiahehongye.robred.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.PhotoBean;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.fragment.GrabMerchantFragment;
import com.jiahehongye.robred.fragment.GrabPersonalFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by huangjunhui on 2016/12/2.13:00
 */
public class GrabRedActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private FragmentTransaction fragmentTransaction;
    private GrabPersonalFragment grabPersonalFragment;
    private GrabMerchantFragment grabMerchantFragment;
    private Fragment mTempFragment;
    private Fragment fragment;
    private TextView mTvPersonal;
    private TextView mTvMerchant;
    private PersistentCookieStore persistentCookieStore;
    private static final int GET_ALL = 0000;
    private ArrayList<PhotoBean> photos = new ArrayList<>();
    private Call call;

    public static String userPhoto;
    public static String nickname;
    public static String job;
    public static String xingzuo;
    public static String maritalStatus;
    public static String schoolRecord;
    public static String datingPurpose;
    public static String personalDescription;
    public static int photesize = 0;
    public static boolean isContain=false;


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
                            userPhoto = data.getString("userPhoto");
                            nickname = data.getString("nickName");
                            job = data.getString("profession");
                            xingzuo =data.getString("constellation");
                            maritalStatus = data.getString("maritalStatus");
                            schoolRecord = data.getString("schoolRecord");
                            datingPurpose = data.getString("datingPurpose");
                            personalDescription = data.getString("personalDescription");

                            String resList = data.getString("resList");
                            photos = (ArrayList<PhotoBean>) JSON.parseArray(resList, PhotoBean.class);
                            photesize = photos.size();

//                            if (nickname.length()>2){
//                                String ss = nickname.substring(0, 2);
//                                if (ss.contains("hb")){
//                                    isContain =true;
//                                }
//                            }
                            if(nickname.startsWith("hb")){
                                isContain =true;
                            }else {
                                isContain = false;
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
//        mTintManager.setStatusBarTintResource(R.color.grabred_state_color);
        setContentView(R.layout.activity_grabred);

        getdata();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        grabPersonalFragment = new GrabPersonalFragment();
        fragmentTransaction.add(R.id.grabred_fl_content,grabPersonalFragment).show(grabPersonalFragment).commit();
        mTempFragment = grabPersonalFragment;
        fragment = mTempFragment;
        initView();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
    }

    private void initView() {
        ImageView mIvBackRow = (ImageView) findViewById(R.id.grabred_back_row);
        TextView mTvGrabredRole = (TextView) findViewById(R.id.grabred_role);
//        RadioGroup mRgContenter = (RadioGroup) findViewById(R.id.grabred_rg_container);
        mTvPersonal = (TextView) findViewById(R.id.grabred_rb_personal);
        mTvMerchant = (TextView) findViewById(R.id.grabred_rb_merchant);

        mTvPersonal.setOnClickListener(this);
        mTvMerchant.setOnClickListener(this);

        mIvBackRow.setOnClickListener(this);
        mTvGrabredRole.setOnClickListener(this);
//        mRgContenter.setOnCheckedChangeListener(this);
    }


    private void getdata() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(GrabRedActivity.this);
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
                GrabRedActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GrabRedActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grabred_back_row:
                finish();
                break;
            case R.id.grabred_role://红包规则
                Intent intent1 = new Intent(getApplicationContext(),WebActivity.class);
                intent1.putExtra("title","红包规则");
                intent1.putExtra("URL", Constant.URL+"/wap/redEnveRules.jhtml");
                startActivity(intent1);
                break;


                case R.id.grabred_rb_personal :
                    mTvPersonal.setTextColor(Color.parseColor("#52BDA1"));
                    mTvMerchant.setTextColor(Color.parseColor("#000000"));

                    if(grabPersonalFragment==null){
                        grabPersonalFragment = new GrabPersonalFragment();
                    }
                    fragment = null;
                    fragment = grabPersonalFragment;

                    if (fragment != mTempFragment) {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();

                        if (!fragment.isAdded()) {
                            fragmentTransaction.hide(mTempFragment)
                                    .add(R.id.grabred_fl_content, fragment).commit();
                        } else {
                            fragmentTransaction.hide(mTempFragment)
                                    .show(fragment).commit();
                        }
                        mTempFragment = fragment;
                    }
                    break;
                case R.id.grabred_rb_merchant :
                    mTvMerchant.setTextColor(Color.parseColor("#52BDA1"));
                    mTvPersonal.setTextColor(Color.parseColor("#000000"));
                    if(grabMerchantFragment==null){
                        grabMerchantFragment = new GrabMerchantFragment();
                    }
                    fragment = null;
                    fragment = grabMerchantFragment;


                    if (fragment != mTempFragment) {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();

                        if (!fragment.isAdded()) {
                            fragmentTransaction.hide(mTempFragment)
                                    .add(R.id.grabred_fl_content, fragment).commit();
                        } else {
                            fragmentTransaction.hide(mTempFragment)
                                    .show(fragment).commit();
                        }
                        mTempFragment = fragment;
                    }
                    break;




        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment= null;
        switch (checkedId){
            case R.id.grabred_rb_personal :
                if(grabPersonalFragment==null){
                    grabPersonalFragment = new GrabPersonalFragment();
                }
                fragment = grabPersonalFragment;
//                fragmentTransaction.show(grabPersonalFragment).hide(grabMerchantFragment).commit();
                break;
            case R.id.grabred_rb_merchant :
                if(grabMerchantFragment==null){
                    grabMerchantFragment = new GrabMerchantFragment();
                }
                fragment = grabMerchantFragment;
//                fragmentTransaction.show(grabMerchantFragment).hide(grabPersonalFragment).commit();
                break;
        }

        if (fragment != mTempFragment) {
            if (!fragment.isAdded()) {
                fragmentTransaction.hide(mTempFragment)
                        .add(R.id.grabred_fl_content, fragment).commit();
            } else {
                fragmentTransaction.hide(mTempFragment)
                        .show(fragment).commit();
            }
            mTempFragment = fragment;
        }


    }
}

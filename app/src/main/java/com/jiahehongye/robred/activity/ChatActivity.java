package com.jiahehongye.robred.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.util.NetUtils;
import com.jiahehongye.robred.AppHelper;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.fragment.ChatFragment;
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

/**
 * Created by huangjunhui on 2016/12/20.10:27
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvTitle;
    private String name;
    private String userId;
    private int chatType;
    private String userAvatar;

    private static final int GET_INFO = 123;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_INFO:
                    String sss = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(sss);
                        if (object.getString("result").equals("success")) {
                            JSONObject data = new JSONObject(object.getString("data"));
                            userPhoto = data.getString("userPhoto");
                            nickname = data.getString("nickName");
                            initView();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

    };
    private String userPhoto;
    private String nickname;

    /**
     * 聊天需要传的参数：
     * 1,聊天类型
     * 2,聊天人对象的id
     * 3,对方用户的头像
     * 4,对方用户的昵称
     *
     * 5,自己的头像
     * 6,自己的昵称
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        chatType = intent.getIntExtra(EaseConstant.EXTRA_CHAT_TYPE,1);
        userId = intent.getStringExtra(EaseConstant.EXTRA_USER_ID);
        name = intent.getStringExtra("Name");//对方的 昵称
        userAvatar = intent.getStringExtra("Avatar"); //对方的头像

        getInfodata();

        EMClient.getInstance().addConnectionListener(new MyConnectionListener());

    }

    private void getInfodata() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.MEMBERINFO)
                .post(body)
                .build();


        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ChatActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChatActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                Message msg = handler.obtainMessage();
                msg.what = GET_INFO;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });

    }

    private void initView() {
        ImageView mIvBack = (ImageView) findViewById(R.id.application_iv_back);
        mTvTitle = (TextView) findViewById(R.id.application_tv_title);
        mIvBack.setOnClickListener(this);
        mTvTitle.setText(name);

//        UserDao userDao = new UserDao(UIUtils.getContext());
//        String mobile= (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_MOBILE, "");
//        EaseUser user = userDao.getContactList().get(mobile);

        String avatar = userPhoto;


        EaseChatFragment easeChatFragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, chatType);
        args.putString(EaseConstant.EXTRA_USER_ID, userId);

        //发送的
        args.putString("Avatar",userAvatar);//好友的
        args.putString("Name",name);
        args.putString("Avatar2",avatar);//本人的
        args.putString("Name2",nickname);

        easeChatFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.chat_fl_contaner,easeChatFragment).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.application_iv_back :
                finish();
                break;
        }
    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }
        @Override
        public void onDisconnected(final int error) {
            UIUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    if(error == EMError.USER_REMOVED){
                        // 显示帐号已经被移除
                        Toast.makeText(ChatActivity.this, "帐号已经被移除!", Toast.LENGTH_SHORT).show();
                    }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                        Toast.makeText(ChatActivity.this, "帐号在其他设备登录!", Toast.LENGTH_SHORT).show();

                    } else {
                        if (NetUtils.hasNetwork(ChatActivity.this)){//连接不到聊天服务器
                            AppHelper.getInstance().logoutHuanXin();
                            AppHelper.getInstance().loginHuanXin();
                        }else{//当前网络不可用，请检查网络设置
                            Toast.makeText(ChatActivity.this, "当前网络不可用，请检查网络设置!", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            });

        }
    }
}

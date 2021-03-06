package com.jiahehongye.robred;

import android.content.Context;
import android.content.IntentFilter;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.jiahehongye.robred.application.BaseApplication;
import com.jiahehongye.robred.bean.LoginResult;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.db.Model;
import com.jiahehongye.robred.receive.CallReceiver;
import com.jiahehongye.robred.utils.DesUtil;
import com.jiahehongye.robred.utils.LocationUtils;
import com.jiahehongye.robred.utils.LogUtil;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by huangjunhui on 2016/12/22.10:02
 */
public class AppHelper {

    private static AppHelper instance;
    private EaseUI easeUI;
    private Model model;
    private Map<String, EaseUser> contactList;
    public boolean isVideoCalling = true;
    public boolean isVoiceCalling = true;
    private CallReceiver callReceiver;

    public synchronized static AppHelper getInstance() {
        if (instance == null) {
            instance = new AppHelper();
        }
        return instance;
    }


    /**
     * 初始化操作
     *
     * @param context
     */
    public void init(Context context) {
        model = new Model(context);
        easeUI = EaseUI.getInstance();
        setEaseUIProviders();
        initLocation();//初始化位置信息

        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        if(callReceiver == null){
            callReceiver = new CallReceiver();
        }

        //register incoming call receiver
        context.registerReceiver(callReceiver, callFilter);

    }


    public void loginHuanXin() {
        String currentUser = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_MOBILE, "");
        if (currentUser != null && currentUser != "") {

            if (EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().getCurrentUser().equals(currentUser)) {
                EMClient.getInstance().chatManager().loadAllConversations();
                return;
            }

            EMClient.getInstance().login(currentUser, "admin123", new EMCallBack() {//回调
                @Override
                public void onSuccess() {
                    EMClient.getInstance().chatManager().loadAllConversations();
                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {

                }
            });
        }

    }


    private void initLocation() {
        LocationUtils.getInstance().initLocation(UIUtils.getContext());

    }


    /**
     * 设置信息提供者
     */
    private void setEaseUIProviders() {
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });

    }

    /**
     * 得到当前用户的信息
     *
     * @param username 环信id
     * @return EaseUser
     */
    private EaseUser getUserInfo(String username) {
        EaseUser user = null;
        user = getContactList().get(username);
        if (user == null) {
            user = new EaseUser(username);
        }
        return user;
    }

    /**
     * 得到所有联系人列表
     *
     * @return
     */
    public Map<String, EaseUser> getContactList() {
        contactList = model.getContactList();
        if (contactList == null) {
            return new Hashtable<>();
        }
        return contactList;
    }

    /**
     * 得到当前用户的环信id
     *
     * @return
     */
    public String getCurrentUsernName() {
        return (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_MOBILE, "");
    }

    public void logoutHuanXin() {
        EMClient.getInstance().logout(true);
    }


    /**
     * 这个是用来发送jpush的注册id到服务器的，以方便后面单独设备推送消息。
     *
     * @param regId
     */
    public void registerId(String regId) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(UIUtils.getContext());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {
            json.put("registrationId", regId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.DEVICEREGISTER)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                UIUtils.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        registerId(BaseApplication.registrationId);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

    }

    public void loginMySelefServer() {
        String userName = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_MOBILE, "");
        String password = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_PASSWORD, "");
        if (userName == "" || password == "") {
            return;
        }
        String encryptPassword = DesUtil.encrypt(password);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(UIUtils.getContext());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", userName);
            jsonObject.put("password", encryptPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.LOGIN_URL)
                .post(body)
                .build();

        LogUtil.LogShitou("登陆请求：", "mobile=" + userName + ",password=" + encryptPassword);

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LoginResult loginResult = new Gson().fromJson(result, LoginResult.class);
                if(null==loginResult.getData()){
                    return;
                }
                String id = loginResult.getData().getId(); //唯一标示
                String memberRank = loginResult.getData().getMemberRank();//会员等级
                String memberType = loginResult.getData().getMemberType();//会员类型
                String mobile = loginResult.getData().getMobile();//会员手机
                String redIntegral = loginResult.getData().getRedIntegral();//积分

                SPUtils.put(UIUtils.getContext(), Constant.LOGIN_ID, id);
                SPUtils.put(UIUtils.getContext(), Constant.LOGIN_MEMBERRANK, memberRank);
                SPUtils.put(UIUtils.getContext(), Constant.LOGIN_MEMBERTYPE, memberType);
                SPUtils.put(UIUtils.getContext(), Constant.LOGIN_MOBILE, mobile);
                SPUtils.put(UIUtils.getContext(), Constant.LOGIN_REDINTEGRAL, redIntegral);

            }
        });
    }

    /**
     * if ever logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }
}

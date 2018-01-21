package com.jiahehongye.robred.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.easeui.domain.EaseUser;
import com.jiahehongye.robred.BaseFragment;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.AboutUsActivity;
import com.jiahehongye.robred.activity.DiamondAccountActivity;
import com.jiahehongye.robred.activity.FeedBackActivity;
import com.jiahehongye.robred.activity.MyGiftActivity;
import com.jiahehongye.robred.activity.MyOrderActivity;
import com.jiahehongye.robred.activity.MyredActivity;
import com.jiahehongye.robred.activity.PersonalActivity;
import com.jiahehongye.robred.activity.RechargeBalanceActivity;
import com.jiahehongye.robred.activity.RelationActivity;
import com.jiahehongye.robred.activity.SetActivity;
import com.jiahehongye.robred.activity.SnatchOrderActivity;
import com.jiahehongye.robred.activity.WebActivity;
import com.jiahehongye.robred.bean.Account_AllBean;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.db.Model;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.GlideCircleTransform;
import com.jiahehongye.robred.view.MyProgressDialog;
import com.jiahehongye.robred.view.ZoomListView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

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
 * Created by Administrator on 2016/11/30.
 */
public class PersonalFragment extends BaseFragment implements View.OnClickListener {

    //动态控件
//    private TextView mUserName;
//    private TextView mAccount, mMyred, mAccountBalance;
    private PersistentCookieStore persistentCookieStore;
    private MyProgressDialog animDialog;
    private Call call;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_LOGS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SET_DEBUG_APP,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.WRITE_APN_SETTINGS

    };
    private ArrayList<Account_AllBean> allBeen = new ArrayList<>();
    private static final int GET_ALL = 0000;
    private static final int GET_INFO = 0006;
    private String toFtl;
    private String leaveWord;
    private String userPhoto;
    private String nickname;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_ALL:
//                    animDialog.dismiss();
                    String s = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(s);
                        if (object.getString("result").equals("success")) {
                            JSONObject object1 = new JSONObject(object.getString("data"));
                            mAccount.setText(object1.getString("accountAmount"));
                            mMyred.setText(object1.getString("redEnveRemainAmount"));
                            mAccountBalance.setText(object1.getString("rechargeRemainAmount"));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case 555:
                    String share = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(share);
                        if (object.getString("result").equals("success")) {
                            JSONObject object1 = new JSONObject(object.getString("data"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case GET_INFO:
                    String sss = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(sss);
                        if (object.getString("result").equals("success")) {
                            JSONObject data = new JSONObject(object.getString("data"));
                            userPhoto = data.getString("userPhoto");
                            nickname = data.getString("nickName");

                            Glide.with(UIUtils.getContext()).load(userPhoto).transform(new GlideCircleTransform(UIUtils.getContext())).placeholder(R.mipmap.avatar).into(mine_iv_userhead);
                            mUserName.setText(nickname);

                            SPUtils.put(UIUtils.getContext(), Constant.USER_HOBBY, data.getString("hobbies"));


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case 521:
                    String getshare = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(getshare);
                        if (object.getString("result").equals("success")) {
                            JSONObject object2 = new JSONObject(object.getString("data"));
                            toFtl = object2.getString("toFtl");
                            leaveWord = object2.getString("leaveWord");

                            int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                // We don't have permission so prompt the user
                                ActivityCompat.requestPermissions(
                                        getActivity(),
                                        PERMISSIONS_STORAGE,//需要请求的所有权限，这是个数组String[]
                                        REQUEST_EXTERNAL_STORAGE//请求码
                                );
                            }
                            // 用来弹出错误日志
                            com.umeng.socialize.utils.Log.LOG = true;


                            UMImage image = new UMImage(getActivity(), R.drawable.logo);//设置分享图片
                            UMImage thumb = new UMImage(getActivity(), R.drawable.logo);//设置缩略图
                            image.compressStyle = UMImage.CompressStyle.SCALE;
                            image.setThumb(thumb);
                            ShareAction mAction = new ShareAction(getActivity())
                                    .withTitle(leaveWord)
                                    .withText("金猴宝红包")
                                    .withTargetUrl(toFtl)
                                    .withMedia(image)
                                    .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                                    .setCallback(umShareListener);
                            mAction.open();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    private ImageView mine_iv_userhead;
    private TextView mine_tv_username;
    private boolean isFirst;
    private LinearLayout mLLhh;
    private ImageView mIvGuid;
    private View view;
    private ZoomListView listView;
    private TextView mUserName;
    private TextView mAccount;
    private TextView mMyred;
    private TextView mAccountBalance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainUi.applyKitKatTranslucency();
        view = inflater.from(getActivity()).inflate(R.layout.fragment_mine, null);

        initView(view);


        mUserName = (TextView) view.findViewById(R.id.mine_tv_username);
        mAccount = (TextView) view.findViewById(R.id.mine_tv_account);
        mMyred = (TextView) view.findViewById(R.id.mine_tv_myred);
        mAccountBalance = (TextView) view.findViewById(R.id.mine_tv_account_balance);
        LinearLayout changeMyself = (LinearLayout) view.findViewById(R.id.mine_ll_change_myself);
        changeMyself.setOnClickListener(this);
        mLLhh = (LinearLayout) view.findViewById(R.id.personal_ll_hh);
        mine_iv_userhead = (ImageView) view.findViewById(R.id.mine_iv_userhead);
//        mIvGuid = (ImageView) view.findViewById(R.id.personal_iv_guid);


//        /**------------------------------------------------*/
//        //设置用户的头像和昵称
//        mine_iv_userhead = (ImageView) view.findViewById(R.id.mine_iv_userhead);
//
//        String mobile = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_MOBILE, "");
//        Model model = new Model(UIUtils.getContext());
//        EaseUser user = model.getContactList().get(mobile);
//        if (user != null) {
//            String avatar = user.getAvatar();
//            String nick = user.getNick();
//            Glide.with(UIUtils.getContext()).load(avatar).transform(new GlideCircleTransform(UIUtils.getContext())).into(mine_iv_userhead);
//            mUserName.setText(nick);
//        }
//        /**------------------------------------------------*/
//
////        isFirst = (boolean) SPUtils.get(UIUtils.getContext(), Constant.FIRST_COMEIN_PERSONAL, true);
////        if (!isFirst) {
//
////        }

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_EXTERNAL_STORAGE:
                try {

                }catch (RuntimeException e){

                }
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getInfodata() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
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

    private void initView(View view) {
        LinearLayout account = (LinearLayout) view.findViewById(R.id.mine_ll_account);
        LinearLayout myRed = (LinearLayout) view.findViewById(R.id.mine_ll_myred);
        LinearLayout accountBalance = (LinearLayout) view.findViewById(R.id.mine_ll_account_balance);
        LinearLayout myDiamond = (LinearLayout) view.findViewById(R.id.mine_ll_my_diamond);
        LinearLayout myPresent = (LinearLayout) view.findViewById(R.id.mine_ll_my_present);
        LinearLayout robCenter = (LinearLayout) view.findViewById(R.id.mine_ll_rob_center);
        LinearLayout myComment = (LinearLayout) view.findViewById(R.id.mine_ll_my_comment);
        LinearLayout serviceCenter = (LinearLayout) view.findViewById(R.id.mine_ll_service_center);
        LinearLayout inviteFriend = (LinearLayout) view.findViewById(R.id.mine_ll_invite_friend);
        LinearLayout suggestionBack = (LinearLayout) view.findViewById(R.id.mine_ll_suggestion_back);
        LinearLayout aboutUs = (LinearLayout) view.findViewById(R.id.mine_ll_about_us);
        RelativeLayout mine_rl_setting = (RelativeLayout) view.findViewById(R.id.mine_rl_setting);
        LinearLayout mine_all_order = (LinearLayout) view.findViewById(R.id.mine_all_order);
        LinearLayout mine_waitpay_order = (LinearLayout) view.findViewById(R.id.mine_waitpay_order);
        LinearLayout mine_waitsend_order = (LinearLayout) view.findViewById(R.id.mine_waitsend_order);
        LinearLayout mine_waitrefund_order = (LinearLayout) view.findViewById(R.id.mine_waitrefund_order);


        account.setOnClickListener(this);
        myRed.setOnClickListener(this);
        accountBalance.setOnClickListener(this);
        myDiamond.setOnClickListener(this);
        myPresent.setOnClickListener(this);
        robCenter.setOnClickListener(this);
        myComment.setOnClickListener(this);
        serviceCenter.setOnClickListener(this);
        inviteFriend.setOnClickListener(this);
        suggestionBack.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        mine_rl_setting.setOnClickListener(this);
        mine_all_order.setOnClickListener(this);
        mine_waitpay_order.setOnClickListener(this);
        mine_waitsend_order.setOnClickListener(this);
        mine_waitrefund_order.setOnClickListener(this);
    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            sharedata();
            Toast.makeText(getActivity(), "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getActivity(), "分享失败", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(getActivity(),"分享取消", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mMainUi.applyKitKatTranslucency();
            mMainUi.mTintManager.setStatusBarTintResource(R.color.home_state_color);
        }
    }




    private void getshare() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.GET_SHARE)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
//                        animDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                Message msg = handler.obtainMessage();
                msg.what = 521;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });

    }


    private void sharedata() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.SHARE_SUCCESS)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
//                        animDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                Message msg = handler.obtainMessage();
                msg.what = 555;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });

    }




    private void getdata() {
//        showMyDialog();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();
        JSONObject jsonObject = new JSONObject();
        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.GET_TOTAL)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
//                        animDialog.dismiss();
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

    /**
     * 显示对话框
     */
    public void showMyDialog() {
        animDialog = new MyProgressDialog(getActivity(), "玩命加载中...", R.drawable.loading);
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

    @Override
    public void onResume() {
        super.onResume();
        isFirst = (boolean) SPUtils.get(UIUtils.getContext(), Constant.FIRST_COMEIN_PERSONAL, true);
        getdata();
        getInfodata();

    }

    public void refresh() {
        String mobile = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_MOBILE, "");
        Model model = new Model(UIUtils.getContext());
        EaseUser user = model.getContactList().get(mobile);
        if (user != null) {
            String avatar = user.getAvatar();
            String nick = user.getNick();
//            Glide.with(UIUtils.getContext()).load(avatar).transform(new GlideCircleTransform(UIUtils.getContext())).into(mine_iv_userhead);
//            mUserName.setText(nick);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_rl_setting://设置中心
                startActivity(new Intent(getActivity(), SetActivity.class));
                break;
            case R.id.mine_ll_change_myself://修改个人资料
                startActivity(new Intent(getActivity(), PersonalActivity.class));
                SPUtils.put(UIUtils.getContext(), Constant.FIRST_COMEIN_PERSONAL, false);
                break;
            case R.id.mine_ll_account://账户总额
//                startActivity(new Intent(getActivity(), AccountActivity.class));
                break;
            case R.id.mine_ll_myred://我的红包
                startActivity(new Intent(getActivity(), MyredActivity.class));
                break;
            case R.id.mine_ll_account_balance://充值余额
                startActivity(new Intent(getActivity(), RechargeBalanceActivity.class));
                break;
            case R.id.mine_ll_my_diamond://我的钻石
                startActivity(new Intent(getActivity(), DiamondAccountActivity.class));
                break;
            case R.id.mine_ll_my_present://我的礼物
                startActivity(new Intent(getContext(), MyGiftActivity.class));
                break;
            case R.id.mine_ll_rob_center://夺宝中心
                startActivity(new Intent(getContext(), SnatchOrderActivity.class).putExtra("title","夺宝中心"));
                break;
            case R.id.mine_ll_my_comment://我的关系
                startActivity(new Intent(getContext(), RelationActivity.class));
                break;
            case R.id.mine_ll_service_center://客服中心
                Intent intentk = new Intent(getActivity(),WebActivity.class);
                intentk.putExtra("title","客服中心");
                intentk.putExtra("URL", Constant.URL+"/wap/customerServiceCenter.jhtml");
                startActivity(intentk);
                break;
            case R.id.mine_ll_invite_friend://邀请好友
//                //6.0权限
//                if(Build.VERSION.SDK_INT>=23){
//                    String[] mPermissionList = new String[]{
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                            Manifest.permission.ACCESS_FINE_LOCATION,
//                            Manifest.permission.CALL_PHONE,
//                            Manifest.permission.READ_LOGS,
//                            Manifest.permission.READ_PHONE_STATE,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                            Manifest.permission.SET_DEBUG_APP,
//                            Manifest.permission.SYSTEM_ALERT_WINDOW,
//                            Manifest.permission.GET_ACCOUNTS,
//                            Manifest.permission.WRITE_APN_SETTINGS};
//                    ActivityCompat.requestPermissions(getActivity(),mPermissionList,123);
//                }
//                // 用来弹出错误日志
//                com.umeng.socialize.utils.Log.LOG = true;
//
//
//                UMImage image = new UMImage(getActivity(), R.drawable.logo);//设置分享图片
//                UMImage thumb =  new UMImage(getActivity(), R.drawable.logo);//设置缩略图
//                image.compressStyle = UMImage.CompressStyle.SCALE;
//                image.setThumb(thumb);
//                ShareAction mAction = new ShareAction(getActivity())
//                        .withTitle("金猴宝红包")
//                        .withText("抢红包，约秘聊，您的好友给您送献花了，赶快来领取吧!")
//                        .withTargetUrl("http://www.baidu.com")
//                        .withMedia(image)
//                        .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
//                        .setCallback(umShareListener);
//                mAction.open();
                getshare();
                break;
            case R.id.mine_ll_suggestion_back://意见反馈
                startActivity(new Intent(getContext(), FeedBackActivity.class));
                break;
            case R.id.mine_ll_about_us://关于我们
                startActivity(new Intent(getContext(), AboutUsActivity.class));
                break;
            case R.id.mine_all_order:
                Intent intent = new Intent(getActivity(), MyOrderActivity.class);
                intent.putExtra("order_tag", "1");
                startActivity(intent);
                break;
            case R.id.mine_waitpay_order:
                Intent intent1 = new Intent(getActivity(), MyOrderActivity.class);
                intent1.putExtra("order_tag", "2");
                startActivity(intent1);
                break;
            case R.id.mine_waitsend_order:
                Intent intent2 = new Intent(getActivity(), MyOrderActivity.class);
                intent2.putExtra("order_tag", "3");
                startActivity(intent2);
                break;
            case R.id.mine_waitrefund_order:
                Intent intent3 = new Intent(getActivity(), MyOrderActivity.class);
                intent3.putExtra("order_tag", "4");
                startActivity(intent3);
                break;
        }
    }


}

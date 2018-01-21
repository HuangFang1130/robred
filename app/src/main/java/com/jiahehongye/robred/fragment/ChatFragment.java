package com.jiahehongye.robred.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.ContanctDetailActivity;
import com.jiahehongye.robred.activity.RechargeActivity;
import com.jiahehongye.robred.activity.VideoCallActivity;
import com.jiahehongye.robred.activity.VoiceCallActivity;
import com.jiahehongye.robred.adapter.ChatGiftPagerAdapter;
import com.jiahehongye.robred.bean.ChatGiftResult;
import com.jiahehongye.robred.bean.ChatSendGiftResult;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.db.UserDao;
import com.jiahehongye.robred.utils.LogUtil;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.TUtils;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.ChatPopPager;
import com.jiahehongye.robred.view.GlideCircleTransform;

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

/**
 * Created by huangjunhui on 2016/12/21.13:45
 */
public class ChatFragment extends EaseChatFragment implements EaseChatFragment.onClickGiftLinstener, View.OnClickListener, ViewPager.OnPageChangeListener, EaseChatFragment.EaseChatFragmentHelper {


    private static final int GIFT_SUCCESS = 2;
    private static final int SEND_GIFT_SUCCESS = 3;
    private static final int TIMER = 201;
    private ChatGiftPagerAdapter chatGiftPagerAdapter;
    private ArrayList<ChatPopPager> arrayLists;
    private String avatar;
    private String name;
    private String avatar2;
    private String name2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GIFT_SUCCESS:
                    parserDate(msg.obj);
                    break;
                case SEND_GIFT_SUCCESS:
                    String result = (String) msg.obj;
                    LogUtil.LogShitou("SEND_GIFT_SUCCESS : ", result);
                    try {
                        ChatSendGiftResult chatSendGiftResult = new Gson().fromJson(result, ChatSendGiftResult.class);

                        if (chatSendGiftResult.getResult().equals("success")) {//制作一个文本消息发送过去。
                            timer = 3;
                            mPopTtimer.setText("" + timer + "s");
                            handler.removeMessages(TIMER);
                            value++;
                            mPopTvFensu.setText("X" + value);
                            Message message = handler.obtainMessage(TIMER);     // Message
                            handler.sendMessageDelayed(message, 1000);
                            mPopTtimer.setVisibility(View.VISIBLE);

                        } else if (chatSendGiftResult.getResult().equals("fail")) {
                            String errorMsg = chatSendGiftResult.getErrorMsg();
                            TUtils.showToast(getActivity(), errorMsg);
                            mPopTtimer.setVisibility(View.GONE);
                            mPopTvFensu.setText("");
                            value = 0;

                        } else {

                        }
                    } catch (Exception e) {
                    }


                    LogUtil.LogShitou("result: " + result.toString());
                    break;

                case TIMER:
                    timer--;
                    mPopTtimer.setText("" + timer + "s");

                    if (timer > 0) {
                        Message message = handler.obtainMessage(TIMER);
                        handler.sendMessageDelayed(message, 1000);      // send message
                    } else {
                        mPopTtimer.setVisibility(View.GONE);
                        sendSpeacileMessage();
                        mPopTvFensu.setText("");
                        value = 0;
                    }
                    break;

            }
        }
    };


    private List<ChatGiftResult.DataBean.GiftLBean> giftList;
    private String result;
    private PopupWindow popupWindow;
    private String diamond;
    private String usernameHuanXinId;
    private ChatPopPager chatPopPager;
    private int currentSelectorPager = 0;
    private TranslateAnimation inAnim;
    private FrameLayout mFlAin;
    private AnimationSet set;
    private EaseUser user;
    private int type;
    private String giftId;
    private String giftName;
    private String gifImage;
    private String gifDimon;
    private TextView mTvMasonry;
    private TextView mPopTvFensu;
    private int value = 0;
    private TextView mPopTtimer;
    private int timer = 3;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        usernameHuanXinId = arguments.getString(EaseConstant.EXTRA_USER_ID);
        this.setOnClickGiftListener(this);
        reqeustDate();
        avatar = fragmentArgs.getString("Avatar");
        name = fragmentArgs.getString("Name");
        avatar2 = fragmentArgs.getString("Avatar2");
        name2 = fragmentArgs.getString("Name2");
        this.setChatFragmentListener(this);
        String[] perms = {"android.permission.RECORD_AUDIO"};
        boolean b = UIUtils.hasPermission("android.permission.RECORD_AUDIO");
        if (!b) {
            requestPermissions(perms, 200);
        }

    }


    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        switch (permsRequestCode) {
            case 200:
                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (cameraAccepted) {
                } else {
                }
                break;
        }

    }

    /**
     * 请求网络数据
     */
    private void reqeustDate() {
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(getActivity())))
                .build();

        JSONObject jsonObject = new JSONObject();
        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.CHAT_GIFT_LIST)
                .post(body)
                .build();


        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = GIFT_SUCCESS;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onClickGift() {
        showPopupWindow();
    }

    private void showPopupWindow() {

        if (giftList == null || giftList.size() < 0) {
            return;
        }

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.ease_chat_popwindow, null);
        Button mPopBtnSend = (Button) contentView.findViewById(R.id.chat_pop_btn_send);
        mPopTtimer = (TextView) contentView.findViewById(R.id.pop_timer);

        mPopTvFensu = (TextView) contentView.findViewById(R.id.et_fenshu);
        TextView mTvRecharge = (TextView) contentView.findViewById(R.id.chat_pop_recharge);
        mTvMasonry = (TextView) contentView.findViewById(R.id.chat_pop_tv_masonry);
        mTvMasonry.setText("钻石： " + diamond);
        ViewPager mPopViewpager = (ViewPager) contentView.findViewById(R.id.chat_pop_viewpager);
        mPopViewpager.setOnPageChangeListener(this);
        int i1 = giftList.size() / 8;
        int i2 = giftList.size() % 8;
        arrayLists = new ArrayList<>();
        if (i1 <= 0) {
            chatPopPager = new ChatPopPager(getActivity(), giftList);
            arrayLists.add(chatPopPager);
        } else if (i1 == 1 && i2 == 0) {
            chatPopPager = new ChatPopPager(getActivity(), giftList);
            arrayLists.add(chatPopPager);
        } else if (i1 == 1 && i2 != 0) {
            for (int i = 0; i <= i1; i++) {
                if (i < i1) {
                    chatPopPager = new ChatPopPager(getActivity(), giftList.subList(8 * i, 8 * (i + 1)));
                } else {
                    chatPopPager = new ChatPopPager(getActivity(), giftList.subList(8 * i, giftList.size()));
                }
                arrayLists.add(chatPopPager);
            }
        } else {

        }

        chatGiftPagerAdapter = new ChatGiftPagerAdapter(getActivity(), arrayLists);
        mPopViewpager.setAdapter(chatGiftPagerAdapter);
        mPopBtnSend.setOnClickListener(this);
        mTvRecharge.setOnClickListener(this);
        mPopTvFensu.setOnClickListener(this);
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(inflate, Gravity.BOTTOM, 5, 5);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.chat_pop_btn_send://发送礼物
                sendGift();
                break;

            case R.id.chat_pop_recharge://去充值
                gotoRecharge();
                break;

            case R.id.et_fenshu:
                break;
        }
    }

    /**
     * 去充值
     */
    private void gotoRecharge() {
        Intent intent = new Intent(getActivity(), RechargeActivity.class);
        startActivity(intent);
    }

    /**
     * 发送礼物
     */
    private void sendGift() {
        giftId = arrayLists.get(currentSelectorPager).getGiftId();
        giftName = arrayLists.get(currentSelectorPager).getGifName();
        gifImage = arrayLists.get(currentSelectorPager).getGifImage();
        gifDimon = arrayLists.get(currentSelectorPager).getGifDimon();
        LogUtil.LogShitou("gifImage  : " + gifImage);
        if (giftId == "" || gifImage == "" || gifImage == null || giftId == null) {
            Toast.makeText(getActivity(), "请选择礼物！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (gifDimon.equals("0")) {
            type = 1;
        } else {
            type = 2;
        }
        sendGiftToServer(type, giftId, usernameHuanXinId);

    }

    /**
     * 向服务器发送数据
     *
     * @param type              1:鲜花  2：非鲜花
     * @param giftId            接收礼物用户的手机号
     * @param usernameHuanXinId 礼物ID
     */
    private void sendGiftToServer(int type, String giftId, String usernameHuanXinId) {

        LogUtil.LogShitou("type" + type + "giftId" + giftId + "huanxinid" + usernameHuanXinId);
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(getActivity())))
                .build();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", type + "");
            jsonObject.put("mobile", usernameHuanXinId);
            jsonObject.put("giftId", giftId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.CHAT_SEND_GIFT)
                .post(body)
                .build();


        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = SEND_GIFT_SUCCESS;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    private void sendSpeacileMessage() {
        EMMessage message = EMMessage.createTxtSendMessage(giftName + mPopTvFensu.getText().toString(), toChatUsername);
        message.setAttribute("Gift", "1");
        message.setAttribute("GiftImgUrl", gifImage);
        message.setAttribute("Name", name2);//自己的
        message.setAttribute("Avatar", avatar2);
        message.setAttribute("Avatar2", avatar);//对方的 移花接木
        message.setAttribute("Name2", name);
        EMClient.getInstance().chatManager().sendMessage(message);
        messageList.refresh();


    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        currentSelectorPager = i;

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    /**
     * 解析数据
     *
     * @param obj
     */
    private void parserDate(Object obj) {
        result = (String) obj;
        ChatGiftResult chatGiftResult = new Gson().fromJson(result, ChatGiftResult.class);
        ChatGiftResult.DataBean data = chatGiftResult.getData();
        if (data != null) {
            diamond = data.getDiamond();
            giftList = data.getGiftL();
        } else {
            diamond = "";
            giftList = null;
        }

    }

    /**
     * 初始化动画
     */
    public void initAnimatar() {
        set = new AnimationSet(true);
        inAnim = new TranslateAnimation(-150, 250, 0, 0);
        inAnim.setDuration(2000);//设置动画持续时间
        inAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        inAnim.setRepeatCount(0);//设置重复次数
        set.addAnimation(inAnim);

    }

    /**
     * 展示动画
     *
     * @param chatSendGiftResult
     */
    private void showAnimation(ChatSendGiftResult chatSendGiftResult) {
        diamond = chatSendGiftResult.getData().getDiamond();
        mTvMasonry.setText(diamond);
        //sendUnvarnishedMessage();
        UserDao userDao = new UserDao(UIUtils.getContext());
        String mobile = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_MOBILE, "");
        user = userDao.getContactList().get(mobile);

        mFlAin = (FrameLayout) inflate.findViewById(R.id.chat_fl_anim);
        ImageView mIvUserAvatar = (ImageView) inflate.findViewById(R.id.gift_userheader_iv);
        TextView mTvUserName = (TextView) inflate.findViewById(R.id.gift_usernickname_tv);
        ImageView mIvGift = (ImageView) inflate.findViewById(R.id.animation_light);
        TextView mTvGiftName = (TextView) inflate.findViewById(R.id.gift_usersign_tv);
        mTvGiftName.setText("送一个" + giftName);
        Glide.with(UIUtils.getContext()).load(user.getAvatar()).transform(new GlideCircleTransform(UIUtils.getContext())).into(mIvUserAvatar);
        Glide.with(UIUtils.getContext()).load(gifImage).into(mIvGift);
        mTvUserName.setText(user.getNick());

        mFlAin.setVisibility(View.VISIBLE);

        if (set != null) {
            mFlAin.setAnimation(set);
            set.startNow();
        }

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mFlAin.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 发送透传消息
     */
    private void sendUnvarnishedMessage() {
        // EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
        //
        // 支持单聊和群聊，默认单聊，如果是群聊添加下面这行
        // String action="showanimation";//action可以自定义
        // EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        // String toUsername = usernameHuanXinId;//发送给某个人
        // cmdMsg.setReceipt(toUsername);
        // cmdMsg.addBody(cmdBody);
        // EMClient.getInstance().chatManager().sendMessage(cmdMsg);
    }


    //发送扩展消息
    @Override
    public void onSetMessageAttributes(EMMessage message) {
        message.setAttribute("Name", name2);//自己的
        message.setAttribute("Avatar", avatar2);
        message.setAttribute("Avatar2", avatar);//对方的 移花接木
        message.setAttribute("Name2", name);
        message.setAttribute("Gift", "0");
    }

    @Override
    public void onEnterToChatDetails() {

    }

    @Override
    public void onAvatarClick(String username) {
        Intent intent = new Intent(getActivity(), ContanctDetailActivity.class);
        intent.putExtra("mobile",username);
        startActivity(intent);
    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;
    @Override
    protected void registerExtendMenuItem() {
        super.registerExtendMenuItem();

//        inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.em_chat_video_selector, ITEM_VIDEO, extendMenuItemClickListener);


        inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.drawable.em_chat_voice_call_selector, ITEM_VOICE_CALL, extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.drawable.em_chat_video_call_selector, ITEM_VIDEO_CALL, extendMenuItemClickListener);

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {

        switch (itemId) {


            case ITEM_VOICE_CALL:
                startVoiceCall();
                break;
            case ITEM_VIDEO_CALL:
                startVideoCall();
                break;
            default:
                break;
        }
        return false;


    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }


    /**
     * make a voice call
     */
    protected void startVoiceCall() {
        if (!EMClient.getInstance().isConnected()) {
//            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(getActivity(), VoiceCallActivity.class).putExtra("username", toChatUsername)
                    .putExtra("Name",name)
                    .putExtra("Avatar",avatar)
                    .putExtra("Avatar2",avatar2)
                    .putExtra("Name2",name2)
                    .putExtra("isComingCall", false)

            );
            // voiceCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }

//    message.setAttribute("Name", name2);//自己的
//    message.setAttribute("Avatar", avatar2);
//    message.setAttribute("Avatar2", avatar);//对方的 移花接木
//    message.setAttribute("Name2", name);
//    message.setAttribute("Gift", "0");
    /**
     * make a video call
     */
    protected void startVideoCall() {
        if (!EMClient.getInstance().isConnected()) {
//            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        }else {
            startActivity(new Intent(getActivity(), VideoCallActivity.class).putExtra("username", toChatUsername)
                    .putExtra("isComingCall", false)
                    .putExtra("Name",name)
                    .putExtra("Avatar",avatar)
                    .putExtra("Avatar2",avatar2)
                    .putExtra("Name2",name2)

            );
            // videoCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }
}

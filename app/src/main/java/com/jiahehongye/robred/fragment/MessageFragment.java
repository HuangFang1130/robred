package com.jiahehongye.robred.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;
import com.jiahehongye.robred.BaseFragment;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.ChatActivity;
import com.jiahehongye.robred.activity.InteractionActivity;
import com.jiahehongye.robred.activity.NotifyActivity;
import com.jiahehongye.robred.activity.SystemActivity;
import com.jiahehongye.robred.adapter.MessageRecycleAdapter;
import com.jiahehongye.robred.bean.MessageResult;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.db.Model;
import com.jiahehongye.robred.db.UserDao;
import com.jiahehongye.robred.interfaces.MyHeadViewClickListener;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.LogUtil;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.DividerItemDecoration;
import com.jiahehongye.robred.view.MyProgressDialog;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/30.
 * <p/>
 * 消息列表
 */
public class MessageFragment extends BaseFragment {
    private final static int MSG_REFRESH = 2;
    private SwipeRefreshLayout mMessageSwiperefresh;
    private RecyclerView mMessageRecycle;
    private LinearLayoutManager linearLayoutManager;
    private MessageRecycleAdapter messageRecycleAdapter;
    public static final int MESSAGE_UNREAD_SUCCESS = 204;

    private MessageResult messageResult;
    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:

                    break;
                case 1:
                    break;

                case MSG_REFRESH: {
                    emConversations.clear();
                    emConversations.addAll(loadConversationList());
                    messageRecycleAdapter.hintFootView();
                    messageRecycleAdapter.notifyDataSetChanged();
                    break;
                }

                case MESSAGE_UNREAD_SUCCESS:
                    String result = (String) msg.obj;
                    try {
                        messageResult = new Gson().fromJson(result, MessageResult.class);
                    } catch (Exception e) {

                    }
                    if (mMessageSwiperefresh != null) {
                        mMessageSwiperefresh.setRefreshing(false);
                    }
                    animDialog.dismiss();

                    messageRecycleAdapter.hintFootView();
                    messageRecycleAdapter.setHeadDate(messageResult);
                    messageRecycleAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };


    protected EMConversationListener convListener = new EMConversationListener() {

        @Override
        public void onCoversationUpdate() {
            refresh();
        }

    };
    private MyProgressDialog animDialog;

    /**
     * 显示对话框
     */
    public void showMyDialog() {
        animDialog = new MyProgressDialog(getActivity(), "玩命加载中...", R.drawable.loading);
        animDialog.show();
        animDialog.setCancelable(true);

    }

    private List<EMConversation> emConversations;
    private MyMessageListener myMessageListener;

    private String userName;//对方的环信名字

    /**
     * 刷新
     */
    private void refresh() {
        if (!handler.hasMessages(MSG_REFRESH)) {
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mMainUi.applyKitKatTranslucency();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_message, null);
        mMessageSwiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.message_swiperefreshlayout);
        mMessageRecycle = (RecyclerView) view.findViewById(R.id.message_recycleview);
        myMessageListener = new MyMessageListener();

        EMClient.getInstance().chatManager().addMessageListener(myMessageListener);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        emConversations = loadConversationList();
        requestDate();

        mMessageSwiperefresh.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        mMessageSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestDate();
                refresh();
            }
        });

        linearLayoutManager = new LinearLayoutManager(getActivity());
        mMessageRecycle.setLayoutManager(linearLayoutManager);
        mMessageRecycle.setItemAnimator(new DefaultItemAnimator());
        mMessageRecycle.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
        messageRecycleAdapter = new MessageRecycleAdapter(mMainUi, emConversations);
        mMessageRecycle.setAdapter(messageRecycleAdapter);

        messageRecycleAdapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                userName = emConversations.get(postion - 1).getUserName();
                UserDao userDao = new UserDao(UIUtils.getContext());
                EaseUser user = userDao.getContactList().get(userName);
                String avatar = user.getAvatar();
                String nick = user.getNick();

                Intent intent = new Intent(mMainUi, ChatActivity.class);

                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, userName);//对方的环信id
                intent.putExtra("Name", nick);
                intent.putExtra("Avatar", avatar);
                mMainUi.startActivity(intent);

            }
        });

        //滑动的监听
        mMessageRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
//                    if (lastVisiblePosition >= linearLayoutManager.getItemCount() - 1) {
//                        messageRecycleAdapter.showFootView();
//                        refresh();
//                    }
//
//                }
            }
        });

        messageRecycleAdapter.setOnHeadClickListener(new MyHeadViewClickListener() {
            @Override
            public void onHeadClick(View view) {
                switch (view.getId()) {
                    case R.id.message_notification://通知消息
                        Intent notifyIntent = new Intent(mMainUi, NotifyActivity.class);
                        startActivity(notifyIntent);
                        break;
                    case R.id.message_interaction://互动消息
                        Intent interactionIntent = new Intent(mMainUi, InteractionActivity.class);
                        startActivity(interactionIntent);
                        break;
                    case R.id.message_system://系统消息
                        Intent systemIntent = new Intent(mMainUi, SystemActivity.class);
                        startActivity(systemIntent);
                        break;

                }
            }
        });


    }

    /**
     * 请求数据
     * MESSAGE_UNREAD
     */
    private void requestDate() {
        showMyDialog();
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(getActivity())))
                .build();

        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.MESSAGE_UNREAD)
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
                        if (mMessageSwiperefresh != null) {
                            mMessageSwiperefresh.setRefreshing(false);
                        }
                        animDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = MESSAGE_UNREAD_SUCCESS;
                msg.obj = result;
                handler.sendMessage(msg);
                LogUtil.LogShitou("MESSAGE_UNREAD", result);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mMainUi.applyKitKatTranslucency();
        }
    }


    /**
     * load conversation list
     *
     * @return
     */
    protected List<EMConversation> loadConversationList() {
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * 排序
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    class MyMessageListener implements EMMessageListener {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {

            for (EMMessage message : messages) {
                String from = message.getFrom();
/*******************************************************************************************************/
                EMMessage.Type type = message.getType();
                if(EMMessage.Type.VOICE == type){
                    LogUtil.LogShitou("是语音消息,","需要播放出来");
                    EMVoiceMessageBody body =(EMVoiceMessageBody) message.getBody();
//                    body.getLength()


                }
/*******************************************************************************************************/

                String Avatar = "";
                String Name = "";
                if (!from.equals(EMClient.getInstance().getCurrentUser())) {
                    try {
                        Avatar = message.getStringAttribute("Avatar");//对方的。
                        Name = message.getStringAttribute("Name");
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    Model model = new Model(UIUtils.getContext());
                    EaseUser user = new EaseUser(from);
                    user.setNick(Name);
                    user.setAvatar(Avatar);
                    model.saveContact(user);
                }

            }
            refresh();


        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

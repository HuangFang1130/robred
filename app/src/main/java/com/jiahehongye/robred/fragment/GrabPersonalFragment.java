package com.jiahehongye.robred.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.GrabRedActivity;
import com.jiahehongye.robred.activity.RedDetailActivity;
import com.jiahehongye.robred.adapter.GrabPersonalRecycleAdapter;
import com.jiahehongye.robred.bean.GrabPersonalListResult;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.LogUtil;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.GlideCircleTransform;
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

/**
 * Created by huangjunhui on 2016/12/2.13:57
 */
public class GrabPersonalFragment extends Fragment {

    private static final int ENTER_OTHER = 300;
    private static final int GRAB_ACTIVI = 400;
    private static final int GRAB_PERSONAL_LIST = 200;
    private static final int GRAB_RED = 201;
    private static final int IS_GAB = 202;
    private List<GrabPersonalListResult.DataBean.SendRedEnveListBean> grabRedList;
    private PersistentCookieStore persistentCookieStore;
    private Call call;
    private SwipeRefreshLayout mPersonalSwiperefresh;
    private RecyclerView mPersonalRecycleview;
    private LinearLayoutManager linearLayoutManager;
    private List<GrabPersonalListResult.DataBean.SendRedEnveListBean> fatherArrayList;
    private GrabPersonalRecycleAdapter mGrabPersonalRecycleAdapter;
    private String pageNumber = "1";

    private boolean ismore = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GRAB_PERSONAL_LIST:
                    String result = (String) msg.obj;
                    GrabPersonalListResult grabPersonalListResult = new Gson().fromJson(result, GrabPersonalListResult.class);
                    if (grabPersonalListResult.getResult().equals("success")) {
                        grabRedList = grabPersonalListResult.getData().getSendRedEnveList();
                        fatherArrayList.addAll(grabRedList);
                        if(grabRedList.size()>=20){
                            ismore = true;
                        }else {
                            ismore = false;
                        }
                    } else if (grabPersonalListResult.getResult().equals("fail")) {
                        Toast.makeText(getActivity(), "解析失败", Toast.LENGTH_SHORT).show();
                    }
                    if (mPersonalSwiperefresh != null) {
                        mPersonalSwiperefresh.setRefreshing(false);
                    }
                    mGrabPersonalRecycleAdapter.hintFootView();
                    mGrabPersonalRecycleAdapter.notifyDataSetChanged();
                    if(animDialog!=null) {
                        animDialog.dismiss();
                    }
                    LogUtil.LogShitou("获取红包列表：-----测试", result.toString());
                    break;

                case GRAB_ACTIVI://抢的动作执行后跳转到详情页面。

                    try {
                        String re = (String) msg.obj;
                        if (re != null) {
                            JSONObject jsonObject = new JSONObject(re);
                            result = jsonObject.getString("result");
                            if (result.equals("fail")) {//可以抢
                                Toast.makeText(getActivity(), jsonObject.getString("errorMsg"), Toast.LENGTH_SHORT).show();
                                popupWindow.dismiss();
                            } else {//已经抢光了
                                Intent activIntent = new Intent(getActivity(), RedDetailActivity.class);
                                activIntent.putExtra("redEnveCode", redEnveCode);
                                activIntent.putExtra("type", "0");//0，个人红包，1，商家红包
                                activIntent.putExtra("redEnveMark", redEnveMark);
                                startActivity(activIntent);
                                popupWindow.dismiss();
                            }

                        } else {//已经抢光了
//                            popupWindow.dismiss();
//                            pageNumber = "1";
//                            requestDate(pageNumber);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;

                case GRAB_RED://开始抢红包
                    grabRedIsVisibily();//告诉服务器我抢了。(检测这个时候还有没有)
                    break;
                case IS_GAB://判断服务器返回到数据是否可以抢
                    try {
                        String data2 = (String) msg.obj;
                        if (data2 != null) {
                            JSONObject jsonObject = new JSONObject(data2);
                            result = jsonObject.getString("result");
                            if (result.equals("success")) {//可以抢
                                grabRedActive(redEnveCode);
                            } else {//已经抢光了
                                popupWindow.dismiss();
                                pageNumber = "1";
                                Toast.makeText(getActivity() , "您手慢了，被别人抢光了！", Toast.LENGTH_SHORT).show();
                                requestDate(pageNumber);
                            }

                        } else {//已经抢光了
                            popupWindow.dismiss();
                            pageNumber = "1";
                            Toast.makeText(getActivity() , "您手慢了，被别人抢光了！", Toast.LENGTH_SHORT).show();

                            requestDate(pageNumber);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };
    private String memberId;
    private MyProgressDialog animDialog;

    private void grabRedIsVisibily() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("redEnveCode", redEnveCode);
            jsonObject.put("memberId", memberId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.LogShitou("抢红包操作：请求的数据", jsonObject.toString());
        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .url(Constant.BEGINROBURL)
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

                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }


                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtil.LogShitou(result);
                Message msg = handler.obtainMessage();
                msg.what = IS_GAB;
                msg.obj = result;
                handler.sendMessage(msg);
                LogUtil.LogShitou("抢红包操作：", result);

            }
        });

    }

    private View view;
    private PopupWindow popupWindow;
    private String redEnveCode;
    private String redEnveMark;
    private String userId;//发红包人的id


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_grabred_personal, null);
        mPersonalSwiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.grabred_personal_swiperefresh);
        mPersonalRecycleview = (RecyclerView) view.findViewById(R.id.grabred_personal_recycleview);
        memberId = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_ID, "");
        animDialog = new MyProgressDialog(getActivity(), "玩命加载中...", R.drawable.loading);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPersonalSwiperefresh.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        mPersonalRecycleview.setLayoutManager(linearLayoutManager);

        fatherArrayList = new ArrayList<>();

        mGrabPersonalRecycleAdapter = new GrabPersonalRecycleAdapter(getActivity(), fatherArrayList);
        mPersonalRecycleview.setAdapter(mGrabPersonalRecycleAdapter);

        mPersonalSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if(mPersonalSwiperefresh.isRefreshing()){
//                    if(!ismore){
//                        mPersonalSwiperefresh.setRefreshing(false);
//                    }
//                    return;
//                }
                fatherArrayList.clear();
                mGrabPersonalRecycleAdapter.notifyDataSetChanged();
                pageNumber = 1 + "";
                requestDate(pageNumber);

            }
        });
        //滑动的监听
        mPersonalRecycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (linearLayoutManager.getItemCount() <= 1) {
                        return;
                    }
                    if (lastVisiblePosition >= linearLayoutManager.getItemCount() - 1) {
                       if(mGrabPersonalRecycleAdapter!=null){
                           mGrabPersonalRecycleAdapter.showFootView();
                       }
                        int i = Integer.parseInt(pageNumber);
                        i++;
                        pageNumber = i + "";
                        requestDate(pageNumber);

                    }

                }
            }
        });

        mGrabPersonalRecycleAdapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {

                if (GrabRedActivity.userPhoto.equals("")||GrabRedActivity.nickname.equals("")||GrabRedActivity.datingPurpose.equals("")
                        ||GrabRedActivity.job.equals("")||GrabRedActivity.maritalStatus.equals("")||GrabRedActivity.personalDescription.equals("")
                        ||GrabRedActivity.schoolRecord.equals("")||GrabRedActivity.xingzuo.equals("")||GrabRedActivity.photesize==0){
                    Toast.makeText(getActivity(), "完善个人信息才可抢红包哦", Toast.LENGTH_SHORT).show();
                }else if (GrabRedActivity.isContain==true){
                    Toast.makeText(getActivity(), "昵称不能以hb开始哦", Toast.LENGTH_SHORT).show();
                }

                else {
                    redEnveMark = fatherArrayList.get(postion).getRedEnveMark();
                    //红包的编号
                    redEnveCode = fatherArrayList.get(postion).getRedEnveCode();
                    userId = fatherArrayList.get(postion).getMemberId();

                    if (redEnveMark.equals("1")) {
                        showPopWindow(view, postion);
                    } else if (redEnveMark.equals("2")) {//来得及
                        showPopWindow(view, postion);
                    } else if (redEnveMark.equals("3")) {//已领取
                        enterDetailPager(view, postion);
                    } else if (redEnveMark.equals("4")) {//来晚了
                        enterDetailPager(view, postion);
                    }
                }


            }
        });


    }

    /**
     * 跳转至详情页
     *
     * @param view
     * @param postion
     */
    private void enterDetailPager(View view, int postion) {
        Intent intent = new Intent(getActivity(), RedDetailActivity.class);
        intent.putExtra("redEnveCode", redEnveCode);//红包的编号
        intent.putExtra("redEnveMark", redEnveMark);//
        intent.putExtra("type", "0");//0，个人红包，1，商家红包
        startActivity(intent);
    }


    /**
     * 弹出抢红包的popwindow
     *
     * @param view
     * @param postion
     */
    private void showPopWindow(View view, int postion) {

        String nickName = fatherArrayList.get(postion).getNickName();
        String userPhoto = fatherArrayList.get(postion).getUserPhoto();
        String redEnveLeaveword = fatherArrayList.get(postion).getRedEnveLeaveword();
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popView = inflater.inflate(R.layout.grab_redlist_pop, null);
        popupWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        ImageView mIvPopClose = (ImageView) popView.findViewById(R.id.grab_pop_close);
        TextView mTvPopName = (TextView) popView.findViewById(R.id.grab_pop_name);
        ImageView mIvPopAvatar = (ImageView) popView.findViewById(R.id.grab_pop_avatar);
        TextView mTvPopDes = (TextView) popView.findViewById(R.id.grab_pop_des);
        final ImageView mIvPopAnim = (ImageView) popView.findViewById(R.id.grab_pop_anim);

        mTvPopName.setText(nickName);
        Glide.with(UIUtils.getContext()).load(userPhoto).transform(new GlideCircleTransform(UIUtils.getContext())).into(mIvPopAvatar);
        mTvPopDes.setText(redEnveLeaveword);
        mIvPopClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        mIvPopAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(UIUtils.getContext()).load(R.drawable.grab_red).asGif().into(mIvPopAnim);
                handler.sendEmptyMessageDelayed(GRAB_RED, 500);
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(view, Gravity.CENTER, 5, 5);
    }

    /**
     * 抢红包的操作
     *
     * @param sendRedEnve 红包的编号
     */
    private void grabRedActive(String sendRedEnve) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("redEnveCode", sendRedEnve);
            jsonObject.put("grabUser", memberId);
            jsonObject.put("upperShareUser", "null");
            jsonObject.put("shareLevel", 0 + "");
            jsonObject.put("channelSource", 2 + "");
            jsonObject.put("mobileTermModel", android.os.Build.MODEL + "");
            Log.e("手机型号", android.os.Build.MODEL);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .url(Constant.GRAB_ACTIVE)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                UIUtils.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = GRAB_ACTIVI;
                msg.obj = result;
                handler.sendMessage(msg);
                LogUtil.LogShitou("抢的动作执行后：返回得到的钱数：", result);

            }
        });

    }


    /**
     * 获取个人红包列表
     *
     * @param pageNumber 页数
     */
    private void requestDate(String pageNumber) {
        showMyDialog();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pageSize", "20");
            jsonObject.put("pageNumber", pageNumber);
            jsonObject.put("type", "0");//0，个人红包  1：商家红包
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.GRAB_PERSONURL)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                UIUtils.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
                        if(animDialog!=null) {
                            animDialog.dismiss();
                        }
                        if (mPersonalSwiperefresh != null) {
                            mPersonalSwiperefresh.setRefreshing(false);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = GRAB_PERSONAL_LIST;
                msg.obj = result;
                handler.sendMessage(msg);

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        fatherArrayList.clear();
        mGrabPersonalRecycleAdapter.notifyDataSetChanged();
        pageNumber = "1";
        requestDate(pageNumber);
    }

    /**
     * 显示对话框
     */
    public void showMyDialog() {

        animDialog.show();
        animDialog.setCancelable(true);
//        animDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                if (call.isExecuted()) {
//                    call.cancel();
//                }
//            }
//        });
    }
}

package com.jiahehongye.robred.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.FreshNewsDetailActivity;
import com.jiahehongye.robred.adapter.FreshNewsRecycleAdapter;
import com.jiahehongye.robred.bean.NewsBean;
import com.jiahehongye.robred.bean.RecommendBanner;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.LogUtil;
import com.jiahehongye.robred.view.MyProgressDialog;

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
 * Created by huangjunhui on 2016/12/6.11:44
 *
 * 新闻中心唯一指定fragment
 */
public class HotFreshNewsFragment extends Fragment {

    private static final String RESULT = "result";
    private static final String RESULTs = "result";
    private SwipeRefreshLayout mFreshNewsSwiperefresh;
    private RecyclerView mFreshNewsRecycleview;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<NewsBean> fatherArrayList = new ArrayList<>();
    private FreshNewsRecycleAdapter mFreshNewsRecycleAdapter;
    private String NUMBERS=  "10";
    private int PAGENUMBER = 1;
    private boolean isFrist = true;
    private ArrayList<RecommendBanner> banners = new ArrayList<>();

    public static HotFreshNewsFragment newInstances(String i,String j) {
        Bundle args = new Bundle();
        args.putString(RESULT,i);
        args.putString("aaa",j);
        LogUtil.LogShitou("categoryid:-----------------------------------   "+i);
        HotFreshNewsFragment fragment = new HotFreshNewsFragment();
        fragment.setArguments(args);
        return fragment;

    }

    private static final int GET_ALL = 0000;
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

                            //category


                            String information = data.getString("information");
                            ArrayList<NewsBean> arrayList = (ArrayList<NewsBean>) JSON.parseArray(information,NewsBean.class);
                            if (arrayList.size()==0){
                                if(mFreshNewsRecycleAdapter!=null)
                                mFreshNewsRecycleAdapter.hintFootView();
                                return;
                            }else {
                                fatherArrayList.addAll(arrayList);
                            }

                            if (mFreshNewsRecycleAdapter ==null){
                                mFreshNewsRecycleAdapter = new FreshNewsRecycleAdapter(getActivity(), fatherArrayList);
                            }

                            if (getArguments().get("aaa").equals("头条")){
                                if (isFrist){

                                    String syAdvertImg = data.getString("syAdvertImg");
                                    banners = (ArrayList<RecommendBanner>) JSON.parseArray(syAdvertImg,RecommendBanner.class);
                                    mFreshNewsRecycleAdapter.setHeardDate(banners);
                                    mFreshNewsRecycleview.setAdapter(mFreshNewsRecycleAdapter);
                                    isFrist = false;
                                }else {

                                }

                            }

                            mFreshNewsRecycleAdapter.notifyDataSetChanged();
                            mFreshNewsRecycleAdapter.setOnItemClickListener(new MyItemClickListener() {
                                @Override
                                public void onItemClick(View view, int postion) {
                                    Intent intent = new Intent(getActivity(), FreshNewsDetailActivity.class);
                                    intent.putExtra("id",fatherArrayList.get(postion).getId());
                                    intent.putExtra("title",fatherArrayList.get(postion).getTitle());
                                    intent.putExtra("flag",fatherArrayList.get(postion).getFLAG());
                                    startActivity(intent);
                                }
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_fresh_content, null);
        mFreshNewsSwiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.freshnews_swiperefresh);
        mFreshNewsRecycleview = (RecyclerView) view.findViewById(R.id.freshnews_recycleview);
        fatherArrayList.clear();
        PAGENUMBER = 1;
        isFrist = true;
        getdata();
        mFreshNewsRecycleAdapter = new FreshNewsRecycleAdapter(getActivity(), fatherArrayList);
        mFreshNewsRecycleview.setAdapter(mFreshNewsRecycleAdapter);
        return view;

    }
private boolean isLoading = false;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PAGENUMBER = 1;
        mFreshNewsSwiperefresh.setColorSchemeResources(R.color.holo_blue_bright,R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        mFreshNewsSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PAGENUMBER = 1;
                fatherArrayList.clear();
                getdata();
                mFreshNewsSwiperefresh.setRefreshing(false);
            }
        });
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mFreshNewsRecycleview.setLayoutManager(linearLayoutManager);
        mFreshNewsRecycleview.setItemAnimator(new DefaultItemAnimator());



        //滑动的监听
        mFreshNewsRecycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE&&!isLoading){
                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                    if(lastVisiblePosition >= linearLayoutManager.getItemCount()-1){
                        if(mFreshNewsRecycleAdapter!=null)mFreshNewsRecycleAdapter.showFootView();
                        PAGENUMBER = PAGENUMBER+1;
                        getdata();
                    }

                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     *获取分类新闻列表
     */
    private PersistentCookieStore persistentCookieStore;
    private MyProgressDialog animDialog;
    private Call call;

    private void getdata() {

        showMyDialog();
        isLoading=true;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("categoryId",getArguments().get(RESULT).toString());
            jsonObject.put("pageNumber",PAGENUMBER+"");
            jsonObject.put("pageSize",NUMBERS);
            LogUtil.LogShitou("   ====== :  ",getArguments().get(RESULT).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.NEWS_LIST)
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
                isLoading=false;
                animDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtil.LogShitou("   ======:",result);

                isLoading=false;
                animDialog.dismiss();
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
}


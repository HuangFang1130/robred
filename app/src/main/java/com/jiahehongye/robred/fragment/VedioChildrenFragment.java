package com.jiahehongye.robred.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.jiahehongye.robred.BaseFragment;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.adapter.VedioChildFragmentRecycleAdapter;
import com.jiahehongye.robred.bean.DFTTTitleResult;
import com.jiahehongye.robred.bean.DFTTVideoResult;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.jiecao.JCVideoPlayer;
import com.jiahehongye.robred.utils.LogUtil;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.MyProgressDialog;
import com.songheng.newsapisdk.sdk.apiservice.DFTTVideoApiService;
import com.songheng.newsapisdk.sdk.apiservice.listener.DfttApiServiceCallBack;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by huangjunhui on 2017/5/22.9:52
 */
public class VedioChildrenFragment extends BaseFragment {
    private ArrayList<DFTTVideoResult.DataBean> fatherListDate;

    private View view;
    private int pagerNamber = 1;
    private static final int GET_VEDIO_LIST = 300;

    private String isup;
    private String type;

    private String name;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case GET_VEDIO_LIST:
//                    if (animDialog.isShowing()) {
//                        animDialog.dismiss();
//                    }
                    if (mVedioHeadSwiperefresh != null) {
                        if (mVedioHeadSwiperefresh.isRefreshing()) {
                            mVedioHeadSwiperefresh.setRefreshing(false);
                        }
                    }

                    animDialog.dismiss();

                    String result = (String) msg.obj;
                    DFTTVideoResult dfttNewsResult = new Gson().fromJson(result, DFTTVideoResult.class);
                    LogUtil.LogShitou("视频数据", result.toString());
                    List<DFTTVideoResult.DataBean> data = dfttNewsResult.getData();
                    if (data != null) {
                        fatherListDate.addAll(data);

                    }

                    mHomeRecycleAdapter.notifyDataSetChanged();


                    break;
            }
        }
    };
    private RecyclerView mVedioHeadRecycleView;
    private SwipeRefreshLayout mVedioHeadSwiperefresh;
    private boolean isLogin;
    private LinearLayoutManager linearLayoutManager;
    private VedioChildFragmentRecycleAdapter mHomeRecycleAdapter;
    private MyProgressDialog animDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_vedio_children, null);

        Bundle arguments = getArguments();
        isup = arguments.getString("isup");
        type = arguments.getString("type");
        name = arguments.getString("name");

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        showMyDialog();
        mVedioHeadRecycleView = (RecyclerView) view.findViewById(R.id.home_recycleviewddd333);
        mVedioHeadSwiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.home_swiperefreshlayou22t);
        pagerNamber = 1;
        requestListDate(pagerNamber, 1);
        fatherListDate = new ArrayList<>();
        isLogin = (boolean) SPUtils.get(UIUtils.getContext(), Constant.IS_LOGIN, false);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        mVedioHeadRecycleView.setLayoutManager(linearLayoutManager);


        mVedioHeadSwiperefresh.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);


        mHomeRecycleAdapter = new VedioChildFragmentRecycleAdapter(mMainUi, fatherListDate);
        mVedioHeadRecycleView.setAdapter(mHomeRecycleAdapter);

        //滑动的监听
        mVedioHeadRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();

                    if (linearLayoutManager.getItemCount() <= 1) {
                        return;
                    }
                    if (lastVisiblePosition >= linearLayoutManager.getItemCount() - 1) {
                        if (mHomeRecycleAdapter != null) {
                            mHomeRecycleAdapter.showFootView();
                        }

                        pagerNamber++;
                        requestListDate(pagerNamber, 10);

                    }

                }
            }
        });


        //false： 没有登录， true : 登录了
        mHomeRecycleAdapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {

//                Intent intent = new Intent(getActivity(), DFTTVedioDetailActivity.class);
//                intent.putExtra("url", fatherListDate.get(postion).getUrl());
//                intent.putExtra("playurl", fatherListDate.get(postion).getVideo_link());//视频地址
//                intent.putExtra("title", fatherListDate.get(postion).getTopic());
//                intent.putExtra("type",type);
//                startActivity(intent);
            }
        });


        mVedioHeadSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mHomeRecycleAdapter != null) {
                    mHomeRecycleAdapter.hintFootView();
                    fatherListDate.clear();
                    mHomeRecycleAdapter.notifyDataSetChanged();
                    requestListDate(1, -10);
                }

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    public static VedioChildrenFragment newInstances(DFTTTitleResult.DdBean ddBean) {
        VedioChildrenFragment vedioChildrenFragment = new VedioChildrenFragment();

        Bundle args = new Bundle();
        args.putString("isup", ddBean.getIsup());
        args.putString("type", ddBean.getType());
        args.putString("name", ddBean.getName());
        vedioChildrenFragment.setArguments(args);
        return vedioChildrenFragment;
    }


    /**
     * 显示对话框
     */
    public void showMyDialog() {
        animDialog = new MyProgressDialog(getActivity(), "玩命加载中...", R.drawable.loading);
        animDialog.show();
        animDialog.setCancelable(true);

    }


    private void requestListDate(int page, int index) {
        showMyDialog();
        DFTTVideoApiService.getVideoNewsListByType(type, page, index, new DfttApiServiceCallBack() {
            @Override
            public void onSuccess(String s) {
                LogUtil.LogShitou("视频数据：", s.toString());
                Message msg = handler.obtainMessage();
                msg.what = GET_VEDIO_LIST;
                msg.obj = s;
                handler.sendMessage(msg);
            }


            @Override
            public void onError(String s, String s1, Response response, Exception e) {
                animDialog.dismiss();
            }
        });
    }
}

package com.jiahehongye.robred.fragment;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiahehongye.robred.BaseFragment;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.BillionairesActivity;
import com.jiahehongye.robred.activity.ContactActivity;
import com.jiahehongye.robred.activity.DiamondAccountActivity;
import com.jiahehongye.robred.activity.GrabRedActivity;
import com.jiahehongye.robred.activity.OneyuanActivity;
import com.jiahehongye.robred.activity.SendRedActivity;
import com.jiahehongye.robred.adapter.HomeRecycleAdapter;
import com.jiahehongye.robred.bean.HomeFragmentResult;
import com.jiahehongye.robred.bean.HomeYIyuanGouResult;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.interfaces.MyHeadViewClickListener;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.oneyuan.OneyuanProductDetailActivity;
import com.jiahehongye.robred.utils.LogUtil;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;

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
 * Created by Administrator on 2016/11/30.
 * <p/>
 * 首页
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {


    private String pagerNamber = "1";
    private static final int GET_HOME = 0000;
    private static final int GET_HOME_LIST = 200;
    private RecyclerView mRecycleViewHome;
    private HomeRecycleAdapter mHomeRecycleAdapter;
    private GridLayoutManager gridLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    HomeFragmentResult homeFragmentResult = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_HOME:
                    String homefragment = (String) msg.obj;
                    LogUtil.LogShitou("homefragment：   ", homefragment);
                    try {
                        homeFragmentResult = new Gson().fromJson(homefragment, HomeFragmentResult.class);
                    } catch (Exception e) {
                    }
                    mHomeRecycleAdapter.setHeardDate(homeFragmentResult);
                    mRecycleViewHome.setAdapter(mHomeRecycleAdapter);
                    mHomeRecycleAdapter.notifyDataSetChanged();

                    break;

                case GET_HOME_LIST:
                    String result = (String) msg.obj;
                    LogUtil.LogShitou("首页列表1111：", result.toString());
                    HomeYIyuanGouResult homeYIyuanGouResult = null;
                    try {
                        homeYIyuanGouResult = new Gson().fromJson(result, HomeYIyuanGouResult.class);
                    } catch (Exception e) {
                    }

                    if (homeYIyuanGouResult.getResult().equals("success")) {
                        resProductList = homeYIyuanGouResult.getData().getResProductList();
                        fatherListDate.addAll(resProductList);
                    } else if (homeYIyuanGouResult.getResult().equals("fail")) {
                        Toast.makeText(getActivity(), "解析失败", Toast.LENGTH_SHORT).show();
                    }
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    mHomeRecycleAdapter.hintFootView();
                    mHomeRecycleAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private List<HomeYIyuanGouResult.DataBean.ResProductListBean> resProductList;
    private List<HomeYIyuanGouResult.DataBean.ResProductListBean> fatherListDate;
    private boolean isLogin;
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainUi.applyKitKatTranslucency();
        mMainUi.mTintManager.setStatusBarTintResource(R.color.home_state_color);
        View view = LayoutInflater.from(mMainUi).inflate(R.layout.fragment_home, null);
        TextView hTvRich = (TextView) view.findViewById(R.id.home_head_rich);
        mRecycleViewHome = (RecyclerView) view.findViewById(R.id.home_recycleview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.home_swiperefreshlayout);
        hTvRich.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        requestHeadDate();
        pagerNamber = "1";
        requestListDate(pagerNamber);
        fatherListDate = new ArrayList<>();
        isLogin = (boolean) SPUtils.get(UIUtils.getContext(), Constant.IS_LOGIN, false);

        gridLayoutManager = new GridLayoutManager(mMainUi, 2);
        gridLayoutManager.setSpanSizeLookup(new HeaderSpanSizeLookup(gridLayoutManager));//设置脚布局占用多少个条目
        mRecycleViewHome.setLayoutManager(gridLayoutManager);
        mRecycleViewHome.setItemAnimator(new DefaultItemAnimator());


        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);


        mHomeRecycleAdapter = new HomeRecycleAdapter(mMainUi, fatherListDate);
        mRecycleViewHome.setAdapter(mHomeRecycleAdapter);

        //滑动的监听
        mRecycleViewHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisiblePosition = gridLayoutManager.findLastVisibleItemPosition();

                    if (gridLayoutManager.getItemCount() <= 1) {
                        return;
                    }
                    if (lastVisiblePosition >= gridLayoutManager.getItemCount() - 1) {
                        if (mHomeRecycleAdapter != null) {
                            mHomeRecycleAdapter.showFootView();
                        }

                        int i = Integer.parseInt(pagerNamber);
                        i++;
                        pagerNamber = i + "";
                        requestListDate(pagerNamber);

                    }

                }
            }
        });


        //false： 没有登录， true : 登录了
        mHomeRecycleAdapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                if (isLogin) {
                    Intent intent = new Intent(getActivity(), OneyuanProductDetailActivity.class);
                    intent.putExtra("productId", fatherListDate.get(postion - 1).getProductId() + "");
                    startActivity(intent);
                } else {
                    mMainUi.startLogin();
                }

            }
        });

        mHomeRecycleAdapter.setOnHeadClickListener(new MyHeadViewClickListener() {
            @Override
            public void onHeadClick(View view) {
                switch (view.getId()) {
                    case R.id.home_rl_personal_information://个人中心
                        if (isLogin) {
                            startActivity(new Intent(mMainUi, DiamondAccountActivity.class));
                        } else {
                            mMainUi.startLogin();
                        }
                        break;
                    case R.id.home_rb_grab://抢红包
                        if (isLogin) {
                            Intent intent = new Intent(mMainUi, GrabRedActivity.class);
                            startActivity(intent);
                        } else {
                            mMainUi.startLogin();
                        }
                        break;
                    case R.id.home_rb_send://发红包
                        if (isLogin) {
                            Intent sendIntent = new Intent(mMainUi, SendRedActivity.class);
                            startActivity(sendIntent);
                        } else {
                            mMainUi.startLogin();
                        }

                        break;
                    case R.id.home_rb_find_favorable://找优惠
                        if (isLogin) {
                            Intent intent = new Intent(mMainUi, ContactActivity.class);
                            startActivity(intent);
                        } else {
                            mMainUi.startLogin();
                        }

                        break;
                    case R.id.home_rb_spell_luck://拼手气
                        if (isLogin) {
                            Intent luckyIntent = new Intent(mMainUi, OneyuanActivity.class);
                            startActivity(luckyIntent);
                        } else {
                            mMainUi.startLogin();
                        }

                        break;

                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mHomeRecycleAdapter != null) {
                    fatherListDate.clear();
                    mHomeRecycleAdapter.notifyDataSetChanged();
                    requestListDate("1");
                }

            }
        });

    }


    /**
     * 请求列表
     * 传参数：pageSize分页记录数、pageNumber页码
     */
    private void requestListDate(String pagerNamber) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pageSize", "10");
            jsonObject.put("pageNumber", pagerNamber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.HOME_YIYUANGOU_LIST)
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
                        if (mSwipeRefreshLayout != null) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = GET_HOME_LIST;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 获取首页信息
     */
    private PersistentCookieStore persistentCookieStore;


    /**
     * 请求头部数据
     */
    private void requestHeadDate() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.HOME_PAGE)
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
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                Message msg = handler.obtainMessage();
                msg.what = GET_HOME;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_head_rich:
                if (isLogin) {
                    Intent intent = new Intent(mMainUi, BillionairesActivity.class);//富豪榜
                    startActivity(intent);
                } else {
                    mMainUi.startLogin();
                }


                break;
        }
    }

    public void refresh() {
        Toast.makeText(getActivity(), "shoudaoguangbole", Toast.LENGTH_SHORT).show();
        mHomeRecycleAdapter.setHeardDate(homeFragmentResult);
        mRecycleViewHome.setAdapter(mHomeRecycleAdapter);
        mHomeRecycleAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
//
//        if(resultCode==200){
//            isLogin = (boolean) SPUtils.get(UIUtils.getContext(), Constant.IS_LOGIN, false);
//        }
//
//    }


    class HeaderSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        private final GridLayoutManager layoutManager;

        public HeaderSpanSizeLookup(GridLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        //在这里设置头布局和脚布局占用的条数
        @Override
        public int getSpanSize(int position) {
            int type = position;
            if (position == 0) {
                return 2;
            }
            if (fatherListDate != null) {
                if (fatherListDate.size() > 0 && fatherListDate.size() % 2 == 0 && position == fatherListDate.size() + 1) {
                    return 2;
                }
            }

            type = position == 0 ? layoutManager.getSpanCount() : 1;
            if (mHomeRecycleAdapter.getItemViewType(position) == mHomeRecycleAdapter.ITEM_TYPE_FOOT) {
                return 2;
            }
            return type;


        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
//            requestHeadDate();
            isLogin = (boolean) SPUtils.get(UIUtils.getContext(), Constant.IS_LOGIN, false);
            mMainUi.applyKitKatTranslucency();
            mMainUi.mTintManager.setStatusBarTintResource(R.color.home_state_color);
            if (mHomeRecycleAdapter.hRollingView != null) {
                if (!mHomeRecycleAdapter.hRollingView.isScrolling()) {
                    mHomeRecycleAdapter.hRollingView.resume();
                }
            }
        } else {
            if (mHomeRecycleAdapter.hRollingView != null) {
                mHomeRecycleAdapter.hRollingView.pause();
            }
        }


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isLogin) {
            isLogin = (boolean) SPUtils.get(UIUtils.getContext(), Constant.IS_LOGIN, false);
            requestHeadDate();
        }

    }
}

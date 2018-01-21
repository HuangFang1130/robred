package com.jiahehongye.robred.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jiahehongye.robred.BaseFragment;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.adapter.FreshNewsPagerAdapter;
import com.jiahehongye.robred.bean.NewsTypeBean;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
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
 * Created by Administrator on 2016/11/30.
 */
public class HotFragment extends BaseFragment {

    private TabLayout mFreshTabLayout;
    private ViewPager mFreshViewpager;
    private ArrayList<HotFreshNewsFragment> mViewList;
    private ArrayList<String> mTitleList;
    private FreshNewsPagerAdapter freshNewsPagerAdapter;
    private ArrayList<NewsTypeBean> typeBeen = new ArrayList<>();
    private static final int GET_ALL = 0000;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_ALL:
                    animDialog.dismiss();
                    String s = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(s);
                        if (object.getString("result").equals("success")) {
                            String data = object.getString("data");
                            typeBeen = (ArrayList<NewsTypeBean>) JSON.parseArray(data,NewsTypeBean.class);
                            addDataToFragent(typeBeen);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };

    private void addDataToFragent(ArrayList<NewsTypeBean> data) {
        //添加页卡视图
        mViewList = new ArrayList<>();
        for(int i = 0;i<typeBeen.size();i++){
            HotFreshNewsFragment hotFreshNewsFragment = HotFreshNewsFragment.newInstances(typeBeen.get(i).getId(),typeBeen.get(i).getCategory());
            mViewList.add(hotFreshNewsFragment);
        }

        freshNewsPagerAdapter = new FreshNewsPagerAdapter(mMainUi, mViewList, data);
        mFreshViewpager.setAdapter(freshNewsPagerAdapter);
        //为TabLayout设置ViewPager
        mFreshTabLayout.setupWithViewPager(mFreshViewpager);
        mFreshTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //使用ViewPager的适配器
        mFreshTabLayout.setTabsFromPagerAdapter(freshNewsPagerAdapter);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainUi.applyKitKatTranslucency();
        View view = inflater.from(getActivity()).inflate(R.layout.activity_fresh_news, null);
        mFreshTabLayout = (TabLayout) view.findViewById(R.id.freshnews_tablayout);
        mFreshViewpager = (ViewPager) view.findViewById(R.id.freshnews_viewpager);
        getdata();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }




    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            mMainUi.applyKitKatTranslucency();
//            mMainUi.mTintManager.setStatusBarTintResource(R.color.home_white_color);
        }
    }

    /**
     *获取分类
     */
    private PersistentCookieStore persistentCookieStore;
    private MyProgressDialog animDialog;
    private Call call;

    private void getdata() {

        showMyDialog();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.NEWS_TYPE)
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
                        animDialog.dismiss();
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
}

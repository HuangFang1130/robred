package com.jiahehongye.robred.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiahehongye.robred.BaseFragment;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.MainActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.BillionairesActivity;
import com.jiahehongye.robred.adapter.HomeTabPagerAdapter;
import com.jiahehongye.robred.bean.DFTTTitleResult;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.MyProgressDialog;
import com.songheng.newsapisdk.sdk.apiservice.DFTTNewsApiService;
import com.songheng.newsapisdk.sdk.apiservice.listener.DfttApiServiceCallBack;

import java.util.ArrayList;

import okhttp3.Response;

/**
 * Created by huangjunhui on 2017/5/18.10:00
 */
public class HomeSingleFragment extends BaseFragment implements View.OnClickListener {


    private TabLayout mHomeSingleTablayout;
    private ViewPager mHomeSingleViewpager;
    private HomeTabPagerAdapter homeTabPagerAdapter;
    private MainActivity activity;
    private View view;
    private ArrayList<DFTTTitleResult.DdBean> dateTabList;
    private ArrayList<BaseFragment> mViewList;
    private MyProgressDialog animDialog;
    private String hobby = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_homesingle, null);
        TextView hTvRich = (TextView) view.findViewById(R.id.home_head_rich);
        hTvRich.setOnClickListener(this);
        return view;

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
//                if (call.isExecuted()) {
//                    call.cancel();
//                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHomeSingleTablayout = (TabLayout) view.findViewById(R.id.fragment_home_tablayout);
        activity = (MainActivity) getActivity();
        mHomeSingleViewpager = (ViewPager) view.findViewById(R.id.fragment_home_viewpager);
//        showMyDialog();


        /**
         * 拿到 tablayout  de 栏目
         */
        DFTTNewsApiService.getNewsColumnTag(new DfttApiServiceCallBack() {
            @Override
            public void onSuccess(String s) {
                StringBuilder sb = new StringBuilder();
                StringBuilder append = sb.append("{\"dd\":").append(s).append("}");

                initDate(append);
//                LogUtil.LogShitou("heheda:",s.toString());
//                textview.setText(s.toString());
            }

            @Override
            public void onError(String s, String s1, Response response, Exception e) {

            }
        });

    }

    private void initDate(StringBuilder append) {

        DFTTTitleResult dfttTitleResult = new Gson().fromJson(append.toString(), DFTTTitleResult.class);
        ArrayList<DFTTTitleResult.DdBean> dd = (ArrayList) dfttTitleResult.getDd();
        dateTabList = new ArrayList<>();
        dateTabList.clear();

        hobby = (String) SPUtils.get(UIUtils.getContext(), Constant.USER_HOBBY, "");

        if (hobby.equals("")) {
            for (int i = 0; i < dd.size(); i++) {
                if (dd.get(i).getIsup().equals("1")) {
                    dateTabList.add(dd.get(i));
                }
            }
        } else {
            String[] chrstr = hobby.split(",");
            for (int j = 0; j < dd.size(); j++) {
                DFTTTitleResult.DdBean ddBean = dd.get(j);
                String ddName = ddBean.getName();
                if (ddName.equals("头条"))  dateTabList.add(ddBean);
                for (int i = 0; i < chrstr.length; i++) {
                    if (chrstr[i].equals(ddName)) {
                        dateTabList.add(ddBean);
                    }
                }
            }
        }


        //添加页卡视图
        mViewList = new ArrayList<>();
        for (int i = 0; i < dateTabList.size(); i++) {
            if (i == 0) {
                HomeHeadFragment homeHeadFragment = HomeHeadFragment.newInstances(dateTabList.get(i));
                mViewList.add(homeHeadFragment);
            } else {
                HomeChildFragment homeTabChildFragment = HomeChildFragment.newInstances(dateTabList.get(i));
                mViewList.add(homeTabChildFragment);
            }
        }
        homeTabPagerAdapter = new HomeTabPagerAdapter(getActivity(), mViewList, dateTabList);

        mHomeSingleViewpager.setAdapter(homeTabPagerAdapter);
        mHomeSingleTablayout.setupWithViewPager(mHomeSingleViewpager);
        mHomeSingleTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mHomeSingleTablayout.setTabsFromPagerAdapter(homeTabPagerAdapter);
        mHomeSingleViewpager.setCurrentItem(0);

//        animDialog.dismiss();
//

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_head_rich:
                Intent intent = new Intent(mMainUi, BillionairesActivity.class);//富豪榜
                startActivity(intent);
                break;
        }
    }
}

package com.jiahehongye.robred.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.jiahehongye.robred.BaseFragment;
import com.jiahehongye.robred.MainActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.DFTTTitleResult;
import com.jiahehongye.robred.jiecao.JCVideoPlayer;
import com.jiahehongye.robred.view.MyProgressDialog;
import com.songheng.newsapisdk.sdk.apiservice.DFTTVideoApiService;
import com.songheng.newsapisdk.sdk.apiservice.listener.DfttApiServiceCallBack;

import java.util.ArrayList;

import okhttp3.Response;

/**
 * Created by huangjunhui on 2017/5/22.9:33
 */
public class VedioFragment extends BaseFragment {


    private View view;
    private MainActivity activity;
    private TabLayout mVedioSingleTablayout;
    private ViewPager mVedioSingleViewpager;
    private MyProgressDialog animDialog;
    private ArrayList<DFTTTitleResult.DdBean> dateTabList;
    private ArrayList<BaseFragment> mViewList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_vedio, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mVedioSingleTablayout = (TabLayout) view.findViewById(R.id.fragment_home_tablayout1);
        activity = (MainActivity) getActivity();
        mVedioSingleViewpager = (ViewPager) view.findViewById(R.id.fragment_home_viewpager1);
//        showMyDialog();

        DFTTVideoApiService.getVideoColumnTag(new DfttApiServiceCallBack() {
            @Override
            public void onSuccess(String s) {
                StringBuilder sb = new StringBuilder();
                StringBuilder append = sb.append("{\"dd\":").append(s).append("}");
//
                initDate(append);
//                LogUtil.LogShitou("vedio:",s.toString());
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
        for (int i = 0; i < dd.size(); i++) {
            if (dd.get(i).getIsup().equals("1")) {
                dateTabList.add(dd.get(i));
            }
        }


        //添加页卡视图
        mViewList = new ArrayList<>();
        for (int i = 0; i < dateTabList.size(); i++) {

            VedioChildrenFragment vedioChildrenFragment = VedioChildrenFragment.newInstances(dateTabList.get(i));
            mViewList.add(vedioChildrenFragment);
        }
        HomeTabPagerAdapter2 homeTabPagerAdapter = new HomeTabPagerAdapter2(activity, mViewList, dateTabList);

        mVedioSingleViewpager.setAdapter(homeTabPagerAdapter);
        mVedioSingleTablayout.setupWithViewPager(mVedioSingleViewpager);
        mVedioSingleTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mVedioSingleTablayout.setTabsFromPagerAdapter(homeTabPagerAdapter);
        mVedioSingleViewpager.setCurrentItem(0);

//        animDialog.dismiss();


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(hidden){
            JCVideoPlayer.releaseAllVideos();
        }
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


            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    class HomeTabPagerAdapter2 extends FragmentPagerAdapter

    {

        private ArrayList<DFTTTitleResult.DdBean> mDate;
        private ArrayList<BaseFragment> mViewList;


        public HomeTabPagerAdapter2(MainActivity activity, ArrayList<BaseFragment> mViewList, ArrayList<DFTTTitleResult.DdBean> mTitleList) {
            super(activity.getSupportFragmentManager());
            this.mViewList = mViewList;
            this.mDate = mTitleList;
        }

        @Override
        public Fragment getItem(int position) {
            return mViewList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mDate.get(position).getName();
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

    }

}

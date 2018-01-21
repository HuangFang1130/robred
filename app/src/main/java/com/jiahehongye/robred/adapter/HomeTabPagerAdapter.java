package com.jiahehongye.robred.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

import com.jiahehongye.robred.BaseFragment;
import com.jiahehongye.robred.bean.DFTTTitleResult;

import java.util.ArrayList;

/**
 * Created by huangjunhui on 2017/5/18.10:38
 */
public class HomeTabPagerAdapter extends FragmentPagerAdapter

{

    private ArrayList<DFTTTitleResult.DdBean> mDate;
    private ArrayList<BaseFragment> mViewList;


    public HomeTabPagerAdapter(FragmentActivity activity, ArrayList<BaseFragment> mViewList, ArrayList<DFTTTitleResult.DdBean> mTitleList) {
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
        return  mDate.get(position).getName();
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }



}

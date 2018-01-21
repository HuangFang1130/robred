package com.jiahehongye.robred.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.jiahehongye.robred.MainActivity;
import com.jiahehongye.robred.bean.NewsTypeBean;
import com.jiahehongye.robred.fragment.HotFreshNewsFragment;

import java.util.ArrayList;

/**
 * Created by huangjunhui on 2016/12/6.11:48
 */
public class FreshNewsPagerAdapter extends FragmentPagerAdapter {

    private  ArrayList<HotFreshNewsFragment> mViewList;
    private  ArrayList<NewsTypeBean> mTitleList;
    private MainActivity mActivity;

    public FreshNewsPagerAdapter(MainActivity activity, ArrayList<HotFreshNewsFragment> mViewList, ArrayList<NewsTypeBean> mTitleList) {
        super(activity.getSupportFragmentManager());
        this.mActivity = activity;
        this.mViewList = mViewList;
        this.mTitleList = mTitleList;

    }

    @Override
    public Fragment getItem(int position) {
        return mViewList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return  mTitleList.get(position).getCategory();
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

}

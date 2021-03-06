package com.jiahehongye.robred.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by huangjunhui on 2017/4/13.10:27
 */
public class ApplicationFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    private FragmentManager fm;

    public ApplicationFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int arg0) {
        return (Fragment) fragments.get(arg0);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    /**
     * 防止数据重复加载
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}

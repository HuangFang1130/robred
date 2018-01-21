package com.jiahehongye.robred.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jiahehongye.robred.fragment.SplashFragment;

import java.util.List;

/**
 * Created by huangjunhui on 2017/1/12.11:00
 */
public class  SplashPagerAdapter extends FragmentPagerAdapter {

    private List<SplashFragment> fragList;

    public SplashPagerAdapter(FragmentManager fm,List<SplashFragment> fragList) {
        super(fm);
        this.fragList = fragList;
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return fragList.get(arg0);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return fragList.size();
    }
}

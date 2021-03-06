package com.jiahehongye.robred.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.jiahehongye.robred.view.ChatPopPager;

import java.util.ArrayList;

/**
 * Created by huangjunhui on 2016/12/21.14:23
 */
public class ChatGiftPagerAdapter extends PagerAdapter {

    private ArrayList<ChatPopPager> arrayLists;

    public ChatGiftPagerAdapter(FragmentActivity activity, ArrayList<ChatPopPager> arrayLists) {
        this.arrayLists = arrayLists;
    }

    @Override
    public int getCount() {
        return arrayLists.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ChatPopPager chatPopPager = arrayLists.get(position);
        View view = chatPopPager.initView();
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

}

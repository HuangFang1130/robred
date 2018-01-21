package com.jiahehongye.robred.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.adapter.ApplicationFragmentAdapter;
import com.jiahehongye.robred.fragment.RelationAttentionFragment;
import com.jiahehongye.robred.fragment.RelationFansFragment;
import com.jiahehongye.robred.view.MyProgressDialog;

import java.util.ArrayList;

/**
 * Created by huangjunhui on 2017/5/25.15:59
 */
public class RelationActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout.LayoutParams params;
    private View label;
    private LinearLayout mLLNoGet;
    private LinearLayout mLLAlreadGet;
    private TextView mTvAlreadGet;
    private TextView mTvNoGet;
    private ArrayList<Fragment> allFragment = new ArrayList<>();

    private int mScreenWidth;
    private int text_width;

    private ViewPager mRelationViewPager;
    private int type;
    private MyProgressDialog animDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relation);
        type = getIntent().getIntExtra("type", 0);
        ImageView mRootBack = (ImageView) findViewById(R.id.application_iv_back);
        TextView mRootTitle = (TextView) findViewById(R.id.application_tv_title);
        mRootTitle.setText("我的关系");
        mRootBack.setOnClickListener(this);

//        mRelationTablayout = (TabLayout) findViewById(R.id.activity_relation_tablayout);
//        mRelationTablayout.setTabMode(TabLayout.MODE_FIXED);
        initTab();


    }

    private void initTab() {

        mLLNoGet = (LinearLayout) findViewById(R.id.my_reply);
        mLLAlreadGet = (LinearLayout) findViewById(R.id.my_comment);
        mTvAlreadGet = (TextView) findViewById(R.id.my_reply_text);
        mTvNoGet = (TextView) findViewById(R.id.my_comment_text);
        mRelationViewPager = (ViewPager) findViewById(R.id.activity_relation_viewpager);
        label = findViewById(R.id.comment_label);
        //获取屏幕宽度
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        setLabelWidth();
        addFragment();
        ApplicationFragmentAdapter adapter = new ApplicationFragmentAdapter(getSupportFragmentManager(), allFragment);


        mRelationViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                params.setMargins((int) ((mScreenWidth / 2 - mLabelWidth) / 2
                        + mScreenWidth / 2 * (position + positionOffset)), 0, 0, 0);
                label.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mTvNoGet.setTextColor(getResources().getColor(R.color.theme_color));
                        mTvAlreadGet.setTextColor(getResources().getColor(R.color.black));
                        break;

                    case 1:
                        mTvNoGet.setTextColor(getResources().getColor(R.color.black));
                        mTvAlreadGet.setTextColor(getResources().getColor(R.color.theme_color));
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        params.setMargins((mScreenWidth / 2 - mLabelWidth) / 2, 0, 0, 0);
        label.setLayoutParams(params);
        mRelationViewPager.setAdapter(adapter);
        mRelationViewPager.setCurrentItem(type);
        mLLAlreadGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRelationViewPager.setCurrentItem(0);
                mTvNoGet.setTextColor(getResources().getColor(R.color.theme_color));
                mTvAlreadGet.setTextColor(getResources().getColor(R.color.black));
            }
        });
        mLLNoGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRelationViewPager.setCurrentItem(1);
                mTvNoGet.setTextColor(getResources().getColor(R.color.black));
                mTvAlreadGet.setTextColor(getResources().getColor(R.color.theme_color));
            }
        });

    }

    private int mLabelWidth;

    private void setLabelWidth() {
        //获取文字宽度
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mTvNoGet.measure(w, h);
        text_width = mTvNoGet.getMeasuredWidth();

        // Log.i("width", text_width + "");
        //获取红色标记的属性
        params = (LinearLayout.LayoutParams) label.getLayoutParams();
        //蓝色标记的宽度为 文字宽度+图片宽度+5dp
        mLabelWidth = params.width = text_width;
    }


    private void addFragment() {
        RelationAttentionFragment relationAttentionFragment = RelationAttentionFragment.newInstances();
        RelationFansFragment relationFansFragment = RelationFansFragment.newInstances();
        allFragment.add(relationAttentionFragment);
        allFragment.add(relationFansFragment);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.application_iv_back:
                finish();
                break;
        }
    }

    public void showMyDialog() {
        animDialog = new MyProgressDialog(this, "玩命加载中...", R.drawable.loading);
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

    public void dismissMyDialog() {
        if (animDialog != null)
            animDialog.dismiss();

    }

//    @Override
//    protected void addListener() {

//        dateTabList = new ArrayList<>();
//        dateTabList.clear();
//        dateTabList.add("我的关注");
//        dateTabList.add("我的粉丝");


//        ArrayList<BaseFragment> mViewList = new ArrayList<>();
//        for (int i = 0; i < dateTabList.size(); i++) {
//            if(i==0){
//                RelationAttentionFragment relationAttentionFragment = RelationAttentionFragment.newInstances();
//                mViewList.add(relationAttentionFragment);
//            }else if(i==1){
//                RelationFansFragment relationFansFragment = RelationFansFragment.newInstances();
//                mViewList.add(relationFansFragment);
//            }
//
//        }
//        RelationPagerAdapter relationPagerAdapter = new RelationPagerAdapter(this, mViewList, dateTabList);
//        mRelationViewPager.setAdapter(relationPagerAdapter);
//        mRelationTablayout.setupWithViewPager(mRelationViewPager);
//        mRelationTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        mRelationTablayout.setTabsFromPagerAdapter(relationPagerAdapter);
//        mRelationViewPager.setCurrentItem(type);

//    }
}

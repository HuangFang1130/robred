package com.jiahehongye.robred.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.fragment.FlowerChangeFragment;
import com.jiahehongye.robred.fragment.GoldChangeFragment;
import com.jiahehongye.robred.utils.DensityUtil;

import java.util.ArrayList;

public class MyGiftActivity extends BaseActivity {

    private RelativeLayout mygift_back;
    private View gift_label;
    private int mScreenWidth;
    private TextView gold_change_text, flower_change_text;
    private int text_width;
    private LinearLayout.LayoutParams params;
    private int mLabelWidth;
    private ArrayList<Fragment> allFragment = new ArrayList<>();
    private View label;
    private ViewPager gift_viewpager;
    private LinearLayout gold_change, flower_change;
    private RelativeLayout go_goldrank;
    private TextView look_lingquJilu;
    private int type =1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_my_gift);
        gift_label = findViewById(R.id.gift_label);
        look_lingquJilu = (TextView) findViewById(R.id.look_lingquJilu);
        go_goldrank = (RelativeLayout) findViewById(R.id.go_goldrank);
        mygift_back = (RelativeLayout) findViewById(R.id.mygift_back);
        gold_change_text = (TextView) findViewById(R.id.gold_change_text);
        label = findViewById(R.id.gift_label);
        gift_viewpager = (ViewPager) findViewById(R.id.gift_viewpager);
        flower_change_text = (TextView) findViewById(R.id.flower_change_text);
        gold_change = (LinearLayout) findViewById(R.id.gold_change);
        flower_change = (LinearLayout) findViewById(R.id.flower_change);

        //获取屏幕宽度
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;

        setLabelWidth();
        addFragment();
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), allFragment);
        gift_viewpager.setCurrentItem(0);
        gift_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                        gold_change_text.setTextColor(getResources().getColor(R.color.white));
                        flower_change_text.setTextColor(getResources().getColor(R.color.view_background));
                        type = 1;
                        break;

                    case 1:
                        gold_change_text.setTextColor(getResources().getColor(R.color.view_background));
                        flower_change_text.setTextColor(getResources().getColor(R.color.white));
                        type = 2;
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        params.setMargins((mScreenWidth / 2 - mLabelWidth) / 2, 0, 0, 0);
        label.setLayoutParams(params);
        gift_viewpager.setAdapter(adapter);

        gold_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                gift_viewpager.setCurrentItem(0);
                gold_change_text.setTextColor(getResources().getColor(R.color.white));
                flower_change_text.setTextColor(getResources().getColor(R.color.view_background));
            }
        });

        flower_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                gift_viewpager.setCurrentItem(1);
                gold_change_text.setTextColor(getResources().getColor(R.color.view_background));
                flower_change_text.setTextColor(getResources().getColor(R.color.white));
            }
        });
        mygift_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        go_goldrank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GoldRankActivity.class));
            }
        });

        look_lingquJilu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type ==1){
                    Intent intent = new Intent(getApplicationContext(),LingQuJiLuActivity.class);
                    intent.putExtra("type","1");
                    startActivity(intent);
                }else if (type ==2){
                    Intent intent = new Intent(getApplicationContext(),LingQuJiLuActivity.class);
                    intent.putExtra("type","2");
                    startActivity(intent);
                }
            }
        });
    }

    private void setLabelWidth() {
        //获取文字宽度
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        gold_change_text.measure(w, h);
        text_width = gold_change_text.getMeasuredWidth();

        // Log.i("width", text_width + "");
        //获取红色标记的属性
        params = (LinearLayout.LayoutParams) label.getLayoutParams();
        //蓝色标记的宽度为 文字宽度+图片宽度+5dp
        mLabelWidth = params.width = text_width + DensityUtil.dip2px(this, 5);
    }

    private void addFragment() {
        GoldChangeFragment a = new GoldChangeFragment();
        FlowerChangeFragment b = new FlowerChangeFragment();
        allFragment.add(a);
        allFragment.add(b);
    }

    /**
     * 可滑动Fragment
     */
    class FragmentAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private FragmentManager fm;

        public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
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
            return fragments.get(arg0);
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
}

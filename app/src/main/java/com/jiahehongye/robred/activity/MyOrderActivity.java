package com.jiahehongye.robred.activity;

import android.annotation.TargetApi;
import android.os.Build;
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
import com.jiahehongye.robred.fragment.OrderAllFragment;
import com.jiahehongye.robred.fragment.OrderAlreadyTakeFragment;
import com.jiahehongye.robred.fragment.OrderWaitPayFragment;
import com.jiahehongye.robred.fragment.OrderWaitTakeFragment;
import com.jiahehongye.robred.utils.DensityUtil;

import java.util.ArrayList;

public class MyOrderActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager order_viewpager;
    private int mScreenWidth;
    private View label;
    private int text_width;
    private TextView order_wait_pay_text,order_all_text,order_wait_send_text,order_wait_refund_text;
    private LinearLayout.LayoutParams params;
    private int mLabelWidth;
    private String OrderTaG;
    private ArrayList<Fragment> allFragment =new ArrayList<>();
    private LinearLayout order_all,order_wait_pay,order_wait_send,order_wait_refund;
    private RelativeLayout myorder_back;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_my_order);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            OrderTaG = bundle.getString("order_tag");
        }
        init();
        //获取屏幕宽度
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;

        setLabelWidth();
        addFragment();

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), allFragment);
        order_viewpager.setCurrentItem(0);
        order_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                params.setMargins((int) ((mScreenWidth / 4 - mLabelWidth) / 2
                        + mScreenWidth / 4 * (position + positionOffset)), 0, 0, 0);
                label.setLayoutParams(params);
            }
 
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        order_all_text.setTextAppearance(act,R.style.text_bold);
                        order_wait_pay_text.setTextAppearance(act,R.style.text_normal);
                        order_wait_send_text.setTextAppearance(act,R.style.text_normal);
                        order_wait_refund_text.setTextAppearance(act,R.style.text_normal);
                        break;
                    case 1:
                        order_all_text.setTextAppearance(act,R.style.text_normal);
                        order_wait_pay_text.setTextAppearance(act,R.style.text_bold);
                        order_wait_send_text.setTextAppearance(act,R.style.text_normal);
                        order_wait_refund_text.setTextAppearance(act,R.style.text_normal);
                        break;
                    case 2:
                        order_all_text.setTextAppearance(act,R.style.text_normal);
                        order_wait_pay_text.setTextAppearance(act,R.style.text_normal);
                        order_wait_send_text.setTextAppearance(act,R.style.text_bold);
                        order_wait_refund_text.setTextAppearance(act,R.style.text_normal);
                        break;
                    case 3:
                        order_all_text.setTextAppearance(act,R.style.text_normal);
                        order_wait_pay_text.setTextAppearance(act,R.style.text_normal);
                        order_wait_send_text.setTextAppearance(act,R.style.text_normal);
                        order_wait_refund_text.setTextAppearance(act,R.style.text_bold);
                        break;
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        params.setMargins((mScreenWidth / 4 - mLabelWidth) / 2, 0, 0, 0);
        label.setLayoutParams(params);
        order_viewpager.setAdapter(adapter);

        OrderTaG = getIntent().getExtras().get("order_tag").toString();
        if (OrderTaG.equals("1")){
            order_viewpager.setCurrentItem(0);
            order_all_text.setTextAppearance(act,R.style.text_bold);
            order_wait_pay_text.setTextAppearance(act,R.style.text_normal);
            order_wait_send_text.setTextAppearance(act,R.style.text_normal);
            order_wait_refund_text.setTextAppearance(act,R.style.text_normal);
        }
        if (OrderTaG.equals("2")){
            order_viewpager.setCurrentItem(1);
            order_all_text.setTextAppearance(act,R.style.text_normal);
            order_wait_pay_text.setTextAppearance(act,R.style.text_bold);
            order_wait_send_text.setTextAppearance(act,R.style.text_normal);
            order_wait_refund_text.setTextAppearance(act,R.style.text_normal);
        }
        if (OrderTaG.equals("3")){
            order_viewpager.setCurrentItem(2);
            order_all_text.setTextAppearance(act,R.style.text_normal);
            order_wait_pay_text.setTextAppearance(act,R.style.text_normal);
            order_wait_send_text.setTextAppearance(act,R.style.text_bold);
            order_wait_refund_text.setTextAppearance(act,R.style.text_normal);
        }
        if (OrderTaG.equals("4")){
            order_viewpager.setCurrentItem(3);
            order_all_text.setTextAppearance(act,R.style.text_normal);
            order_wait_pay_text.setTextAppearance(act,R.style.text_normal);
            order_wait_send_text.setTextAppearance(act,R.style.text_normal);
            order_wait_refund_text.setTextAppearance(act,R.style.text_bold);
        }

    }

    private void init() {
        myorder_back = (RelativeLayout) findViewById(R.id.myorder_back);
        order_wait_refund_text = (TextView) findViewById(R.id.morder_wait_refund_text);
        order_wait_send_text = (TextView) findViewById(R.id.morder_wait_send_text);
        order_all_text = (TextView) findViewById(R.id.morder_all_text);
        order_all = (LinearLayout) findViewById(R.id.morder_all);
        order_wait_pay = (LinearLayout) findViewById(R.id.morder_wait_pay);
        order_wait_send = (LinearLayout) findViewById(R.id.morder_wait_send);
        order_wait_refund = (LinearLayout) findViewById(R.id.morder_wait_refund);
        order_wait_pay_text = (TextView) findViewById(R.id.morder_wait_pay_text);
        label = findViewById(R.id.morder_label);
        order_viewpager = (ViewPager) findViewById(R.id.morder_viewpager);


        order_all.setOnClickListener(this);
        order_wait_pay.setOnClickListener(this);
        order_wait_send.setOnClickListener(this);
        order_wait_refund.setOnClickListener(this);
        myorder_back.setOnClickListener(this);
    }

    private void setLabelWidth() {
        //获取文字宽度
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        order_wait_pay_text.measure(w, h);
        text_width = order_wait_pay_text.getMeasuredWidth();

        // Log.i("width", text_width + "");
        //获取红色标记的属性
        params = (LinearLayout.LayoutParams)label.getLayoutParams();
        //蓝色标记的宽度为 文字宽度+图片宽度+5dp
        mLabelWidth = params.width= text_width +  DensityUtil.dip2px(this, 5);
    }

    private void addFragment() {
        OrderAllFragment a = new OrderAllFragment();
        OrderWaitPayFragment b = new OrderWaitPayFragment();
        OrderWaitTakeFragment c = new OrderWaitTakeFragment();
        OrderAlreadyTakeFragment d = new OrderAlreadyTakeFragment();
        allFragment.add(a);
        allFragment.add(b);
        allFragment.add(c);
        allFragment.add(d);

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.myorder_back:
                finish();
                break;
            case R.id.morder_all:
                order_viewpager.setCurrentItem(0);
                order_all_text.setTextAppearance(act,R.style.text_bold);
                order_wait_pay_text.setTextAppearance(act,R.style.text_normal);
                order_wait_send_text.setTextAppearance(act,R.style.text_normal);
                order_wait_refund_text.setTextAppearance(act,R.style.text_normal);
                break;
            case R.id.morder_wait_pay:
                order_viewpager.setCurrentItem(1);
                order_all_text.setTextAppearance(act,R.style.text_normal);
                order_wait_pay_text.setTextAppearance(act,R.style.text_bold);
                order_wait_send_text.setTextAppearance(act,R.style.text_normal);
                order_wait_refund_text.setTextAppearance(act,R.style.text_normal);
                break;
            case R.id.morder_wait_send:
                order_viewpager.setCurrentItem(2);
                order_all_text.setTextAppearance(act,R.style.text_normal);
                order_wait_pay_text.setTextAppearance(act,R.style.text_normal);
                order_wait_send_text.setTextAppearance(act,R.style.text_bold);
                order_wait_refund_text.setTextAppearance(act,R.style.text_normal);
                break;
            case R.id.morder_wait_refund:
                order_viewpager.setCurrentItem(3);
                order_all_text.setTextAppearance(act,R.style.text_normal);
                order_wait_pay_text.setTextAppearance(act,R.style.text_normal);
                order_wait_send_text.setTextAppearance(act,R.style.text_normal);
                order_wait_refund_text.setTextAppearance(act,R.style.text_bold);
                break;
        }
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

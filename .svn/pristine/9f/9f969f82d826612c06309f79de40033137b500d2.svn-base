package com.jiahehongye.robred.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.adapter.AccountAdapter;
import com.jiahehongye.robred.bean.FaSong;
import com.jiahehongye.robred.bean.QiangDao;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.fragment.RedYuEFrag;
import com.jiahehongye.robred.fragment.RobRedFrag;
import com.jiahehongye.robred.fragment.SendRedFrag;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;

public class MyredActivity extends BaseActivity {

    private RelativeLayout mBack;
    private Call call;
    private PersistentCookieStore persistentCookieStore;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private TextView mUsered;
    private ArrayList<QiangDao> qdAll = new ArrayList<>();
    private ArrayList<FaSong> fsAll = new ArrayList<>();
    private AccountAdapter adapter;
    private int PAGENUMBER = 1;
    private String NUMBERS = "20";
    private static final int GET_ALL = 0000;
    private static final int GET_SEND = 0001;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_ALL:
                    String s = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(s);
                        if (object.getString("result").equals("success")) {
                            JSONObject data = new JSONObject(object.getString("data"));
                            String grabRedEnveAmount = data.getString("grabRedEnveAmount");
                            middle_num.setText(""+grabRedEnveAmount);
                            String grabRendEnveList = data.getString("grabRendEnveList");
                            ArrayList<QiangDao> qd = (ArrayList<QiangDao>) JSON.parseArray(grabRendEnveList,QiangDao.class);
                            qdAll.addAll(qd);
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case GET_SEND:
                    String ss = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(ss);
                        if (object.getString("result").equals("success")) {
                            JSONObject data = new JSONObject(object.getString("data"));
                            String sendRedEnveAmount = data.getString("sendRedEnveAmount");
                            middle_num.setText(""+sendRedEnveAmount);
                            String sendRendEnveList = data.getString("sendRendEnveList");
                            ArrayList<FaSong> fs = (ArrayList<FaSong>) JSON.parseArray(sendRendEnveList,FaSong.class);
                            fsAll.addAll(fs);
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //sendRedEnveAmount
                    break;
            }
        }
    };
    private int status = 1;
    private TextView myred_rb_rob,myred_rb_send;
    private TextView middle_num;
    private ViewPager red_viewpager;
    private ArrayList<Fragment> allFragment =new ArrayList<>();
    private TextView myred_rb_ye;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_myred);

        red_viewpager = (ViewPager) findViewById(R.id.red_viewpager);
        myred_rb_ye = (TextView) findViewById(R.id.myred_rb_ye);
        mBack = (RelativeLayout) findViewById(R.id.myred_rl_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        myred_rb_send = (TextView) findViewById(R.id.myred_rb_send);
        myred_rb_rob = (TextView) findViewById(R.id.myred_rb_rob);

//        mListView = (ListView) findViewById(R.id.myred_lv);

        RobRedFrag a= new RobRedFrag();
        SendRedFrag b = new SendRedFrag();
        RedYuEFrag c = new RedYuEFrag();
        allFragment.add(c);
        allFragment.add(a);
        allFragment.add(b);

        myred_rb_ye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                red_viewpager.setCurrentItem(0);
                myred_rb_ye.setTextColor(getResources().getColor(R.color.white));
                myred_rb_ye.setBackgroundResource(R.drawable.myred_bg_redbalance_red);
                myred_rb_rob.setBackgroundResource(R.drawable.myred_bg_robred_white);
                myred_rb_rob.setTextColor(getResources().getColor(R.color.billion_tv_ss));
                myred_rb_send.setTextColor(getResources().getColor(R.color.billion_tv_ss));
                myred_rb_send.setBackgroundResource(R.drawable.myred_bg_sendred_white);
            }
        });

        myred_rb_rob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                red_viewpager.setCurrentItem(1);
                myred_rb_ye.setTextColor(getResources().getColor(R.color.billion_tv_ss));
                myred_rb_ye.setBackgroundResource(R.drawable.myred_bg_redbalance);
                myred_rb_rob.setBackgroundResource(R.drawable.myred_bg_robred_red);
                myred_rb_rob.setTextColor(getResources().getColor(R.color.white));
                myred_rb_send.setTextColor(getResources().getColor(R.color.billion_tv_ss));
                myred_rb_send.setBackgroundResource(R.drawable.myred_bg_sendred_white);

            }
        });
        myred_rb_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                red_viewpager.setCurrentItem(2);
                myred_rb_ye.setTextColor(getResources().getColor(R.color.billion_tv_ss));
                myred_rb_ye.setBackgroundResource(R.drawable.myred_bg_redbalance_white);
                myred_rb_rob.setTextColor(getResources().getColor(R.color.billion_tv_ss));
                myred_rb_send.setTextColor(getResources().getColor(R.color.white));
                myred_rb_rob.setBackgroundResource(R.drawable.myred_bg_robred_white);
                myred_rb_send.setBackgroundResource(R.drawable.myred_bg_sendred_red);
            }
        });

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), allFragment);
        red_viewpager.setCurrentItem(0);


        red_viewpager.setAdapter(adapter);


        red_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i){
                    case 0:
                        red_viewpager.setCurrentItem(0);
                        myred_rb_ye.setTextColor(getResources().getColor(R.color.white));
                        myred_rb_ye.setBackgroundResource(R.drawable.myred_bg_redbalance_red);
                        myred_rb_rob.setBackgroundResource(R.drawable.myred_bg_robred_white);
                        myred_rb_rob.setTextColor(getResources().getColor(R.color.billion_tv_ss));
                        myred_rb_send.setTextColor(getResources().getColor(R.color.billion_tv_ss));
                        myred_rb_send.setBackgroundResource(R.drawable.myred_bg_sendred_white);
                        break;
                    case 1:
                        red_viewpager.setCurrentItem(1);
                        myred_rb_ye.setTextColor(getResources().getColor(R.color.billion_tv_ss));
                        myred_rb_ye.setBackgroundResource(R.drawable.myred_bg_redbalance);
                        myred_rb_rob.setBackgroundResource(R.drawable.myred_bg_robred_red);
                        myred_rb_rob.setTextColor(getResources().getColor(R.color.white));
                        myred_rb_send.setTextColor(getResources().getColor(R.color.billion_tv_ss));
                        myred_rb_send.setBackgroundResource(R.drawable.myred_bg_sendred_white);
                        break;
                    case 2:
                        red_viewpager.setCurrentItem(2);
                        myred_rb_ye.setTextColor(getResources().getColor(R.color.billion_tv_ss));
                        myred_rb_ye.setBackgroundResource(R.drawable.myred_bg_redbalance_white);
                        myred_rb_rob.setTextColor(getResources().getColor(R.color.billion_tv_ss));
                        myred_rb_send.setTextColor(getResources().getColor(R.color.white));
                        myred_rb_rob.setBackgroundResource(R.drawable.myred_bg_robred_white);
                        myred_rb_send.setBackgroundResource(R.drawable.myred_bg_sendred_red);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mUsered = (TextView) findViewById(R.id.myred_tv_usered);
        mUsered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UseredActivity.class));
            }
        });
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

package com.jiahehongye.robred.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.QiangDao;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.GlideCircleTransform;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MySendRedDetailActivity extends BaseActivity {

    private ListView mListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int PAGENUMBER = 1;
    private Call call;
    private PersistentCookieStore persistentCookieStore;
    private Adapter mAdapter;
    private ArrayList<QiangDao> qiangDaos = new ArrayList<>();
    private String NUMBERS = "20";
    private static final int GET_ALL = 0000;
    private static final int DETAIL = 1100;
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
                            String grabRedEnveList = data.getString("grabRedEnveList");
                            ArrayList<QiangDao> a = (ArrayList<QiangDao>) JSON.parseArray(grabRedEnveList,QiangDao.class);
                            qiangDaos.addAll(a);
                            mAdapter.notifyDataSetChanged();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case DETAIL:
                    String de = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(de);
                        if (object.getString("result").equals("success")) {
                            JSONObject data = new JSONObject(object.getString("data"));
                            String redEnveLeaveword = data.getString("redEnveLeaveword");
                            if (!TextUtils.isEmpty(redEnveLeaveword)){
                                detail_liuyan.setText(redEnveLeaveword);
                            }
                            String redEnveMoney = data.getString("redEnveMoney");
                            String redEnveNum =data.getString("redEnveNum");
                            String grabMoney = data.getString("grabMoney");
                            String grabRedEnve = data.getString("grabRedEnve");
                            detail_numberdetail.setText("已领取"+grabRedEnve+"/"+redEnveNum+"个,共"+grabMoney+"/"+redEnveMoney+"元");
                            String enveMark =  data.getString("enveMark");
                            reddetail_status.setText(enveMark);
                            String grabRedEnveAd = data.getString("grabRedEnveAd");
                            String redEnveType = data.getString("redEnveType");
                            if (redEnveType.equals("1")){
                                detail_GGimg.setVisibility(View.VISIBLE);
                                displayNetImage(grabRedEnveAd,detail_guanImg);
                                String redIntroduction = data.getString("redIntroduction");
                                introduction.setText(redIntroduction);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }
    };
    private String redEnveCode;
    private TextView detail_liuyan,detail_numberdetail,introduction;
    private LinearLayout detail_GGimg;
    private ImageView detail_guanImg;
    private TextView reddetail_status;
    private RelativeLayout send_detailrl_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_my_send_red_detail);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            redEnveCode = bundle.getString("redEnveCode");
        }

        mListView = (ListView) findViewById(R.id.mysendreddetail_lv);
        mListView.addHeaderView(LayoutInflater.from(getApplicationContext()).inflate(R.layout.detail_head,null));

        detail_liuyan = (TextView) findViewById(R.id.detail_liuyan);
        send_detailrl_back = (RelativeLayout) findViewById(R.id.send_detailrl_back);
        reddetail_status = (TextView) findViewById(R.id.reddetail_status);
        introduction = (TextView) findViewById(R.id.introduction);
        detail_guanImg = (ImageView) findViewById(R.id.detail_guanImg);
        detail_GGimg = (LinearLayout) findViewById(R.id.detail_GGimg);
        detail_numberdetail = (TextView) findViewById(R.id.detail_numberdetail);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mysendreddetail_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                PAGENUMBER = 1;
                qiangDaos.clear();
                getdata();
            }
        });
        getdata();
        detail();
        mAdapter = new Adapter();
        mListView.setAdapter(mAdapter);

        send_detailrl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        //加载更多功能的代码
                        PAGENUMBER = PAGENUMBER + 1;
                        getdata();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void detail(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(MySendRedDetailActivity.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("redEnveCode",redEnveCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.SEND_DETAIL_top)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MySendRedDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MySendRedDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                Message msg = handler.obtainMessage();
                msg.what = DETAIL;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    private void getdata() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(MySendRedDetailActivity.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("redEnveCode",redEnveCode);
            jsonObject.put("pageNumber", PAGENUMBER + "");
            jsonObject.put("pageSize", NUMBERS);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.SEND_DETAIL)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MySendRedDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MySendRedDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                Message msg = handler.obtainMessage();
                msg.what = GET_ALL;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return qiangDaos.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.sendred_detail_item, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.robs_name);
                holder.time = (TextView) convertView.findViewById(R.id.robs_time);
                holder.price = (TextView) convertView.findViewById(R.id.robs_price);
                holder.detail_head = (ImageView) convertView.findViewById(R.id.detail_head);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            String head = qiangDaos.get(position).getUserPhoto();
            if (head==null||head.equals("")){
                Glide.with(act).load(R.mipmap.avatar).transform(new GlideCircleTransform(UIUtils.getContext())).into(holder.detail_head);
            }else {
                Glide.with(act).load(head).transform(new GlideCircleTransform(UIUtils.getContext())).into(holder.detail_head);
            }


            holder.time.setText(qiangDaos.get(position).getGrabDate());
            holder.name.setText(qiangDaos.get(position).getNickName());
            holder.price.setText(qiangDaos.get(position).getGrabMoney()+"元");

            return convertView;
        }

        class ViewHolder {
            TextView name, time, price, sendPrice, status;
            LinearLayout send_layout;
            ImageView detail_head;
        }
    }
}

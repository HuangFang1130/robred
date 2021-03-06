package com.jiahehongye.robred.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.QiangDao;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;

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

public class YuEBalance extends BaseActivity {

    private RelativeLayout chongzhiyuemx_back;
    private TextView chongzhiyuemx_num;
    private ListView chongzhiyemx_lv;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    private Call call;
    private PersistentCookieStore persistentCookieStore;
    private ListView mListView;
    private Adapter adapter;
    private int PAGENUMBER = 1;
    private ArrayList<QiangDao> qdAll = new ArrayList<>();
    private String NUMBERS = "20";
    private static final int GET_ALL = 0000;
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
                            String accountBala = data.getString("accountBala");
                            chongzhiyuemx_num.setText("¥"+accountBala);
                            String accoRedEnveDetail = data.getString("accoRedEnveDetail");
                            ArrayList<QiangDao> qd = (ArrayList<QiangDao>) JSON.parseArray(accoRedEnveDetail,QiangDao.class);
                            qdAll.addAll(qd);
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_yu_ebalance);
        chongzhiyuemx_back = (RelativeLayout) findViewById(R.id.chongzhiyuemx_back);
        chongzhiyuemx_num = (TextView) findViewById(R.id.chongzhiyuemx_num);
        chongzhiyemx_lv = (ListView) findViewById(R.id.chongzhiyemx_lv);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.myred_swiperefreshlayoutmx);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                qdAll.clear();
                PAGENUMBER = 1;
                getdata();
            }
        });

        chongzhiyuemx_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getdata();
        adapter = new Adapter();
        chongzhiyemx_lv.setAdapter(adapter);

        chongzhiyemx_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
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

    private void getdata() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(YuEBalance.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("pageNumber", PAGENUMBER + "");
            jsonObject.put("pageSize", NUMBERS);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.CHONGZHI_YUE_BALANCE)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                YuEBalance.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(YuEBalance.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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
            return qdAll.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_account, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.red_name);
                holder.time = (TextView) convertView.findViewById(R.id.red_time);
                holder.price = (TextView) convertView.findViewById(R.id.red_price);
                holder.sendPrice = (TextView) convertView.findViewById(R.id.red_send_price);
                holder.status = (TextView) convertView.findViewById(R.id.red_status);
                holder.send_layout = (LinearLayout) convertView.findViewById(R.id.send_layout);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.time.setText(qdAll.get(position).getCreateDate());
            if (qdAll.get(position).getChangeType().equals("1")){
                holder.name.setText("充值");
                holder.price.setText("+"+qdAll.get(position).getAccountMoney());
            }else if (qdAll.get(position).getChangeType().equals("2")){
                holder.name.setText("提现");
                holder.price.setText("-"+qdAll.get(position).getAccountMoney());
            }
            else if (qdAll.get(position).getChangeType().equals("3")){
                holder.name.setText("发红包");
                holder.price.setText("-"+qdAll.get(position).getAccountMoney());
            }
            else if (qdAll.get(position).getChangeType().equals("4")){
                holder.name.setText("商品消费");
                holder.price.setText("-"+qdAll.get(position).getAccountMoney());
            }
            else if (qdAll.get(position).getChangeType().equals("5")){
                holder.name.setText("红包退款");
                holder.price.setText("+"+qdAll.get(position).getAccountMoney());
            }
            else if (qdAll.get(position).getChangeType().equals("6")){
                holder.name.setText("拼运气");
                holder.price.setText("-"+qdAll.get(position).getAccountMoney());
            }
            else if (qdAll.get(position).getChangeType().equals("7")){
                holder.name.setText("金币兑换");
                holder.price.setText("+"+qdAll.get(position).getAccountMoney());
            }
            else if (qdAll.get(position).getChangeType().equals("8")){
                holder.name.setText("鲜花兑换");
                holder.price.setText("+"+qdAll.get(position).getAccountMoney());
            }
            else if (qdAll.get(position).getChangeType().equals("9")){
                holder.name.setText("充值钻石");
                holder.price.setText("-"+qdAll.get(position).getAccountMoney());
            }

            return convertView;
        }

        class ViewHolder {
            TextView name, time, price, sendPrice, status;
            LinearLayout send_layout;
        }
    }
}

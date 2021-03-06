package com.jiahehongye.robred.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.RedDetailBean;
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

/**
 * Created by qianduan on 2016/12/30.
 */
public class RedYuEFrag extends Fragment {
    private View view;
    private Call call;
    private PersistentCookieStore persistentCookieStore;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private Adapter adapter;
    private TextView middle_num;
    private int PAGENUMBER = 1;
    private ArrayList<RedDetailBean> qdAll = new ArrayList<>();
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
                            String accoRedEnve = data.getString("accoRedEnve");
                            middle_num.setText("¥"+accoRedEnve);
                            String accoRedEnveDetail = data.getString("accoRedEnveDetail");
                            ArrayList<RedDetailBean> qd = (ArrayList<RedDetailBean>) JSON.parseArray(accoRedEnveDetail,RedDetailBean.class);
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.rob_red_frag, container, false);

        middle_num = (TextView) view.findViewById(R.id.middle_num);
        mListView  = (ListView) view.findViewById(R.id.myred_lv);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.myred_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                PAGENUMBER = 1;
                qdAll.clear();
                getdata();
            }
        });

        //解决listview和swiperefreshlayout滑动冲突
//        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem,
//                                 int visibleItemCount, int totalItemCount) {
//                boolean enable = false;
//                if (mListView != null && mListView.getChildCount() > 0) {
//                    // check if the first item of the list is visible
//                    boolean firstItemVisible = mListView.getFirstVisiblePosition() == 0;
//                    // check if the top of the first item is visible
//                    boolean topOfFirstItemVisible = mListView.getChildAt(0).getTop() == 0;
//                    // enabling or disabling the refresh layout
//                    enable = firstItemVisible && topOfFirstItemVisible;
//                }
//                mSwipeRefreshLayout.setEnabled(enable);
//            }
//        });

        adapter = new Adapter();
        mListView.setAdapter(adapter);

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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getdata();
    }

    private void getdata() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
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
                .url(Constant.RED_YUELIST)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_account, null);
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
                holder.name.setText("抢红包");
                holder.price.setText("+"+qdAll.get(position).getRedEnveMoney());
            }else if (qdAll.get(position).getChangeType().equals("2")){
                holder.name.setText("提现");
                holder.price.setText("-"+qdAll.get(position).getRedEnveMoney());
            }
            else if (qdAll.get(position).getChangeType().equals("3")){
                holder.name.setText("商品消费");
                holder.price.setText("-"+qdAll.get(position).getRedEnveMoney());
            }
            else if (qdAll.get(position).getChangeType().equals("4")){
                holder.name.setText("发红包");
                holder.price.setText("-"+qdAll.get(position).getRedEnveMoney());
            }
            else if (qdAll.get(position).getChangeType().equals("5")){
                holder.name.setText("一级分享");
                holder.price.setText("+"+qdAll.get(position).getRedEnveMoney());
            }
            else if (qdAll.get(position).getChangeType().equals("6")){
                holder.name.setText("二级分享");
                holder.price.setText("+"+qdAll.get(position).getRedEnveMoney());
            }
            else if (qdAll.get(position).getChangeType().equals("7")){
                holder.name.setText("拼运气");
                holder.price.setText("-"+qdAll.get(position).getRedEnveMoney());
            }else if (qdAll.get(position).getChangeType().equals("8")){
                holder.name.setText("充值钻石");
                holder.price.setText("-"+qdAll.get(position).getRedEnveMoney());
            }

            return convertView;
        }

        class ViewHolder {
            TextView name, time, price, sendPrice, status;
            LinearLayout send_layout;
        }
    }
}

package com.jiahehongye.robred.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.jiahehongye.robred.bean.JILuBean;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.view.MyProgressDialog;

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

public class LingQuJiLuActivity extends BaseActivity {

    private String type;
    private PersistentCookieStore persistentCookieStore;
    private MyProgressDialog animDialog;
    private Adapter adapter;
    private Call call;
    private int PAGENUMBER = 1;
    private String NUMBERS = "2";
    private static final int GET_ALL = 0000;
    private ArrayList<JILuBean> all = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_ALL:
                    animDialog.dismiss();
                    if (type.equals("1")){
                        String s = (String) msg.obj;
                        try {
                            JSONObject object = new JSONObject(s);
                            if (object.getString("result").equals("success")) {
                                JSONObject data = new JSONObject(object.getString("data"));
                                String cash = data.getString("cash");
                                gift_middle_num.setText("¥"+cash);
                                String goldL = data.getString("goldL");
                                ArrayList<JILuBean> jl = (ArrayList<JILuBean>) JSON.parseArray(goldL,JILuBean.class);
                                all.addAll(jl);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (type.equals("2")){
                        String s = (String) msg.obj;
                        try {
                            JSONObject object = new JSONObject(s);
                            if (object.getString("result").equals("success")) {
                                JSONObject data = new JSONObject(object.getString("data"));
                                String cash = data.getString("cash");
                                gift_middle_num.setText("¥"+cash);
                                String flowerL = data.getString("flowerL");
                                ArrayList<JILuBean> jl = (ArrayList<JILuBean>) JSON.parseArray(flowerL,JILuBean.class);
                                all.addAll(jl);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    break;

            }
        }
    };
    private ListView mListView;
    private TextView gift_middle_num;
    private RelativeLayout gift_jilu_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_ling_qu_ji_lu);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            type = bundle.getString("type");
        }
        gift_jilu_back = (RelativeLayout) findViewById(R.id.gift_jilu_back);
        gift_jilu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView = (ListView) findViewById(R.id.gift_lv);
        gift_middle_num = (TextView) findViewById(R.id.gift_middle_num);
        getdata();
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

    }


    private void getdata() {

        showMyDialog();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getApplicationContext());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("type",type);
            jsonObject.put("pageNumber", PAGENUMBER + "");
            jsonObject.put("pageSize", NUMBERS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.GIFT_JILU)
                .post(body)
                .build();



        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LingQuJiLuActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LingQuJiLuActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        animDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i("df1",result);
                Message msg = handler.obtainMessage();
                msg.what = GET_ALL;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });

    }

    /**
     * 显示对话框
     */
    public void showMyDialog(){
        animDialog =new MyProgressDialog(this, "玩命加载中...",R.drawable.loading);
        animDialog.show();
        animDialog.setCancelable(true);
        animDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(call.isExecuted()){
                    call.cancel();
                }
            }
        });
    }


    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return all.size();
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

            holder.time.setText(all.get(position).getCreateDate());
            if (type.equals("1")){
                holder.name.setText("金币领取");
            }else if (type.equals("2")){
                holder.name.setText("鲜花领取");
            }

            holder.price.setText("+"+all.get(position).getCash());
            holder.price.setTextColor(getResources().getColor(R.color.home_state_color));
            return convertView;
        }

        class ViewHolder {
            TextView name, time, price, sendPrice, status;
            LinearLayout send_layout;
        }
    }
}

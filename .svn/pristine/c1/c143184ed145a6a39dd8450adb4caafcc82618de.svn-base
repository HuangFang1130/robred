package com.jiahehongye.robred.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.GoldRankBean;
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

public class GoldRankActivity extends BaseActivity {

    private RelativeLayout mBack;
    private Call call;
    private PersistentCookieStore persistentCookieStore;
    private ListView goldrank_list;
    private ArrayList<GoldRankBean> been = new ArrayList<>();
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

                            String personnum  = data.getString("personalGold");
                            geren_goldnum.setText(personnum);
                            String goldCoinL =data.getString("goldCoinL");
                            ArrayList<GoldRankBean> a = (ArrayList<GoldRankBean>) JSON.parseArray(goldCoinL, GoldRankBean.class);
                            been.addAll(a);
                            adapter.notifyDataSetChanged();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };
    private Adapter adapter;
    private int PAGENUMBER = 1;
    private String NUMBERS = "2";
    private TextView geren_goldnum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        setContentView(R.layout.activity_gold_rank);
        geren_goldnum = (TextView) findViewById(R.id.geren_goldnum);
        mBack = (RelativeLayout) findViewById(R.id.goldrank_rl_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        goldrank_list = (ListView) findViewById(R.id.goldrank_list);
        adapter = new Adapter();

        getdata();

        goldrank_list.setAdapter(adapter);

        goldrank_list.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        persistentCookieStore = new PersistentCookieStore(GoldRankActivity.this);
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
                .url(Constant.GOLD_RANK)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                GoldRankActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GoldRankActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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


    private class Adapter extends BaseAdapter{

        private final int TYPE_ONE=0,TYPE_TWO=1,TYPE_THREE=2,TYPE_FOUR=3,TYPE_COUNT=4;

        @Override
        public int getCount() {
            return been.size();
        }

        @Override
        public int getViewTypeCount() {

            return TYPE_COUNT;
        }

        @Override
        public int getItemViewType(int position) {
            if (position ==0){
                return TYPE_ONE;
            }else if (position == 1){
                return TYPE_TWO;
            }else if (position == 2){
                return TYPE_THREE;
            }else {
                return TYPE_FOUR;
            }
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
            ViewHolder1 vh1=null;
            ViewHolder2 vh2=null;
            ViewHolder3 vh3=null;
            ViewHolder4 vh4=null;
            int type=getItemViewType(position);
            if(convertView==null){
                switch (type) {
                    case TYPE_ONE:
                        vh1=new ViewHolder1();
                        convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.gold_rank_item, null);
                        vh1.rank_number1 = (TextView) convertView.findViewById(R.id.rank_number1);
                        vh1.gd_head1 = (ImageView) convertView.findViewById(R.id.gd_head1);
                        vh1.gd_name1 = (TextView) convertView.findViewById(R.id.gd_name1);
                        vh1.gd_num1 = (TextView) convertView.findViewById(R.id.gd_num1);
                        vh1.gd_sex1 = (ImageView) convertView.findViewById(R.id.gd_sex1);
                        vh1.gd_level1 = (ImageView) convertView.findViewById(R.id.gd_level1);
                        convertView.setTag(vh1);
                        break;
                    case TYPE_TWO:
                        vh2=new ViewHolder2();
                        convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.gold_rank_item2, null);
                        vh2.rank_number2 = (TextView) convertView.findViewById(R.id.rank_number2);
                        vh2.gd_head2 = (ImageView) convertView.findViewById(R.id.gd_head2);
                        vh2.gd_name2 = (TextView) convertView.findViewById(R.id.gd_name2);
                        vh2.gd_num2 = (TextView) convertView.findViewById(R.id.gd_num2);
                        vh2.gd_sex2 = (ImageView) convertView.findViewById(R.id.gd_sex2);
                        vh2.gd_level2 = (ImageView) convertView.findViewById(R.id.gd_level2);
                        convertView.setTag(vh2);
                        break;
                    case TYPE_THREE:
                        vh3=new ViewHolder3();
                        convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.gold_rank_item3, null);
                        vh3.rank_number3 = (TextView) convertView.findViewById(R.id.rank_number3);
                        vh3.gd_head3 = (ImageView) convertView.findViewById(R.id.gd_head3);
                        vh3.gd_name3 = (TextView) convertView.findViewById(R.id.gd_name3);
                        vh3.gd_num3 = (TextView) convertView.findViewById(R.id.gd_num3);
                        vh3.gd_sex3 = (ImageView) convertView.findViewById(R.id.gd_sex3);
                        vh3.gd_level3 = (ImageView) convertView.findViewById(R.id.gd_level3);
                        convertView.setTag(vh3);
                        break;
                    case TYPE_FOUR:
                        vh4=new ViewHolder4();
                        convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.gold_rank_item4, null);
                        vh4.rank_number4 = (TextView) convertView.findViewById(R.id.rank_number4);
                        vh4.gd_head4 = (ImageView) convertView.findViewById(R.id.gd_head4);
                        vh4.gd_name4 = (TextView) convertView.findViewById(R.id.gd_name4);
                        vh4.gd_num4 = (TextView) convertView.findViewById(R.id.gd_num4);
                        vh4.gd_sex4 = (ImageView) convertView.findViewById(R.id.gd_sex4);
                        vh4.gd_level4 = (ImageView) convertView.findViewById(R.id.gd_level4);
                        convertView.setTag(vh4);
                        break;
                }
            }else {
                switch (type) {
                    case TYPE_ONE:
                        vh1=(ViewHolder1) convertView.getTag();
                        break;
                    case TYPE_TWO:
                        vh2=(ViewHolder2) convertView.getTag();
                        break;
                    case TYPE_THREE:
                        vh3=(ViewHolder3) convertView.getTag();
                        break;
                    case TYPE_FOUR:
                        vh4=(ViewHolder4) convertView.getTag();
                        break;
                }
            }
            switch (type){
                case TYPE_ONE:
                    Glide.with(UIUtils.getContext()).load(been.get(position).getUSER_PHOTO()).transform(
                            new GlideCircleTransform(UIUtils.getContext())).into(vh1.gd_head1);
                    vh1.gd_name1.setText(been.get(position).getNickName());
                    vh1.gd_num1.setText(been.get(position).getTotalGold());
                    if (been.get(position).getGENDER().equals("0")){
                        vh1.gd_sex1.setVisibility(View.GONE);
                    }else if (been.get(position).getGENDER().equals("1")){
                        vh1.gd_sex1.setImageResource(R.mipmap.men);
                    }else if (been.get(position).getGENDER().equals("2")){
                        vh1.gd_sex1.setImageResource(R.mipmap.women);
                    }

                    if (been.get(position).getUserLevel().equals("5")){
                        vh1.gd_level1.setImageResource(R.mipmap.loacl_lord);
                    }else if (been.get(position).getUserLevel().equals("1")){
                        vh1.gd_level1.setImageResource(R.mipmap.general);
                    }else if (been.get(position).getUserLevel().equals("2")){
                        vh1.gd_level1.setImageResource(R.mipmap.white_collar);
                    }else if (been.get(position).getUserLevel().equals("3")){
                        vh1.gd_level1.setImageResource(R.mipmap.gold_collar);
                    }else if (been.get(position).getUserLevel().equals("4")){
                        vh1.gd_level1.setImageResource(R.mipmap.boss);
                    }
                    break;
                case TYPE_TWO:
                    Glide.with(UIUtils.getContext()).load(been.get(position).getUSER_PHOTO()).transform(
                            new GlideCircleTransform(UIUtils.getContext())).into(vh2.gd_head2);
                    vh2.gd_name2.setText(been.get(position).getNickName());
                    vh2.gd_num2.setText(been.get(position).getTotalGold());
                    if (been.get(position).getGENDER().equals("0")){
                        vh2.gd_sex2.setVisibility(View.GONE);
                    }else if (been.get(position).getGENDER().equals("1")){
                        vh2.gd_sex2.setImageResource(R.mipmap.men);
                    }else if (been.get(position).getGENDER().equals("2")){
                        vh2.gd_sex2.setImageResource(R.mipmap.women);
                    }

                    if (been.get(position).getUserLevel().equals("5")){
                        vh2.gd_level2.setImageResource(R.mipmap.loacl_lord);
                    }else if (been.get(position).getUserLevel().equals("1")){
                        vh2.gd_level2.setImageResource(R.mipmap.general);
                    }else if (been.get(position).getUserLevel().equals("2")){
                        vh2.gd_level2.setImageResource(R.mipmap.white_collar);
                    }else if (been.get(position).getUserLevel().equals("3")){
                        vh2.gd_level2.setImageResource(R.mipmap.gold_collar);
                    }else if (been.get(position).getUserLevel().equals("4")){
                        vh2.gd_level2.setImageResource(R.mipmap.boss);
                    }
                    vh2.rank_number2.setText("NO."+(position+1));
                    break;
                case TYPE_THREE:
                    Glide.with(UIUtils.getContext()).load(been.get(position).getUSER_PHOTO()).transform(
                            new GlideCircleTransform(UIUtils.getContext())).into(vh3.gd_head3);
                    vh3.gd_name3.setText(been.get(position).getNickName());
                    vh3.gd_num3.setText(been.get(position).getTotalGold());
                    if (been.get(position).getGENDER().equals("0")){
                        vh3.gd_sex3.setVisibility(View.GONE);
                    }else if (been.get(position).getGENDER().equals("1")){
                        vh3.gd_sex3.setImageResource(R.mipmap.men);
                    }else if (been.get(position).getGENDER().equals("2")){
                        vh3.gd_sex3.setImageResource(R.mipmap.women);
                    }

                    if (been.get(position).getUserLevel().equals("5")){
                        vh3.gd_level3.setImageResource(R.mipmap.loacl_lord);
                    }else if (been.get(position).getUserLevel().equals("1")){
                        vh3.gd_level3.setImageResource(R.mipmap.general);
                    }else if (been.get(position).getUserLevel().equals("2")){
                        vh3.gd_level3.setImageResource(R.mipmap.white_collar);
                    }else if (been.get(position).getUserLevel().equals("3")){
                        vh3.gd_level3.setImageResource(R.mipmap.gold_collar);
                    }else if (been.get(position).getUserLevel().equals("4")){
                        vh3.gd_level3.setImageResource(R.mipmap.boss);
                    }
                    vh3.rank_number3.setText("NO."+(position+1));
                    break;
                case TYPE_FOUR:
                    vh4.rank_number4.setText("NO."+(position+1));
                    Glide.with(UIUtils.getContext()).load(been.get(position).getUSER_PHOTO()).transform(
                            new GlideCircleTransform(UIUtils.getContext())).into(vh4.gd_head4);
                    vh4.gd_name4.setText(been.get(position).getNickName());
                    vh4.gd_num4.setText(been.get(position).getTotalGold());
                    if (been.get(position).getGENDER().equals("0")){
                        vh4.gd_sex4.setVisibility(View.GONE);
                    }else if (been.get(position).getGENDER().equals("1")){
                        vh4.gd_sex4.setImageResource(R.mipmap.men);
                    }else if (been.get(position).getGENDER().equals("2")){
                        vh4.gd_sex4.setImageResource(R.mipmap.women);
                    }

                    if (been.get(position).getUserLevel().equals("5")){
                        vh4.gd_level4.setImageResource(R.mipmap.loacl_lord);
                    }else if (been.get(position).getUserLevel().equals("1")){
                        vh4.gd_level4.setImageResource(R.mipmap.general);
                    }else if (been.get(position).getUserLevel().equals("2")){
                        vh4.gd_level4.setImageResource(R.mipmap.white_collar);
                    }else if (been.get(position).getUserLevel().equals("3")){
                        vh4.gd_level4.setImageResource(R.mipmap.gold_collar);
                    }else if (been.get(position).getUserLevel().equals("4")){
                        vh4.gd_level4.setImageResource(R.mipmap.boss);
                    }
                    break;
            }
            return convertView;
        }
        class ViewHolder1{
            TextView rank_number1;
            ImageView gd_head1;
            TextView gd_name1;
            TextView gd_num1;
            ImageView gd_sex1;
            ImageView gd_level1;
        }
        class ViewHolder2{
            TextView rank_number2;
            ImageView gd_head2;
            TextView gd_name2;
            TextView gd_num2;
            ImageView gd_sex2;
            ImageView gd_level2;
        }
        class ViewHolder3{
            TextView rank_number3;
            ImageView gd_head3;
            TextView gd_name3;
            TextView gd_num3;
            ImageView gd_sex3;
            ImageView gd_level3;
        }
        class ViewHolder4{
            TextView rank_number4;
            ImageView gd_head4;
            TextView gd_name4;
            TextView gd_num4;
            ImageView gd_sex4;
            ImageView gd_level4;
        }
    }
}

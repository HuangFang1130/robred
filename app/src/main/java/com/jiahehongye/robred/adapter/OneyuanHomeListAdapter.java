package com.jiahehongye.robred.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.OneyuanHomeProductListBean.ProductList;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.oneyuan.OneyuanPayActivity;
import com.jiahehongye.robred.oneyuan.OneyuanProductDetailActivity;
import com.jiahehongye.robred.view.MyProgressDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OneyuanHomeListAdapter extends BaseAdapter {

    private MyProgressDialog animDialog;

    private LayoutInflater inflater;
    private List<ProductList> list;
    private Context context;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    animDialog.dismiss();
                    String[] str = (String[]) msg.obj;
                    String dataJson = str[0];
                    String productId = str[1];
                    try {
                        JSONObject result = new JSONObject(dataJson);
                        if (result.get("result").equals("success")) {//未参与
                            JSONObject data = result.getJSONObject("data");
                            JSONArray memberList = data.getJSONArray("memberList");

                            JSONObject accoRedEnve = memberList.getJSONObject(0);
                            String money = accoRedEnve.getString("accoRedEnve");

                            Intent intent = new Intent();
                            intent.setClass(context, OneyuanPayActivity.class);
                            intent.putExtra("productId", productId);
                            intent.putExtra("accoRedEnve", money);
                            context.startActivity(intent);
                        } else if (result.getString("result").equals("fail")) {//已参与
                            Toast.makeText(context, "已参与", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;

                case 111:
                    Toast.makeText(context, "网络请求失败", Toast.LENGTH_SHORT).show();
                    animDialog.dismiss();
                    break;
            }
        }
    };

    public OneyuanHomeListAdapter(Context context, List<ProductList> list) {
        super();
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home_hotsales, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_oneyuan_home_item_title);
            holder.image = (ImageView) convertView.findViewById(R.id.iv_oneyuan_home_item_image);
            holder.join = (Button) convertView.findViewById(R.id.btn_onyuan_home_item_join);
            holder.totalNum = (TextView) convertView.findViewById(R.id.tv_oneyuan_home_item_totalNum);
            holder.leftNum = (TextView) convertView.findViewById(R.id.tv_oneyuan_home_item_leftNum);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.pb_oneyuan_home_item_bar);
            holder.detail = (TextView) convertView.findViewById(R.id.tv_oneyuan_home_item_detail);
            holder.price = (TextView) convertView.findViewById(R.id.tv_oneyuan_home_item_price);
            convertView.setTag(R.id.tag_holder, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.tag_holder);
        }

        String totalNum = list.get(position).getTotalPeople();
        String leftNum = list.get(position).getSurplusPeople();
        int percent = 100 - Integer.parseInt(leftNum) * 100 / Integer.parseInt(totalNum);
        Picasso.with(context).load(list.get(position).getImage()).into(holder.image);
        ;
        holder.name.setText(list.get(position).getName());
        holder.totalNum.setText(totalNum);
        holder.leftNum.setText(leftNum);
        holder.price.setText(list.get(position).getPrice());
        holder.detail.setText(list.get(position).getComments());
        holder.progressBar.setProgress(percent);


        holder.join.setTag(R.id.tag_adapter, position);

        if (list.get(position).getIsParticipate().equals("1")) {
            holder.join.setBackgroundResource(R.drawable.oneyuan_win_submit_complete);
            holder.join.setText("已参与");
            holder.join.setClickable(false);
        } else {
            holder.join.setText("我要参与");
            holder.join.setBackgroundResource(R.drawable.selector_shape_register);
            holder.join.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String productId = list.get((Integer) v.getTag(R.id.tag_adapter)).getProductId();
                    initData(productId);
                }
            });
        }

        convertView.setTag(R.id.tag_adapter, position);
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context, OneyuanProductDetailActivity.class);
                String productId = list.get((Integer) v.getTag(R.id.tag_adapter)).getProductId();
                Log.e("传过去产品的ID：", productId);
                intent.putExtra("productId", productId);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    /**
     * 点击我要参与请求
     */
    private PersistentCookieStore persistentCookieStore;
    private Call call;
    protected void initData(final String productId) {

        showMyDialog();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(context);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {
            Log.e("当前请求：", productId);
            json.put("productId", productId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.ONEYUANJOIN)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = Message.obtain();
                msg.what = 111;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.e("我要参与返回数据：", data);
                String[] str = new String[2];
                str[0] = data;
                str[1] = productId;
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = str;
                handler.sendMessage(msg);
            }
        });
    }

    class ViewHolder {
        TextView name, totalNum, leftNum, detail, price;
        ImageView image;
        Button join;
        ProgressBar progressBar;
    }

    /**
     * 显示对话框
     */
    public void showMyDialog() {
        animDialog = new MyProgressDialog(context, "玩命加载中...", R.drawable.loading);
        animDialog.show();
        animDialog.setCancelable(true);
    }

}

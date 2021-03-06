package com.jiahehongye.robred.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.CommentSingleActivity;
import com.jiahehongye.robred.activity.FreshNewsDetailActivity;
import com.jiahehongye.robred.bean.CommentBean;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
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

/**
 * Created by huangjunhui on 2016/12/6.15:34
 */
public class NewsCommentRecycleAdapter extends BaseAdapter {
    //item类型
//    public static final int ITEM_TYPE_HEADER = 0;
//    public static final int ITEM_TYPE_CONTENT = 1;
//    public static final int ITEM_TYPE_FOOT = 2;

    private LayoutInflater layoutInflater;
    private FreshNewsDetailActivity mActivity;
    private ArrayList<CommentBean> arrayList;
    private PersistentCookieStore persistentCookieStore;
    private Call call;
    private String id;

    private MyItemClickListener mItemClickListener;

    public NewsCommentRecycleAdapter(FreshNewsDetailActivity mActivity,ArrayList<CommentBean> arrayList, LayoutInflater layoutInflater,String id) {
        this.arrayList = arrayList;
        this.layoutInflater = layoutInflater;
        this.mActivity = mActivity;
        this.id = id;
    }





    @Override
    public int getCount() {

        return arrayList.size();
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.fragment_news_detail_content, null);
            holder = new ViewHolder();
            holder.comment_tv_name = (TextView) convertView.findViewById(R.id.comment_tv_name);
            holder.comment_iv_avatar = (ImageView) convertView.findViewById(R.id.comment_iv_avatar);
            holder.comment_tv_content = (TextView) convertView.findViewById(R.id.comment_tv_content);
            holder.comment_tv_zan_number= (TextView) convertView.findViewById(R.id.comment_tv_zan_number);
            holder.comment_tv_answer = (TextView) convertView.findViewById(R.id.comment_tv_answer);
            holder.comment_tv_huifu = (TextView) convertView.findViewById(R.id.comment_tv_huifu);
            holder.pinglun_jubao = (TextView) convertView.findViewById(R.id.pinglun_jubao);
            holder.like = (ImageView) convertView.findViewById(R.id.like);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.comment_tv_name.setText(arrayList .get(position).getNickName());
        holder.comment_tv_content.setText(arrayList.get(position).getContent());
        holder.comment_tv_zan_number.setText(arrayList.get(position).getPraiseNum());
        holder.comment_tv_answer.setText(arrayList.get(position).getCreateDate());
        holder.comment_tv_huifu.setText("回复-"+arrayList.get(position).getCommentNum());
        holder.pinglun_jubao.setVisibility(View.GONE);

        holder.comment_tv_huifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UIUtils.getContext(), CommentSingleActivity.class);
                intent.putExtra("firstid",arrayList.get(position).getId());
                intent.putExtra("nickname",arrayList.get(position).getNickName());
                intent.putExtra("headimg",arrayList.get(position).getUserPhoto());
                intent.putExtra("content",arrayList.get(position).getContent());
                intent.putExtra("creatdate",arrayList.get(position).getCreateDate());
                intent.putExtra("zixunid",id);
                mActivity.startActivity(intent);
            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                persistentCookieStore = new PersistentCookieStore(UIUtils.getContext());
                CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
                builder.cookieJar(cookieJarImpl);
                OkHttpClient client = builder.build();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("commentId",arrayList.get(position).getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
                Request request = new Request.Builder()
                        .url(Constant.NEWS_LIKE)
                        .post(body)
                        .build();


                call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mActivity, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        try {
                            JSONObject object = new JSONObject(result);

                            final JSONObject data = new JSONObject(object.getString("data"));


                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (data.getString("message").equals("已点赞")){
                                            holder.like.setSelected(true);
                                        }else if (data.getString("message").equals("已取消")){
                                            holder.like.setSelected(false);
                                        }
                                        holder.comment_tv_zan_number.setText(data.getString("praiseNum"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        Glide.with(UIUtils.getContext()).load(arrayList.get(position).getUserPhoto()).transform(new GlideCircleTransform(UIUtils.getContext())).into(holder.comment_iv_avatar);
        return convertView;
    }

    class ViewHolder {
        TextView comment_tv_name;
        ImageView comment_iv_avatar;
        TextView comment_tv_content;
        TextView comment_tv_zan_number;
        TextView comment_tv_answer,comment_tv_huifu;
        TextView pinglun_jubao;
        ImageView like;
    }

}

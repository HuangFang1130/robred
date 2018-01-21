package com.jiahehongye.robred.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.Comment;
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

/**
 * Created by qianduan on 2016/12/12.
 */
public class MyReplyFragment extends Fragment {
    private View view;
    private ListView my_replylist;
    private SwipeRefreshLayout reply;
    private ArrayList<Comment> bean = new ArrayList<>();

    private static final int GET_ALL = 0000;
    private int PAGENUMBER = 1;
    private String NUMBERS = "20";
    private PersistentCookieStore persistentCookieStore;
    private Call call;

    private Adapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_ALL:
                    String s = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(s);
                        if (object.getString("result").equals("success")) {
                            String data = object.getString("data");
                            ArrayList<Comment> a = (ArrayList<Comment>) JSON.parseArray(data, Comment.class);
                            bean.addAll(a);
                            my_replylist.setAdapter(adapter);

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
        view = LayoutInflater.from(getActivity()).inflate(R.layout.reply, container, false);
        my_replylist = (ListView) view.findViewById(R.id.my_replylist);
        reply = (SwipeRefreshLayout) view.findViewById(R.id.reply);

        adapter = new Adapter();
        getdata();
        reply.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        reply.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reply.setRefreshing(false);
            }
        });

        //解决listview和swiperefreshlayout滑动冲突
        my_replylist.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (my_replylist != null && my_replylist.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = my_replylist.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = my_replylist.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                reply.setEnabled(enable);
            }
        });

        my_replylist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        //加载更多功能的代码
                        PAGENUMBER= PAGENUMBER+1;
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



    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return bean.size();
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.conment_item, null);
                holder = new ViewHolder();
                holder.my_comment_head = (ImageView) convertView.findViewById(R.id.my_comment_head);
                holder.my_comment_nickname = (TextView) convertView.findViewById(R.id.my_comment_nickname);
                holder.my_comment_content = (TextView) convertView.findViewById(R.id.my_comment_content);
                holder.my_comment_title = (TextView) convertView.findViewById(R.id.my_comment_title);
                holder.my_comment_time = (TextView) convertView.findViewById(R.id.my_comment_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.my_comment_time.setText(bean.get(position).getCreateDate());
            holder.my_comment_title.setText(bean.get(position).getTITLE());
            holder.my_comment_content.setText(bean.get(position).getContent());
            holder.my_comment_nickname.setText(bean.get(position).getNickName());
            Glide.with(UIUtils.getContext()).load(bean.get(position).getUserPhoto()).transform(new GlideCircleTransform(UIUtils.getContext())).into(holder.my_comment_head);
            return convertView;
        }

        class ViewHolder {
            ImageView my_comment_head;
            TextView my_comment_nickname;
            TextView my_comment_content;
            TextView my_comment_title;
            TextView my_comment_time;
        }
    }



    /**
     * 获取分类新闻详情
     */


    private void getdata() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "2");
            jsonObject.put("pageNumber", PAGENUMBER + "");
            jsonObject.put("pageSize", NUMBERS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.MY_COMMENT)
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
                Log.e("登陆返回：", result);

                Message msg = handler.obtainMessage();
                msg.what = GET_ALL;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });

    }
}

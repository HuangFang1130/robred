package com.jiahehongye.robred.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.jiahehongye.robred.bean.PeopleBean;
import com.jiahehongye.robred.bean.SonCommentsBean;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.GlideCircleTransform;
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

/**
 * Created by huangjunhui on 2016/12/6.17:00
 */
public class CommentSingleActivity extends BaseActivity implements View.OnClickListener {

    private String top_id;
    private ListView son_list;
    private PersistentCookieStore persistentCookieStore;
    private MyProgressDialog animDialog;
    private static final int GET_ALL = 0000;
    private static final int UP_BACK = 0001;
    private ImageView dianzan1, dianzan2, dianzan3, dianzan4;
    private ArrayList<SonCommentsBean> plarraylist = new ArrayList<>();
    private ArrayList<SonCommentsBean> fatherlist = new ArrayList<>();

    private ArrayList<PeopleBean> peopleBeen = new ArrayList<>();
    private Call call;
    private int PAGENUMBER = 1;
    private String NUMBERS = "20";
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
                            JSONObject data = new JSONObject(object.getString("data"));
                            String son = data.getString("sonComments");
                            ArrayList<SonCommentsBean> bean = (ArrayList<SonCommentsBean>) JSON.parseArray(son, SonCommentsBean.class);
                            if (bean.size() != 0) {
                                fatherlist.addAll(bean);

                                adapter.notifyDataSetChanged();
                            }
                            String people = data.getString("praisePeople");
                            peopleBeen = (ArrayList<PeopleBean>) JSON.parseArray(people, PeopleBean.class);
                            first_zan_number.setText(peopleBeen.size() + "个人赞过>");
                            if (peopleBeen.size() == 1) {
                                dianzan2.setVisibility(View.GONE);
                                dianzan3.setVisibility(View.GONE);
                                dianzan4.setVisibility(View.GONE);
                                Glide.with(UIUtils.getContext()).load(peopleBeen.get(0).getUserPhoto()).transform(new GlideCircleTransform(UIUtils.getContext())).into(dianzan1);
                            } else if (peopleBeen.size() == 2) {
                                dianzan3.setVisibility(View.GONE);
                                dianzan4.setVisibility(View.GONE);
                                Glide.with(UIUtils.getContext()).load(peopleBeen.get(0).getUserPhoto()).transform(new GlideCircleTransform(UIUtils.getContext())).into(dianzan1);
                                Glide.with(UIUtils.getContext()).load(peopleBeen.get(1).getUserPhoto()).transform(new GlideCircleTransform(UIUtils.getContext())).into(dianzan2);
                            } else if (peopleBeen.size() == 3) {
                                dianzan4.setVisibility(View.GONE);
                                Glide.with(UIUtils.getContext()).load(peopleBeen.get(0).getUserPhoto()).transform(new GlideCircleTransform(UIUtils.getContext())).into(dianzan1);
                                Glide.with(UIUtils.getContext()).load(peopleBeen.get(1).getUserPhoto()).transform(new GlideCircleTransform(UIUtils.getContext())).into(dianzan2);
                                Glide.with(UIUtils.getContext()).load(peopleBeen.get(2).getUserPhoto()).transform(new GlideCircleTransform(UIUtils.getContext())).into(dianzan3);
                            } else if (peopleBeen.size() == 4) {
                                Glide.with(UIUtils.getContext()).load(peopleBeen.get(0).getUserPhoto()).transform(new GlideCircleTransform(UIUtils.getContext())).into(dianzan1);
                                Glide.with(UIUtils.getContext()).load(peopleBeen.get(1).getUserPhoto()).transform(new GlideCircleTransform(UIUtils.getContext())).into(dianzan2);
                                Glide.with(UIUtils.getContext()).load(peopleBeen.get(2).getUserPhoto()).transform(new GlideCircleTransform(UIUtils.getContext())).into(dianzan3);
                                Glide.with(UIUtils.getContext()).load(peopleBeen.get(3).getUserPhoto()).transform(new GlideCircleTransform(UIUtils.getContext())).into(dianzan4);
                            } else {
                                dianzan1.setVisibility(View.GONE);
                                dianzan2.setVisibility(View.GONE);
                                dianzan3.setVisibility(View.GONE);
                                dianzan4.setVisibility(View.GONE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case UP_BACK:
                    String a = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(a);
                        if (object.getString("result").equals("success")) {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    private TextView first_zan_number;
    private String top_nickname;
    private String top_headimg;
    private String top_content;
    private String top_creatdate;
    private ImageView comment_iv_avatar;
    private TextView comment_tv_name, comment_tv_content, comment_tv_answer, comment_tv_huifu;
    private TextView pinglun_jubao;
    private ImageView like;
    private TextView pinglun_first_edit;
    private String top_personid;
    private String zixunid;
    private ImageView application_iv_back;
    private TextView application_tv_title;


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_comment_single);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            zixunid = bundle.getString("zixunid");
            top_id = bundle.getString("firstid");
            top_nickname = bundle.getString("nickname");
            top_headimg = bundle.getString("headimg");
            top_content = bundle.getString("content");
            top_creatdate = bundle.getString("creatdate");
            top_personid = bundle.getString("personid");
        }
        son_list = (ListView) findViewById(R.id.son_list);

        son_list.addHeaderView(LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_news_detail_content,null));
        son_list.addHeaderView(LayoutInflater.from(getApplicationContext()).inflate(R.layout.single_header,null));

        application_tv_title = (TextView) findViewById(R.id.application_tv_title);
        application_tv_title.setText("全部评论");
        application_iv_back = (ImageView) findViewById(R.id.application_iv_back);
        dianzan1 = (ImageView) findViewById(R.id.dianzan1);
        dianzan2 = (ImageView) findViewById(R.id.dianzan2);
        dianzan3 = (ImageView) findViewById(R.id.dianzan3);
        dianzan4 = (ImageView) findViewById(R.id.dianzan4);
        first_zan_number = (TextView) findViewById(R.id.first_zan_number);
        comment_tv_name = (TextView) findViewById(R.id.comment_tv_name);
        comment_iv_avatar = (ImageView) findViewById(R.id.comment_iv_avatar);
        comment_tv_content = (TextView) findViewById(R.id.comment_tv_content);
        comment_tv_answer = (TextView) findViewById(R.id.comment_tv_answer);
        comment_tv_huifu = (TextView) findViewById(R.id.comment_tv_huifu);
        pinglun_jubao = (TextView) findViewById(R.id.pinglun_jubao);
        like = (ImageView) findViewById(R.id.like);
        pinglun_first_edit = (TextView) findViewById(R.id.pinglun_first_edit);

        application_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Glide.with(UIUtils.getContext()).load(top_headimg).transform(new GlideCircleTransform(UIUtils.getContext())).into(comment_iv_avatar);
        comment_tv_name.setText(top_nickname);
        comment_tv_content.setText(top_content);
        comment_tv_answer.setText(top_creatdate);
        comment_tv_huifu.setText("举报");
        pinglun_jubao.setVisibility(View.GONE);
        RelativeLayout mRlafdljaklfj = (RelativeLayout) findViewById(R.id.rl_djaklfjd);
        mRlafdljaklfj.setOnClickListener(this);
        comment_tv_huifu.setOnClickListener(this);
        like.setOnClickListener(this);
        pinglun_first_edit.setOnClickListener(this);
        adapter = new Adapter(fatherlist, LayoutInflater.from(act));
        son_list.setAdapter(adapter);
        getdata();
        son_list.setOnScrollListener(new AbsListView.OnScrollListener() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pinglun_first_edit:
                Intent intent1 = new Intent(this, PinglunActivity.class);
                intent1.putExtra("zixunid", zixunid);
                intent1.putExtra("type", "1");
                intent1.putExtra("to_hf_id", top_id);
                intent1.putExtra("shangyiji_id", top_id);
                intent1.putExtra("shangyiji_person_id", top_personid);
                startActivityForResult(intent1,66);
                break;
            case R.id.rl_djaklfjd:
                Intent intent = new Intent(this, PraiseAllActivity.class);
                intent.putExtra("id",top_id);
                startActivity(intent);
                break;
            case R.id.comment_tv_huifu:
                //举报
                Intent intent2 = new Intent(this, ContanctReportActivty.class);
                intent2.putExtra("type", "1");
                intent2.putExtra("id", top_id);
                startActivity(intent2);
                break;

            case R.id.like:

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                persistentCookieStore = new PersistentCookieStore(getApplicationContext());
                CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
                builder.cookieJar(cookieJarImpl);
                OkHttpClient client = builder.build();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("commentId", top_id);
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
                        CommentSingleActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CommentSingleActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        try {
                            JSONObject object = new JSONObject(result);

                            final JSONObject data = new JSONObject(object.getString("data"));


                            CommentSingleActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (data.getString("message").equals("已点赞")) {
                                            like.setSelected(true);
                                        } else if (data.getString("message").equals("已取消")) {
                                            like.setSelected(false);
                                        }
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
                break;
        }
    }


    /**
     * 获取一级所有子评论
     */


    private void getdata() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(CommentSingleActivity.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", top_id);
            jsonObject.put("pageNumber", PAGENUMBER + "");
            jsonObject.put("pageSize", NUMBERS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.FIRST_SON)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                CommentSingleActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CommentSingleActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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

    private class Adapter extends BaseAdapter {

        private ArrayList<SonCommentsBean> fatherlist;
        private LayoutInflater layoutInflater;

        public Adapter(ArrayList<SonCommentsBean> fatherlist, LayoutInflater layoutInflater) {
            this.fatherlist = fatherlist;
            this.layoutInflater = layoutInflater;
        }

        @Override
        public int getCount() {
            return fatherlist.size();
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
                holder.comment_tv_zan_number = (TextView) convertView.findViewById(R.id.comment_tv_zan_number);
                holder.comment_tv_answer = (TextView) convertView.findViewById(R.id.comment_tv_answer);
                holder.comment_tv_huifu = (TextView) convertView.findViewById(R.id.comment_tv_huifu);
                holder.like = (ImageView) convertView.findViewById(R.id.like);
                holder.pinglun_jubao = (TextView) convertView.findViewById(R.id.pinglun_jubao);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (fatherlist.get(position).getCommentLevel().equals("2")) {

                holder.comment_tv_content.setText(fatherlist.get(position).getContent());
                holder.comment_tv_huifu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), PinglunActivity.class);
                        intent.putExtra("type", "3");
                        intent.putExtra("zixunid", fatherlist.get(position).getInformationId());
                        intent.putExtra("to_hf_id", fatherlist.get(position).getHeadCommentId());
                        intent.putExtra("shangyiji_id", fatherlist.get(position).getId());
                        intent.putExtra("shangyiji_person_id", fatherlist.get(position).getMemId());
                        startActivityForResult(intent,66);
                    }
                });

            } else if (fatherlist.get(position).getCommentLevel().equals("3")) {
                holder.comment_tv_content.setText(fatherlist.get(position).getContent() + "//@" + fatherlist.get(position).getUpperNickName() + ":" + fatherlist
                        .get(position).getUpperComment());
                holder.comment_tv_huifu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), PinglunActivity.class);
                        intent.putExtra("type", "3");
                        intent.putExtra("zixunid", fatherlist.get(position).getInformationId());
                        intent.putExtra("to_hf_id", fatherlist.get(position).getHeadCommentId());
                        intent.putExtra("shangyiji_id", fatherlist.get(position).getId());
                        intent.putExtra("shangyiji_person_id", fatherlist.get(position).getMemId());
                        startActivityForResult(intent,66);
                    }
                });
            }
            holder.comment_tv_name.setText(fatherlist.get(position).getNickName());
            holder.comment_tv_zan_number.setText(fatherlist.get(position).getPraiseNum());
            holder.comment_tv_answer.setText(fatherlist.get(position).getCreateDate());


            Glide.with(UIUtils.getContext()).load(fatherlist.get(position).getUserPhoto()).transform(
                    new GlideCircleTransform(UIUtils.getContext())).into(holder.comment_iv_avatar);

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
                        jsonObject.put("commentId", fatherlist.get(position).getId());
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
                            CommentSingleActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CommentSingleActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = response.body().string();
                            try {
                                JSONObject object = new JSONObject(result);

                                final JSONObject data = new JSONObject(object.getString("data"));


                                CommentSingleActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if (data.getString("message").equals("已点赞")) {
                                                holder.like.setSelected(true);
                                            } else if (data.getString("message").equals("已取消")) {
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

            holder.pinglun_jubao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent4 = new Intent(getApplicationContext(), ContanctReportActivty.class);
                    intent4.putExtra("type", "1");
                    intent4.putExtra("id", fatherlist.get(position).getId());
                    startActivity(intent4);
                }
            });

            return convertView;
        }

        class ViewHolder {
            TextView pinglun_jubao;
            TextView comment_tv_name;
            ImageView comment_iv_avatar;
            TextView comment_tv_content;
            TextView comment_tv_zan_number;
            TextView comment_tv_answer, comment_tv_huifu;
            ImageView like;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case 66:
                fatherlist.clear();
                adapter.notifyDataSetChanged();
                getdata();
                break;
        }
    }
}

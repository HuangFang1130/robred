package com.jiahehongye.robred.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.adapter.CommentAllRecycleAdapter;
import com.jiahehongye.robred.bean.CommentBean;
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

/**
 * Created by huangjunhui on 2016/12/6.16:40
 * <p>
 * 所有评论
 */
public class CommentAllActivity extends BaseActivity {
    private ArrayList<CommentBean> fatherArrayList = new ArrayList<>();
    private RecyclerView mCommentAllRecycleView;
    private LinearLayoutManager linearLayoutManager;
    private CommentAllRecycleAdapter commentAllRecycleAdapter;
    public static String id;
    private static final int GET_ALL = 0000;
    private static final int UP_BACK = 0001;
    private PersistentCookieStore persistentCookieStore;
    private MyProgressDialog animDialog;
    private Call call;


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
                            ArrayList<CommentBean> arrayList = (ArrayList<CommentBean>) JSON.parseArray(data, CommentBean.class);
                            fatherArrayList.addAll(arrayList);
                            commentAllRecycleAdapter.notifyDataSetChanged();
                            if (commentAllRecycleAdapter!=null){
                                commentAllRecycleAdapter.hintFootView();
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
                            String data = object.getString("data");
                            if (data.equals("评论成功")) {
                                pinglun_first_edit.setText("");
                                hideSoftKeyboard();
                                Toast.makeText(CommentAllActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                                PAGENUMBER = 1;
                                fatherArrayList.clear();
                                getdata();
                            } else {
                                Toast.makeText(CommentAllActivity.this, "评论失败，稍后再试", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    private String NUMBERS = "20";
    private int PAGENUMBER = 1;
    private TextView pinglun_first;
    private EditText pinglun_first_edit;
    private ImageView application_iv_back;
    private TextView application_tv_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_comment_all_content);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("informationId");
        }
        application_tv_title = (TextView) findViewById(R.id.application_tv_title);
        application_tv_title.setText("全部评论");
        application_iv_back = (ImageView) findViewById(R.id.application_iv_back);
        commentAllRecycleAdapter = new CommentAllRecycleAdapter(CommentAllActivity.this, fatherArrayList);
        pinglun_first = (TextView) findViewById(R.id.pinglun_first);
        pinglun_first_edit = (EditText) findViewById(R.id.pinglun_first_edit);

        mCommentAllRecycleView = (RecyclerView) findViewById(R.id.comment_all_recycleview);
        linearLayoutManager = new LinearLayoutManager(this);
        mCommentAllRecycleView.setLayoutManager(linearLayoutManager);
        mCommentAllRecycleView.setItemAnimator(new DefaultItemAnimator());

        //滑动的监听
        mCommentAllRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastVisiblePosition >= linearLayoutManager.getItemCount() - 1) {
                        if(commentAllRecycleAdapter!=null)commentAllRecycleAdapter.showFootView();
                        PAGENUMBER = PAGENUMBER + 1;
                        getdata();
                    }
                }
            }
        });

        application_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getdata();
        mCommentAllRecycleView.setAdapter(commentAllRecycleAdapter);

        pinglun_first.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 if (TextUtils.isEmpty(pinglun_first_edit.getText())) {
                                                     Toast.makeText(CommentAllActivity.this, "请输入评论内容", Toast.LENGTH_SHORT).show();
                                                     return;
                                                 }
                                                 UpData();
                                             }

                                             private void UpData() {
                                                 OkHttpClient.Builder builder = new OkHttpClient.Builder();
                                                 persistentCookieStore = new PersistentCookieStore(CommentAllActivity.this);
                                                 CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
                                                 builder.cookieJar(cookieJarImpl);
                                                 OkHttpClient client = builder.build();

                                                 JSONObject jsonObject = new JSONObject();
                                                 try {
                                                     jsonObject.put("informationId", id);
                                                     jsonObject.put("content", pinglun_first_edit.getText().toString());
                                                     jsonObject.put("commentLevel", "1");
                                                 } catch (JSONException e) {
                                                     e.printStackTrace();
                                                 }

                                                 RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
                                                 Request request = new Request.Builder()
                                                         .url(Constant.UP_COMMENT)
                                                         .post(body)
                                                         .build();


                                                 call = client.newCall(request);
                                                 call.enqueue(new Callback() {
                                                     @Override
                                                     public void onFailure(Call call, IOException e) {
                                                         CommentAllActivity.this.runOnUiThread(new Runnable() {
                                                             @Override
                                                             public void run() {
                                                                 Toast.makeText(CommentAllActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                                             }
                                                         });
                                                     }

                                                     @Override
                                                     public void onResponse(Call call, Response response) throws IOException {
                                                         String result = response.body().string();

                                                         Message msg = handler.obtainMessage();
                                                         msg.what = UP_BACK;
                                                         msg.obj = result;
                                                         handler.sendMessage(msg);
                                                     }
                                                 });
                                             }
                                         }
        );
    }

    /**
     * 获取分类新闻详情
     */


    private void getdata() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(CommentAllActivity.this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("pageNumber", PAGENUMBER + "");
            jsonObject.put("pageSize", NUMBERS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.ALL_FIEST_COMMENT)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                CommentAllActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CommentAllActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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

package com.jiahehongye.robred.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.adapter.NewsCommentRecycleAdapter;
import com.jiahehongye.robred.bean.CommentBean;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.FullyLinearLayoutManager;
import com.jiahehongye.robred.view.MyProgressDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

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
 * Created by huangjunhui on 2016/12/6.15:19
 * <p/>
 * 新闻详情页
 */
public class FreshNewsDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private ListView mCommentRecycleView;
    private FullyLinearLayoutManager linearLayoutManager;
    private ArrayList<CommentBean> fatherArrayList;
    private ArrayList<CommentBean> arrayList = new ArrayList<>();
    private NewsCommentRecycleAdapter newsCommentRecycleAdapter;
    private WebView freshnews_detail_webview;
    private String id, title;
    private TextView comment_all_number;
    private PersistentCookieStore persistentCookieStore;
    private MyProgressDialog animDialog;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_LOGS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SET_DEBUG_APP,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.WRITE_APN_SETTINGS

    };
    private Call call;

    private static final int GET_ALL = 0000;
    private static final int UP_BACK = 0001;
    private String content;
    private String commentNum;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_ALL:
                    animDialog.dismiss();
                    String s = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(s);
                        if (object.getString("result").equals("success")) {
                            JSONObject data = new JSONObject(object.getString("data"));
                            commentNum = data.getString("commentNum");
                            comment_all_number.setText(commentNum);
                            content = data.getString("content");
                            if (flag.equals("1")) {

                                freshnews_detail_webview.loadUrl(content);
                            } else {
                                freshnews_detail_webview.loadDataWithBaseURL("about:blank", content, "text/html", "utf-8", null);
                            }
                            String commentL = data.getString("commentL");
                            arrayList = (ArrayList<CommentBean>) JSON.parseArray(commentL, CommentBean.class);
                            newsCommentRecycleAdapter = new NewsCommentRecycleAdapter(FreshNewsDetailActivity.this, arrayList, LayoutInflater.from(act), id);

                            mCommentRecycleView.setAdapter(newsCommentRecycleAdapter);
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
                                int num = Integer.parseInt(commentNum);
                                num = num+1;

                                comment_all_number.setText(num+"");
                                pinglun_first_edit.setText("");
                                hideSoftKeyboard();
                                Toast.makeText(FreshNewsDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    private TextView pinglun_first;
    private EditText pinglun_first_edit;
    private RelativeLayout mBack, mShare;
    private String flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
//        mTintManager.setStatusBarTintResource(R.color.home_white_color);
        setContentView(R.layout.activity_fresh_news_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
            title = bundle.getString("title");
            flag = bundle.getString("flag");
//            Toast.makeText(FreshNewsDetailActivity.this, id, Toast.LENGTH_SHORT).show();
        }

        mBack = (RelativeLayout) findViewById(R.id.newsdetail_rl_back);
        mShare = (RelativeLayout) findViewById(R.id.newsdetail_rl_share);
        mBack.setOnClickListener(this);
        mShare.setOnClickListener(this);

        pinglun_first = (TextView) findViewById(R.id.pinglun_first);
        pinglun_first_edit = (EditText) findViewById(R.id.pinglun_first_edit);
        mCommentRecycleView = (ListView) findViewById(R.id.lv_deital);
        comment_all_number = (TextView) findViewById(R.id.comment_all_number);
        freshnews_detail_webview = new WebView(act);
        RelativeLayout mRlComment = (RelativeLayout) findViewById(R.id.comment_rl_dd);
        getdata();
        mRlComment.setOnClickListener(this);
        mCommentRecycleView.addHeaderView(freshnews_detail_webview);

        pinglun_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((boolean) SPUtils.get(UIUtils.getContext(), Constant.IS_LOGIN, false)) {
                    UpData();
                }else {
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_EXTERNAL_STORAGE:
                try {

                }catch (RuntimeException e){

                }
                break;
        }
    }

    /**
     * 提交一级评论
     */
    private void UpData() {
        if (TextUtils.isEmpty(pinglun_first_edit.getText())) {
            Toast.makeText(FreshNewsDetailActivity.this, "请输入评论内容", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(FreshNewsDetailActivity.this);
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
                FreshNewsDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FreshNewsDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment_rl_dd:
                if ((boolean) SPUtils.get(UIUtils.getContext(), Constant.IS_LOGIN, false)) {

                    Intent intent = new Intent(this, CommentAllActivity.class);
                    intent.putExtra("informationId", id);
                    startActivity(intent);
                }else {
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }
                break;
            case R.id.newsdetail_rl_back:
                finish();
                break;
            case R.id.newsdetail_rl_share:
                //6.0权限
                int permission = ActivityCompat.checkSelfPermission(FreshNewsDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    Toast.makeText(FreshNewsDetailActivity.this, "分享失败,请尝试打开读取文件权限", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(
                            FreshNewsDetailActivity.this,
                            PERMISSIONS_STORAGE,//需要请求的所有权限，这是个数组String[]
                            REQUEST_EXTERNAL_STORAGE//请求码
                    );
                }
                // 用来弹出错误日志
                com.umeng.socialize.utils.Log.LOG = true;
                if (flag.equals("0")){
                    UMImage image = new UMImage(this, R.drawable.logo);//设置分享图片
                    UMImage thumb = new UMImage(this, R.drawable.logo);//设置缩略图
                    image.compressStyle = UMImage.CompressStyle.SCALE;
                    image.setThumb(thumb);
                    ShareAction mAction = new ShareAction(this)
                            .withTitle("金猴宝红包")
                            .withText(title)
                            .withTargetUrl(Constant.HOLD_NEWS + id)
                            .withMedia(image)
                            .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                            .setCallback(umShareListener);
                    mAction.open();
                }else if (flag.equals("1")){
                    UMImage image = new UMImage(this, R.drawable.logo);//设置分享图片
                    UMImage thumb = new UMImage(this, R.drawable.logo);//设置缩略图
                    image.compressStyle = UMImage.CompressStyle.SCALE;
                    image.setThumb(thumb);
                    ShareAction mAction = new ShareAction(this)
                            .withTitle("金猴宝红包")
                            .withText(title)
                            .withTargetUrl(content)
                            .withMedia(image)
                            .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                            .setCallback(umShareListener);
                    mAction.open();
                }



                break;
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            Toast.makeText(FreshNewsDetailActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(FreshNewsDetailActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(getActivity(),"分享取消", Toast.LENGTH_SHORT).show();
        }
    };


    /**
     * 获取新闻详情
     */

    private void getdata() {

        showMyDialog();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getApplicationContext());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.NEWS_DETAIL)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                FreshNewsDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        animDialog.dismiss();

                        Toast.makeText(FreshNewsDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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
    /**
     * 显示对话框
     */
    public void showMyDialog() {
        animDialog = new MyProgressDialog(FreshNewsDetailActivity.this, "玩命加载中...", R.drawable.loading);
        animDialog.show();
        animDialog.setCancelable(true);
        animDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (call.isExecuted()) {
                    call.cancel();
                }
            }
        });
    }
}

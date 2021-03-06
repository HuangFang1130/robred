package com.jiahehongye.robred.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.adapter.DFTTVedioDetailRecycleAdapter;
import com.jiahehongye.robred.bean.DFTTVideoDetailResult;
import com.jiahehongye.robred.bean.DFTTVideoResult;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.jiecao.JCVideoPlayer;
import com.jiahehongye.robred.utils.LogUtil;
import com.jiahehongye.robred.view.MyProgressDialog;
import com.songheng.newsapisdk.sdk.apiservice.DFTTVideoApiService;
import com.songheng.newsapisdk.sdk.apiservice.listener.DfttApiServiceCallBack;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by huangjunhui on 2017/5/23.13:13
 */
public class DFTTVedioDetailActivity extends BaseActivity implements View.OnClickListener {
    private String type;
    private String title;
    private static final int GET_LIST = 400;
    private String url;
    private RecyclerView mRecycleView;
    private MyProgressDialog animDialog;
    private ArrayList<DFTTVideoResult.DataBean> fatherListDate;
    private LinearLayoutManager linearLayoutManager;
    private DFTTVedioDetailRecycleAdapter mHomeRecycleAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {


                case GET_LIST:
                    if(animDialog.isShowing()){
                        animDialog.dismiss();
                    }
                    String result = (String) msg.obj;
                    DFTTVideoDetailResult dfttNewsResult = new Gson().fromJson(result, DFTTVideoDetailResult.class);
                    List<DFTTVideoResult.DataBean> data = dfttNewsResult.getData();

                    fatherListDate.addAll(data);
                    mHomeRecycleAdapter.notifyDataSetChanged();
                    break;

//                case GET_ADV:
//                    if (animDialog.isShowing()) {
//                        animDialog.dismiss();
//                    }
//                    if (mHomeHeadSwiperefresh != null) {
//                        if (mHomeHeadSwiperefresh.isRefreshing()) {
//                            mHomeHeadSwiperefresh.setRefreshing(false);
//                        }
//                    }
//                    String result3 = (String) msg.obj;
//                    DFTTAdvResult dfttAdvResult = new Gson().fromJson(result3, DFTTAdvResult.class);
//                    List<DFTTNewsResult.DataBean> data1 = dfttAdvResult.getData();
//
//
//                    /**
//                     * 遍历最外面的。然后在第几个位置停下来
//                     *  取第一个位置的数据放进来
//                     */
//                    for (int i = 0; i < data.size(); i++) {//20
//                        if (i == 3 || i == 8 || i == 13 || i == 18) {
//                            if(data1.size()==0){
//                                break;
//                            }
//                            DFTTNewsResult.DataBean dataBean = data1.get(0);//4个
////                            dataBean.setIsadv("1");//是否是广告
//                            if (i == 3 || i == 13) {//大图（1张）   小图（3张的）
////                                DFTTNewsResult.DataBean dataBean = data1.get(0);
//                                dataBean.setBigpic("1");
//                            }
//                            data.remove(i);//19
//                            data.add(i, data1.get(0));
//                            data1.remove(0);//移除掉
//                            continue;
//                        }
//
//                    }
//                    fatherListDate.addAll(data);
//                    mHomeRecycleAdapter.notifyDataSetChanged();
//                    break;
            }
        }

    };
    private String playurl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dfttvedio);

        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        playurl = getIntent().getStringExtra("playurl");
        ImageView mIvBack = (ImageView) findViewById(R.id.application_iv_back);
        TextView mTvTitle = (TextView) findViewById(R.id.application_tv_title);
        mRecycleView = (RecyclerView) findViewById(R.id.dfttvedio_recycleview);

        mIvBack.setOnClickListener(this);
        mTvTitle.setText(title);


        fatherListDate = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(linearLayoutManager);

        mHomeRecycleAdapter = new DFTTVedioDetailRecycleAdapter(this, fatherListDate,url,playurl);
        mRecycleView.setAdapter(mHomeRecycleAdapter);


        //false： 没有登录， true : 登录了
        mHomeRecycleAdapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent = new Intent(DFTTVedioDetailActivity.this, DFTTVedioDetailActivity.class);

                intent.putExtra("url", fatherListDate.get(postion).getUrl());
                intent.putExtra("playurl", fatherListDate.get(postion).getVideo_link());//视频地址
                intent.putExtra("title", fatherListDate.get(postion).getTopic());
                intent.putExtra("type",type);
                startActivity(intent);
                finish();
            }
        });


        showMyDialog();
        //请求数据

        DFTTVideoApiService.getVideoRelativeList(type, url, new DfttApiServiceCallBack() {
            @Override
            public void onSuccess(String s) {
                LogUtil.LogShitou("视频详情页的推荐视频：",s.toString());


                Message msg = handler.obtainMessage();
                msg.what = GET_LIST;
                msg.obj = s;
                handler.sendMessage(msg);

            }

            @Override
            public void onError(String s, String s1, Response response, Exception e) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JCVideoPlayer.releaseAllVideos();

    }

    /**
     * 显示对话框
     */
    public void showMyDialog() {
        animDialog = new MyProgressDialog(this, "玩命加载中...", R.drawable.loading);
        animDialog.show();
        animDialog.setCancelable(true);
        animDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
//                if (call.isExecuted()) {
//                    call.cancel();
//                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.application_iv_back:
                finish();
                break;
        }
    }
}

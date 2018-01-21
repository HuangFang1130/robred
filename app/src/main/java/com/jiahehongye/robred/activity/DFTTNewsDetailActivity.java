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
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.adapter.DFTTDetailRecycleAdapter;
import com.jiahehongye.robred.bean.DFTTAdvResult;
import com.jiahehongye.robred.bean.DFTTDetailResult;
import com.jiahehongye.robred.bean.DFTTNewsResult;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.jiecao.JCVideoPlayer;
import com.jiahehongye.robred.utils.LogUtil;
import com.jiahehongye.robred.view.MyProgressDialog;
import com.songheng.newsapisdk.sdk.apiservice.DFTTAdsApiService;
import com.songheng.newsapisdk.sdk.apiservice.DFTTNewsApiService;
import com.songheng.newsapisdk.sdk.apiservice.DFTTStatisticsApiService;
import com.songheng.newsapisdk.sdk.apiservice.listener.DfttApiServiceCallBack;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by huangjunhui on 2017/5/18.16:01
 */
public class DFTTNewsDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final int GET_LIST = 500;
    private static final int GET_ADV_DETAIL = 600;

    //    private WebView mDFTTWebView;
    private MyProgressDialog animDialog;
    private RecyclerView mRecycleView;
    private String type;
    private String title;
    private String url;
    private ArrayList<DFTTNewsResult.DataBean> fatherListDate;
    private LinearLayoutManager linearLayoutManager;
    private DFTTDetailRecycleAdapter mHomeRecycleAdapter;
    //        WebSettings settings = mDFTTWebView.getSettings();
//        settings.setJavaScriptEnabled(true);// 设置支持javascript脚本
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        settings.setSupportZoom(true); // 支持缩放
//        settings.setBuiltInZoomControls(true);// 设置显示缩放按钮
//        settings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
//        settings.setLoadWithOverviewMode(true);
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {


                case GET_LIST:
                    if (animDialog.isShowing()) {
                        animDialog.dismiss();
                    }
                    String result = (String) msg.obj;
                    DFTTDetailResult dfttNewsResult = new Gson().fromJson(result, DFTTDetailResult.class);
                    data = dfttNewsResult.getData();
                    fatherListDate.addAll(data);
                    if (mHomeRecycleAdapter != null) {
                        mHomeRecycleAdapter.notifyDataSetChanged();
                    }

                    break;

                case GET_ADV_DETAIL:
                    if (animDialog.isShowing()) {
                        animDialog.dismiss();
                    }

                    String result3 = (String) msg.obj;
                    DFTTAdvResult dfttAdvResult = new Gson().fromJson(result3, DFTTAdvResult.class);
                    data1 = dfttAdvResult.getData();


                    mHomeRecycleAdapter = new DFTTDetailRecycleAdapter(DFTTNewsDetailActivity.this, fatherListDate, url, isVideo, videolink, resource, title, img, data1);
                    mRecycleView.setAdapter(mHomeRecycleAdapter);

                    //false： 没有登录， true : 登录了
                    mHomeRecycleAdapter.setOnItemClickListener(new MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, int postion) {
                            int itemViewType = mHomeRecycleAdapter.getItemViewType(postion + 1);
                            if (itemViewType == mHomeRecycleAdapter.ITEM_TYPE_BIG_ADV ||
                                    itemViewType == mHomeRecycleAdapter.ITEM_TYPE_ADV) {
                                Intent intent = new Intent(DFTTNewsDetailActivity.this, DFTTAdvActivity.class);
                                intent.putExtra("url", data1.get(0).getUrl());
                                LogUtil.LogShitou("url:", data1.get(0).getUrl().toString());
                                intent.putExtra("isdownload", data1.get(0).getIsdownload() + "");
                                intent.putExtra("isdsp", data1.get(0).getIsdsp());
                                intent.putExtra("reporturl", data1.get(0).getReporturl());
                                intent.putStringArrayListExtra("clickrep", (ArrayList<String>) data1.get(0).getClickrep());
                                intent.putStringArrayListExtra("showrep", (ArrayList<String>) data1.get(0).getShowrep());
                                intent.putExtra("position", data1.get(0).getIdx() + "");
                                intent.putExtra("title", data1.get(0).getTopic());
                                intent.putExtra("type", type);
                                intent.putExtra("id", data1.get(0).getAdv_id());
                                startActivity(intent);
                                return;
                            }
                            if (itemViewType == mHomeRecycleAdapter.ITEM_TYPE_CONTENT ||
                                    itemViewType == mHomeRecycleAdapter.ITEM_TYPE_CONTENT_THREE) {
                                int i = 0;
                                if (data1 == null) {
                                    i = postion;
                                }else {
                                    if(data1.size()<0){
                                        i = postion;
                                    }else {
                                        i = postion-1;
                                    }
                                }

                                Intent intent = new Intent(DFTTNewsDetailActivity.this, DFTTNewsDetailActivity.class);
                                intent.putExtra("url", fatherListDate.get(i).getUrl());
                                intent.putExtra("issptopic", fatherListDate.get(i).getIssptopic());
                                intent.putExtra("title", fatherListDate.get(i).getTopic());
                                intent.putExtra("isVideo", fatherListDate.get(i).getIsvideo());//1 是   0  不是
                                intent.putExtra("videolink", fatherListDate.get(i).getVideo_link());
                                intent.putExtra("resource", fatherListDate.get(i).getSource());
                                intent.putExtra("img", fatherListDate.get(i).getMiniimg().get(0).getSrc());
                                intent.putExtra("type", type);
                                intent.putExtra("page", "1");
                                intent.putExtra("postion", i);
                                intent.putExtra("ishot", fatherListDate.get(i).getHotnews());
                                intent.putExtra("recomdType", fatherListDate.get(i).getRecommendtype());
                                intent.putExtra("recomdurl", fatherListDate.get(i).getReporturl());
                                intent.putExtra("supertop", fatherListDate.get(i).getSuptop());
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

//                    if(data1!=null ){
//                        if(data1.size()>1){
//                            mHomeRecycleAdapter.setAdvList(data1);
//                            mRecycleView.setAdapter(mHomeRecycleAdapter);
//                            mHomeRecycleAdapter.notifyDataSetChanged();
//                        }
//                    }


//
//                    /**
//                     * 遍历最外面的。然后在第几个位置停下来
//                     *  取第一个位置的数据放进来
//                     */
//                    for (int i = 0; i < data.size(); i++) {//20
//                        if (i == 3 || i == 8) {
//                            if(data1.size()==0){
//                                break;
//                            }
//                            DFTTNewsResult.DataBean dataBean = data1.get(0);//4个
////                            dataBean.setIsadv("1");//是否是广告
//                            if (i == 3) {//大图（1张）   小图（3张的）
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


                    break;
            }
        }

    };
    private List<DFTTNewsResult.DataBean> data;
    private String isVideo;
    private String videolink;
    private String resource;
    private String img;
    private ImageView mIvShare;
    private String issptopic;
    private List<DFTTNewsResult.DataBean> data1;
    private String page;
    private String postion;
    private String ishot;
    private String recomdType;
    private String recomdurl;
    private String supertop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*intent.putExtra("ishot",fatherListDate.get(postion-1).getHotnews());
        intent.putExtra("recomdType",fatherListDate.get(postion-1).getRecommendtype());
        intent.putExtra("recomdurl",fatherListDate.get(postion-1).getReporturl());
        intent.putExtra("supertop",fatherListDate.get(postion-1).getSuptop());*/


        ishot = getIntent().getStringExtra("ishot");
        recomdType = getIntent().getStringExtra("recomdType");
        recomdurl = getIntent().getStringExtra("recomdurl");
        supertop = getIntent().getStringExtra("supertop");
        url = getIntent().getStringExtra("url");
        page = getIntent().getStringExtra("page");
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        isVideo = getIntent().getStringExtra("isVideo");
        videolink = getIntent().getStringExtra("videolink");
        resource = getIntent().getStringExtra("resource");
        img = getIntent().getStringExtra("img");
        postion = getIntent().getStringExtra("postion");
//        intent.putExtra("issptopic", fatherListDate.get(postion).getIssptopic());

        issptopic = getIntent().getStringExtra("issptopic");


        setContentView(R.layout.activity_dfttnewsdetail);
        ImageView mIvBack = (ImageView) findViewById(R.id.application_iv_back);
        TextView mTvTitle = (TextView) findViewById(R.id.application_tv_title);
        mRecycleView = (RecyclerView) findViewById(R.id.detail_recycleview);

        mIvShare = (ImageView) findViewById(R.id.share_iv);
        mIvShare.setOnClickListener(this);


        if ("0".equals(issptopic)) {
            if ("0".equals(isVideo)) {
                mIvShare.setVisibility(View.VISIBLE);
            } else {
                mIvShare.setVisibility(View.GONE);
            }
        } else {
            mIvShare.setVisibility(View.GONE);
        }
        mIvBack.setOnClickListener(this);
        mTvTitle.setText(title);


        fatherListDate = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(linearLayoutManager);
        showMyDialog();
        requestAdv();

        //请求数据
        DFTTNewsApiService.getHotNews(type, url, new DfttApiServiceCallBack() {
            @Override
            public void onSuccess(String s) {
                LogUtil.LogShitou("新闻详情页的推荐新闻：", s.toString());
                Message msg = handler.obtainMessage();
                msg.what = GET_LIST;
                msg.obj = s;
                handler.sendMessage(msg);

            }

            @Override
            public void onError(String s, String s1, Response response, Exception e) {

            }
        });

        //进入新闻详情页的回调接口
        //String from, String newsType, String to, String pgNum, String idx, String isHot, String recommendType, String recommendUrl, String isPush, String supTop,DfttApiServiceCallBackcallBack
        //
        //
        //
        DFTTStatisticsApiService.getPageArrival(url, type, url, page, postion, ishot, recomdType, recomdurl, "0", supertop, new DfttApiServiceCallBack() {
            @Override
            public void onSuccess(String s) {
                LogUtil.LogShitou("新闻详情页上报success", s.toString());
            }

            @Override
            public void onError(String s, String s1, Response response, Exception e) {
                ResponseBody body = response.body();
                try {
                    String string = body.string();
                    LogUtil.LogShitou("新闻详情页上报error：", string);

                } catch (IOException e1) {
                }


            }
        });


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
        switch (v.getId()) {
            case R.id.application_iv_back:
                finish();
                break;
            case R.id.share_iv:
                share();
                break;
        }
    }

    private void share() {

        String shareUrl = Constant.URL + "/mobile/infor/shareInfor.jhtml?url=" + url;
        UMImage image = new UMImage(DFTTNewsDetailActivity.this, R.drawable.logo);//设置分享图片
        UMImage thumb = new UMImage(DFTTNewsDetailActivity.this, R.drawable.logo);//设置缩略图
        image.compressStyle = UMImage.CompressStyle.SCALE;
        image.setThumb(thumb);
        image.compressStyle = UMImage.CompressStyle.SCALE;
        image.setThumb(thumb);
        ShareAction mAction = new ShareAction(DFTTNewsDetailActivity.this)
                .withTitle(title)
                .withTargetUrl(shareUrl)
                .withText("金猴宝，看新闻，看世界！")
                .withMedia(image)
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(umShareListener);
        mAction.open();
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

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(DFTTNewsDetailActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(DFTTNewsDetailActivity.this, "分享失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };

    public void requestAdv() {
        DFTTAdsApiService.getAdvertisement("list", type, null, 1 + "", new DfttApiServiceCallBack() {
            @Override
            public void onSuccess(String s) {
                LogUtil.LogShitou("广告：", s.toString());
                Message msg = handler.obtainMessage();
                msg.what = GET_ADV_DETAIL;
                msg.obj = s;
                handler.sendMessage(msg);

            }

            @Override
            public void onError(String s, String s1, Response response, Exception e) {

            }
        });
    }


}

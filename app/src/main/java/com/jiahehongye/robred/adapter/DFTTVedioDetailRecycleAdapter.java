package com.jiahehongye.robred.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.DFTTVedioDetailActivity;
import com.jiahehongye.robred.bean.DFTTVideoResult;
import com.jiahehongye.robred.interfaces.MyHeadViewClickListener;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.DensityUtil;
import com.jiahehongye.robred.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by huangjunhui on 2017/5/23.13:27
 */
public class DFTTVedioDetailRecycleAdapter extends BaseRecycleViewAdapter {


    private String playurl;
    private String url;
    private DFTTVedioDetailActivity mMainUi;
    private ArrayList<DFTTVideoResult.DataBean> fatherArraylist;
    public DFTTVedioDetailRecycleAdapter(DFTTVedioDetailActivity dfttVedioDetailActivity, ArrayList<DFTTVideoResult.DataBean> fatherListDate, String url, String playUrl) {
        this.mMainUi = dfttVedioDetailActivity;
        this.fatherArraylist = fatherListDate;
        this.url = url;
        this.playurl = playUrl;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_HEADER:
//                View headView = LayoutInflater.from(mMainUi).inflate(R.layout.activity_vedio, null);
//                return new HomeHeadViewHolder2(headView, mHeadClickListener);

                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(UIUtils.getContext(),200));
                WebView headView = new WebView(parent.getContext());
                headView.setLayoutParams(lp);
                return new HomeHeadViewHolder(headView, mHeadClickListener);

            case ITEM_TYPE_CONTENT://1图片de
                View contentViewOne = LayoutInflater.from(mMainUi).inflate(R.layout.fragment_fresh_news_content_type_one, null);
                return new HomeSingleContentViewHolder(contentViewOne, mItemClickListener);

        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HomeSingleContentViewHolder) {
            onBindContentViewHolder((HomeSingleContentViewHolder) holder, fatherArraylist.get(position - 1));
        }
    }

    private void onBindContentViewHolder(HomeSingleContentViewHolder holder, DFTTVideoResult.DataBean newsBean) {
        holder.title.setText(newsBean.getTopic());
        Glide.with(UIUtils.getContext()).load(newsBean.getMiniimg().get(0).getSrc())
                .error(R.mipmap.default_img)
                .placeholder(R.mipmap.default_img)
                .into(holder.imageView);

        if (newsBean.getIsvideo().equals("1")) {
            holder.video.setVisibility(View.VISIBLE);
        } else {
            holder.video.setVisibility(View.GONE);
        }
//        holder.des.setText(newsBean.getSource()+"   "+newsBean.getComment_count()+"   评论"+newsBean.getDate());
        holder.des.setText(newsBean.getSource());

//        holder.title.setText(newsBean.getTitle());
//        Glide.with(UIUtils.getContext()).load(newsBean.getImgUrl()).asBitmap().into(holder.imageView);
//        holder.des.setText(newsBean.getContentSource()+"   评论"+newsBean.getCommentNum()+"   "+newsBean.getCreateDate());


    }


    @Override
    public int getItemCount() {
        return fatherArraylist.size() + 1;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        } else {
            return ITEM_TYPE_CONTENT;
        }

    }


    private class HomeHeadViewHolder extends RecyclerView.ViewHolder {


        public HomeHeadViewHolder(WebView mDFTTWebView, MyHeadViewClickListener mHeadClickListener) {
            super(mDFTTWebView);
//            mDFTTWebView = (WebView) headView.findViewById(R.id.dftt_webview);
            mDFTTWebView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
//                    showMyDialog();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

//                    animDialog.dismiss();

                }
            });

            mDFTTWebView.loadUrl(playurl);

        }
    }

    /**
     * 1图的
     */
    private class HomeSingleContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyItemClickListener mItemClickListener;
        private TextView title;
        private TextView des;
        private ImageView imageView;
        private TextView zhiding;
        private ImageView video;

        public HomeSingleContentViewHolder(View contentView, MyItemClickListener mItemClickListener) {
            super(contentView);
            this.mItemClickListener = mItemClickListener;
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.freshnews_content_tv_title);
            imageView = (ImageView) itemView.findViewById(R.id.freshnews_content_one_iv_des);
            des = (TextView) itemView.findViewById(R.id.des);
            zhiding = (TextView) itemView.findViewById(R.id.zhiding);
            video = (ImageView) itemView.findViewById(R.id.iv_video);
            video.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition() - 1);
            }
        }
    }

    private class HomeHeadViewHolder2 extends RecyclerView.ViewHolder {

        private final VideoView mVideoView;

        public HomeHeadViewHolder2(View headView, MyHeadViewClickListener mHeadClickListener) {
            super(
                    headView
            );

            mVideoView = (VideoView) headView.findViewById(R.id.videoview);

            // 播放在线视频
            Uri mVideoUri = Uri.parse(playurl);
            mVideoView.setVideoPath(mVideoUri.toString());

            mVideoView.start();
            mVideoView.requestFocus();


        }
    }
}

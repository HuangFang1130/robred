package com.jiahehongye.robred.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiahehongye.robred.MainActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.DFTTVideoResult;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.jiecao.JCVideoPlayer;
import com.jiahehongye.robred.jiecao.JCVideoPlayerStandard;
import com.jiahehongye.robred.utils.UIUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



/**
 * Created by huangjunhui on 2017/5/22.10:19
 */
public class VedioChildFragmentRecycleAdapter extends BaseRecycleViewAdapter {
    private MainActivity mMainUi;
    private ArrayList<DFTTVideoResult.DataBean> fatherArraylist;
    private View footView;

    public VedioChildFragmentRecycleAdapter(MainActivity mMainUi, ArrayList<DFTTVideoResult.DataBean> fatherListDate) {
        this.mMainUi = mMainUi;
        this.fatherArraylist = fatherListDate;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_CONTENT://1图片de
                View contentViewOne = LayoutInflater.from(mMainUi).inflate(R.layout.fragment_vedio_content_type_one, null);
                return new HomeSingleContentViewHolder(contentViewOne, mItemClickListener); //1图片de
            case ITEM_TYPE_CONTENT_TWO:
                View contentViewTwo = LayoutInflater.from(mMainUi).inflate(R.layout.fragment_vedio_content_type_two, null);
                return new HomeSingleContentTwoViewHolder(contentViewTwo, mItemClickListener);

            case ITEM_TYPE_FOOT:
                footView = LayoutInflater.from(mMainUi).inflate(R.layout.fragment_home_foot, null);
                hintFootView();
                return new HomeFootViewHolder(footView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)){
            case ITEM_TYPE_CONTENT :
                onBindContentViewHolder((HomeSingleContentViewHolder) holder,fatherArraylist.get(position));
                break;
            case ITEM_TYPE_CONTENT_TWO:
                HomeSingleContentTwoViewHolder holder1 = (HomeSingleContentTwoViewHolder) holder;

                holder1.jcVideoPlayerStandard.setUp(
                        fatherArraylist.get(position).getVideo_link(), JCVideoPlayer.SCREEN_LAYOUT_LIST,
                        fatherArraylist.get(position).getTopic());
                holder1.mTvTitle.setText(fatherArraylist.get(position).getSource());

                Picasso.with(holder1.jcVideoPlayerStandard.getContext())
                        .load(fatherArraylist.get(position).getMiniimg().get(0).getSrc())
                        .into(holder1.jcVideoPlayerStandard.thumbImageView);
                break;
        }
    }


    private void onBindContentViewHolder(HomeSingleContentViewHolder holder, DFTTVideoResult.DataBean newsBean){
        Glide.with(UIUtils.getContext()).load(newsBean.getMiniimg().get(0).getSrc())
                .error(R.mipmap.default_img)
                .placeholder(R.mipmap.default_img)
                .into(holder.imageView);

        holder.des.setText(newsBean.getSource());



    }
    class HomeFootViewHolder extends RecyclerView.ViewHolder {

        public HomeFootViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void showFootView() {
        if (footView != null) {
            footView.setVisibility(View.VISIBLE);
        }
    }

    public void hintFootView() {
        if (footView != null) {
            footView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return fatherArraylist.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == fatherArraylist.size()) {
            return ITEM_TYPE_FOOT;
        }
        return ITEM_TYPE_CONTENT_TWO;

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
        private final ImageView video;

        public HomeSingleContentViewHolder(View contentView, MyItemClickListener mItemClickListener) {
            super(contentView);
            this.mItemClickListener = mItemClickListener;
            itemView.setOnClickListener(this);
//            title = (TextView) itemView.findViewById(R.id.freshnews_content_tv_title);
            imageView = (ImageView) itemView.findViewById(R.id.freshnews_content_one_iv_des);
            video = (ImageView) itemView.findViewById(R.id.iv_video);
            des = (TextView) itemView.findViewById(R.id.desdd);
//            zhiding = (TextView) itemView.findViewById(R.id.zhiding);


        }
        @Override
        public void onClick(View v) {
            if(mItemClickListener!=null){
                mItemClickListener.onItemClick(v,getPosition());
            }
        }
    }

    private class HomeSingleContentTwoViewHolder extends RecyclerView.ViewHolder {

        JCVideoPlayerStandard jcVideoPlayerStandard;
        private TextView mTvTitle;

        public HomeSingleContentTwoViewHolder(View contentViewTwo, MyItemClickListener mItemClickListener) {
            super(contentViewTwo);
            jcVideoPlayerStandard = (JCVideoPlayerStandard) contentViewTwo.findViewById(R.id.videoplayer);
            mTvTitle = (TextView) contentViewTwo.findViewById(R.id.tv_title);



        }
    }
}

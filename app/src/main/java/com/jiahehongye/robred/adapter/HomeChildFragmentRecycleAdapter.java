package com.jiahehongye.robred.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.baidu.mobad.feeds.NativeResponse;
import com.bumptech.glide.Glide;
import com.jiahehongye.robred.MainActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.DFTTNewsResult;
import com.jiahehongye.robred.bean.HomeFragmentResult;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjunhui on 2017/5/18.16:40
 */

public class HomeChildFragmentRecycleAdapter extends BaseRecycleViewAdapter {
    private MainActivity mMainUi;
    private ArrayList<DFTTNewsResult.DataBean> fatherArraylist;
    private HomeFragmentResult homeFragmentResult;
    private  ArrayList<ImageView> imageViews;
    private int mPointDis;
    private View footView;
    private List<DFTTNewsResult.DataBean> advDate
            ;

    public HomeChildFragmentRecycleAdapter(MainActivity mMainUi, ArrayList<DFTTNewsResult.DataBean> fatherListDate) {
        this.mMainUi = mMainUi;
        this.fatherArraylist = fatherListDate;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_CONTENT://1图片de
                View contentViewOne = LayoutInflater.from(mMainUi).inflate(R.layout.fragment_fresh_news_content_type_one, null);
                return new HomeSingleContentViewHolder(contentViewOne, mItemClickListener);
            case ITEM_TYPE_CONTENT_TWO://3图片de
                View contentViewTwo = LayoutInflater.from(mMainUi).inflate(R.layout.fragment_fresh_news_content_type_two, null);
                return new FreshNewsContentViewHolder(contentViewTwo, mItemClickListener);
            case ITEM_TYPE_FOOT:
                footView = LayoutInflater.from(mMainUi).inflate(R.layout.fragment_home_foot, null);
                hintFootView();
                return new HomeFootViewHolder(footView);

            case ITEM_TYPE_BIG_ADV://大图广告
                View avdTwo3 = LayoutInflater.from(mMainUi).inflate(R.layout.fragment_fresh_news_content, null);
//                return new FreshBigADVViewHolder(avdTwo3, mItemClickListener);
                return new FreshBigADBaiDuXinXiLiuVViewHolder(avdTwo3, mItemClickListener);
            case ITEM_TYPE_ADV://三张小图的广告
                View avdTwo = LayoutInflater.from(mMainUi).inflate(R.layout.fragment_fresh_news_content_type_two, null);
                return new FreshNewsADVViewHolder(avdTwo, mItemClickListener);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)){
            case ITEM_TYPE_CONTENT_TWO:
                onBindTitleViewHolder((FreshNewsContentViewHolder) holder,fatherArraylist.get(position));
                break;
            case ITEM_TYPE_CONTENT :
                onBindContentViewHolder((HomeSingleContentViewHolder) holder,fatherArraylist.get(position));
                break;
            case ITEM_TYPE_ADV:

                onBindContentAdvHolder((FreshNewsADVViewHolder)holder,fatherArraylist.get(position));
                break;
//            case ITEM_TYPE_BIG_ADV:
//                onbindConteBigAdvHolder((FreshBigADVViewHolder)holder,fatherArraylist.get(position));
//                break;

            case ITEM_TYPE_BIG_ADV:
//                onbindConteBigAdvHolder((FreshBigADVViewHolder) holder, fatherArraylist.get(position - 1));
                onbindConteBigBaiduAdvHolder((FreshBigADBaiDuXinXiLiuVViewHolder) holder, fatherArraylist.get(position));
                break;
        }
    }
    private void onbindConteBigBaiduAdvHolder(FreshBigADBaiDuXinXiLiuVViewHolder holder, DFTTNewsResult.DataBean dataBean) {
        NativeResponse nrAd = dataBean.getNativeResponse();
        if (nrAd == null) {
            return;
        }

//        AQuery aq = new AQuery(convertView);
        AQuery aq = holder.aQuery;
//        aq.id(R.id.native_icon_image).image(nrAd.getIconUrl(), false, true);
        aq.id(R.id.freshnews_content_one_iv_des).image(nrAd.getImageUrl(), false, true);
        aq.id(R.id.des).text(nrAd.getDesc());
        aq.id(R.id.freshnews_content_tv_title).text(nrAd.getTitle());
//        aq.id(R.id.native_brand_name).text(nrAd.getBrandName());
//        aq.id(R.id.native_adlogo).image(nrAd.getAdLogoUrl(), false, true);
//        aq.id(R.id.native_baidulogo).image(nrAd.getBaiduLogoUrl(), false, true);
//        String text = nrAd.isDownloadApp() ? "下载" : "查看";
//        aq.id(R.id.native_cta).text(text);
        nrAd.recordImpression(holder.avdView);


//        holder.title.setText(dataBean.getTopic());
//        Glide.with(UIUtils.getContext()).load(dataBean.getMiniimg().get(0).getSrc())
//                .error(R.mipmap.default_img)
//                .placeholder(R.mipmap.default_img)
//                .into(holder.imageView);
//
//        holder.des.setText(dataBean.getSource());
    }
    private void onbindConteBigAdvHolder(FreshBigADVViewHolder holder, DFTTNewsResult.DataBean dataBean) {
        holder.title.setText(dataBean.getTopic());
        Glide.with(UIUtils.getContext()).load(dataBean.getMiniimg().get(0).getSrc())
                .error(R.mipmap.default_img)
                .placeholder(R.mipmap.default_img)
                .into(holder.imageView);

//        if(dataBean.getIsvideo().equals("1")){
//            holder.video.setVisibility(View.VISIBLE);
//        }else {
//            holder.video.setVisibility(View.GONE);
//        }
//        holder.des.setText(newsBean.getSource()+"   "+newsBean.getComment_count()+"   评论"+newsBean.getDate());
        holder.des.setText(dataBean.getSource());
    }

    private void onBindContentAdvHolder(FreshNewsADVViewHolder holder, DFTTNewsResult.DataBean dataBean) {
        holder.title.setText(dataBean.getTopic());
        Glide.with(UIUtils.getContext()).load(dataBean.getMiniimg().get(0).getSrc())
                .error(R.mipmap.default_img).placeholder(R.mipmap.default_img)
                .into(holder.imageView1);
        Glide.with(UIUtils.getContext()).load(dataBean.getMiniimg().get(1).getSrc())
                .error(R.mipmap.default_img).placeholder(R.mipmap.default_img)
                .into(holder.imageView2);

        Glide.with(UIUtils.getContext()).load(dataBean.getMiniimg().get(2).getSrc())
                .error(R.mipmap.default_img).placeholder(R.mipmap.default_img)
                .into(holder.imageView3);
//        holder.des.setText(newsBean.getSource()+"   "+newsBean.getComment_count()+"   评论"+newsBean.getDate());
        holder.des.setText(dataBean.getSource());

        holder.mTvAdv.setVisibility(View.VISIBLE);


    }

    private void onBindTitleViewHolder(FreshNewsContentViewHolder holder, DFTTNewsResult.DataBean newsBean){
        holder.title.setText(newsBean.getTopic());
        Glide.with(UIUtils.getContext()).load(newsBean.getMiniimg().get(0).getSrc())
                .error(R.mipmap.default_img).placeholder(R.mipmap.default_img)
                .into(holder.imageView1);
        Glide.with(UIUtils.getContext()).load(newsBean.getMiniimg().get(1).getSrc())
                .error(R.mipmap.default_img).placeholder(R.mipmap.default_img)
                .into(holder.imageView2);

        Glide.with(UIUtils.getContext()).load(newsBean.getMiniimg().get(2).getSrc())
                .error(R.mipmap.default_img).placeholder(R.mipmap.default_img)
                .into(holder.imageView3);
//        holder.des.setText(newsBean.getSource()+"   "+newsBean.getComment_count()+"   评论"+newsBean.getDate());
        holder.des.setText(newsBean.getSource());
        holder.mTvAdv.setVisibility(View.GONE);

//        if (newsBean.getTop().equals("1")){
//            holder.zhiding.setVisibility(View.VISIBLE);
//        }
    }
    private void onBindContentViewHolder(HomeSingleContentViewHolder holder, DFTTNewsResult.DataBean newsBean){
        holder.title.setText(newsBean.getTopic());
        if(newsBean.getMiniimg().size()>0)
        Glide.with(UIUtils.getContext()).load(newsBean.getMiniimg().get(0).getSrc())
                .error(R.mipmap.default_img)
                .placeholder(R.mipmap.default_img)
                .into(holder.imageView);

        if(newsBean.getIsvideo().equals("1")){
            holder.video.setVisibility(View.VISIBLE);
        }else {
            holder.video.setVisibility(View.GONE);
        }
//        holder.des.setText(newsBean.getSource()+"   "+newsBean.getComment_count()+"   评论"+newsBean.getDate());
        holder.des.setText(newsBean.getSource());

//        holder.title.setText(newsBean.getTitle());
//        Glide.with(UIUtils.getContext()).load(newsBean.getImgUrl()).asBitmap().into(holder.imageView);
//        holder.des.setText(newsBean.getContentSource()+"   评论"+newsBean.getCommentNum()+"   "+newsBean.getCreateDate());


    }
    public void showFootView() {
        if (footView != null) {
            footView.setVisibility(View.VISIBLE);
        }
    }

//    public void setAdvDate(List<DFTTNewsResult.DataBean> data1) {
//        this.advDate = data1;
//    }

    class HomeFootViewHolder extends RecyclerView.ViewHolder {

        public HomeFootViewHolder(View itemView) {
            super(itemView);
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
        } else {
            if(fatherArraylist.get(position).getIsadv().equals("1")){

                if(fatherArraylist.get(position).getBigpic().equals("1")){
                    return ITEM_TYPE_BIG_ADV;
                }
                return ITEM_TYPE_ADV;
            }

            if(fatherArraylist.get(position).getMiniimg_size().equals("3")){
                return ITEM_TYPE_CONTENT_TWO;
            }
            return ITEM_TYPE_CONTENT;
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
        private final ImageView video;

        public HomeSingleContentViewHolder(View contentView, MyItemClickListener mItemClickListener) {
            super(contentView);
            this.mItemClickListener = mItemClickListener;
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.freshnews_content_tv_title);
            imageView = (ImageView) itemView.findViewById(R.id.freshnews_content_one_iv_des);
            video = (ImageView) itemView.findViewById(R.id.iv_video);
            des = (TextView) itemView.findViewById(R.id.des);
            zhiding = (TextView) itemView.findViewById(R.id.zhiding);


        }
        @Override
        public void onClick(View v) {
            if(mItemClickListener!=null){
                mItemClickListener.onItemClick(v,getPosition());
            }
        }
    }


    /**
     * 3图的
     */
    class FreshNewsContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyItemClickListener mItemClickListener;
        private TextView title;
        private TextView des;
        private ImageView imageView;
        private TextView zhiding;
        private  ImageView imageView1;
        private  ImageView imageView2;
        private  ImageView imageView3;
        private final TextView mTvAdv;

        public FreshNewsContentViewHolder(View itemView, MyItemClickListener mItemClickListener) {
            super(itemView);
            this.mItemClickListener = mItemClickListener;
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.freshnews_content_tv_title);
            imageView1 = (ImageView) itemView.findViewById(R.id.freshnews_content_iv1);
            imageView2 = (ImageView) itemView.findViewById(R.id.freshnews_content_iv2);
            imageView3 = (ImageView) itemView.findViewById(R.id.freshnews_content_iv3);
            mTvAdv = (TextView) itemView.findViewById(R.id.adv_dddd);
            mTvAdv.setVisibility(View.GONE);
            des = (TextView) itemView.findViewById(R.id.des);
//            zhiding = (TextView) itemView.findViewById(R.id.zhiding);
        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener!=null){
                mItemClickListener.onItemClick(v,getPosition());
            }
        }
    }

    private class FreshNewsADVViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyItemClickListener mItemClickListener;
        private TextView title;
        private TextView des;
        private ImageView imageView;
        private TextView zhiding;
        private  ImageView imageView1;
        private  ImageView imageView2;
        private  ImageView imageView3;
        private  TextView mTvAdv;
        public FreshNewsADVViewHolder(View avdTwo, MyItemClickListener mItemClickListener) {
            super(avdTwo);
            this.mItemClickListener = mItemClickListener;
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.freshnews_content_tv_title);
            imageView1 = (ImageView) itemView.findViewById(R.id.freshnews_content_iv1);
            imageView2 = (ImageView) itemView.findViewById(R.id.freshnews_content_iv2);
            imageView3 = (ImageView) itemView.findViewById(R.id.freshnews_content_iv3);
            mTvAdv = (TextView) itemView.findViewById(R.id.adv_dddd);
            mTvAdv.setVisibility(View.VISIBLE);
            des = (TextView) itemView.findViewById(R.id.des);
        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener!=null){
                mItemClickListener.onItemClick(v,getPosition());
            }
        }
    }

    private class FreshBigADVViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyItemClickListener mItemClickListener;
        private TextView title;
        private TextView des;
        private ImageView imageView;
        private TextView zhiding;
        private final ImageView video;
        public FreshBigADVViewHolder(View avdTwo3, MyItemClickListener mItemClickListener) {
            super(avdTwo3);
            this.mItemClickListener = mItemClickListener;
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.freshnews_content_tv_title);
            imageView = (ImageView) itemView.findViewById(R.id.freshnews_content_one_iv_des);
            video = (ImageView) itemView.findViewById(R.id.iv_video);
            des = (TextView) itemView.findViewById(R.id.des);
            zhiding = (TextView) itemView.findViewById(R.id.zhiding);
            TextView mTvDAD = (TextView) itemView.findViewById(R.id.adv_dddd);
            mTvDAD.setVisibility(View.VISIBLE);

        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener!=null){
                mItemClickListener.onItemClick(v,getPosition());
            }
        }
    }

    private class FreshBigADBaiDuXinXiLiuVViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View avdView;
        private MyItemClickListener mItemClickListener;
        private TextView title;
        private TextView des;
        private ImageView imageView;
        private TextView zhiding;
        private ImageView video;
        private AQuery aQuery;

        public FreshBigADBaiDuXinXiLiuVViewHolder(View itemView, MyItemClickListener mItemClickListener) {
            super(itemView);
            this.avdView =itemView;
            this.mItemClickListener = mItemClickListener;
            aQuery = new AQuery(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.freshnews_content_tv_title);
            imageView = (ImageView) itemView.findViewById(R.id.freshnews_content_one_iv_des);
            video = (ImageView) itemView.findViewById(R.id.iv_video);
            des = (TextView) itemView.findViewById(R.id.des);
            zhiding = (TextView) itemView.findViewById(R.id.zhiding);
            TextView mTvDAD = (TextView) itemView.findViewById(R.id.adv_dddd);
            mTvDAD.setVisibility(View.VISIBLE);


        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener!=null){
                mItemClickListener.onItemClick(v,getPosition());
            }
        }
    }
}

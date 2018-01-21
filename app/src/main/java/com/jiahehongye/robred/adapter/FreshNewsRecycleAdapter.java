package com.jiahehongye.robred.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiahehongye.robred.MainActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.WebActivity;
import com.jiahehongye.robred.bean.NewsBean;
import com.jiahehongye.robred.bean.RecommendBanner;
import com.jiahehongye.robred.interfaces.MyHeadViewClickListener;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by huangjunhui on 2016/12/6.13:31
 */
public class FreshNewsRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MainActivity mActivity;
    private ArrayList<NewsBean> fatherArrayList;
    private Context context;

    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_FOOT = 2;
    public static final int ITEM_TYPE_CONTENT_ONE = 3;
    public static final int ITEM_TYPE_CONTENT_TWO = 4;
    private MyItemClickListener mItemClickListener;
    private MyHeadViewClickListener mHeadClickListener;
    public static View footView;
    private ArrayList<RecommendBanner> banners;
    private Handler handler;
    private ArrayList<ImageView> imageViews;


    public FreshNewsRecycleAdapter(FragmentActivity activity, ArrayList<NewsBean> fatherArrayList) {
        this.mActivity = (MainActivity) activity;
        this.fatherArrayList = fatherArrayList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_HEADER:
                View headView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_fresh_news_head, null);
                return new FreshNewsHeadViewHolder(headView, mHeadClickListener);
            case ITEM_TYPE_CONTENT:
                View contentView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_fresh_news_content, null);
                return new FreshNewsContentViewHolder(contentView, mItemClickListener);
            case ITEM_TYPE_FOOT:
                footView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_home_foot, null);
                hintFootView();
                return new FreshNewsFootViewHolder(footView);
            case ITEM_TYPE_CONTENT_ONE:
                View contentViewOne = LayoutInflater.from(mActivity).inflate(R.layout.fragment_fresh_news_content_type_one, null);
                return new FreshNewsContentViewHolder(contentViewOne, mItemClickListener);
            case ITEM_TYPE_CONTENT_TWO:
                View contentViewTwo = LayoutInflater.from(mActivity).inflate(R.layout.fragment_fresh_news_content_type_two, null);
                return new FreshNewsContentViewHolder(contentViewTwo, mItemClickListener);

        }
        return null;
    }



    @Override
    public int getItemViewType(int position) {
        if (getItemCount() == 1) {
            return ITEM_TYPE_HEADER;
        }
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        } else if (position == fatherArrayList.size() + 1) {
            return ITEM_TYPE_FOOT;
        } else {
            if (fatherArrayList.get(position-1).getStyle().equals("1")) {
                return ITEM_TYPE_CONTENT_ONE;
            }else if (fatherArrayList.get(position-1).getStyle().equals("2")) {
                return ITEM_TYPE_CONTENT;
            }
        }
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)){
            case ITEM_TYPE_CONTENT_ONE:
                onBindTitleViewHolder((FreshNewsContentViewHolder) holder,fatherArrayList.get(position-1));
                break;
            case ITEM_TYPE_CONTENT :
                onBindContentViewHolder((FreshNewsContentViewHolder) holder,fatherArrayList.get(position-1));
                break;

        }
    }



    private void onBindTitleViewHolder(FreshNewsContentViewHolder holder,NewsBean newsBean){
        holder.title.setText(newsBean.getTitle());
        Glide.with(UIUtils.getContext()).load(newsBean.getImgUrl()).asBitmap().into(holder.imageView);
        holder.des.setText(newsBean.getContentSource()+"   "+newsBean.getCreateDate()+"   评论"+newsBean.getCommentNum());
        if (newsBean.getTop().equals("1")){
            holder.zhiding.setVisibility(View.VISIBLE);
        }
    }
    private void onBindContentViewHolder(FreshNewsContentViewHolder holder,NewsBean newsBean){
        holder.title.setText(newsBean.getTitle());
        Glide.with(UIUtils.getContext()).load(newsBean.getImgUrl()).asBitmap().into(holder.imageView);
        holder.des.setText(newsBean.getContentSource()+"   评论"+newsBean.getCommentNum()+"   "+newsBean.getCreateDate());


    }

    @Override
    public int getItemCount() {
        if (fatherArrayList != null && fatherArrayList.size() > 0) {
            return fatherArrayList.size() + 2;
        } else {
            return 1;
        }
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnHeadClickListener(MyHeadViewClickListener listener) {
        this.mHeadClickListener = listener;
    }

    public void setHeardDate(ArrayList<RecommendBanner> banners) {
        this.banners = banners;
    }

    class FreshNewsHeadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private MyHeadViewClickListener mHeadClickListener;
        private LinearLayout mHeardLl;
        private ViewPager mHeardVp;

        public FreshNewsHeadViewHolder(View itemView, MyHeadViewClickListener mHeadClickListener) {
            super(itemView);
            this.mHeadClickListener = mHeadClickListener;
            itemView.setOnClickListener(this);
            mHeardLl = (LinearLayout) itemView.findViewById(R.id.freshnews_heard_ll);
            mHeardVp = (ViewPager) itemView.findViewById(R.id.freshnews_heard_vp);
            mHeardLl.setVisibility(banners==null? View.GONE: View.VISIBLE);

            if(banners!=null){
                imageViews = new ArrayList<>();
                if(banners!=null){
                    ImageView imageView = null;
                    for (int i = 0; i < banners.size(); i++) {
                        imageView = new ImageView(mActivity);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        Glide.with(UIUtils.getContext()).load(banners.get(i).getAdvertAddr()).into(imageView);
                        imageViews.add(imageView);
                    }

                    FreshNewsBannersPagerAdapter  freshNewsBannersPagerAdapter = new FreshNewsBannersPagerAdapter();
                    mHeardVp.setAdapter(freshNewsBannersPagerAdapter);

                    if (handler == null) {
                        handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                switch (msg.what) {
                                    case 0:
                                        int currentItem = mHeardVp.getCurrentItem();
                                        currentItem++;
                                        if (currentItem >= imageViews.size()) {
                                            currentItem = 0;
                                        }
                                        mHeardVp.setCurrentItem(currentItem);
                                        break;
                                }
                                handler.sendEmptyMessageDelayed(0, 3000);
                            }
                        };
                        handler.sendEmptyMessageDelayed(0, 3000);
                    }
                }

            }


        }


        @Override
        public void onClick(View v) {
            if(mHeadClickListener!=null){
                mHeadClickListener.onHeadClick(v);
            }
        }
    }

    class FreshNewsContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyItemClickListener mItemClickListener;
        private TextView title;
        private TextView des;
        private ImageView imageView;
        private TextView zhiding;
        public FreshNewsContentViewHolder(View itemView, MyItemClickListener mItemClickListener) {
            super(itemView);
            this.mItemClickListener = mItemClickListener;
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.freshnews_content_tv_title);
            imageView = (ImageView) itemView.findViewById(R.id.freshnews_content_one_iv_des);
            des = (TextView) itemView.findViewById(R.id.des);
            zhiding = (TextView) itemView.findViewById(R.id.zhiding);
        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener!=null){
                mItemClickListener.onItemClick(v,getPosition()-1);
            }
        }
    }

    class FreshNewsFootViewHolder extends RecyclerView.ViewHolder {
        public FreshNewsFootViewHolder(View itemView) {
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


    private class FreshNewsBannersPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if(imageViews==null){
                return 0;
            }
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView view = imageViews.get(position);
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeAllViewsInLayout();
            }
            container.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String hrefAddr = banners.get(position).getHrefAddr();
                    if(hrefAddr==null||hrefAddr.equals("")){
                        return;
                    }
                    Intent intent = new Intent(mActivity, WebActivity.class);
                    intent.putExtra("title","");
                    intent.putExtra("URL",hrefAddr);
                    mActivity.startActivity(intent);
                }
            });

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

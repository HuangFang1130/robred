package com.jiahehongye.robred.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.SystemActivity;
import com.jiahehongye.robred.bean.MessageDetailResult;
import com.jiahehongye.robred.interfaces.MyItemClickListener;

import java.util.List;

/**
 * Created by huangjunhui on 2017/1/5.17:29
 */
public class SystemListRecycleAdapter extends RecyclerView.Adapter {
    private SystemActivity activity;
    private MyItemClickListener mItemClickListener;
    private List<MessageDetailResult.FindMessageBean> fatherArrayList;

    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_FOOT = 2;
    private View footView;
    private boolean flag;
    private RotateAnimation animationup;
    private RotateAnimation animationdown;

    public SystemListRecycleAdapter(SystemActivity systemActivity, List<MessageDetailResult.FindMessageBean> fatherListDate) {
        this.activity = systemActivity;
        this.fatherArrayList = fatherListDate;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {

            case ITEM_TYPE_CONTENT:
                View contentView = LayoutInflater.from(activity).inflate(R.layout.activity_system_content, null);
                return new SystemListContentViewHolder(contentView, mItemClickListener);
            case ITEM_TYPE_FOOT:
                footView = LayoutInflater.from(activity).inflate(R.layout.fragment_home_foot, null);
                hintFootView();
                return new SystemListFootViewHolder(footView);
        }
        return null;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof SystemListContentViewHolder) {
            SystemListContentViewHolder contentViewHolder = (SystemListContentViewHolder) holder;
            String messTitle = fatherArrayList.get(position).getMessTitle();//标题
            String messCont = fatherArrayList.get(position).getMessCont();//内容
            String createDate = fatherArrayList.get(position).getCreateDate();//时间日期

            contentViewHolder.mTvSystemTitle.setText(messTitle);
            contentViewHolder.mTvSystemDate.setText(createDate);
            contentViewHolder.mTvSystemDetail1.setText(messCont);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == fatherArrayList.size()) {
            return ITEM_TYPE_FOOT;
        } else {
            return ITEM_TYPE_CONTENT;
        }

    }

    @Override
    public int getItemCount() {
        return fatherArrayList.size() + 1;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    private class SystemListContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mItemClickListener;
        private ImageView mIvRowDown;
        private TextView mTvSystemTitle;

        private TextView mTvSystemDetail1;
        private TextView mTvSystemDate;
        private final RelativeLayout mRlSystem;

        public SystemListContentViewHolder(View contentView, MyItemClickListener mItemClickListener) {
            super(contentView);
            contentView.setOnClickListener(this);
            this.mItemClickListener = mItemClickListener;
            initAnimation();
            mIvRowDown = (ImageView) contentView.findViewById(R.id.system_content_iv_row_down);
            mTvSystemTitle = (TextView) contentView.findViewById(R.id.system_content_tv_title);
            mTvSystemDetail1 = (TextView) contentView.findViewById(R.id.system_content_tv_detail);
            mTvSystemDate = (TextView) contentView.findViewById(R.id.system_content_tv_date);
            mRlSystem = (RelativeLayout) contentView.findViewById(R.id.system_content_rl_detail);

            mRlSystem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int lineHeight = mTvSystemDetail1.getLineHeight();
                    int lineCount = mTvSystemDetail1.getLineCount();

                    mTvSystemDetail1.setHeight(lineHeight*lineCount);
                    if(flag){
                            if(animationup!=null){
                                mIvRowDown.startAnimation(animationup);
                            }
                            flag = false;
                        }else {
                            if(animationdown!=null){
                                mIvRowDown.startAnimation(animationdown);
                            }
                            flag = true;


                    }
                }
            });

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }


    private void initAnimation(){
        flag = true;
        animationup = new RotateAnimation(0,180, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animationup.setDuration(500);
        animationup.setFillAfter(true);
        animationup.setInterpolator(new AccelerateInterpolator());
        animationup.setRepeatCount(0);

        animationdown = new RotateAnimation(180,0, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animationdown.setDuration(500);
        animationdown.setFillAfter(true);
        animationdown.setInterpolator(new AccelerateInterpolator());
        animationdown.setRepeatCount(0);
    }
    private class SystemListFootViewHolder extends RecyclerView.ViewHolder {
        public SystemListFootViewHolder(View footView) {
            super(footView);
        }
    }
}

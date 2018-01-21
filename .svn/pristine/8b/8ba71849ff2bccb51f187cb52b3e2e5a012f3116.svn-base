package com.jiahehongye.robred.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.InteractionActivity;
import com.jiahehongye.robred.bean.MessageDetailResult;
import com.jiahehongye.robred.interfaces.MyItemClickListener;

import java.util.List;

/**
 * Created by huangjunhui on 2017/1/5.17:12
 */
public class InteractionListRecycleAdapter extends RecyclerView.Adapter {
    private List<MessageDetailResult.FindMessageBean> fatherArrayList;
    private InteractionActivity activity;
    private MyItemClickListener mItemClickListener;
    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_FOOT = 2;
    private View footView;
    public InteractionListRecycleAdapter(InteractionActivity interactionActivity, List<MessageDetailResult.FindMessageBean> fatherListDate) {
        this.activity = interactionActivity;
        this.fatherArrayList = fatherListDate;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {

            case ITEM_TYPE_CONTENT:
                View contentView = LayoutInflater.from(activity).inflate(R.layout.activity_interaction_content, null);
                return new InteractionListContentViewHolder(contentView, mItemClickListener);
            case ITEM_TYPE_FOOT:
                footView = LayoutInflater.from(activity).inflate(R.layout.fragment_home_foot, null);
                hintFootView();
                return new InteractionListFootViewHolder(footView);
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
        if (holder instanceof InteractionListContentViewHolder) {
            InteractionListContentViewHolder contentViewHolder = (InteractionListContentViewHolder) holder;
            String messTitle = fatherArrayList.get(position).getMessTitle();//标题
            String messCont = fatherArrayList.get(position).getMessCont();//内容
            String createDate = fatherArrayList.get(position).getCreateDate();//时间日期

            contentViewHolder.mTvInteractionTitle.setText(messTitle);
            contentViewHolder.mTvInteractionDate.setText(createDate);
            contentViewHolder.mTvInteractionDetail1.setText(messCont);
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

    private class InteractionListContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyItemClickListener mItemClickListener;
        private final TextView mTvInteractionTitle;
        private final TextView mTvInteractionDetail1;
        private final TextView mTvInteractionDate;

        public InteractionListContentViewHolder(View contentView, MyItemClickListener mItemClickListener) {
            super(contentView);
            contentView.setOnClickListener(this);
            this.mItemClickListener = mItemClickListener;
            mTvInteractionTitle = (TextView) contentView.findViewById(R.id.interaction_content_tv_title);
            mTvInteractionDetail1 = (TextView) contentView.findViewById(R.id.interaction_content_tv_detail);
            mTvInteractionDate = (TextView) contentView.findViewById(R.id.interaction_content_tv_date);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    private class InteractionListFootViewHolder extends RecyclerView.ViewHolder {
        public InteractionListFootViewHolder(View footView) {
            super(footView);
        }
    }
}

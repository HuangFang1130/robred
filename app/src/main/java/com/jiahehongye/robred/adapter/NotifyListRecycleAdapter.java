package com.jiahehongye.robred.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.NotifyActivity;
import com.jiahehongye.robred.bean.MessageDetailResult;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.UIUtils;

import java.util.List;

/**
 * Created by huangjunhui on 2017/1/5.16:44
 */
public class NotifyListRecycleAdapter extends RecyclerView.Adapter {
    private List<MessageDetailResult.FindMessageBean> fatherArrayList;
    private NotifyActivity activity;

    private MyItemClickListener mItemClickListener;
    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_FOOT = 2;
    private View footView;

    public NotifyListRecycleAdapter(NotifyActivity notifyActivity, List<MessageDetailResult.FindMessageBean> fatherListDate) {
        this.activity = notifyActivity;
        this.fatherArrayList = fatherListDate;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {

            case ITEM_TYPE_CONTENT:
                View contentView = LayoutInflater.from(activity).inflate(R.layout.activity_norify_content, null);
                return new NotifyListContentViewHolder(contentView, mItemClickListener);
            case ITEM_TYPE_FOOT:
                footView = LayoutInflater.from(activity).inflate(R.layout.fragment_home_foot, null);
                hintFootView();
                return new NotifyListFootViewHolder(footView);
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
        if (holder instanceof NotifyListContentViewHolder) {
            NotifyListContentViewHolder contentViewHolder = (NotifyListContentViewHolder) holder;
            String messTitle = fatherArrayList.get(position).getMessTitle();//标题
            String messCont = fatherArrayList.get(position).getMessCont();//内容
            String createDate = fatherArrayList.get(position).getCreateDate();//时间日期

            contentViewHolder.mTvNotifyTitle.setText(messTitle);
            contentViewHolder.mTvNotifyDate.setText(createDate);
            contentViewHolder.mTvNotifyDetail1.setText(messCont);
            Glide.with(UIUtils.getContext()).load(fatherArrayList.get(position).getYygImg()).into(contentViewHolder.mIvNotifyAvatar);
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
    private class NotifyListContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mItemClickListener;
        private final TextView mTvNotifyTitle;
        private final TextView mTvNotifyDate;
        private final ImageView mIvNotifyAvatar;
        private final TextView mTvNotifyDetail1;

        public NotifyListContentViewHolder(View contentView, MyItemClickListener mItemClickListener) {
            super(contentView);
            contentView.setOnClickListener(this);
            this.mItemClickListener = mItemClickListener;
            mTvNotifyTitle = (TextView) contentView.findViewById(R.id.notify_content_tv_title);
            mTvNotifyDate = (TextView) contentView.findViewById(R.id.notify_content_tv_date);
            mIvNotifyAvatar = (ImageView) contentView.findViewById(R.id.notify_content_iv_avatar);
            mTvNotifyDetail1 = (TextView) contentView.findViewById(R.id.notify_content_tv_detail);


        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    private class NotifyListFootViewHolder extends RecyclerView.ViewHolder {
        public NotifyListFootViewHolder(View footView) {
            super(footView);
        }
    }
}

package com.jiahehongye.robred.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.ChatGiftResult;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.UIUtils;

import java.util.List;

/**
 * Created by huangjunhui on 2016/12/21.17:05
 */
public class SendGiftRecycleAdapter extends RecyclerView.Adapter {
    private FragmentActivity activity;
    private List<ChatGiftResult.DataBean.GiftLBean> fatherArrayList;
    private MyItemClickListener mItemClickListener;

    public SendGiftRecycleAdapter(FragmentActivity activity, List<ChatGiftResult.DataBean.GiftLBean> fatherArrayList) {
        this.fatherArrayList = fatherArrayList;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.chat_send_gift_recycle,null);
        return new SendGiftRecycleHolder(view,mItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof SendGiftRecycleHolder){
            SendGiftRecycleHolder sendHolder = (SendGiftRecycleHolder) holder;
            Glide.with(UIUtils.getContext()).load(fatherArrayList.get(position).getGiftAddr()).into(sendHolder.hIvFlower);
            sendHolder.hTvName.setText(fatherArrayList.get(position).getName());
            sendHolder.hTvMasonry.setText(fatherArrayList.get(position).getDiamondNum());

        }
    }

    @Override
    public int getItemCount() {
        return fatherArrayList.size();
    }
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    private class SendGiftRecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mItemClickListener;
        private RelativeLayout mRlLiWu;
        private ImageView hIvFlower;
        private TextView hTvName;
        private TextView hTvMasonry;

        public SendGiftRecycleHolder(View view, MyItemClickListener mItemClickListener) {
            super(view);
            mRlLiWu = (RelativeLayout) view.findViewById(R.id.rl_liwu);
            this.mItemClickListener = mItemClickListener;
            hIvFlower = (ImageView) view.findViewById(R.id.iv_flower);
            hTvName = (TextView) view.findViewById(R.id.tv_flower);
            hTvMasonry =  (TextView) view.findViewById(R.id.tv_masonry);


            mRlLiWu.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }
}

package com.jiahehongye.robred.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.PraiseAllActivity;
import com.jiahehongye.robred.bean.SonCommentsBean;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.GlideCircleTransform;

import java.util.ArrayList;

/**
 * Created by huangjunhui on 2016/12/6.17:21
 */
public class PraiseRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_FOOT = 2;
    private ArrayList<SonCommentsBean> fatherArrayList;
    private PraiseAllActivity mActivity;
    private MyItemClickListener mItemClickListener;


    public PraiseRecycleAdapter(PraiseAllActivity praiseAllActivity, ArrayList<SonCommentsBean> fatherArrayList) {
        this.mActivity = praiseAllActivity;
        this.fatherArrayList = fatherArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_CONTENT:
                View contentView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_praise_content, null);
                return new PraiseContentViewHolder(contentView, mItemClickListener);
            case ITEM_TYPE_FOOT:
                View footView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_home_foot, null);
                return new PraiseFootViewHolder(footView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case ITEM_TYPE_CONTENT:
                onBindTitleViewHolder((PraiseContentViewHolder) holder,fatherArrayList.get(position));
                break;
        }
    }

    private void onBindTitleViewHolder(final PraiseContentViewHolder holder, final SonCommentsBean Bean){
        Glide.with(UIUtils.getContext()).load(Bean.getUserPhoto()).transform(new GlideCircleTransform(UIUtils.getContext())).into(holder.all_zan_head);
        holder.all_zan_name.setText(Bean.getNickName());
    }

    @Override
    public int getItemViewType(int position) {

//        if (position == getItemCount() ) {
//            return ITEM_TYPE_FOOT;
//        } else {
            return ITEM_TYPE_CONTENT;
//        }
    }

    @Override
    public int getItemCount() {
//        if (fatherArrayList != null && fatherArrayList.size() > 0) {
//            return fatherArrayList.size() + 1;
//        } else {
//            return 1;
//        }
        return fatherArrayList.size();
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    class PraiseContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private MyItemClickListener mItemClickListener;
        private ImageView all_zan_head;
        private TextView all_zan_name;

        public PraiseContentViewHolder(View itemView, MyItemClickListener mItemClickListener) {
            super(itemView);
            this.mItemClickListener =mItemClickListener;
            itemView.setOnClickListener(this);
            all_zan_head = (ImageView) itemView.findViewById(R.id.all_zan_head);
            all_zan_name = (TextView) itemView.findViewById(R.id.all_zan_name);
        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener!=null){
                mItemClickListener.onItemClick(v,getPosition());
            }
        }
    }

    class PraiseFootViewHolder extends RecyclerView.ViewHolder {

        public PraiseFootViewHolder(View itemView) {
            super(itemView);
        }
    }
}

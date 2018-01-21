package com.jiahehongye.robred.adapter;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.ContanctDetailActivity;
import com.jiahehongye.robred.activity.GrabRedActivity;
import com.jiahehongye.robred.bean.GrabPersonalListResult;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.GlideRoundTransform;

import java.util.List;

/**
 * Created by huangjunhui on 2016/12/2.14:17
 */
public class GrabPersonalRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<GrabPersonalListResult.DataBean.SendRedEnveListBean> fatherArrayList;
    private GrabRedActivity mActivity;
    //item类型
    public static final int ITEM_TYPE_CONTENT_OTHER = 1;
    public static final int ITEM_TYPE_CONTENT_SELF = 3;
    public static final int ITEM_TYPE_FOOT = 2;
    private View footView;
    private MyItemClickListener mItemClickListener;

    public GrabPersonalRecycleAdapter(FragmentActivity activity, List<GrabPersonalListResult.DataBean.SendRedEnveListBean> fatherArrayList) {
        this.mActivity = (GrabRedActivity) activity;
        this.fatherArrayList = fatherArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_CONTENT_OTHER://他人的
                View contentView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_grabred_personal_content, null);
                return new GrabPersonalContentViewHolder(contentView, mItemClickListener);
             case ITEM_TYPE_CONTENT_SELF://自己的
                View contentSelfView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_grabred_merchant_content, null);
                return new GrabPersonalContentSelfViewHolder(contentSelfView, mItemClickListener);

            case ITEM_TYPE_FOOT:
                footView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_home_foot, null);
                hintFootView();
                return new GrabPersonalFootViewHolder(footView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        /**
         *   redEnveMark
            1即将开始
            2开抢了
            3已抢过
            4已抢光
         */
        if (holder instanceof GrabPersonalContentViewHolder) {
            GrabPersonalContentViewHolder contentHolder = (GrabPersonalContentViewHolder) holder;
            contentHolder.mTvGrabPersonalName.setText(fatherArrayList.get(position).getNickName());
            contentHolder.mTvGrabPersonalDes.setText(fatherArrayList.get(position).getRedEnveLeaveword());
            String user_level = fatherArrayList.get(position).getUSER_LEVEL();//用户的级别

            String userPhoto = fatherArrayList.get(position).getUserPhoto();

                Glide.with(UIUtils.getContext()).load(userPhoto)
                        .error(R.mipmap.headsq_pic)
                        .placeholder(R.mipmap.headsq_pic)
                        .transform(new CenterCrop(UIUtils.getContext()),new GlideRoundTransform(UIUtils.getContext()))
                        .into(contentHolder.mIvGrabPersonalAvatar);


            String redEnveMark = fatherArrayList.get(position).getRedEnveMark();
            if(redEnveMark.equals("1")){
                contentHolder.mTvGrabPersonalState.setText("来得及");
            }else if(redEnveMark.equals("2")){
                contentHolder.mTvGrabPersonalState.setText("来得及");
            }else if(redEnveMark.equals("3")){
                contentHolder.mTvGrabPersonalState.setText("已领取");
            }else if(redEnveMark.equals("4")){
                contentHolder.mTvGrabPersonalState.setText("来晚了");
            }


            switch (user_level) {
                case "1":
                    contentHolder.mIvGrabPersonalLevel.setImageResource(R.mipmap.general);
                    break;
                case "2":
                    contentHolder.mIvGrabPersonalLevel.setImageResource(R.mipmap.white_collar);
                    break;
                case "3":
                    contentHolder.mIvGrabPersonalLevel.setImageResource(R.mipmap.gold_collar);
                    break;
                case "4":
                    contentHolder.mIvGrabPersonalLevel.setImageResource(R.mipmap.boss);
                    break;
                case "5":
                    contentHolder.mIvGrabPersonalLevel.setImageResource(R.mipmap.loacl_lord);
                    break;
            }


            contentHolder.mIvGrabPersonalAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String memberMobile = fatherArrayList.get(position).getMemberMobile();
                    Intent intent = new Intent(mActivity, ContanctDetailActivity.class);
                    intent.putExtra("mobile",memberMobile);
                    mActivity.startActivity(intent);
                }
            });

        }
        if(holder instanceof GrabPersonalContentSelfViewHolder){
            GrabPersonalContentSelfViewHolder contentSelfHolder = (GrabPersonalContentSelfViewHolder) holder;
            contentSelfHolder.mTvGrabPersonalSelfName.setText(fatherArrayList.get(position).getNickName());
            contentSelfHolder.mTvGrabPersonalSelfDes.setText(fatherArrayList.get(position).getRedEnveLeaveword());
//            contentSelfHolder.mTvGrabPersonalSelfState.setText(fatherArrayList.get(position).getAuditStatus());
            String user_level = fatherArrayList.get(position).getUSER_LEVEL();//用户的级别

            String userPhoto = fatherArrayList.get(position).getUserPhoto();

                Glide.with(UIUtils.getContext()).load(userPhoto)
                        .error(R.mipmap.headsq_pic)
                        .placeholder(R.mipmap.headsq_pic)
                        .transform(new CenterCrop(UIUtils.getContext()),new GlideRoundTransform(UIUtils.getContext()))
                        .into(contentSelfHolder.mIvGrabPersonalSelfAvatar);


            String redEnveMark = fatherArrayList.get(position).getRedEnveMark();
            if(redEnveMark.equals("1")){
                contentSelfHolder.mTvGrabPersonalSelfState.setText("来得及");
            }else if(redEnveMark.equals("2")){
                contentSelfHolder.mTvGrabPersonalSelfState.setText("来得及");
            }else if(redEnveMark.equals("3")){
                contentSelfHolder.mTvGrabPersonalSelfState.setText("已领取");
            }else if(redEnveMark.equals("4")){
                contentSelfHolder.mTvGrabPersonalSelfState.setText("来晚了");
            }
            switch (user_level) {
                case "1":
                    contentSelfHolder.mIvGrabPersonalSelfLevel.setImageResource(R.mipmap.general);
                    break;
                case "2":
                    contentSelfHolder.mIvGrabPersonalSelfLevel.setImageResource(R.mipmap.white_collar);
                    break;
                case "3":
                    contentSelfHolder.mIvGrabPersonalSelfLevel.setImageResource(R.mipmap.gold_collar);
                    break;
                case "4":
                    contentSelfHolder.mIvGrabPersonalSelfLevel.setImageResource(R.mipmap.boss);
                    break;
                case "5":
                    contentSelfHolder.mIvGrabPersonalSelfLevel.setImageResource(R.mipmap.loacl_lord);
                    break;
            }
        }
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        if (fatherArrayList != null && fatherArrayList.size() > 0) {
            return fatherArrayList.size() + 1;
        } else {
            return 1;
        }
    }

    /**
     * 在这里判断如果是自己发的红包的类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position == fatherArrayList.size()) {
            return ITEM_TYPE_FOOT;
        } else {
            String  id = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_ID, "");

            if (fatherArrayList.get(position).getMemberId().equals(id)) {//
                return ITEM_TYPE_CONTENT_SELF;
            }else{
                return  ITEM_TYPE_CONTENT_OTHER;
            }
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

    class GrabPersonalContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyItemClickListener mItemClickListener;
        private ImageView mIvGrabPersonalAvatar;
        private TextView mTvGrabPersonalName;
        private TextView mTvGrabPersonalDes;
        private TextView mTvGrabPersonalState;
        private ImageView mIvGrabPersonalLevel;

        public GrabPersonalContentViewHolder(View itemView, MyItemClickListener mItemClickListener) {
            super(itemView);
            this.mItemClickListener = mItemClickListener;
            itemView.setOnClickListener(this);

            mIvGrabPersonalAvatar = (ImageView) itemView.findViewById(R.id.grabred_personal_avatar);
            mTvGrabPersonalName = (TextView) itemView.findViewById(R.id.grabred_personal_name);
            mTvGrabPersonalDes = (TextView) itemView.findViewById(R.id.grabred_personal_des);
            mTvGrabPersonalState = (TextView) itemView.findViewById(R.id.grabred_personal_state);
            mIvGrabPersonalLevel = (ImageView) itemView.findViewById(R.id.grabred_personal_level);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }


    class GrabPersonalContentSelfViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyItemClickListener mItemClickListener;
        private  ImageView mIvGrabPersonalSelfAvatar;
        private  TextView mTvGrabPersonalSelfName;
        private  TextView mTvGrabPersonalSelfDes;
        private  TextView mTvGrabPersonalSelfState;
        private  ImageView mIvGrabPersonalSelfLevel;

        public GrabPersonalContentSelfViewHolder(View itemView, MyItemClickListener mItemClickListener) {
            super(itemView);
            this.mItemClickListener = mItemClickListener;
            itemView.setOnClickListener(this);


            mIvGrabPersonalSelfAvatar = (ImageView) itemView.findViewById(R.id.grabred_personal_self_avatar);
            mTvGrabPersonalSelfName = (TextView) itemView.findViewById(R.id.grabred_personal_self_name);
            mTvGrabPersonalSelfDes = (TextView) itemView.findViewById(R.id.grabred_personal_self_des);
            mTvGrabPersonalSelfState = (TextView) itemView.findViewById(R.id.grabred_personal_self_state);
            mIvGrabPersonalSelfLevel = (ImageView) itemView.findViewById(R.id.grabred_personal_self_level);

        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener !=null){
                mItemClickListener.onItemClick(v,getPosition());
            }
        }
    }
    class GrabPersonalFootViewHolder extends RecyclerView.ViewHolder {

        public GrabPersonalFootViewHolder(View itemView) {
            super(itemView);
        }
    }

}

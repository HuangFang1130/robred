package com.jiahehongye.robred.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.ContactActivity;
import com.jiahehongye.robred.bean.AddressResult;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.GlideRoundTransform;
import com.jiahehongye.robred.view.RoundedCornerImageView;

import java.util.List;

/**
 * Created by huangjunhui on 2016/12/5.12:02
 */
public class AddressRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ContactActivity mActivity;
    private List<AddressResult.DataBean> fatherArraylist;
    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_FOOT = 2;
    private View footView;
    private MyItemClickListener mItemClickListener = null;
    private Bitmap bitmap;

    public AddressRecycleAdapter(ContactActivity activity, List<AddressResult.DataBean> fatherArrayList) {
        this.mActivity = (ContactActivity) activity;
        this.fatherArraylist = fatherArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_CONTENT:
                AddressContentViewHolder addressContentViewHolder = null;
                View contentView = null;
                if (contentView == null) {
                    contentView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_address_content, null);
                    addressContentViewHolder = new AddressContentViewHolder(contentView, mItemClickListener);
                    contentView.setTag(R.id.glide_tag, addressContentViewHolder);
                }else {
                    addressContentViewHolder = (AddressContentViewHolder) contentView.getTag();
                }
                return addressContentViewHolder;
            case ITEM_TYPE_FOOT:
                footView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_home_foot, null);
                hintFootView();
                return new AddressFootViewHolder(footView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof AddressContentViewHolder) {
            AddressResult.DataBean dataBean = fatherArraylist.get(position);

            AddressContentViewHolder addressHolder = (AddressContentViewHolder) holder;
            addressHolder.mTvName.setText(dataBean.getNickName());
            addressHolder.mTvSignature.setText(dataBean.getPersonalDescription());
            String recentTime = dataBean.getRecentTime();

            addressHolder.mTvLocation.setText(dataBean.getDistance()+"-"+recentTime);
            String user_photo = dataBean.getUSER_PHOTO();


            if(user_photo.equals("")||user_photo == null){
                Glide.with(UIUtils.getContext()).load(R.mipmap.headsq_pic)
                        .error(R.mipmap.headsq_pic)
                        .placeholder(R.mipmap.headsq_pic)
                        .transform(new CenterCrop(UIUtils.getContext()),new GlideRoundTransform(UIUtils.getContext()))
                        .into(addressHolder.mIvAvatar);
            }else {
                Glide.with(UIUtils.getContext()).load(dataBean.getUSER_PHOTO())
                        .error(R.mipmap.headsq_pic)
                        .placeholder(R.mipmap.headsq_pic)
                        .transform(new CenterCrop(UIUtils.getContext()),new GlideRoundTransform(UIUtils.getContext()))
                        .into(addressHolder.mIvAvatar);
            }




            if (dataBean.getGENDER().equals("0")) {//保密
                addressHolder.mIvManOrWoman.setVisibility(View.GONE);
            } else if (dataBean.getGENDER().equals("1")) {
                Glide.with(UIUtils.getContext()).load(R.mipmap.men).into(addressHolder.mIvManOrWoman);
                addressHolder.mIvManOrWoman.setBackgroundColor(mActivity.getResources().getColor(R.color.man_back));
                addressHolder.mIvManOrWoman.setVisibility(View.VISIBLE);
            } else if (dataBean.getGENDER().equals("2")) {
                Glide.with(UIUtils.getContext()).load(R.mipmap.women).into(addressHolder.mIvManOrWoman);
                addressHolder.mIvManOrWoman.setBackgroundColor(mActivity.getResources().getColor(R.color.woman_back));
                addressHolder.mIvManOrWoman.setVisibility(View.VISIBLE);
            } else {
                addressHolder.mIvManOrWoman.setVisibility(View.GONE);
            }

            String userLevel = dataBean.getUserLevel();
            if (userLevel.equals("1")) {//普通
                Glide.with(UIUtils.getContext()).load(R.mipmap.general).into(addressHolder.mIvLevel);
            } else if (userLevel.equals("2")) {//白领
                Glide.with(UIUtils.getContext()).load(R.mipmap.white_collar).into(addressHolder.mIvLevel);
            } else if (userLevel.equals("3")) {//金领
                Glide.with(UIUtils.getContext()).load(R.mipmap.gold_collar).into(addressHolder.mIvLevel);
            } else if (userLevel.equals("4")) {//老板
                Glide.with(UIUtils.getContext()).load(R.mipmap.boss).into(addressHolder.mIvLevel);
            } else if (userLevel.equals("5")) {//土豪
                Glide.with(UIUtils.getContext()).load(R.mipmap.loacl_lord).into(addressHolder.mIvLevel);
            } else {
                Glide.with(UIUtils.getContext()).load(R.mipmap.general).into(addressHolder.mIvLevel);
            }


        }
    }

    @Override
    public int getItemCount() {
        return fatherArraylist.size() + 1;
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
    public int getItemViewType(int position) {
        if (position == fatherArraylist.size()) {
            return ITEM_TYPE_FOOT;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    class AddressContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyItemClickListener mItemClickListener;
        private TextView mTvLocation;
        private TextView mTvName;
        private TextView mTvSignature;
        private ImageView mIvManOrWoman;
        private ImageView mIvAvatar;
        private ImageView mIvLevel;

        public AddressContentViewHolder(View itemView, MyItemClickListener mItemClickListener) {
            super(itemView);
            this.mItemClickListener = mItemClickListener;
            mTvLocation = (TextView) itemView.findViewById(R.id.address_tv_location);
            mTvName = (TextView) itemView.findViewById(R.id.address_tv_name);
            mTvSignature = (TextView) itemView.findViewById(R.id.address_tv_des);
            mIvManOrWoman = (ImageView) itemView.findViewById(R.id.address_tv_gener);
            mIvLevel = (ImageView) itemView.findViewById(R.id.address_tv_level);
            mIvAvatar = (ImageView) itemView.findViewById(R.id.address_iv_avatar);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    class AddressFootViewHolder extends RecyclerView.ViewHolder {

        public AddressFootViewHolder(View itemView) {
            super(itemView);
        }
    }


}

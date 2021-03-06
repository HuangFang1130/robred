package com.jiahehongye.robred.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.easeui.domain.EaseUser;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.GrabRedActivity;
import com.jiahehongye.robred.bean.GrabMerchantListResult;
import com.jiahehongye.robred.db.Model;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by huangjunhui on 2016/12/2.16:03
 */
public class GrabMerchantRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<GrabMerchantListResult.DataBean.SendRedEnveListBean> fatherArrayList;
    private GrabRedActivity mActivity;
    //item类型
    public static final int ITEM_TYPE_CONTENT_OTHER = 1;
    public static final int ITEM_TYPE_FOOT = 2;
    public static final int ITEM_TYPE_CONTENT_SELF = 3;
    private View footView;
    private MyItemClickListener mItemClickListener;
    private final String nick;

    public GrabMerchantRecycleAdapter(FragmentActivity activity, List<GrabMerchantListResult.DataBean.SendRedEnveListBean> fatherArrayList) {
        this.mActivity = (GrabRedActivity) activity;
        this.fatherArrayList = fatherArrayList;

        String  id = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_ID, "");
        Model model = new Model(UIUtils.getContext());
        Map<String, EaseUser> contactList = model.getContactList();
        String phone = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_MOBILE, "");
        EaseUser easeUser = contactList.get(phone);
        nick = easeUser.getNick();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_CONTENT_OTHER:
                View contentView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_grabred_personal_content, null);
                return new GrabMerchantContentViewHolder(contentView, mItemClickListener);
            case ITEM_TYPE_CONTENT_SELF://自己的
                View contentSelfView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_grabred_merchant_content, null);
                return new GrabMerchantContentSelfViewHolder(contentSelfView, mItemClickListener);
            case ITEM_TYPE_FOOT:
                footView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_home_foot, null);
                hintFootView();
                return new GrabMerchantFootViewHolder(footView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GrabMerchantContentViewHolder) {
            GrabMerchantContentViewHolder contentHolder = (GrabMerchantContentViewHolder) holder;
            contentHolder.mTvGrabPersonalName.setText(fatherArrayList.get(position).getNickName());
            contentHolder.mTvGrabPersonalDes.setText(fatherArrayList.get(position).getRedEnveLeaveword());
            String user_level = fatherArrayList.get(position).getUSER_LEVEL();//用户的级别


            String userPhoto = fatherArrayList.get(position).getUserPhoto();
            if (userPhoto == null || userPhoto.equals("")) {
                Glide.with(UIUtils.getContext()).load(R.mipmap.headsq_pic).centerCrop().into(contentHolder.mIvGrabPersonalAvatar);
            } else {
                Glide.with(UIUtils.getContext()).load(fatherArrayList.get(position).getUserPhoto()).centerCrop().into(contentHolder.mIvGrabPersonalAvatar);

            }
            String redEnveMark = fatherArrayList.get(position).getRedEnveMark();
            if (redEnveMark.equals("1")) {
                contentHolder.mTvGrabPersonalState.setText("来得及");
            } else if (redEnveMark.equals("2")) {
                contentHolder.mTvGrabPersonalState.setText("来得及");
            } else if (redEnveMark.equals("3")) {
                contentHolder.mTvGrabPersonalState.setText("已领取");
            } else if (redEnveMark.equals("4")) {
                contentHolder.mTvGrabPersonalState.setText("来晚了");
            }
            switch (user_level) {
                case "1":
                    contentHolder.mIvGrabPersoanlLevel.setImageResource(R.mipmap.general);
                    break;
                case "2":
                    contentHolder.mIvGrabPersoanlLevel.setImageResource(R.mipmap.white_collar);
                    break;
                case "3":
                    contentHolder.mIvGrabPersoanlLevel.setImageResource(R.mipmap.gold_collar);
                    break;
                case "4":
                    contentHolder.mIvGrabPersoanlLevel.setImageResource(R.mipmap.boss);
                    break;
                case "5":
                    contentHolder.mIvGrabPersoanlLevel.setImageResource(R.mipmap.loacl_lord);
                    break;
            }

        }
        if (holder instanceof GrabMerchantContentSelfViewHolder) {
            GrabMerchantContentSelfViewHolder contentSelfHolder = (GrabMerchantContentSelfViewHolder) holder;
            contentSelfHolder.mTvGrabPersonalSelfName.setText(fatherArrayList.get(position).getNickName());
            contentSelfHolder.mTvGrabPersonalSelfDes.setText(fatherArrayList.get(position).getRedEnveLeaveword());
            String user_level = fatherArrayList.get(position).getUSER_LEVEL();//用户的级别
            String userPhoto = fatherArrayList.get(position).getUserPhoto();
            if (userPhoto == null || userPhoto.equals("")) {
                Glide.with(UIUtils.getContext()).load(R.mipmap.headsq_pic).centerCrop().into(contentSelfHolder.mIvGrabPersonalSelfAvatar);
            } else {
                Glide.with(UIUtils.getContext()).load(fatherArrayList.get(position).getUserPhoto()).centerCrop().into(contentSelfHolder.mIvGrabPersonalSelfAvatar);
            }
            String redEnveMark = fatherArrayList.get(position).getRedEnveMark();
            if (redEnveMark.equals("1")) {
                contentSelfHolder.mTvGrabPersonalSelfState.setText("来得及");
            } else if (redEnveMark.equals("2")) {
                contentSelfHolder.mTvGrabPersonalSelfState.setText("来得及");
            } else if (redEnveMark.equals("3")) {
                contentSelfHolder.mTvGrabPersonalSelfState.setText("已领取");
            } else if (redEnveMark.equals("4")) {
                contentSelfHolder.mTvGrabPersonalSelfState.setText("来晚了");
            }

            switch (user_level) {
                case "1":
                    contentSelfHolder.mIvGrabPersoanlSelfLevel.setImageResource(R.mipmap.general);
                    break;
                case "2":
                    contentSelfHolder.mIvGrabPersoanlSelfLevel.setImageResource(R.mipmap.white_collar);
                    break;
                case "3":
                    contentSelfHolder.mIvGrabPersoanlSelfLevel.setImageResource(R.mipmap.gold_collar);
                    break;
                case "4":
                    contentSelfHolder.mIvGrabPersoanlSelfLevel.setImageResource(R.mipmap.boss);
                    break;
                case "5":
                    contentSelfHolder.mIvGrabPersoanlSelfLevel.setImageResource(R.mipmap.loacl_lord);
                    break;
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

    @Override
    public int getItemCount() {
        if (fatherArrayList != null && fatherArrayList.size() > 0) {
            return fatherArrayList.size() + 1;
        } else {
            return 1;
        }
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    @Override
    public int getItemViewType(int position) {


        if (position == fatherArrayList.size()) {
            return ITEM_TYPE_FOOT;
        } else {



            if (fatherArrayList.get(position).getNickName().equals(nick)) {//
                return ITEM_TYPE_CONTENT_SELF;
            }else{
                return  ITEM_TYPE_CONTENT_OTHER;
            }
        }
    }

    class GrabMerchantContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyItemClickListener mItemClickListener;
        private ImageView mIvGrabPersonalAvatar;
        private TextView mTvGrabPersonalName;
        private TextView mTvGrabPersonalDes;
        private TextView mTvGrabPersonalState;
        private ImageView mIvGrabPersoanlLevel;

        public GrabMerchantContentViewHolder(View itemView, MyItemClickListener mItemClickListener) {
            super(itemView);
            this.mItemClickListener = mItemClickListener;
            itemView.setOnClickListener(this);

            mIvGrabPersonalAvatar = (ImageView) itemView.findViewById(R.id.grabred_personal_avatar);
            mTvGrabPersonalName = (TextView) itemView.findViewById(R.id.grabred_personal_name);
            mTvGrabPersonalDes = (TextView) itemView.findViewById(R.id.grabred_personal_des);
            mTvGrabPersonalState = (TextView) itemView.findViewById(R.id.grabred_personal_state);
            mIvGrabPersoanlLevel = (ImageView) itemView.findViewById(R.id.grabred_personal_level);

        }


        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    class GrabMerchantContentSelfViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mItemClickListener;
        private ImageView mIvGrabPersonalSelfAvatar;
        private TextView mTvGrabPersonalSelfName;
        private TextView mTvGrabPersonalSelfDes;
        private TextView mTvGrabPersonalSelfState;
        private ImageView mIvGrabPersoanlSelfLevel;

        public GrabMerchantContentSelfViewHolder(View itemView, MyItemClickListener mItemClickListener) {
            super(itemView);
            this.mItemClickListener = mItemClickListener;
            itemView.setOnClickListener(this);
            mIvGrabPersonalSelfAvatar = (ImageView) itemView.findViewById(R.id.grabred_personal_self_avatar);
            mTvGrabPersonalSelfName = (TextView) itemView.findViewById(R.id.grabred_personal_self_name);
            mTvGrabPersonalSelfDes = (TextView) itemView.findViewById(R.id.grabred_personal_self_des);
            mTvGrabPersonalSelfState = (TextView) itemView.findViewById(R.id.grabred_personal_self_state);
            mIvGrabPersoanlSelfLevel = (ImageView) itemView.findViewById(R.id.grabred_personal_self_level);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    class GrabMerchantFootViewHolder extends RecyclerView.ViewHolder {
        public GrabMerchantFootViewHolder(View itemView) {
            super(itemView);
        }
    }
}

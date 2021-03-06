package com.jiahehongye.robred.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.BillionairesActivity;
import com.jiahehongye.robred.bean.BillionairesResult;
import com.jiahehongye.robred.interfaces.MyHeadViewClickListener;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.DensityUtil;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.GlideCircleTransform;

import java.util.List;

/**
 * Created by huangjunhui on 2016/12/2.
 */
public class BillionairesRecycleAdapter extends RecyclerView.Adapter<ViewHolder> {
    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_FOOT = 2;
    private List<BillionairesResult.DataBean.RichLBean> fatherArrayList;
    private BillionairesActivity mBillionActivity;
    private View footView;
    private int i = 0;
    private MyItemClickListener mItemClickListener;
    private String personalRank="";//个人排名
    private MyHeadViewClickListener mHeadClickListener;


    public BillionairesRecycleAdapter(BillionairesActivity billionairesActivity, List<BillionairesResult.DataBean.RichLBean> fathreArrayList) {
        this.mBillionActivity = billionairesActivity;
        this.fatherArrayList = fathreArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_HEADER:
                View headView = LayoutInflater.from(mBillionActivity).inflate(R.layout.billionaires_head, null);
                return new BillionHeadViewHolder(headView,mHeadClickListener);
            case ITEM_TYPE_CONTENT:
                View contentView = LayoutInflater.from(mBillionActivity).inflate(R.layout.billionaires_content, null);
                return new BillionContentViewHolder(contentView, mItemClickListener);
            case ITEM_TYPE_FOOT:
                footView = LayoutInflater.from(mBillionActivity).inflate(R.layout.fragment_home_foot, null);
                hintFootView();
                return new BillionFootViewHolder(footView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (fatherArrayList != null) {
            if (holder instanceof BillionHeadViewHolder) {

                int size = fatherArrayList.size();

                BillionHeadViewHolder heardHolder = (BillionHeadViewHolder) holder;

                heardHolder.mTvPersonalAwarder.setText(personalRank);
                heardHolder.mTvAwardedDD.setVisibility(View.VISIBLE);
                heardHolder.mTvPersonalAwarder.setTextSize(DensityUtil.dip2px(UIUtils.getContext(), 20));
                if(personalRank!=null){
                    if (personalRank.equals("暂无排名")) {
                        heardHolder.mTvAwardedDD.setVisibility(View.GONE);
                        heardHolder.mTvPersonalAwarder.setTextSize(DensityUtil.dip2px(UIUtils.getContext(), 10));
                    }
                }

                if(size>=1){
                    if(fatherArrayList.get(0).getUserPhoto().equals("")||fatherArrayList.get(0).getUserPhoto()==null){
                        Glide.with(UIUtils.getContext()).load(R.mipmap.avatar).transform(new GlideCircleTransform(UIUtils.getContext())).into(heardHolder.mIvPaiMingOne);
                    }else {
                        Glide.with(UIUtils.getContext()).load(fatherArrayList.get(0).getUserPhoto())
                                .error(R.mipmap.avatar)
                                .placeholder(R.mipmap.avatar).transform(new GlideCircleTransform(UIUtils.getContext())).into(heardHolder.mIvPaiMingOne);
                    }

                    heardHolder.mTvPaiMingOne.setText(fatherArrayList.get(0).getNickName());
                    heardHolder.mTvPaiMingMoneyOne.setText("￥ " + fatherArrayList.get(0).getSendMoney());
                }
                if(size>=2){
                    if(fatherArrayList.get(1).getUserPhoto().equals("")||fatherArrayList.get(1).getUserPhoto()==null){
                        Glide.with(UIUtils.getContext()).load(R.mipmap.avatar).transform(new GlideCircleTransform(UIUtils.getContext())).into(heardHolder.mIvPaiMingTwo);
                    }else {
                        Glide.with(UIUtils.getContext()).load(fatherArrayList.get(1).getUserPhoto())
                                .error(R.mipmap.avatar)
                                .placeholder(R.mipmap.avatar).transform(new GlideCircleTransform(UIUtils.getContext())).into(heardHolder.mIvPaiMingTwo);
                    }

                    heardHolder.mTvPaiMingTwo.setText(fatherArrayList.get(1).getNickName());

                    heardHolder.mTvPaiMingMoneyTwo.setText("￥ " + fatherArrayList.get(1).getSendMoney());
                }

               if(size>=3){
                   if(fatherArrayList.get(2).getUserPhoto().equals("")||fatherArrayList.get(2).getUserPhoto()==null){
                       Glide.with(UIUtils.getContext()).load(R.mipmap.avatar).transform(new GlideCircleTransform(UIUtils.getContext())).into(heardHolder.mIvPaiMingThree);
                   }else {
                       Glide.with(UIUtils.getContext()).load(fatherArrayList.get(2).getUserPhoto())
                               .error(R.mipmap.avatar)
                               .placeholder(R.mipmap.avatar).transform(new GlideCircleTransform(UIUtils.getContext())).into(heardHolder.mIvPaiMingThree);
                   }

                   heardHolder.mTvPaiMingThree.setText(fatherArrayList.get(2).getNickName());

                   heardHolder.mTvPaiMingMoneyThree.setText("￥ " + fatherArrayList.get(2).getSendMoney());
               }



            }

            if (holder instanceof BillionContentViewHolder) {
                BillionContentViewHolder contentHolder = (BillionContentViewHolder) holder;
                String userPhoto = fatherArrayList.get(position + 2).getUserPhoto();
                if (userPhoto == null || userPhoto.equals("")) {
                    Glide.with(UIUtils.getContext()).load(R.mipmap.avatar).into(contentHolder.mIvContentAvatar);
                } else {
                    Glide.with(UIUtils.getContext()).load(fatherArrayList.get(position + 2).getUserPhoto())
                            .error(R.mipmap.avatar)
                            .placeholder(R.mipmap.avatar).transform(new GlideCircleTransform(UIUtils.getContext())).into(contentHolder.mIvContentAvatar);
                }
                i = position+3;
                contentHolder.mTvContentNumber.setText("No."+i + "");
                contentHolder.mTvContentName.setText(fatherArrayList.get(position + 2).getNickName());
                contentHolder.mTvContentMoney.setText("￥ " + fatherArrayList.get(position + 2).getSendMoney());
            }
        }

    }


    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public void setOnHeadClickListener(MyHeadViewClickListener listener) {
        this.mHeadClickListener = listener;
    }


    @Override
    public int getItemCount() {
        if(fatherArrayList.size()<=3){
            return 2;
        }
        return fatherArrayList.size() - 1;
    }

    @Override
    public int getItemViewType(int position) {
        if ( fatherArrayList.size()<=3) {
            if(position==0){
                return ITEM_TYPE_HEADER;
            }else if(position==1){
                return ITEM_TYPE_FOOT;
            }
        } else{
            if (position == 0) {
                return ITEM_TYPE_HEADER;
            } else if (position == fatherArrayList.size() - 2)  {
                return ITEM_TYPE_FOOT;
            } else {
                return ITEM_TYPE_CONTENT;
            }
        }
        return ITEM_TYPE_FOOT;


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

    public void setHeardDate(String personalRank) {
        this.personalRank = personalRank;
    }

    class BillionHeadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyHeadViewClickListener mHeadClickListener;
        private TextView mTvPersonalAwarder;
        private TextView mTvPaiMingOne;
        private TextView mTvPaiMingMoneyOne;
        private ImageView mIvPaiMingTwo;
        private TextView mTvPaiMingTwo;
        private TextView mTvPaiMingMoneyTwo;
        private ImageView mIvPaiMingThree;
        private TextView mTvPaiMingThree;
        private ImageView mIvPaiMingOne;
        private TextView mTvPaiMingMoneyThree;
        private TextView mTvAwardedDD;

        public BillionHeadViewHolder(View itemView, MyHeadViewClickListener mHeadClickListener) {
            super(itemView);
            this.mHeadClickListener = mHeadClickListener;
            //我的排名
            mTvAwardedDD = (TextView) itemView.findViewById(R.id.billion_my_awarded_dd);

            mTvPersonalAwarder = (TextView) itemView.findViewById(R.id.billion_my_awarded);
            mIvPaiMingOne = (ImageView) itemView.findViewById(R.id.billion_paiming_avatar_one);
            mTvPaiMingOne = (TextView) itemView.findViewById(R.id.billion_one_user_name);
            mTvPaiMingMoneyOne = (TextView) itemView.findViewById(R.id.billion_paiming_money_one);

            mIvPaiMingTwo = (ImageView) itemView.findViewById(R.id.billion_paiming_avatar_two);
            mTvPaiMingTwo = (TextView) itemView.findViewById(R.id.billion_two_user_name);
            mTvPaiMingMoneyTwo = (TextView) itemView.findViewById(R.id.billion_paiming_money_two);

            mIvPaiMingThree = (ImageView) itemView.findViewById(R.id.billion_paiming_avatar_three);
            mTvPaiMingThree = (TextView) itemView.findViewById(R.id.billion_three_user_name);
            mTvPaiMingMoneyThree = (TextView) itemView.findViewById(R.id.billion_paiming_money_three);


            RelativeLayout mRlThree= (RelativeLayout) itemView.findViewById(R.id.billion_head_rl_three);
            RelativeLayout mRlOne= (RelativeLayout) itemView.findViewById(R.id.billion_head_rl_one);
            RelativeLayout mRlTwo= (RelativeLayout) itemView.findViewById(R.id.billion_head_rl_two);

            mRlThree.setOnClickListener(this);
            mRlOne.setOnClickListener(this);
            mRlTwo.setOnClickListener(this);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(mHeadClickListener!=null){
                mHeadClickListener.onHeadClick(v);
            }
        }
    }

    class BillionContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyItemClickListener itemClickListener;
        private TextView mTvContentNumber;
        private ImageView mIvContentAvatar;
        private TextView mTvContentName;
        private TextView mTvContentMoney;

        public BillionContentViewHolder(View itemView, MyItemClickListener mItemClickListener) {
            super(itemView);
            this.itemClickListener = mItemClickListener;
            itemView.setOnClickListener(this);
            //序号
            mTvContentNumber = (TextView) itemView.findViewById(R.id.billion_tv_number);
            mTvContentName = (TextView) itemView.findViewById(R.id.billion_tv_name);
            mTvContentMoney = (TextView) itemView.findViewById(R.id.billion_tv_money);
            mIvContentAvatar = (ImageView) itemView.findViewById(R.id.billion_iv_avatar);

        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    class BillionFootViewHolder extends RecyclerView.ViewHolder {

        public BillionFootViewHolder(View itemView) {
            super(itemView);
        }
    }
}

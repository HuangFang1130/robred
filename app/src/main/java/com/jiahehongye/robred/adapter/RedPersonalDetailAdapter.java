package com.jiahehongye.robred.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobads.AdView;
import com.bumptech.glide.Glide;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.RedDetailActivity;
import com.jiahehongye.robred.bean.DFTTNewsResult;
import com.jiahehongye.robred.bean.GrabDetailPeopleDetailResult;
import com.jiahehongye.robred.bean.GrabDetailPeopleListResult;
import com.jiahehongye.robred.interfaces.MyHeadViewClickListener;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.LogUtil;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjunhui on 2016/12/2.18:24
 */
public class RedPersonalDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_FOOT = 2;
    private MyHeadViewClickListener mHeadClickListener;
    private MyItemClickListener mItemClickListener;

    private List<GrabDetailPeopleListResult.DataBean.GrabRedEnveListBean> fatherArrayList;
    private RedDetailActivity mActivity;
    private View footView;
    private GrabDetailPeopleDetailResult grabDetailPeopleDetailResult;
    private String personalOrMarchant;//个人或者商家 0：个人 1：商家
    private String redEnveCode;
    private String redEnveMark;//标记红包当前的状态
    private List<DFTTNewsResult.DataBean> bannerdata = new ArrayList<>();
    private AdView bannerAdView;
    private final int widthPixels;

    public RedPersonalDetailAdapter(RedDetailActivity redPersonalDetailActivity, List<GrabDetailPeopleListResult.DataBean.GrabRedEnveListBean> fatherArrayList) {

        this.mActivity = redPersonalDetailActivity;
        this.fatherArrayList = fatherArrayList;

        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        widthPixels = mActivity.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_HEADER:
                View headView = LayoutInflater.from(mActivity).inflate(R.layout.activity_reddetail_head, null);
                return new RedDetailHeadViewHolder(headView, mHeadClickListener);
            case ITEM_TYPE_CONTENT:
                View contentView = LayoutInflater.from(mActivity).inflate(R.layout.red_detail_content, null);
                return new RedDetailContentViewHolder(contentView, mItemClickListener);
            case ITEM_TYPE_FOOT:
                footView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_home_foot, null);
                hintFootView();
                return new RedDetailFootViewHolder(footView);
        }
        return null;

    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnHeadClickListener(MyHeadViewClickListener listener) {
        this.mHeadClickListener = listener;
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
        if (holder instanceof RedDetailHeadViewHolder) {
            RedDetailHeadViewHolder heardHolder = (RedDetailHeadViewHolder) holder;
            if (grabDetailPeopleDetailResult != null) {
                GrabDetailPeopleDetailResult.DataLBean.SendRedEnveLBean sendRedEnve = grabDetailPeopleDetailResult.getData().getSendRedEnve();
                GrabDetailPeopleDetailResult.DataLBean.CurrentGrabRedEnveLBean currentGrabRedEnve = grabDetailPeopleDetailResult.getData().getCurrentGrabRedEnve();
                List<GrabDetailPeopleDetailResult.DataLBean.MemberLBean> member = grabDetailPeopleDetailResult.getData().getMember();
                String userMobile = member.get(0).getMobile();
                String nickName = member.get(0).getNickName();
                String personalDescription = member.get(0).getPersonalDescription();
                String userLevel = member.get(0).getUserLevel();
                String userPhoto = member.get(0).getUserPhoto();

                if (bannerdata != null) {
                    if (bannerdata.size() > 0) {

                        if (bannerdata.size() > 0 || bannerdata.get(0).getMiniimg().size() > 0) {

                            Glide.with(UIUtils.getContext()).load(bannerdata.get(0).getMiniimg().get(0).getSrc()).into(heardHolder.DF_adv);
                        }
                    }
                } else {
                    heardHolder.DF_adv.setVisibility(View.GONE);
                }


                heardHolder.mTvRedDetailName.setText(nickName);
                heardHolder.mTvRedDetailDes.setText(personalDescription);

                switch (userLevel) {
                    case "1":
                        heardHolder.mIvRedDetailLevel.setImageResource(R.mipmap.general);
                        break;
                    case "2":
                        heardHolder.mIvRedDetailLevel.setImageResource(R.mipmap.white_collar);
                        break;
                    case "3":
                        heardHolder.mIvRedDetailLevel.setImageResource(R.mipmap.gold_collar);
                        break;
                    case "4":
                        heardHolder.mIvRedDetailLevel.setImageResource(R.mipmap.boss);
                        break;
                    case "5":
                        heardHolder.mIvRedDetailLevel.setImageResource(R.mipmap.loacl_lord);
                        break;
                    default:
                        heardHolder.mIvRedDetailLevel.setVisibility(View.GONE);
                        break;
                }


                Glide.with(UIUtils.getContext()).load(userPhoto)
                        .error(R.mipmap.avatar)
                        .placeholder(R.mipmap.avatar)
                        .into(heardHolder.mIvRedDetailAvatar);


                if (redEnveMark.equals("1")) {
                    if (currentGrabRedEnve != null) {
                        String grabMoney = currentGrabRedEnve.getGrabMoney();//这是自己抢的红包
                        heardHolder.mTvRedDetailMoney.setText(grabMoney + "元");
                    }
                } else if (redEnveMark.equals("2")) {//来得及
                    if (currentGrabRedEnve != null) {

                        String grabMoney = currentGrabRedEnve.getGrabMoney();//这是自己抢的红包
                        heardHolder.mTvRedDetailMoney.setText(grabMoney + "元");
                    }
                } else if (redEnveMark.equals("3")) {//已领取
                    if (currentGrabRedEnve != null) {

                        String grabMoney = currentGrabRedEnve.getGrabMoney();//这是自己抢的红包
                        heardHolder.mTvRedDetailMoney.setText(grabMoney + "元");
                    }
                } else if (redEnveMark.equals("4")) {//来晚了

                }
                String remainRedEnveSum = sendRedEnve.getRemainRedEnveSum();//剩余多少个人
                String grabedRedEnveSum = sendRedEnve.getGrabedRedEnveSum();//抢了多少个
                String redEnveNum = sendRedEnve.getRedEnveNum();//一共多少个

                heardHolder.mTvRedDetailPeople.setText(grabedRedEnveSum + "/" + redEnveNum);

                if (personalOrMarchant.equals("1")) {//商家
                    Glide.with(UIUtils.getContext()).load(sendRedEnve.getGrabRedEnveAd()).into(heardHolder.mIvRedDetailBig);
                    heardHolder.mTvRedDetailContent.setText(sendRedEnve.getRedIntroduction());
                }
            }
        }


        if (holder instanceof RedDetailContentViewHolder) {
            RedDetailContentViewHolder contentHolder = (RedDetailContentViewHolder) holder;
            contentHolder.mRedTvPeopleName.setText(fatherArrayList.get(position - 1).getNickName());
            contentHolder.mRedTvPeopleDate.setText(fatherArrayList.get(position - 1).getGrabDate());
            contentHolder.mRedTvPeopleMoney.setText(fatherArrayList.get(position - 1).getGrabMoney() + " 元");
            String userPhoto = fatherArrayList.get(position - 1).getUserPhoto();

            Glide.with(UIUtils.getContext()).load(fatherArrayList.get(position - 1).getUserPhoto())
                    .placeholder(R.mipmap.avatar)
                    .transform(new GlideCircleTransform(UIUtils.getContext())).error(R.mipmap.avatar).into(contentHolder.mRedIvPeopleAvatar);


        }
    }

    @Override
    public int getItemCount() {
        if (fatherArrayList != null && fatherArrayList.size() > 0) {
            return fatherArrayList.size() + 2;
        } else {
            return 1;
        }
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
            return ITEM_TYPE_CONTENT;
        }
    }

    public void setAdv(List<DFTTNewsResult.DataBean> bannerdata) {
        this.bannerdata = bannerdata;
    }



    public void setHeadDate(GrabDetailPeopleDetailResult grabDetailPeopleDetailResult, String type, String redEnveCode, String redEnveMark
    ,AdView adView) {
        this.grabDetailPeopleDetailResult = grabDetailPeopleDetailResult;
        this.personalOrMarchant = type;//个人红包还是商家红包
        this.redEnveCode = redEnveCode;
        this.redEnveMark = redEnveMark;
        this.bannerAdView = adView;
    }

    public void setHengFuBanner(AdView adView) {

    }

    class RedDetailContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyItemClickListener mItemClickListener;
        private ImageView mRedIvPeopleAvatar;
        private TextView mRedTvPeopleName;
        private TextView mRedTvPeopleDate;
        private TextView mRedTvPeopleMoney;

        public RedDetailContentViewHolder(View itemView, MyItemClickListener mItemClickListener) {
            super(itemView);
            this.mItemClickListener = mItemClickListener;
            itemView.setOnClickListener(this);

            mRedIvPeopleAvatar = (ImageView) itemView.findViewById(R.id.red_detail_people_avatar);
            mRedTvPeopleName = (TextView) itemView.findViewById(R.id.red_detail_people_name);
            mRedTvPeopleDate = (TextView) itemView.findViewById(R.id.red_detail_people_date);
            mRedTvPeopleMoney = (TextView) itemView.findViewById(R.id.red_detail_people_money);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    class RedDetailFootViewHolder extends RecyclerView.ViewHolder {


        public RedDetailFootViewHolder(View itemView) {
            super(itemView);
        }
    }

    class RedDetailHeadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private MyHeadViewClickListener mHeadClickListener;
        private ImageView mIvRedDetailAvatar;
        private TextView mTvRedDetailName;
        private TextView mTvRedDetailDes;
        private ImageView mIvRedDetailLevel;
        private TextView mTvRedDetailChat;
        private TextView mTvRedDetailMoney;
        private TextView mTvRedDetailLuck;
        private TextView mTvRedDetailLook;
        private RelativeLayout mRlRedDetailMerchant;
        private TextView mTvRedDetailLeat;

        private TextView mTvRedDetailPeople;
        private ImageView mIvRedDetailBig;
        private TextView mTvRedDetailContent;
        private ImageView mIvRedDetailDownRow;
        private ImageView DF_adv;
        private RelativeLayout mRlParent;

        public RedDetailHeadViewHolder(View itemView, MyHeadViewClickListener mHeadClickListener) {
            super(itemView);
            this.mHeadClickListener = mHeadClickListener;
            itemView.setOnClickListener(this);

            LinearLayout mRlRedDetail = (LinearLayout) itemView.findViewById(R.id.red_detail_rl_chat);
            mIvRedDetailAvatar = (ImageView) itemView.findViewById(R.id.red_detail_iv_avatar);
            mTvRedDetailName = (TextView) itemView.findViewById(R.id.red_detail_tv_name);
            mTvRedDetailDes = (TextView) itemView.findViewById(R.id.red_detail_tv_des);
            mIvRedDetailLevel = (ImageView) itemView.findViewById(R.id.red_detail_iv_level);
            mTvRedDetailChat = (TextView) itemView.findViewById(R.id.red_detail_tv_chat);
            mTvRedDetailLuck = (TextView) itemView.findViewById(R.id.red_detail_tv_luck);
            mTvRedDetailMoney = (TextView) itemView.findViewById(R.id.red_detail_tv_money);
            mTvRedDetailLook = (TextView) itemView.findViewById(R.id.red_detail_tv_look);
            mTvRedDetailPeople = (TextView) itemView.findViewById(R.id.red_detail_tv_number);
            mRlRedDetailMerchant = (RelativeLayout) itemView.findViewById(R.id.record_detail_rl_top);
            mIvRedDetailBig = (ImageView) itemView.findViewById(R.id.record_detail_image);
            mTvRedDetailContent = (TextView) itemView.findViewById(R.id.record_detail_tv_content);
            mTvRedDetailLeat = (TextView) itemView.findViewById(R.id.red_detail_tv_leat);
            mIvRedDetailDownRow = (ImageView) itemView.findViewById(R.id.record_detail_iv_down_right);
            DF_adv = (ImageView) itemView.findViewById(R.id.DF_adv);

            mRlParent = (RelativeLayout) itemView.findViewById(R.id.rl_banner_detail);

//            ViewGroup.LayoutParams layoutParams = mRlParent.getLayoutParams();
//            RelativeLayout ss = (RelativeLayout) findViewById(R.id.myRelativeLayout);

//            int widthPixels1 = mActivity.getResources().getDisplayMetrics().widthPixels;
//
            int height = ((widthPixels) / 7);
            LogUtil.LogShitou("banner 的高：  ",height+"");
            mRlParent.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,height ));

            if(bannerdata!=null){
                mRlParent.setVisibility(View.GONE);
                DF_adv.setVisibility(View.VISIBLE);
            }

            if(bannerAdView!=null){
                mRlParent.setVisibility(View.VISIBLE);
                DF_adv.setVisibility(View.GONE);
//                ViewGroup p = (ViewGroup) mRlParent.getParent();
//                if (p != null) {
//                    p.removeAllViewsInLayout();
//                }

//                RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
//                        RelativeLayout.LayoutParams.WRAP_CONTENT);
//                rllp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                mRlParent.addView(mRlParent,rllp);


//                ViewGroup.LayoutParams layoutParams = mRlParent.getLayoutParams();
//                layoutParams.height =50;
//                mRlParent.setLayoutParams(layoutParams);
                mRlParent.addView(bannerAdView);
            }

            DF_adv.setOnClickListener(this);
            mTvRedDetailChat.setOnClickListener(this);
            mTvRedDetailLook.setOnClickListener(this);
            mRlRedDetail.setOnClickListener(this);
            mIvRedDetailDownRow.setOnClickListener(this);
            if (grabDetailPeopleDetailResult != null) {

                if (personalOrMarchant.equals("0")) {
                    mRlRedDetailMerchant.setVisibility(View.GONE);
                } else if (personalOrMarchant.equals("1")) {
                    mRlRedDetailMerchant.setVisibility(View.VISIBLE);
                }

                if (redEnveMark.equals("1")) {
                    mTvRedDetailLeat.setVisibility(View.GONE);
                    mTvRedDetailLuck.setVisibility(View.VISIBLE);
                    mTvRedDetailMoney.setVisibility(View.VISIBLE);
                    mTvRedDetailLook.setVisibility(View.VISIBLE);
                } else if (redEnveMark.equals("2")) {//来得及
                    mTvRedDetailLeat.setVisibility(View.GONE);
                    mTvRedDetailLuck.setVisibility(View.VISIBLE);
                    mTvRedDetailMoney.setVisibility(View.VISIBLE);
                    mTvRedDetailLook.setVisibility(View.VISIBLE);
                } else if (redEnveMark.equals("3")) {//已领取
                    mTvRedDetailLeat.setVisibility(View.GONE);
                    mTvRedDetailLuck.setVisibility(View.VISIBLE);
                    mTvRedDetailMoney.setVisibility(View.VISIBLE);
                    mTvRedDetailLook.setVisibility(View.VISIBLE);

                } else if (redEnveMark.equals("4")) {//来晚了
                    mTvRedDetailLeat.setVisibility(View.VISIBLE);
                    mTvRedDetailLuck.setVisibility(View.INVISIBLE);
                    mTvRedDetailMoney.setVisibility(View.INVISIBLE);
                    mTvRedDetailLook.setVisibility(View.INVISIBLE);

                }

            }
        }

        @Override
        public void onClick(View v) {
            if (mHeadClickListener != null) {
                mHeadClickListener.onHeadClick(v);
            }
        }
    }


}

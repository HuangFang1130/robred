package com.jiahehongye.robred.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.easeui.domain.EaseUser;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.MainActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.FreshNewsDetailActivity;
import com.jiahehongye.robred.activity.WebActivity;
import com.jiahehongye.robred.bean.HomeFragmentResult;
import com.jiahehongye.robred.bean.HomeYIyuanGouResult;
import com.jiahehongye.robred.db.Model;
import com.jiahehongye.robred.interfaces.MyHeadViewClickListener;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.DensityUtil;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.CustomRollingView;
import com.jiahehongye.robred.view.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
public class HomeRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public CustomRollingView hRollingView;
    private HomeFragmentResult homeFragmentResult;
    private int mPointDis;

    private MyHeadViewClickListener mHeadClickListener;
    private MyItemClickListener mItemClickListener;
    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_FOOT = 2;

    private MainActivity mMainUi;

    private List<HomeYIyuanGouResult.DataBean.ResProductListBean> fatherArraylist = null;
    private ArrayList<ImageView> imageViews;
    private Handler handler;
    private View footView;
    private List<HomeFragmentResult.DataBean.BannerLBean> bannerList;//轮播图

    private String userPhoto;
    private String userName;
    private String userLevel;
    private String flower;
    private String diamond;
    private List<HomeFragmentResult.DataBean.InforLBean> inforList;//社区热点新闻


    public HomeRecycleAdapter(MainActivity mMainUi, List<HomeYIyuanGouResult.DataBean.ResProductListBean> fatherListDate) {
        this.mMainUi = mMainUi;
        this.fatherArraylist = fatherListDate;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case ITEM_TYPE_HEADER:
                View headView = LayoutInflater.from(mMainUi).inflate(R.layout.fragment_home_head, null);
                return new HomeHeadViewHolder(headView, mHeadClickListener);
            case ITEM_TYPE_CONTENT:
                View contentView = LayoutInflater.from(mMainUi).inflate(R.layout.fragment_home_content, null);
                return new HomeContentViewHolder(contentView, mItemClickListener);
            case ITEM_TYPE_FOOT:
                footView = LayoutInflater.from(mMainUi).inflate(R.layout.fragment_home_foot, null);
                hintFootView();
                return new HomeFootViewHolder(footView);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof HomeContentViewHolder){
            HomeContentViewHolder contentViewHolder = (HomeContentViewHolder) holder;
            Glide.with(UIUtils.getContext()).load(fatherArraylist.get(position-1).getImage()).into(contentViewHolder.hContentIvIcon);

            contentViewHolder.hContentTvName.setText(fatherArraylist.get(position - 1).getName());
            contentViewHolder.hContentTvAllNumber.setText(fatherArraylist.get(position - 1).getTotalPeople());
            contentViewHolder.hContentTvResiduerNumber.setText(fatherArraylist.get(position - 1).getSurplusPeople());
            int max = Integer.parseInt(fatherArraylist.get(position - 1).getTotalPeople());
            String surplusPeople = fatherArraylist.get(position - 1).getSurplusPeople();
            int surplus = Integer.parseInt(surplusPeople);
            contentViewHolder.hContentPb.setMax(max);
            contentViewHolder.hContentPb.setProgress(max-surplus);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() == 1) {
            return ITEM_TYPE_HEADER;
        }
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        } else if (position == fatherArraylist.size() + 1) {
            return ITEM_TYPE_FOOT;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public int getItemCount() {
        if (fatherArraylist != null && fatherArraylist.size() > 0) {
            return fatherArraylist.size() + 2;
        } else {
            return 1;
        }
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

    /**
     * 设置头部数据
     * @param homeFragmentResult
     */
    public void setHeardDate(HomeFragmentResult homeFragmentResult) {
       this.homeFragmentResult = homeFragmentResult;
        if(homeFragmentResult!=null){
            HomeFragmentResult.DataBean data = homeFragmentResult.getData();
            if(data!=null){
                bannerList = homeFragmentResult.getData().getBannerL();//banner图
                //钻石数
                diamond = homeFragmentResult.getData().getDiamond();
                //鲜花数
                flower = homeFragmentResult.getData().getFlower();
                //1：普通2：白领3：金领4：老板5：土豪
                userLevel = homeFragmentResult.getData().getUserLevel();
                //用户姓名
                userName = homeFragmentResult.getData().getUserName();
                //用户头像
                userPhoto = homeFragmentResult.getData().getUserPhoto();
                //社区热点列表
                inforList = homeFragmentResult.getData().getInforL();

                //保存用户信息到本地数据库中
                //保存到数据库中
                EaseUser user = new EaseUser((String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_MOBILE,""));
                user.setNick(userName);
                user.setAvatar(userPhoto);
                Model model = new Model(UIUtils.getContext());
                model.saveContact(user);
            }




        }
    }


    /**
     * 内容布局的viewholder
     */
    class HomeContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyItemClickListener mItemClickListener;
        private ImageView hContentIvIcon;
        private ProgressBar hContentPb;
        private TextView hContentTvName;
        private TextView hContentTvAllNumber;
        private TextView hContentTvResiduerNumber;

        public HomeContentViewHolder(View contentView, MyItemClickListener mItemClickListener) {
            super(contentView);
            this.mItemClickListener = mItemClickListener;
            hContentIvIcon = (ImageView) contentView.findViewById(R.id.home_content_iv_icon);
            hContentPb = (ProgressBar) contentView.findViewById(R.id.home_content_pb);
            hContentTvName = (TextView) contentView.findViewById(R.id.home_content_tv_name);
            hContentTvAllNumber = (TextView) contentView.findViewById(R.id.home_content_tv_allnumber);
            hContentTvResiduerNumber = (TextView) contentView.findViewById(R.id.home_content_tv_residuenumber);

            contentView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }


    }

    /**
     * 头布局 的 viewholder
     */
    class HomeHeadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ViewPager hVpHeadCarosel;
        private MyHeadViewClickListener mHeadClickListener;
        private LinearLayout hLLContener;
        private ImageView hIvRedPoint;
        private ImageView hIvAvatar;
        private TextView hTvName;
        private TextView hTvMasonry;
        private TextView hTvFlower;
        private ImageView hIvLevel;

        private void registerBroadcastReceiver() {
            LocalBroadcastManager  broadcastManager = LocalBroadcastManager.getInstance(mMainUi);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constant.ACTION_AVATAR_CHANAGED);
            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    String  mobile= (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_MOBILE, "");
                    Model model = new Model(UIUtils.getContext());
                    EaseUser user = model.getContactList().get(mobile);
                    userPhoto = user.getAvatar();
                    userName = user.getNick();
                    if(hIvAvatar!=null){
                        Glide.with(UIUtils.getContext()).load(userPhoto)
                                .bitmapTransform(new GlideCircleTransform(UIUtils.getContext())).into(hIvAvatar);

                        hTvName.setText(userName);
                    }


                }
            };
            broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
        }
        public HomeHeadViewHolder(View headView, MyHeadViewClickListener clicklistener) {
            super(headView);
            this.mHeadClickListener = clicklistener;
            registerBroadcastReceiver();
            RadioButton hRbGrab = (RadioButton) headView.findViewById(R.id.home_rb_grab);
            RadioButton hRbSend = (RadioButton) headView.findViewById(R.id.home_rb_send);
            RadioButton hRbFindFavorable = (RadioButton) headView.findViewById(R.id.home_rb_find_favorable);
            RadioButton hRbSpellLuck = (RadioButton) headView.findViewById(R.id.home_rb_spell_luck);

            hRbGrab.setOnClickListener(this);
            hRbSend.setOnClickListener(this);
            hRbFindFavorable.setOnClickListener(this);
            hRbSpellLuck.setOnClickListener(this);
            RelativeLayout hRlPersonalInformation = (RelativeLayout) headView.findViewById(R.id.home_rl_personal_information);
            hRlPersonalInformation.setOnClickListener(this);

            if(homeFragmentResult==null){
                return;
            }
            hVpHeadCarosel = (ViewPager) headView.findViewById(R.id.home_vp_carousel);
            hLLContener = (LinearLayout) headView.findViewById(R.id.home_ll_container);
            hIvRedPoint = (ImageView) headView.findViewById(R.id.home_iv_redpoint);
            hRollingView = (CustomRollingView) headView.findViewById(R.id.home_head_rollingview);

            hIvAvatar = (ImageView) headView.findViewById(R.id.home_iv_avatar);//用户头像
            hTvName = (TextView) headView.findViewById(R.id.home_tv_name); //用户名字
            hTvMasonry = (TextView) headView.findViewById(R.id.home_tv_masonry);//砖石
            hTvFlower = (TextView) headView.findViewById(R.id.home_tv_flower);//鲜花
            hIvLevel = (ImageView) headView.findViewById(R.id.home_iv_level);//用户等级
            if(userPhoto.equals("")|| userPhoto==null){

            }else {
                Glide.with(UIUtils.getContext()).load(userPhoto)
                        .bitmapTransform(new GlideCircleTransform(UIUtils.getContext())).into(hIvAvatar);
            }

            hTvName.setText(userName);
            if(diamond==null || diamond.equals("")){
                hTvMasonry.setText("0");
            }else {
                hTvMasonry.setText(diamond);
            }
            hTvFlower.setText(flower);
            if(userLevel!=null){
                switch (userLevel){
                    case "1" :
                        hIvLevel.setImageResource(R.mipmap.general);
                        break;
                    case "2" :
                        hIvLevel.setImageResource(R.mipmap.white_collar);
                        break;
                    case "3" :
                        hIvLevel.setImageResource(R.mipmap.gold_collar);
                        break;
                    case "4" :
                        hIvLevel.setImageResource(R.mipmap.boss);
                        break;
                    case "5" :
                        hIvLevel.setImageResource(R.mipmap.loacl_lord);
                        break;
                }

            }

            hVpHeadCarosel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                }
            });

            // 绑定数据
//            hRollingView.setPageSize(2);
//            hRollingView.setClickColor(0xff888888);
            //hRollingView.setLeftDrawable(R.drawable.drawable_red_dot);
//            hRollingView.setRollingText(inforList);
//            hRollingView.setOnItemClickListener(new CustomRollingView.onItemClickListener() {
//                @Override
//                public void onItemClick(TextView v) {
//                    if(inforList!=null){
//                        for (int i = 0;i<inforList.size();i++){
//                            if(inforList.get(i).getTitle().equals(v.getText())){
//                                String id = inforList.get(i).getId();
//                                String flag = inforList.get(i).getFlag();
//                                Intent intent = new Intent(mMainUi, FreshNewsDetailActivity.class);
//                                intent.putExtra("id",id);
//                                intent.putExtra("flag",flag);
//                                mMainUi.startActivity(intent);
//                                break;
//                            }
//                        }
//                    }
//
//                }
//            });
//            hRollingView.resume();

            imageViews = new ArrayList<>();
            if(bannerList!=null){
                for (int i = 0; i < bannerList.size(); i++) {
                    ImageView point = new ImageView(mMainUi);
                    ImageView imageView = new ImageView(mMainUi);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                    // 初始化小圆点
                    point.setImageResource(R.drawable.home_red_point);// 设置图片(shape形状)
                    // 初始化布局参数, 宽高包裹内容,父控件是谁,就是谁声明的布局参数
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    if (i > 0) {
                        // 从第二个点开始设置左边距
                        params.leftMargin = DensityUtil.dip2px(mMainUi, 10);
                    }
                    point.setLayoutParams(params);// 设置布局参数
                    hLLContener.addView(point);// 给容器添加圆点
                    Glide.with(UIUtils.getContext()).load(bannerList.get(i).getAdvertAddr()).into(imageView);
                    imageViews.add(imageView);
                }

                HomePagerAdapter personalPagerAdapter = new HomePagerAdapter();
                hVpHeadCarosel.setAdapter(personalPagerAdapter);

                hVpHeadCarosel.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        // 更新小红点距离
                        int leftMargin = (int) (mPointDis * positionOffset) + position
                                * mPointDis;// 计算小红点当前的左边距
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) hIvRedPoint
                                .getLayoutParams();
                        params.leftMargin = leftMargin;// 修改左边距
                        // 重新设置布局参数
                        hIvRedPoint.setLayoutParams(params);
                    }

                    @Override
                    public void onPageSelected(int position) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });

                hIvRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {

                            @Override
                            public void onGlobalLayout() {
                                // 移除监听,避免重复回调
                                hIvRedPoint.getViewTreeObserver()
                                        .removeGlobalOnLayoutListener(this);
                                // ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                // layout方法执行结束的回调
                                mPointDis = hLLContener.getChildAt(1).getLeft()
                                        - hLLContener.getChildAt(0).getLeft();
                            }
                        });

                if (handler == null) {
                    handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case 0:
                                    int currentItem = hVpHeadCarosel.getCurrentItem();
                                    currentItem++;
                                    if (currentItem >= imageViews.size()) {
                                        currentItem = 0;
                                    }
                                    hVpHeadCarosel.setCurrentItem(currentItem);
                                    break;
                            }
                            handler.sendEmptyMessageDelayed(0, 3000);
                        }
                    };
                    handler.sendEmptyMessageDelayed(0, 3000);
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

    /**
     * 脚布局 的 viewholder
     */
    class HomeFootViewHolder extends RecyclerView.ViewHolder {

        public HomeFootViewHolder(View itemView) {
            super(itemView);
        }
    }


    class HomePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if(imageViews==null){
                return 0;
            }
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView view = imageViews.get(position);
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeAllViewsInLayout();
            }
            container.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String hrefAddr = bannerList.get(position).getHrefAddr();
                    if(hrefAddr==null||hrefAddr.equals("")){
                        return;
                    }
                    Intent intent = new Intent(mMainUi, WebActivity.class);
                    intent.putExtra("title","");
                    intent.putExtra("URL",hrefAddr);
                    mMainUi.startActivity(intent);
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}




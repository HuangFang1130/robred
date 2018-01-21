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
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.baidu.mobad.feeds.NativeResponse;
import com.bumptech.glide.Glide;
import com.hyphenate.easeui.domain.EaseUser;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.MainActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.WebActivity;
import com.jiahehongye.robred.bean.DFTTNewsResult;
import com.jiahehongye.robred.bean.HomeFragmentResult;
import com.jiahehongye.robred.biz.model.HomeBannerBean;
import com.jiahehongye.robred.biz.model.HomeWinningResponse;
import com.jiahehongye.robred.db.Model;
import com.jiahehongye.robred.interfaces.MyHeadViewClickListener;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.DensityUtil;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.CustomRollingView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjunhui on 2017/5/18.11:54
 */
public class HomeHeadFragmentRecycleAdapter extends BaseRecycleViewAdapter {

    private MainActivity mMainUi;
    private ArrayList<DFTTNewsResult.DataBean> fatherArraylist;
    private HomeFragmentResult homeFragmentResult;
    private ArrayList<ImageView> imageViews;
    private int mPointDis;
    private View footView;
    private WebView advWevView;

    public HomeHeadFragmentRecycleAdapter(MainActivity mMainUi, ArrayList<DFTTNewsResult.DataBean> fatherListDate) {
        this.mMainUi = mMainUi;
        this.fatherArraylist = fatherListDate;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_HEADER:
                View headView = LayoutInflater.from(mMainUi).inflate(R.layout.fragment_home_head, null);
                return new HomeHeadViewHolder(headView, mHeadClickListener);
            case ITEM_TYPE_CONTENT://1图片de
                View contentViewOne = LayoutInflater.from(mMainUi).inflate(R.layout.fragment_fresh_news_content_type_one, null);
                return new HomeSingleContentViewHolder(contentViewOne, mItemClickListener);
            case ITEM_TYPE_CONTENT_TWO://3图片de
                View contentViewTwo = LayoutInflater.from(mMainUi).inflate(R.layout.fragment_fresh_news_content_type_two, null);
                return new FreshNewsContentViewHolder(contentViewTwo, mItemClickListener);
            case ITEM_TYPE_FOOT:
                footView = LayoutInflater.from(mMainUi).inflate(R.layout.fragment_home_foot, null);
                hintFootView();
                return new HomeFootViewHolder(footView);
            case ITEM_TYPE_BIG_ADV://大图广告
                View avdTwo3 = LayoutInflater.from(mMainUi).inflate(R.layout.fragment_fresh_news_content, null);
//                return new FreshBigADVViewHolder(avdTwo3, mItemClickListener);
                return new FreshBigADBaiDuXinXiLiuVViewHolder(avdTwo3, mItemClickListener);
            case ITEM_TYPE_ADV://三张小图的广告
                View avdTwo = LayoutInflater.from(mMainUi).inflate(R.layout.fragment_fresh_news_content_type_two, null);
                return new FreshNewsADVViewHolder(avdTwo, mItemClickListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM_TYPE_CONTENT_TWO:
                onBindTitleViewHolder((FreshNewsContentViewHolder) holder, fatherArraylist.get(position - 1));
                break;
            case ITEM_TYPE_CONTENT:
                onBindContentViewHolder((HomeSingleContentViewHolder) holder, fatherArraylist.get(position - 1));
                break;
            case ITEM_TYPE_ADV:
                onBindContentAdvHolder((FreshNewsADVViewHolder) holder, fatherArraylist.get(position - 1));
                break;
            case ITEM_TYPE_BIG_ADV:
//                onbindConteBigAdvHolder((FreshBigADVViewHolder) holder, fatherArraylist.get(position - 1));
                onbindConteBigBaiduAdvHolder((FreshBigADBaiDuXinXiLiuVViewHolder) holder, fatherArraylist.get(position - 1));
                break;


        }
    }

    private void onbindConteBigBaiduAdvHolder(FreshBigADBaiDuXinXiLiuVViewHolder holder, DFTTNewsResult.DataBean dataBean) {
        NativeResponse nrAd = dataBean.getNativeResponse();
        if (nrAd == null) {
            return;
        }

//        AQuery aq = new AQuery(convertView);
        AQuery aq = holder.aQuery;
//        aq.id(R.id.native_icon_image).image(nrAd.getIconUrl(), false, true);
        aq.id(R.id.freshnews_content_one_iv_des).image(nrAd.getImageUrl(), false, true);
//        Glide.with(UIUtils.getContext()).load(nrAd.getImageUrl())
//                .error(R.mipmap.default_img)
//                .placeholder(R.mipmap.default_img)
//                .into(holder.imageView);
        aq.id(R.id.des).text(nrAd.getDesc());
        aq.id(R.id.freshnews_content_tv_title).text(nrAd.getTitle());
//        aq.id(R.id.native_brand_name).text(nrAd.getBrandName());
//        aq.id(R.id.native_adlogo).image(nrAd.getAdLogoUrl(), false, true);
//        aq.id(R.id.native_baidulogo).image(nrAd.getBaiduLogoUrl(), false, true);
//        String text = nrAd.isDownloadApp() ? "下载" : "查看";
//        aq.id(R.id.native_cta).text(text);
        nrAd.recordImpression(holder.avdView);


//        holder.title.setText(dataBean.getTopic());
//        Glide.with(UIUtils.getContext()).load(dataBean.getMiniimg().get(0).getSrc())
//                .error(R.mipmap.default_img)
//                .placeholder(R.mipmap.default_img)
//                .into(holder.imageView);
//
//        holder.des.setText(dataBean.getSource());
    }

    private void onbindConteBigAdvHolder(FreshBigADVViewHolder holder, DFTTNewsResult.DataBean dataBean) {
        holder.title.setText(dataBean.getTopic());
        Glide.with(UIUtils.getContext()).load(dataBean.getMiniimg().get(0).getSrc())
                .error(R.mipmap.default_img)
                .placeholder(R.mipmap.default_img)
                .into(holder.imageView);

        holder.des.setText(dataBean.getSource());
    }

    private void onBindContentAdvHolder(FreshNewsADVViewHolder holder, DFTTNewsResult.DataBean dataBean) {
        holder.title.setText(dataBean.getTopic());
        if (dataBean.getMiniimg().size() >= 1) {
            Glide.with(UIUtils.getContext()).load(dataBean.getMiniimg().get(0).getSrc())
                    .error(R.mipmap.default_img).placeholder(R.mipmap.default_img)
                    .into(holder.imageView1);
        }
        if (dataBean.getMiniimg().size() >= 2) {
            Glide.with(UIUtils.getContext()).load(dataBean.getMiniimg().get(1).getSrc())
                    .error(R.mipmap.default_img).placeholder(R.mipmap.default_img)
                    .into(holder.imageView2);
        }

        if (dataBean.getMiniimg().size() >= 3) {
            Glide.with(UIUtils.getContext()).load(dataBean.getMiniimg().get(2).getSrc())
                    .error(R.mipmap.default_img).placeholder(R.mipmap.default_img)
                    .into(holder.imageView3);
        }

        holder.des.setText(dataBean.getSource());
        holder.mTvAdv.setVisibility(View.VISIBLE);


    }

    private void onBindTitleViewHolder(FreshNewsContentViewHolder holder, DFTTNewsResult.DataBean newsBean) {
        holder.title.setText(newsBean.getTopic());
        Glide.with(UIUtils.getContext()).load(newsBean.getMiniimg().get(0).getSrc())
                .error(R.mipmap.default_img).placeholder(R.mipmap.default_img)
                .into(holder.imageView1);
        Glide.with(UIUtils.getContext()).load(newsBean.getMiniimg().get(1).getSrc())
                .error(R.mipmap.default_img).placeholder(R.mipmap.default_img)
                .into(holder.imageView2);

        Glide.with(UIUtils.getContext()).load(newsBean.getMiniimg().get(2).getSrc())
                .error(R.mipmap.default_img).placeholder(R.mipmap.default_img)
                .into(holder.imageView3);
        holder.des.setText(newsBean.getSource());
    }

    private void onBindContentViewHolder(HomeSingleContentViewHolder holder, DFTTNewsResult.DataBean newsBean) {
        holder.title.setText(newsBean.getTopic());
        if (newsBean.getMiniimg().size() > 0)

            Glide.with(UIUtils.getContext()).load(newsBean.getMiniimg().get(0).getSrc())
                    .error(R.mipmap.default_img)
                    .placeholder(R.mipmap.default_img)
                    .into(holder.imageView);

        if ("1".equals(newsBean.getIsvideo())) {
            holder.video.setVisibility(View.VISIBLE);
        } else {
            holder.video.setVisibility(View.GONE);
        }
//        holder.des.setText(newsBean.getSource()+"   "+newsBean.getComment_count()+"   评论"+newsBean.getDate());
        holder.des.setText(newsBean.getSource());

//        holder.title.setText(newsBean.getTitle());
//        Glide.with(UIUtils.getContext()).load(newsBean.getImgUrl()).asBitmap().into(holder.imageView);
//        holder.des.setText(newsBean.getContentSource()+"   评论"+newsBean.getCommentNum()+"   "+newsBean.getCreateDate());


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
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        } else if (position == fatherArraylist.size() + 1) {
            return ITEM_TYPE_FOOT;
        } else {

            if (fatherArraylist.get(position - 1).getIsadv().equals("1")) {
                if (fatherArraylist.get(position - 1).getBigpic().equals("1")) {

//                    if(fatherArraylist.get(position - 1))
                    return ITEM_TYPE_BIG_ADV;
                }
                return ITEM_TYPE_ADV;
            }

            if (fatherArraylist.get(position - 1).getMiniimg_size().equals("3")) {
                return ITEM_TYPE_CONTENT_TWO;
            }
            return ITEM_TYPE_CONTENT;
        }

    }


    @Override
    public int getItemCount() {
        return fatherArraylist.size() + 2;
    }

    public CustomRollingView hRollingView;

    public void setBannerAdv(WebView webView) {
        this.advWevView = webView;
    }

    /**
     * 脚布局 的 viewholder
     */
    class HomeFootViewHolder extends RecyclerView.ViewHolder {

        public HomeFootViewHolder(View itemView) {
            super(itemView);
        }
    }

    private Handler handler;

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
        private TextView mTvTitle;
        private RelativeLayout mRlParent;
        private RelativeLayout mRlBanner;

        private void registerBroadcastReceiver() {
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mMainUi);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constant.ACTION_AVATAR_CHANAGED);
            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    String mobile = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_MOBILE, "");
                    Model model = new Model(UIUtils.getContext());
                    EaseUser user = model.getContactList().get(mobile);
//                    userPhoto = user.getAvatar();
//                    userName = user.getNick();
//                    if (hIvAvatar != null) {
//                        Glide.with(UIUtils.getContext()).load(userPhoto)
//                                .bitmapTransform(new GlideCircleTransform(UIUtils.getContext())).into(hIvAvatar);
//
//                        hTvName.setText(userName);
//                    }


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
            mRlParent = (RelativeLayout) headView.findViewById(R.id.rl_parent);
            mRlBanner = (RelativeLayout) headView.findViewById(R.id.rl_banner);


            hRbGrab.setOnClickListener(this);
            hRbSend.setOnClickListener(this);
            hRbFindFavorable.setOnClickListener(this);
            hRbSpellLuck.setOnClickListener(this);
            RelativeLayout hRlPersonalInformation = (RelativeLayout) headView.findViewById(R.id.home_rl_personal_information);
            hRlPersonalInformation.setOnClickListener(this);

            if (homeFragmentResult == null) {
                return;
            }
            hVpHeadCarosel = (ViewPager) headView.findViewById(R.id.home_vp_carousel);
            mTvTitle = (TextView) headView.findViewById(R.id.title_banner);


            hLLContener = (LinearLayout) headView.findViewById(R.id.home_ll_container);
            hIvRedPoint = (ImageView) headView.findViewById(R.id.home_iv_redpoint);
            hRollingView = null;
            hRollingView = (CustomRollingView) headView.findViewById(R.id.home_head_rollingview);

            hIvAvatar = (ImageView) headView.findViewById(R.id.home_iv_avatar);//用户头像
            hTvName = (TextView) headView.findViewById(R.id.home_tv_name); //用户名字
            hTvMasonry = (TextView) headView.findViewById(R.id.home_tv_masonry);//砖石
            hTvFlower = (TextView) headView.findViewById(R.id.home_tv_flower);//鲜花
            hIvLevel = (ImageView) headView.findViewById(R.id.home_iv_level);//用户等级

            if (inforList != null) {
                hRollingView.setPageSize(1);
                hRollingView.setClickColor(0xff888888);
//                hRollingView.setLeftDrawable(R.drawable.drawable_red_dot);
                hRollingView.setRollingText(inforList);
//
                hRollingView.resume();
            }
//
            //-- end remark: 替换掉之前东方头条的数据，所以点击事件删除


            //-- start remark: 头部banner数据，替换为东方头条
            imageViews = new ArrayList<>();
            if (bannerList != null) {

//                int widthPixels = mMainUi.getResources().getDisplayMetrics().widthPixels;
//                int width = widthPixels;
//                double scral = 2/1;
//                int height = (int) ((width*scral)+0.5f);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mRlParent.getLayoutParams();
                layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                mRlParent.setLayoutParams(layoutParams);
                mRlBanner.setVisibility(View.VISIBLE);


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
                    Glide.with(UIUtils.getContext()).load(bannerList.get(i).getImgPath())
                            .error(R.mipmap.default_img)
                            .placeholder(R.mipmap.default_img).into(imageView);
                    imageViews.add(imageView);
                }

                HomePagerAdapter personalPagerAdapter = new HomePagerAdapter();
                hVpHeadCarosel.setAdapter(personalPagerAdapter);
                mTvTitle.setText(bannerList.get(0).getTopic());
                hVpHeadCarosel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                        mTvTitle.setText(bannerList.get(position).getTopic());
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
                                if (hLLContener != null && bannerList.size() >= 2) {
                                    mPointDis = hLLContener.getChildAt(1).getLeft()
                                            - hLLContener.getChildAt(0).getLeft();
                                }
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
            } else if (advWevView != null) {
                DisplayMetrics metric = new DisplayMetrics();
                mMainUi.getWindowManager().getDefaultDisplay().getMetrics(metric);
                int widthPixels = mMainUi.getResources().getDisplayMetrics().widthPixels;
                int width = widthPixels;
//                double scral = 1/2;
                int height = (int) ((width / 2) + 0.5f);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mRlParent.getLayoutParams();
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height);
                layoutParams.height = height;

                mRlParent.setLayoutParams(layoutParams);

                mRlParent.removeAllViews();
                ViewGroup p = (ViewGroup) advWevView.getParent();
                if (p != null) {
                    p.removeAllViewsInLayout();
                }

                mRlParent.addView(advWevView);
                mRlBanner.setVisibility(View.GONE);

            }
            //-- end remark: 头部banner数据，替换为东方头条

        }

        @Override
        public void onClick(View v) {
            if (mHeadClickListener != null) {
                mHeadClickListener.onHeadClick(v);
            }
        }
    }

    //    private List<HomeFragmentResult.DataBean.BannerLBean> bannerList;//轮播图
    private List<HomeBannerBean> bannerList;//轮播图
    private String userPhoto;
    private String userName;
    private String userLevel;
    private String flower;
    private String diamond;
    //    private List<HomeFragmentResult.DataBean.InforLBean> inforList;//社区热点新闻
    private List<HomeWinningResponse.WinningBean> inforList; //首页轮播中奖信息

    /**
     * 设置头部数据
     *
     * @param homeFragmentResult
     */
    public void setHeardDate(HomeFragmentResult homeFragmentResult) {
        this.homeFragmentResult = homeFragmentResult;
        if (homeFragmentResult != null) {
            HomeFragmentResult.DataBean data = homeFragmentResult.getData();
            if (data != null) {
//                bannerList = homeFragmentResult.getData().getBannerL();//banner图
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
//                inforList = homeFragmentResult.getData().getInforL();

                //保存用户信息到本地数据库中
                //保存到数据库中
                EaseUser user = new EaseUser((String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_MOBILE, ""));
                user.setNick(userName);
                user.setAvatar(userPhoto);
                Model model = new Model(UIUtils.getContext());
                model.saveContact(user);
            }

        }
    }

    public void setWinningData(List<HomeWinningResponse.WinningBean> mList) {
        this.inforList = mList;
        // 绑定数据
//        hRollingView.setPageSize(1);
//        hRollingView.setClickColor(0xff888888);
//        //hRollingView.setLeftDrawable(R.drawable.drawable_red_dot);
//        hRollingView.setRollingText(inforList);
//        hRollingView.resume();
    }

    public void setBannerData(List<HomeBannerBean> bannerList) {
        this.bannerList = bannerList;
    }

    class HomePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (imageViews == null) {
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
                    String hrefAddr = bannerList.get(position).getUrl();
                    if (hrefAddr == null || hrefAddr.equals("")) {
                        return;
                    }
                    Intent intent = new Intent(mMainUi, WebActivity.class);
                    intent.putExtra("title", "");
                    intent.putExtra("URL", hrefAddr);
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


    /**
     * 1图的
     */
    private class HomeSingleContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyItemClickListener mItemClickListener;
        private TextView title;
        private TextView des;
        private ImageView imageView;
        private TextView zhiding;
        private ImageView video;

        public HomeSingleContentViewHolder(View contentView, MyItemClickListener mItemClickListener) {
            super(contentView);
            this.mItemClickListener = mItemClickListener;
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.freshnews_content_tv_title);
            imageView = (ImageView) itemView.findViewById(R.id.freshnews_content_one_iv_des);
            des = (TextView) itemView.findViewById(R.id.des);
            zhiding = (TextView) itemView.findViewById(R.id.zhiding);
            video = (ImageView) itemView.findViewById(R.id.iv_video);


        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition() - 1);
            }
        }
    }


    /**
     * 3图的
     */
    class FreshNewsContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyItemClickListener mItemClickListener;
        private TextView title;
        private TextView des;
        private ImageView imageView;
        private TextView zhiding;
        private ImageView imageView1;
        private ImageView imageView2;
        private ImageView imageView3;

        public FreshNewsContentViewHolder(View itemView, MyItemClickListener mItemClickListener) {
            super(itemView);
            this.mItemClickListener = mItemClickListener;
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.freshnews_content_tv_title);
            imageView1 = (ImageView) itemView.findViewById(R.id.freshnews_content_iv1);
            imageView2 = (ImageView) itemView.findViewById(R.id.freshnews_content_iv2);
            imageView3 = (ImageView) itemView.findViewById(R.id.freshnews_content_iv3);

            des = (TextView) itemView.findViewById(R.id.des);
//            zhiding = (TextView) itemView.findViewById(R.id.zhiding);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition() - 1);
            }
        }
    }

    private class FreshBigADVViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyItemClickListener mItemClickListener;
        private TextView title;
        private TextView des;
        private ImageView imageView;
        private TextView zhiding;
        private final ImageView video;

        public FreshBigADVViewHolder(View avdTwo3, MyItemClickListener mItemClickListener) {
            super(avdTwo3);
            this.mItemClickListener = mItemClickListener;
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.freshnews_content_tv_title);
            imageView = (ImageView) itemView.findViewById(R.id.freshnews_content_one_iv_des);
            video = (ImageView) itemView.findViewById(R.id.iv_video);
            des = (TextView) itemView.findViewById(R.id.des);
            zhiding = (TextView) itemView.findViewById(R.id.zhiding);
            TextView mTvDAD = (TextView) itemView.findViewById(R.id.adv_dddd);
            mTvDAD.setVisibility(View.VISIBLE);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition() - 1);
            }
        }
    }

    private class FreshNewsADVViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyItemClickListener mItemClickListener;
        private TextView title;
        private TextView des;
        private ImageView imageView;
        private TextView zhiding;
        private ImageView imageView1;
        private ImageView imageView2;
        private ImageView imageView3;
        private TextView mTvAdv;

        public FreshNewsADVViewHolder(View avdTwo, MyItemClickListener mItemClickListener) {
            super(avdTwo);
            this.mItemClickListener = mItemClickListener;
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.freshnews_content_tv_title);
            imageView1 = (ImageView) itemView.findViewById(R.id.freshnews_content_iv1);
            imageView2 = (ImageView) itemView.findViewById(R.id.freshnews_content_iv2);
            imageView3 = (ImageView) itemView.findViewById(R.id.freshnews_content_iv3);
            mTvAdv = (TextView) itemView.findViewById(R.id.adv_dddd);
            mTvAdv.setVisibility(View.VISIBLE);
            des = (TextView) itemView.findViewById(R.id.des);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition() - 1);
            }
        }
    }

    private class FreshBigADBaiDuXinXiLiuVViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View avdView;
        private MyItemClickListener mItemClickListener;
        private TextView title;
        private TextView des;
        private ImageView imageView;
        private TextView zhiding;
        private ImageView video;
        private AQuery aQuery;

        public FreshBigADBaiDuXinXiLiuVViewHolder(View itemView, MyItemClickListener mItemClickListener) {
            super(itemView);
            this.avdView =itemView;
            aQuery = new AQuery(itemView);
            this.mItemClickListener = mItemClickListener;
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.freshnews_content_tv_title);
            imageView = (ImageView) itemView.findViewById(R.id.freshnews_content_one_iv_des);
            video = (ImageView) itemView.findViewById(R.id.iv_video);
            des = (TextView) itemView.findViewById(R.id.des);
            zhiding = (TextView) itemView.findViewById(R.id.zhiding);
            TextView mTvDAD = (TextView) itemView.findViewById(R.id.adv_dddd);
            mTvDAD.setVisibility(View.VISIBLE);


        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener!=null){
                mItemClickListener.onItemClick(v,getPosition()-1);
            }
        }
    }
}

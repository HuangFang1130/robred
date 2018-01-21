package com.jiahehongye.robred.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.ScrollViewListener;
import com.jiahehongye.robred.activity.SnatchOrderActivity;
import com.jiahehongye.robred.adapter.OneyuanHomeListAdapter;
import com.jiahehongye.robred.bean.OneyuanHomeBannerBean;
import com.jiahehongye.robred.bean.OneyuanHomeBannerBean.Banner;
import com.jiahehongye.robred.bean.OneyuanHomeBannerBean.ParticipationRecordList;
import com.jiahehongye.robred.bean.OneyuanHomeProductListBean;
import com.jiahehongye.robred.bean.OneyuanHomeProductListBean.ProductList;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.oneyuan.MyRobListActivity;
import com.jiahehongye.robred.oneyuan.OneyuanBannerUrlActivity;
import com.jiahehongye.robred.oneyuan.OneyuanProductDetailActivity;
import com.jiahehongye.robred.utils.DensityUtil;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.AlwaysMarqueeTextView;
import com.jiahehongye.robred.view.MarqueeText;
import com.jiahehongye.robred.view.MyProgressDialog;
import com.jiahehongye.robred.view.ObservableHorizontalScrollView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FoundFragment extends Fragment implements ScrollViewListener, OnClickListener, OnScrollListener {

    private AlwaysMarqueeTextView always;
    private int mPointDis;
    private ArrayList<ImageView> imageViews;

    private String colorStart = "<font color=#249FFF>";
    private String colorEnd = "</font>";
    private String title = "";

    // 首页产品列表请求参数5个
    private String mProductID = "";
    private String mPageSize = "10";
    private int mPageNum = 1;
    private String mType = null;
    private int mOrders = 0;

    // for循环计数器
    private int i_for = 0;

    // 获奖者字体颜色
    public static final int BLUE = 0xFF249CFF;

    // 排序标记
    private boolean FLAG_PRICE = true;
    private boolean FLAG_RENQI = true;
    private boolean FLAG_SHENGYU = true;
    private int flag_price = -1;
    private int flag_renqi = -1;
    private int flag_shengyu = -1;


    // 判断是否为上拉加载
    private boolean FLAG_REFRESH = false;

    // 次数统计
    private int count = 0;// 距离为负的次数判断
    private int requestCount = 0;// 请求成功的次数判断
    private int request = 1;

    private MarqueeText winMsg;// 滚动获奖者信息条
    private TextView myRobList;// 我的抢单
    private ViewPager banner;// 广告轮播图
    private LinearLayout points;
    private int prePointIndex = 0;// 记录前一个点的位置
    private boolean isRunning = true;
    private TextView moren1, price1, renqi1, shengyu1, moren2, price2, renqi2, shengyu2;// 排序列表
    private LinearLayout ll_moren1, ll_price1, ll_renqi1, ll_shengyu1, ll_moren2, ll_price2, ll_renqi2, ll_shengyu2;// 排序点击布局
    private ImageView price_up1, renqi_up1, shengyu_up1, price_down1, renqi_down1, shengyu_down1, price_up2, renqi_up2,
            shengyu_up2, price_down2, renqi_down2, shengyu_down2;// 排序箭头
    private ImageView noArraws1, noArraws2, noArraws3, noArraws4, noArraws5, noArraws6;// 默认箭头
    private PullToRefreshListView mHomeList;
    private ListView listview;
    private LinearLayout tags, tags2;// 两个包裹标签的linearlayout
    private LinearLayout listRule1, listRule2;// 列表排序选项条
    private LinearLayout llsv1, llsv2;// 两个包裹标签以及列表排序选项条的linearlayout
    private ObservableHorizontalScrollView sv1, sv2;// 两个横向滚动scrollview
    private ArrayList<View> viewList = new ArrayList<View>();// 存放第一组下划线
    private ArrayList<View> viewList2 = new ArrayList<View>();// 存放第二组下划线
    private ArrayList<LinearLayout> lllist = new ArrayList<LinearLayout>();// 存放第一组点击条目
    private ArrayList<LinearLayout> lllist2 = new ArrayList<LinearLayout>();// 存放第二组点击条目
    private ArrayList<ImageView> bannerList = new ArrayList<ImageView>();// 存放所有banner图
    private ArrayList<String> mTabNames = new ArrayList<String>();// 存放所有标签栏名称
    public static ArrayList<ProductList> totalList = new ArrayList<ProductList>();
    private OneyuanHomeBannerBean bannerBean;
    private OneyuanHomeProductListBean listBean;
    private OneyuanHomeListAdapter homeListAdapter;
    private List<Banner> banners;

    private ImageView hIvRedPoint;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 1:
                    if (animDialog != null) {
                        animDialog.dismiss();
                    }
                    mHomeList.onRefreshComplete();
                    String dataBanner = (String) msg.obj;
                    Gson gson1 = new Gson();
                    bannerBean = gson1.fromJson(dataBanner, OneyuanHomeBannerBean.class);
                    if (bannerBean.getResult().equals("success")) {

                        mProductID = bannerBean.getData().getProductClassificationList().get(0).getProductClassificationId();

                        // 设置顶部中奖公告变色字体
                        setWinMessage();

                        if (request == 1) {
                            // 设置banner轮播图
                            banners = bannerBean.getData().getBanner();
                            imageViews = new ArrayList<>();
                            if (banners != null) {
                                for (int i = 0; i < banners.size(); i++) {
                                    ImageView point = new ImageView(getActivity());
                                    ImageView imageView = new ImageView(getActivity());
                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                                    // 初始化小圆点
                                    point.setImageResource(R.drawable.home_red_point);// 设置图片(shape形状)
                                    // 初始化布局参数, 宽高包裹内容,父控件是谁,就是谁声明的布局参数
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                                    if (i > 0) {
                                        // 从第二个点开始设置左边距
                                        params.leftMargin = DensityUtil.dip2px(getActivity(), 10);
                                    }
                                    point.setLayoutParams(params);// 设置布局参数
                                    hLLContener.addView(point);// 给容器添加圆点
                                    Glide.with(UIUtils.getContext()).load(banners.get(i).getImage())
                                            .error(R.mipmap.default_img)
                                            .placeholder(R.mipmap.default_img).into(imageView);
                                    imageViews.add(imageView);
                                }

//							banner.setAdapter(new FoundViewpagerAdapter(getActivity(), banners));

                                HomePagerAdapter personalPagerAdapter = new HomePagerAdapter();
                                banner.setAdapter(personalPagerAdapter);

							banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                                                if (hLLContener != null && banners.size() >= 2) {
                                                    mPointDis = hLLContener.getChildAt(1).getLeft()
                                                            - hLLContener.getChildAt(0).getLeft();
                                                }
                                            }
                                        });


                                handler.sendEmptyMessageDelayed(5, 3000);

                            }
                            request++;
                        }
                        // 设置顶部中奖公告变色字体
                        setWinMessage();
                        /**
                         * 设置标签栏
                         */
                        setTagBar();
                        // 获取产品列表
                        initProductList();
                    } else {
                        Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 2:
                    if (animDialog != null) {
                        animDialog.dismiss();
                    }
                    mHomeList.onRefreshComplete();
                    String dataList = (String) msg.obj;
                    Gson gson2 = new Gson();
                    listBean = gson2.fromJson(dataList, OneyuanHomeProductListBean.class);
                    if (listBean.getResult().equals("success")) {
                        requestCount++;
                        ArrayList<ProductList> list = (ArrayList<ProductList>) listBean.getData().getProductList();
                        // 判断是否为上拉加载
                        if (FLAG_REFRESH) {
                            // 如果返回数据为空，则将页码数返回减少1
                            if (list.size() == 0) {
                                mPageNum--;
                            }
                            FLAG_REFRESH = !FLAG_REFRESH;
                            totalList.addAll(list);
                            homeListAdapter.notifyDataSetChanged();
                        } else {
                            totalList.clear();// 下拉刷新清空列表
                            totalList.addAll(list);// 填充最新请求到的数据
                            homeListAdapter = new OneyuanHomeListAdapter(getActivity(), totalList);
                            mHomeList.setAdapter(homeListAdapter);
                        }
                    } else {
                        Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 5:
                    int currentItem = banner.getCurrentItem();
                    currentItem++;
                    if (currentItem >= imageViews.size()) {
                        currentItem = 0;
                    }
                    banner.setCurrentItem(currentItem);
                    handler.sendEmptyMessageDelayed(5, 3000);

                    break;

                default:
                    break;
            }
        }

        private void setTagBar() {
            mTabNames.clear();// 清空标签名称
            viewList.clear();// 清空下划线1
            viewList2.clear();// 清空下划线2
            lllist.clear();// 清空排序条1
            lllist2.clear();// 清空排序条2
            tags.removeAllViews();// 移除所有添加的view
            tags2.removeAllViews();// 移除所有添加的view
            listRule1.setVisibility(View.GONE);// 因为刷新到热门推荐，所以隐藏排序条件栏
            listRule2.setVisibility(View.GONE);

            for (int j = 0; j < bannerBean.getData().getProductClassificationList().size(); j++) {
                mTabNames.add(bannerBean.getData().getProductClassificationList().get(j).getName());
            }
            // 添加两套标签栏
            for (int i = 0; i < mTabNames.size(); i++) {
                LinearLayout ll = new LinearLayout(getActivity());
                ll.setOrientation(LinearLayout.VERTICAL);
                LayoutParams params = new LayoutParams(
                        getActivity().getWindowManager().getDefaultDisplay().getWidth() / 15 * 4,
                        getActivity().getWindowManager().getDefaultDisplay().getHeight() / 16);
                ll.setLayoutParams(params);
                params.gravity = Gravity.CENTER;

                TextView tv = new TextView(getActivity());
                tv.setLayoutParams(params);
                tv.setText(mTabNames.get(i));
                tv.setTextSize(14);
                tv.setGravity(Gravity.CENTER);
                tv.setSingleLine(true);

                ll.addView(tv);

                View vv = new View(getActivity());
                vv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
                vv.setBackgroundColor(Color.parseColor("#4ebfa1"));
                vv.setVisibility(View.INVISIBLE);
                viewList.add(vv);
                if (i == 0) {
                    vv.setVisibility(View.VISIBLE);
                }

                ll.addView(vv);
                ll.setGravity(Gravity.CENTER);

                // 第二套
                LinearLayout ll2 = new LinearLayout(getActivity());
                ll2.setOrientation(LinearLayout.VERTICAL);
                LayoutParams params2 = new LayoutParams(
                        getActivity().getWindowManager().getDefaultDisplay().getWidth() / 15 * 4,
                        getActivity().getWindowManager().getDefaultDisplay().getHeight() / 16);
                ll2.setLayoutParams(params2);
                params2.gravity = Gravity.CENTER;

                TextView tv2 = new TextView(getActivity());
                tv2.setLayoutParams(params2);
                tv2.setText(mTabNames.get(i));
                tv2.setTextSize(14);
                tv2.setGravity(Gravity.CENTER);
                tv2.setSingleLine(true);

                ll2.addView(tv2);
                ll2.setGravity(Gravity.CENTER);

                View vv2 = new View(getActivity());
                vv2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
                vv2.setBackgroundColor(Color.parseColor("#4ebfa1"));
                vv2.setVisibility(View.INVISIBLE);
                viewList2.add(vv2);
                if (i == 0) {
                    vv2.setVisibility(View.VISIBLE);
                }

                ll2.addView(vv2);
                ll2.setGravity(Gravity.CENTER);

                ll.setTag(R.id.tag_first, vv);
                ll.setTag(R.id.tag_second, vv2);
                ll.setTag(R.id.tag_num, i);
                lllist.add(ll);
                tags.addView(ll);

                ll2.setTag(R.id.tag_first, vv);
                ll2.setTag(R.id.tag_second, vv2);
                ll2.setTag(R.id.tag_num, i);
                lllist2.add(ll2);
                tags2.addView(ll2);
            }
            // 设置两套标签栏互相监听
            for (int i = 0; i < lllist.size(); i++) {
                // 如果是第一个“热门推荐”，设置列表排序选项条隐藏，否侧显示
                if (i == 0) {
                    lllist.get(i).setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            for (int j = 0; j < viewList.size(); j++) {
                                viewList.get(j).setVisibility(View.INVISIBLE);
                                viewList2.get(j).setVisibility(View.INVISIBLE);
                            }
                            ((View) v.getTag(R.id.tag_first)).setVisibility(View.VISIBLE);
                            ((View) v.getTag(R.id.tag_second)).setVisibility(View.VISIBLE);
                            listRule1.setVisibility(View.GONE);
                            listRule2.setVisibility(View.GONE);

                            totalList.clear();// 切换了标签，所以要清空列表所有的数据
                            mPageNum = 1;// 页码重置为1
                            mProductID = bannerBean.getData().getProductClassificationList()
                                    .get((Integer) v.getTag(R.id.tag_num)).getProductClassificationId();
                            initProductList();
                        }
                    });
                } else {
                    lllist.get(i).setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            for (int j = 0; j < viewList.size(); j++) {
                                viewList.get(j).setVisibility(View.INVISIBLE);
                                viewList2.get(j).setVisibility(View.INVISIBLE);
                            }

                            ((View) v.getTag(R.id.tag_first)).setVisibility(View.VISIBLE);
                            ((View) v.getTag(R.id.tag_second)).setVisibility(View.VISIBLE);
                            listRule1.setVisibility(View.VISIBLE);
                            listRule2.setVisibility(View.VISIBLE);

                            totalList.clear();// 切换了标签，所以要清空列表所有的数据
                            mPageNum = 1;// 页码重置为1
                            mProductID = bannerBean.getData().getProductClassificationList()
                                    .get((Integer) v.getTag(R.id.tag_num)).getProductClassificationId();
                            initProductList();
                        }
                    });
                }
            }
            for (int i = 0; i < lllist2.size(); i++) {
                i_for = i;
                // 如果是第一个“热门推荐”，设置列表排序选项条隐藏，否侧显示
                if (i == 0) {
                    lllist2.get(i).setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            for (int j = 0; j < viewList.size(); j++) {
                                viewList.get(j).setVisibility(View.INVISIBLE);
                                viewList2.get(j).setVisibility(View.INVISIBLE);
                            }
                            ((View) v.getTag(R.id.tag_first)).setVisibility(View.VISIBLE);
                            ((View) v.getTag(R.id.tag_second)).setVisibility(View.VISIBLE);
                            listRule1.setVisibility(View.GONE);
                            listRule2.setVisibility(View.GONE);

                            totalList.clear();// 切换了标签，所以要清空列表所有的数据
                            mPageNum = 1;// 页码重置为1
                            mProductID = bannerBean.getData().getProductClassificationList()
                                    .get((Integer) v.getTag(R.id.tag_num)).getProductClassificationId();
                            initProductList();
                        }
                    });
                } else {
                    lllist2.get(i).setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            for (int j = 0; j < viewList.size(); j++) {
                                viewList.get(j).setVisibility(View.INVISIBLE);
                                viewList2.get(j).setVisibility(View.INVISIBLE);
                            }
                            ((View) v.getTag(R.id.tag_first)).setVisibility(View.VISIBLE);
                            ((View) v.getTag(R.id.tag_second)).setVisibility(View.VISIBLE);
                            listRule1.setVisibility(View.VISIBLE);
                            listRule2.setVisibility(View.VISIBLE);

                            totalList.clear();// 切换了标签，所以要清空列表所有的数据
                            mPageNum = 1;// 页码重置为1
                            mProductID = bannerBean.getData().getProductClassificationList()
                                    .get((Integer) v.getTag(R.id.tag_num)).getProductClassificationId();
                            initProductList();
                        }
                    });
                }
            }
        }

        private void setBanner(List<Banner> banners) {
//			bannerList.clear();
//			for (int i = 0; i < bannerBean.getData().getBanner().size(); i++) {
//				ImageView iv = new ImageView(getActivity());
//				iv.setScaleType(ScaleType.FIT_XY);
//				Picasso.with(getActivity()).load(bannerBean.getData().getBanner().get(i).getImage()).into(iv);
//				bannerList.add(iv);
//			}
//			MyPagerAdapter bannerAdapter = new MyPagerAdapter();
//			banner.setAdapter(bannerAdapter);]
            points.removeAllViews();
            for (int i = 0; i < banners.size(); i++) {

                // 添加灰色的点
                ImageView point = new ImageView(getActivity());
                point.setBackgroundResource(R.drawable.point_selector);
                // 设置宽高
                LayoutParams params = (LayoutParams) new LayoutParams(10, 10);
                if (i != 0) {
                    // 设置左边距
                    params.leftMargin = 5;
                }
                point.setLayoutParams(params);
                // 设置点的enable属性，让点根据属性变化图片
                point.setEnabled(false);
                // 把灰点添加到线性布局中
                points.addView(point);
            }

        }

        private void setWinMessage() {
            List<ParticipationRecordList> list = bannerBean.getData().getParticipationRecordList();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getNickNameAndSn() == null) {
                    title = list.get(i).getScrollBar();
                } else {
                    String name = (list.get(i).getNickNameAndSn());
                    String all = (list.get(i).getScrollBar());
                    int index1 = all.indexOf(name);
                    int length = name.length();
                    int index2 = index1 + length;
                    String start = all.substring(0, index1);
                    String color = all.substring(index1, index2);
                    String end = all.substring(index2);
                    Log.e("名字==", start + "--" + color + "--" + end);
                    String total = start + colorStart + color + colorEnd + end + "\t\t\t\t";
                    title = title + total;
                }
            }

            Spanned text = Html.fromHtml(title);

            always.setText(text);
        }

    };
    private LinearLayout hLLContener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pager_found, null);

        // 添加headview
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.oneyuan_home_headview, mHomeList, false);
        header.setLayoutParams(layoutParams);


        initView(view, header);

        initBanner();// 加载界面第一次请求
        // // 填充数据
        // ArrayList<String> list = new ArrayList<String>();
        // for (int i = 0; i < 10; i++) {
        // list.add(i + "");
        // }

        mHomeList.setMode(Mode.BOTH);
        listview = mHomeList.getRefreshableView();
        listview.addHeaderView(header);

        // // 列表设置适配器
        // OneyuanHomeListAdapter adapter = new
        // OneyuanHomeListAdapter(getActivity(), list);
        // mHomeList.setAdapter(adapter);

        mHomeList.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                if (refreshView.isRefreshing()) {
                    if (mHomeList.isHeaderShown()) {// 下拉刷新
                        mProductID = "";// 重置ID
                        requestCount = 0;// 重置请求次数为0
                        mPageNum = 1;// 重置
                        homeListAdapter.notifyDataSetInvalidated();
                        initBanner();
                    } else if (mHomeList.isFooterShown()) {// 上拉加载
                        FLAG_REFRESH = true;
                        mPageNum++;
                        initProductList();
                    } else {
                        mHomeList.onRefreshComplete();
                    }
                }
            }
        });

        return view;

    }

    /**
     * 获取首页banner数据
     */
    private PersistentCookieStore persistentCookieStore;
    private MyProgressDialog animDialog;
    private Call call;

    private void initBanner() {

        showMyDialog();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        Request request = new Request.Builder()
                .addHeader("accept", "application/json")
                .url(Constant.ONEYUANHOMEBANNER)
                .get().build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
                        animDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.e("一元抢购首页banner==", data);
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        });

    }

    /**
     * 获取首页产品列表
     */
    private void initProductList() {

        showMyDialog();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject json = new JSONObject();
        try {
            Log.e("当前请求：", mProductID + "---" + mType + "---" + mOrders);
            json.put("productClassificationId", mProductID);
            json.put("pageSize", "10");
            json.put("pageNumber", mPageNum + "");
            json.put("type", mType);
            json.put("orders", mOrders);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, json.toString());
        Request request = new Request.Builder()
                .url(Constant.ONEYUANHOMEPRODUCTLIST)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
                        animDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.e("一元抢购首页产品列表==", data);
                Message msg = Message.obtain();
                msg.what = 2;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        });
    }

    private void initView(View view, View header) {
        always = (AlwaysMarqueeTextView) header.findViewById(R.id.tv_always);
        always.setSelected(true);
        mHomeList = (PullToRefreshListView) view.findViewById(R.id.ptrlv_oneyuan_home);

        myRobList = (TextView) header.findViewById(R.id.tv_oneyuan_home_myroblist);
        myRobList.setOnClickListener(this);
        // 监听上下两部分布局滑动时的隐藏与显示
        mHomeList.setOnScrollListener(this);
//		points = (LinearLayout) header.findViewById(R.id.points);
        tags = (LinearLayout) header.findViewById(R.id.ll_oneyuan_home_hsv);
        tags2 = (LinearLayout) view.findViewById(R.id.ll_oneyuan_home_hsv2);
        // 两个横向scrollview设置同时滑动
        sv1 = (ObservableHorizontalScrollView) header.findViewById(R.id.sv1);
        sv2 = (ObservableHorizontalScrollView) view.findViewById(R.id.sv2);
        sv1.setOnScrollViewListener(this);
        sv2.setOnScrollViewListener(this);
        llsv1 = (LinearLayout) header.findViewById(R.id.llsv1);
        llsv2 = (LinearLayout) view.findViewById(R.id.llsv2);
        listRule1 = (LinearLayout) header.findViewById(R.id.ll_oneyuan_home_listrule1);
        listRule2 = (LinearLayout) view.findViewById(R.id.ll_oneyuan_home_listrule2);
        moren1 = (TextView) header.findViewById(R.id.tv_oneyuan_home_moren1);
        price1 = (TextView) header.findViewById(R.id.tv_oneyuan_home_price1);
        renqi1 = (TextView) header.findViewById(R.id.tv_oneyuan_home_renqi1);
        shengyu1 = (TextView) header.findViewById(R.id.tv_oneyuan_home_shengyu1);
        moren2 = (TextView) view.findViewById(R.id.tv_oneyuan_home_moren2);
        price2 = (TextView) view.findViewById(R.id.tv_oneyuan_home_price2);
        renqi2 = (TextView) view.findViewById(R.id.tv_oneyuan_home_renqi2);
        shengyu2 = (TextView) view.findViewById(R.id.tv_oneyuan_home_shengyu2);
        price_up1 = (ImageView) header.findViewById(R.id.iv_oneyuan_home_price_up1);
        price_down1 = (ImageView) header.findViewById(R.id.iv_oneyuan_home_price_down1);
        renqi_up1 = (ImageView) header.findViewById(R.id.iv_oneyuan_home_renqi_up1);
        renqi_down1 = (ImageView) header.findViewById(R.id.iv_oneyuan_home_renqi_down1);
        shengyu_up1 = (ImageView) header.findViewById(R.id.iv_oneyuan_home_shengyu_up1);
        shengyu_down1 = (ImageView) header.findViewById(R.id.iv_oneyuan_home_shengyu_down1);
        price_up2 = (ImageView) view.findViewById(R.id.iv_oneyuan_home_price_up2);
        price_down2 = (ImageView) view.findViewById(R.id.iv_oneyuan_home_price_down2);
        renqi_up2 = (ImageView) view.findViewById(R.id.iv_oneyuan_home_renqi_up2);
        renqi_down2 = (ImageView) view.findViewById(R.id.iv_oneyuan_home_renqi_down2);
        shengyu_up2 = (ImageView) view.findViewById(R.id.iv_oneyuan_home_shengyu_up2);
        shengyu_down2 = (ImageView) view.findViewById(R.id.iv_oneyuan_home_shengyu_down2);
        noArraws1 = (ImageView) header.findViewById(R.id.iv_oneyuan_home_noarrows1);
        noArraws2 = (ImageView) header.findViewById(R.id.iv_oneyuan_home_noarrows2);
        noArraws3 = (ImageView) header.findViewById(R.id.iv_oneyuan_home_noarrows3);
        noArraws4 = (ImageView) view.findViewById(R.id.iv_oneyuan_home_noarrows4);
        noArraws5 = (ImageView) view.findViewById(R.id.iv_oneyuan_home_noarrows5);
        noArraws6 = (ImageView) view.findViewById(R.id.iv_oneyuan_home_noarrows6);
        ll_moren1 = (LinearLayout) header.findViewById(R.id.ll_oneyuan_home_moren1);
        ll_price1 = (LinearLayout) header.findViewById(R.id.ll_oneyuan_home_price1);
        ll_renqi1 = (LinearLayout) header.findViewById(R.id.ll_oneyuan_home_renqi1);
        ll_shengyu1 = (LinearLayout) header.findViewById(R.id.ll_oneyuan_home_shengyu1);
        ll_moren2 = (LinearLayout) view.findViewById(R.id.ll_oneyuan_home_moren2);
        ll_price2 = (LinearLayout) view.findViewById(R.id.ll_oneyuan_home_price2);
        ll_renqi2 = (LinearLayout) view.findViewById(R.id.ll_oneyuan_home_renqi2);
        ll_shengyu2 = (LinearLayout) view.findViewById(R.id.ll_oneyuan_home_shengyu2);
        ll_moren1.setOnClickListener(this);
        ll_price1.setOnClickListener(this);
        ll_renqi1.setOnClickListener(this);
        ll_shengyu1.setOnClickListener(this);
        ll_moren2.setOnClickListener(this);
        ll_price2.setOnClickListener(this);
        ll_renqi2.setOnClickListener(this);
        ll_shengyu2.setOnClickListener(this);
        hIvRedPoint = (ImageView) header.findViewById(R.id.home_iv_redpoint);
        hLLContener = (LinearLayout) header.findViewById(R.id.home_ll_container);
        banner = (ViewPager) header.findViewById(R.id.oneyuan_home_banner);



    }

    // 两个scrollview联动
    @Override
    public void onScrollChanged(ObservableHorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (scrollView == sv1) {
            sv2.scrollTo(x, y);
        } else if (scrollView == sv2) {
            sv1.scrollTo(x, y);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 我的抢单
            case R.id.tv_oneyuan_home_myroblist:
                startActivity(new Intent(getContext(), SnatchOrderActivity.class).putExtra("title","我的抢单"));
                break;
            // 分类排序按钮8个
            case R.id.ll_oneyuan_home_moren1:
                setTextColor();
                setArrowsVisitable();
                setNoArrowsVisitable();
                moren1.setTextColor(Color.parseColor("#4ebfa1"));
                moren2.setTextColor(Color.parseColor("#4ebfa1"));
                mType = null;
                break;
            case R.id.ll_oneyuan_home_moren2:
                setTextColor();
                setArrowsVisitable();
                setNoArrowsVisitable();
                moren1.setTextColor(Color.parseColor("#4ebfa1"));
                moren2.setTextColor(Color.parseColor("#4ebfa1"));
                mType = null;
                break;
            case R.id.ll_oneyuan_home_price1:
                setTextColor();
                setArrowsVisitable();
                if (flag_price == 0) {
                    price_down1.setVisibility(View.VISIBLE);
                    price_down2.setVisibility(View.VISIBLE);
                    price_up1.setVisibility(View.GONE);
                    price_up2.setVisibility(View.GONE);
                    flag_price = 1;
                } else if (flag_price == 1 || flag_price == -1) {
                    price_down1.setVisibility(View.GONE);
                    price_down2.setVisibility(View.GONE);
                    price_up1.setVisibility(View.VISIBLE);
                    price_up2.setVisibility(View.VISIBLE);
                    flag_price = 0;
                }
                FLAG_PRICE = !FLAG_PRICE;
                setNoArrowsVisitable();
                noArraws1.setVisibility(View.INVISIBLE);
                noArraws4.setVisibility(View.INVISIBLE);
                price1.setTextColor(Color.parseColor("#4ebfa1"));
                price2.setTextColor(Color.parseColor("#4ebfa1"));

                mOrders = flag_price;
                mType = "priceType";
                initProductList();
                break;
            case R.id.ll_oneyuan_home_price2:
                setTextColor();
                setArrowsVisitable();
                if (flag_price == 0) {
                    price_down1.setVisibility(View.VISIBLE);
                    price_down2.setVisibility(View.VISIBLE);
                    price_up1.setVisibility(View.GONE);
                    price_up2.setVisibility(View.GONE);
                    flag_price = 1;
                } else if (flag_price == 1 || flag_price == -1) {
                    price_down1.setVisibility(View.GONE);
                    price_down2.setVisibility(View.GONE);
                    price_up1.setVisibility(View.VISIBLE);
                    price_up2.setVisibility(View.VISIBLE);
                    flag_price = 0;
                }
                FLAG_PRICE = !FLAG_PRICE;
                setNoArrowsVisitable();
                noArraws1.setVisibility(View.INVISIBLE);
                noArraws4.setVisibility(View.INVISIBLE);
                price1.setTextColor(Color.parseColor("#4ebfa1"));
                price2.setTextColor(Color.parseColor("#4ebfa1"));

                mOrders = flag_price;
                mType = "priceType";
                initProductList();
                break;
            case R.id.ll_oneyuan_home_renqi1:
                setTextColor();
                setArrowsVisitable();
                if (flag_renqi == 0) {
                    renqi_down1.setVisibility(View.VISIBLE);
                    renqi_down2.setVisibility(View.VISIBLE);
                    renqi_up1.setVisibility(View.GONE);
                    renqi_up2.setVisibility(View.GONE);
                    flag_renqi = 1;
                } else if (flag_renqi == 1 || flag_renqi == -1) {
                    renqi_down1.setVisibility(View.GONE);
                    renqi_down2.setVisibility(View.GONE);
                    renqi_up1.setVisibility(View.VISIBLE);
                    renqi_up2.setVisibility(View.VISIBLE);
                    flag_renqi = 0;
                }
                FLAG_RENQI = !FLAG_RENQI;
                setNoArrowsVisitable();
                noArraws2.setVisibility(View.INVISIBLE);
                noArraws5.setVisibility(View.INVISIBLE);
                renqi1.setTextColor(Color.parseColor("#4ebfa1"));
                renqi2.setTextColor(Color.parseColor("#4ebfa1"));

                mOrders = flag_renqi;
                mType = "totalPeopleType";
                initProductList();
                break;
            case R.id.ll_oneyuan_home_renqi2:
                setTextColor();
                setArrowsVisitable();
                if (flag_renqi == 0) {
                    renqi_down1.setVisibility(View.VISIBLE);
                    renqi_down2.setVisibility(View.VISIBLE);
                    renqi_up1.setVisibility(View.GONE);
                    renqi_up2.setVisibility(View.GONE);
                    flag_renqi = 1;
                } else if (flag_renqi == 1 || flag_renqi == -1) {
                    renqi_down1.setVisibility(View.GONE);
                    renqi_down2.setVisibility(View.GONE);
                    renqi_up1.setVisibility(View.VISIBLE);
                    renqi_up2.setVisibility(View.VISIBLE);
                    flag_renqi = 0;
                }
                FLAG_RENQI = !FLAG_RENQI;
                setNoArrowsVisitable();
                noArraws2.setVisibility(View.INVISIBLE);
                noArraws5.setVisibility(View.INVISIBLE);
                renqi1.setTextColor(Color.parseColor("#4ebfa1"));
                renqi2.setTextColor(Color.parseColor("#4ebfa1"));

                mOrders = flag_renqi;
                mType = "totalPeopleType";
                initProductList();
                break;
            case R.id.ll_oneyuan_home_shengyu1:
                setTextColor();
                setArrowsVisitable();
                if (flag_shengyu == 0) {
                    shengyu_down1.setVisibility(View.VISIBLE);
                    shengyu_down2.setVisibility(View.VISIBLE);
                    shengyu_up1.setVisibility(View.GONE);
                    shengyu_up2.setVisibility(View.GONE);
                    flag_shengyu = 1;
                } else if (flag_shengyu == 1 || flag_shengyu == -1) {
                    shengyu_down1.setVisibility(View.GONE);
                    shengyu_down2.setVisibility(View.GONE);
                    shengyu_up1.setVisibility(View.VISIBLE);
                    shengyu_up2.setVisibility(View.VISIBLE);
                    flag_shengyu = 0;
                }
                FLAG_SHENGYU = !FLAG_SHENGYU;
                setNoArrowsVisitable();
                noArraws3.setVisibility(View.INVISIBLE);
                noArraws6.setVisibility(View.INVISIBLE);
                shengyu1.setTextColor(Color.parseColor("#4ebfa1"));
                shengyu2.setTextColor(Color.parseColor("#4ebfa1"));

                mOrders = flag_shengyu;
                mType = "surplusPeopleType";
                initProductList();
                break;
            case R.id.ll_oneyuan_home_shengyu2:
                setTextColor();
                setArrowsVisitable();
                if (flag_shengyu == 0) {
                    shengyu_down1.setVisibility(View.VISIBLE);
                    shengyu_down2.setVisibility(View.VISIBLE);
                    shengyu_up1.setVisibility(View.GONE);
                    shengyu_up2.setVisibility(View.GONE);
                    flag_shengyu = 1;
                } else if (flag_shengyu == 1 || flag_shengyu == -1) {
                    shengyu_down1.setVisibility(View.GONE);
                    shengyu_down2.setVisibility(View.GONE);
                    shengyu_up1.setVisibility(View.VISIBLE);
                    shengyu_up2.setVisibility(View.VISIBLE);
                    flag_shengyu = 0;
                }
                FLAG_SHENGYU = !FLAG_SHENGYU;
                setNoArrowsVisitable();
                noArraws3.setVisibility(View.INVISIBLE);
                noArraws6.setVisibility(View.INVISIBLE);
                shengyu1.setTextColor(Color.parseColor("#4ebfa1"));
                shengyu2.setTextColor(Color.parseColor("#4ebfa1"));

                mOrders = flag_shengyu;
                mType = "surplusPeopleType";
                initProductList();
                break;
        }
    }

    // 排序默认箭头全显示
    private void setNoArrowsVisitable() {
        noArraws1.setVisibility(View.VISIBLE);
        noArraws2.setVisibility(View.VISIBLE);
        noArraws3.setVisibility(View.VISIBLE);
        noArraws4.setVisibility(View.VISIBLE);
        noArraws5.setVisibility(View.VISIBLE);
        noArraws6.setVisibility(View.VISIBLE);
    }

    // 排序箭头全部隐藏
    private void setArrowsVisitable() {
        price_up1.setVisibility(View.INVISIBLE);
        price_up2.setVisibility(View.INVISIBLE);
        price_down1.setVisibility(View.INVISIBLE);
        price_down2.setVisibility(View.INVISIBLE);
        renqi_up1.setVisibility(View.INVISIBLE);
        renqi_up2.setVisibility(View.INVISIBLE);
        renqi_down1.setVisibility(View.INVISIBLE);
        renqi_down2.setVisibility(View.INVISIBLE);
        shengyu_up1.setVisibility(View.INVISIBLE);
        shengyu_up2.setVisibility(View.INVISIBLE);
        shengyu_down1.setVisibility(View.INVISIBLE);
        shengyu_down2.setVisibility(View.INVISIBLE);
    }

    // 排序字体全部黑色
    private void setTextColor() {
        moren1.setTextColor(Color.BLACK);
        moren2.setTextColor(Color.BLACK);
        price1.setTextColor(Color.BLACK);
        price2.setTextColor(Color.BLACK);
        renqi1.setTextColor(Color.BLACK);
        renqi2.setTextColor(Color.BLACK);
        shengyu1.setTextColor(Color.BLACK);
        shengyu2.setTextColor(Color.BLACK);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    /**
     * 判断两个布局的屏幕位置，如果小于就两个都显示，如果不小于就显示一个
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int[] location1 = new int[2];
        int[] location2 = new int[2];
        llsv1.getLocationOnScreen(location1);
        llsv2.getLocationOnScreen(location2);
        // Log.e("高度啊", location1[1] + "====" + location2[1]);
        if (location1[1] > location2[1]) {
            llsv2.setVisibility(View.INVISIBLE);
        } else if (location1[1] < location2[1]) {
            if (location1[1] < 0) {
                count++;
            }
            if (count > 1) {
                llsv2.setVisibility(View.VISIBLE);
            } else if (count == 1) {
                llsv2.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 显示对话框
     */
    public void showMyDialog() {
        animDialog = new MyProgressDialog(getActivity(), "玩命加载中...", R.drawable.loading);
        animDialog.show();
        animDialog.setCancelable(true);
    }

    class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {

            // // 把真正的position取模 图片的size
            int newPosition = position % banners.size();
            // // 把前一个白点变为灰色
            points.getChildAt(prePointIndex).setEnabled(false);
            // // 当切换到某一页时，修改文字内容
            // photoInfo.setText(words[newPosition]);
            // // 当切换到某一页时，把当前位置的点变为白色
            points.getChildAt(newPosition).setEnabled(true);
            // // 记录前一个白点的位置
            prePointIndex = newPosition;

        }

    }

    Thread thread;

    private void autoPlay() {

        thread = new Thread() {
            public void run() {
                while (isRunning) {
                    try {


                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // System.out.println("切换到下一页");
                                // 切换到下一页
                                banner.setCurrentItem(banner.getCurrentItem() + 1);

                            }
                        });
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        };

        thread.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
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

                    if (banners.get(position).getIsNetwork().equals("0")) {// 如果是包含产品ID
                        Intent intent2 = new Intent(getActivity(), OneyuanProductDetailActivity.class);
                        intent2.putExtra("productId", banners.get(position).getProductId());
                        getActivity().startActivity(intent2);
                    } else {
                        // 如果是包含外网地址
                        // 把当前option下的url获取到传递过去
                        Intent intent = new Intent(getActivity(), OneyuanBannerUrlActivity.class);
                        intent.putExtra("url", banners.get(position).getUrl());
                        getActivity().startActivity(intent);
                    }
//					String hrefAddr = banners.get(position).getUrl();
//					if (hrefAddr == null || hrefAddr.equals("")) {
//						return;
//					}
//					Intent intent = new Intent(getActivity(), WebActivity.class);
//					intent.putExtra("title", "");
//					intent.putExtra("URL", hrefAddr);
//					getActivity().startActivity(intent);
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

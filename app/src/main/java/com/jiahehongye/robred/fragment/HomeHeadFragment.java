package com.jiahehongye.robred.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.baidu.mobad.feeds.BaiduNative;
import com.baidu.mobad.feeds.NativeErrorCode;
import com.baidu.mobad.feeds.NativeResponse;
import com.baidu.mobad.feeds.RequestParameters;
import com.google.gson.Gson;
import com.jiahehongye.robred.BaseFragment;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.ContactActivity;
import com.jiahehongye.robred.activity.DFTTNewsDetailActivity;
import com.jiahehongye.robred.activity.DiamondAccountActivity;
import com.jiahehongye.robred.activity.GrabRedActivity;
import com.jiahehongye.robred.activity.OneyuanActivity;
import com.jiahehongye.robred.activity.SendRedActivity;
import com.jiahehongye.robred.activity.WebActivity;
import com.jiahehongye.robred.adapter.HomeHeadFragmentRecycleAdapter;
import com.jiahehongye.robred.application.BaseApplication;
import com.jiahehongye.robred.bean.DFTTAdvResult;
import com.jiahehongye.robred.bean.DFTTNewsResult;
import com.jiahehongye.robred.bean.DFTTTitleResult;
import com.jiahehongye.robred.bean.HomeFragmentResult;
import com.jiahehongye.robred.biz.model.HomeBannerBean;
import com.jiahehongye.robred.biz.model.HomeWinningResponse;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.interfaces.MyHeadViewClickListener;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.LogUtil;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.MyProgressDialog;
import com.songheng.newsapisdk.sdk.apiservice.DFTTAdsApiService;
import com.songheng.newsapisdk.sdk.apiservice.DFTTNewsApiService;
import com.songheng.newsapisdk.sdk.apiservice.DFTTStatisticsApiService;
import com.songheng.newsapisdk.sdk.apiservice.listener.DfttApiServiceCallBack;

import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by huangjunhui on 2017/5/18.10:34
 */
public class HomeHeadFragment extends BaseFragment {
    private static final int GET_HOME = 0000;
    private static final int GET_HOME_LIST = 200;
    private static final int GET_ADV = 500;
    private static final int SHOW_HTML = 700;
    private static final int BAIDU_XINXILIU_ADV = 800;

    private static final int GET_HOME_SERVICE_BANNER = 600;
    private String isup;
    private int fuPage = -1;
    private int fuIndex = -20;
    private List<DFTTNewsResult.DataBean> data = new ArrayList<>();
    private String type;
    private String name;
    private RecyclerView mHomeHeadRecycleView;
    private SwipeRefreshLayout mHomeHeadSwiperefresh;
    private int pagerNamber = 1;
    private ArrayList<DFTTNewsResult.DataBean> fatherListDate;
    private boolean isLogin;
    private HomeHeadFragmentRecycleAdapter mHomeRecycleAdapter;
    private PersistentCookieStore persistentCookieStore;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_HOME:
                    String homefragment = (String) msg.obj;
                    LogUtil.LogShitou("homefragment：   ", homefragment);
                    try {
                        homeFragmentResult = new Gson().fromJson(homefragment, HomeFragmentResult.class);
                    } catch (Exception e) {

                    }
                    mHomeRecycleAdapter.setHeardDate(homeFragmentResult);
                    mHomeHeadRecycleView.setAdapter(mHomeRecycleAdapter);
                    mHomeRecycleAdapter.notifyDataSetChanged();
                    break;

                case GET_HOME_LIST:

                    if (mHomeHeadSwiperefresh != null) {
                        if (mHomeHeadSwiperefresh.isRefreshing()) {
                            mHomeHeadSwiperefresh.setRefreshing(false);
                        }
                    }
                    String result = (String) msg.obj;
                    DFTTNewsResult dfttNewsResult = new Gson().fromJson(result, DFTTNewsResult.class);
                    data = dfttNewsResult.getData();
//                    requestAdvDate(pagesdssss);
                    //请求百度广告
                    requestBaiDuLiuAdv();

                    break;

                case GET_ADV:
                    if (mHomeHeadSwiperefresh != null) {
                        if (mHomeHeadSwiperefresh.isRefreshing()) {
                            mHomeHeadSwiperefresh.setRefreshing(false);
                        }
                    }
                    String result3 = (String) msg.obj;
                    DFTTAdvResult dfttAdvResult = new Gson().fromJson(result3, DFTTAdvResult.class);
                    List<DFTTNewsResult.DataBean> data1 = dfttAdvResult.getData();
                    for (int i = 0; i < data1.size(); i++) {
                        requestServerShow(fatherListDate.size(), data1.get(i));
                    }

                    /**
                     * 遍历最外面的。然后在第几个位置停下来
                     *  取第一个位置的数据放进来
                     */
                    if (data1 != null && data != null) {
                        for (int i = 0; i < data.size(); i++) {
                            if (i == 3 || i == 8 || i == 13 || i == 18) {
                                if (data1.size() == 0) {
                                    break;
                                }
                                DFTTNewsResult.DataBean dataBean = data1.get(0);//4个
//                              dataBean.setIsadv("1");//是否是广告
                                if (i == 3 || i == 13) {//大图（1张）   小图（3张的）
//                                DFTTNewsResult.DataBean dataBean = data1.get(0);
                                    dataBean.setBigpic("1");
                                }
                                data.remove(i);//19
                                data.add(i, data1.get(0));
                                data1.remove(0);//移除掉
                                continue;
                            }

                        }

                        fatherListDate.addAll(data);
                        mHomeRecycleAdapter.notifyDataSetChanged();
                    }

                    break;

                case GET_DFBANNER:
                    List bannerList = new ArrayList();
                    DFTTAdvResult banner = (DFTTAdvResult) msg.obj;
                    List<DFTTNewsResult.DataBean> bannerData = banner.getData();
                    for (int i = 0; i < bannerData.size(); i++) {
                        DFTTNewsResult.DataBean item = bannerData.get(i);
                        DFTTNewsResult.DataBean.MiniimgBean imgItem = item.getMiniimg().get(0);
                        String imgPath = imgItem.getSrc(); //图片地址
                        String imgUrl = item.getUrl(); //关联地址
                        String topic = item.getTopic();//标题
                        HomeBannerBean bannerItem = new HomeBannerBean(imgUrl, imgPath, topic);
                        bannerList.add(bannerItem);
                    }
                    mHomeRecycleAdapter.setBannerData(bannerList);
                    mHomeHeadRecycleView.setAdapter(mHomeRecycleAdapter);
                    mHomeRecycleAdapter.notifyDataSetChanged();
                    break;

                case Constant.RESULT_HOME_WINNING:
                    mHomeRecycleAdapter.setWinningData(((HomeWinningResponse) msg.obj).getData());
                    mHomeHeadRecycleView.setAdapter(mHomeRecycleAdapter);
                    mHomeRecycleAdapter.notifyDataSetChanged();
                    break;

                case GET_HOME_SERVICE_BANNER:
                    String homeservice = (String) msg.obj;
                    List bannerListss = new ArrayList();
                    LogUtil.LogShitou("homefragment：   ", homeservice);
                    try {
                        homeFragmentResult = new Gson().fromJson(homeservice, HomeFragmentResult.class);
                        List<HomeFragmentResult.DataBean.BannerLBean> bannerL = homeFragmentResult.getData().getBannerL();

                        for (int i = 0; i < bannerL.size(); i++) {
                            String imgPath = bannerL.get(i).getAdvertAddr(); //图片地址
                            String imgUrl = bannerL.get(i).getHrefAddr(); //关联地址
                            String topic = bannerL.get(i).getAdvertName();//标题
                            HomeBannerBean bannerItem = new HomeBannerBean(imgUrl, imgPath, topic);
                            bannerListss.add(bannerItem);
                        }
                    } catch (Exception e) {
                    }
                    mHomeRecycleAdapter.setBannerData(bannerListss);
                    mHomeHeadRecycleView.setAdapter(mHomeRecycleAdapter);
                    mHomeRecycleAdapter.notifyDataSetChanged();

                    break;

                case SHOW_HTML:
                    WebView webView = mNrAd.getWebView();
                    mHomeRecycleAdapter.setBannerAdv(webView);
                    mHomeHeadRecycleView.setAdapter(mHomeRecycleAdapter);
                    mHomeRecycleAdapter.notifyDataSetChanged();
//                    adsParent.addView(webView);

                    break;

                case BAIDU_XINXILIU_ADV:

                    if(animDialog!=null) {
                        animDialog.dismiss();
                    }
                    List<NativeResponse>  bdAdv = (List<NativeResponse>) msg.obj;

                    /**
                     * 遍历最外面的。然后在第几个位置停下来
                     *  取第一个位置的数据放进来
                     */
                    DFTTNewsResult.DataBean dataBean;
                    boolean adAvailable;
                    if (bdAdv != null && data != null) {
                        for (int i = 0; i < data.size(); i++) {
                            if (i == 4 || i == 9 || i == 14 || i == 19) {
                                if (bdAdv.size() == 0) {
                                    break;
                                }
                                NativeResponse nativeResponse = bdAdv.get(0);//5个
                                if(nativeResponse.getImageUrl()==null){
                                    bdAdv.remove(0);//移除掉
                                    break;
                                }
                                if(TextUtils.isEmpty(nativeResponse.getImageUrl())){
                                    bdAdv.remove(0);//移除掉
                                    break;
                                }
                                adAvailable = nativeResponse.isAdAvailable(getActivity());
                                if(!adAvailable){
                                    bdAdv.remove(0);//移除掉
                                    break;
                                }
                                dataBean = new DFTTNewsResult.DataBean();
                                dataBean.setIsadv("1");
                                dataBean.setNativeResponse(nativeResponse);
//                              dataBean.setIsadv("1");//是否是广告
//                                if (i == 3 || i == 13) {//大图（1张）   小图（3张的）
//                                DFTTNewsResult.DataBean dataBean = data1.get(0);
                                    dataBean.setBigpic("1");
//                                }
                                data.remove(i);//19
                                data.add(i, dataBean);
                                bdAdv.remove(0);//移除掉
                                continue;
                            }

                        }

                        fatherListDate.addAll(data);
                        mHomeRecycleAdapter.showFootView();
                        mHomeRecycleAdapter.notifyDataSetChanged();
                    }


                    break;
            }
        }

    };


    private void requestBaiDuLiuAdv() {
        /**
         * Step 1. 创建BaiduNative对象，参数分别为： 上下文context，广告位ID, BaiduNativeNetworkListener监听（监听广告请求的成功与失败）
         * 注意：请将YOUR_AD_PALCE_ID替换为自己的广告位ID
         */
        String BAIDU_XINXILIU_ID = "4505308";
        BaiduNative baidu = new BaiduNative(mMainUi, BAIDU_XINXILIU_ID, new BaiduNative.BaiduNativeNetworkListener() {

            @Override
            public void onNativeFail(NativeErrorCode arg0) {
//                Log.w("FeedNativeListViewActivity", "onNativeFail reason:" + arg0.name());
                Toast.makeText(getActivity(), arg0.toString()+"", Toast.LENGTH_SHORT).show();

                if(animDialog!=null) {
                    animDialog.dismiss();
                }
            }

            @Override
            public void onNativeLoad(List<NativeResponse> arg0) {
                // 一个广告只允许展现一次，多次展现、点击只会计入一次
                if (arg0 != null && arg0.size() > 0) {
//                    List<NativeResponse>  nrAdList = arg0;
//                    Toast.makeText(getActivity(), nrAdList.size()+"", Toast.LENGTH_SHORT).show();
//                    insertAdToRecycle(nrAdList);
                    Message obtain = Message.obtain();
                    obtain.what = BAIDU_XINXILIU_ADV;
                    obtain.obj = arg0;
                    handler.sendMessage(obtain);
                }else {
                    Toast.makeText(getActivity(), "onNativeLoad "+"     null" , Toast.LENGTH_SHORT).show();

                }
            }

        });

        /**
         * Step 2. 创建requestParameters对象，并将其传给baidu.makeRequest来请求广告
         */
        // 用户点击下载类广告时，是否弹出提示框让用户选择下载与否
        RequestParameters requestParameters =
                new RequestParameters.Builder()
                        .downloadAppConfirmPolicy(
                                RequestParameters.DOWNLOAD_APP_CONFIRM_ONLY_MOBILE).build();

        baidu.makeRequest(requestParameters);


    }


//    //把广告插入到recycleview中去
//    private void insertAdToRecycle(List<NativeResponse> nrAdList) {
//
//
//    }

    private HomeFragmentResult homeFragmentResult;
    private View contentView;
    private LinearLayoutManager linearLayoutManager;
    private MyProgressDialog animDialog;
    private int pagesdssss = 1;
    private static final int GET_DFBANNER = 49;
    private int chi = 0;
    private int widthPixels;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_head_home, null);
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        widthPixels = getResources().getDisplayMetrics().widthPixels;
        Bundle arguments = getArguments();
        isup = arguments.getString("isup");
        type = arguments.getString("type");
        name = arguments.getString("name");

        return contentView;
    }

    public static HomeHeadFragment newInstances(DFTTTitleResult.DdBean ddBean) {
        HomeHeadFragment homeHeadFragment = new HomeHeadFragment();
        Bundle args = new Bundle();
        args.putString("isup", ddBean.getIsup());
        args.putString("type", ddBean.getType());
        args.putString("name", ddBean.getName());
        homeHeadFragment.setArguments(args);
        return homeHeadFragment;
    }



    private void requestServerShow(int size, DFTTNewsResult.DataBean dataBean) {
        String url = dataBean.getUrl();
        List<String> clickrep = dataBean.getClickrep();
        String reporturl = dataBean.getReporturl();
        List<String> showrep = dataBean.getShowrep();
        int isdsp = dataBean.getIsdsp();
        String position = dataBean.getAdidx() + size;
        String id = dataBean.getAdv_id();


        String reqeusturl = url;
        String reqeustPage = "1";

        if (isdsp == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(reporturl).append("\t");
            if (clickrep != null) {
                for (int i = 0; i < clickrep.size(); i++) {
                    stringBuilder.append(clickrep.get(i)).append("\t");
                }
            }
            if (showrep != null) {
                for (int i = 0; i < showrep.size(); i++) {
                    stringBuilder.append(showrep.get(i));
                    if (i != showrep.size() - 1) {
                        stringBuilder.append("\t");
                    }
                }

            }
            //联盟广告
            DFTTStatisticsApiService.pageUnionAdsOperate(reporturl, id, type, null, url, String.valueOf(position), null, stringBuilder.toString(), reqeustPage, new DfttApiServiceCallBack() {
                @Override
                public void onSuccess(String s) {
                    LogUtil.LogShitou("广告页success：", s.toString());

                }

                @Override
                public void onError(String s, String s1, Response response, Exception e) {
                    LogUtil.LogShitou("广告页error：", e.toString());

                }
            });
        } else {
            //dsp广告
            DFTTStatisticsApiService.pageDSPAdsClick(url, url, id, new DfttApiServiceCallBack() {
                @Override
                public void onSuccess(String s) {
                    LogUtil.LogShitou("广告页success：", s.toString());

                }

                @Override
                public void onError(String s, String s1, Response response, Exception e) {
//                    LogUtil.LogShitou("广告页：",s.toString()+"  "+s1.toString());
                    LogUtil.LogShitou("广告页error：", e.toString());


                }
            });
        }
    }

    /**
     * 显示对话框
     */
    public void showMyDialog() {
        animDialog = new MyProgressDialog(getActivity(), "玩命加载中...", R.drawable.loading);
        animDialog.show();
        animDialog.setCancelable(true);
//        animDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//
//            }
//        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isLogin = (boolean) SPUtils.get(UIUtils.getContext(), Constant.IS_LOGIN, false);
        mHomeHeadRecycleView = (RecyclerView) contentView.findViewById(R.id.home_recycleviewddd);
        mHomeHeadSwiperefresh = (SwipeRefreshLayout) contentView.findViewById(R.id.home_swiperefreshlayout);

        String isadv = (String) SPUtils.get(UIUtils.getContext(), Constant.IS_ADV, "");
        //如果是yes  展示服务器的   如果是no展示 百度联盟的
        if ("YES".equals(isadv)) {
            requestHeadServerDate();
        } else {
//            getDFTTBanner();//请求东方头条的banner

            requestBaiDuAdv();


        }

        requestHeadDate();//请求头部数据
        requestRobredWinningData();
        pagerNamber = 1;
        requestListDate(pagerNamber, 20);//请求东方头条新闻
        chi = 0;
        fatherListDate = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        mHomeHeadRecycleView.setLayoutManager(linearLayoutManager);


        mHomeHeadSwiperefresh.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);


        mHomeRecycleAdapter = new HomeHeadFragmentRecycleAdapter(mMainUi, fatherListDate);
        mHomeHeadRecycleView.setAdapter(mHomeRecycleAdapter);

        //滑动的监听
        mHomeHeadRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();

                    if (linearLayoutManager.getItemCount() <= 1) {
                        return;
                    }
                    if (lastVisiblePosition >= linearLayoutManager.getItemCount() - 1) {
                        if (mHomeRecycleAdapter != null) {
                            mHomeRecycleAdapter.showFootView();
                        }

//                        int i = Integer.parseInt(pagerNamber);
                        pagerNamber++;
//                        pagerNamber = i + "";
                        requestListDate(pagerNamber, 20);
                        chi = 0;

                    }

                }
            }
        });


        //false： 没有登录， true : 登录了
        mHomeRecycleAdapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {

                int itemViewType = mHomeRecycleAdapter.getItemViewType(postion + 1);
                if (itemViewType == mHomeRecycleAdapter.ITEM_TYPE_BIG_ADV ||
                        itemViewType == mHomeRecycleAdapter.ITEM_TYPE_ADV) {

//                    Intent intent = new Intent(getActivity(), DFTTAdvActivity.class);
//                    intent.putExtra("url", fatherListDate.get(postion).getUrl());
//                    intent.putExtra("title", fatherListDate.get(postion).getTopic());
//                    intent.putExtra("isdownload", fatherListDate.get(postion).getIsdownload() + "");
//                    intent.putExtra("isdsp", fatherListDate.get(postion).getIsdsp());
//                    intent.putExtra("reporturl", fatherListDate.get(postion).getReporturl());
//                    intent.putStringArrayListExtra("clickrep", (ArrayList<String>) fatherListDate.get(postion).getClickrep());
//                    intent.putStringArrayListExtra("showrep", (ArrayList<String>) fatherListDate.get(postion).getShowrep());
//                    intent.putExtra("position", fatherListDate.get(postion).getIdx() + "");
//                    intent.putExtra("id", fatherListDate.get(postion).getAdv_id());
//
//                    intent.putExtra("type", type);
//                    startActivity(intent);

                    NativeResponse nrAd = fatherListDate.get(postion).getNativeResponse();

                    nrAd.handleClick(view);



                    return;
                }

                if (fatherListDate.get(postion).getIssptopic().equals("1")) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("URL", fatherListDate.get(postion).getUrl());
                    intent.putExtra("title", fatherListDate.get(postion).getTopic());
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent(getActivity(), DFTTNewsDetailActivity.class);
                intent.putExtra("url", fatherListDate.get(postion).getUrl());
                intent.putExtra("issptopic", fatherListDate.get(postion).getIssptopic());
                intent.putExtra("title", fatherListDate.get(postion).getTopic());
                intent.putExtra("isVideo", fatherListDate.get(postion).getIsvideo());//1 是   0  不是
                intent.putExtra("videolink", fatherListDate.get(postion).getVideo_link());
                intent.putExtra("resource", fatherListDate.get(postion).getSource());
                intent.putExtra("img", fatherListDate.get(postion).getMiniimg().get(0).getSrc());
                intent.putExtra("type", type);
                intent.putExtra("page", pagerNamber);
                intent.putExtra("postion", postion);
                intent.putExtra("ishot", fatherListDate.get(postion).getHotnews());
                intent.putExtra("recomdType", fatherListDate.get(postion).getRecommendtype());
                intent.putExtra("recomdurl", fatherListDate.get(postion).getReporturl());
                intent.putExtra("supertop", fatherListDate.get(postion).getSuptop());
                startActivity(intent);
            }
        });

        mHomeRecycleAdapter.setOnHeadClickListener(new MyHeadViewClickListener() {
            @Override
            public void onHeadClick(View view) {
                switch (view.getId()) {
                    case R.id.home_rl_personal_information://个人中心
                        if (isLogin) {
                            startActivity(new Intent(mMainUi, DiamondAccountActivity.class));
                        } else {
                            mMainUi.startLogin();
                        }
                        break;
                    case R.id.home_rb_grab://抢红包
                        if (isLogin) {
                            Intent intent = new Intent(mMainUi, GrabRedActivity.class);
                            startActivity(intent);
                        } else {
                            mMainUi.startLogin();
                        }
                        break;
                    case R.id.home_rb_send://发红包
                        if (isLogin) {
                            Intent sendIntent = new Intent(mMainUi, SendRedActivity.class);
                            startActivity(sendIntent);
                        } else {
                            mMainUi.startLogin();
                        }

                        break;
                    case R.id.home_rb_find_favorable://找优惠
                        if (isLogin) {

                            Intent intent = new Intent(mMainUi, ContactActivity.class);
                            startActivity(intent);
                        } else {
                            mMainUi.startLogin();
                        }

                        break;
                    case R.id.home_rb_spell_luck://拼手气
                        if (isLogin) {
                            Intent luckyIntent = new Intent(mMainUi, OneyuanActivity.class);
                            startActivity(luckyIntent);
                        } else {
                            mMainUi.startLogin();
                        }

                        break;

                }
            }
        });

        mHomeHeadSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mHomeRecycleAdapter != null) {
                    fatherListDate.clear();
                    mHomeRecycleAdapter.hintFootView();
                    mHomeRecycleAdapter.notifyDataSetChanged();

                    chi+=1;
                    requestListDate(fuPage * chi, fuIndex * chi);

                }

            }
        });

        /**
         * 东方头条banner
         * http://test-lianmeng.dftoutiao.com/admethod/appad
         *
         */


//        requestDFTTBanner();
//        requestCacheBanner();

    }
    private NativeResponse mNrAd;

    List<NativeResponse> nrAdList = new ArrayList<NativeResponse>();
    private static String YOUR_AD_PLACE_ID = "4510621"; // 双引号中填写自己的广告位ID
    private void requestBaiDuAdv() {
        /**
        * Step 1. 创建BaiduNative对象，参数分别为：
        * 上下文context，广告位ID, BaiduNativeNetworkListener监听（监听广告请求的成功与失败）
        * 注意：请将YOUR_AD_PALCE_ID替换为自己的广告位ID
         */
        BaiduNative baidu = new BaiduNative(getActivity(), YOUR_AD_PLACE_ID,
                new BaiduNative.BaiduNativeNetworkListener() {

                    @Override
                    public void onNativeFail(NativeErrorCode arg0) {
//                        Toast.makeText(getActivity(), "没有收到轮播模板广告，请检查", Toast.LENGTH_LONG).show();
                        requestBaiDuAdv();

                    }

                    @Override
                    public void onNativeLoad(List<NativeResponse> arg0) {
                        if (arg0 != null && arg0.size() > 0) {
                            nrAdList = arg0;

                            mNrAd = nrAdList.get(0);

                            if (mNrAd.getMaterialType() == NativeResponse.MaterialType.HTML) {
//                                Toast.makeText(getActivity(), "收到轮播模板广告.", Toast.LENGTH_LONG).show();
                                Message obtain = Message.obtain();
                                obtain.what = SHOW_HTML;
                                handler.sendMessage(obtain);
                            } else {
//                                Toast.makeText(getActivity(),
//                                        "收到广告,但不是模板广告,请检查", Toast.LENGTH_LONG)
//                                        .show();
                            }
                        }
                    }

                });

        /**
         * Step 2. 创建requestParameters对象，并将其传给baidu.makeRequest来请求广告
         */


        int width = widthPixels;
//        double scral = 1/2;
        int height = (int) ((width/2)+0.5f);
        RequestParameters requestParameters = new RequestParameters.Builder()


                .setWidth((int) width)
                .setHeight((int) height)
//                .setWidth((int) (360*3))
//                .setHeight((int) (250*3))
                .downloadAppConfirmPolicy(
                        RequestParameters.DOWNLOAD_APP_CONFIRM_ONLY_MOBILE) // 用户点击下载类广告时，是否弹出提示框让用户选择下载与否
                .build();

        baidu.makeRequest(requestParameters);

    }

    /**
     * 东方头条缓存接口请求数据
     */
    private void requestCacheBanner() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();


        /**
         * http://test-lianmeng.dftoutiao.com/admethod/appad
         * <p>
         * http://test-lianmeng.dftoutiao.com/admethod/appad?
         * pgtype=list
         * &newstype=null
         * &url=null
         * &pgnum=1
         * &param=jinhoubao%09jinhoubao%09DC2CD0E8-508E-4743-A0C6-9DAC6147E079%09appstore%09
         * jinhoubao%09
         * 3.0.3%09
         * iOS10.2%09
         * %09030003%09
         * C26DA222-2CE0-470A-A61D-62263EEFE471
         */
        int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素，如：480px）
        int screenHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高（像素，如：800p）
        String localIpAddress = getLocalIpAddress();


        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();//imei   devicestoken
        String id = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_ID, "");
        String lat = (String) SPUtils.get(UIUtils.getContext(), Constant.LATITUDE, "");//经纬度
        String lng = (String) SPUtils.get(UIUtils.getContext(), Constant.LONGITUDE, "");

        String release = Build.VERSION.RELEASE;//版本号 4.4.4
        String ANDROID_ID = Settings.System.getString(getActivity().getContentResolver(), Settings.System.ANDROID_ID);

        String model = Build.MODEL;//设备号
        String manufacturer = Build.MANUFACTURER;//设备厂商
        long l = System.currentTimeMillis();
//        String defaultUserAgent = WebSettings.getDefaultUserAgent(getActivity());
//        WebSettings.get();

        WebView webView = new WebView(getActivity());
        String userAgentString = webView.getSettings().getUserAgentString();
        LogUtil.LogShitou("defaultUserAgent:", userAgentString);

        String longTime = String.valueOf(l);
        StringBuilder sb = new StringBuilder();
        String url = "http://test-nativeadv.dftoutiao.com/union/api?";

        sb.append(url).append("soltid=" + "");
        sb.append("&solttype=103").append("&slotheight=320").append("&slotwidth=800").append("&deviceid=" + DEVICE_ID)
                .append("&devicetype=1").append("&vendor=" + manufacturer).append("&model=" + model).append("&devicewidth=" + screenWidth).append("&deviceheight=" + screenHeight)
                .append("&imei=" + DEVICE_ID).append("&os=Android").append("&osver=" + release).append("&msc=" + localIpAddress).append("&network=0").append("&operatortype=0");

        sb.append("&softtype=jinhoubao").append("&softname=jinhoubao").append("&position=1").append("&srcurl=").append("&qid=360")
                .append("&typeid=jinhoubao").append("&appver=" + release).append("&ttaccid=" + id).append("&lat=" + lat).append("&lng=" + lng)
                .append("&coordtime=" + longTime).append("&useragent=" + userAgentString).append("&currentcache=-1");
//        sb.append("jinhoubao\t").append("jinhoubao\t").append(ANDROID_ID+"\t").append("360\t").append("jinhoubao\t")
//                .append("2.0.5\t").append(release+"\t").append(id+"\t").append("020005").append(DEVICE_ID+"\t");


        LogUtil.LogShitou("东方头条缓存接口请求数据:", sb.toString());
        JSONObject jsonObject = new JSONObject();
        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(sb.toString())
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                UIUtils.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                LogUtil.LogShitou("requestDFTTBanner:      ", result.toString());
//                Message msg = handler.obtainMessage();
//                msg.what = GET_HOME_SERVICE_BANNER;
//                msg.obj = result;
//                handler.sendMessage(msg);
            }
        });
    }


    //获取本地IP
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
//            Log.e("WifiPreference IpAddress", ex.toString());
        }


        return "";
    }

    /**
     * 东方头条
     * http://test-lianmeng.dftoutiao.com/admethod/appad
     * <p/>
     * http://test-lianmeng.dftoutiao.com/admethod/appad?
     * pgtype=list
     * &newstype=null
     * &url=null
     * &pgnum=1
     * &param=jinhoubao%09jinhoubao%09DC2CD0E8-508E-4743-A0C6-9DAC6147E079%09appstore%09
     * jinhoubao%09
     * 3.0.3%09
     * iOS10.2%09
     * %09030003%09
     * C26DA222-2CE0-470A-A61D-62263EEFE471
     */
    private void requestDFTTBanner() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        StringBuilder sb = new StringBuilder();
        String url = "http://test-lianmeng.dftoutiao.com/admethod/appad?";
        sb.append(url).append("pgtype=list&newstype=&newstype=&pgnum=1&param=");
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();//iME
        String id = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_ID, "");
        String release = Build.VERSION.RELEASE;
        String ANDROID_ID = Settings.System.getString(getActivity().getContentResolver(), Settings.System.ANDROID_ID);

        sb.append("jinhoubao\t").append("jinhoubao\t").append(DEVICE_ID + "\t").append("360\t").append("jinhoubao\t")
                .append("2.0.5\t").append("Android " + release + "\t").append(id + "\t").append("020005\t").append(DEVICE_ID + "\t");

        JSONObject jsonObject = new JSONObject();
        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(sb.toString())
                .post(body)
                .build();
        LogUtil.LogShitou("东方头条数据接口请求数据:", sb.toString());


        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                UIUtils.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                LogUtil.LogShitou("requestDFTTBanner:      ", result.toString());
//                Message msg = handler.obtainMessage();
//                msg.what = GET_HOME_SERVICE_BANNER;
//                msg.obj = result;
//                handler.sendMessage(msg);
            }
        });


    }

    private void requestListDate(final int page, int index) {
        showMyDialog();
        DFTTNewsApiService.getNewsListByType(type, page, index, new DfttApiServiceCallBack() {
            @Override
            public void onSuccess(String s) {
                LogUtil.LogShitou("新闻数据：", s.toString());
                Message msg = handler.obtainMessage();
                msg.what = GET_HOME_LIST;
                msg.obj = s;
                pagesdssss = page;
                handler.sendMessage(msg);
            }


            @Override
            public void onError(String s, String s1, Response response, Exception e) {
                if(animDialog!=null) {
                    animDialog.dismiss();
                }
            }
        });


    }


    /**
     * 请求头部数据
     */
    private void requestHeadServerDate() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.HOME_PAGE)
                .post(body)
                .build();


        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                UIUtils.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = GET_HOME_SERVICE_BANNER;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 请求中奖数据接口，放入轮播的textSwitcher内
     */
    private void requestRobredWinningData() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(BaseApplication.getInstance());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.HOME_WINNING_INFO)
                .post(body)
                .build();


        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                    onFailer("网络请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
//                        if (handler != null) {
                    HomeWinningResponse info = new Gson().fromJson(response.body().string(), HomeWinningResponse.class);
                    if ("success".equals(info.getResult())) {
                        Message msg = handler.obtainMessage();
                        msg.what = Constant.RESULT_HOME_WINNING;
                        msg.obj = info;
                        handler.sendMessage(msg);
                    }
//                            }else{
//                                onFailer(info.getErrorMsg());
//                            }
//                        }else{
//                            onFailer("返回数据失效");
//                        }
//                    }else{
//                        onFailer("服务器异常 " + response.code());
//                    }
//                }
                }
            }
        });

    }


    /**
     * 请求头部数据
     */
    private void requestHeadDate() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.HOME_PAGE)
                .post(body)
                .build();


        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                UIUtils.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = GET_HOME;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    public void requestAdvDate(int page) {
        DFTTAdsApiService.getAdvertisement("list", type, null, page + "", new DfttApiServiceCallBack() {
            @Override
            public void onSuccess(String s) {
                LogUtil.LogShitou("广告：", s.toString());
                Message msg = handler.obtainMessage();
                msg.what = GET_ADV;
                msg.obj = s;
                handler.sendMessage(msg);

            }

            @Override
            public void onError(String s, String s1, Response response, Exception e) {

            }
        });

    }


    private void getDFTTBanner() {
        DFTTAdsApiService.getAdvertisement("list", "toutiao", null, "1", new DfttApiServiceCallBack() {
            @Override
            public void onSuccess(String s) {
                DFTTAdvResult banner = new Gson().fromJson(s, DFTTAdvResult.class);
                Message msg = handler.obtainMessage();
                msg.what = GET_DFBANNER;
                msg.obj = banner;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(String s, String s1, Response response, Exception e) {

            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isLogin = (boolean) SPUtils.get(UIUtils.getContext(), Constant.IS_LOGIN, false);
        if (!hidden) {//显示的时候
            if (mHomeRecycleAdapter.hRollingView != null) {
                if (!mHomeRecycleAdapter.hRollingView.isScrolling()) {///没有滚动的时候
                    mHomeRecycleAdapter.hRollingView.pause();//先暂停

                    mHomeRecycleAdapter.hRollingView.resume();//再开始
                }
            }
        } else {
            if (mHomeRecycleAdapter.hRollingView != null) {//隐藏的时候
                mHomeRecycleAdapter.hRollingView.pause();//直接暂停
            }
        }

        LogUtil.LogShitou(TAG,"onHiddenChanged");

    }

    private static final String TAG = HomeHeadFragment.class.getSimpleName();
    @Override
    public void onResume() {
        super.onResume();
        isLogin = (boolean) SPUtils.get(UIUtils.getContext(), Constant.IS_LOGIN, false);
        LogUtil.LogShitou(TAG,"onResume");
        if(mHomeRecycleAdapter.hRollingView!=null){


        if (!mHomeRecycleAdapter.hRollingView.isScrolling()) {///没有滚动的时候
            mHomeRecycleAdapter.hRollingView.pause();//先暂停

            mHomeRecycleAdapter.hRollingView.resume();//再开始
        }  }
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.LogShitou(TAG,"onPause");
        if (mHomeRecycleAdapter.hRollingView != null) {//隐藏的时候
            mHomeRecycleAdapter.hRollingView.pause();//直接暂停
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.LogShitou(TAG,"onStart");

    }


}

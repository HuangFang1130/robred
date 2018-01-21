package com.jiahehongye.robred.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mobad.feeds.BaiduNative;
import com.baidu.mobad.feeds.NativeErrorCode;
import com.baidu.mobad.feeds.NativeResponse;
import com.baidu.mobad.feeds.RequestParameters;
import com.google.gson.Gson;
import com.jiahehongye.robred.BaseFragment;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.DFTTNewsDetailActivity;
import com.jiahehongye.robred.adapter.HomeChildFragmentRecycleAdapter;
import com.jiahehongye.robred.bean.DFTTAdvResult;
import com.jiahehongye.robred.bean.DFTTNewsResult;
import com.jiahehongye.robred.bean.DFTTTitleResult;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.LogUtil;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.MyProgressDialog;
import com.songheng.newsapisdk.sdk.apiservice.DFTTAdsApiService;
import com.songheng.newsapisdk.sdk.apiservice.DFTTNewsApiService;
import com.songheng.newsapisdk.sdk.apiservice.DFTTStatisticsApiService;
import com.songheng.newsapisdk.sdk.apiservice.listener.DfttApiServiceCallBack;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by huangjunhui on 2017/5/18.10:34
 *
 *
 */
public class HomeChildFragment extends BaseFragment {
    private static final int GET_HOME = 0000;
    private static final int GET_HOME_LIST = 200;
    private static final int GET_ADV = 400;
    private int pagerNamber = 1;
    private static final int BAIDU_XINXILIU_ADV_CHILDREN = 900;

    private int fuPage  = -1;
    private  int fuIndex = -20;
    private  int chi = 0;
    private ArrayList<DFTTNewsResult.DataBean> fatherListDate;
    private boolean isLogin;
    private HomeChildFragmentRecycleAdapter mHomeRecycleAdapter;
    private PersistentCookieStore persistentCookieStore;
    private String isup;
    private String type;
    private String name;
    private MyProgressDialog animDialog;
    private View contentView;
    private SwipeRefreshLayout mHomeHeadSwiperefresh;
    private RecyclerView mHomeHeadRecycleView;
    private LinearLayoutManager linearLayoutManager;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case GET_HOME_LIST:
//                    if (animDialog.isShowing()) {
//                        animDialog.dismiss();
//                    }
                    if (mHomeHeadSwiperefresh != null) {
                        if (mHomeHeadSwiperefresh.isRefreshing()) {
                            mHomeHeadSwiperefresh.setRefreshing(false);
                        }
                    }
                    String result = (String) msg.obj;
                    DFTTNewsResult dfttNewsResult = new Gson().fromJson(result, DFTTNewsResult.class);
                    data = dfttNewsResult.getData();
//                    requestAdvDate(pagesdssss);
                    requestBaiDuLiuAdv();
//                    if (fatherListDate == null) {
//                        fatherListDate = new ArrayList<>();
//                    }
//                    fatherListDate.addAll(data);
//                    mHomeRecycleAdapter.notifyDataSetChanged();


                    break;
                case GET_ADV:
//                    if (animDialog.isShowing()) {
//                        animDialog.dismiss();
//                    }
                    if (mHomeHeadSwiperefresh != null) {
                        if (mHomeHeadSwiperefresh.isRefreshing()) {
                            mHomeHeadSwiperefresh.setRefreshing(false);
                        }
                    }
                    String result3 = (String) msg.obj;
                    DFTTAdvResult dfttAdvResult = new Gson().fromJson(result3, DFTTAdvResult.class);
                    int dd = 0;
                    List<DFTTNewsResult.DataBean> data1 = dfttAdvResult.getData();
                    for (int i = 0; i < data1.size(); i++) {
                        requestServerShow(fatherListDate.size(),data1.get(i));
                    }

                    /**
                     * 遍历最外面的。然后在第几个位置停下来
                     *  取第一个位置的数据放进来
                     */
                    if (data!=null){
                        for (int i = 0; i < data.size(); i++) {
                            if (i == 3 || i == 8 || i == 13 || i == 18) {
                                if(data1.size()==0){
                                    break;
                                }

                                if (i == 3 || i == 13) {
                                    DFTTNewsResult.DataBean dataBean = data1.get(0);
                                    dataBean.setBigpic("1");
                                }
                                data.remove(i);
                                data.add(i, data1.get(0));
                                data1.remove(0);
                                continue;
                            }

                        }
                        fatherListDate.addAll(data);
                        mHomeRecycleAdapter.notifyDataSetChanged();

                    }





//                    if(fatherListDate==null){
//                        fatherListDate = new ArrayList<>();
//                    }
//                    for (int i = 0; i < data1.size(); i++) {
//                        fatherListDate.add(data1.get(i));
//                    }
//                    mHomeRecycleAdapter.setAdvDate(data1);

                    mHomeRecycleAdapter.notifyDataSetChanged();


                    break;


                case BAIDU_XINXILIU_ADV_CHILDREN:
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
                                NativeResponse nativeResponse = bdAdv.get(0);//4个
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
                        mHomeRecycleAdapter.notifyDataSetChanged();
                    }


                    break;


            }
        }
    };
    private List<DFTTNewsResult.DataBean> data;
    private int pagesdssss = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_children, null);
        Bundle arguments = getArguments();
        isup = arguments.getString("isup");
        type = arguments.getString("type");
        name = arguments.getString("name");

        return contentView;
    }

    public static HomeChildFragment newInstances(DFTTTitleResult.DdBean ddBean) {

        HomeChildFragment homeChildFragment = new HomeChildFragment();
        Bundle args = new Bundle();
        args.putString("isup", ddBean.getIsup());
        args.putString("type", ddBean.getType());
        args.putString("name", ddBean.getName());
        homeChildFragment.setArguments(args);
        return homeChildFragment;
    }

    private void requestServerShow(int size, DFTTNewsResult.DataBean dataBean) {
        String url = dataBean.getUrl();
        List<String> clickrep = dataBean.getClickrep();
        String reporturl = dataBean.getReporturl();
        List<String> showrep = dataBean.getShowrep();
        int isdsp = dataBean.getIsdsp();
        String position = dataBean.getAdidx()+size;
        String id = dataBean.getAdv_id();


        String reqeusturl= url;
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
            DFTTStatisticsApiService.pageUnionAdsOperate(reporturl,id, type, null, url, String.valueOf(position), null, stringBuilder.toString(), reqeustPage, new DfttApiServiceCallBack() {
                @Override
                public void onSuccess(String s) {
                    LogUtil.LogShitou("广告页success：",s.toString());

                }

                @Override
                public void onError(String s, String s1, Response response, Exception e) {
                    LogUtil.LogShitou("广告页error：",e.toString());

                }
            });
        } else {
            //dsp广告
            DFTTStatisticsApiService.pageDSPAdsClick(url, url, id, new DfttApiServiceCallBack() {
                @Override
                public void onSuccess(String s) {
                    LogUtil.LogShitou("广告页success：",s.toString());

                }

                @Override
                public void onError(String s, String s1, Response response, Exception e) {
//                    LogUtil.LogShitou("广告页：",s.toString()+"  "+s1.toString());
                    LogUtil.LogShitou("广告页error：",e.toString());


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
        animDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
//                if (call.isExecuted()) {
//                    call.cancel();
//                }
            }
        });
    }



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
            }

            @Override
            public void onNativeLoad(List<NativeResponse> arg0) {
                // 一个广告只允许展现一次，多次展现、点击只会计入一次
                if (arg0 != null && arg0.size() > 0) {
//                    List<NativeResponse>  nrAdList = arg0;
//                    Toast.makeText(getActivity(), nrAdList.size()+"", Toast.LENGTH_SHORT).show();
//                    insertAdToRecycle(nrAdList);
                    Message obtain = Message.obtain();
                    obtain.what = BAIDU_XINXILIU_ADV_CHILDREN;
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
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        showMyDialog();
        mHomeHeadRecycleView = (RecyclerView) contentView.findViewById(R.id.home_recycleviewddd);
        mHomeHeadSwiperefresh = (SwipeRefreshLayout) contentView.findViewById(R.id.home_swiperefreshlayout);
        pagerNamber = 1;
        requestListDate(pagerNamber, 20);
        chi = 0;
        fatherListDate = new ArrayList<>();
        isLogin = (boolean) SPUtils.get(UIUtils.getContext(), Constant.IS_LOGIN, false);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        mHomeHeadRecycleView.setLayoutManager(linearLayoutManager);


        mHomeHeadSwiperefresh.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);


        mHomeRecycleAdapter = new HomeChildFragmentRecycleAdapter(mMainUi, fatherListDate);
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

                        pagerNamber++;
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
                int itemViewType = mHomeRecycleAdapter.getItemViewType(postion);
                if (itemViewType==mHomeRecycleAdapter.ITEM_TYPE_BIG_ADV||
                        itemViewType==mHomeRecycleAdapter.ITEM_TYPE_ADV){

//                    Intent intent = new Intent(getActivity(),DFTTAdvActivity.class);
//                    intent.putExtra("url", fatherListDate.get(postion).getUrl());
//                    LogUtil.LogShitou("url:",fatherListDate.get(postion).getUrl().toString());
//
//                    intent.putExtra("isdownload",fatherListDate.get(postion).getIsdownload()+"");
//                    intent.putExtra("isdsp",fatherListDate.get(postion).getIsdsp());
//                    intent.putExtra("reporturl",fatherListDate.get(postion).getReporturl());
//                    intent.putStringArrayListExtra("clickrep", (ArrayList<String>) fatherListDate.get(postion).getClickrep());
//                    intent.putStringArrayListExtra("showrep", (ArrayList<String>) fatherListDate.get(postion).getShowrep());
//                    intent.putExtra("position",fatherListDate.get(postion).getIdx()+"");
//                    intent.putExtra("id",fatherListDate.get(postion).getAdv_id());
//
//
//                    intent.putExtra("title",fatherListDate.get(postion).getTopic());
//                    intent.putExtra("type",type);
//                    startActivity(intent);

                    NativeResponse nrAd = fatherListDate.get(postion).getNativeResponse();
                    nrAd.handleClick(view);
                    return;
                }

                Intent intent = new Intent(getActivity(), DFTTNewsDetailActivity.class);
                intent.putExtra("issptopic", fatherListDate.get(postion).getIssptopic());
                intent.putExtra("url", fatherListDate.get(postion).getUrl());
                intent.putExtra("title", fatherListDate.get(postion).getTopic());
                intent.putExtra("isVideo",fatherListDate.get(postion).getIsvideo());//1 是   0  不是
                intent.putExtra("videolink",fatherListDate.get(postion).getVideo_link());
                intent.putExtra("resource",fatherListDate.get(postion).getSource());
                intent.putExtra("img",fatherListDate.get(postion).getMiniimg().get(0).getSrc());
                intent.putExtra("type", type);
                intent.putExtra("page",pagerNamber);
                intent.putExtra("postion",postion);
                intent.putExtra("ishot",fatherListDate.get(postion).getHotnews());
                intent.putExtra("recomdType",fatherListDate.get(postion).getRecommendtype());
                intent.putExtra("recomdurl",fatherListDate.get(postion).getReporturl());
                intent.putExtra("supertop",fatherListDate.get(postion).getSuptop());
                startActivity(intent);
            }
        });


        mHomeHeadSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mHomeRecycleAdapter != null) {
                    fatherListDate.clear();
                    mHomeRecycleAdapter.notifyDataSetChanged();
                    chi++;
                    requestListDate(fuPage*chi, fuIndex*chi);
                }

            }
        });

    }


    private void requestListDate(final int page, int index) {
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

            }
        });


//        DFTTAdsApiService. getDspAdvertisements("list", type, null, page+"", new DfttApiServiceCallBack() {
//            @Override
//            public void onSuccess(String s) {
//                LogUtil.LogShitou("DSP广告：",s.toString());
//
//            }
//
//            @Override
//            public void onError(String s, String s1, Response response, Exception e) {
//
//            }
//        });
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

}

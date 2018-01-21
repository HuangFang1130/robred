package com.jiahehongye.robred.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.google.gson.Gson;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.adapter.RedPersonalDetailAdapter;
import com.jiahehongye.robred.bean.DFTTAdvResult;
import com.jiahehongye.robred.bean.DFTTNewsResult;
import com.jiahehongye.robred.bean.GrabDetailPeopleDetailResult;
import com.jiahehongye.robred.bean.GrabDetailPeopleListResult;
import com.jiahehongye.robred.bean.RedDetailBannerResult;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.db.Model;
import com.jiahehongye.robred.interfaces.MyHeadViewClickListener;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.LogUtil;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.DividerItemDecoration;
import com.songheng.newsapisdk.sdk.apiservice.DFTTAdsApiService;
import com.songheng.newsapisdk.sdk.apiservice.listener.DfttApiServiceCallBack;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONException;
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

/**
 * Created by huangjunhui on 2016/12/2.17:09
 *
 *     红包详情页
 *
 *   个人红包详情   商家红包详情
 */
public class RedDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final int GRAB_SERVICE_BANNER = 5000;
    private String pagerNumber = "1";
    private static final int GRAB_PEOPEL_DETAIL = 200;
    private static final int GRAB_PEOPLE_LIST = 201;
    private RecyclerView mRedDetailRecycle;
    private LinearLayoutManager linearLayoutManager;
    private List<GrabDetailPeopleListResult.DataBean.GrabRedEnveListBean> fatherArrayList;
    private RedPersonalDetailAdapter redPersonalDetailAdapter;
    private String type;
    private String redEnveCode;
    private String redEnveMark;
    private GrabDetailPeopleDetailResult grabDetailPeopleDetailResult;
    private static final int GET_DFBANNER = 49;

    private List<DFTTNewsResult.DataBean> bannerdata;
    private Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case GET_DFBANNER:
                    String result4 = (String) msg.obj;
                    DFTTAdvResult banner = new Gson().fromJson(result4, DFTTAdvResult.class);
                    bannerdata = banner.getData();
                    redPersonalDetailAdapter.setAdv(bannerdata);
                    redPersonalDetailAdapter.notifyDataSetChanged();
                    break;

                case GRAB_PEOPLE_LIST://列表
                    String result = (String) msg.obj;
                    grabListResult = new Gson().fromJson(result, GrabDetailPeopleListResult.class);
                    if (grabListResult.getResult().equals("success")) {
                        List<GrabDetailPeopleListResult.DataBean.GrabRedEnveListBean> grabRedEnveList =
                                grabListResult.getData().getGrabRedEnveList();
                        fatherArrayList.addAll(grabRedEnveList);
                    } else if (grabListResult.getResult().equals("fail")) {
                        Toast.makeText(RedDetailActivity.this, "解析失败", Toast.LENGTH_SHORT).show();
                    }
                    redPersonalDetailAdapter.hintFootView();
                    redPersonalDetailAdapter.notifyDataSetChanged();

                    break;

                case GRAB_PEOPEL_DETAIL://头部数据
                    String detialResult = (String) msg.obj;
                    grabDetailPeopleDetailResult = new Gson().fromJson(detialResult, GrabDetailPeopleDetailResult.class);
                    if (grabDetailPeopleDetailResult.getResult().equals("success")) {
                        redPersonalDetailAdapter.setHeadDate(grabDetailPeopleDetailResult,type,redEnveCode,redEnveMark,bannerADview);
                    } else if (grabDetailPeopleDetailResult.getResult().equals("fail")) {
                        Toast.makeText(RedDetailActivity.this, "解析失败", Toast.LENGTH_SHORT).show();
                    }
                    mRedDetailRecycle.setAdapter(redPersonalDetailAdapter);
                    redPersonalDetailAdapter.notifyDataSetChanged();
                    break;

                case GRAB_SERVICE_BANNER:
                    String result5 = (String) msg.obj;
                    LogUtil.LogShitou("reddetailbanner:",result5);
                    RedDetailBannerResult bannerds = new Gson().fromJson(result5, RedDetailBannerResult.class);
                    bannerdata = new ArrayList<>();
                    DFTTNewsResult.DataBean dataBean = new DFTTNewsResult.DataBean();
                    List<DFTTNewsResult.DataBean.MiniimgBean> objects = new ArrayList<>();
                    DFTTNewsResult.DataBean.MiniimgBean miniimgBean = new DFTTNewsResult.DataBean.MiniimgBean();
                    miniimgBean.setSrc(bannerds.getData().getDefaultAdImgUri());
                    objects.add(0,miniimgBean);
                    dataBean.setMiniimg(objects);
                    bannerdata.add(0,dataBean);
                    redPersonalDetailAdapter.setAdv(bannerdata);
                    redPersonalDetailAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private GrabDetailPeopleListResult grabListResult;
    private AdView bannerADview;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        setContentView(R.layout.activity_red_personal_detail);
        Intent intent = getIntent();

        type = intent.getStringExtra("type");//商家红包，还是个人红包
        redEnveCode = intent.getStringExtra("redEnveCode");//红包编号
        redEnveMark = intent.getStringExtra("redEnveMark");//红包状态

        LogUtil.LogShitou("                 type:"+type+"     红包编号:"+redEnveCode+"         红包状态: "+redEnveMark);
        initView();

        String IS_ADV_BANNER_RED_DETAIL = (String) SPUtils.get(UIUtils.getContext(), Constant.IS_ADV_BANNER_RED_DETAIL, "");
        if("YES".equals(IS_ADV_BANNER_RED_DETAIL)){
            getServiceBanner();
        }else {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    getBaiduBanner();//百度banner  横幅banner
                }
            }).start();
//          getDFBanner();
        }
        requestDate();
        pagerNumber = "1";
        requestListDate(pagerNumber);

    }

    private void getBaiduBanner() {
//        AppActivity.setActionBarColorTheme(AppActivity.ActionBarColorTheme.ACTION_BAR_WHITE_THEME);
//        另外，也可设置动作栏中单个元素的颜色, 颜色参数为四段制，0xFF(透明度, 一般填FF)DE(红)DA(绿)DB(蓝)
//        AppActivity.getActionBarColorTheme().set[Background|Title|Progress|Close]Color(0xFFDEDADB);

        // 创建广告View
        String adPlaceId = "4510761"; //  重要：请填上您的广告位ID，代码位错误会导致无法请求到广告
        bannerADview = new AdView(this, adPlaceId);
        // 设置监听器
        bannerADview.setListener(new AdViewListener() {
            public void onAdSwitch() {
                Log.w("", "onAdSwitch");
            }

            public void onAdShow(JSONObject info) {
                // 广告已经渲染出来
                Log.w("", "onAdShow " + info.toString());
            }

            public void onAdReady(AdView adView) {
                // 资源已经缓存完毕，还没有渲染出来
                Log.w("", "onAdReady " + adView);
            }

            public void onAdFailed(String reason) {
                Log.w("", "onAdFailed " + reason);
            }

            public void onAdClick(JSONObject info) {
                // Log.w("", "onAdClick " + info.toString());

            }

            @Override
            public void onAdClose(JSONObject arg0) {
                Log.w("", "onAdClose");
            }
        });
//        // 将adView添加到父控件中(注：该父控件不一定为您的根控件，只要该控件能通过addView能添加广告视图即可)
//        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT);
//        rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        yourOriginnalLayout.addView(adView, rllp);

//        redPersonalDetailAdapter.setHengFuBanner(adView);
//        redPersonalDetailAdapter.notifyDataSetChanged();



    }

    private void getServiceBanner() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .addHeader("Content-Type","application/json; charset=utf-8")
                .url(Constant.GET_RED_DETAIL_BANNER)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RedDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( RedDetailActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = GRAB_SERVICE_BANNER;
                msg.obj = result;
                handler.sendMessage(msg);
                LogUtil.LogShitou("requestListDate",result);

            }
        });
    }

    /**
     * 请求列表的数据
     * @param pagerNumber
     */
    private void requestListDate(String pagerNumber) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("redEnveCode", redEnveCode);
            jsonObject.put("pageSize", 20+"");
            jsonObject.put("pageNumber", pagerNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .addHeader("Content-Type","application/json; charset=utf-8")
                .url(Constant.GRAB_RED_DETAIL_LIST)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RedDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( RedDetailActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = GRAB_PEOPLE_LIST;
                msg.obj = result;
                handler.sendMessage(msg);
                LogUtil.LogShitou("requestListDate",result);

            }
        });
    }

    /**
     * 请求头部数据
     */
    private void requestDate() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("redEnveCode", redEnveCode);
            LogUtil.LogShitou("                       红包的code: ",redEnveCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .addHeader("Content-Type","application/json; charset=utf-8")
                .url(Constant.GRAB_RED_DETAIL)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                RedDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( RedDetailActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = GRAB_PEOPEL_DETAIL;
                msg.obj = result;
                handler.sendMessage(msg);
                LogUtil.LogShitou(result);

            }
        });

    }

    private void initView() {
        ImageView mBack = (ImageView) findViewById(R.id.red_detail_back_row);
        mBack.setOnClickListener(this);
        mRedDetailRecycle = (RecyclerView) findViewById(R.id.red_detail_recycleview);

        linearLayoutManager = new LinearLayoutManager(this);
        mRedDetailRecycle.setLayoutManager(linearLayoutManager);
        mRedDetailRecycle.setItemAnimator(new DefaultItemAnimator());
        mRedDetailRecycle.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL_LIST));
        fatherArrayList = new ArrayList<>();
        redPersonalDetailAdapter = new RedPersonalDetailAdapter(this, fatherArrayList);
        mRedDetailRecycle.setAdapter(redPersonalDetailAdapter);

        //滑动的监听
        mRedDetailRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();

                    if (linearLayoutManager.getItemCount() <= 1) {
                        return;
                    }
                    if (lastVisiblePosition >= linearLayoutManager.getItemCount() - 1) {
                        if(redPersonalDetailAdapter!=null){
                            redPersonalDetailAdapter.showFootView();
                        }

                        int i = Integer.parseInt(pagerNumber);
                        i++;
                        pagerNumber = i + "";
                        requestListDate(pagerNumber);
                    }

                }
            }
        });


        redPersonalDetailAdapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent = new Intent(RedDetailActivity.this,ContanctDetailActivity.class);
                String mobile = fatherArrayList.get(postion - 1).getUserName();
                intent.putExtra("mobile",mobile);
                startActivity(intent);
            }
        });

        redPersonalDetailAdapter.setOnHeadClickListener(new MyHeadViewClickListener() {
            @Override
            public void onHeadClick(View view) {
                switch (view.getId()) {
                    case R.id.red_detail_tv_chat://聊天

                        GrabDetailPeopleDetailResult.DataLBean.MemberLBean memberBean = grabDetailPeopleDetailResult.getData().getMember().get(0);
                        String mobile = memberBean.getMobile();
                        String nickName = memberBean.getNickName();
                        String userPhoto = memberBean.getUserPhoto();

                        EaseUser user = new EaseUser(mobile);
                        user.setNick(nickName);
                        user.setAvatar(userPhoto);
                        new Model(UIUtils.getContext()).saveContact(user);

                        Intent intent = new Intent(RedDetailActivity.this,ChatActivity.class);
                        intent.putExtra("Name",nickName);
                        intent.putExtra("Avatar",userPhoto);
                        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE,1);
                        intent.putExtra(EaseConstant.EXTRA_USER_ID,mobile);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.red_detail_tv_look://邀请好友
                        GrabDetailPeopleDetailResult.DataLBean.CurrentGrabRedEnveLBean currentGrabRedEnve = grabDetailPeopleDetailResult.getData().getCurrentGrabRedEnve();
                        String redEnveCode = currentGrabRedEnve.getRedEnveCode();
                        String userid  = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_ID, "");
                        String shareUrl = Constant.URL + "/wap/redEnveShare/redEnveShare.jhtml?" + "redEnveCode=" + redEnveCode + "&upperShareUser="
                                + userid + "&shareLevel=" + "1" + "&channelSource=" + "2";
                        UMImage image = new UMImage(RedDetailActivity.this, R.drawable.logo);//设置分享图片
                        UMImage thumb = new UMImage(RedDetailActivity.this, R.drawable.logo);//设置缩略图
                        image.compressStyle = UMImage.CompressStyle.SCALE;
                        image.setThumb(thumb);
                        image.compressStyle = UMImage.CompressStyle.SCALE;
                        image.setThumb(thumb);
                        ShareAction mAction = new ShareAction(RedDetailActivity.this)
                                .withTitle("金猴宝红包")
                                .withTargetUrl(shareUrl)
                                .withText("又有土豪发红包啦！")
                                .withMedia(image)
                                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                                .setCallback(umShareListener);
                        mAction.open();
                        break;

                    case R.id.red_detail_rl_chat:
                        Intent detailIntent = new Intent(RedDetailActivity.this,ContanctDetailActivity.class);
                        detailIntent.putExtra("mobile",grabDetailPeopleDetailResult.getData().getMember().get(0).getMobile());
                        startActivity(detailIntent);
                        break;

                    case R.id.record_detail_iv_down_right:

                        break;

                    case R.id.DF_adv:
                        startActivity(new Intent(getApplicationContext(),WebActivity.class).putExtra("URL",bannerdata.get(0).getUrl()).putExtra("title",""));
                        break;

                }
            }
        });

    }
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(RedDetailActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(RedDetailActivity.this, "分享失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.red_detail_back_row:
                finish();
                break;
        }
    }


    private void getDFBanner(){
        DFTTAdsApiService.getAdvertisement("list", "toutiao", null, "1", new DfttApiServiceCallBack() {
            @Override
            public void onSuccess(String s) {
                LogUtil.LogShitou("DFBANNER：", s.toString());
                Message msg = handler.obtainMessage();
                msg.what = GET_DFBANNER;
                msg.obj = s;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(String s, String s1, Response response, Exception e) {

            }
        });
    }
}

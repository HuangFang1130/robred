package com.jiahehongye.robred.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.jiahehongye.robred.AppHelper;
import com.jiahehongye.robred.utils.DisplayImageOptionsUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.songheng.newsapisdk.sdk.global.DFTTSdk;
import com.songheng.newsapisdk.sdk.global.DFTTSdkConfig;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/11/30.
 */
public class BaseApplication extends Application {
    public static final String LOCAL_STORE_DIR = "dftt_sdk_demo";

    private UMShareAPI mShareAPI = null;

    {
        PlatformConfig.setWeixin("wx8f543a00c79d8221", "29185b05aa8f94c9347f65ce2b352a2f");
        PlatformConfig.setSinaWeibo("3973417494", "a1e3dd1db397981229511c81c11f17c0");//ok
        PlatformConfig.setQQZone("1105044794", "hMbVeLF6Ye8Bnrgx");

    }

    // 用于存放倒计时时间
    public static Map<String, Long> map;
    private static BaseApplication instance;
    // 获取到主线程的handler
    private static Handler mMainThreadHandler = null;
    // 获取到主线程
    private static Thread mMainThread = null;
    // 获取到主线程的id
    private static int mMainThreadId;
    private static BaseApplication mContext = null;
    private static Looper mMainThreadLooper = null;
    private boolean isInit;
    public static String registrationId;
    public final ImageLoader imageLoader = ImageLoader.getInstance();
    private static final int REQUEST_READ_PHONE_STATE = 2;

    public static synchronized BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        instance = this;

        mMainThreadLooper = getMainLooper();
        mMainThreadHandler = new Handler();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();


        //初始化JPUSH
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        registrationId = JPushInterface.getRegistrationID(this);
        Log.e("registrationId = ", registrationId);

        //解决umeng友盟集成后ClassNotFound找不到okhttp
        MultiDex.install(this);
        Config.DEBUG = true;//debug模式
        //友盟umeng初始化
        Config.REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
        mShareAPI = UMShareAPI.get(this);
        //初始化EasuUI 初始化SDK
        initEasemob();
        AppHelper.getInstance().init(this);
        initImageLoader(getApplicationContext());

        initDFTT();


    }

    /**
     * 金猴宝红包
      appid: 201705120001
     appkey: 16ba778bade60288
     softtypeid: sdk_jinhoubao
     softname: jinhoubaoAndroid / jinhoubaoIOS
     softtype: jinhoubaoSDK
     */
    private void initDFTT() {

        DFTTSdkConfig dftTSdkConfig = DFTTSdkConfig.getInstance();//设置默认参数
        dftTSdkConfig.setDebug(true)
                .setStoreDir(LOCAL_STORE_DIR)
                .setAppId("201705120001")
                .setAppKey("16ba778bade60288")
                .setAppTypeId("sdk_jinhoubao")
                .setSoftName("jinhoubaoAndroid")
                .setSoftType("jinhoubaoSDK")
                .setAppQid("360")
                .setAdsQid("360");

        try{
             DFTTSdk.getInstance().init(BaseApplication.getInstance(), dftTSdkConfig);

        }catch (Exception e){

        }
//        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
//        }else {
//            DFTTSdk.getInstance().init(BaseApplication.getInstance(), dftTSdkConfig);
//        }


    }

    /**
     * 初始化ImageLoader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
                context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);// 线程池中线程的个数
        config.denyCacheImageMultipleSizesInMemory();// 拒绝缓存多个图片
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());// 将保存的时候的URI名称用MD5
        // 加密
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);// 设置图片下载和显示的工作队列排序
        // config.writeDebugLogs(); // 打开调试日志,删除不显示日志
        config.defaultDisplayImageOptions(DisplayImageOptionsUtil.defaultOptions);// 显示图片的参数
        // config.diskCache(new UnlimitedDiskCache(cacheDir));//自定义缓存路径

        ImageLoader.getInstance().init(config.build());
    }


    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }

    // 对外暴露主线程的handler
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    // 对外暴露主线程
    public static Thread getMainThread() {
        return mMainThread;
    }

    // 对外暴露主线程id
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    public UMShareAPI getShareApi() {
        return mShareAPI;
    }

    public static BaseApplication getApplication() {
        return mContext;
    }

    private void initEasemob() {
        // 获取当前进程 id 并取得进程名
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        /**
         * 如果app启用了远程的service，此application:onCreate会被调用2次
         * 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
         * 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
         */
        if (processAppName == null || !processAppName.equalsIgnoreCase(mContext.getPackageName())) {
            // 则此application的onCreate 是被service 调用的，直接返回
            return;
        }
        if (isInit) {
            return;
        }

        //调用初始化方法初始化sdk
        EMClient.getInstance().init(mContext, initOptions());
        try {
            EaseUI.getInstance().init(mContext, initOptions());
        } catch (Exception e) {

        }

        // 设置开启debug模式
        EMClient.getInstance().setDebugMode(true);

        // 设置初始化已经完成
        isInit = true;
    }


    /**
     * SDK初始化的一些配置
     * 关于 EMOptions 可以参考官方的 API 文档
     * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1chat_1_1_e_m_options.html
     */
    private EMOptions initOptions() {
        EMOptions options = new EMOptions();
        // 设置Appkey，如果配置文件已经配置，这里可以不用设置
        // options.setAppKey("lzan13#hxsdkdemo");
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        //options.setRequireAck(true);
        // 设置是否需要发送回执，
        // options.setRequireDeliveryAck(true);
        options.setAutoAcceptGroupInvitation(true);
        // 设置是否需要服务器收到消息确认
        options.setRequireServerAck(true);
        // 设置是否根据服务器时间排序，默认是true
        options.setSortMessageByServerTime(false);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(false);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);
        // 设置google GCM推送id，国内可以不用设置
        // options.setGCMNumber(MLConstants.ML_GCM_NUMBER);
        // 设置集成小米推送的appid和appkey
        // options.setMipushConfig(MLConstants.ML_MI_APP_ID, MLConstants.ML_MI_APP_KEY);
        return options;
    }

    /**
     * 根据Pid获取当前进程的名字，一般就是当前app的包名
     *
     * @param pid 进程的id
     * @return 返回进程的名字
     */
    private String getAppName(int pid) {
        String processName = null;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    // 根据进程的信息获取当前进程的名字
                    processName = info.processName;
                    // 返回当前进程名
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return null;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

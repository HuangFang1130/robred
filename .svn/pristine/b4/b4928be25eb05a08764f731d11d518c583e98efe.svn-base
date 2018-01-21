package com.jiahehongye.robred.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;

import com.jiahehongye.robred.application.BaseApplication;
import com.umeng.socialize.UMShareAPI;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/30.
 */
public class UIUtils {

    public static Context getContext() {
        return BaseApplication.getApplication();
    }

    public static Looper getMainLooper(){
        return BaseApplication.getMainThreadLooper();
    }
    public static UMShareAPI getShareApi(){
        return BaseApplication.getApplication().getShareApi();
    }

    /**获取主线程*/
    public static Thread getMainThread() {
        return BaseApplication.getMainThread();
    }

    /**获取主线程的id*/
    public static long getMainThreadId() {
        return BaseApplication.getMainThreadId();
    }

    /** 获取主线程的handler */
    public static Handler getHandler() {
        return BaseApplication.getMainThreadHandler();
    }

    private static List<Activity> activityList = new LinkedList<Activity>();


    //添加Activity到容器中
    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    //遍历所有Activity并finish
    public static void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }


    //遍历所有Activity并finish
    public static void remove(Activity removeActivity) {
       if(activityList.contains(removeActivity)){
           activityList.remove(removeActivity);
           removeActivity.finish();
       }
//        System.exit(0);
    }
    // 判断当前的线程是不是在主线程
    public static boolean isRunInMainThread() {
        return android.os.Process.myTid() == getMainThreadId();
    }

    /**运行在主线程*/
    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }

    /** 在主线程执行runnable */
    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    public static boolean canMakeSmores(){

        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);

    }

    public static boolean hasPermission(String permission){

        if(canMakeSmores()){

            return(ContextCompat.checkSelfPermission(UIUtils.getContext(),permission)== PackageManager.PERMISSION_GRANTED);

        }

        return true;

    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        opt.inSampleSize = 1;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
}

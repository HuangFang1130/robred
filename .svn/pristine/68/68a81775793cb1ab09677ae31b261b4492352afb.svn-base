package com.jiahehongye.robred.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.jiahehongye.robred.Constant;

/**
 * Created by huangjunhui on 2016/12/21.10:59
 */
public class LocationUtils {
    // 纬度
    public double latitude = 0.0;
    // 经度
    public double longitude = 0.0;
    private static LocationUtils instance;

    /**
     * 初始化位置信息
     */
    public static LocationUtils getInstance() {
        if (instance == null) {
            instance = new LocationUtils();
        }
        return instance;

    }

    public void initLocation(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            Location location = locationManager
//                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (location != null) {
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
//                SPUtils.put(UIUtils.getContext(), Constant.LATITUDE,latitude+"");
//                SPUtils.put(UIUtils.getContext(),Constant.LONGITUDE,longitude+"");
//                LogUtil.LogShitou("location","                      经度:"+latitude+"  纬度:"+longitude);
//            }
//        }


        LocationListener locationListener = new LocationListener() {

            // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {

            }

            // Provider被enable时触发此函数，比如GPS被打开
            @Override
            public void onProviderEnabled(String provider) {

            }

            // Provider被disable时触发此函数，比如GPS被关闭
            @Override
            public void onProviderDisabled(String provider) {

            }

            // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    SPUtils.put(UIUtils.getContext(), Constant.LATITUDE, latitude + "");
                    SPUtils.put(UIUtils.getContext(), Constant.LONGITUDE, longitude + "");
                    LogUtil.LogShitou("location", "                      经度:" + latitude + "  纬度:" + longitude);
                }
            }
        };


        if (ActivityCompat.checkSelfPermission(UIUtils.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UIUtils.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000, 5, locationListener);
            Location location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude(); //
                longitude = location.getLongitude(); // 纬度
                SPUtils.put(UIUtils.getContext(), Constant.LATITUDE,latitude+"");
                SPUtils.put(UIUtils.getContext(),Constant.LONGITUDE,longitude+"");
                LogUtil.LogShitou("location","                      经度:"+latitude+"  纬度:"+longitude);
            }




    }

    /**
     * 获取纬度
     * @return
     */
    public double getLatitude(){
        return latitude;
    }

    /**
     * 获取经度
     * @return
     */
    public double getLongitude() {
        return longitude;
    }
}

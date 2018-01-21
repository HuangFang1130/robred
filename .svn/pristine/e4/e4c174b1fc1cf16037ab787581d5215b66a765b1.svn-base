package com.jiahehongye.robred.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;

/**
 * Created by huangjunhui on 2017/5/16.16:50
 */
public class TestActivity  extends BaseActivity{


    private TextView textview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        textview = (TextView) findViewById(R.id.framelayout);


        /**
         * 新闻title    getNewsColumnTag
         */
       /* DFTTNewsApiService.getNewsColumnTag(new DfttApiServiceCallBack() {
            @Override
            public void onSuccess(String s) {
                StringBuilder sb = new StringBuilder();
                StringBuilder append = sb.append("{\"dd\":").append(s).append("}");

                DFTTTitleResult dfttTitleResult = new Gson().fromJson(append.toString(), DFTTTitleResult.class);

                LogUtil.LogShitou("heheda:",s.toString());
                textview.setText(s.toString());
            }

            @Override
            public void onError(String s, String s1, Response response, Exception e) {

            }
        });*/


        /**
         * 电影的   getNewsListByType
         */
       /* DFTTNewsApiService.getNewsListByType("dianying", 2, 10, new DfttApiServiceCallBack() {
            @Override
            public void onSuccess(String s) {

                DFTTMoveResult dfttTitleResult = new Gson().fromJson(s.toString(), DFTTMoveResult.class);

                LogUtil.LogShitou("电影：",s.toString());
            }

            @Override
            public void onError(String s, String s1, Response response, Exception e) {

            }
        });*/


        /**
         * 视频栏目
         */
      /*  DFTTVideoApiService.getVideoColumnTag(new DfttApiServiceCallBack() {
            @Override
            public void onSuccess(String s) {
                LogUtil.LogShitou("视频栏目：",s.toString());
            }

            @Override
            public void onError(String s, String s1, Response response, Exception e) {

            }
        });*/






    }

}

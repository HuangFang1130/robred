package com.jiahehongye.robred.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.jiahehongye.robred.AppHelper;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.MainActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.adapter.SplashPagerAdapter;
import com.jiahehongye.robred.fragment.SplashFragment;
import com.jiahehongye.robred.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjunhui on 2017/1/12.10:49
 */
public class SplashActivity extends BaseActivity {

    private ViewPager splashViewPager;
    private List<SplashFragment> fragList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        boolean first_comein = (boolean) SPUtils.get(this, Constant.FIRST_COMEIN, true);
        if (!first_comein) {
            AppHelper.getInstance().logoutHuanXin();
            AppHelper.getInstance().loginHuanXin();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_splash);
        splashViewPager = (ViewPager) findViewById(R.id.splash_viewpager);


        //创建pager的数据源
        fragList = new ArrayList<>();
        for(int i=0;i<4;i++){
            SplashFragment guideFragment = new SplashFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("INDEX", i);
            guideFragment.setArguments(bundle);
            fragList.add(guideFragment);
        }

        //给ViewPager设置适配器
        SplashPagerAdapter adapter = new SplashPagerAdapter
                (getSupportFragmentManager(), fragList);
        splashViewPager.setAdapter(adapter);
    }


}

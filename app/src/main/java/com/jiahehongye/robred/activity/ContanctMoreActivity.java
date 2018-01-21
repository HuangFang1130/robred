package com.jiahehongye.robred.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;

/**
 * Created by huangjunhui on 2016/12/5.14:46
 */
public class ContanctMoreActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
//        mTintManager.setStatusBarTintResource(R.color.home_white_color);
        setContentView(R.layout.activity_contanct_detail_more);
        ImageView mIvBack = (ImageView) findViewById(R.id.red_detail_more_iv_back_row);
        mIvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.red_detail_more_iv_back_row:
                finish();
                break;
        }
    }
}

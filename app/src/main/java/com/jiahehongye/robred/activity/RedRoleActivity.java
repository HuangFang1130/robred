package com.jiahehongye.robred.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;

/**
 * 客服中心
 * Created by huangjunhui on 2016/12/9.16:40
 */
public class RedRoleActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        setContentView(R.layout.activity_redrole);
        initView();
    }

    private void initView() {
       TextView mTvTitle= (TextView) findViewById(R.id.application_tv_title);
        ImageView mIvBack = (ImageView) findViewById(R.id.application_iv_back);

        mIvBack.setOnClickListener(this);
        mTvTitle.setText("客服中心");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.application_iv_back:
                finish();
                break;
        }
    }
}

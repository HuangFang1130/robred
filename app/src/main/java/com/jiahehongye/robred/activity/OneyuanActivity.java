package com.jiahehongye.robred.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.fragment.FoundFragment;
import com.jiahehongye.robred.oneyuan.OneyuanRulesActivity;

public class OneyuanActivity extends BaseActivity {

    private RelativeLayout mBack;
    private TextView mRules;

    private FoundFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_oneyuan);

        mBack = (RelativeLayout) findViewById(R.id.oneyuan_rl_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRules = (TextView) findViewById(R.id.oneyuan_tv_rules);
        mRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OneyuanActivity.this, OneyuanRulesActivity.class));
            }
        });


        fragment = new FoundFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.oneyuan_fl_container,fragment).commit();
    }
}

package com.jiahehongye.robred.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;

/**
 * Created by huangjunhui on 2016/12/5.10:14
 */
public class RedProblemActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_red_problem);

       TextView mTvHow = (TextView) findViewById(R.id.redproblem_tv_how);
        ImageView mIvBack= (ImageView) findViewById(R.id.redproblem_iv_back_row);
        mIvBack.setOnClickListener(this);
        mTvHow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.redproblem_tv_how:
                Intent intent = new Intent(this,RedProblemDetailActivity.class);
                startActivity(intent);
                break;

            case R.id.redproblem_iv_back_row:
                finish();
                break;
        }
    }
}

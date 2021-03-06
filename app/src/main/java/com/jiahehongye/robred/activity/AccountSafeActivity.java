package com.jiahehongye.robred.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;

public class AccountSafeActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mBack;
    private LinearLayout mChangePhone,mChangePsw,account_safe_rl_changepaypsw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_account_safe);

        initView();
    }

    private void initView() {
        mBack = (RelativeLayout) findViewById(R.id.account_safe_rl_back);
        mChangePhone = (LinearLayout) findViewById(R.id.account_safe_rl_changephone);
        mChangePsw = (LinearLayout) findViewById(R.id.account_safe_rl_changepsw);
        account_safe_rl_changepaypsw = (LinearLayout) findViewById(R.id.account_safe_rl_changepaypsw);

        mBack.setOnClickListener(this);
        mChangePhone.setOnClickListener(this);
        mChangePsw.setOnClickListener(this);
        account_safe_rl_changepaypsw.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.account_safe_rl_back:
                finish();
                break;
            case R.id.account_safe_rl_changephone:
                startActivity(new Intent(this,ChangePhoneActivity.class));
                break;
            case R.id.account_safe_rl_changepsw:
                startActivity(new Intent(this,ChangePswActivity.class));
                break;
            case R.id.account_safe_rl_changepaypsw:
                startActivity(new Intent(this,ModifyPaypwd.class));
                break;
        }
    }
}

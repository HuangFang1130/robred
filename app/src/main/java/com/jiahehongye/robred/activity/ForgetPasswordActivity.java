package com.jiahehongye.robred.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;

/**
 * Created by huangjunhui on 2016/12/9.11:50
 */
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEtPhone;
    private String mobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_forget_password);
        initView();
    }

    private void initView() {
        ImageView mIvClose= (ImageView) findViewById(R.id.forget_iv_close);
        Button mTvForgetNextSetp= (Button) findViewById(R.id.forget_btn_next_step);
        mEtPhone = (EditText) findViewById(R.id.forget_et_phone);

        mTvForgetNextSetp.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forget_btn_next_step:
                if (mEtPhone.getText().toString().length()!=11){
                    Toast.makeText(ForgetPasswordActivity.this, "请输入11位手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!BaseActivity.isMobileNO(mEtPhone.getText().toString())){
                    Toast.makeText(ForgetPasswordActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                mobile = mEtPhone.getText().toString();
                Intent nextIntent = new Intent(this,ResetPasswordActivity.class);
                nextIntent.putExtra("mobile",mobile);
                startActivity(nextIntent);
                finish();
                break;
            case R.id.forget_iv_close :
                finish();
                break;
        }
    }
}

package com.jiahehongye.robred.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;

public class ChangePhoneActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mBack;
    private EditText et_password;
    private Button mNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.red);
        setContentView(R.layout.activity_change_phone);

        initView();
    }

    private void initView() {
        mBack = (RelativeLayout) findViewById(R.id.changephone_rl_back);
        et_password = (EditText) findViewById(R.id.changephone_et_psw);
        mNext = (Button) findViewById(R.id.changephone_btn_next);

        mBack.setOnClickListener(this);
        mNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.changephone_rl_back:
                finish();
                break;
            case R.id.changephone_btn_next:
                String psw = et_password.getText().toString();
                String rightpsw = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_PASSWORD,"");
                if(psw!=null&&psw.length()>0&&psw.equals(rightpsw)){
                    startActivity(new Intent(this,ChangePhoneOkActivity.class));
                    finish();
                }else {
                    Toast.makeText(this,"请输入正确的密码",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

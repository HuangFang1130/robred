package com.jiahehongye.robred.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;

/**
 * Created by huangjunhui on 2016/12/9.10:46
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Button mBtnNextStep;
    private EditText mEtPhone;
    private String openId;
    private String mediaType;
    private boolean isThird;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();
        isThird = intent.getBooleanExtra("isThird", false);
        if (isThird) {
            openId = intent.getStringExtra("openId");
            mediaType = intent.getStringExtra("mediaType");
        }


        initView();
    }

    private void initView() {
        ImageView mIvClose = (ImageView) findViewById(R.id.register_iv_close);
        mEtPhone = (EditText) findViewById(R.id.register_et_phone);

        mBtnNextStep = (Button) findViewById(R.id.register_bt_next_step);
        mBtnNextStep.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_bt_next_step:
                String phone = mEtPhone.getText().toString();
                if (TextUtils.isEmpty(phone) || phone.equals("") || phone.length() != 11) {
                    Toast.makeText(RegisterActivity.this, "请输入有效的手机号！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isMobileNO(phone)){
                    Toast.makeText(RegisterActivity.this, "请输入有效的手机号！", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(this, RegisterReceiverVerificyActivity.class);
                intent.putExtra("phone", phone);
                if (isThird) {
                    intent.putExtra("openId", openId);
                    intent.putExtra("mediaType", mediaType);
                    intent.putExtra("isThird", true);
                } else {
                    intent.putExtra("isThird", false);
                }
                startActivity(intent);
                break;

            case R.id.register_iv_close:
                finish();
                break;
        }
    }
}

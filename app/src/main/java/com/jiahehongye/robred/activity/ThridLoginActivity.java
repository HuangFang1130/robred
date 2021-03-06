package com.jiahehongye.robred.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.CircleImageView;

/**
 * Created by huangjunhui on 2017/1/4.18:24
 */
public class ThridLoginActivity extends BaseActivity implements View.OnClickListener {


    private String userAvatar;
    private String mediaType;
    private String userName;
    private String openId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();

        setContentView(R.layout.activity_thrid_login);
        initView();
    }

    private void initView() {
        Button thrid_register = (Button) findViewById(R.id.thrid_register);
        Button thrid_relevance = (Button) findViewById(R.id.thrid_relevance);
        CircleImageView thrid_headpic = (CircleImageView) findViewById(R.id.thrid_headpic);
        TextView thrid_name = (TextView) findViewById(R.id.thrid_name);
        Intent intent = getIntent();
        userAvatar = intent.getStringExtra("headpicc");
        mediaType = intent.getStringExtra("mediaType");
        userName = intent.getStringExtra("name");
        openId = intent.getStringExtra("openId");

        Glide.with(UIUtils.getContext()).load(userAvatar).into(thrid_headpic);
        thrid_name.setText(userName);

        thrid_register.setOnClickListener(this);
        thrid_relevance.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.thrid_register://快速注册

                Intent intent = new Intent(this,RegisterActivity.class);
                intent.putExtra("isThird", true);//false ;不是第三方，true:是第三方
                intent.putExtra("openId", openId);
                intent.putExtra("mediaType", mediaType);
                startActivity(intent);
                finish();
                break;
            case R.id.thrid_relevance://绑定账号信息（立即关联）
                Intent thridLoginIntent = new Intent(this,ThridBindActivity.class);
                thridLoginIntent.putExtra("openId", openId);
                thridLoginIntent.putExtra("mediaType", mediaType);
                startActivity(thridLoginIntent);
                finish();
                break;


        }

    }
}

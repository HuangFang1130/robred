package com.jiahehongye.robred.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;

public class ModifyPaypwd extends BaseActivity implements OnClickListener {
	private RelativeLayout forget_pay_pwd;
	private RelativeLayout modif_pay_pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		applyKitKatTranslucency();
		mTintManager.setStatusBarTintResource(R.color.red);
		setContentView(R.layout.modif_pay_pwd);
		forget_pay_pwd = (RelativeLayout) findViewById(R.id.forget_pay_pwd);
		modif_pay_pwd = (RelativeLayout) findViewById(R.id.modif_pay_pwd);
		RelativeLayout mdf_pp_back = (RelativeLayout) findViewById(R.id.mdf_pp_back);

		mdf_pp_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		forget_pay_pwd.setOnClickListener(this);
		modif_pay_pwd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.forget_pay_pwd:
			startActivity(new Intent(getApplicationContext(), PayForgetPwd.class));
			break;
		case R.id.modif_pay_pwd:
			startActivity(new Intent(getApplicationContext(), PayModifPwd.class));
			break;

		default:
			break;
		}

	}

}

package com.jiahehongye.robred.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.utils.DesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PayModifPwd extends BaseActivity implements OnClickListener {
	private EditText old, newPwd, reNewPwd;
	private Button pay_modif_ok;

	private PersistentCookieStore persistentCookieStore;
	private Call call;
	private static final int GET_ALL = 0000;
	private static final int GET_FORGET = 0001;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case GET_FORGET:
					String forget = (String) msg.obj;
					try {
						JSONObject object = new JSONObject(forget);

						if (object.getString("result").equals("success")) {
							Toast.makeText(PayModifPwd.this, "修改成功", Toast.LENGTH_SHORT).show();
							finish();
						} else {
							Toast.makeText(PayModifPwd.this, "修改失败，请重试", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		applyKitKatTranslucency();
		mTintManager.setStatusBarTintResource(R.color.white);
		setContentView(R.layout.pay_modif_pwd);

		old = (EditText) findViewById(R.id.pay_modif_inputpay);
		newPwd = (EditText) findViewById(R.id.pay_modif_fpwd1);
		reNewPwd = (EditText) findViewById(R.id.pay_modif_fpwd2);
		pay_modif_ok = (Button) findViewById(R.id.pay_modif_ok);
		RelativeLayout pmdf_back = (RelativeLayout) findViewById(R.id.pmdf_back);

		pmdf_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		pay_modif_ok.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pay_modif_ok:

			String old = this.old.getText().toString().trim();
			String newPwd = this.newPwd.getText().toString().trim();
			String reNewPwd = this.reNewPwd.getText().toString().trim();

			System.out.println(old + newPwd + reNewPwd);

			// 这里需要判断两个新密码是否一致。并且要等于6位数。
			if (old.length() != 6) {
				Toast.makeText(getApplicationContext(), "原密码为6位数字", Toast.LENGTH_SHORT).show();
				return;
			} else if (newPwd.length() != 6) {
				Toast.makeText(getApplicationContext(), "新密码为6位数字", Toast.LENGTH_SHORT).show();
				return;
			} else if (reNewPwd.length() != 6) {
				Toast.makeText(getApplicationContext(), "确认密码为6位数字", Toast.LENGTH_SHORT).show();
				return;
			} else if (old.equals(newPwd)) {
				Toast.makeText(getApplicationContext(), "新旧密码不能一样", Toast.LENGTH_SHORT).show();
				return;
			} else if (!newPwd.equals(reNewPwd)) {
				Toast.makeText(getApplicationContext(), "两次新密码不一致", Toast.LENGTH_SHORT).show();
				return;
			}

			String pay1 = DesUtil.encrypt(old);
			String newpwdd = DesUtil.encrypt(newPwd);
			// 把密码加密进行传输。
			initData(pay1, newpwdd);

			break;

		default:
			break;
		}

	}

	private void initData(String pay1, String newpwdd) {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		persistentCookieStore = new PersistentCookieStore(PayModifPwd.this);
		CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
		builder.cookieJar(cookieJarImpl);
		OkHttpClient client = builder.build();

		JSONObject json = new JSONObject();


		try {
			json.put("payPassword", pay1);
			json.put("newPayPassword", newpwdd);
		} catch (JSONException e) {
			e.printStackTrace();
		}


		RequestBody body = RequestBody.create(Constant.JSON, json.toString());
		Request request = new Request.Builder()
				.url(Constant.MODIFYPAYPASSWORD)
				.post(body)
				.build();


		call = client.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				PayModifPwd.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(PayModifPwd.this, "网络连接失败", Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String result = response.body().string();

				Message msg = handler.obtainMessage();
				msg.what = GET_FORGET;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		});

	}
}

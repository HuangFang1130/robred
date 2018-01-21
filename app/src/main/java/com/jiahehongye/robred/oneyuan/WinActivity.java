package com.jiahehongye.robred.oneyuan;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.OneyuanMyWinBean;
import com.jiahehongye.robred.bean.OneyuanMyWinBean.Addresss;
import com.jiahehongye.robred.bean.OneyuanMyWinBean.Product;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.view.MyProgressDialog;
import com.jiahehongye.robred.view.MySquareImageView;
import com.lidroid.xutils.http.HttpHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WinActivity extends BaseActivity implements OnClickListener {
	

	private static final int CODE_REQUEST = 0; 
	private static final int CODE_REQUEST2 = 2; 
	
	private MyProgressDialog animDialog;
	
	private String mProductId = null;
	
	private String addressId = "";

	private ScrollView sv_total;
	
	private LinearLayout ll_noAddress;//无收货地址布局
	private LinearLayout ll_yesAddress;//有收货地址布局
	
	private Button insertAdds;//添加收货地址
	private Button submitAdds;//提交收货地址
	private LinearLayout updateAdds;//更新地址
	private MySquareImageView image;//商品图片
	private TextView seeDetail;//查看商品详情
	private Button share, share1;//分享按钮
	private TextView name;//收货姓名
	private TextView phone;//收货电话
	private TextView address;//收货详细地址
	private TextView message;// 获奖信息
	private OneyuanMyWinBean winBean;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				sv_total.setVisibility(View.VISIBLE);
				animDialog.dismiss();
				String data = (String) msg.obj;
				Gson gson = new Gson();
				winBean = gson.fromJson(data, OneyuanMyWinBean.class);
				if (winBean.getResult().equals("success")) {
					Product product = winBean.getData().getProduct().get(0);
					
					if(winBean.getData().getAddress().size()>0){
						
						addressId = winBean.getData().getAddress().get(0).getAddressId();
						
						if(product.getIsAddress().equals("1")){
							submitAdds.setBackgroundResource(R.drawable.oneyuan_win_submit_complete);// 提交收货地址之后变灰色
							submitAdds.setClickable(false);// 提交收货地址之后不可点击
							updateAdds.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Toast.makeText(WinActivity.this, "地址已提交不可修改", Toast.LENGTH_SHORT).show();
								}
							});
						}
					}

					//无论如何都必要的
					Spanned text = Html.fromHtml("\t\t\t\t您好！恭喜您在拼运气中成功获得" + "<font color=#249FFF>"
							+ winBean.getData().getProduct().get(0).getName() + "</font>" + "商品（期号：" + "<font color=#249FFF><u>"
							+ winBean.getData().getProduct().get(0).getNoStage() + "</u></font>" + "），以下是您获得的商品信息：");
					message.setText(text);
					Picasso.with(WinActivity.this).load(product.getImage()).into(image);
					
					if(winBean.getData().getAddress().size()>0){//有收货地址
						Addresss adds = winBean.getData().getAddress().get(0);
						
						ll_noAddress.setVisibility(View.GONE);
						ll_yesAddress.setVisibility(View.VISIBLE);
						name.setText(adds.getRealName());
						phone.setText(adds.getMobile());
						address.setText(adds.getDetailedAddress());
					}else {//没有收货地址
						ll_noAddress.setVisibility(View.VISIBLE);
						ll_yesAddress.setVisibility(View.GONE);
					}

				} else if (winBean.getResult().equals("fail")) {
					Toast.makeText(WinActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
				}

				break;

			case 2:
				animDialog.dismiss();
				String data2 = (String) msg.obj;
				try {
					JSONObject object = new JSONObject(data2);
					if(object.get("result").equals("success")){
						Toast.makeText(WinActivity.this, "提交地址成功", Toast.LENGTH_SHORT).show();
						submitAdds.setBackgroundResource(R.drawable.oneyuan_win_submit_complete);// 提交收货地址之后变灰色
						submitAdds.setClickable(false);// 提交收货地址之后不可点击
					}else {
						Toast.makeText(WinActivity.this, "提交地址失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}

	};

	private HttpHandler<String> send;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		applyKitKatTranslucency();
		mTintManager.setStatusBarTintResource(R.color.home_state_color);
		setContentView(R.layout.activity_win);

		Intent intent = getIntent();
		mProductId = intent.getStringExtra("productId");
		Log.e("传进来产品的ID：", mProductId);

		initView();

		initData();

	}

	private void initView() {
		
		sv_total = (ScrollView) findViewById(R.id.sv_oneyuan_win_total);
		
		insertAdds = (Button) findViewById(R.id.btn_oneyuan_win_insertadds);
		insertAdds.setOnClickListener(this);
		updateAdds = (LinearLayout) findViewById(R.id.ll_oneyuan_win_updateadds);
		updateAdds.setOnClickListener(this);
		submitAdds = (Button) findViewById(R.id.btn_oneyuan_win_submitadds);
		submitAdds.setOnClickListener(this);
		
		message = (TextView) findViewById(R.id.tv_oneyuan_win_message);
		image = (MySquareImageView) findViewById(R.id.iv_oneyuan_win_image);

		seeDetail = (TextView) findViewById(R.id.tv_oneyuan_win_seedetail);
		seeDetail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 添加下划线
		seeDetail.getPaint().setAntiAlias(true);// 抗锯齿
		seeDetail.setOnClickListener(this);
		
		ll_noAddress = (LinearLayout) findViewById(R.id.ll_oneyuan_win_address_no);
		ll_yesAddress = (LinearLayout) findViewById(R.id.ll_oneyuan_win_address_yes);

		share = (Button) findViewById(R.id.btn_oneyuan_win_share);
		share1 = (Button) findViewById(R.id.btn_oneyuan_win_share1);

		name = (TextView) findViewById(R.id.tv_oneyuan_win_name);
		phone = (TextView) findViewById(R.id.tv_oneyuan_win_phonenum);
		address = (TextView) findViewById(R.id.tv_oneyuan_win_adds);
	}

	/**
	 * 获奖信息查询
	 */
	private void initData() {
		
		showMyDialog();

		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		persistentCookieStore = new PersistentCookieStore(this);
		CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
		builder.cookieJar(cookieJarImpl);
		OkHttpClient client = builder.build();

		JSONObject json = new JSONObject();
		try {
			// 把json对象添加到正文对象里面。
			json.put("productId", mProductId);
			Log.e("请求参数：", mProductId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		RequestBody body = RequestBody.create(Constant.JSON, json.toString());
		Request request = new Request.Builder()
				.url(Constant.ONEYUANMYWIN)
				.post(body)
				.build();

		call = client.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				WinActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(WinActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
						animDialog.dismiss();
					}
				});
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String data = response.body().string();
				Log.e("获奖信息==", data);
				Message msg = Message.obtain();
				msg.what = 1;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		});

	}
	
	/**
	 * 确认提交收货地址不可更改
	 */
	private PersistentCookieStore persistentCookieStore;
	private Call call;
	private void initSubmit() {
		
		showMyDialog();

		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		persistentCookieStore = new PersistentCookieStore(this);
		CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
		builder.cookieJar(cookieJarImpl);
		OkHttpClient client = builder.build();

		JSONObject json = new JSONObject();
		try {
			// 把json对象添加到正文对象里面。
			json.put("productId", mProductId);
			json.put("addressId", addressId);
			Log.e("确认最后地址产品ID", mProductId);
			Log.e("确认最后地址地址ID", addressId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		RequestBody body = RequestBody.create(Constant.JSON, json.toString());
		Request request = new Request.Builder()
				.url(Constant.ONEYUANSUBMITADDRESS)
				.post(body)
				.build();

		call = client.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				WinActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(WinActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
						animDialog.dismiss();
					}
				});
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String data = response.body().string();
				Log.e("提交地址返回数据==", data);
				Message msg = Message.obtain();
				msg.what = 2;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		});

	}
	

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.tv_oneyuan_win_seedetail:
			Intent intent = new Intent(WinActivity.this, OneyuanProductDetailActivity.class);
			Log.e("传过去产品的ID：", mProductId);
			intent.putExtra("productId", mProductId);
			startActivity(intent);
			break;

		case R.id.btn_oneyuan_win_insertadds://添加收货地址
			Intent intent2 = new Intent(WinActivity.this, AddressOneyuanActivity.class);
			Log.e("传过去产品的ID：", mProductId);
			intent2.putExtra("productId", mProductId);
			startActivityForResult(intent2, CODE_REQUEST2);;
			break;
		case R.id.ll_oneyuan_win_updateadds://修改收货地址
			if(winBean.getResult().equals("success")&&winBean.getData().getAddress().size()>0){
				Addresss address = winBean.getData().getAddress().get(0);
				addressId = address.getAddressId();
				Intent intent3 = new Intent(WinActivity.this, AddressOneyuanActivity.class);
				Log.e("传过去产品的ID：", mProductId);
				Log.e("传过去地址的ID：", addressId);
				intent3.putExtra("productId", mProductId);
				intent3.putExtra("country", address.getCounty());
				intent3.putExtra("province", address.getProvince());
				intent3.putExtra("realName", address.getRealName());
				intent3.putExtra("detailedAddress", address.getDetailedAddress());
				intent3.putExtra("city", address.getCity());
				intent3.putExtra("mobile", address.getMobile());
				intent3.putExtra("addressId", addressId);
				startActivityForResult(intent3, CODE_REQUEST);
			}else {
				Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_oneyuan_win_submitadds://确认提交收货地址
			initSubmit();
			break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CODE_REQUEST:
			initData();
			break;

		case CODE_REQUEST2:
			initData();
			break;
		}
		
	}
	
	/**
     * 显示对话框
     */
    public void showMyDialog(){
    	animDialog =new MyProgressDialog(WinActivity.this, "玩命加载中...",R.drawable.loading);
    	animDialog.show();
    	animDialog.setCancelable(true);
    }
    
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}

package com.jiahehongye.robred.oneyuan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.R.id;
import com.jiahehongye.robred.adapter.OneyuanPastAnnouncedAdapter;
import com.jiahehongye.robred.bean.OneyuanPastAnnouncedBean;
import com.jiahehongye.robred.bean.OneyuanPastAnnouncedBean.ResultList;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.view.MyProgressDialog;
import com.lidroid.xutils.http.HttpHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OneyuanPastAnnouncedActivity extends AppCompatActivity {
	
	private boolean FLAG_REFRESH = false;
	
	private String fixedValue;
	
	private MyProgressDialog animDialog;
	
	private PullToRefreshListView mPastList;
	private OneyuanPastAnnouncedBean listBean;
	private OneyuanPastAnnouncedAdapter listAdapter;
	private List<ResultList> totalList = new ArrayList<ResultList>();
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				animDialog.dismiss();
				mPastList.onRefreshComplete();
				
				String data = (String) msg.obj;
				Gson gson = new Gson();
				listBean = gson.fromJson(data, OneyuanPastAnnouncedBean.class);
				if(listBean.getResult().equals("success")){
					List<ResultList> list = listBean.getData().getResultList();
					
					totalList.clear();
					totalList.addAll(list);
					
					listAdapter = new OneyuanPastAnnouncedAdapter(OneyuanPastAnnouncedActivity.this, totalList);
					mPastList.setAdapter(listAdapter);
					
				}else if (listBean.getResult().equals("fail")) {
					Toast.makeText(OneyuanPastAnnouncedActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
				}
				break;

			default:
				break;
			}
		}
	};

	private HttpHandler<String> send;

	private RelativeLayout mBack;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oneyuan_past_announced);
//		base_title.setText("往期揭晓");

		mBack = (RelativeLayout) findViewById(id.oneyuanold_rl_back);
		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		Intent intent = getIntent();
		fixedValue = intent.getStringExtra("fixedValue");
		Log.e("传进来的产品固定值：", fixedValue);
		
		mPastList = (PullToRefreshListView) findViewById(id.ptrlv_oneyuan_pastlist);
		
		initData();
		
	}

	private PersistentCookieStore persistentCookieStore;
	private Call call;
	private void initData() {
		
		showMyDialog();

		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		persistentCookieStore = new PersistentCookieStore(this);
		CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
		builder.cookieJar(cookieJarImpl);
		OkHttpClient client = builder.build();

		JSONObject json = new JSONObject();
		try {
			Log.e("当前请求：", "产品固定值：" + fixedValue);
			json.put("fixedValue", fixedValue);
		} catch (Exception e) {
			e.printStackTrace();
		}

		RequestBody body = RequestBody.create(Constant.JSON, json.toString());
		Request request = new Request.Builder()
				.url(Constant.ONEYUANPASTANNOUNCED)
				.post(body)
				.build();

		call = client.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				OneyuanPastAnnouncedActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(OneyuanPastAnnouncedActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
						animDialog.dismiss();
						mPastList.onRefreshComplete();
					}
				});
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String data = response.body().string();
				Log.e("往期揭晓列表==", data);
				Message msg = Message.obtain();
				msg.what = 1;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		});

	}
	
	/**
     * 显示对话框
     */
    public void showMyDialog(){
    	animDialog =new MyProgressDialog(OneyuanPastAnnouncedActivity.this, "玩命加载中...", R.drawable.loading);
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

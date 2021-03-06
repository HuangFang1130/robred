package com.jiahehongye.robred.oneyuan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.R.id;
import com.jiahehongye.robred.adapter.MyRobListAdapter;
import com.jiahehongye.robred.bean.OneyuanMyRobListBean;
import com.jiahehongye.robred.bean.OneyuanMyRobListBean.ParticipationRecordList;
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

public class MyRobListActivity extends BaseActivity {

	private boolean FLAG_REFRESH = false;

	private String mPageSize = "10";
	private int mPageNumber = 1;

	private MyProgressDialog animDialog;

	private PullToRefreshListView mMyroblist;
	private ListView listview;
	private MyRobListAdapter adapter;
	private List<ParticipationRecordList> totalList = new ArrayList<ParticipationRecordList>();
	private OneyuanMyRobListBean listBean;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if(animDialog != null){
					animDialog.dismiss();
				}
				mMyroblist.onRefreshComplete();
				String data = (String) msg.obj;
				Gson gson = new Gson();
				listBean = gson.fromJson(data, OneyuanMyRobListBean.class);
				if(listBean.getResult().equals("success")){
					List<ParticipationRecordList> list = listBean.getData().getParticipationRecordList();
					//判断是否为上拉加载
					if(FLAG_REFRESH){
						//如果返回数据为空，则将页码数返回减少1
						if(list.size() == 0){
							mPageNumber--;
						}
						FLAG_REFRESH = !FLAG_REFRESH;
						totalList.addAll(list);
						adapter.notifyDataSetChanged();
					}else {
						totalList.clear();// 下拉刷新清空列表
						totalList.addAll(list);// 填充最新请求到的数据
						adapter.notifyDataSetChanged();
					}
				}else if (listBean.getResult().equals("fail")) {
					Toast.makeText(MyRobListActivity.this, "获取数据异常", Toast.LENGTH_SHORT).show();
				}
				break;

			default:
				break;
			}
		}
	};

	private HttpHandler<String> send;

	private RelativeLayout mBack;
	private int mScreenWidth;
	private int mScreenHeight;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		applyKitKatTranslucency();
		mTintManager.setStatusBarTintResource(R.color.home_state_color);
		setContentView(R.layout.activity_my_rob_list);
//
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mScreenWidth = metrics.widthPixels;
		mScreenHeight = metrics.heightPixels;
		mBack = (RelativeLayout) findViewById(id.oneyuanmyroblist_rl_back);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		initProductList();

		mMyroblist = (PullToRefreshListView) findViewById(id.ptrlv_oneyuan_myroblist);
		mMyroblist.setMode(Mode.BOTH);

		listview = mMyroblist.getRefreshableView();
		listview.setDivider(null);

		adapter = new MyRobListAdapter(this, totalList,mScreenWidth,mScreenHeight);
		mMyroblist.setAdapter(adapter);

		mMyroblist.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isRefreshing()) {
					if (mMyroblist.isHeaderShown()) {// 下拉刷新
						mPageNumber = 1;// 重置
						initProductList();
					} else if (mMyroblist.isFooterShown()) {// 上拉加载
						FLAG_REFRESH = true;
						mPageNumber++;
						initProductList();
					} else {
						mMyroblist.onRefreshComplete();
					}
				}
			}
		});

	}

	/**
	 * 获取我的抢单列表
	 */
	private PersistentCookieStore persistentCookieStore;
	private Call call;
	private void initProductList() {

		showMyDialog();

		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		persistentCookieStore = new PersistentCookieStore(this);
		CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
		builder.cookieJar(cookieJarImpl);
		OkHttpClient client = builder.build();

		JSONObject json = new JSONObject();
		try {
			json.put("pageSize", mPageSize);
			json.put("pageNumber", mPageNumber+"");
		} catch (Exception e) {
			e.printStackTrace();
		}

		RequestBody body = RequestBody.create(Constant.JSON, json.toString());
		Request request = new Request.Builder()
				.url(Constant.ONEYUANMYROBLIST)
				.post(body)
				.build();

		call = client.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				MyRobListActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(MyRobListActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
						animDialog.dismiss();
						mMyroblist.onRefreshComplete();
					}
				});
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String data = response.body().string();
				Log.e("我的抢单列表==", data);
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
    	animDialog =new MyProgressDialog(MyRobListActivity.this, "玩命加载中...",R.drawable.loading);
    	animDialog.show();
    	animDialog.setCancelable(true);
    }

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			animDialog = null;
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}

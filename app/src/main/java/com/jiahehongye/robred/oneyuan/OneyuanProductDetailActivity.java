package com.jiahehongye.robred.oneyuan;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.adapter.OneyuanProductParticipationAdapter;
import com.jiahehongye.robred.bean.OneyuanProductDetailBean;
import com.jiahehongye.robred.bean.OneyuanProductDetailBean.Product;
import com.jiahehongye.robred.bean.OneyuanProductDetailBean.ResultList;
import com.jiahehongye.robred.bean.OneyuanProductParticipationListBean;
import com.jiahehongye.robred.bean.OneyuanProductParticipationListBean.ParticipationRecordList;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.view.CircleImageView;
import com.jiahehongye.robred.view.MyListView;
import com.jiahehongye.robred.view.MyProgressDialog;
import com.jiahehongye.robred.view.MySquareImageView;
import com.jiahehongye.robred.view.MySquareLinearLayout;
import com.jiahehongye.robred.view.RoundProgressBar;
import com.lidroid.xutils.http.HttpHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
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

public class OneyuanProductDetailActivity extends BaseActivity implements OnClickListener {

	private String mProductId;
	private String mPageSize = "10";
	private int mPageNum = 1;

	private MyProgressDialog animDialog;

	// 列表是否显示的标记
	private boolean list_flag = false;

	// 判断是否为上拉加载
	private boolean FLAG_REFRESH = false;
	private int ccount = 0;

	private String mIsParticipate;// 是否参与此产品
	private String mType;// 产品状态：未开始，进行中，已结束

	private LinearLayout ll_total;

	private LinearLayout finish_other;// 中奖人信息
	private LinearLayout finish_self;// 自己抽奖信息
	private TextView finish_nojoin;// 开奖未参与
	private LinearLayout finish_yesjoin;// 开奖已参与

	private RelativeLayout mIntroduction;// 图文详情按钮
	private RelativeLayout mParticipation;// 参与记录按钮
	private View view_introduction, view_participation;// 两个下划线
	// private ImageView xiangqing;// 详情图片
	private WebView xiangqing;

	private PullToRefreshScrollView ptrlv_scrollview;
	private MyListView lv_joinlist;// 参与列表
	private MySquareLinearLayout noOne_join;// 无人参与
	private LinearLayout mNoBuy, mYesBuy, mGotoNext;// 底部布局
	private LinearLayout mLLprogressBar;// 百分比进度条布局
	private RoundProgressBar mProgressBar;
	private ImageView goBack;// 回退键
	private ImageView mImage;// 产品图片
	private TextView mNoStage;// 产品期号
	private TextView mName;// 产品名称
	private TextView mComments;// 产品介绍备注
	private TextView mTotalPeople;// 总人次
	private TextView mSurplusPeople;// 剩余人次
	private TextView mPercentage;// 百分比
	private TextView mNoOne;
	private TextView gotoMath;// 计算方式
	private LinearLayout mNo1;// 第一期正在进行布局
	private RelativeLayout mBefore;// 往期揭晓进入
	private RelativeLayout mAgo;// 上一期晒单布局
	private TextView mPrice;// 价格
	private Button mJoin;// 立即参与
	private Button mSeeMore;// 查看更多
	private Button mGotoNew;// 前往最新一期

	private CircleImageView winnerHead;// 当期获奖者头像
	private TextView winnerName;// 当期获奖者昵称
	private TextView winnerTime;// 当期获奖者时间
	private TextView winnerCode;// 当期获奖者幸运号
	private TextView joinCode;// 当期自己参与幸运号

	private String isHaveNext;// 判断是否有最新一期
	private String noStageObj;// 最新一期号码
	private String productObjId;// 最新一期ID
	private TextView tv_haveNext;

	private TextView lastWinnerNoStage;// 上一期期号
	private TextView lastWinnerTime;// 上一期中奖时间
	private TextView lastWinnerMonth;// 上一期中奖日期
	private TextView lastWinnerName;// 上一期中奖姓名
	private TextView lastWinnerCode;// 上一期中奖号码
	private TextView lastWinnerMessage;// 上一期中奖感言
	private MySquareImageView image1, image2, image3;// 上一期中奖三张图
	private List<MySquareImageView> imageList = new ArrayList<MySquareImageView>();

	private List<String> nullList = new ArrayList<String>();
	// private List<ParticipationRecordList> nullList = new
	// ArrayList<ParticipationRecordList>();
	private List<ParticipationRecordList> totalList = new ArrayList<ParticipationRecordList>();
	private OneyuanProductDetailBean detailBean;
	private OneyuanProductParticipationListBean listBean;
	private OneyuanProductParticipationAdapter listAdapter;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				animDialog.dismiss();
				ptrlv_scrollview.onRefreshComplete();
				String data = (String) msg.obj;
				Gson gson = new Gson();
				detailBean = gson.fromJson(data, OneyuanProductDetailBean.class);
				if (detailBean.getResult().equals("success")) {
					ll_total.setVisibility(View.VISIBLE);
					Product product = detailBean.getData().getProduct().get(0);

					// 无论什么状态都必要的
					Picasso.with(OneyuanProductDetailActivity.this).load(product.getImage()).into(mImage);
					mNoStage.setText(product.getNoStage());
					mName.setText(product.getName());
					mComments.setText(product.getCommnets());
					mTotalPeople.setText(product.getTotalPeople());
					mSurplusPeople.setText(product.getSurplusPeople());
					mProgressBar.setProgress(Integer.parseInt(product.getPercentage()));
					mPrice.setText("￥" + product.getPrice());
					String str = product.getIntroduction();
					String start = "<html><style>body img{width:100%;}</style><body>";
					String end = "</body></html>";
					xiangqing.loadDataWithBaseURL(null, start + product.getIntroduction() + end, "text/html", "utf-8",
							null);
					Log.e("html", start + product.getIntroduction() + end);

					// 设置头布局显示隐藏
					if (product.getNoStage().equals("第1期")) {
						mNo1.setVisibility(View.VISIBLE);
						mAgo.setVisibility(View.GONE);

						// 如果是第二期以后
					} else {
						mNo1.setVisibility(View.GONE);
						mAgo.setVisibility(View.GONE);
						List<ResultList> reList = detailBean.getData().getResultList();
						if (reList.size() > 0) {// 说明有晒单
							mAgo.setVisibility(View.VISIBLE);
							lastWinnerNoStage.setText(reList.get(0).getName());
							lastWinnerTime.setText(reList.get(0).getTime());
							lastWinnerMonth.setText(reList.get(0).getMonth());
							lastWinnerName.setText(reList.get(0).getUserName());
							lastWinnerCode.setText(reList.get(0).getLuckyNumber());
							lastWinnerMessage.setText(reList.get(0).getContents());
							if (reList.get(0).getPictureLibraryList().size() < 1) {
								for (int k = 0; k < imageList.size(); k++) {
									imageList.get(k).setVisibility(View.GONE);
								}
							}
							for (int i = 0; i < reList.get(0).getPictureLibraryList().size(); i++) {
								Picasso.with(OneyuanProductDetailActivity.this)
										.load(reList.get(0).getPictureLibraryList().get(i).getImgUrl())
										.into(imageList.get(i));
								if (i == 2) {
									i = 7;
								}
							}
						}
					}

					// 分状态显示与隐藏的
					if (product.getType().equals("1")) {// 进行中
						finish_other.setVisibility(View.GONE);
						finish_self.setVisibility(View.GONE);

						// 设置百分比显示隐藏
						if (product.getPercentage().equals("0")) {
							mNoOne.setVisibility(View.VISIBLE);
						} else {
							mNoOne.setVisibility(View.GONE);
						}

						// 设置全局布局显示隐藏
						if (product.getIsParticipate().equals("0")) {// 未参与
							mNoBuy.setVisibility(View.VISIBLE);
							mYesBuy.setVisibility(View.GONE);
							mGotoNext.setVisibility(View.GONE);
						} else if (product.getIsParticipate().equals("1")) {// 已参与
							mYesBuy.setVisibility(View.VISIBLE);
							mNoBuy.setVisibility(View.GONE);
							mGotoNext.setVisibility(View.GONE);
						}

					} else if (product.getType().equals("2")) {// 已结束

						// Date dt = new Date();
						// SimpleDateFormat matter = new
						// SimpleDateFormat("yyyy-MM-dd");

						// 无论如何都要的
						if (detailBean.getData().getParticipationRecord().get(0).getUserPhoto().length() > 0) {
							Picasso.with(OneyuanProductDetailActivity.this)
									.load(detailBean.getData().getParticipationRecord().get(0).getUserPhoto())
									.placeholder(R.mipmap.head_pic1).error(R.mipmap.head_pic1).into(winnerHead);
						} else {
							Picasso.with(OneyuanProductDetailActivity.this).load(R.mipmap.head_pic1).into(winnerHead);
						}
						winnerName.setText(detailBean.getData().getParticipationRecord().get(0).getUserName());
						winnerTime.setText(detailBean.getData().getParticipationRecord().get(0).getRevealedTime());
						winnerCode.setText(detailBean.getData().getParticipationRecord().get(0).getSn());

						// 设置全局布局显示隐藏
						if (product.getIsParticipate().equals("0")) {// 未参与
							mNoBuy.setVisibility(View.VISIBLE);
							mYesBuy.setVisibility(View.GONE);
							mGotoNext.setVisibility(View.GONE);
						} else if (product.getIsParticipate().equals("1")) {// 已参与
							mYesBuy.setVisibility(View.VISIBLE);
							mNoBuy.setVisibility(View.GONE);
							mGotoNext.setVisibility(View.GONE);
						}

						// 设置头布局显示隐藏
						mLLprogressBar.setVisibility(View.GONE);
						finish_other.setVisibility(View.VISIBLE);
						if (product.getIsParticipate().equals("0")) {// 未参与
							finish_nojoin.setVisibility(View.VISIBLE);
							finish_self.setVisibility(View.GONE);
						} else if (product.getIsParticipate().equals("1")) {// 已参与
							finish_nojoin.setVisibility(View.GONE);
							finish_self.setVisibility(View.VISIBLE);
							joinCode.setText(detailBean.getData().getParticipationRecord().get(0).getSnList().get(0).getMemberSn());
							finish_self.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									showPopupWindow(joinCode,detailBean.getData().getParticipationRecord().get(0).getSnList());
								}
							});


						}

						if (product.getIsResetNext().equals("1")) {// 有最新一期
							mGotoNext.setVisibility(View.VISIBLE);
							mYesBuy.setVisibility(View.GONE);
							mNoBuy.setVisibility(View.GONE);
							tv_haveNext.setText(product.getNoStageObj());

						} else {

						}

					} else if (product.getType().equals("0")) {// 未开始

					}

					initList();
				} else {
//					try {
//						JSONObject object = new JSONObject(data);
//						String error = object.getString("errorMsg");
//						Toast.makeText(OneyuanProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
//						mAgo.setVisibility(View.GONE);
//						finish_nojoin.setVisibility(View.GONE);
//						finish_other.setVisibility(View.GONE);
//						finish_self.setVisibility(View.GONE);
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					Toast.makeText(OneyuanProductDetailActivity.this, "获取数据异常", Toast.LENGTH_SHORT).show();
				}
				break;

			case 2:
				animDialog.dismiss();
				ptrlv_scrollview.onRefreshComplete();
				String dataList = (String) msg.obj;
				Gson gson2 = new Gson();
				listBean = gson2.fromJson(dataList, OneyuanProductParticipationListBean.class);
				if (listBean.getResult().equals("success")) {
					ArrayList<ParticipationRecordList> list = (ArrayList<ParticipationRecordList>) listBean.getData()
							.getParticipationRecordList();
					// 如果返回数据为空，则将页码数返回减少1
					if (list.size() == 0) {
						mPageNum--;
						if (totalList.size() == 0) {// 如果总集合还是空的
							// noOne_join.setVisibility(View.VISIBLE);// 显示无人抢购
						} else {// 如果总集合是有数据的
							noOne_join.setVisibility(View.GONE);
							listAdapter.notifyDataSetChanged();
						}
					} else {
						totalList.addAll(list);
						listAdapter.notifyDataSetChanged();
					}
				}
				break;
			case 3:
				if (animDialog != null) {
					animDialog.dismiss();
				}
				ptrlv_scrollview.onRefreshComplete();
				String[] str = (String[]) msg.obj;
				String dataJson = str[0];
				String productId = str[1];
				try {
					JSONObject result = new JSONObject(dataJson);
					if (result.get("result").equals("success")) {// 未参与
						JSONObject data1 = result.getJSONObject("data");
						JSONArray memberList = data1.getJSONArray("memberList");

						JSONObject accoRedEnve = memberList.getJSONObject(0);
						String money = accoRedEnve.getString("accoRedEnve");

						Intent intent = new Intent();
						intent.setClass(OneyuanProductDetailActivity.this, OneyuanPayActivity.class);
						intent.putExtra("productId", productId);
						intent.putExtra("accoRedEnve", money);
						OneyuanProductDetailActivity.this.startActivity(intent);
					} else if (result.getString("result").equals("fail")) {// 已参与
						Toast.makeText(OneyuanProductDetailActivity.this, "已参与", Toast.LENGTH_SHORT).show();
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
	private int mScreenWidth;
	private int mScreenHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		applyKitKatTranslucency();
		mTintManager.setStatusBarTintResource(R.color.home_state_color);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sales_detail);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mScreenWidth = metrics.widthPixels;
		mScreenHeight = metrics.heightPixels;

		Intent intent = getIntent();
		mProductId = intent.getStringExtra("productId");
		Log.e("传进来产品的ID", mProductId);

		initView();

		ptrlv_scrollview.setMode(Mode.BOTH);
		ptrlv_scrollview.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// TODO Auto-generated method stub
				if (refreshView.isRefreshing()) {
					if (ptrlv_scrollview.isHeaderShown()) {// 下拉刷新
						// Toast.makeText(OneyuanProductDetailActivity.this,
						// "刷新", Toast.LENGTH_SHORT).show();
						totalList.clear();
						initData();
					} else if (ptrlv_scrollview.isFooterShown()) {// 上拉加载
						// Toast.makeText(OneyuanProductDetailActivity.this,
						// "加载", Toast.LENGTH_SHORT).show();
						mPageNum++;
						initList();
					} else {
						ptrlv_scrollview.onRefreshComplete();
					}

				}
			}

		});

		listAdapter = new OneyuanProductParticipationAdapter(OneyuanProductDetailActivity.this, totalList);
		lv_joinlist.setAdapter(listAdapter);
		lv_joinlist.setVisibility(View.GONE);



	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	/**
	 * 获取布局控件
	 */
	private void initView() {

		ll_total = (LinearLayout) findViewById(R.id.ll_oneyuan_product_detail);

		ptrlv_scrollview = (PullToRefreshScrollView) findViewById(R.id.ptrlv_oneyuan_salesdetail);

		mNoBuy = (LinearLayout) findViewById(R.id.ll_oneyuan_salesdetail_nobuy);
		mYesBuy = (LinearLayout) findViewById(R.id.ll_oneyuan_salesdetail_yesbuy);
		mGotoNext = (LinearLayout) findViewById(R.id.ll_oneyuan_salesdetail_gotonext);
		tv_haveNext = (TextView) findViewById(R.id.tv_oneyuan_havenext);

		mJoin = (Button) findViewById(R.id.btn_oneyuan_salesdetail_submit);
		mJoin.setOnClickListener(this);

		mSeeMore = (Button) findViewById(R.id.btn_oneyuan_salesdetail_seemore);
		mSeeMore.setOnClickListener(this);

		mGotoNew = (Button) findViewById(R.id.btn_oneyuan_salesdetail_gotonext);
		mGotoNew.setOnClickListener(this);
		finish_other = (LinearLayout) findViewById(R.id.ll_oneyuan_salesdetail_finish_other);
		finish_self = (LinearLayout) findViewById(R.id.ll_oneyuan_salesdetail_finish_self);
		finish_nojoin = (TextView) findViewById(R.id.tv_oneyuan_salesdetail_nojoin);
		finish_yesjoin = (LinearLayout) findViewById(R.id.ll_oneyuan_salesdetail_yesjoin);
		mLLprogressBar = (LinearLayout) findViewById(R.id.ll_oneyuan_salesdetail_progressbar);
		mNo1 = (LinearLayout) findViewById(R.id.ll_oneyuan_salesdetail_no1);
		mAgo = (RelativeLayout) findViewById(R.id.rl_oneyuan_salesdetail_ago);
		mBefore = (RelativeLayout) findViewById(R.id.rl_oneyuan_salesdetail_before);
		mBefore.setOnClickListener(this);
		mImage = (ImageView) findViewById(R.id.iv_oneyuan_salesdetail_image);
		mNoStage = (TextView) findViewById(R.id.tv_oneyuan_salesdetail_nostage);
		mName = (TextView) findViewById(R.id.tv_oneyuan_salesdetail_name);
		mComments = (TextView) findViewById(R.id.tv_oneyuan_salesdetail_comments);
		mTotalPeople = (TextView) findViewById(R.id.tv_oneyuan_salesdetail_totalpeople);
		mSurplusPeople = (TextView) findViewById(R.id.tv_oneyuan_salesdetail_surpluspeople);
		mPrice = (TextView) findViewById(R.id.tv_oneyuan_salesdetail_price);
		mProgressBar = (RoundProgressBar) findViewById(R.id.progressbar_percentage);
		mNoOne = (TextView) findViewById(R.id.tv_oneyuan_salesdetail_noone);
		view_introduction = findViewById(R.id.view_introduction);
		view_participation = findViewById(R.id.view_participationlist);
		mIntroduction = (RelativeLayout) findViewById(R.id.rl_oneyuan_salesdetail_introduction);
		mParticipation = (RelativeLayout) findViewById(R.id.rl_oneyuan_salesdetail_participationlist);
		mIntroduction.setOnClickListener(this);
		mParticipation.setOnClickListener(this);
		xiangqing = (WebView) findViewById(R.id.web_oneyuan_salesdetail_xiangqing);
		xiangqing.getSettings().setUseWideViewPort(true);
		xiangqing.getSettings().setLoadWithOverviewMode(true);
		LayoutParams layoutParams = xiangqing.getLayoutParams();
		layoutParams.width = getWindowManager().getDefaultDisplay().getWidth();
		xiangqing.setLayoutParams(layoutParams);

		xiangqing.requestFocus();

		WebSettings settings = xiangqing.getSettings();
		settings.setLoadWithOverviewMode(true);
		settings.setJavaScriptEnabled(true);// 设置支持javascript脚本
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int mDensity = metrics.densityDpi;
		if (mDensity == 120) {
			settings.setDefaultZoom(ZoomDensity.CLOSE);
		} else if (mDensity == 160) {
			settings.setDefaultZoom(ZoomDensity.MEDIUM);
		} else if (mDensity == 240) {
			settings.setDefaultZoom(ZoomDensity.FAR);
		}
		xiangqing.setInitialScale(960 * 100 / layoutParams.height);

		xiangqing.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}
		});

		winnerHead = (CircleImageView) findViewById(R.id.iv_oneyuan_salesdetail_winner);
		winnerName = (TextView) findViewById(R.id.tv_oneyuan_salesdetail_winnername);
		winnerTime = (TextView) findViewById(R.id.tv_oneyuan_salesdetail_winnertime);
		winnerCode = (TextView) findViewById(R.id.tv_oneyuan_salesdetail_luckynum);
		joinCode = (TextView) findViewById(R.id.tv_oneyuan_salesdetail_myluckycode);

		gotoMath = (TextView) findViewById(R.id.tv_oneyuan_salesdetail_gotoMath);
		gotoMath.setOnClickListener(this);

		lastWinnerNoStage = (TextView) findViewById(R.id.tv_oneyuan_salesdetail_lastissue);
		lastWinnerTime = (TextView) findViewById(R.id.tv_oneyuan_salesdetail_lasttime);
		lastWinnerMonth = (TextView) findViewById(R.id.tv_oneyuan_salesdetail_lastday);
		lastWinnerName = (TextView) findViewById(R.id.tv_oneyuan_salesdetail_lastname);
		lastWinnerCode = (TextView) findViewById(R.id.tv_oneyuan_salesdetail_lastcode);
		lastWinnerMessage = (TextView) findViewById(R.id.tv_oneyuan_salesdetail_lastmessage);
		image1 = (MySquareImageView) findViewById(R.id.iv_oneyuan_salesdetail_lastwinnerimage1);
		image2 = (MySquareImageView) findViewById(R.id.iv_oneyuan_salesdetail_lastwinnerimage2);
		image3 = (MySquareImageView) findViewById(R.id.iv_oneyuan_salesdetail_lastwinnerimage3);
		imageList.add(image1);
		imageList.add(image2);
		imageList.add(image3);

		lv_joinlist = (MyListView) findViewById(R.id.lv_oneyuan_salsedetail_joinlist);
		noOne_join = (MySquareLinearLayout) findViewById(R.id.ll_noone_join);

		goBack = (ImageView) findViewById(R.id.iv_oneyuan_salesdetail_goback);
		goBack.setOnClickListener(this);

	}

	/**
	 * 详情页数据
	 */
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
			// 把json对象添加到正文对象里面。
			json.put("productId", mProductId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		RequestBody body = RequestBody.create(Constant.JSON, json.toString());
		Request request = new Request.Builder()
				.url(Constant.ONEYUANPRODUCTDETAIL)
				.post(body)
				.build();

		call = client.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				OneyuanProductDetailActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(OneyuanProductDetailActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
						animDialog.dismiss();
					}
				});
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String data = response.body().string();
				Log.e("该产品详情数据==", data);
				Message msg = Message.obtain();
				msg.what = 1;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		});

	}

	/**
	 * 参与记录数据
	 */
	private void initList() {

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
			json.put("pageSize", mPageSize);
			json.put("pageNumber", mPageNum + "");
		} catch (Exception e) {
			e.printStackTrace();
		}

		RequestBody body = RequestBody.create(Constant.JSON, json.toString());
		Request request = new Request.Builder()
				.url(Constant.ONEYUANPARTICIPATIONLIST)
				.post(body)
				.build();

		call = client.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				OneyuanProductDetailActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(OneyuanProductDetailActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
						animDialog.dismiss();
					}
				});
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String data = response.body().string();
				Log.e("该产品参与记录==", data);
				Message msg = Message.obtain();
				msg.what = 2;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		});
	}

	/**
	 * 点击我要参与请求
	 */
	protected void initData(final String productId) {

		showMyDialog();

		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		persistentCookieStore = new PersistentCookieStore(this);
		CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
		builder.cookieJar(cookieJarImpl);
		OkHttpClient client = builder.build();

		JSONObject json = new JSONObject();
		try {
			Log.e("当前请求：", productId);
			json.put("productId", productId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		RequestBody body = RequestBody.create(Constant.JSON, json.toString());
		Request request = new Request.Builder()
				.url(Constant.ONEYUANJOIN)
				.post(body)
				.build();

		call = client.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				OneyuanProductDetailActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(OneyuanProductDetailActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
						animDialog.dismiss();
					}
				});
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String data = response.body().string();
				String[] str = new String[2];
				str[0] = data;
				str[1] = productId;
				Message msg = Message.obtain();
				msg.what = 3;
				msg.obj = str;
				handler.sendMessage(msg);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回上一层
		case R.id.iv_oneyuan_salesdetail_goback:
			animDialog = null;
			finish();
			break;

		// 立即参与
		case R.id.btn_oneyuan_salesdetail_submit:
			initData(mProductId);
			break;

		// 查看更多
		case R.id.btn_oneyuan_salesdetail_seemore:
			finish();
			break;

		// 前往最新一期
		case R.id.btn_oneyuan_salesdetail_gotonext:
			Intent intent3 = new Intent(this, OneyuanProductDetailActivity.class);
			String productId = detailBean.getData().getProduct().get(0).getProductObjId();
			Log.e("传过去产品的ID：", productId);
			intent3.putExtra("productId", productId);
			startActivity(intent3);
			break;

		// 查看计算方式
		case R.id.tv_oneyuan_salesdetail_gotoMath:
			Log.e("传过去的产品固定值：", mProductId);
			Intent intent = new Intent();
			intent.setClass(OneyuanProductDetailActivity.this, MathActivity.class);
			intent.putExtra("productId", mProductId);
			startActivity(intent);
			break;

		// 查看往期揭晓
		case R.id.rl_oneyuan_salesdetail_before:
			//
			String fixedValue = detailBean.getData().getProduct().get(0).getFixedValue();// 产品固定值
			Log.e("传过去的产品固定值：", fixedValue);
			Intent intent2 = new Intent();
			intent2.setClass(OneyuanProductDetailActivity.this, OneyuanPastAnnouncedActivity.class);
			intent2.putExtra("fixedValue", fixedValue);
			startActivity(intent2);
			break;

		// 图文详情
		case R.id.rl_oneyuan_salesdetail_introduction:
			view_introduction.setVisibility(View.VISIBLE);
			view_participation.setVisibility(View.INVISIBLE);
			xiangqing.setVisibility(View.VISIBLE);
			lv_joinlist.setVisibility(View.GONE);
			noOne_join.setVisibility(View.GONE);
			break;

		// 参与列表
		case R.id.rl_oneyuan_salesdetail_participationlist:
			view_introduction.setVisibility(View.INVISIBLE);
			view_participation.setVisibility(View.VISIBLE);
			xiangqing.setVisibility(View.GONE);
			lv_joinlist.setVisibility(View.VISIBLE);
			if (totalList.size() > 0) {
				noOne_join.setVisibility(View.GONE);
			} else {
				noOne_join.setVisibility(View.VISIBLE);
			}
			break;
		}
	}

	/**
	 * 显示对话框
	 * 
	 */
	public void showMyDialog() {
		animDialog = new MyProgressDialog(OneyuanProductDetailActivity.this, "玩命加载中...", R.drawable.loading);
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
	public void showPopupWindow(View view, final List<OneyuanProductDetailBean.SnList> a) {
		// 一个自定义的布局，作为显示的内容
		final View contentView = LayoutInflater.from(getApplicationContext()).inflate( R.layout.popstate, null);
		GridView duijiang_grid = (GridView) contentView.findViewById(R.id.duijiang_grid);
		NumAdapter numAdapter = new NumAdapter(a);
		duijiang_grid.setAdapter(numAdapter);
		final PopupWindow popupWindow = new PopupWindow(contentView, mScreenWidth-250, mScreenHeight/2);
		// 让Popupwindow获取焦点
		popupWindow.setFocusable(true);
		// 给Popupwindow设置背景，为了点击其他地方时，Popupwindow自动消失
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 显示Popupwindow,让Popupwindow的左上点挨着号码框的左下点
		popupWindow.showAtLocation(view, Gravity.CENTER,0,0);



	}
	private LayoutInflater inflater;
	class NumAdapter extends BaseAdapter {

		private List<OneyuanProductDetailBean.SnList> a;
		public NumAdapter(List<OneyuanProductDetailBean.SnList> a) {
			this.a= a;
		}

		@Override
		public int getCount() {
			return a.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view1, ViewGroup parent) {
			ViewHolder holder = null;
			if(view1 == null){
				view1 = inflater.from(getApplicationContext()).inflate(R.layout.item_text, null);
				holder = new ViewHolder();
				holder.num = (TextView) view1.findViewById(R.id.djnum);
				view1.setTag(holder);
			}else {
				holder = (ViewHolder) view1.getTag();
			}
			holder.num.setText(a.get(position).getMemberSn());
			if (a.get(position).getType().equals("2")){
				holder.num.setTextColor(Color.parseColor("#ff1943"));
			}else {
				holder.num.setTextColor(Color.parseColor("#000000"));
			}
			return view1;
		}
		class ViewHolder{
			TextView num;
		}
	}
}

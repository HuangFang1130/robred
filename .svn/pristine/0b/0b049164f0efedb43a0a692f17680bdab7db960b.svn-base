package com.jiahehongye.robred.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.adapter.BillionairesRecycleAdapter;
import com.jiahehongye.robred.bean.BillionairesResult;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.interfaces.MyHeadViewClickListener;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.LogUtil;

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

/**
 * Created by Administrator on 2016/12/2.
 *
 * 富豪榜
 */
public class BillionairesActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_SUCCESS = 200;
    private SwipeRefreshLayout mBillionSwipeRefresh;
    private RecyclerView mBillionRecycle;
    private BillionairesRecycleAdapter billionairesRecycleAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<BillionairesResult.DataBean.RichLBean> fathreArrayList;
    private String pageNumber ="1";
    private List<BillionairesResult.DataBean.RichLBean> richL;
    private boolean isMore = true;
    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    String result = (String) msg.obj;
                    LogUtil.LogShitou("BillionairesResult:  ",result);
                    BillionairesResult billionairesResult = new Gson().fromJson(result, BillionairesResult.class);
                    if (billionairesResult.getResult().equals("success")) {
                       parserDate(billionairesResult);
                    } else if(billionairesResult.getResult().equals("fail")){
                        Toast.makeText(BillionairesActivity.this, "解析错误", Toast.LENGTH_SHORT).show();
                    }
                    if(billionairesRecycleAdapter!=null){
                        billionairesRecycleAdapter.hintFootView();
                    }
                    LogUtil.LogShitou("result: " + result.toString());
                    break;
            }
        }
    };




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_billionaires);
        initView();
        pageNumber = "1";
        requestDate(pageNumber);
    }

    /**
     *  请求网络数据
     * @param pageNumber
     */
    private void requestDate(String pageNumber) {

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(this)))
                .build();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pageSize", "20");
            jsonObject.put("pageNumber", pageNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.HOME_RICH_LIST)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               BillionairesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BillionairesActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = handler.obtainMessage();
                msg.what = REQUEST_SUCCESS;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initView() {
        ImageView mIvback= (ImageView) findViewById(R.id.billion_back_row);
        mBillionSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.billion_swiperefreshlayout);
        mBillionRecycle = (RecyclerView) findViewById(R.id.billion_recycleview);
        mBillionSwipeRefresh.setColorSchemeResources(R.color.holo_blue_bright,R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        fathreArrayList = new ArrayList<>();

        billionairesRecycleAdapter = new BillionairesRecycleAdapter(this, fathreArrayList);
        linearLayoutManager = new LinearLayoutManager(this);
        mBillionRecycle.setLayoutManager(linearLayoutManager);
        mBillionRecycle.setItemAnimator(new DefaultItemAnimator());
        mBillionRecycle.setAdapter(billionairesRecycleAdapter);
        mIvback.setOnClickListener(this);
        mBillionSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBillionSwipeRefresh.setRefreshing(false);
            }
        });
        //滑动的监听
        mBillionRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                    if(linearLayoutManager.getItemCount()<=1){
                        return;
                    }

                    if(lastVisiblePosition >= linearLayoutManager.getItemCount()-1){
                        if(billionairesRecycleAdapter!=null)billionairesRecycleAdapter.showFootView();

                        int i = Integer.parseInt(pageNumber);
                        i++;
                        pageNumber = i+"";
                        requestDate(pageNumber);
                    }

                }
            }
        });


        billionairesRecycleAdapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent = new Intent(BillionairesActivity.this,ContanctDetailActivity.class);
                intent.putExtra("mobile",fathreArrayList.get(postion+2).getMobile());
                startActivity(intent);


            }
        });

        billionairesRecycleAdapter.setOnHeadClickListener(new MyHeadViewClickListener() {
            @Override
            public void onHeadClick(View view) {
                switch (view.getId()){
                    case R.id.billion_head_rl_three:
                        if(fathreArrayList.size()>=3){
                            Intent intent = new Intent(BillionairesActivity.this, ContanctDetailActivity.class);
                            intent.putExtra("mobile",fathreArrayList.get(2).getMobile());
                            startActivity(intent);
                            return;
                        }
                        break;

                    case R.id.billion_head_rl_two:
                        if(fathreArrayList.size()>=2){
                            Intent intent = new Intent(BillionairesActivity.this, ContanctDetailActivity.class);
                            intent.putExtra("mobile",fathreArrayList.get(1).getMobile());
                            startActivity(intent);
                        }
                        break;
                    case R.id.billion_head_rl_one:
                        if(fathreArrayList.size()>=1){
                            Intent intent = new Intent(BillionairesActivity.this, ContanctDetailActivity.class);
                            intent.putExtra("mobile",fathreArrayList.get(0).getMobile());
                            startActivity(intent);
                        }
                        break;
                }

            }
        });

    }

    /**
     * 更新数据
     * @param billionairesResult
     */
    private void parserDate(BillionairesResult billionairesResult) {
        String personalRank = billionairesResult.getData().getPersonalRank();
        richL = billionairesResult.getData().getRichL();
        if(richL.size()<10){
            isMore = false;
        }
        billionairesRecycleAdapter.setHeardDate(personalRank);
        fathreArrayList.addAll(richL);
        billionairesRecycleAdapter.notifyDataSetChanged();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.billion_back_row:
                finish();
                break;
        }
    }

}

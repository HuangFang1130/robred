package com.jiahehongye.robred.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.adapter.AccountAdapter;

import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends BaseActivity {

    private RelativeLayout mBack;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;

    private AccountAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.white);
        setContentView(R.layout.activity_account);

        mBack = (RelativeLayout) findViewById(R.id.account_rl_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mListView = (ListView) findViewById(R.id.account_lv);
        List<String> list = new ArrayList<>();
        list.add("0");list.add("0");list.add("0");list.add("0");list.add("0");
//        adapter = new AccountAdapter(this,list);
//        mListView.setAdapter(adapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.account_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright,R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


        //解决listview和swiperefreshlayout滑动冲突
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (mListView != null && mListView.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = mListView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = mListView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mSwipeRefreshLayout.setEnabled(enable);
            }
        });
    }
}

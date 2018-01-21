package com.jiahehongye.robred.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.activity.RelationActivity;
import com.jiahehongye.robred.adapter.RelationFansRecycleViewAdpatre;
import com.jiahehongye.robred.bean.AttentionListResult;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.interfaces.MyItemClickListener;
import com.jiahehongye.robred.utils.LogUtil;
import com.jiahehongye.robred.utils.SPUtils;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.MyProgressDialog;

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
 * Created by huangjunhui on 2017/3/29.13:52
 */
public class RelationFansFragment extends Fragment implements MyItemClickListener {
    private static final int REQUEST_SUCCESS = 2;

    private RecyclerView mRelationRecycleView;
    private ArrayList<AttentionListResult.DataBean.ListBean> fatherArrayList;

    private LinearLayoutManager linearLayoutManager;
    private String id;
    private String page = "1";
    private RelationFansRecycleViewAdpatre fansRecycleViewAdpatre;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    if(mSwipeRefresh!=null ){
                        if(mSwipeRefresh.isRefreshing())
                            mSwipeRefresh.setRefreshing(false);
                    }
                    if(fansRecycleViewAdpatre!=null){
                        fansRecycleViewAdpatre.hintFootView();
                    }
                    String result = (String) msg.obj;
                    AttentionListResult addressDetailResult = new Gson().fromJson(result, AttentionListResult.class);
                    if (addressDetailResult.getResult().equals("success")) {
                        List<AttentionListResult.DataBean.ListBean> list =
                                addressDetailResult.getData().getList();
                        fatherArrayList.addAll(list);
                        fansRecycleViewAdpatre.notifyDataSetChanged();
                    } else if (addressDetailResult.getResult().equals("fail")) {
                        String errorMsg = addressDetailResult.getResult();
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                    } else {

                    }
                    if(animDialog!=null){
                        animDialog.dismiss();
                    }
                    LogUtil.LogShitou("result: " + result.toString());
                    break;

            }
        }
    };
    private MyProgressDialog animDialog;
    private Call call;
    private SwipeRefreshLayout mSwipeRefresh;

    @Override
    public void onResume() {
        super.onResume();
        fatherArrayList.clear();
        fansRecycleViewAdpatre.notifyDataSetChanged();
        page = "1";
        request();
    }
    /**
     * 显示对话框
     */
    public void showMyDialog() {
        animDialog = new MyProgressDialog(getActivity(), "玩命加载中...", R.drawable.loading);
        animDialog.show();
        animDialog.setCancelable(true);
        animDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (call.isExecuted()) {
                    call.cancel();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mSwipeRefresh.setColorSchemeResources(R.color.holo_blue_bright,R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (fansRecycleViewAdpatre != null) {
                    fatherArrayList.clear();
                    fansRecycleViewAdpatre.notifyDataSetChanged();
                    page = "1";
                    request();
                }
            }
        });
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRelationRecycleView.setLayoutManager(linearLayoutManager);
        fatherArrayList = new ArrayList<>();
        fansRecycleViewAdpatre = new RelationFansRecycleViewAdpatre((RelationActivity) getActivity(), fatherArrayList);
        mRelationRecycleView.setAdapter(fansRecycleViewAdpatre);
        fansRecycleViewAdpatre.setOnItemClickListener(this);


        mRelationRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();

                    if (linearLayoutManager.getItemCount() <= 1) {
                        return;
                    }
                    if (lastVisiblePosition >= linearLayoutManager.getItemCount() - 1) {
                        if (fansRecycleViewAdpatre != null) {
                            fansRecycleViewAdpatre.showFootView();
                        }

                        int i = Integer.parseInt(page);
                        i++;
                        page = String.valueOf(i);
                        request();

                    }

                }
            }
        });

//        fatherArrayList.clear();
//        fansRecycleViewAdpatre.notifyDataSetChanged();


    }

    private void request() {
//        personalPresenter.requestMyFans(id, page);//我的粉丝
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(UIUtils.getContext());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pageNumber", page);
            jsonObject.put("pageSize", "10");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.FANS_LIST_URL)
                .post(body)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.LogShitou("粉丝列表",e.toString());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                LogUtil.LogShitou("关注列表",result);
                LogUtil.LogShitou("请求关注接口返回数据：",result.toString());
                Message msg = handler.obtainMessage();
                msg.what = REQUEST_SUCCESS;
                msg.obj = result;
                handler.sendMessage(msg);

            }
        });




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_relation_frans, null);
        id = (String) SPUtils.get(UIUtils.getContext(), Constant.LOGIN_ID, "");
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.relsdfjklasdfds);

        mRelationRecycleView = (RecyclerView) view.findViewById(R.id.fragment_relation_recycleview);
        return view;
    }


    public static RelationFansFragment newInstances() {
        Bundle args = new Bundle();
        RelationFansFragment fragment = new RelationFansFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onItemClick(View view, int postion) {

    }


//
//    @Override
//    public void parserDate(Object data) {
//        if (data instanceof MyFansResult) {
//            MyFansResult myFansResult = (MyFansResult) data;
//            List<MyFansResult.BodyBean> body = myFansResult.getBody();
//            if (body.size() < 10) {
//                fansRecycleViewAdpatre.SCROLL_STATE_CURRENT = Constant.SCROLL_STATE_LAST;
//            } else {
//                fansRecycleViewAdpatre.SCROLL_STATE_CURRENT = Constant.SCROLL_STATE_IN;
//            }
//            fatherArrayList.addAll(body);
//            fansRecycleViewAdpatre.notifyDataSetChanged();
//        }
//    }

//    @Override
//    public void hintProcress() {
//        if (mRelationRefresh != null) {
//            mRelationRefresh.endRefreshing();
//        }
//        if (fansRecycleViewAdpatre != null) {
//            fansRecycleViewAdpatre.hintFootView();
//        }
//        dismissLoadingDialog();
//    }



//    @Override
//    public void onItemClick(View view, int postion) {
//        String userid = fatherArrayList.get(postion).getUserid();
//        if (id.equals(userid)) {
//            Toast.makeText(getActivity(), "不能点击自己哦", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Intent intent = new Intent(getActivity(), OtherPersonalHomePagersActivity.class);
//        intent.putExtra("id", userid);
//        startActivity(intent);
//    }
}

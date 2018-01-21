package com.jiahehongye.robred.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.adapter.MyRobListAdapter;
import com.jiahehongye.robred.bean.OneyuanMyRobListBean;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;

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
 * Created by qianduan on 2016/12/14.
 */
public class SnatchWinFragment extends Fragment {
    private View view;
    private PersistentCookieStore persistentCookieStore;
    private Call call;
    private static final int GET_ALL = 0000;
    private OneyuanMyRobListBean listBean;
    private MyRobListAdapter adapter;
    private List<OneyuanMyRobListBean.ParticipationRecordList> totalList = new ArrayList<OneyuanMyRobListBean.ParticipationRecordList>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_ALL:
                    String data = (String) msg.obj;
                    Gson gson = new Gson();
                    listBean = gson.fromJson(data, OneyuanMyRobListBean.class);
                    if(listBean.getResult().equals("success")){
                        List<OneyuanMyRobListBean.ParticipationRecordList> list = listBean.getData().getParticipationRecordList();
                        totalList.addAll(list);
                        adapter.notifyDataSetChanged();
                    }

                    break;
            }
        }
    };
    private String mPageSize = "10";
    private int mPageNumber = 1;
    private ListView list_win;
    private int mScreenWidth;
    private int mScreenHeight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.snatch_win, container, false);
        list_win= (ListView) view.findViewById(R.id.list_win);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        getdata();

        adapter = new MyRobListAdapter(getActivity(), totalList,mScreenWidth,mScreenHeight);
        list_win.setAdapter(adapter);

        list_win.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        //加载更多功能的代码
                        mPageNumber = mPageNumber + 1;
                        getdata();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        return view;
    }


    private void getdata() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("type","2");
            jsonObject.put("pageSize", mPageSize);
            jsonObject.put("pageNumber", mPageNumber+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.ONEYUANMYROBLIST)
                .post(body)
                .build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                Message msg = handler.obtainMessage();
                msg.what = GET_ALL;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });

    }
}

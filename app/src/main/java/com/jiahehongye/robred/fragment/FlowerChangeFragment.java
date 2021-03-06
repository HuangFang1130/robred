package com.jiahehongye.robred.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.view.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by qianduan on 2016/12/12.
 */
public class FlowerChangeFragment extends Fragment {
    private View view;
    private static final int DUI_HUAN = 0001;
    private static final int GET_ALL = 0000;
    private TextView flower_changenumber,flower_can;
    private Button flower_exchange;

    private String flower;
    private String cash;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_ALL:
                    animDialog.dismiss();
                    String login = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(login);
                        if (object.getString("result").equals("success")){
                            JSONObject data = new JSONObject(object.getString("data"));

                            cash = data.getString("getCash");
                            flower_changenumber.setText(data.getString("flower"));
                            flower_can.setText(data.getString("getCash"));
                            if (data.getString("flag").equals("0")){
                                flower_exchange.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        duihuan();
                                    }
                                });
                            }else if (data.getString("flag").equals("1")){
                                flower_exchange.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(getActivity(), "不足一元，不能兑换", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case DUI_HUAN:
                    String s = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(s);
                        if (object.getString("result").equals("success")){
                            Toast.makeText(getContext(), "兑换成功", Toast.LENGTH_SHORT).show();
                            JSONObject data = new JSONObject(object.getString("data"));
                            flower = data.getString("flower");
                            flower_changenumber.setText(data.getString("flower"));
                            flower_can.setText(data.getString("getCash"));

                            if (data.getString("flag").equals("0")){
                                flower_exchange.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        duihuan();
                                    }
                                });
                            }else if (data.getString("flag").equals("1")){
                                flower_exchange.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(getActivity(), "不足一元，不能兑换", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.flower_change, container, false);
        flower_changenumber = (TextView) view.findViewById(R.id.flower_changenumber);
        flower_can = (TextView) view.findViewById(R.id.flower_can);
        flower_exchange = (Button) view.findViewById(R.id.flower_exchange);
        getdata();
        return view;

    }

    /**
     *
     */
    private PersistentCookieStore persistentCookieStore;
    private MyProgressDialog animDialog;
    private Call call;
    private void getdata() {

        showMyDialog();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("type","2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.MY_GIFT)
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
                        animDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("鲜花兑换",result);
                Message msg = handler.obtainMessage();
                msg.what = GET_ALL;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });

    }
    /**
     * 显示对话框
     */
    public void showMyDialog(){
        animDialog =new MyProgressDialog(getActivity(), "玩命加载中...",R.drawable.loading);
        animDialog.show();
        animDialog.setCancelable(true);
        animDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(call.isExecuted()){
                    call.cancel();
                }
            }
        });
    }


    /**
     *
     */

    private void duihuan() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("type","2");
            jsonObject.put("cash",cash);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.DUIHUAN)
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
                        animDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("金币兑换",result);
                Message msg = handler.obtainMessage();
                msg.what = DUI_HUAN;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });

    }
}

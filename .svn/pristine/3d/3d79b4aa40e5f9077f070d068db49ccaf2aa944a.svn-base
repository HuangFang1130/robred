package com.jiahehongye.robred.biz;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.application.BaseApplication;
import com.jiahehongye.robred.biz.model.HomeWinningResponse;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.utils.UIUtils;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jiahe008_lvlanlan on 2017/5/27.
 * 首页轮播获奖信息数据
 */
public class HomeTextSwitcherBiz {

    Handler handler;

    public HomeTextSwitcherBiz(Handler handler) {
        this.handler = handler;
    }

    /**
     * 获取首页轮播中奖信息数据
     */
    public void getHomeRobredWinning(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(BaseApplication.getInstance());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.HOME_WINNING_INFO)
                .post(body)
                .build();


        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFailer("网络请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (handler != null) {
                        HomeWinningResponse info = new Gson().fromJson(response.body().string(), HomeWinningResponse.class);
                        if ("success".equals(info.getResult())){
                            Message msg = handler.obtainMessage();
                            msg.what = Constant.RESULT_HOME_WINNING;
                            msg.obj = info;
                            handler.sendMessage(msg);
                        }else{
                            onFailer(info.getErrorMsg());
                        }
                    }else{
                        onFailer("返回数据失效");
                    }
                }else{
                    onFailer("服务器异常 " + response.code());
                }
            }
        });
    }

    private void onFailer(final String errorMsg){
        UIUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseApplication.getInstance(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

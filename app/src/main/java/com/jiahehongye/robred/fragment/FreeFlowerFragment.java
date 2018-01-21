package com.jiahehongye.robred.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

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
 * Created by qianduan on 2016/12/9.
 */
public class FreeFlowerFragment extends Fragment {

    private View view;
    private TextView free_flower2text1, free_flower2text2, free_flowertext1;
    private Button yaoqing_now;
    private PersistentCookieStore persistentCookieStore;
    private Call call;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private String toFtl;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_LOGS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SET_DEBUG_APP,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.WRITE_APN_SETTINGS

    };
    private String leaveWord;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 555:
                    String share = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(share);
                        if (object.getString("result").equals("success")) {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 521:
                    String getshare = (String) msg.obj;
                    try {
                        JSONObject object = new JSONObject(getshare);
                        if (object.getString("result").equals("success")) {
                            JSONObject object2 = new JSONObject(object.getString("data"));
                            toFtl = object2.getString("toFtl");
                            leaveWord = object2.getString("leaveWord");

                            //6.0权限
                            int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                // We don't have permission so prompt the user
                                Toast.makeText(getActivity(), "分享失败,请尝试打开读取文件权限", Toast.LENGTH_SHORT).show();
                                ActivityCompat.requestPermissions(
                                        getActivity(),
                                        PERMISSIONS_STORAGE,//需要请求的所有权限，这是个数组String[]
                                        REQUEST_EXTERNAL_STORAGE//请求码
                                );
                            }

                            // 用来弹出错误日志
                            com.umeng.socialize.utils.Log.LOG = true;


                            UMImage image = new UMImage(getActivity(), R.drawable.logo);//设置分享图片
                            UMImage thumb = new UMImage(getActivity(), R.drawable.logo);//设置缩略图
                            image.compressStyle = UMImage.CompressStyle.SCALE;
                            image.setThumb(thumb);
                            ShareAction mAction = new ShareAction(getActivity())
                                    .withTitle(leaveWord)
                                    .withText("金猴宝红包")
                                    .withTargetUrl(toFtl)
                                    .withMedia(image)
                                    .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                                    .setCallback(umShareListener);
                            mAction.open();
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
        view = LayoutInflater.from(getActivity()).inflate(R.layout.free_flower, container, false);
//        if(Build.VERSION.SDK_INT>=23){
//            String[] mPermissionList = new String[]{
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.CALL_PHONE,
//                    Manifest.permission.READ_LOGS,
//                    Manifest.permission.READ_PHONE_STATE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.SET_DEBUG_APP,
//                    Manifest.permission.SYSTEM_ALERT_WINDOW,
//                    Manifest.permission.GET_ACCOUNTS,
//                    Manifest.permission.WRITE_APN_SETTINGS};
//            ActivityCompat.requestPermissions(getActivity(),mPermissionList,123);
//        }
        yaoqing_now = (Button) view.findViewById(R.id.yaoqing_now);
        free_flower2text1 = (TextView) view.findViewById(R.id.free_flower2text1);
        free_flowertext1 = (TextView) view.findViewById(R.id.free_flowertext1);
        free_flower2text2 = (TextView) view.findViewById(R.id.free_flower2text2);
        SpannableString a = new SpannableString(getString(R.string.free_flower1));
        a.setSpan(new ForegroundColorSpan(Color.RED), 10, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        a.setSpan(new ForegroundColorSpan(Color.RED), 22, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString b = new SpannableString(getString(R.string.free_flower2));
        b.setSpan(new ForegroundColorSpan(Color.RED), 14, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        b.setSpan(new ForegroundColorSpan(Color.RED), 27, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString c = new SpannableString(getString(R.string.free_flower3));
        c.setSpan(new ForegroundColorSpan(Color.RED), 13, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        free_flower2text1.setText(a);
        free_flower2text2.setText(b);
        free_flowertext1.setText(c);


        yaoqing_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getshare();
            }
        });
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_EXTERNAL_STORAGE:
                try {

                }catch (RuntimeException e){

                }
                break;
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            Toast.makeText(getActivity(), "分享成功", Toast.LENGTH_SHORT).show();
            sharedata();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getActivity(), "分享失败,请尝试打开读取文件权限", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(getActivity(),"分享取消", Toast.LENGTH_SHORT).show();
        }
    };

    private void sharedata() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.SHARE_SUCCESS)
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
                msg.what = 555;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getshare() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(getActivity());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        JSONObject jsonObject = new JSONObject();

        RequestBody body = RequestBody.create(Constant.JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constant.GET_SHARE)
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
                msg.what = 521;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });

    }

}

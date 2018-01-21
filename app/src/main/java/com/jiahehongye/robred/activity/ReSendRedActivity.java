package com.jiahehongye.robred.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.FaSong;
import com.jiahehongye.robred.cook.CookieJarImpl;
import com.jiahehongye.robred.cook.PersistentCookieStore;
import com.jiahehongye.robred.utils.UIUtils;
import com.jiahehongye.robred.view.MyProgressDialog;
import com.lling.photopicker.PhotoPickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReSendRedActivity extends BaseActivity implements View.OnClickListener {
    private FaSong data;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA

    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private CheckBox mSendredCheck;
    private RelativeLayout mRlAdvertising;
    private RelativeLayout mRlBrief;
    private RelativeLayout mRlWeb;
    private TextView mTvDes;
    private TextView mTvIsGeneral;
    private TextView mTvCurrentRed;
    private TextView mTvAllMoney;
    private TextView et_number, et_price;
    private TextView mTvTopPrice;
    private TextView mOk;
    private ImageView ad_pic;
    private ArrayList<String> result;
    private File f;
    private EditText sendred_et_message;
    private Uri uritempFile;
    private String urlpath;
    private static final int REQUESTCODE_CUTTING = 2; // 图片裁切标记
    private PersistentCookieStore persistentCookieStore;
    private MyProgressDialog animDialog;
    private Call call;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");
    private static final int SEND_SUCCESS = 6666;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEND_SUCCESS:
                    animDialog.dismiss();

                    String data1 = (String) msg.obj;
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(data1);
                        String res = jsonObject.getString("result");
                        if (res.equals("success")) {
                            Toast.makeText(ReSendRedActivity.this, "发红包成功", Toast.LENGTH_SHORT).show();
//                            JSONObject jsonObject2 = jsonObject.getJSONObject("data");
//                            String code = jsonObject2.getString("redEnveCode");
//                            Intent intent = new Intent(SendRedActivity.this, PayRedActivity.class);
//                            intent.putExtra("redCode", code);
//                            startActivity(intent);
////                            finish();
                        } else {
                            Toast.makeText(UIUtils.getContext(), jsonObject.optString("errorMsg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    private EditText sendred_et_brief_introduction;
    private EditText sendred_et_web_links;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_send_red);
        int permission = ActivityCompat.checkSelfPermission(ReSendRedActivity.this, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    ReSendRedActivity.this,
                    PERMISSIONS_STORAGE,//需要请求的所有权限，这是个数组String[]
                    REQUEST_EXTERNAL_STORAGE//请求码
            );
        }


        data = (FaSong) getIntent().getSerializableExtra("bean");
        initView();
    }
    private void initView() {
        sendred_et_brief_introduction = (EditText) findViewById(R.id.resendred_et_brief_introduction);
        sendred_et_web_links = (EditText) findViewById(R.id.resendred_et_web_links);
        sendred_et_message = (EditText) findViewById(R.id.resendred_et_message);
        ad_pic = (ImageView) findViewById(R.id.read_pic);
        ImageView mIvBack = (ImageView) findViewById(R.id.resendred_iv_back_row);
        ImageView mIvHelper = (ImageView) findViewById(R.id.resendred_iv_helper);
        mSendredCheck = (CheckBox) findViewById(R.id.resendred_checkbox);
        //描述信息
        mTvDes = (TextView) findViewById(R.id.resendred_tv_des_isshow);
        //广告图
        mRlAdvertising = (RelativeLayout) findViewById(R.id.resendred_rl_advertising_figure);
        //简介
        mRlBrief = (RelativeLayout) findViewById(R.id.resendred_rl_brief_introduction);
        //网站链接
        mRlWeb = (RelativeLayout) findViewById(R.id.resendred_rl_web_links);
        //确定发送
        mOk = (TextView) findViewById(R.id.resendred_tv_ok);
        //顶部总额
        mTvTopPrice = (TextView) findViewById(R.id.resendred_tv_money_count);

        mTvIsGeneral = (TextView) findViewById(R.id.resendred_tv_is_general);
        mTvCurrentRed = (TextView) findViewById(R.id.resendred_tv_current_red);
        mTvAllMoney = (TextView) findViewById(R.id.resendred_tv_all_money);
        et_number = (TextView) findViewById(R.id.resendred_et_number);
        et_price = (TextView) findViewById(R.id.resendred_et_count);

        ad_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permission = ActivityCompat.checkSelfPermission(ReSendRedActivity.this, Manifest.permission.CAMERA);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ReSendRedActivity.this, "请检查文件读取和拍照权限", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(ReSendRedActivity.this, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, 0);
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 1);
                    startActivityForResult(intent, 1);
                }

            }
        });



        mIvBack.setOnClickListener(this);
        mIvHelper.setOnClickListener(this);
        mTvIsGeneral.setOnClickListener(this);
        mOk.setOnClickListener(this);

        mTvTopPrice.setText(data.getRedEnveMoney());

        if (data.getRedEnveMode().equals("1")) {
            et_price.setText(data.getRedEnveMoney());
            mTvCurrentRed.setText("当前是手气红包");
            mTvAllMoney.setText("总金额");
        } else {
            et_price.setText(new BigDecimal(data.getRedEnveMoney()).divide(new BigDecimal(data.getRedEnveNum())) + "");
            mTvCurrentRed.setText("当前是普通红包");
            mTvAllMoney.setText("单个金额");
        }
        displayNetImage(data.getGrabRedEnveAd(),ad_pic);
        et_number.setText(data.getRedEnveNum());

        sendred_et_message.setText(data.getRedEnveLeaveword());
        sendred_et_brief_introduction.setText(data.getRedIntroduction());
        sendred_et_web_links.setText(data.getUserWap());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resendred_tv_ok:
                sendRed();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data!=null){
                result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                if (result.size() > 0) {
                    File f = new File(result.get(0));
                    startPhotoZoom(Uri.fromFile(f));
                }
            }

        }else if (requestCode ==REQUESTCODE_CUTTING){
            if(data!=null&&data.getData()!=null&&data.getData().getPath()!=null){
                f =  new File(data.getData().getPath());
                Glide.with(act).load(f).into(ad_pic);
            }
        }
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 25);
        intent.putExtra("aspectY", 16);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", false);
        uritempFile = Uri                .parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis()+"small.jpg");
        urlpath = uritempFile.getPath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }



    private void sendRed() {



        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("redEnveCode",data.getRedEnveCode());
        requestBody.addFormDataPart("id",data.getId());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ly_time = sdf.format(new java.util.Date());
        Log.e("时间==", ly_time);
        requestBody.addFormDataPart("sendRedEnveDate", ly_time);

        requestBody.addFormDataPart("userProp", "1");//个人用户
        requestBody.addFormDataPart("redEnveType", "1");//0无广告1有
        if(f == null){
            Toast.makeText(ReSendRedActivity.this, "请重新选择一张图片", Toast.LENGTH_SHORT).show();
            return;
        }else {
            requestBody.addFormDataPart("grabRedEnveAdFile", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
        }
        requestBody.addFormDataPart("redEnveNum", data.getRedEnveNum());
        requestBody.addFormDataPart("redEnveLeaveword", sendred_et_message.getText().toString());//红包留言
        requestBody.addFormDataPart("redIntroduction",sendred_et_brief_introduction.getText().toString());
        requestBody.addFormDataPart("userWap", sendred_et_web_links.getText().toString());

        showMyDialog();
        Request request = new Request.Builder()
                .url(Constant.RESENDURL)
                .post(requestBody.build())
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ReSendRedActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UIUtils.getContext(), "网络请求失败", Toast.LENGTH_SHORT).show();
                        animDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("发红包返回：", result);

                Message msg = handler.obtainMessage();
                msg.what = SEND_SUCCESS;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 显示对话框
     */
    public void showMyDialog() {
        animDialog = new MyProgressDialog(this, "玩命加载中...", R.drawable.loading);
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
}

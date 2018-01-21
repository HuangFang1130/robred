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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.Constant;
import com.jiahehongye.robred.R;
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
import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by huangjunhui on 2016/12/2.17:05
 */
public class SendRedActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private LinearLayout mChecked;
    private static final int REQUESTCODE_CUTTING = 2; // 图片裁切标记
    private Uri uritempFile;
    private String urlpath;
    private CheckBox mSendredCheck;
    private RelativeLayout mRlAdvertising;
    private RelativeLayout mRlBrief;
    private RelativeLayout mRlWeb;
    private TextView mTvDes;
    private TextView mTvIsGeneral;
    private TextView mTvCurrentRed;
    private TextView mTvAllMoney;
    private EditText et_number, et_price;
    private TextView mTvTopPrice;
    private TextView mOk;
    private boolean isGeneral, isPerson;

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
                            JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                            String code = jsonObject2.getString("redEnveCode");
                            Intent intent = new Intent(SendRedActivity.this, PayRedActivity.class);
                            intent.putExtra("redCode", code);
                            intent.putExtra("type","0");
                            startActivity(intent);
//                            finish();
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
    private ImageView ad_pic;
    private ArrayList<String> result;
    private File f;
    private EditText sendred_et_message;
    private EditText sendred_et_web_links;
    private EditText sendred_et_brief_introduction;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA

    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private CheckBox isshouqi,isputong,is_all,is_guanzhu;
    private String grab_status = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();
        mTintManager.setStatusBarTintResource(R.color.home_state_color);
        setContentView(R.layout.activity_sendred);
        isGeneral = false;
        isPerson = true;
        initView();

        int permission = ActivityCompat.checkSelfPermission(SendRedActivity.this, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    SendRedActivity.this,
                    PERMISSIONS_STORAGE,//需要请求的所有权限，这是个数组String[]
                    REQUEST_EXTERNAL_STORAGE//请求码
            );
        }
    }

    private void initView() {

        is_all = (CheckBox) findViewById(R.id.is_all);
        is_guanzhu = (CheckBox) findViewById(R.id.is_guanzhu);
        isshouqi = (CheckBox) findViewById(R.id.sendred_checkbox_isshouqi);
        isputong = (CheckBox) findViewById(R.id.sendred_checkbox_isputong);

        sendred_et_brief_introduction = (EditText) findViewById(R.id.sendred_et_brief_introduction);
        sendred_et_web_links = (EditText) findViewById(R.id.sendred_et_web_links);
        sendred_et_message = (EditText) findViewById(R.id.sendred_et_message);
        ad_pic = (ImageView) findViewById(R.id.ad_pic);
        ImageView mIvBack = (ImageView) findViewById(R.id.sendred_iv_back_row);
        TextView mIvHelper = (TextView) findViewById(R.id.sendred_iv_helper);
        mSendredCheck = (CheckBox) findViewById(R.id.sendred_checkbox);
        //切换是否为商家红包
        mChecked = (LinearLayout) findViewById(R.id.sendred_rl_checked);
        //描述信息
        mTvDes = (TextView) findViewById(R.id.sendred_tv_des_isshow);
        //广告图
        mRlAdvertising = (RelativeLayout) findViewById(R.id.sendred_rl_advertising_figure);
        //简介
        mRlBrief = (RelativeLayout) findViewById(R.id.sendred_rl_brief_introduction);
        //网站链接
        mRlWeb = (RelativeLayout) findViewById(R.id.sendred_rl_web_links);
        //确定发送
        mOk = (TextView) findViewById(R.id.sendred_tv_ok);
        //顶部总额
        mTvTopPrice = (TextView) findViewById(R.id.sendred_tv_money_count);

        mTvIsGeneral = (TextView) findViewById(R.id.sendred_tv_is_general);
        mTvCurrentRed = (TextView) findViewById(R.id.sendred_tv_current_red);
        mTvAllMoney = (TextView) findViewById(R.id.sendred_tv_all_money);
        et_number = (EditText) findViewById(R.id.sendred_et_number);
        et_price = (EditText) findViewById(R.id.sendred_et_count);

        ad_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permission = ActivityCompat.checkSelfPermission(SendRedActivity.this, Manifest.permission.CAMERA);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(SendRedActivity.this, "请检查文件读取和拍照权限", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(SendRedActivity.this, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, 0);
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 1);
                    startActivityForResult(intent, 1);
                }

            }
        });

        is_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(is_all.isChecked()){
                    is_guanzhu.setChecked(false);
                    grab_status = "0";
                }else {
                    is_guanzhu.setChecked(true);
                    grab_status = "1";
                }
            }
        });

        is_guanzhu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (is_guanzhu.isChecked()){
                    is_all.setChecked(false);
                    grab_status = "1";
                }else {
                    is_all.setChecked(true);
                    grab_status = "0";
                }
            }
        });

        isshouqi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){//当前是选中的时候走的方法

                    mTvAllMoney.setText("总金额");
                    isputong.setChecked(false);
                    isGeneral=false;
                    if (!TextUtils.isEmpty(et_price.getText().toString())&&!TextUtils.isEmpty(et_number.getText().toString())){
                        et_price.setText(mTvTopPrice.getText().toString());
                    }



                }else {
                    mTvAllMoney.setText("单个金额");
                    isputong.setChecked(true);
                    isGeneral=true;
                    if (!TextUtils.isEmpty(et_price.getText().toString())&&!TextUtils.isEmpty(et_number.getText().toString())) {
                        double price = Double.parseDouble(et_price.getText().toString());
                        int num = Integer.parseInt(et_number.getText().toString());


                        String dange = price / num + "";
                        DecimalFormat format = new DecimalFormat("0.00");

                        String GG = price * num + "";
                        String ForGG = format.format(new BigDecimal(dange));
                        mTvTopPrice.setText(ForGG);
                    }


                }
            }
        });

        isputong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isputong.isChecked()){

                    mTvAllMoney.setText("单个金额");
                    isshouqi.setChecked(false);
                    isGeneral=true;
                    if (!TextUtils.isEmpty(et_price.getText().toString())&&!TextUtils.isEmpty(et_number.getText().toString())) {
                        double price = Double.parseDouble(et_price.getText().toString());
                        int num = Integer.parseInt(et_number.getText().toString());
                        DecimalFormat format = new DecimalFormat("0.00");
                        String GG = price / num + "";
                        String ForGG = format.format(new BigDecimal(GG));
                        et_price.setText(ForGG);
                    }


                }else {
                    mTvAllMoney.setText("总金额");
                    isshouqi.setChecked(true);
                    isGeneral=false;
                    if (!TextUtils.isEmpty(et_price.getText().toString())&&!TextUtils.isEmpty(et_number.getText().toString())) {
//
                        et_price.setText(mTvTopPrice.getText().toString());
//
                    }



                }
            }
        });

        et_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_number.length() > 0 && et_price.length() > 0) {
                    if (!isGeneral) {//手气
                        DecimalFormat format = new DecimalFormat("0.00");
                        String abc = et_price.getText().toString();
                        if (editable.length() == 0) {
                            mTvTopPrice.setText("0.00");
                        } else {
                            String a = format.format(new BigDecimal(abc));
                            mTvTopPrice.setText(a);
                        }
                        Log.e("手气", et_price.getText().toString() + "");
                    } else {//普通
                        Editable singleMText = et_price.getText();
                        Editable numText = et_number.getText();
                        if (singleMText != null && !"".equals(singleMText.toString()) && numText != null
                                && !"".equals(numText.toString())) {
                            BigDecimal bSingleMoney = new BigDecimal(singleMText.toString());
                            BigDecimal bNum = new BigDecimal(numText.toString());
                            BigDecimal totalAmount = bSingleMoney.multiply(bNum);
                            float n = totalAmount.floatValue();
                            DecimalFormat format = new DecimalFormat("0.00");
                            String abc = n + "";
                            if (editable.length() == 0) {
                                mTvTopPrice.setText("0.00");
                            } else {
                                String a = format.format(new BigDecimal(abc));
                                mTvTopPrice.setText(a);
                            }
                            Log.d("单个金额：", Float.parseFloat(et_price.getText().toString()) + "");
                            Log.e("总额：", mTvTopPrice.getText().toString() + "");
                        }
                    }
                } else {
                    mTvTopPrice.setText("0.00");
                }
            }
        });

        et_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //限制住小数点不能为第一位，并且小数点后只能有两位小数
                if (charSequence.toString().contains(".")) {
                    if (charSequence.length() - 1 - charSequence.toString().indexOf(".") > 2) {
                        charSequence = charSequence.toString().subSequence(0, charSequence.toString().indexOf(".") + 3);
                        et_price.setText(charSequence);
                        et_price.setSelection(charSequence.length());
                        if (et_number.getText().length() > 0 && isGeneral) {
                            mTvTopPrice.setText(Integer.parseInt(et_number.getText().toString())
                                    * Float.parseFloat(et_price.getText().toString()) + "");
                        }
                    }
                }
                if (charSequence.toString().trim().substring(0).equals(".")) {
                    charSequence = "0" + charSequence;
                    et_price.setText(charSequence);
                    et_price.setSelection(2);
                }

                if (charSequence.toString().startsWith("0") && charSequence.toString().trim().length() > 1) {
                    if (!charSequence.toString().substring(1, 2).equals(".")) {
                        et_price.setText(charSequence.subSequence(0, 1));
                        et_price.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_number.length() > 0 && et_price.length() > 0) {
                    if (et_price.getText().toString().equals(".")) {
                        et_price.setText("0.");
                        et_price.setSelection(2);
                    }
                    if (!isGeneral) {//手气
                        DecimalFormat format = new DecimalFormat("0.00");
                        String abc = et_price.getText().toString();
                        if (editable.length() == 0) {
                            mTvTopPrice.setText("0.00");
                        } else {
                            String a = format.format(new BigDecimal(abc));
                            mTvTopPrice.setText(a);
                        }
                        Log.e("手气", et_price.getText().toString() + "");
                    } else {//普通
                        Editable singleMText = et_price.getText();
                        Editable numText = et_number.getText();
                        if (singleMText != null && !"".equals(singleMText.toString()) && numText != null
                                && !"".equals(numText.toString())) {
                            BigDecimal bSingleMoney = new BigDecimal(singleMText.toString());
                            BigDecimal bNum = new BigDecimal(numText.toString());
                            BigDecimal totalAmount = bSingleMoney.multiply(bNum);
                            float n = totalAmount.floatValue();
                            DecimalFormat format = new DecimalFormat("0.00");
                            String abc = n + "";
                            if (editable.length() == 0) {
                                mTvTopPrice.setText("0.00");
                            } else {
                                String a = format.format(new BigDecimal(abc));
                                mTvTopPrice.setText(a);
                            }
                            Log.d("单个金额：", Float.parseFloat(et_price.getText().toString()) + "");
                            Log.e("总额：", n + "");

                        } else {
                            mTvTopPrice.setText("0.00");
                        }
                    }
                } else {
                    mTvTopPrice.setText("0.00");
                }
            }
        });

        mChecked.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mIvHelper.setOnClickListener(this);
        mSendredCheck.setOnCheckedChangeListener(this);
        mTvIsGeneral.setOnClickListener(this);
        mOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendred_iv_back_row://关闭当前
                finish();
                break;
            case R.id.sendred_rl_checked://切换是否为商家红包
                if (isPerson) {
                    mSendredCheck.setChecked(false);
                } else {
                    mSendredCheck.setChecked(true);
                }
                isPerson = !isPerson;
                break;
            case R.id.sendred_iv_helper://帮助中心
                Intent intent1 = new Intent(getApplicationContext(),WebActivity.class);
                intent1.putExtra("title","抢红包规则");
                intent1.putExtra("URL", Constant.URL+"/wap/redEnveRules.jhtml");
                startActivity(intent1);
                break;
            case R.id.sendred_tv_is_general://更改红包类型
                if (isGeneral) {
                    mTvCurrentRed.setText("当前为手气红包， ");
                    mTvIsGeneral.setText("改为普通红包");
                    mTvAllMoney.setText("总金额");
                    et_price.setHint("填写红包总金额");
                } else {
                    mTvCurrentRed.setText("当前为普通红包， ");
                    mTvIsGeneral.setText("改为手气红包");
                    mTvAllMoney.setText("单个金额");
                    et_price.setHint("填写单个红包金额");
                }
                isGeneral = !isGeneral;
                //切换时重置所有数据
                et_price.setText("");
                et_number.setText("");
                mTvTopPrice.setText("0.00");
                break;
            case R.id.sendred_tv_ok:
                String number = et_number.getText().toString();
                String price = et_price.getText().toString();
//                String sumPrice = mTvTopPrice.getText().toString();

                if (number.length() == 0) {
                    Toast.makeText(UIUtils.getContext(), "请填写红包个数", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (price.length() == 0) {
                    Toast.makeText(UIUtils.getContext(), "请填写红包金额", Toast.LENGTH_SHORT).show();
                    return;
                }

//                if (sumPrice.length() == 0) {
//                    Toast.makeText(UIUtils.getContext(), "请填写红包金额", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if(!isGeneral){//如果是手气红包，判断是否单个小于0.01
                    double cachePrice = Double.parseDouble(price) / Double.parseDouble(number);
                    if (cachePrice < 0.01) {
                        Toast.makeText(UIUtils.getContext(), "单个红包不能小于0.01元", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                sendRed(number, price);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            if (requestCode == 1) {

                result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                if (result.size() > 0) {
                    File f = new File(result.get(0));
                    startPhotoZoom(Uri.fromFile(f));


                }
            }else if (requestCode ==REQUESTCODE_CUTTING){
                if(data!=null&&data.getData()!=null&&data.getData().getPath()!=null){
                    f =  new File(data.getData().getPath());
                    Glide.with(act).load(f).into(ad_pic);
                }
            }
        }

    }

    /**
     * 发红包请求开始
     */
    private PersistentCookieStore persistentCookieStore;
    private MyProgressDialog animDialog;
    private Call call;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");
    private void sendRed(String number, String price) {



        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        persistentCookieStore = new PersistentCookieStore(this);
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        OkHttpClient client = builder.build();

        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("userProp", "1");//个人用户

        requestBody.addFormDataPart("redType", grab_status);//手气红包

        if (mSendredCheck.isChecked()){
            requestBody.addFormDataPart("redIntroduction",sendred_et_brief_introduction.getText().toString());
            requestBody.addFormDataPart("userWap", sendred_et_web_links.getText().toString());
            requestBody.addFormDataPart("redEnveType", "1");//0无广告1有
            if(f == null){
                Toast.makeText(SendRedActivity.this, "请选择一张图片", Toast.LENGTH_SHORT).show();
                return;
            }else {
                requestBody.addFormDataPart("grabRedEnveAdFile", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
            }
        }else {
            requestBody.addFormDataPart("redEnveType", "0");//0无广告1有


        }
        requestBody.addFormDataPart("redEnveNum", number);
        requestBody.addFormDataPart("redEnveMoney", price);
        if (isGeneral) {
            requestBody.addFormDataPart("redEnveMode", "0");//普通红包
        } else {
            requestBody.addFormDataPart("redEnveMode", "1");//手气红包
        }
        requestBody.addFormDataPart("redEnveLeaveword", sendred_et_message.getText().toString());//红包留言




        showMyDialog();
        Request request = new Request.Builder()
                .url(Constant.SEND_RED)
                .post(requestBody.build())
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SendRedActivity.this.runOnUiThread(new Runnable() {
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

    /**
     * 判断是否是个人红包？
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {//商家红包
            mRlAdvertising.setVisibility(View.VISIBLE);
            mRlBrief.setVisibility(View.VISIBLE);
            mRlWeb.setVisibility(View.VISIBLE);
            mTvDes.setVisibility(View.VISIBLE);
        } else {//个人红包
            mRlAdvertising.setVisibility(View.GONE);
            mRlBrief.setVisibility(View.GONE);
            mRlWeb.setVisibility(View.GONE);
            mTvDes.setVisibility(View.GONE);
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
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis()+"small.jpg");
        urlpath = uritempFile.getPath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }
}

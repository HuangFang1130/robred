package com.jiahehongye.robred.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiahehongye.robred.R;

/**
 * Created by jiahe007 on 2016/12/16 0016.
 */
public class MyProgressDialog extends ProgressDialog {

    private Context context;
    private AlertDialog dialog;

    public MyProgressDialog(Context context, int theme) {
        super(context, R.style.CommProgressDialog);
        // TODO Auto-generated constructor stub
    }

    public MyProgressDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    private AnimationDrawable mAnimation;
    private ImageView mImageView;
    private TextView mTextView;
    private String loadingTip;
    private int resid;

    /**
     *
     * @param context 上下文对象
     * @param content 显示文字提示信息内容
     * @param resid 动画id
     */
    public MyProgressDialog(Context context, String content, int resid) {
        super(context,0);
        this.context = context;
        this.loadingTip = content;
        this.resid = resid;
        //点击提示框外面是否取消提示框
        setCanceledOnTouchOutside(false);
        //点击返回键是否取消提示框
        setCancelable(false);
        setIndeterminate(true);
        setProgressStyle(R.style.CustomDialog00);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.progress_dialog);

        mTextView = (TextView) findViewById(R.id.loadingTv);
        mImageView = (ImageView) findViewById(R.id.loadingIv);

        mImageView.setImageResource(resid);
        // 通过ImageView对象拿到背景显示的AnimationDrawable
        mAnimation = (AnimationDrawable) mImageView.getBackground();

        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();
            }
        });
        mTextView.setText(loadingTip);
    }


}

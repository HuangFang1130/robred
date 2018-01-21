package com.jiahehongye.robred.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.WindowManager;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;

/**
 * Created by huangjunhui on 2016/12/21.16:08
 */
public class ChatPopwindowActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyKitKatTranslucency();

        setContentView(R.layout.ease_chat_popwindow);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(params);
    }
}

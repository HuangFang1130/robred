package com.jiahehongye.robred.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.utils.UIUtils;

import uk.co.senab.photoview.PhotoView;

public class LookHeadImageActivity extends AppCompatActivity {

    private PhotoView single_photo;
    private RelativeLayout others;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_head_image);

        single_photo = (PhotoView) findViewById(R.id.single_photo);
        others = (RelativeLayout) findViewById(R.id.others);

        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            String string = bundle.getString("url");
            Glide.with(UIUtils.getContext()).load(string).asBitmap().into(single_photo);
        }
    }
}

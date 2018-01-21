package com.jiahehongye.robred.oneyuan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;

import com.jiahehongye.robred.R.id;
import com.jiahehongye.robred.R.layout;
import com.jiahehongye.robred.view.MySquareImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SeePhotoActivity extends AppCompatActivity {
	
	private int index = 0;
	
	private ViewPager vp ;
	private ArrayList<String> urlList = new ArrayList<String>();
	private ArrayList<MySquareImageView> list = new ArrayList<MySquareImageView>();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_see_photo);
//		base_title.setText("晒图");
		
		Intent intent = getIntent();
		urlList = intent.getStringArrayListExtra("list");
		index = intent.getIntExtra("index", 1);
		Log.e("list==", urlList.toString()+" "+index);
		
		vp = (ViewPager) findViewById(id.vp_seephoto);
		
		for (int i = 0; i < urlList.size(); i++) {
			MySquareImageView iv = new MySquareImageView(this);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			iv.setLayoutParams(params);
			iv.setScaleType(ScaleType.FIT_XY);
			Picasso.with(this).load(urlList.get(i)).resize(500, 500).into(iv);
			list.add(iv);
		}
		MyPagerAdapter bannerAdapter = new MyPagerAdapter();
		vp.setAdapter(bannerAdapter);
		
		vp.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				vp.setCurrentItem(index);
			}
		});
		
	}
	
	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(list.get(position));
			return list.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(list.get(position));
		}
	}
	
}

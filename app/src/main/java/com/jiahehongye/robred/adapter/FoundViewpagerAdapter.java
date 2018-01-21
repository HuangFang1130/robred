package com.jiahehongye.robred.adapter;

import java.util.List;

import com.jiahehongye.robred.MainActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.OneyuanHomeBannerBean.Banner;
import com.jiahehongye.robred.oneyuan.OneyuanBannerUrlActivity;
import com.jiahehongye.robred.oneyuan.OneyuanProductDetailActivity;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class FoundViewpagerAdapter extends PagerAdapter {

	private List<Banner> data2;
	private Context context;

	public FoundViewpagerAdapter(Context context, List<Banner> data2) {
		this.context = context;
		this.data2 = data2;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE / 2;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// 这里设置了无限轮播
		int newPosition = position %= data2.size();
//		Log.e("Position", "newPosition=" + newPosition + "     position=" + position);
		String url = data2.get(newPosition).getImage();
		ImageView imageView = new ImageView(context);

		imageView.setScaleType(ScaleType.FIT_XY);
		// 加载图片
		Picasso.with(context).load(url).into(imageView);
		container.addView(imageView);
		imageView.setTag(R.id.tag_first, newPosition);
		if (data2.get(newPosition).getIsNetwork().equals("0")) {// 如果是包含产品ID
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent2 = new Intent(context, OneyuanProductDetailActivity.class);
					intent2.putExtra("productId", data2.get((Integer) v.getTag(R.id.tag_first)).getProductId());
					context.startActivity(intent2);
				}
			});
		} else {
			// 如果是包含外网地址
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 把当前option下的url获取到传递过去
					Intent intent = new Intent(context, OneyuanBannerUrlActivity.class);
					intent.putExtra("url", data2.get((Integer) v.getTag(R.id.tag_first)).getUrl());
					context.startActivity(intent);
				}
			});
		}

		return imageView;

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// container.removeViewAt(position-1);
		container.removeView((View) object);

	}

}

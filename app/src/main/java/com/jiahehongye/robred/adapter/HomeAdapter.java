package com.jiahehongye.robred.adapter;

import java.util.List;

import com.jiahehongye.robred.MainActivity;
import com.jiahehongye.robred.aview.HomeViewpager;
import com.jiahehongye.robred.bean.HomeBean.Banner;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class HomeAdapter extends PagerAdapter {

	private List<Banner> data2;
	private Context context;

	public HomeAdapter(Context context, List<Banner> data2) {
		this.context = context;
		this.data2 = data2;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// return data2.size();
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
		final int newPosition = position %= data2.size();
		String url = data2.get(newPosition).getAdvertAddr();
		ImageView imageView = new ImageView(context);

		imageView.setScaleType(ScaleType.FIT_XY);  
		// 加载图片
		Picasso.with(context).load(url).fit().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(imageView);
		container.addView(imageView); 
		try {
			if (!TextUtils.isEmpty(data2.get(newPosition).getHrefAddr())) {
				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 把当前option下的url获取到传递过去
						System.out.println(data2.get(newPosition).getHrefAddr());
						Intent intent = new Intent(context, HomeViewpager.class);
						intent.putExtra("url", data2.get(newPosition).getHrefAddr());
						context.startActivity(intent);

					}
				});
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		

		return imageView;

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// container.removeViewAt(position-1);
		container.removeView((View) object);

	}

}

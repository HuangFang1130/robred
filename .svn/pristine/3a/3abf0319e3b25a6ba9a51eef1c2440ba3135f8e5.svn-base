package com.jiahehongye.robred.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jiahehongye.robred.BaseActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.utils.UIUtils;

import java.util.ArrayList;

public class SettingWelcome extends BaseActivity {
	private ViewPager vp_guide_bg;
	private ArrayList<ImageView> imgs;
	int[] imgIds = new int[] { R.drawable.guid1, R.drawable.guid2, R.drawable.guid3, R.drawable.guid4 };
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_welcome);
//		base_title.setText("欢迎导航");
		initData();
		init();
		
	}

	private void init() {

		vp_guide_bg = (ViewPager) findViewById(R.id.vp_guide_bg);
		vp_guide_bg.setAdapter(new Myadapter());

	}

	private void initData() {

			imgs = new ArrayList<>();
			ImageView imageView;
			for (int i : imgIds) {
				imageView = new ImageView(this);
				Bitmap bitmap = UIUtils.readBitMap(this, i);
				imageView.setImageBitmap(bitmap);
				imgs.add(imageView);
			}


//		for (int i = 0; i < imgIds.length; i++) {
//			ImageView imageView = new ImageView(SettingWelcome.this);
//
//			//防止内存溢出方案。
//			BitmapFactory.Options opt = new BitmapFactory.Options();
//
//			opt.inPreferredConfig = Bitmap.Config.RGB_565;
//
//			opt.inPurgeable = true;
//
//			opt.inInputShareable = true;
//
//			InputStream is = getResources().openRawResource(imgIds[i]);
//
//			Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
//
//			BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
//
//			imageView.setBackgroundDrawable(bd);
//
////			imageView.setBackgroundResource(imgIds[i]);
//			imgs.add(imageView);
//
//		}
	}

	class Myadapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imgs.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = imgs.get(position);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			ViewGroup parent = (ViewGroup) imageView.getParent();
			if (parent != null) {
				parent.removeAllViewsInLayout();
			}
			container.addView(imageView);
			return imageView;
		}

	}

}

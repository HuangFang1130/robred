package com.jiahehongye.robred.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;


import com.jiahehongye.robred.MainActivity;
import com.jiahehongye.robred.R;
import com.jiahehongye.robred.application.BaseApplication;
import com.jiahehongye.robred.bean.AddressDetailResult;
import com.jiahehongye.robred.bean.PhotoBean;
import com.jiahehongye.robred.utils.DisplayImageOptionsUtil;
import com.jiahehongye.robred.view.PhotoViewPager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

/**
 * @author Cloudsoar(wangyb)
 * @datetime 2015-11-19 20:53 GMT+8
 * @email 395044952@qq.com
 */
public class PhotoviewActivity extends AppCompatActivity implements OnViewTapListener {
    private PhotoViewPager mViewPager;
    private PhotoView mPhotoView;
    private ArrayList<AddressDetailResult.PhotosBean> mImgUrls;
    private PhotoViewAdapter mAdapter;
    private PhotoViewAttacher mAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewpager);
        setupView();
        setupData();
    }

    private void setupView(){
        mViewPager = (PhotoViewPager) findViewById(R.id.view_pager);
        mPhotoView = (PhotoView) findViewById(R.id.photo);
    }

    private void setupData(){
        int mCurrentUrl = getIntent().getIntExtra(ContanctDetailActivity.PHOTO_POSITION,0);
        mImgUrls = (ArrayList<AddressDetailResult.PhotosBean>) getIntent().getSerializableExtra("infoList");
        mAdapter = new PhotoViewAdapter();
        mViewPager.setAdapter(mAdapter);
        //设置当前需要显示的图片
        mViewPager.setCurrentItem(mCurrentUrl);
    }

    @Override
    public void onViewTap(View view, float x, float y) {
        finish();
    }

    class PhotoViewAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = container.inflate(PhotoviewActivity.this,
                    R.layout.item_photo_view,null);
            mPhotoView = (PhotoView) view.findViewById(R.id.photo);
            //使用ImageLoader加载图片
            BaseApplication.getInstance().imageLoader.displayImage(
                    mImgUrls.get(position).getPhoto(),mPhotoView, DisplayImageOptionsUtil.defaultOptions);
            //给图片增加点击事件
            mAttacher = new PhotoViewAttacher(mPhotoView);
            mAttacher.setOnViewTapListener(PhotoviewActivity.this);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mAttacher = null;
            container.removeView((View)object);
        }

        @Override
        public int getCount() {
            return mImgUrls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}

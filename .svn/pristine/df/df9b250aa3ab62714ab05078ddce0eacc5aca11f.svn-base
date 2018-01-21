package com.jiahehongye.robred.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MySquareImageView extends ImageView {

	public MySquareImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MySquareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MySquareImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = Math.round(width*1);
        setMeasuredDimension(width,height);
	}

}

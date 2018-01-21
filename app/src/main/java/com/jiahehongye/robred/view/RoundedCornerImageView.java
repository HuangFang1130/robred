package com.jiahehongye.robred.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2018/1/21 0021.
 */

public class RoundedCornerImageView extends View {

    private Bitmap mImage;

    private Paint mBitmapPaint;
    private RectF mBounds;
    private float mRadius = 25.0f;

    public RoundedCornerImageView(Context context) {
        super(context);
        init();
    }

    public RoundedCornerImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedCornerImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }



    private void init() {
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBounds = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height ,width;
        height = width = 0;
        int imageHeight,imageWidth;
        if(mImage ==null){
            imageHeight = imageWidth = 0;
        }else {
            imageHeight = mImage.getHeight();
            imageWidth = mImage.getWidth();

        }
        width = getMeasurement(widthMeasureSpec,imageWidth);
        height = getMeasurement(heightMeasureSpec,imageHeight);

        setMeasuredDimension(width,height);

    }

    private  int getMeasurement(int measureSpec, int contentSize){
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (MeasureSpec.getMode(measureSpec)){
            case MeasureSpec.AT_MOST:
                return Math.min(specSize,contentSize);
            case MeasureSpec.UNSPECIFIED:
                return contentSize;
            case MeasureSpec.EXACTLY:
                return specSize;
        }

        return 0;
    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//      if(w!=oldw ||h!=oldh){
//
//          int imageWidth ,imageHeight;
//          if(mImage == null) {
//              imageWidth = imageHeight = 0;
//          }else {
//              imageWidth = mImage.getWidth();
//              imageHeight = mImage.getHeight();
//          }
//          int left = (w - imageWidth)/7;
//          int top = (h - imageHeight)/7;
//          mBounds.set(left,top,left+imageWidth,top+imageHeight);
//            if(mBitmapPaint.getShader()!=null){
//                Matrix m = new Matrix();
//                m.setTranslate(left,top);
//                mBitmapPaint.getShader().setLocalMatrix(m);
//            }
//      }
//    }

    public void setImage(Bitmap bitmap){
        if(mImage!=bitmap){
            mImage =  bitmap;
            if(mImage!=null){
                BitmapShader shader =  new BitmapShader(mImage, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                mBitmapPaint.setShader(shader);
            }else {
                mBitmapPaint.setShader(null);
            }
            requestLayout();
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mBitmapPaint!=null){
            canvas.drawRoundRect(mBounds,mRadius,mRadius,mBitmapPaint);
        }
    }
}

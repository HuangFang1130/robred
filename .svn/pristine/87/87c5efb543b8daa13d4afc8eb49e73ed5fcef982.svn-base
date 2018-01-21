package com.jiahehongye.robred.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jiahehongye.robred.R;

/**
 * Created by huangjunhui on 2017/5/24.16:06
 */
public class ZoomListView extends ListView {
    // 最大的伸缩量
    private final float defaultHeaderMaxScale = 1.2f;
    // 头部最大的高度
    private float headerMaxHeight;
    // 头部初始高度
    private float headerHeight;
    // 头部默认初始高度
    private float defaultHeaderHeight;
    // 头部默认最大的高度
    private float defaultHeaderMaxHeight;
    private ImageView headerView;
    private ViewGroup.LayoutParams layoutParams;
    private LinearLayout linearLayout;
    // 最大的缩放值
    private float headerMaxScale;

    public ZoomListView(Context context) {
        this(context, null);
    }

    public ZoomListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultHeaderHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, context.getResources().getDisplayMetrics());
        defaultHeaderMaxHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, context.getResources().getDisplayMetrics());
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZoomListView);
        headerHeight = a.getDimension(R.styleable.ZoomListView_header_height, defaultHeaderHeight);
        headerMaxHeight = a.getDimension(R.styleable.ZoomListView_header_max_height, defaultHeaderMaxHeight);
        headerMaxScale = a.getFloat(R.styleable.ZoomListView_header_max_scale, defaultHeaderMaxScale);
        a.recycle();
        initView();
    }
    private void initView() {
        headerView = new ImageView(getContext());
        headerView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        linearLayout = new LinearLayout(getContext());
        linearLayout.addView(headerView);
        layoutParams = headerView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) headerHeight);
        } else {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = (int) headerHeight;
        }
        headerView.setLayoutParams(layoutParams);
        addHeaderView(linearLayout);
    }

    public void setDrawableId(int id) {
        headerView.setImageResource(id);
    }
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if (deltaY < 0 && isTouchEvent) {
            if (headerView.getHeight() < headerMaxHeight) {
                int newHeight = headerView.getHeight()
                        + Math.abs(deltaY / 3);
                headerView.getLayoutParams().height = newHeight;
                headerView.requestLayout();
                float temp = 1 + (headerMaxScale - 1f) * (headerView.getHeight() - headerHeight) / (headerMaxHeight - headerHeight);
                headerView.animate().scaleX(temp)
                        .scaleY(temp).setDuration(0).start();
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                startAnim();
                break;
        }
        return super.onTouchEvent(ev);
    }

    // 开始执行动画
    private void startAnim() {
        ValueAnimator animator = ValueAnimator.ofFloat(headerView.getHeight(), headerHeight);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                headerView.getLayoutParams().height = (int) fraction;
                headerView.requestLayout();
            }
        });
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());

        ValueAnimator animator2 = ValueAnimator.ofFloat(headerView.getScaleX(), 1f);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                headerView.setScaleX(fraction);
                headerView.setScaleY(fraction);
            }
        });
        animator2.setDuration(500);
        animator2.setInterpolator(new LinearInterpolator());
        animator.start();
        animator2.start();
    }

}

package com.jiahehongye.robred.adapter;

import android.support.v7.widget.RecyclerView;

import com.jiahehongye.robred.interfaces.MyHeadViewClickListener;
import com.jiahehongye.robred.interfaces.MyItemClickListener;

/**
 * Created by huangjunhui on 2017/5/18.11:56
 */
public abstract class BaseRecycleViewAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    protected MyHeadViewClickListener mHeadClickListener;

    //item类型
    public static final int ITEM_TYPE_HEADER = 1;
    public static final int ITEM_TYPE_CONTENT = 2;
    public static final int ITEM_TYPE_FOOT = 3;
    public static final int ITEM_TYPE_CONTENT_TWO = 4;
    public static final int ITEM_TYPE_BIG_ADV = 5;
    public static final int ITEM_TYPE_ADV = 6;
    public static final int ITEM_TYPE_EMPTY = 10;
    public static final int ITEM_TYPE_CONTENT_ADV_THREE = 12;
    public static final int ITEM_TYPE_CONTENT_THREE = 15;



    protected MyItemClickListener mItemClickListener;



    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }
    public void setOnHeadClickListener(MyHeadViewClickListener listener) {
        this.mHeadClickListener = listener;
    }
}

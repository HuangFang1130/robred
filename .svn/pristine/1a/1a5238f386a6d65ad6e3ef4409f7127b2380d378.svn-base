package com.jiahehongye.robred.view;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.jiahehongye.robred.R;
import com.jiahehongye.robred.adapter.SendGiftRecycleAdapter;
import com.jiahehongye.robred.bean.ChatGiftResult;
import com.jiahehongye.robred.interfaces.MyItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjunhui on 2016/12/21.16:45
 */
public class ChatPopPager {


    private List<ChatGiftResult.DataBean.GiftLBean> giftList;
    private FragmentActivity activity;
    private GridLayoutManager gridLayoutManager;
    private SendGiftRecycleAdapter sendGiftRecycleAdapter;
    private ArrayList<String> integers;
    private String currentGiftId;
    private CheckBox currentCheckBox;
    private String currentGifImage;
    private String currentGifName;
    private String currentGifDimon;

    public ChatPopPager(FragmentActivity activity, List<ChatGiftResult.DataBean.GiftLBean> giftList) {
        this.activity = activity;
        this.giftList = giftList;
    }

    public View initView() {
        integers = new ArrayList<>();
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_send_gift, null);
        RecyclerView mGiftRecycle = (RecyclerView) view.findViewById(R.id.send_gift_recycleview);
        gridLayoutManager = new GridLayoutManager(activity, 4);
        mGiftRecycle.setLayoutManager(gridLayoutManager);
        mGiftRecycle.setItemAnimator(new DefaultItemAnimator());

        sendGiftRecycleAdapter = new SendGiftRecycleAdapter(activity, giftList);
        mGiftRecycle.setAdapter(sendGiftRecycleAdapter);
        sendGiftRecycleAdapter.setOnItemClickListener(new MyItemClickListener() {

            @Override
            public void onItemClick(View view, int postion) {


                CheckBox mCheckBox = (CheckBox) view.findViewById(R.id.chat_send_gift_checkbox);
                if (currentCheckBox != null && currentCheckBox != mCheckBox) {
                    currentCheckBox.setChecked(false);
                    currentGiftId = "";
                    currentGifImage = "";
                    currentGifName = "";
                    currentGifDimon = "";
                }
                if (mCheckBox.isChecked()) {
                    mCheckBox.setChecked(false);
                    currentGiftId = "";
                    currentGifImage = "";
                    currentGifName = "";
                    currentGifDimon = "";

                } else {
                    mCheckBox.setChecked(true);
                    currentGiftId = giftList.get(postion).getId();
                    currentGifImage = giftList.get(postion).getGiftAddr();
                    currentGifName = giftList.get(postion).getName();
                    currentGifDimon = giftList.get(postion).getDiamondNum();

                }




                currentCheckBox = mCheckBox;
            }
        });
        return view;
    }

    public String getGiftId() {
        return currentGiftId;
    }

    public String getGifImage() {
        return currentGifImage;
    }

    public String getGifName() {
        return currentGifName;
    }
    public String getGifDimon() {
        return currentGifDimon;
    }
}

package com.jiahehongye.robred.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jiahehongye.robred.R;
import com.jiahehongye.robred.R.id;
import com.jiahehongye.robred.bean.OneyuanPastAnnouncedBean.ResultList;
import com.jiahehongye.robred.oneyuan.OneyuanProductDetailActivity;
import com.jiahehongye.robred.oneyuan.SeePhotoActivity;
import com.jiahehongye.robred.view.CircleImageView;
import com.jiahehongye.robred.view.MySquareImageView;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OneyuanPastAnnouncedAdapter extends BaseAdapter {

	private Context context;
	private List<ResultList> list;

	public OneyuanPastAnnouncedAdapter(Context context, List<ResultList> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list != null) {
			return list.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_oneyuan_past_announced, null);
			holder = new ViewHolder();
			holder.mGoto = (RelativeLayout) convertView.findViewById(id.rl_oneyuan_past_goto);
			holder.noStage = (TextView) convertView.findViewById(id.tv_oneyuan_past_lastissue);
			holder.time = (TextView) convertView.findViewById(id.tv_oneyuan_past_lasttime);
			holder.month = (TextView) convertView.findViewById(id.tv_oneyuan_past_lastday);
			holder.name = (TextView) convertView.findViewById(id.tv_oneyuan_past_name);
			holder.code = (TextView) convertView.findViewById(id.tv_oneyuan_past_code);
			holder.message = (TextView) convertView.findViewById(id.tv_oneyuan_past_message);
			holder.messageHalf = (LinearLayout) convertView.findViewById(id.ll_oneyuan_past_message_half);
			holder.messageAll = (TextView) convertView.findViewById(id.tv_oneyuan_past_message_all);
			holder.layoutMessage = (RelativeLayout) convertView.findViewById(id.rl_oneyuan_past_havemessage);
			holder.photoFirst = (RelativeLayout) convertView.findViewById(id.rl_oneyuan_past_first);
			holder.photoSecond = (RelativeLayout) convertView.findViewById(id.rl_oneyuan_past_second);
			holder.visible_msg = (TextView) convertView.findViewById(id.tv_oneyuan_past_visible_havemessage);
			holder.visible_photo_open = (TextView) convertView
					.findViewById(id.tv_oneyuan_past_visible_havephoto_open);
			holder.visible_photo_close = (TextView) convertView
					.findViewById(id.tv_oneyuan_past_visible_havephoto_close);
			holder.image1 = (MySquareImageView) convertView.findViewById(id.iv_oneyuan_past_lastwinnerimage1);
			holder.image2 = (MySquareImageView) convertView.findViewById(id.iv_oneyuan_past_lastwinnerimage2);
			holder.image3 = (MySquareImageView) convertView.findViewById(id.iv_oneyuan_past_lastwinnerimage3);
			holder.image4 = (MySquareImageView) convertView.findViewById(id.iv_oneyuan_past_lastwinnerimage4);
			holder.image5 = (MySquareImageView) convertView.findViewById(id.iv_oneyuan_past_lastwinnerimage5);
			holder.image6 = (MySquareImageView) convertView.findViewById(id.iv_oneyuan_past_lastwinnerimage6);
			holder.imageList.add(holder.image1);
			holder.imageList.add(holder.image2);
			holder.imageList.add(holder.image3);
			holder.imageList.add(holder.image4);
			holder.imageList.add(holder.image5);
			holder.imageList.add(holder.image6);
			convertView.setTag(id.tag_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(id.tag_holder);
		}

		final ArrayList<String> urlList = new ArrayList<String>();
		for (int i = 0; i < list.get(position).getPictureLibraryList().size(); i++) {
			urlList.add(list.get(position).getPictureLibraryList().get(i).getImgUrl());
		}
		
		holder.mGoto.setTag(id.tag_holder, list.get(position).getProductId());
		holder.mGoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String productId = (String) v.getTag(id.tag_holder);
				Intent intent = new Intent(context, OneyuanProductDetailActivity.class);
				intent.putExtra("productId", productId);
				context.startActivity(intent);
			}
		});

		holder.noStage.setText(list.get(position).getName());
		holder.time.setText(list.get(position).getTime());
		holder.month.setText(list.get(position).getMonth());
		holder.name.setText(list.get(position).getUserName());
		holder.code.setText(list.get(position).getLuckyNumber());
		holder.message.setText(list.get(position).getContents());
		holder.messageAll.setText("获奖感言：" + list.get(position).getContents());
		holder.visible_msg.setVisibility(View.GONE);
		holder.visible_photo_open.setVisibility(View.GONE);
		holder.visible_photo_close.setVisibility(View.GONE);
		for (int i = 0; i < list.get(position).getPictureLibraryList().size(); i++) {
			Picasso.with(context).load(list.get(position).getPictureLibraryList().get(i).getImgUrl()).resize(300, 300)
					.into(holder.imageList.get(i));
			holder.imageList.get(i).setTag(id.tag_num, i);
			holder.imageList.get(i).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(context, SeePhotoActivity.class);
					intent.putStringArrayListExtra("list", urlList);
					int index = (Integer) v.getTag(id.tag_num);
					intent.putExtra("index", index);
					context.startActivity(intent);
				}
			});
			if (i == 5) {
				i = 7;
			}
		}

		holder.photoFirst.setVisibility(View.GONE);
		holder.photoSecond.setVisibility(View.GONE);
		// 如果没有获奖感言，下面布局全部隐藏
		if (holder.message.getText().toString().trim().length() < 1) {
			holder.layoutMessage.setVisibility(View.GONE);
		} else {
			// 有获奖感言
			holder.layoutMessage.setVisibility(View.VISIBLE);
			if (list.get(position).getPictureLibraryList().size() < 1) {// 如果没有图片
				holder.message.setVisibility(View.GONE);
				holder.messageAll.setVisibility(View.VISIBLE);
				holder.messageHalf.setVisibility(View.GONE);
			} else {
				// 如果有图片
				holder.photoFirst.setVisibility(View.VISIBLE);

				if (list.get(position).getPictureLibraryList().size() <= 3) {// 如果图片小于等于3张
					holder.visible_photo_open.setVisibility(View.GONE);
					holder.photoSecond.setVisibility(View.GONE);
				} else {
					// 如果图片大于3张
					holder.visible_photo_open.setVisibility(View.VISIBLE);
				}
			}
		}

		holder.visible_msg.setTag(id.tag_first, holder);
		holder.visible_photo_open.setTag(id.tag_first, holder);
		holder.visible_photo_close.setTag(id.tag_first, holder);

		holder.visible_photo_open.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ViewHolder hold = (ViewHolder) v.getTag(id.tag_first);
				hold.photoSecond.setVisibility(View.VISIBLE);
				hold.visible_photo_close.setVisibility(View.VISIBLE);
				v.setVisibility(View.INVISIBLE);
			}
		});
		
		holder.visible_photo_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ViewHolder hold = (ViewHolder) v.getTag(id.tag_first);
				hold.photoSecond.setVisibility(View.GONE);
				hold.visible_photo_open.setVisibility(View.VISIBLE);
			}
		});

		return convertView;
	}

	class ViewHolder {
		TextView noStage, time, month, name, code, message, messageAll, visible_msg, visible_photo_open,
				visible_photo_close;
		MySquareImageView image1, image2, image3, image4, image5, image6;
		List<MySquareImageView> imageList = new ArrayList<MySquareImageView>();
		RelativeLayout layoutMessage, photoFirst, photoSecond,mGoto;
		LinearLayout messageHalf;
	}

}

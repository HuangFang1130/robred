package com.jiahehongye.robred.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.OneyuanProductParticipationListBean.ParticipationRecordList;
import com.jiahehongye.robred.oneyuan.OneyuanProductDetailActivity;
import com.jiahehongye.robred.view.CircleImageView;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class OneyuanProductParticipationAdapter extends BaseAdapter {

	private Context context;
	private List<ParticipationRecordList> list;

	private List<View> totalList = new ArrayList<View>();
	private boolean visible_flag = true;

	public OneyuanProductParticipationAdapter(Context context, List<ParticipationRecordList> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_oneyuan_product_participation, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.tv_oneyuan_salesdetail_item_name);
			holder.time = (TextView) convertView.findViewById(R.id.tv_oneyuan_salesdetail_item_time);
			holder.code = (TextView) convertView.findViewById(R.id.tv_oneyuan_salesdetail_item_code);
			holder.image = (CircleImageView) convertView.findViewById(R.id.iv_oneyuan_salesdetail_item_image);
			convertView.setTag(R.id.tag_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_holder);
		}

//		Date dt = new Date();
//		SimpleDateFormat matter = new SimpleDateFormat("yyyy-MM-dd");

		holder.name.setText(list.get(position).getNickName());
		holder.time.setText(list.get(position).getCreateDate() + ":"
				+ list.get(position).getMillisecond());
		holder.code.setText(list.get(position).getSn());
		if (list.get(position).getUserPhoto().length() > 0) {
			Picasso.with(context).load(list.get(position).getUserPhoto()).into(holder.image);
		} else {
			Picasso.with(context).load(R.mipmap.head_pic1).into(holder.image);
		}
		return convertView;
	}

	class ViewHolder {
		TextView name, time, code;
		CircleImageView image;
	}

}

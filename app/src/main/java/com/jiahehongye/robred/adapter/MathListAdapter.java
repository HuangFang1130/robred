package com.jiahehongye.robred.adapter;

import java.util.List;

import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.OneyuanMathListBean.ParticipationRecordList;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MathListAdapter extends BaseAdapter {
	
	private Context context;
	private List<ParticipationRecordList> list;

	public MathListAdapter(Context context, List<ParticipationRecordList> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		if(list!=null){
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
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_oneyuan_math_list, null);
			holder = new ViewHolder();
			holder.time = (TextView) convertView.findViewById(R.id.tv_oneyuan_math_item_time);
			holder.user = (TextView) convertView.findViewById(R.id.tv_oneyuan_math_item_user);
			convertView.setTag(R.id.tag_holder,holder);
		}else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_holder);
		}
		
		Spanned text = Html.fromHtml(list.get(position).getDate()+"→"+"<font color=#4ebfa1>" + list.get(position).getDateTime() + "</font>");
		
		holder.time.setText(text);
		holder.user.setText(list.get(position).getUserName());
		
		return convertView;
	}
	
	class ViewHolder {
		TextView time,user;
	}

}

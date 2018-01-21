package com.jiahehongye.robred.adapter;

import java.util.List;

import com.jiahehongye.robred.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NullListAdapter extends BaseAdapter {
	
	private Context context;
	private List<String> list;

	public NullListAdapter(Context context, List<String> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
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
		// TODO Auto-generated method stub
		View  view = LayoutInflater.from(context).inflate(R.layout.item_oneyuan_product_participation_null, null);
		return view;
	}

}

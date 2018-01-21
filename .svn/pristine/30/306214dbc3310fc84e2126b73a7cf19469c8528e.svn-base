package com.jiahehongye.robred.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jiahehongye.robred.R;
import com.jiahehongye.robred.bean.FaSong;
import com.jiahehongye.robred.bean.QiangDao;

import java.util.ArrayList;

/**
 * Created by qianduan on 2016/12/29.
 */
public class SendRedAdapter extends BaseAdapter {
    private ArrayList<QiangDao> list;
    private ArrayList<FaSong> fs;
    private Context context;
    private int stas;

    public SendRedAdapter(Context context, ArrayList<QiangDao> list, ArrayList<FaSong> fs,int status) {
        this.list = list;
        this.context = context;
        this.stas = status;
        this.fs = fs;
    }

    public SendRedAdapter(RadioGroup.OnCheckedChangeListener onCheckedChangeListener, ArrayList<QiangDao> qdAll, ArrayList<FaSong> fsAll, int status) {
        this.stas = status;
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
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_account, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.red_name);
            holder.time = (TextView) convertView.findViewById(R.id.red_time);
            holder.price = (TextView) convertView.findViewById(R.id.red_price);
            holder.sendPrice = (TextView) convertView.findViewById(R.id.red_send_price);
            holder.status = (TextView) convertView.findViewById(R.id.red_status);
            holder.send_layout = (LinearLayout) convertView.findViewById(R.id.send_layout);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (stas==1){
            holder.name.setText(list.get(position).getSendNickName());
            holder.time.setText(list.get(position).getGrabDate());
            holder.price.setText(list.get(position).getTotalMoney());
            holder.send_layout.setVisibility(View.GONE);
            holder.price.setVisibility(View.VISIBLE);
        }else if (stas==2){
            holder.name.setText(fs.get(position).getNickName());
            holder.time.setText(fs.get(position).getCreateDate());
            holder.send_layout.setVisibility(View.VISIBLE);
            holder.price.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder{
        TextView name,time,price,sendPrice,status;
        LinearLayout send_layout;
    }
}

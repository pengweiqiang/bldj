package com.bldj.lexiang.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bldj.lexiang.R;

public class ListviewAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> list;
	private int type;//地址
	private int currentItem = 0;
	public ListviewAdapter(Context context, ArrayList<String> list) {
		this.context = context;
		this.list = list;
	}
	public ListviewAdapter(Context context, ArrayList<String> list,int type) {
		this.context = context;
		this.list = list;
		this.type = type;
	}
	public void setCurrentItem(int currentItem){
		this.currentItem = currentItem;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		LayoutInflater inflater = LayoutInflater.from(context);
		if(type == 0){
			convertView = inflater.inflate(R.layout.text_item, null);
		}else if(type == 1){
			convertView = inflater.inflate(R.layout.location_item, null);
		}
		TextView textView = (TextView) convertView.findViewById(R.id.itemText);
		textView.setText(list.get(position));
//		if(type == 1){
//			if(currentItem == position){
//				convertView.setBackgroundColor(context.getResources().getColor(R.color.app_bg_color));
//			}else{
//				convertView.setBackgroundColor(Color.TRANSPARENT);
//			}
//		}
		return convertView;
	}

}

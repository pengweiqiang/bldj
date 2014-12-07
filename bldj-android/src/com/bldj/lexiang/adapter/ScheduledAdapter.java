package com.bldj.lexiang.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bldj.lexiang.R;
import com.bldj.lexiang.api.vo.Time;

public class ScheduledAdapter extends BaseListAdapter {

	private List<Time> dataList = new ArrayList<Time>();
	private Context context;
	private LayoutInflater mInflater;

	public ScheduledAdapter(Context c, List<Time> dataList) {
		this.context = c;
		this.dataList = dataList;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Time getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Time time = getItem(position);
		convertView = mInflater.inflate(R.layout.address_item, null);
		TextView tv_time = (TextView) convertView.findViewById(R.id.time);
		TextView tv_status = (TextView) convertView
				.findViewById(R.id.tv_status);

		tv_time.setText(time.getTime());
		if (time.getStatus() == 0) {
			tv_status.setText("可预约");
		} else {
			tv_status.setText("被预约");
		}

		return convertView;
	}

}

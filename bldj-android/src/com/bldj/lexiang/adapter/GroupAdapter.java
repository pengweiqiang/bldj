package com.bldj.lexiang.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bldj.lexiang.R;
import com.bldj.lexiang.constant.enums.TitleBarEnum;

/**
 * @author handong E-mail: handong@si-tech.com.cn
 * @version 创建时间：2014-11-27 下午4:52:05 类说明
 */
public class GroupAdapter extends BaseAdapter {

	private Context context;

	private List<TitleBarEnum> list;
	private int type = 0;// 0 更多 1 排序

	public GroupAdapter(Context context, List<TitleBarEnum> list) {
		this.context = context;
		this.list = list;
	}

	public GroupAdapter(Context context, List<TitleBarEnum> list, int type) {

		this.context = context;
		this.list = list;
		this.type = type;

	}

	@Override
	public int getCount() {
		return list.size();
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

		ViewHolder holder;
		if (convertView == null) {
			if (type == 1) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.order_item_view, null);
			} else {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.group_item_view, null);
			}

			holder = new ViewHolder();

			convertView.setTag(holder);

			holder.groupItem = (TextView) convertView
					.findViewById(R.id.groupItem);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.groupItem.setTextColor(Color.WHITE);
		holder.groupItem.setText(list.get(position).getMsg());
		return convertView;
	}

	static class ViewHolder {
		TextView groupItem;
	}
}

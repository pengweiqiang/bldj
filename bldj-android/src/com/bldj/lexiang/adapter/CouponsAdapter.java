package com.bldj.lexiang.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.vo.Coupon;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.universalimageloader.core.ImageLoader;

public class CouponsAdapter extends BaseListAdapter {

	private Context context;
	private List<Coupon> dataList;
	private LayoutInflater mInflater;

	public CouponsAdapter(Context c, List<Coupon> dataList) {
		this.context = c;
		this.dataList = dataList;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		int count = dataList.size();
		return count;
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Coupon coupon = (Coupon) getItem(position);
		if (null == convertView) {
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.coupon_item, null);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.price);
			holder.tv_startTime = (TextView) convertView
					.findViewById(R.id.starttime);
			holder.tv_endTime = (TextView) convertView
					.findViewById(R.id.endtime);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_price.setText(String.valueOf(coupon.getPrice()));
		holder.tv_startTime.setText(coupon.getStarttime());
		holder.tv_endTime.setText(coupon.getEndtime());

		return convertView;
	}

	public final class ViewHolder {
		public TextView tv_price;
		public TextView tv_startTime;
		public TextView tv_endTime;
	}

}

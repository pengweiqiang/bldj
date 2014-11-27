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
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.universalimageloader.core.ImageLoader;

public class MallAdapter extends BaseListAdapter {

	private Context context;
	private List<Product> dataList;
	private LayoutInflater mInflater;

	public MallAdapter(Context c, List<Product> dataList) {
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
		Product product = (Product) getItem(position);
		if (null == convertView) {
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.mall_item, null);
			holder.logo = (ImageView) convertView
					.findViewById(R.id.product_img);
			holder.tv_product_title = (TextView) convertView
					.findViewById(R.id.product_title);
			holder.tv_product_info = (TextView) convertView
					.findViewById(R.id.product_info);
			holder.tv_price_market = (TextView) convertView
					.findViewById(R.id.price_market);
			holder.tv_price_discount = (TextView) convertView.findViewById(R.id.price_discount);
			holder.tv_buy_counts = (TextView) convertView.findViewById(R.id.buy_count);
			holder.tv_level = (TextView) convertView.findViewById(R.id.level);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}


		return convertView;
	}

	public final class ViewHolder {
		public ImageView logo;
		public TextView tv_product_title;
		public TextView tv_product_info;
		public TextView tv_price_market;
		public TextView tv_price_discount;
		public TextView tv_buy_counts;
		public TextView tv_level;
	}

}

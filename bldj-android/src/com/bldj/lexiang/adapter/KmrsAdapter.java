package com.bldj.lexiang.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.universalimageloader.core.ImageLoader;

public class KmrsAdapter extends BaseListAdapter {

	private Context context;
	private List<Seller> dataList;
	private LayoutInflater mInflater;

	public KmrsAdapter(Context c, List<Seller> dataList) {
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
		Seller seller = (Seller) getItem(position);
		if (null == convertView) {
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.kmrs_item, null);
			holder.headImg = (ImageView) convertView
					.findViewById(R.id.headImage);
			holder.tv_username = (TextView) convertView
					.findViewById(R.id.username);
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.address);
			holder.tv_price = (TextView) convertView.findViewById(R.id.price);
			holder.tv_order_count = (TextView) convertView
					.findViewById(R.id.order_count);
			holder.tv_distance = (TextView) convertView
					.findViewById(R.id.distance);
			holder.feedBack = (RatingBar) convertView
					.findViewById(R.id.feedBack);
			holder.tv_age = (TextView)convertView.findViewById(R.id.age);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_username.setText(seller.getUsername());
		holder.tv_address.setText(seller.getAddress());
		holder.tv_price.setText("均价：￥" + String.valueOf(seller.getAvgPrice()));
		ImageLoader.getInstance().displayImage(seller.getHeadurl(),
				holder.headImg,
				MyApplication.getInstance().getOptions(R.drawable.ic_launcher));

		holder.tv_distance.setText(String.valueOf("距您" + seller.getDistance()
				+ "公里"));
		holder.tv_age.setText(String.valueOf(seller.getUserGrade()));

		if (seller.getDealnumSum() < 20) {
			// levelStr = "★";
			holder.feedBack.setNumStars(1);
		} else if (seller.getDealnumSum() >= 20 && seller.getDealnumSum() < 200) {
			holder.feedBack.setNumStars(2);
		} else if (seller.getDealnumSum() >= 200) {
			// levelStr = "★★";
			holder.feedBack.setNumStars(3);
		} else {
			// levelStr = "★★★★";
			holder.feedBack.setNumStars(5);
		}

		String orderCount = "共接单" + seller.getDealnumSum() + "次";
		SpannableStringBuilder style = new SpannableStringBuilder(orderCount);
		style.setSpan(
				new ForegroundColorSpan(context.getResources().getColor(
						R.color.app_title_color)), 3, orderCount.indexOf("次"),
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		holder.tv_order_count.setText(style);

		return convertView;
	}

	public final class ViewHolder {
		public ImageView headImg;
		public RatingBar feedBack;
		public TextView tv_username, tv_distance, tv_address, tv_price,
				tv_order_count;
		public TextView tv_age;
	}

}

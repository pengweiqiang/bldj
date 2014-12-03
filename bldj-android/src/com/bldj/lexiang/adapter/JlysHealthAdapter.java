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

public class JlysHealthAdapter extends BaseListAdapter {

	private Context context;
	private List<Seller> dataList;
	private LayoutInflater mInflater;

	public JlysHealthAdapter(Context c, List<Seller> dataList) {
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

			convertView = mInflater.inflate(R.layout.jlys_health_item, null);
			holder.headImg = (ImageView) convertView
					.findViewById(R.id.headImage);
			holder.tv_username = (TextView) convertView
					.findViewById(R.id.username);
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.address);
			holder.tv_avgprice = (TextView) convertView.findViewById(R.id.avg_price);
			holder.tv_comment = (TextView) convertView.findViewById(R.id.order_count);
			holder.tv_distance = (TextView) convertView.findViewById(R.id.distance);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_username.setText(seller.getNickname());
		holder.tv_address.setText(seller.getAddress());
		holder.tv_avgprice.setText(String.valueOf(seller.getAvgPrice()) + "岁");
		ImageLoader.getInstance().displayImage(
				seller.getHeadurl(),
				holder.headImg,
				MyApplication.getInstance()
						.getOptions(R.drawable.ic_launcher));

		holder.tv_distance.setText(String.valueOf("距您"+seller.getDistance()+"公里"));
		holder.tv_comment.setText(seller.getRecommend());

		return convertView;
	}

	public final class ViewHolder {
		public ImageView headImg;
		public TextView tv_username, tv_distance, tv_address, tv_avgprice,
				tv_comment;
	}

}

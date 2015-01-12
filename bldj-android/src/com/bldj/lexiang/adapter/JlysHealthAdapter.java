package com.bldj.lexiang.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.vo.ConfParams;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.lexiang.ui.SellerPersonalActivity;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.universalimageloader.core.ImageLoader;

public class JlysHealthAdapter extends BaseListAdapter {

	private Context context;
	private List<Seller> dataList;
	private LayoutInflater mInflater;
	private Map<String, Object> params;
	public JlysHealthAdapter(Context c, List<Seller> dataList) {
		this.context = c;
		this.dataList = dataList;
		this.mInflater = LayoutInflater.from(context);
	}
	public JlysHealthAdapter(Context c,List<Seller> dataList,Map<String, Object> params){
		this.context = c;
		this.dataList = dataList;
		this.mInflater = LayoutInflater.from(context);
		this.params = params;
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
		final Seller seller = (Seller) getItem(position);
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
			holder.tv_order_count = (TextView) convertView.findViewById(R.id.order_count);
			holder.tv_distance = (TextView) convertView.findViewById(R.id.distance);
			holder.tv_level = (RatingBar)convertView.findViewById(R.id.level);
			holder.tv_seller_info = (TextView)convertView.findViewById(R.id.seller_info);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_username.setText(seller.getNickname());
		holder.tv_address.setText(seller.getAddress());
		holder.tv_avgprice.setText(String.valueOf(seller.getUserGrade()) + "岁");
		holder.tv_seller_info.setText(StringUtils.isEmpty(seller.getRecommend())?"无":seller.getRecommend());
		ImageLoader.getInstance().displayImage(
				seller.getHeadurl(),
				holder.headImg,
				MyApplication.getInstance()
						.getOptions(R.drawable.default_head_image));

		holder.tv_distance.setText(String.valueOf("距您"+seller.getDistance()+"公里"));
		String orderCount ="共接单"+seller.getDealnumSum()+"次";
		SpannableStringBuilder style=new SpannableStringBuilder(orderCount);
		style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.app_title_color)),3,orderCount.indexOf("次"),Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
		holder.tv_order_count.setText(style);
		
		int level = ConfParams.getStarRuleCount(seller.getDealnumSum(), MyApplication.getInstance().getConfParams().getStarRule());
		holder.tv_level.setNumStars(level);
		holder.tv_level.setRating(level);
		holder.headImg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// 启动美容师个人界面
				Intent intent = new Intent(context,
						SellerPersonalActivity.class);
				if(params != null){
					intent.putExtra("time", (String)params.get("time"));// 预约时间
					intent.putExtra("timeIndex", (Integer)params.get("timeIndex"));// 预约时间位置
					intent.putExtra("product", (Product)params.get("product"));//预约产品
					intent.putExtra("address", (String)params.get("address"));//详细地址
				}
				intent.putExtra("seller", seller);
				context.startActivity(intent);
			}
		});

		return convertView;
	}
	

	public final class ViewHolder {
		public ImageView headImg;
		public TextView tv_username, tv_distance, tv_address, tv_avgprice,
				tv_order_count,tv_seller_info;
		public RatingBar tv_level;
	}

}

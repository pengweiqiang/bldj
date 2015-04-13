package com.bldj.lexiang.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.lexiang.utils.DeviceInfo;
import com.bldj.universalimageloader.core.ImageLoader;

public class HomeNewAdapter extends BaseListAdapter {

	private Context mContext;
	private List<Product> dataList;
	private LayoutInflater mInflater;
	private int type = 0;//1是美容师下面的服务项目，进入三步骤时不需要进入第二步   2.从（我要预约他）按钮进入
	private Seller sellerVo;
	
	public HomeNewAdapter(Context c, List<Product> dataList) {
		this.mContext = c;
		this.dataList = dataList;
		this.mInflater = LayoutInflater.from(mContext);
	}
	public HomeNewAdapter(Context c,List<Product> dataList,int type,Seller sellerVo){
		this.mContext = c;
		this.dataList = dataList;
		this.mInflater = LayoutInflater.from(mContext);
		this.type = type;
		this.sellerVo = sellerVo;
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
		final Product productItem = (Product)getItem(position);
		if (null == convertView) {
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.home_new_item, null);
			holder.bg_trans = (LinearLayout)convertView.findViewById(R.id.bg_trans);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.img = (ImageView) convertView.findViewById(R.id.image);
//			holder.price = (TextView) convertView
//					.findViewById(R.id.product_price);
//			holder.price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
			holder.yixiujia_price = (TextView) convertView.findViewById(R.id.yixiu_price);
			holder.title = (TextView) convertView.findViewById(R.id.title);
//			holder.frameOne = (LinearLayout) convertView.findViewById(R.id.good_cell_one);
			LayoutParams params1 = holder.img.getLayoutParams();
			params1.width = DeviceInfo.getDisplayMetricsWidth((Activity)mContext);
			params1.height = (int) (params1.width * 1.0 / 4 * 2.5);
			holder.img.setLayoutParams(params1);
			holder.bg_trans.setLayoutParams(params1);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(productItem.getName());
		holder.title.setText(productItem.getOneword());
//		holder.price.setText(String.valueOf(productItem.getMarketPrice()) );
		holder.yixiujia_price.setText(String.valueOf(productItem.getCurPrice())+"元");
		ImageLoader.getInstance().displayImage(
				productItem.getPicurl(),
				holder.img,
				MyApplication.getInstance()
						.getOptions(R.drawable.default_image));
//		holder.frameOne.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				Intent intent = new Intent();
//				intent.setClass(mContext, HealthProductDetailActivity.class);
//				intent.putExtra("product", productItem);
//				mContext.startActivity(intent);
//			}
//		});

		return convertView;
	}

	public final class ViewHolder {
		public ImageView img;
		public TextView title;
		public TextView price;
		public TextView yixiujia_price;
		public TextView name;
		public LinearLayout frameOne;
		public LinearLayout bg_trans;
	}

}

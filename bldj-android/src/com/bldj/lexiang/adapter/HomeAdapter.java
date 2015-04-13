package com.bldj.lexiang.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.bldj.lexiang.commons.AppManager;
import com.bldj.lexiang.ui.AppointmentDoor1Activity;
import com.bldj.lexiang.ui.HealthProductDetailActivity;
import com.bldj.lexiang.utils.DeviceInfo;
import com.bldj.universalimageloader.core.ImageLoader;

public class HomeAdapter extends BaseListAdapter {

	private Context mContext;
	private List<Product> dataList;
	private LayoutInflater mInflater;
	private int type = 0;//1是美容师下面的服务项目，进入三步骤时不需要进入第二步   2.从（我要预约他）按钮进入
	private Seller sellerVo;
	
	public HomeAdapter(Context c, List<Product> dataList) {
		this.mContext = c;
		this.dataList = dataList;
		this.mInflater = LayoutInflater.from(mContext);
	}
	public HomeAdapter(Context c,List<Product> dataList,int type,Seller sellerVo){
		this.mContext = c;
		this.dataList = dataList;
		this.mInflater = LayoutInflater.from(mContext);
		this.type = type;
		this.sellerVo = sellerVo;
	}

	@Override
	public int getCount() {
		int count = dataList.size() / 2 + (dataList.size() % 2 == 0 ? 0 : 1);
		return count;
	}

	@Override
	public Object getItem(int position) {
		List<Product> product = new ArrayList<Product>();
		product.add(dataList.get(position + position));
		if (dataList.size() != ((position + 1) * 2) - 1) {
			product.add(dataList.get(position + position + 1));
		}
		return product;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final List<Product> productItem = (List<Product>) getItem(position);
		if (null == convertView) {
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.home_item, null);
			holder.frameOne = (LinearLayout) convertView
					.findViewById(R.id.good_cell_one);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.img = (ImageView) convertView.findViewById(R.id.image);
			holder.price = (TextView) convertView
					.findViewById(R.id.product_price);
			holder.price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
			holder.yixiujia_price = (TextView) convertView.findViewById(R.id.yixiu_price);
//			holder.title = (TextView) convertView.findViewById(R.id.title);

			holder.frameTwo = (LinearLayout) convertView
					.findViewById(R.id.good_cell_two);
			holder.name2 = (TextView) convertView.findViewById(R.id.name2);
			holder.img2 = (ImageView) convertView.findViewById(R.id.image2);
			holder.price2 = (TextView) convertView
					.findViewById(R.id.product_price2);
			holder.price2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
			holder.yixiujia_price2 = (TextView) convertView.findViewById(R.id.yixiu_price2);
			
			LayoutParams params1 = holder.img.getLayoutParams();
			params1.width = ((DeviceInfo.getDisplayMetricsWidth((Activity)mContext)-10)/2);
			params1.height = (int) (params1.width * 1.0 / 4 * 2.5);
			holder.img.setLayoutParams(params1);
			holder.img2.setLayoutParams(params1);
			
//			holder.title2 = (TextView) convertView.findViewById(R.id.title2);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(productItem.get(0).getName());
//		holder.title.setText(productItem.get(0).getOneword());
		holder.price.setText("市场价￥"
				+ String.valueOf(productItem.get(0).getMarketPrice()) );
		holder.yixiujia_price.setText("脉度价￥"
				+ String.valueOf(productItem.get(0).getCurPrice()));
		ImageLoader.getInstance().displayImage(
				productItem.get(0).getPicurl(),
				holder.img,
				MyApplication.getInstance()
						.getOptions(R.drawable.default_image));
		holder.frameOne.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if(type == 1){//服务项目，进入单品页--》预约第一步--》第三部
					intent.setClass(mContext, HealthProductDetailActivity.class);
					intent.putExtra("product", productItem.get(0));
					intent.putExtra("seller", sellerVo);
				}else if(type == 2){//我要预约他，直接进入预约第一步--》第三部。
					intent.setClass(mContext, AppointmentDoor1Activity.class);
					intent.putExtra("product", productItem.get(0));
					intent.putExtra("seller", sellerVo);
					AppManager.getAppManager().finishActivity(mContext.getClass());
				}else if(type == 0){//首页，分类
					intent.setClass(mContext, HealthProductDetailActivity.class);
					intent.putExtra("product", productItem.get(0));
				}
				
				mContext.startActivity(intent);
			}
		});

		if ((position * 2 + 1) < dataList.size()) {
			holder.frameTwo.setVisibility(View.VISIBLE);
			holder.name2.setText(productItem.get(1).getName());
//			holder.title2.setText(productItem.get(1).getOneword());
			holder.price2.setText("市场价￥"+String.valueOf(productItem.get(1)
					.getMarketPrice()));
			
			holder.yixiujia_price2.setText("脉度价￥"
					+ String.valueOf(productItem.get(1).getCurPrice()) );
			ImageLoader.getInstance().displayImage(
					productItem.get(1).getPicurl(),
					holder.img2,
					MyApplication.getInstance().getOptions(
							R.drawable.default_image));

			holder.frameTwo.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					if(type == 1){//理疗师界面下的服务项目
						intent.setClass(mContext, HealthProductDetailActivity.class);
						intent.putExtra("product", productItem.get(1));
						intent.putExtra("seller", sellerVo);
						mContext.startActivity(intent);
					}else if(type == 2){//点击我要预约他
						intent.setClass(mContext, AppointmentDoor1Activity.class);
						intent.putExtra("product", productItem.get(1));
						intent.putExtra("seller", sellerVo);
						AppManager.getAppManager().finishActivity(mContext.getClass());
					}else if(type ==0){//首页，分类
						intent.setClass(mContext, HealthProductDetailActivity.class);
						intent.putExtra("product", productItem.get(1));
					}
					mContext.startActivity(intent);
				}
			});

		} else {
			holder.frameTwo.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	public final class ViewHolder {
		public ImageView img, img2;
//		public TextView title, title2;
		public TextView price, price2;
		public TextView yixiujia_price,yixiujia_price2;
		public TextView name, name2;
		public LinearLayout frameOne, frameTwo;
	}

}

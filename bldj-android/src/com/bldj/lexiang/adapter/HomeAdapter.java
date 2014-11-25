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
import com.bldj.universalimageloader.core.ImageLoader;

public class HomeAdapter extends BaseListAdapter {

	private Context context;
	private List<Product> dataList;
	private LayoutInflater mInflater;

	public HomeAdapter(Context c, List<Product> dataList) {
		this.context = c;
		this.dataList = dataList;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		int count = dataList.size()/2+(dataList.size()%2==0?0:1);
		return count;
	}

	@Override
	public Object getItem(int position) {
		List<Product> product = new ArrayList<Product>();
		product.add(dataList.get(position+position));
		product.add(dataList.get(position+position+1));
		return product;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		List<Product> productItem = (List<Product>)getItem(position);
		if(dataList.size()==((position+1)*2)-1){
			View lastView = mInflater.inflate(R.layout.home_item, null);
			lastView.findViewById(R.id.good_cell_two).setVisibility(View.INVISIBLE);
			TextView name = (TextView)lastView.findViewById(R.id.name);
			ImageView img = (ImageView)lastView.findViewById(R.id.image);
			TextView price = (TextView)lastView.findViewById(R.id.product_price);
			TextView title = (TextView)lastView.findViewById(R.id.title);
			
			name.setText(productItem.get(0).getName());
			ImageLoader.getInstance().displayImage(productItem.get(0).getPicurl(), img);
			price.setText(String.valueOf(productItem.get(0).getCurPrice()));
			title.setText(productItem.get(0).getOneword());
			return lastView;
		}else{
			if(null == convertView){
				holder = new ViewHolder();
				
				convertView = mInflater.inflate(R.layout.home_item, null);
				holder.frameTwo = (FrameLayout)convertView.findViewById(R.id.good_cell_two);
				holder.name = (TextView)convertView.findViewById(R.id.name);
				holder.img = (ImageView)convertView.findViewById(R.id.image);
				holder.price = (TextView)convertView.findViewById(R.id.product_price);
				holder.title = (TextView)convertView.findViewById(R.id.title);
				holder.name2 = (TextView)convertView.findViewById(R.id.name2);
				holder.img2 = (ImageView)convertView.findViewById(R.id.image2);
				holder.price2 = (TextView)convertView.findViewById(R.id.product_price2);
				holder.title2 = (TextView)convertView.findViewById(R.id.title2);
				 convertView.setTag(holder);  
				  
	        } else {
	            holder = (ViewHolder) convertView.getTag();  
	        }  
			
			
			holder.name.setText(productItem.get(0).getName());
			holder.title.setText(productItem.get(0).getOneword());
			holder.price.setText("价格："+String.valueOf(productItem.get(0).getCurPrice())+"元/20分钟");
			ImageLoader.getInstance().displayImage(productItem.get(0).getPicurl(), holder.img,MyApplication.getInstance().getOptions(R.drawable.default_image));
			
			holder.name2.setText(productItem.get(1).getName());
			holder.title2.setText(productItem.get(1).getOneword());
			holder.price2.setText(String.valueOf(productItem.get(1).getCurPrice()));
			ImageLoader.getInstance().displayImage(productItem.get(1).getPicurl(), holder.img2,MyApplication.getInstance().getOptions(R.drawable.default_image));
		
			
			return convertView;
		}
	}

	public final class ViewHolder {
		public ImageView img,img2;
		public TextView title,title2;
		public TextView price,price2;
		public TextView name,name2;
		public FrameLayout frameTwo;
	}

}

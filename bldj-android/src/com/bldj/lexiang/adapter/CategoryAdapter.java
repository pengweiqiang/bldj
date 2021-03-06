package com.bldj.lexiang.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.vo.Category;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.ui.CategoryProductActivity;
import com.bldj.lexiang.ui.HealthProductDetailActivity;
import com.bldj.lexiang.utils.DeviceInfo;
import com.bldj.universalimageloader.core.ImageLoader;

public class CategoryAdapter extends BaseListAdapter {

	private Context context;
	private List<Category> dataList;
	private LayoutInflater mInflater;

	public CategoryAdapter(Context c, List<Category> dataList) {
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
		final Category category = (Category) getItem(position);
		final List<Product> products = category.getProducts();
		if (null == convertView) {
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.category_item, null);
			holder.frame1 = (RelativeLayout)convertView.findViewById(R.id.frame1);
			holder.frame2 = (RelativeLayout)convertView.findViewById(R.id.frame2);
			holder.frame3 = (RelativeLayout)convertView.findViewById(R.id.frame3);
			holder.frame4 = (RelativeLayout)convertView.findViewById(R.id.frame4);
			holder.imageView1 = (ImageView) convertView
					.findViewById(R.id.imageview1);
			holder.imageView2 = (ImageView) convertView
					.findViewById(R.id.imageview2);
			holder.imageView3 = (ImageView) convertView
					.findViewById(R.id.imageview3);
			holder.imageView4 = (ImageView) convertView
					.findViewById(R.id.imageview4);
			LayoutParams layoutParams = holder.imageView1.getLayoutParams();
			layoutParams.width = (DeviceInfo.getDisplayMetricsWidth((Activity)context)-10)/4;
			layoutParams.height = (int) (layoutParams.width * 1.0 / 4 * 2.5);
			holder.imageView1.setLayoutParams(layoutParams);
			holder.imageView2.setLayoutParams(layoutParams);
			holder.imageView3.setLayoutParams(layoutParams);
			holder.imageView4.setLayoutParams(layoutParams);

			holder.tv_product_name1 = (TextView) convertView
					.findViewById(R.id.product_name1);
			holder.tv_product_name2 = (TextView) convertView
					.findViewById(R.id.product_name2);
			holder.tv_product_name3 = (TextView) convertView
					.findViewById(R.id.product_name3);
			holder.tv_product_name4 = (TextView) convertView
					.findViewById(R.id.product_name4);

			holder.tv_category_name = (TextView) convertView
					.findViewById(R.id.category_name);
//			holder.title_circle = (View)convertView.findViewById(R.id.title_circle);
			holder.tv_more = (TextView)convertView.findViewById(R.id.more);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		/*CircleView c = (CircleView)convertView.findViewById(R.id.title_circle2);
		c.invalidate();*/
		holder.tv_category_name.setText(category.getName());
//		int res = context.getResources().getColor(R.color.color_random_one);
//		Random rand = new Random();
//		int i = rand.nextInt(10);
//		if(i==0){
//			holder.title_circle.setBackgroundColor(res);
//		}else if(i==1){
//			holder.title_circle.setBackgroundColor(context.getResources().getColor(R.color.color_random_two));
//		}else if(i==2){
//			holder.title_circle.setBackgroundColor(context.getResources().getColor(R.color.color_random_three));
//		}
		

		if (products != null && products.size() > 0) {
			final Product product1 = products.get(0);
			ImageLoader.getInstance().displayImage(
					product1.getPicurl(),
					holder.imageView1,
					MyApplication.getInstance().getOptions(
							R.drawable.ic_launcher));
			holder.tv_product_name1.setText(product1.getName());

			holder.frame1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(context,
							HealthProductDetailActivity.class);
					intent.putExtra("product", product1);
					context.startActivity(intent);
				}
			});
		}
		if (products != null && products.size() > 1) {
			final Product product2 = products.get(1);
			ImageLoader.getInstance().displayImage(
					product2.getPicurl(),
					holder.imageView2,
					MyApplication.getInstance().getOptions(
							R.drawable.ic_launcher));

			holder.tv_product_name2.setText(product2.getName());

			holder.frame2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(context,
							HealthProductDetailActivity.class);
					intent.putExtra("product", product2);
					context.startActivity(intent);
				}
			});
		}else{
			holder.frame2.setVisibility(View.INVISIBLE);
			holder.frame3.setVisibility(View.INVISIBLE);
			holder.frame4.setVisibility(View.INVISIBLE);
			
		}
		if (products != null && products.size() > 2) {
			final Product product3 = products.get(2);
			ImageLoader.getInstance().displayImage(
					product3.getPicurl(),
					holder.imageView3,
					MyApplication.getInstance().getOptions(
							R.drawable.ic_launcher));
			holder.tv_product_name3.setText(product3.getName());

			holder.frame3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(context,
							HealthProductDetailActivity.class);
					intent.putExtra("product", product3);
					context.startActivity(intent);
				}
			});
		}else{
			holder.frame3.setVisibility(View.INVISIBLE);
			holder.frame4.setVisibility(View.INVISIBLE);
			
		}
		if (products != null && products.size() > 3) {
			final Product product4 = products.get(3);
			ImageLoader.getInstance().displayImage(
					product4.getPicurl(),
					holder.imageView4,
					MyApplication.getInstance().getOptions(
							R.drawable.ic_launcher));
			holder.tv_product_name4.setText(product4.getName());

			holder.frame4.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(context,
							HealthProductDetailActivity.class);
					intent.putExtra("product", product4);
					context.startActivity(intent);
				}
			});
		}else{
			holder.frame4.setVisibility(View.INVISIBLE);
			
		}
		//查看更多
		holder.tv_more.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context,CategoryProductActivity.class);
				intent.putExtra("categoryId", category.getCategoryId());
				intent.putExtra("name", category.getName());
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	public final class ViewHolder {
		public TextView tv_category_name;
//		public View title_circle;
		public RelativeLayout frame1,frame2,frame3,frame4;
		public ImageView imageView1, imageView2, imageView3, imageView4;
		public TextView tv_product_name1, tv_product_name2, tv_product_name3,
				tv_product_name4;
		public TextView tv_more;
	}

}

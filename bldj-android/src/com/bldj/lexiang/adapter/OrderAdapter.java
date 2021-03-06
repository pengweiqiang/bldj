package com.bldj.lexiang.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bldj.lexiang.R;
import com.bldj.lexiang.api.vo.Order;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.constant.enums.OrderStatusEnum;
import com.bldj.lexiang.ui.HealthProductDetailActivity;
import com.bldj.lexiang.ui.MainActivity;
import com.bldj.lexiang.ui.MyOrdersActivity;
import com.bldj.lexiang.ui.OrderEvalActivity;
import com.bldj.lexiang.utils.DateUtil;

public class OrderAdapter extends BaseListAdapter {

	private Context context;
	private List<Order> dataList;
	private LayoutInflater mInflater;
	private int type = 0;  //2代表首页的订单  0 是用户订单   1 美容师订单

	public OrderAdapter(Context c, List<Order> dataList,String mobile) {
		this.context = c;
		this.dataList = dataList;
		this.mInflater = LayoutInflater.from(context);
	}
	public OrderAdapter(Context c, List<Order> dataList,int type) {
		this.type = type;
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
		final Order order = (Order) getItem(position);
		if (null == convertView) {
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.order_item, null);
			holder.tv_order_time = (TextView) convertView
					.findViewById(R.id.order_time);
			holder.tv_order_pay = (TextView) convertView
					.findViewById(R.id.order_pay);
			holder.tv_order_num = (TextView) convertView
					.findViewById(R.id.order_num);
			holder.tv_order_status = (TextView) convertView
					.findViewById(R.id.order_status);
			holder.tv_order_pay = (TextView) convertView
					.findViewById(R.id.order_pay);
			holder.tv_eval = (TextView) convertView
					.findViewById(R.id.order_eval);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_order_time.setText(DateUtil.getDateString(order.getCreatetime(),DateUtil.TRIM_PATTERN,DateUtil.CRITICISM_PATTERN));
		holder.tv_order_pay.setText("￥" + String.valueOf(order.getOrderPay()));
		holder.tv_order_num.setText(order.getProName());
		holder.tv_order_num.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );//底部加横线
		holder.tv_order_status.setText(order.getStatusStr());
		
		holder.tv_order_num.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context,HealthProductDetailActivity.class);
				Product product = new Product();
				product.setId(order.getProductId());
				intent.putExtra("product", product);
				context.startActivity(intent);
			}
		});
		
		switch (order.getStatus()) {
		case OrderStatusEnum.NO_PAID:
			holder.tv_order_status.setTextColor(context.getResources().getColor(R.color.order_un_pay));
			break;
		case OrderStatusEnum.PAID_ONLINE:
			holder.tv_order_status.setTextColor(context.getResources().getColor(R.color.light_green));
			break;
		case OrderStatusEnum.PAID_OFFLINE:
			holder.tv_order_status.setTextColor(context.getResources().getColor(R.color.light_green));
			break;
		case OrderStatusEnum.CANCLED:
			holder.tv_order_status.setTextColor(context.getResources().getColor(R.color.grey));
			break;
		case OrderStatusEnum.SELLER_CONFIRM:
			holder.tv_order_status.setTextColor(context.getResources().getColor(R.color.light_green));
			break;
		case OrderStatusEnum.BUYER_CONFIRM:
			holder.tv_order_status.setTextColor(context.getResources().getColor(R.color.light_green));
			break;
		case OrderStatusEnum.COMPLETE:
			holder.tv_order_status.setTextColor(context.getResources().getColor(R.color.light_green));
			break;

		default:
			break;
		}
		if (type != 1 && (order.getStatus() == OrderStatusEnum.PAID_OFFLINE || order.getStatus() == OrderStatusEnum.PAID_ONLINE)) {//已完成才有评价
			holder.tv_eval.setVisibility(View.VISIBLE);
			holder.tv_eval.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// 启动用户评价界面
					Intent intent = new Intent(context, OrderEvalActivity.class);
					intent.putExtra("order", order);
					if(type == 0){
						((MyOrdersActivity)context).startActivityForResult(intent, 22);
					}else if (type == 2){
						((MainActivity)context).startActivityForResult(intent, 22);
					}
				}
			});
		}else{
			holder.tv_eval.setVisibility(View.GONE);
		}

		return convertView;
	}

	public final class ViewHolder {
		public TextView tv_order_time, tv_order_status, tv_order_num,
				tv_order_pay, tv_eval;
	}

}

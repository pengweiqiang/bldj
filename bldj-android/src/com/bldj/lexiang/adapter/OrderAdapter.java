package com.bldj.lexiang.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bldj.lexiang.R;
import com.bldj.lexiang.api.vo.Order;
import com.bldj.lexiang.ui.OrderEvalActivity;

public class OrderAdapter extends BaseListAdapter {

	private Context context;
	private List<Order> dataList;
	private LayoutInflater mInflater;

	public OrderAdapter(Context c, List<Order> dataList) {
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
		Order order = (Order) getItem(position);
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
			holder.tv_order_pay = (TextView) convertView.findViewById(R.id.order_pay);
			holder.tv_eval = (TextView) convertView.findViewById(R.id.order_eval);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_order_time.setText(order.getCreatetime());
		holder.tv_order_pay.setText(order.getOrderPay());
		holder.tv_order_num.setText(String.valueOf(order.getOrderNum()));
		holder.tv_order_status.setText(order.getStatusStr());
		if(order.getStatus() == 3){
			holder.tv_eval.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					//启动用户评价界面
					Intent intent = new Intent(context,OrderEvalActivity.class);
					context.startActivity(intent);
				}
			});
		}
		
		return convertView;
	}

	public final class ViewHolder {
		public TextView tv_order_time, tv_order_status, tv_order_num, tv_order_pay,
				tv_eval;
	}

}

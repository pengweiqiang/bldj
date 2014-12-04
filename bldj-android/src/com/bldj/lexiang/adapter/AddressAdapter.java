package com.bldj.lexiang.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.api.vo.Address;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.ui.AddressInfoActivity;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.ToastUtils;

public class AddressAdapter extends BaseListAdapter {

	private Context context;
	private List<Address> dataList;
	private LayoutInflater mInflater;
	private Handler handler;
	User user = MyApplication.getInstance().getCurrentUser();

	public AddressAdapter(Context c, List<Address> dataList,Handler handler) {
		this.context = c;
		this.dataList = dataList;
		this.mInflater = LayoutInflater.from(context);
		this.handler = handler;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final Address address = (Address) getItem(position);
		if (null == convertView) {
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.address_item, null);
			holder.tv_username = (TextView) convertView
					.findViewById(R.id.username);
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.address_detail);
			holder.tv_phone = (TextView) convertView.findViewById(R.id.phone);
			holder.tv_update = (TextView) convertView.findViewById(R.id.update);
			holder.tv_delete = (TextView) convertView.findViewById(R.id.delete);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_username.setText(user.getNickname());
		holder.tv_address.setText(address.getDetailAddress());
		holder.tv_phone.setText(user.getMobile());

		// 修改
		holder.tv_update.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, AddressInfoActivity.class);
				intent.putExtra("type", 2);
				intent.putExtra("address", address);
				context.startActivity(intent);
			}
		});
		// 删除
		holder.tv_delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				User user = MyApplication.getInstance().getCurrentUser();
				ApiUserUtils.addressManager(context, 1, user.getUserId(), "", "",
						String.valueOf(address.getId()),
						new HttpConnectionUtil.RequestCallback() {

							@Override
							public void execute(ParseModel parseModel) {
								if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
										.getStatus())) {
									ToastUtils.showToast(context, parseModel.getMsg());
								}else{
									ToastUtils.showToast(context, "删除成功");
									Message msg = new Message();
									msg.what = 1;
									msg.arg1 = position;
									handler.sendMessage(msg);
								}
							}
						});

			}
		});

		return convertView;
	}

	public final class ViewHolder {
		public TextView tv_username, tv_address, tv_phone, tv_update,
				tv_delete;
	}

}

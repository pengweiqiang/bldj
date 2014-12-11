package com.bldj.lexiang.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.vo.PayType;
import com.bldj.universalimageloader.core.ImageLoader;

public class PayTypeAdapter extends BaseListAdapter {

	private Context context;
	private List<PayType> dataList;
	private LayoutInflater mInflater;
	private Handler handler;
	private int checkedIndex = 0 ;

	public PayTypeAdapter(Context c, List<PayType> dataList, Handler handler) {
		this.context = c;
		this.dataList = dataList;
		this.mInflater = LayoutInflater.from(context);
		this.handler = handler;
	}
	
	public int getCheckPosition(){
		return checkedIndex;
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
		PayType payType = (PayType) getItem(position);

		convertView = mInflater.inflate(R.layout.paytype_item, null);
		TextView tv_username = (TextView) convertView
				.findViewById(R.id.pay_name);
		ImageView iv_image = (ImageView) convertView
				.findViewById(R.id.pay_image);
		CheckBox check_pay = (CheckBox) convertView
				.findViewById(R.id.pay_radio);
		if(checkedIndex==position){
			check_pay.setChecked(true);
		}else{
			check_pay.setChecked(false);
		}
		check_pay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					checkedIndex = position;
					notifyDataSetChanged();
				}else{
					checkedIndex = -1;
				}
			}
		});
		tv_username.setText(payType.getDescription());
		ImageLoader.getInstance().displayImage(payType.getIconPic(), iv_image,
				MyApplication.getInstance().getOptions(R.drawable.ic_launcher));

		return convertView;
	}

}

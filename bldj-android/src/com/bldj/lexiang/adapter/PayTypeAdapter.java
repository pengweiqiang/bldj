package com.bldj.lexiang.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.vo.PayType;
import com.bldj.lexiang.constant.api.ReqUrls;
import com.bldj.lexiang.utils.StringUtils;
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

		final ViewHolder holder;
		if (null == convertView) {
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.paytype_item, null);
			holder.tv_username = (TextView)convertView.findViewById(R.id.pay_name);
			holder.iv_image = (ImageView)convertView.findViewById(R.id.pay_image);
			holder.check_pay = (CheckBox)convertView.findViewById(R.id.pay_radio);
			holder.ll_pay = (LinearLayout)convertView.findViewById(R.id.ll_pay);
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		if(checkedIndex==position){
			holder.check_pay.setChecked(true);
		}else{
			holder.check_pay.setChecked(false);
		}
		holder.ll_pay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(holder.check_pay.isChecked()){
					holder.check_pay.setChecked(false);
					checkedIndex = -1;
				}else{
					checkedIndex = position;
					notifyDataSetChanged();
				}
			}
		});
		/*holder.check_pay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					checkedIndex = position;
					notifyDataSetChanged();
				}else{
					checkedIndex = -1;
				}
			}
		});*/
		holder.tv_username.setText(payType.getDescription());
		if(!StringUtils.isEmpty(payType.getIconPic())){
			ImageLoader.getInstance().displayImage(ReqUrls.Connection_Type_Common+ReqUrls.DEFAULT_REQ_HOST_IP+payType.getIconPic(), holder.iv_image,
					MyApplication.getInstance().getOptions(R.drawable.default_icon));
		}

		return convertView;
	}
	
	public final class ViewHolder {
		public TextView tv_username;
		public ImageView iv_image;
		public CheckBox check_pay;
		public LinearLayout ll_pay;
		
	}


}

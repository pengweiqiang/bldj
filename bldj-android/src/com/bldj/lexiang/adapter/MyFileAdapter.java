package com.bldj.lexiang.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.vo.MyFiles;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.ui.HealthProductDetailActivity;
import com.bldj.universalimageloader.core.ImageLoader;

public class MyFileAdapter extends BaseListAdapter {

	private Context mContext;
	private List<MyFiles> dataList;
	private LayoutInflater mInflater;

	public MyFileAdapter(Context c, List<MyFiles> dataList) {
		this.mContext = c;
		this.dataList = dataList;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return dataList.size();
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
		final MyFiles myFile = (MyFiles) getItem(position);
		if (null == convertView) {
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.myfile_item, null);
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
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

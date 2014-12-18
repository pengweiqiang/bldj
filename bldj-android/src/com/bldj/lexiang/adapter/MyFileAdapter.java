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
			holder.tv_year = (TextView)convertView.findViewById(R.id.year);
			holder.tv_year_small = (TextView) convertView.findViewById(R.id.year_item);
			holder.tv_date = (TextView)convertView.findViewById(R.id.date);
			holder.iv_leaf1 = (ImageView)convertView.findViewById(R.id.leaf1);
			holder.iv_leaf2 = (ImageView)convertView.findViewById(R.id.leaf2);
			holder.tv_content = (TextView)convertView.findViewById(R.id.content);
			holder.ll_start = (LinearLayout)convertView.findViewById(R.id.start);
			holder.iv_end = (ImageView)convertView.findViewById(R.id.end);
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String year = "2014";
		String date = "1205";
		if(myFile.getContent().length()>4){
			year = myFile.getDealdate().subSequence(0, 4).toString();
			date = myFile.getDealdate().substring(4);
		}
		holder.tv_year.setText(year);
		holder.tv_year_small.setText(year);
		holder.tv_date.setText(date);
		holder.tv_content.setText(myFile.getContent());
		if(position == 0){
			holder.ll_start.setVisibility(View.VISIBLE);
			holder.iv_end.setVisibility(View.GONE);
		}else if(position<dataList.size()-1){
			holder.ll_start.setVisibility(View.GONE);
			holder.iv_end.setVisibility(View.GONE);
		}else{
			holder.ll_start.setVisibility(View.GONE);
			holder.iv_end.setVisibility(View.VISIBLE);
		}
		
		if(position%2==0){
			holder.iv_leaf1.setVisibility(View.VISIBLE);
			holder.iv_leaf2.setVisibility(View.GONE);
		}else{
			holder.iv_leaf1.setVisibility(View.GONE);
			holder.iv_leaf2.setVisibility(View.VISIBLE);
		}
		
		if(position%5==0){
			holder.tv_content.setBackground(mContext.getResources().getDrawable(R.drawable.myfile_bg1));
		}else if(position%5==1){
			holder.tv_content.setBackground(mContext.getResources().getDrawable(R.drawable.myfile_bg2));
		}else if(position%5==2){
			holder.tv_content.setBackground(mContext.getResources().getDrawable(R.drawable.myfile_bg3));
		}else if(position%5==3){
			holder.tv_content.setBackground(mContext.getResources().getDrawable(R.drawable.myfile_bg4));
		}else {
			holder.tv_content.setBackground(mContext.getResources().getDrawable(R.drawable.myfile_bg5));
		}
		
		

		return convertView;
	}

	public final class ViewHolder {
		public TextView tv_year;
		public TextView tv_date;
		public TextView tv_year_small;
		public ImageView iv_leaf1;
		public ImageView iv_leaf2;
		public TextView tv_content;
		public LinearLayout ll_start;
		public ImageView iv_end;
	}

}

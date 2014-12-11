package com.bldj.lexiang.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bldj.lexiang.R;
import com.bldj.lexiang.api.vo.SellerScores;

public class ScoreAdapter extends BaseListAdapter {

	private Context context;
	private List<SellerScores> dataList;
	private LayoutInflater mInflater;

	public ScoreAdapter(Context c, List<SellerScores> dataList) {
		this.context = c;
		this.dataList = dataList;
		this.mInflater = LayoutInflater.from(context);
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final SellerScores score = (SellerScores) getItem(position);
		if (null == convertView) {
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.score_item, null);
			holder.tv_username = (TextView) convertView
					.findViewById(R.id.username);
			holder.tv_time = (TextView) convertView
					.findViewById(R.id.time);
			holder.tv_content = (TextView) convertView.findViewById(R.id.content);
			holder.head_img = (ImageView) convertView.findViewById(R.id.head_img);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_username.setText(score.getName());
		holder.tv_time.setText(score.getCreatetime());
		holder.tv_content.setText(score.getContent());


		return convertView;
	}

	public final class ViewHolder {
		public TextView tv_username, tv_time, tv_content;
		public ImageView head_img;
	}

}

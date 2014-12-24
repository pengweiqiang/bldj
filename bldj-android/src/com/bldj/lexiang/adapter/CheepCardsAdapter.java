package com.bldj.lexiang.adapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bldj.lexiang.R;
import com.bldj.lexiang.api.vo.CheepCards;

public class CheepCardsAdapter extends BaseListAdapter {

	private Context context;
	private List<CheepCards> dataList;
	private LayoutInflater mInflater;

	public CheepCardsAdapter(Context c, List<CheepCards> dataList) {
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
		CheepCards cheepCard = (CheepCards) getItem(position);
		if (null == convertView) {
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.cheep_card_item, null);
			holder.rl_backgroud = (RelativeLayout) convertView.findViewById(R.id.cheep_package);
			holder.line_view = convertView.findViewById(R.id.line_view);
			holder.tv_package1 = (TextView)convertView.findViewById(R.id.package_info1);
			holder.tv_package2 = (TextView)convertView.findViewById(R.id.package_info2);
			holder.tv_package3 = (TextView)convertView.findViewById(R.id.package_info3);
			holder.tv_name = (TextView)convertView.findViewById(R.id.name);
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		String []colors = cheepCard.getColorVal().split(";");
		String []items = cheepCard.getItems().split(";");
		for (int i = 0; i < items.length; i++) {
			if(i==0){
				String item1 = "1、"+items[0];
				holder.tv_package1.setText(item1);
//				holder.tv_package1.setTextColor(Color.parseColor("#"+colors[1]));
				setIndexTextColor(holder.tv_package1,"#"+colors[1]);
			}else if(i == 1){
				String item2 = "2、"+items[1];
				holder.tv_package2.setText(item2);
//				holder.tv_package2.setTextColor(Color.parseColor());
				setIndexTextColor(holder.tv_package2,"#"+colors[1]);
			}else if(i==2){
				String item3 = "3、"+items[2];
				holder.tv_package3.setText(item3);
//				holder.tv_package3.setTextColor(Color.parseColor("#"+colors[1]));
				setIndexTextColor(holder.tv_package3,"#"+colors[1]);
			}
		}
		holder.rl_backgroud.setBackgroundColor(Color.parseColor("#"+colors[0]));
		holder.line_view.setBackgroundColor(Color.parseColor("#"+colors[1]));
		holder.tv_name.setText(cheepCard.getName());
		
		

		return convertView;
	}

	public final class ViewHolder {
		public RelativeLayout rl_backgroud;
		public TextView tv_name;
		public TextView tv_package1;
		public TextView tv_package2;
		public TextView tv_package3;
		public View line_view;
	}
	
	private void setIndexTextColor(TextView tv,String colorstr){
		String str = tv.getText().toString();
		Pattern p = Pattern.compile("[0-9]");
		Matcher m = p.matcher(str.substring(1));
		SpannableStringBuilder style = new SpannableStringBuilder(
				str);
		while(m.find()){
			int index = m.start()+1;
			style.setSpan(
					new ForegroundColorSpan(Color.parseColor(colorstr)), index, index+1,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		tv.setText(style);
	}

}

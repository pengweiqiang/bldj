package com.bldj.lexiang.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bldj.lexiang.R;

@SuppressLint("ResourceAsColor")
public class CustomListEmptyView extends RelativeLayout {
	
	private Context mContext;
	private TextView emptytipTv;
	//private Button emptytipBt;
	//private EmptyViewButtonClickListener mListener;
	
	public CustomListEmptyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		LinearLayout rootView = new LinearLayout(context);
		rootView.setBackgroundColor(com.bldj.lexiang.utils.R.color.default_bg);
		FrameLayout.LayoutParams rootViewParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		rootView.setLayoutParams(rootViewParams);
		rootView.setOrientation(LinearLayout.VERTICAL);
		rootView.setGravity(Gravity.CENTER);
		
		emptytipTv = new TextView(context);
		LinearLayout.LayoutParams tipTVParams = new LinearLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);		
		emptytipTv.setLayoutParams(tipTVParams);
		emptytipTv.setText(R.string.APP_PAGE_EMPTY);
		emptytipTv.setTextColor(com.bldj.lexiang.utils.R.color.TEXT_GRAY);
		emptytipTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		emptytipTv.setGravity(Gravity.CENTER);
		emptytipTv.setPadding(30, 0, 30, 0);
		
		
		rootView.addView(emptytipTv);
		addView(rootView);
		//addListener();
	}
	
	
//	
//	private void addListener() {
//		emptytipBt.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if (mListener != null) {
//					mListener.ClickExe();
//				}
//			}
//		});
//	}
	
	public void setEmptyViewTip(String tip) {
		emptytipTv.setText(tip);
	}
	
	public void setEmptyViewTip(int resid) {
		emptytipTv.setText(mContext.getResources().getString(resid));
	}
	
//	public void showButton(String buttontext, EmptyViewButtonClickListener listener) {
//		emptytipBt.setText(buttontext);
//		mListener = listener;
//		emptytipBt.setVisibility(View.VISIBLE);
//	}
//	
//	public void showButton(int resid, EmptyViewButtonClickListener listener) {
//		emptytipBt.setText(mContext.getResources().getString(resid));
//		mListener = listener;
//		emptytipBt.setVisibility(View.VISIBLE);
//	}
//	
//	public interface EmptyViewButtonClickListener {
//		public void ClickExe();
//	}
}

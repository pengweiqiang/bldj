package com.bldj.lexiang.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bldj.lexiang.R;

/**
 * loading page
 */
public class LoadingView extends LinearLayout {
	//private TextView tipTV;
	//private String[] tipArrays;

	public LoadingView(Context context) {
		this(context,null);
	}

	Context context;

	@SuppressLint("ResourceAsColor")
    public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LinearLayout rootView = new LinearLayout(context);
		rootView.setBackgroundColor(Color.WHITE);
		FrameLayout.LayoutParams rootViewParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		rootView.setLayoutParams(rootViewParams);
		rootView.setOrientation(LinearLayout.VERTICAL);
		rootView.setGravity(Gravity.CENTER);
		
//		TextView tipTV = new TextView(context);
//		LinearLayout.LayoutParams tipTVParams = new LinearLayout.LayoutParams(
//				RelativeLayout.LayoutParams.WRAP_CONTENT,
//				RelativeLayout.LayoutParams.WRAP_CONTENT);
//		tipTV.setLayoutParams(tipTVParams);
//		tipTV.setText(R.string.APP_LOADING_TIP);
//		tipTV.setTextColor(R.color.TEXT_GRAY);
//		tipTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//		tipTV.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
//		tipTV.setPadding(30, 0, 30, 0);


        TextView tipTV = new TextView(context);
        LinearLayout.LayoutParams tipTVParams = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tipTVParams.weight = 1.0f;
        tipTV.setLayoutParams(tipTVParams);
        //tipTV.setText(R.string.APP_LOADING_FAILED);
        //tipTV.setTextColor(R.color.TEXT_GRAY);
        //tipTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tipTV.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
        tipTV.setPadding(30, 0, 30, 0);

        TextView tipTV2 = new TextView(context);
        LinearLayout.LayoutParams tipTVParams2 = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tipTVParams2.weight = 2.0f;
        tipTV2.setLayoutParams(tipTVParams2);
        tipTV2.setText(R.string.APP_LOADING_TIP);
        tipTV2.setTextColor(com.bldj.lexiang.utils.R.color.TEXT_GRAY);
        tipTV2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tipTV2.setGravity(Gravity.CENTER_HORIZONTAL);
        tipTV2.setPadding(30, 0, 30, 0);

        rootView.addView(tipTV);
        rootView.addView(tipTV2);

		addView(rootView);
		//setTips();
		// shadow.setImageDrawable(shadowResId);
		// shadow.setBackground(shadowResId);
	}

	

	public void show() {
		this.setVisibility(View.VISIBLE);
	}

	public void hide() {
		this.setVisibility(View.GONE);
	}
}

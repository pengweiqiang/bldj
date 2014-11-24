package com.bldj.lexiang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.bldj.lexiang.R;

/**
 * 标题栏, 可设置标题和左右图标
 * 
 * @author Will
 *
 */
public class ActionBar extends FrameLayout {

	private TextView mTitleView;
	private ImageView mLeftActionButton;
	private ImageView mRightIconActionButton;
	private Button mRightTextActionButton;
	private LinearLayout mCityButton;

	public ActionBar(Context context) {
		super(context);
	}

	public ActionBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.action_bar_layout,
				this);
		mTitleView = (TextView) findViewById(R.id.actionBarTitle);
		mCityButton = (LinearLayout)findViewById(R.id.leftActionCity);
		mLeftActionButton = (ImageView) findViewById(R.id.leftActionButton);
		mRightIconActionButton = (ImageView) findViewById(R.id.rightIconActionButton);
		mRightTextActionButton = (Button) findViewById(R.id.rightTextActionButton);
	}

	public void setTitle(int resId) {
		mTitleView.setText(resId);
	}

	public void setTitle(String text) {
		mTitleView.setText(text);
	}

	public void setLeftActionButton(int icon, OnClickListener listener) {
		mCityButton.setVisibility(View.GONE);
		mLeftActionButton.setImageResource(icon);
		mLeftActionButton.setOnClickListener(listener);
		mLeftActionButton.setVisibility(View.VISIBLE);
	}
	/**
	 * 设置首页城市的下拉框
	 * @param listener
	 */
	public void setLeftHomeCityActionButton(OnClickListener listener){
		mLeftActionButton.setVisibility(View.INVISIBLE);
		mCityButton.setOnClickListener(listener);
		mCityButton.setVisibility(View.VISIBLE);
	}

	public void hideLeftActionButton() {
		mCityButton.setVisibility(View.INVISIBLE);
		mLeftActionButton.setVisibility(View.INVISIBLE);
	}

	public void hideRightActionButton() {
		mRightIconActionButton.setVisibility(View.INVISIBLE);
		mRightTextActionButton.setVisibility(View.INVISIBLE);
	}

	public void setRightIconActionButton(int resId, OnClickListener listener) {
		mRightIconActionButton.setImageResource(resId);
		mRightIconActionButton.setOnClickListener(listener);
		mRightIconActionButton.setVisibility(View.VISIBLE);
		mRightTextActionButton.setVisibility(View.INVISIBLE);
	}

	public void setRightTextActionButton(String text, OnClickListener listener) {
		mRightTextActionButton.setText(text);
		mRightTextActionButton.setOnClickListener(listener);
		mRightTextActionButton.setVisibility(View.VISIBLE);
		mRightIconActionButton.setVisibility(View.INVISIBLE);
	}
}

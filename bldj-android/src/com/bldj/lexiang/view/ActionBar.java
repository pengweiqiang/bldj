package com.bldj.lexiang.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bldj.lexiang.R;
import com.bldj.lexiang.utils.StringUtils;

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
	private View mRightTextTitleView;
	private TextView mRightTextActionTextView;
	private LinearLayout mCityButton;
	private TextView mCityView;
	private LinearLayout mRightHomeActionLinearLayout; 

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
		mCityView = (TextView)findViewById(R.id.tv_city);
		mCityButton = (LinearLayout)findViewById(R.id.leftActionCity);
		mLeftActionButton = (ImageView) findViewById(R.id.leftActionButton);
		mRightIconActionButton = (ImageView) findViewById(R.id.rightIconActionButton);
		mRightTextTitleView = findViewById(R.id.ll_right_title);
		mRightTextActionTextView = (TextView) findViewById(R.id.rightTextActionTextView);
		mRightHomeActionLinearLayout = (LinearLayout)findViewById(R.id.home_right);
	}

	public void setTitle(int resId) {
		mTitleView.setText(resId);
	}

	public void setTitle(String text) {
		mTitleView.setText(text);
	}
	
	public void setTitleTextColor(int resId){
		mTitleView.setTextColor(getResources().getColor(resId));
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
	public void setCityName(String city){
		if(StringUtils.isEmpty(city)||"null".equalsIgnoreCase(city)){
			city = "城市";
		}
		mCityView.setText(city);
	}

	public void hideLeftActionButton() {
		mCityButton.setVisibility(View.INVISIBLE);
		mLeftActionButton.setVisibility(View.INVISIBLE);
	}

	public void hideRightActionButton() {
		mRightIconActionButton.setVisibility(View.INVISIBLE);
//		mRightTextActionTextView.setVisibility(View.INVISIBLE);
		mRightTextTitleView.setVisibility(View.INVISIBLE);
		mRightHomeActionLinearLayout.setVisibility(View.INVISIBLE);
	}

	public void setRightIconActionButton(int resId, OnClickListener listener) {
		mRightIconActionButton.setImageResource(resId);
		mRightIconActionButton.setOnClickListener(listener);
		mRightIconActionButton.setVisibility(View.VISIBLE);
//		mRightTextActionTextView.setVisibility(View.INVISIBLE);
		mRightTextTitleView.setVisibility(View.INVISIBLE);
		mRightHomeActionLinearLayout.setVisibility(View.INVISIBLE);
	}

	public void setRightTextActionButton(String text, OnClickListener listener) {
		setRightTextActionButton(text, 0, listener);
	}
	public void setRightTextActionButton(String text,int resId,OnClickListener listener){
		mRightTextActionTextView.setText(text);
		if(resId!=0){
			Drawable drawable= getResources().getDrawable(resId);
			/// 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			mRightTextActionTextView.setCompoundDrawables(drawable,null,null,null);
		}
		mRightTextActionTextView.setOnClickListener(listener);
		mRightTextTitleView.setVisibility(View.VISIBLE);
//		mRightTextActionTextView.setVisibility(View.VISIBLE);
		mRightIconActionButton.setVisibility(View.INVISIBLE);
		mRightHomeActionLinearLayout.setVisibility(View.INVISIBLE);
	}
	public void setHomeRightAction(OnClickListener callListener,OnClickListener morelistener){
//		mRightTextActionTextView.setVisibility(View.INVISIBLE);
		mRightTextTitleView.setVisibility(View.INVISIBLE);
		mRightIconActionButton.setVisibility(View.INVISIBLE);
		findViewById(R.id.rightIconActionMore).setOnClickListener(morelistener);
		findViewById(R.id.rightIconActionCall).setOnClickListener(callListener);
		
	}
}

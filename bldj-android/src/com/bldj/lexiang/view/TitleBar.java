package com.bldj.lexiang.view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bldj.lexiang.R;
import com.bldj.lexiang.utils.CommonHelper;

/**
 * 直接在xml中就可以直接指定值？ 通过attr来中转 xml -> attr 在这里java通过从attr里面获取值，在设置到
 * 通过LayoutInflater 显示的View里面
 */
public class TitleBar extends LinearLayout implements View.OnClickListener {
	private TextView leftTitle, centerTitle;
	private ImageButton back,right;
	private RelativeLayout leftBtnView,rightBtnView;
	TitleBarOnClickListener mLeftListener, mRightListener;

	public TitleBar(Context context) {
		super(context);
		this.context = context;
		initView(context);
	}

	Context context;

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView(context);
		// shadow.setImageDrawable(shadowResId);
		// shadow.setBackground(shadowResId);
	}

	@SuppressLint("ResourceAsColor")
    private void initView(Context context){
        int dp15 = CommonHelper.getDp2px(context, 15);
       // int dp7 = CommonHelper.getDp2px(context, 7);
       // int dp72 = CommonHelper.getDp2px(context, 72);
        int TITLEBAR_HEIGHT = CommonHelper.getDp2px(context,com.bldj.lexiang.utils.R.integer.TITLEBAR_HEIGHT);
        int titlebar_icon_width = CommonHelper.getDp2px(context, com.bldj.lexiang.utils.R.integer.titlebar_icon_width);
		RelativeLayout viewRoot = new RelativeLayout(context);
		FrameLayout.LayoutParams rootViewParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT, TITLEBAR_HEIGHT );
		viewRoot.setLayoutParams(rootViewParams);
		viewRoot.setGravity(Gravity.CENTER_VERTICAL);
		viewRoot.setBackgroundColor(com.bldj.lexiang.utils.R.color.bg_titlebar);

		leftBtnView = new RelativeLayout(context);
		RelativeLayout.LayoutParams leftBtnParams = new RelativeLayout.LayoutParams(
                TITLEBAR_HEIGHT, TITLEBAR_HEIGHT);
		leftBtnParams.addRule(RelativeLayout.CENTER_VERTICAL);
		leftBtnParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		//leftBtnParams.leftMargin = dp7;
		leftBtnView.setLayoutParams(leftBtnParams);
		//leftBtnView.setBackgroundDrawable(CommonHelper.getNinePatchStateDrawable(
		//		context, R.drawable.bg_titlebar_btn_normal,
		//		R.drawable.bg_titlebar_btn_pressed));
//		leftBtnView.setBackgroundResource(R.drawable.bg_btn_selector);
		leftBtnView.setClickable(true);
		leftBtnView.setId(0x100001);
		leftBtnView.setVisibility(View.GONE);
		leftBtnView.setOnClickListener(this);

		back = new ImageButton(context);
		RelativeLayout.LayoutParams backParams = new RelativeLayout.LayoutParams(
                titlebar_icon_width,
                titlebar_icon_width);
		backParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		back.setLayoutParams(backParams);
        back.setBackgroundColor(Color.TRANSPARENT);
        back.setClickable(false);
		leftBtnView.addView(back);

		rightBtnView = new RelativeLayout(context);
		RelativeLayout.LayoutParams rightBtnParams = new RelativeLayout.LayoutParams(
                TITLEBAR_HEIGHT, TITLEBAR_HEIGHT);
		rightBtnParams.addRule(RelativeLayout.CENTER_VERTICAL);
		rightBtnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		//rightBtnParams.rightMargin = dp7;
		
		rightBtnView.setLayoutParams(rightBtnParams);
//		rightBtnView.setBackgroundResource(R.drawable.bg_btn_selector);
		//rightBtnView.setBackgroundDrawable(CommonHelper.getStateDrawable(
		//		context, R.drawable.bg_titlebar_btn_normal,
		//		R.drawable.bg_titlebar_btn_pressed));
		rightBtnView.setClickable(true);
		rightBtnView.setId(0x100002);
		rightBtnView.setVisibility(View.GONE);
		rightBtnView.setOnClickListener(this);

		right = new ImageButton(context);
		RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(
                titlebar_icon_width,
                titlebar_icon_width);
		rightParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        right.setLayoutParams(rightParams);
        right.setBackgroundColor(Color.TRANSPARENT);
        right.setClickable(false);
		rightBtnView.addView(right);

		leftTitle = new TextView(context);
		RelativeLayout.LayoutParams leftTitleParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		leftTitleParams.leftMargin = dp15;
		leftTitleParams.addRule(RelativeLayout.CENTER_VERTICAL);
		leftTitleParams.addRule(RelativeLayout.RIGHT_OF,0x100001);
		leftTitle.setLayoutParams(leftTitleParams);
		leftTitle.setVisibility(View.GONE);
		leftTitle.setTextColor(Color.WHITE);
		leftTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
		
		
		centerTitle = new TextView(context);
		RelativeLayout.LayoutParams centerTitleParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		centerTitleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		centerTitle.setLayoutParams(centerTitleParams);
		centerTitle.setTextColor(Color.WHITE);
		centerTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		// ImageView shadow = (ImageView)
		
		viewRoot.addView(leftBtnView);
		viewRoot.addView(rightBtnView);
		viewRoot.addView(leftTitle);
		viewRoot.addView(centerTitle);
		
		addView(viewRoot);
	}
	
	@Override
	public void onClick(View v) {
		final Activity activity = (Activity) context;
		switch (v.getId()) {
		case 0x100001:
			if (null != mLeftListener) {
				mLeftListener.onFinish();
			}
			activity.finish();
			break;
		case 0x100002:
			if (null != mRightListener) {
				mRightListener.onFinish();
			}
			break;
		}

	}

	public void setTitleBarOnLeftClickListener(TitleBarOnClickListener callback) {
		this.mLeftListener = callback;
	}

	public void setTitleBarOnRightClickListener(TitleBarOnClickListener callback) {
		this.mRightListener = callback;
	}

	public void setTitleCenter() {
		centerTitle.setVisibility(View.VISIBLE);
		centerTitle.setText(leftTitle.getText());
		leftTitle.setVisibility(View.GONE);
	}

	public void setLeftDrawable(Drawable drawable){
		back.setImageDrawable(drawable);
		leftBtnView.setVisibility(View.VISIBLE);
	}
	
	public void setRightDrawable(Drawable drawable){
		right.setImageDrawable(drawable);
		rightBtnView.setVisibility(View.VISIBLE);
	}
	
	public interface TitleBarOnClickListener {
		public void onFinish();
	}

	public void setCenterTitle(String title) {
		centerTitle.setText(title);
	}

	public void setLeftTitle(String title) {
		centerTitle.setText(title);
	}
}

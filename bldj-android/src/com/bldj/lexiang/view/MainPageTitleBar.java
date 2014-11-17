package com.bldj.lexiang.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bldj.lexiang.utils.CommonHelper;

/**
 * 直接在xml中就可以直接指定值？ 通过attr来中转 xml -> attr 在这里java通过从attr里面获取值，在设置到
 * 通过LayoutInflater 显示的View里面
 */
@SuppressLint("ResourceAsColor")
public class MainPageTitleBar extends LinearLayout {
	private TextView titleTV;
	private Context mContext;
	private TextView shopNum;
	private RelativeLayout mSettingRL;
	private PopupWindow ppSetting;

	public MainPageTitleBar(Context context) {
		super(context);
		this.mContext = context;
		initView();
	}

	public MainPageTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initView();
	}

	@SuppressLint("ResourceAsColor")
	private void initView() {
		
	}
	public void setCenterTitle(String title) {
		titleTV.setText(title);
	}

	public  void onSettingBtnClick() {
		if(null == ppSetting){
			int dp6 = CommonHelper.getDp2px(mContext, 6);
			int dp200 = CommonHelper.getDp2px(mContext, 200);
			int dp260 = CommonHelper.getDp2px(mContext, 312);
			View view = buildPopWindowView();						
			ppSetting = new PopupWindow(view, dp200,
						dp260, true);				
			ppSetting.setBackgroundDrawable(new ColorDrawable(0x00000000));
			ppSetting.showAsDropDown(mSettingRL, 0, 0);		
		}else{
			if(ppSetting.isShowing()){
				ppSetting.dismiss();
			}else{
				ppSetting.showAsDropDown(mSettingRL, 0, 0);	
			}
		}

	}
	
	private LinearLayout buildPopWindowView(){
		return null;
	}
}

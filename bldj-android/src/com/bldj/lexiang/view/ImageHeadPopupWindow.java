package com.bldj.lexiang.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;

import com.bldj.lexiang.R;

public class ImageHeadPopupWindow extends PopupWindow {

	Button btn_photo;
	Button btn_camera;
	
	public ImageHeadPopupWindow(Context mContext, View parent) {
		super(mContext);
		View view = View
				.inflate(mContext, R.layout.item_popupwindows, null);
		view.startAnimation(AnimationUtils.loadAnimation(mContext,
				R.anim.activity_bottom_in));
		/*LinearLayout ll_popup = (LinearLayout) view
				.findViewById(R.id.ll_popup);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
				R.anim.activity_top_in));*/

		setWidth(LayoutParams.FILL_PARENT);
		setHeight(LayoutParams.FILL_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		update();

		btn_photo = (Button) view
				.findViewById(R.id.item_popupwindows_camera);
		btn_camera = (Button) view
				.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view
				.findViewById(R.id.item_popupwindows_cancel);
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});

	}
}

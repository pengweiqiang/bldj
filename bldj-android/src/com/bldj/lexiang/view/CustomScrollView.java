package com.bldj.lexiang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * 解决ViewPager在ScrollView中滑动不顺畅的问题
 * 
 * @author Administrator
 * 
 */
public class CustomScrollView extends ScrollView {
	private boolean canScroll;
	private GestureDetector mGestureDetector;
	View.OnTouchListener mGestureListener;

	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
//		mGestureDetector = new GestureDetector(new YScrollDetector());
		canScroll = true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_UP)
			canScroll = true;
		try {
			return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	class YScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			if (canScroll)
				if (Math.abs(distanceY) >= Math.abs(distanceX))
					canScroll = true;
				else
					canScroll = false;
			return canScroll;
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(mGestureDetector!=null){
			mGestureDetector.onTouchEvent( ev );
		}
		return super.onTouchEvent(ev);
	}
	
	
	
	public void setGestureDetector(GestureDetector mGestureDetector){
		this.mGestureDetector = mGestureDetector;
	}
}

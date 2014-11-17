package com.bldj.lexiang.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {
	
	private static GestureDetector mDetector = null;
	//private Context mContext = null;

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		//this.mContext = context;
	}

	public CustomViewPager(Context context) {
		super(context);
		//this.mContext = context;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(mDetector!=null){
			mDetector.onTouchEvent( ev );
		}
		return super.onTouchEvent(ev);
	}

	public static GestureDetector getGestureDetector() {
		return mDetector;
	}

	public void setGestureDetector(GestureDetector gestureDetector) {
		mDetector = gestureDetector;
	}

}

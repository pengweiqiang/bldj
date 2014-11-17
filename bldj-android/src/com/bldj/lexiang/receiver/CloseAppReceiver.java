package com.bldj.lexiang.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 关闭APP广播
 */
public class CloseAppReceiver extends BroadcastReceiver {

	private Activity mActivity;

	/**
	 * 
	 * @param activity
	 */
	public CloseAppReceiver(Activity activity) {
		this.mActivity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (mActivity != null) {
			mActivity.finish();
		}
	}
}
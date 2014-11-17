package com.bldj.lexiang.receiver;

import com.bldj.lexiang.GlobalConfig;
import com.bldj.lexiang.utils.WakeLockUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager.WakeLock;

public class InstallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
			String str = intent.getDataString();
			String addedPackageName = null;
			if (str.contains(":")) {
				String[] strs = str.split(":");
				if (strs.length > 1) {
					addedPackageName = strs[1];
				}
			}

			final String name = addedPackageName;
			if (null == name) {
				// do nothing
			} else {
				final WakeLock wl = WakeLockUtils
						.createPartialWakeLock(context);
			}

		} else if (intent.getAction().equals(
				"android.intent.action.PACKAGE_REMOVED")) {
			String str = intent.getDataString();
			String removedPackageName = null;
			if (str.contains(":")) {
				String[] strs = str.split(":");
				if (strs.length > 1) {
					removedPackageName = strs[1];
				}
			}

			final String name = removedPackageName;

			if (null == name) {
				// do nothing
			} else {
			}

		}
	}

}

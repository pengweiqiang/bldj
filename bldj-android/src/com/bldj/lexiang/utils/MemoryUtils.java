package com.bldj.lexiang.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

/**
 * 内存清理
 */
public class MemoryUtils {
	public static void killAll(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		if (processInfos.size() != 0 && processInfos != null) {
			for (RunningAppProcessInfo processInfo : processInfos) {
				String packageName = processInfo.processName;
				if (!"com.sitech.store".equals(packageName)) {
					am.killBackgroundProcesses(packageName);
				}
				
			}
		}
	}
}

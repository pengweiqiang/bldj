package com.bldj.lexiang.utils;

import android.util.Log;

public class Logger {
	
	private static int LOG = 6;
	private static int E = 5;
	
	/**
	 * 控制打印开关
	 */
	private static boolean isDebug = true;
	
	public static void e(String log, String msg) {
		if (LOG > E && isDebug)
			Log.e(log, msg);
	}
	
	public static void e(String log, String msg, Throwable tr) {
		if (LOG > E && isDebug)
			Log.e(log, msg, tr);
	}
	
	public static void debugPrint(String content) {
		if (isDebug) {
			Log.d("debug", content);
		}
	}
	
	public static void debugPrint(String tag, String content) {
		if (isDebug) {
			Log.d(tag, content);
		}
	}
}

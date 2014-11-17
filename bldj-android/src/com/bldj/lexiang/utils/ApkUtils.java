package com.bldj.lexiang.utils;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

/**
 * apk包相关的utils
 * 
 * @author handong
 * @email dong.han1@renren-inc.com
 */
public class ApkUtils {
	
	/**
	 * 获取apk版本code+name+包名
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static AppVer getApkVerCode(Context context) {
		PackageInfo pi = null;
		try {
			pi = context.getPackageManager().getPackageInfo(context.getPackageName(), Context.CONTEXT_IGNORE_SECURITY);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return new AppVer(pi.versionName, pi.versionCode, pi.packageName);
	}
	
	public static class AppVer {
		public AppVer() {
		};
		
		public AppVer(String versionName, int versionCode, String pkgName) {
			this.versionName = versionName;
			this.versionCode = versionCode;
			this.pkgName = pkgName;
		}
		
		public String versionName;
		public int versionCode;
		public String pkgName;
	}
	
	/**
	 * 判断当前包名的应用是否已存在于系统中
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean checkApkExist(Context context, String packageName) {
		if (packageName == null || "".equals(packageName)) {
			return false;
		}
		try {
			ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			if (info == null) {
				return false;
			}
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}
	
	/**
	 * 卸载应用程序
	 * 
	 * @param context
	 * @param packageName
	 */
	public static void uninstallAPK(Context context, String packageName) {
		Uri uri = Uri.parse("package:" + packageName);
		Intent intent = new Intent(Intent.ACTION_DELETE, uri);
		context.startActivity(intent);
	}
	
	/**
	 * 打开应用
	 * 
	 * @param context
	 * @param packageName
	 */
	public static void startInstallApp(Context context, String packageName) {
		PackageInfo pi = null;
		try {
			pi = context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(pi.packageName);
		List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(resolveIntent, 0);
		
		ResolveInfo ri = apps.iterator().next();
		if (ri != null) {
			String packageeName = ri.activityInfo.packageName;
			String className = ri.activityInfo.name;
			
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			ComponentName cn = new ComponentName(packageeName, className);
			
			intent.setComponent(cn);
			context.startActivity(intent);
		}
		
	}
}

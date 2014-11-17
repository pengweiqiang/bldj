package com.bldj.lexiang.api;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.bldj.gson.Gson;
import com.bldj.gson.JsonArray;
import com.bldj.gson.JsonSyntaxException;
import com.bldj.lexiang.GlobalConfig;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.constant.enums.MethodType;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.NetUtil;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.HttpConnectionUtil.HttpMethod;
import com.bldj.lexiang.utils.HttpConnectionUtil.RequestCallback;

/**
 * 通用解析
 * 
 * @author zhuyb
 * @email zhuyongb0@live.com
 */
public class ApiUtils {
	
	//private static UserInfo userInfo;
	
	private static Gson gson;
	
	private static final String TAG = "ApiUtils";
	
	public static String RECOMMEND_POINT; // 推荐文案 格式如 "偶尔玩下;随便玩玩;值得一试;强力推荐"
	
	public static Set<String> userInstalledPackages = new HashSet<String>(); // 用户已经安装包
	
	public static Map<String, android.content.pm.PackageInfo> userInstalledPackageInfo = new HashMap<String, android.content.pm.PackageInfo>(); // 用户已经安装包对象
	
	/**
	 * 转成基类
	 * 
	 * @param aJson
	 * @return
	 */
	public static ParseModel parse2ParseModel(String aJson) {
		Log.d(TAG, "-------服务端返回json---------" + aJson);
		ParseModel pm = null;
		try {
			pm = getGson().fromJson(aJson, ParseModel.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
		if (pm == null)
			return pm;
		if (pm.getData().isJsonArray()) {
			JsonArray array = pm.getData().getAsJsonArray();
			pm.setResultCount(array.size());
		} else {
			pm.setResultCount(1); // TODO 默认值为1
		}
		Log.d(TAG, "返回的数量:" + pm.getResultCount());
		return pm;
	}
	
	public static String getFormatedDownTimes(String downTimes) {
		if (StringUtils.isEmpty(downTimes) || "null".equals(downTimes) || downTimes == null) {
			return 0 + "次";
		} else {
			int times = 0;
			try {
				times = Integer.parseInt(downTimes);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			int unitW = times / 10000;
			if (unitW > 0) {
				return unitW + "万次";
			} else {
				return times + "次";
			}
		}
	}

	/**
	 * 汇报对象
	 * 
	 * @param params
	 * @param requestUrl
	 * @param method
	 */
	public static void repo(Map<String, String> params, String requestUrl, HttpMethod method) {
		HttpConnectionUtil.reqNoWaitResponse(NetUtil.getConectionUrlWithoutPort(requestUrl, false), params, method);
	}
	
	
	/**
	 * 请求转换pm对象
	 * 
	 * @param params
	 * @param requestUrl
	 * @return
	 */
	public static void getParseModel(Map<String, String> params, String requestUrl, boolean isAsync,
			RequestCallback requestCallBack, MethodType methodType, Context context) {
		if (!GlobalConfig.GLOBAL_NET_STATE) { // 无网络
			ParseModel pm = new ParseModel();
			pm.setResult(String.valueOf(NetUtil.NET_ERR));
			pm.setMsg(NetUtil.NET_ERR_MSG);
			requestCallBack.execute(pm);
			return;
		}

		HttpConnectionUtil.asyncConnect(NetUtil.getConectionUrlWithoutPort(requestUrl, isAsync), params,
				HttpConnectionUtil.HttpMethod.POST, requestCallBack, methodType, context);
	
	}
		

	/**
	 * 重启
	 */
	public static void restart() {
		Intent i = GlobalConfig.appContext.getPackageManager().getLaunchIntentForPackage(
				GlobalConfig.appContext.getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		GlobalConfig.appContext.startActivity(i);
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	/**
	 * 根据屏幕分辨率自适应set截图
	 * 
	 * @param app
	 * @return
	 */
//	public static String getAutoDisplayMetrics(AppInfo app) {
//		if (DeviceInfo.getScreenWidth() < 720) { // 小于720自适应用480
//			return app.getScreenUrls480();
//		} else if (DeviceInfo.getScreenWidth() < 1080) { // 小于1080
//			return app.getScreenUrls720();
//		}
//		return app.getScreenUrls480();
//	}
	
	/**
	 * 获取用户已安装包
	 * 
	 * @return
	 */
	public static Set<String> getUserInstalledPackages() {
		if (GlobalConfig.appContext != null) {
			PackageManager pm = GlobalConfig.appContext.getPackageManager();
			List<android.content.pm.PackageInfo> packageInfos = pm.getInstalledPackages(0);
			for (android.content.pm.PackageInfo packageInfo : packageInfos) {
				if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {// 非系统应用
					userInstalledPackages.add(packageInfo.packageName);
					userInstalledPackageInfo.put(packageInfo.packageName, packageInfo);
				}
			}
		}
		return userInstalledPackages;
	}

	
	// /////////////////////////////////////////////////////////////////////////////////////////////
	private static Gson getGson() {
		if (gson == null) {
			gson = new Gson();
		}
		return gson;
	}
	
}

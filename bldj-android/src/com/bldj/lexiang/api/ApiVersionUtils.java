package com.bldj.lexiang.api;

import android.content.Context;

import java.util.Map;

import com.bldj.lexiang.GlobalConfig;
import com.bldj.lexiang.constant.api.ReqUrls;
import com.bldj.lexiang.constant.enums.MethodType;
import com.bldj.lexiang.http.HttpClientAddHeaders;
import com.bldj.lexiang.utils.HttpConnectionUtil.RequestCallback;
/**
 * 版本升级
 * 
 * @author zhuyb
 * @email zhuyongb0@live.com
 */
public class ApiVersionUtils {		
	/**
	 * 获取最新版本信息
	 * @param context
	 * @param requestCallBack
	 */
	public static void checkVersion(Context context,RequestCallback requestCallBack){
		Map<String,Object> params = HttpClientAddHeaders.getHeaders(context);
        params.put("terType",params.get("source"));
        params.put("vcode", GlobalConfig.VERSION_CODE_V);
		ApiUtils.getParseModel(params, ReqUrls.CHECK_VERSION, false, requestCallBack,MethodType.GET_MAINPAGE_AD,null);
	}
}

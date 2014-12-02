package com.bldj.lexiang.api;

import android.content.Context;

import java.util.Map;

import com.bldj.lexiang.constant.api.ReqUrls;
import com.bldj.lexiang.constant.enums.MethodType;
import com.bldj.lexiang.http.HttpClientAddHeaders;
import com.bldj.lexiang.utils.HttpConnectionUtil.HttpMethod;
import com.bldj.lexiang.utils.HttpConnectionUtil.RequestCallback;

/**
 * 首页接口
 * 
 */
public class ApiHomeUtils {
	/**
	 * 获取首页广告位
	 * 
	 * @param context
	 * @param limit
	 * @param type
	 * @param requestCallBack
	 * @param adIndex
	 */
	public static void getAdList(Context context, int limit, int type,
			RequestCallback requestCallBack) {
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.LIMIT, limit);
		params.put("type", type);
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_GET_MAINPAGE_AD, false,
				requestCallBack, MethodType.GET_MAINPAGE_AD, context,
				HttpMethod.GET);
	}
	/**
	 * 企业专区
	 * @param context
	 * @param packageId 所选择的套餐id(从0开始)
	 * @param applicationName
	 * @param enterName
	 * @param contact
	 * @param contactor
	 * @param detailAddress
	 * @param requestCallBack
	 */
	public static void createCompanyZone(Context context, int packageId,
			String applicationName, String enterName, String contact,
			String contactor, String detailAddress,RequestCallback requestCallBack) {
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put("packageId", packageId);
		params.put("applicantName", applicationName);
		params.put("enterName", enterName);
		params.put("contact", contact);
		params.put("contactor", contactor);
		params.put("detailAddress", detailAddress);
		
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_COMPANY_ZONE, false,
				requestCallBack, MethodType.GET_MAINPAGE_AD, context,
				HttpMethod.GET);
	}

}

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
				requestCallBack, MethodType.GET_MAINPAGE_AD, context);
	}
	/**
	 * 企业专区
	 * @param context
	 * @param packageId 所选择的套餐id(从0开始)
	 * @param applicationName 当前预约人
	 * @param enterName 企业名称
	 * @param contact 联系方式
	 * @param contactor 联系人
	 * @param detailAddress 详细地址
	 * @param price	实际支付金额
	 * @param paytype  支付方式
	 * @param requestCallBack
	 */
	public static void createCompanyZone(Context context, int packageId,
			String applicationName, String enterName, String contact,
			String contactor, String detailAddress,double price,int payType,RequestCallback requestCallBack) {
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put("packageId", packageId);
		params.put("applicantName", applicationName);
		params.put("enterName", enterName);
		params.put("contact", contact);
		params.put("contactor", contactor);
		params.put("detailAddress", detailAddress);
		params.put("price", price);
		params.put("payType", payType);
		
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_COMPANY_ZONE, false,
				requestCallBack, MethodType.GET_MAINPAGE_AD, context);
	}
	/**
	 * 获取企业专区优惠列表
	 * @param context
	 * @param start
	 * @param limit
	 * @param requestCallBack
	 */
	public static void getCheapCards(Context context,int start,int limit,RequestCallback requestCallBack){
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.START, start);
		params.put(ReqUrls.LIMIT, limit);
		
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_CHEAP_CARDS, false,
				requestCallBack, MethodType.GET_MAINPAGE_AD, context);
	}

}

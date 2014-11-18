package com.bldj.lexiang.api;

import java.util.Map;

import android.content.Context;

import com.bldj.lexiang.constant.api.ReqUrls;
import com.bldj.lexiang.constant.enums.MethodType;
import com.bldj.lexiang.http.HttpClientAddHeaders;
import com.bldj.lexiang.utils.HttpConnectionUtil.RequestCallback;

/**
 * 购买预约接口
 * 
 * @author will
 * 
 */
public class ApiBuyUtils {
	/**
	 * 预约时间和选择美容师接口
	 * 
	 * @param context
	 * @param id
	 * @param proId
	 * @param dealDate
	 * @param requestCallBack
	 */
	public static void getScheduled(Context context, long id, long proId,
			String dealDate, RequestCallback requestCallBack) {
		Map<String, String> params = HttpClientAddHeaders.getHeaders(context);
		params.put("id", String.valueOf(id));
		params.put("proId", String.valueOf(proId));
		params.put("dealDate", dealDate);
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_SCHEDULED, false,
				requestCallBack, MethodType.GET_MAINPAGE_AD, context);
	}

	/**
	 * 添加、查询和校验优惠卷
	 * 
	 * @param context
	 * @param id
	 * @param type
	 * @param vcode
	 * @param method
	 * @param requestCallBack
	 */
	public static void couponsManage(Context context, long id, int type,
			String vcode, int method, RequestCallback requestCallBack) {
		Map<String, String> params = HttpClientAddHeaders.getHeaders(context);
		params.put("id", String.valueOf(id));
		params.put("type", String.valueOf(type));
		params.put("vcode", vcode);
		params.put("method", String.valueOf(method));
		ApiUtils.getParseModel(params, ReqUrls.COUPONS_MANAGE, false,
				requestCallBack, MethodType.GET_MAINPAGE_AD, context);

	}

	/**
	 * 生成订单
	 * 
	 * @param context
	 * @param userId
	 * @param username
	 * @param sellerId
	 * @param sellerName
	 * @param productId
	 * @param proName
	 * @param orderPay
	 * @param curuser
	 * @param type
	 * @param contactor
	 * @param mobile
	 * @param detailAddress
	 * @param notes
	 * @param payType
	 * @param requestCallback
	 */
	public static void createOrder(Context context, long userId,
			String username, long sellerId, String sellerName, long productId,
			String proName, double orderPay, String curuser, int type,
			String contactor, String mobile, String detailAddress,
			String notes, int payType, RequestCallback requestCallback) {
		Map<String, String> params = HttpClientAddHeaders.getHeaders(context);
		params.put("userId", String.valueOf(userId));
		params.put("username", username);
		params.put("sellerId", String.valueOf(sellerId));
		params.put("sellerName", sellerName);
		params.put("productId", String.valueOf(productId));
		params.put("proName", proName);
		params.put("orderPay", String.valueOf(orderPay));
		params.put("curuser", curuser);
		params.put("type", String.valueOf(type));
		params.put("contactor", contactor);
		params.put("mobile", mobile);
		params.put("detailAddress", detailAddress);
		params.put("notes", notes);
		params.put("payType", String.valueOf(payType));

		ApiUtils.getParseModel(params, ReqUrls.CREATE_ORDER, false,
				requestCallback, MethodType.GET_MAINPAGE_AD, context);

	}
	/**
	 * 获取用户订单
	 * @param context
	 * @param id
	 * @param requestCallback
	 */
	public static void getOrders(Context context,long id,RequestCallback requestCallback){
		Map<String, String> params = HttpClientAddHeaders.getHeaders(context);
		params.put("id", String.valueOf(id));
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_ORDERS, false,
				requestCallback, MethodType.GET_MAINPAGE_AD, context);
	}

}

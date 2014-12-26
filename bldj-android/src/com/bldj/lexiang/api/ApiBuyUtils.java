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
	 * @param id 用户id
	 * @param proId 产品id
	 * @param dealDate YYYYMMDD格式
	 * @param requestCallBack
	 */
	public static void getScheduled(Context context, long id, long proId,
			String dealDate, RequestCallback requestCallBack) {
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.ID, id);
		params.put("proId", proId);
		params.put("dealDate", dealDate);
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_SCHEDULED, false,
				requestCallBack, MethodType.GET_MAINPAGE_AD, context);
	}

	/**
	 * 添加、查询和校验优惠卷
	 * 
	 * @param context
	 * @param id 用户id
	 * @param type -1：获取所有的 0：获取注册和分享的  1：只获取分享的
	 * @param vcode 电子券码
	 * @param method 0添加 3查询 4校验
	 * @param start 起始页
	 * @param limit 每页数量
	 * @param status 0 有效 1过期失效
	 * @param requestCallBack
	 */
	public static void couponsManage(Context context, long id, int type,
			String vcode, int method,int start,int limit,int status, RequestCallback requestCallBack) {
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.ID, id);
		params.put("type", type);
		params.put("vcode", vcode);
		params.put("method", method);
		params.put("status", status);
		params.put(ReqUrls.START, start);
		params.put(ReqUrls.LIMIT, limit);
		ApiUtils.getParseModel(params, ReqUrls.COUPONS_MANAGE, false,
				requestCallBack, MethodType.GET_MAINPAGE_AD, context);

	}

	/**
	 * 生成订单
	 * 
	 * @param context
	 * @param userId 用户id
	 * @param username 用户名
	 * @param sellerId 卖家id
	 * @param sellerName 卖家姓名
	 * @param productId 产品id
	 * @param proName 产品名称
	 * @param orderPay 总价
	 * @param curuser 当前操作人
	 * @param type 0预约 1立即
	 * @param contactor 联系人
	 * @param mobile 手机号
	 * @param detailAddress 详细地址
	 * @param notes 备案
	 * @param payType 0支付宝 1微信 2网银
	 * @param couponsId 优惠券id
	 * @param servicetime 预约的时间如20141218@0~15等数字(0 表示10点；1表示11点类推)
	 * @param requestCallback 
	 */
	public static void createOrder(Context context, long userId,
			String username, long sellerId, String sellerName, long productId,
			String proName, double orderPay, String curuser, int type,
			String contactor, String mobile, String detailAddress,
			String notes, int payType,long couponsId,String servicetime, RequestCallback requestCallback) {
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put("userId", userId);
		params.put("username", username);
		params.put("sellerId", sellerId);
		params.put("sellerName", sellerName);
		params.put("productId", productId);
		params.put("proName", proName);
		params.put("orderPay", orderPay);
		params.put("curuser", curuser);
		params.put("type", type);
		params.put("contactor", contactor);
		params.put("mobile", mobile);
		params.put("detailAddress", detailAddress);
		params.put("notes", notes);
		params.put("payType", payType);
		params.put("couponsId", couponsId);
		params.put("servicetime", servicetime);

		ApiUtils.getParseModel(params, ReqUrls.CREATE_ORDER, false,
				requestCallback, MethodType.GET_MAINPAGE_AD, context);

	}
	/**
	 * 用户支付成功
	 * @param context
	 * @param id 用户id
	 * @param orderNum 订单号
	 * @param requestCallback
	 */
	public static void orderSuccess(Context context,long id ,String orderNum,RequestCallback requestCallback){
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.ID, id);
		params.put("orderNum", orderNum);
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_ORDER_SUCCESS, false,
				requestCallback, MethodType.GET_MAINPAGE_AD, context);
	}
	
	/**
	 * 获取用户订单
	 * @param context
	 * @param id
	 * @param requestCallback
	 */
	public static void getOrders(Context context,long id,int start,int limit,RequestCallback requestCallback){
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.ID, id);
		params.put(ReqUrls.START, start);
		params.put(ReqUrls.LIMIT, limit);
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_ORDERS, false,
				requestCallback, MethodType.GET_MAINPAGE_AD, context);
	}
	/**
	 * 订单管理（作废。。。）
	 * @param context  
	 * @param id 用户id
	 * @param orderNum  订单号
	 * @param status 状态  3-取消
	 * @param requestCallback
	 */
	public static void orderManager(Context context,long id,String orderNum,int status,RequestCallback requestCallback) {
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.ID, id);
		params.put("orderNum", orderNum);
		params.put("status", status);
		ApiUtils.getParseModel(params, ReqUrls.ORDER_MANAGER, false,
				requestCallback, MethodType.GET_MAINPAGE_AD, context);
	}
	/**
	 * 获取支付方式
	 * @param context
	 * @param requestCallback
	 */
	public static void getPayType(Context context,RequestCallback requestCallback){
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_PAY_TYPE, false,
				requestCallback, MethodType.GET_MAINPAGE_AD, context);
	}
	/**
	 * 获取支付参数
	 * @param context
	 * @param id
	 * @param requestCallback
	 */
	public static void getPayTypeById(Context context,long id ,RequestCallback requestCallback){
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.ID, id);
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_PAY_TYPE_BYID, false,
				requestCallback, MethodType.GET_MAINPAGE_AD, context);
		
	}

}

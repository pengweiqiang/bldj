package com.bldj.lexiang.api;

import android.content.Context;

import java.util.Map;

import com.bldj.lexiang.constant.api.ReqUrls;
import com.bldj.lexiang.constant.enums.MethodType;
import com.bldj.lexiang.http.HttpClientAddHeaders;
import com.bldj.lexiang.utils.HttpConnectionUtil.HttpMethod;
import com.bldj.lexiang.utils.HttpConnectionUtil.RequestCallback;

/**
 * api 美容师接口
 * 
 * @author zhuyb
 * @email zhuyongb0@live.com
 */
public class ApiSellerUtils {
	/**
	 * 获取美容师列表
	 * 
	 * @param context
	 * @param start 初始页码
	 * @param limit 显示的个数
	 * @param startPrice 起始价格
	 * @param endPrice  终止价格
	 * @param startWorkyear 起始工作年限
	 * @param endWorkyear 终止工作年限
	 * @param orderbyTag 排序字段 0时间 3均价 4累计成交量 5距离
	 * @param lat
	 * @param lon
	 * @param requestCallBack
	 */
	public static void getSellers(Context context, int start, int limit,
			int startPrice, int endPrice, int startWorkyear, int endWorkyear,
			int orderbyTag,double lat,double lon, RequestCallback requestCallBack) {
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.START, start);
		params.put(ReqUrls.LIMIT, limit);
		params.put("startPrice",startPrice);
		params.put("endPrice", endPrice);
		params.put("startWorkyear", startWorkyear);
		if(endWorkyear!=-1){
			params.put("endWorkyear", endWorkyear);
		}
		params.put("orderbyTag", orderbyTag);
		params.put("lat", lat);
		params.put("lon", lon);

		ApiUtils.getParseModel(params, ReqUrls.REQUEST_SELLERS, false,
				requestCallBack, MethodType.GET_MAINPAGE_AD, context,HttpMethod.GET);
	}
	
	/**
	 * 获取美容师好中差的个数
	 * @param context
	 * @param id
	 * @param requestCallBack
	 */
	public static void getSellerEvals(Context context,long id,RequestCallback requestCallBack){
		
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.ID, id);
		
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_SELLER_EVALS, false,
				requestCallBack, MethodType.GET_MAINPAGE_AD, context);
	}
	/**
	 * 获取美容师产品列表
	 * @param context
	 * @param id 卖家id
	 * @param start 起始页
	 * @param limit 每页条数
	 * @param requestCallBack
	 */
	public static void getSellerProduct(Context context,long id ,int start,int limit,RequestCallback requestCallBack){
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.ID, id);
		params.put(ReqUrls.START, start);
		params.put(ReqUrls.LIMIT, limit);
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_SELLER_PRODUCTS, false,
				requestCallBack, MethodType.GET_MAINPAGE_AD, context);
	}
	
	/**
	 * 获取美容师评论列表
	 * @param context
	 * @param id 卖家id
	 * @param start 起始页
	 * @param limit 每页条数
	 * @param requestCallBack
	 */
	public static void getSellerScores(Context context,long id ,int start,int limit,RequestCallback requestCallBack){
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.ID, id);
		params.put(ReqUrls.START, start);
		params.put(ReqUrls.LIMIT, limit);
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_SELLER_SCORES, false,
				requestCallBack, MethodType.GET_MAINPAGE_AD, context);
	}
	/**
	 * 获取美容师个人信息
	 * @param context
	 * @param id
	 * @param requestCallback
	 */
	public static void getSellerById(Context context,long id,RequestCallback requestCallback){
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.ID, id);
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_SELLER_BYID, false,
				requestCallback, MethodType.GET_MAINPAGE_AD, context);
	}
	
	/**
	 * 获取可预约美容师列表
	 * @param context
	 * @param proId
	 * @param dealDate
	 * @param requestCallback
	 */
	public static void getSellerByProIdAndDate(Context context,long proId,String dealDate,int start,int limit,RequestCallback requestCallback){
		Map<String,Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put("proId", proId);
		params.put("dealDate", dealDate);
		params.put(ReqUrls.START, start);
		params.put(ReqUrls.LIMIT, limit);
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_SELLER_SCHEDULED, false,
				requestCallback, MethodType.GET_MAINPAGE_AD, context);
	}
	
}

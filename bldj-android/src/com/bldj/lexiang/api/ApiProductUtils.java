package com.bldj.lexiang.api;

import android.content.Context;
import java.util.Map;

import com.bldj.lexiang.constant.api.ReqUrls;
import com.bldj.lexiang.constant.enums.MethodType;
import com.bldj.lexiang.http.HttpClientAddHeaders;
import com.bldj.lexiang.utils.HttpConnectionUtil.RequestCallback;
/**
 * api 产品相关的接口
 * 
 * @author handong
 * @email handong@live.com
 */
public class ApiProductUtils {

	/**
	 * 获取精品及热门推荐接口
	 * @param context
	 * @param tag 0热门推荐 1精品推荐
	 * @param userType (0个人1 团体 2所有)
	 * @param orderbyTag (0时间 1价格 2销量)
	 * @param type (0美容 1美体 2养生)
	 * @param start (页数)
	 * @param limit (条数)
	 * @param requestCallBack
	 */
	public static void getProducts(Context context,String tag,int userType,int orderbyTag,int type,int start,int limit,RequestCallback requestCallBack){
		Map<String,Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put("tag", tag);
		params.put("userType", userType);
		params.put("orderbyTag", orderbyTag);
		params.put("type", type);
		params.put(ReqUrls.START, start);
		params.put(ReqUrls.LIMIT, limit);
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_HOT_PRODUCT, false, requestCallBack,MethodType.GET_MAINPAGE_AD,context);
	}
	/**
	 * 获取分类列表
	 * @param context
	 * @param start
	 * @param limit
	 * @param requestCallback
	 */
	public static void getCategory(Context context,int start,int limit,RequestCallback requestCallback){
		Map<String,Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.START, start);
		params.put(ReqUrls.LIMIT, limit);
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_GET_CATEGORY, false, requestCallback,MethodType.GET_MAINPAGE_AD,context);
	
	}
	
	
	/**
	 * 获取分类的产品列表
	 * @param context
	 * @param start 页数
	 * @param limit 条数
	 * @param id 分类id
	 * @param requestCallback
	 */
	public static void getProductByCategoryByid(Context context,int start,int limit,long id,RequestCallback requestCallback){
		Map<String,Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.ID, id);
		params.put(ReqUrls.START, start);
		params.put(ReqUrls.LIMIT, limit);
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_GET_CATEGORY_PRODUCT, false, requestCallback,MethodType.GET_MAINPAGE_AD,context);
	}
	/**
	 * 获取产品明细
	 * @param context
	 * @param productId
	 * @param requestCallback
	 */
	public static void getProductById(Context context,long productId,RequestCallback requestCallback){
		Map<String,Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.ID, productId);
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_GET_PRODUCT_BYID, false, requestCallback,MethodType.GET_MAINPAGE_AD,context);
		
	}
	
}

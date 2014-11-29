package com.bldj.lexiang.api;

import android.content.Context;

import java.util.Map;

import com.bldj.lexiang.constant.api.ReqUrls;
import com.bldj.lexiang.constant.enums.MethodType;
import com.bldj.lexiang.http.HttpClientAddHeaders;
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
	 * @param start
	 * @param limit
	 * @param startPrice
	 * @param endPrice
	 * @param startWorkyear
	 * @param endWorkyear
	 * @param orderbyTag
	 * @param requestCallBack
	 * @param adIndex
	 */
	public static void getSellers(Context context, int start, int limit,
			int startPrice, int endPrice, int startWorkyear, int endWorkyear,
			int orderbyTag, RequestCallback requestCallBack, int adIndex) {
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.START, String.valueOf(start));
		params.put(ReqUrls.LIMIT, String.valueOf(limit));
		params.put("startPrice", String.valueOf(startPrice));
		params.put("endPrice", String.valueOf(endPrice));
		params.put("startWorkyear", String.valueOf(startWorkyear));
		params.put("endWorkyear", String.valueOf(endWorkyear));
		params.put("orderbyTag", String.valueOf(orderbyTag));

		ApiUtils.getParseModel(params, ReqUrls.REQUEST_SELLERS, false,
				requestCallBack, MethodType.GET_MAINPAGE_AD, context);
	}
	
	/**
	 * 获取美容师好中差的个数
	 * @param context
	 * @param id
	 * @param requestCallBack
	 */
	public static void getSellerEvals(Context context,int id,RequestCallback requestCallBack){
		
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.ID, String.valueOf(id));
		
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_SELLER_EVALS, false,
				requestCallBack, MethodType.GET_MAINPAGE_AD, context);
	}
	
}

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
		params.put("tag", String.valueOf(tag));
		params.put("userType", String.valueOf(userType));
		params.put("orderbyTag", String.valueOf(orderbyTag));
		params.put("type", String.valueOf(type));
		params.put(ReqUrls.START, String.valueOf(start));
		params.put(ReqUrls.LIMIT, String.valueOf(limit));
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_HOT_PRODUCT, false, requestCallBack,MethodType.GET_MAINPAGE_AD,context);
	}
	
	
}

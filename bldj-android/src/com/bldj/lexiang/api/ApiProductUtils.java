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
	 * @param tag
	 * @param userType
	 * @param orderbyTag
	 * @param type
	 * @param start
	 * @param limit
	 * @param requestCallBack
	 */
	public static void getProducts(Context context,String tag,int userType,int orderbyTag,int type,int start,int limit,RequestCallback requestCallBack){
		Map<String,String> params = HttpClientAddHeaders.getHeaders(context);
		params.put("tag", tag);
		params.put("userType", String.valueOf(userType));
		params.put("orderbyTag", String.valueOf(orderbyTag));
		params.put("type", String.valueOf(type));
		params.put("start", String.valueOf(start));
		params.put(ReqUrls.LIMIT, String.valueOf(limit));
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_HOT_PRODUCT, false, requestCallBack,MethodType.GET_MAINPAGE_AD,context);
	}
	
	
}

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
	 * @param context
	 * @param limit
	 * @param type
	 * @param requestCallBack
	 * @param adIndex
	 */
	public static void getAdList(Context context,int limit,int type,RequestCallback requestCallBack){
		Map<String,Object> params = HttpClientAddHeaders.getHeaders(context);
        params.put(ReqUrls.LIMIT,limit);
        params.put("type",type);
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_GET_MAINPAGE_AD, false, requestCallBack,MethodType.GET_MAINPAGE_AD,context,HttpMethod.GET);
	}
}

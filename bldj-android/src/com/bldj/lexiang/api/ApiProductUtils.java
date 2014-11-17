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
	 * 获取app 列表
	 * @return
	 */
	public static void getAppList(Context context,long appId,int limit,int type,RequestCallback requestCallBack){
		Map<String,String> params = HttpClientAddHeaders.getHeaders(context);
		if(appId != 0){
			params.put(ReqUrls.APP_ID, String.valueOf(appId));
		}
		params.put(ReqUrls.LIMIT, String.valueOf(limit));
		params.put(ReqUrls.OPER_TYPE, String.valueOf(type));
		//ApiUtils.getParseModel(params, ReqUrls.REQUEST_GET_APP_LIST, false, requestCallBack,MethodType.GET_APPS,null);
	}
}

package com.bldj.lexiang.api;

import android.content.Context;

import java.util.Map;

import com.bldj.lexiang.constant.api.ReqUrls;
import com.bldj.lexiang.constant.enums.MethodType;
import com.bldj.lexiang.http.HttpClientAddHeaders;
import com.bldj.lexiang.utils.HttpConnectionUtil.RequestCallback;
/**
 * api 广告相关的
 * 
 * @author zhuyb
 * @email zhuyongb0@live.com
 */
public class ApiAdUtils {		
	/**
	 * 获取首页广告位top one
	 * @return
	 */
	public static void getAdList(Context context,RequestCallback requestCallBack,int adIndex){
		Map<String,String> params = HttpClientAddHeaders.getHeaders(context);
        params.put(ReqUrls.AD_INDEX,String.valueOf(adIndex));
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_GET_APPSTORE_AD, false, requestCallBack,MethodType.GET_MAINPAGE_AD,null);
	}
}

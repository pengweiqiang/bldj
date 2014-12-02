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
	 * @param start 初始页码
	 * @param limit 显示的个数
	 * @param startPrice 起始价格
	 * @param endPrice  终止价格
	 * @param startWorkyear 起始工作年限
	 * @param endWorkyear 终止工作年限
	 * @param orderbyTag 排序字段 0时间 3均价 4累计成交量 5距离
	 * @param requestCallBack
	 */
	public static void getSellers(Context context, int start, int limit,
			int startPrice, int endPrice, int startWorkyear, int endWorkyear,
			int orderbyTag, RequestCallback requestCallBack) {
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.START, start);
		params.put(ReqUrls.LIMIT, limit);
		params.put("startPrice",startPrice);
		params.put("endPrice", endPrice);
		params.put("startWorkyear", startWorkyear);
		params.put("endWorkyear", endWorkyear);
		params.put("orderbyTag", orderbyTag);

		ApiUtils.getParseModel(params, ReqUrls.REQUEST_SELLERS, false,
				requestCallBack, MethodType.GET_MAINPAGE_AD, context);
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
	
}

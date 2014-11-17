package com.bldj.lexiang.api;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.bldj.lexiang.api.bo.ExceptionInfo;
import com.bldj.lexiang.constant.api.ReqUrls;
import com.bldj.lexiang.constant.enums.MethodType;
import com.bldj.lexiang.http.HttpClientAddHeaders;
import com.bldj.lexiang.utils.HttpConnectionUtil.HttpMethod;
import com.bldj.lexiang.utils.HttpConnectionUtil.RequestCallback;

/**
 * api user相关的接口
 * 
 * @author zhuyb
 * @email zhuyongb0@live.com
 */
public class ApiUserUtils {
	/**
	 * 汇报异常信息
	 * @param exception
	 */
	public static void repoException(ExceptionInfo exception){
		Map<String,String> params = new HashMap<String, String>();
		params.put(ReqUrls.PRODUCT_MODEL, exception.getProductModel());
		params.put(ReqUrls.CUR_VERSION, exception.getCurAppVerMsg());
		params.put(ReqUrls.EXCEPTION_MSG, exception.getExceptionMsg());
		ApiUtils.repo(params, ReqUrls.REQUEST_EXCEPTION_REPO, HttpMethod.POST);
	}

    /**
     * 获取用户个人中心界面
     * @param userId
     * @return
     */
    public static void getUserInfo(Context context,RequestCallback requestCallBack){
        Map<String,String> params = HttpClientAddHeaders.getHeaders(context);
        //ApiUtils.getParseModel(params, ReqUrls.REQUEST_USER_INFO, false, requestCallBack,MethodType.USER_PAGE_INFO,null);
    }


    /**
     * 修改用户信息
     * @param enterId
     * @return
     */
    public static void modifyUserInfo(Context context,String name,String address,String mobile,RequestCallback requestCallBack){
        Map<String,String> params = HttpClientAddHeaders.getHeaders(context);
        params.put(ReqUrls.NAME, name);
        params.put(ReqUrls.MOBILE, mobile);
        params.put(ReqUrls.ADDRESS, address);
        ApiUtils.getParseModel(params, ReqUrls.UPDATE_USER_INFO, false, requestCallBack,MethodType.GET_MAINPAGE_AD,null);
    }

    /**
     * 修改用户信息
     * @param
     * @return
     */
    public static void downloadRepoInfo(Context context,long appId){
        Map<String,String> params = HttpClientAddHeaders.getHeaders(context);
        params.put(ReqUrls.APP_ID, String.valueOf(appId));
        ApiUtils.repo(params, ReqUrls.DOWNLOAD_REPO_INFO, HttpMethod.GET);
        //ApiUtils.getParseModel(params, ReqUrls.DOWNLOAD_REPO_INFO, false, null,MethodType.DOWNLOAD_REPO_INFO,null);
    }


	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static String parseLong2Str(long userId){
		return String.valueOf(userId);
	}
}

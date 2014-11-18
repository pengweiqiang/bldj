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
	 * 
	 * @param exception
	 */
	public static void repoException(ExceptionInfo exception) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(ReqUrls.PRODUCT_MODEL, exception.getProductModel());
		params.put(ReqUrls.CUR_VERSION, exception.getCurAppVerMsg());
		params.put(ReqUrls.EXCEPTION_MSG, exception.getExceptionMsg());
		ApiUtils.repo(params, ReqUrls.REQUEST_EXCEPTION_REPO, HttpMethod.POST);
	}

	/**
	 * 登录
	 * 
	 * @param context
	 * @param username
	 * @param password
	 * @param requestCallBack
	 */
	public static void login(Context context, String username, String password,
			RequestCallback requestCallBack) {
		Map<String, String> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.USERNAME, username);
		params.put(ReqUrls.PASSWORD, password);
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_USER_LOGIN, false,
				requestCallBack, MethodType.LOGIN, context);
	}

	/**
	 * 修改密码
	 * 
	 * @param context
	 * @param username
	 * @param opass
	 * @param password
	 * @param requestCallback
	 */
	public static void updatePwd(Context context, String username,
			String opass, String password, RequestCallback requestCallback) {
		Map<String, String> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.USERNAME, username);
		params.put(ReqUrls.OPASS, opass);
		params.put(ReqUrls.PASSWORD, password);
		ApiUtils.getParseModel(params, ReqUrls.UPDATE_PWD, false,
				requestCallback, MethodType.UPDATE, context);
	}

	/**
	 * 修改昵称
	 * 
	 * @param context
	 * @param username
	 * @param nickname
	 * @param requestCallback
	 */
	public static void updateNickName(Context context, String username,
			String nickname, RequestCallback requestCallback) {
		Map<String, String> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.USERNAME, username);
		params.put(ReqUrls.NICK_NAME, nickname);
		ApiUtils.getParseModel(params, ReqUrls.UPDATE_NICKNAME, false,
				requestCallback, MethodType.UPDATE, context);
	}

	/**
	 * 修改头像
	 * 
	 * @param context
	 * @param username
	 * @param stream
	 *            经过Base64转码过的字符串
	 * @param requestCallback
	 */
	public static void updateHeader(Context context, String username,
			String stream, RequestCallback requestCallback) {
		Map<String, String> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.USERNAME, username);
		params.put(ReqUrls.STREAM, stream);
		ApiUtils.getParseModel(params, ReqUrls.UPDATE_HEADER_URL, false,
				requestCallback, MethodType.UPDATE, context);
	}

	/**
	 * 信息收集接口(意见、招聘、打分)
	 * 
	 * @param context
	 * @param userId
	 * @param content
	 * @param type
	 * @param contactor
	 * @param contact
	 * @param ordernum
	 * @param productName
	 * @param sellerName
	 * @param infoType
	 * @param sellerId
	 * @param requestCallback
	 */
	public static void unifor(Context context, String userId, String content,
			int type, String contactor, String contact, String ordernum,
			String productName, String sellerName, int infoType, long sellerId,
			RequestCallback requestCallback) {
		Map<String, String> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.USER_ID, userId);
		params.put("content", content);
		params.put("type", String.valueOf(type));
		params.put("contactor", contactor);
		params.put("contact", contact);
		params.put("ordernum", ordernum);
		params.put("productName", productName);
		params.put("sellerName", sellerName);
		params.put("infoType", String.valueOf(infoType));
		params.put("sellerId", String.valueOf(sellerId));

		ApiUtils.getParseModel(params, ReqUrls.UNIFOR, false, requestCallback,
				MethodType.UPDATE, context);
	}

	/**
	 * 忘记密码
	 * 
	 * @param context
	 * @param username
	 * @param password
	 * @param requestCallback
	 */
	public static void forgetPwd(Context context, String username,
			String password, RequestCallback requestCallback) {
		Map<String, String> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.USERNAME, username);
		params.put(ReqUrls.PASSWORD, password);
		ApiUtils.getParseModel(params, ReqUrls.FORGET_PWD, false,
				requestCallback, MethodType.UPDATE, context);
	}

	/**
	 * 注册
	 * 
	 * @param context
	 * @param mobile
	 * @param password
	 * @param lon
	 * @param lat
	 * @param requestCallback
	 */
	public static void register(Context context, String mobile,
			String password, String lon, String lat,
			RequestCallback requestCallback) {
		Map<String, String> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.PASSWORD, password);
		params.put(ReqUrls.MOBILE, mobile);
		params.put(ReqUrls.USER_LON, lon);
		params.put(ReqUrls.USER_LAT, lat);
		ApiUtils.getParseModel(params, ReqUrls.REGISTER_USER, false,
				requestCallback, MethodType.UPDATE, context);
	}

	/**
	 * 地址管理
	 * 
	 * @param context
	 * @param type
	 *            0:添加 1删除 2修改 3查询
	 * @param userId
	 *            用户id
	 * @param curLocation
	 *            当前位置
	 * @param detailAddress
	 *            详细地址
	 * @param id
	 *            地址id
	 * @param requestCallback
	 */
	public static void addressManager(Context context, int type, String userId,
			String curLocation, String detailAddress, String id,
			RequestCallback requestCallback) {
		Map<String, String> params = HttpClientAddHeaders.getHeaders(context);
		params.put("type", String.valueOf(type));
		switch (type) {
		case 0://增加地址
			params.put(ReqUrls.USER_ID, userId);
			params.put("curLocation", curLocation);
			params.put("detailAddress", detailAddress);
			break;
		case 1://删除地址
			params.put(ReqUrls.ID, id);
			break;
		case 2://修改地址
			params.put(ReqUrls.USER_ID, userId);
			params.put("curLocation", curLocation);
			params.put("detailAddress", detailAddress);
			params.put(ReqUrls.ID, id);
			break;
		case 3://查询地址
			params.put(ReqUrls.USER_ID, userId);
			break;
			
		default:
			break;
		}
		
		ApiUtils.getParseModel(params, ReqUrls.ADDRESS_MANAGER, false,
				requestCallback, MethodType.UPDATE, context);
	}
	/**
	 * 短信验证
	 * @param context
	 * @param mobile
	 * @param code
	 * @param type
	 * @param name
	 * @param requestCallback
	 */
	public static void checkCode(Context context, String mobile,
			String code, String type, String name,
			RequestCallback requestCallback) {
		Map<String, String> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.MOBILE, mobile);
		params.put("code", code);
		params.put("type", type);
		params.put(ReqUrls.NAME, name);
		ApiUtils.getParseModel(params, ReqUrls.CHECK_CODE, false,
				requestCallback, MethodType.UPDATE, context);
	}
	

//	/**
//	 * 获取用户个人中心界面
//	 * 
//	 * @param userId
//	 * @return
//	 */
//	public static void getUserInfo(Context context,
//			RequestCallback requestCallBack) {
//		Map<String, String> params = HttpClientAddHeaders.getHeaders(context);
//		// ApiUtils.getParseModel(params, ReqUrls.REQUEST_USER_INFO, false,
//		// requestCallBack,MethodType.USER_PAGE_INFO,null);
//	}
//
//	/**
//	 * 修改用户信息
//	 * 
//	 * @param enterId
//	 * @return
//	 */
//	public static void modifyUserInfo(Context context, String name,
//			String address, String mobile, RequestCallback requestCallBack) {
//		Map<String, String> params = HttpClientAddHeaders.getHeaders(context);
//		params.put(ReqUrls.NAME, name);
//		params.put(ReqUrls.MOBILE, mobile);
//		params.put(ReqUrls.ADDRESS, address);
//		ApiUtils.getParseModel(params, ReqUrls.UPDATE_USER_INFO, false,
//				requestCallBack, MethodType.GET_MAINPAGE_AD, null);
//	}
//
//	/**
//	 * 修改用户信息
//	 * 
//	 * @param
//	 * @return
//	 */
//	public static void downloadRepoInfo(Context context, long appId) {
//		Map<String, String> params = HttpClientAddHeaders.getHeaders(context);
//		params.put(ReqUrls.APP_ID, String.valueOf(appId));
//		ApiUtils.repo(params, ReqUrls.DOWNLOAD_REPO_INFO, HttpMethod.GET);
//		// ApiUtils.getParseModel(params, ReqUrls.DOWNLOAD_REPO_INFO, false,
//		// null,MethodType.DOWNLOAD_REPO_INFO,null);
//	}
//
//	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	public static String parseLong2Str(long userId) {
//		return String.valueOf(userId);
//	}
}

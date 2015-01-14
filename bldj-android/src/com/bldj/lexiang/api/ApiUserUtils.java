package com.bldj.lexiang.api;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.bldj.lexiang.api.bo.ExceptionInfo;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.constant.api.ReqUrls;
import com.bldj.lexiang.constant.enums.MethodType;
import com.bldj.lexiang.http.HttpClientAddHeaders;
import com.bldj.lexiang.utils.FormFieldKeyValuePair;
import com.bldj.lexiang.utils.HttpConnectionUtil.HttpMethod;
import com.bldj.lexiang.utils.HttpConnectionUtil.RequestCallback;
import com.bldj.lexiang.utils.HttpPostEmulator;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.NetUtil;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ThreadUtil;
import com.bldj.lexiang.utils.UploadFileItem;

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
		Map<String, Object> params = new HashMap<String, Object>();
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
	public static void login(Context context, String username, String password,double lon,double lat,
			RequestCallback requestCallBack) {
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
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
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
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
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
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
			String stream, File formFile, RequestCallback requestCallback) {
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.USERNAME, username);
		params.put(ReqUrls.IMG_FILE, stream);
		// Map<String,HttpBodyData> paramsImage = new HashMap<String,
		// HttpBodyData>();
		// paramsImage.put("source", new HttpBodyData(HttpBodyData.TYPE_STRING,
		// "Android"));
		// paramsImage.put("version", new HttpBodyData(HttpBodyData.TYPE_STRING,
		// (String)params.get("version")));
		// paramsImage.put("appname", new HttpBodyData(HttpBodyData.TYPE_STRING,
		// (String)params.get("appname")));
		// paramsImage.put(ReqUrls.USERNAME, new
		// HttpBodyData(HttpBodyData.TYPE_STRING, username));
		// paramsImage.put(ReqUrls.IMG_FILE, new
		// HttpBodyData(HttpBodyData.TYPE_IMAGE,formFile));

		// params.put(ReqUrls.STREAM, stream);
		ApiUtils.getParseModel(params, ReqUrls.UPDATE_HEADER_URL, false,
				requestCallback, MethodType.UPDATE, context);
	}

	/**
	 * 
	 * @param context
	 * @param username
	 * @param stream
	 * @param formFile
	 * @param requestCallback
	 */
	public static void updateHeader2(Context context, final String username,
			final String filePath, final RequestCallback requestCallback) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				switch (message.what) {
				case 1:
					requestCallback.execute(((ParseModel) message.obj));
					break;
				}
			}
		};
		ThreadUtil.getTheadPool(true).submit(new Runnable() {
			@Override
			public void run() {
				ParseModel pm = new ParseModel();
				try {
					// 设定服务地址
					String serverUrl = ReqUrls.UPDATE_HEADER_URL;// 上传地址

					ArrayList<FormFieldKeyValuePair> ffkvp = new ArrayList<FormFieldKeyValuePair>();
					ffkvp.add(new FormFieldKeyValuePair(ReqUrls.USERNAME,
							username));// 其他参数
					ffkvp.add(new FormFieldKeyValuePair("type", "png"));
					// 设定要上传的文件
					ArrayList<UploadFileItem> ufi = new ArrayList<UploadFileItem>();
					ufi.add(new UploadFileItem("formFile", filePath));
					HttpPostEmulator hpe = new HttpPostEmulator();
					String response = hpe.sendHttpPostRequest(serverUrl, ffkvp,
							ufi);
					if (!StringUtils.isEmpty(response)) {
						pm = JsonUtils.fromJson(response, ParseModel.class);
						Message msg = handler.obtainMessage();
						msg.what = 1;
						msg.obj = pm;
						handler.sendMessage(msg);
					}else{
						Message msg = handler.obtainMessage();
						msg.what = 1;
						pm = new ParseModel();
						pm.setMsg(NetUtil.NET_ERR_MSG);
						pm.setStatus(String.valueOf(NetUtil.FAIL_CODE));
						msg.obj = pm;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {

					Thread.yield();
					e.printStackTrace();
					Message msg = handler.obtainMessage();
					msg.what = 1;
					pm = new ParseModel();
					pm.setMsg(NetUtil.NET_ERR_MSG);
					pm.setStatus(String.valueOf(NetUtil.FAIL_CODE));
					msg.obj = pm;
					handler.sendMessage(msg);
				}
			}
		});

	}

	/**
	 * 信息收集接口(意见、招聘、打分)
	 * 
	 * @param context
	 * @param userId
	 *            用户ID(手机号)(可以为空)
	 * @param content
	 *            提出的建议内容
	 * @param type
	 *            0 意见反馈 1招聘 2 评论
	 * @param contactor
	 *            联系人姓名
	 * @param contact
	 *            联系方式(手机 or 邮箱拼接)
	 * @param ordernum
	 *            订单号(点评)
	 * @param productName
	 *            产品名称
	 * @param sellerName
	 *            美容师名称
	 * @param infoType
	 *            0好评 1中评 2差评
	 * @param sellerId
	 *            卖家id
	 * @param requestCallback
	 */
	public static void unifor(Context context, long userId, String content,
			int type, String contactor, String contact, String ordernum,
			String productName, String sellerName, int infoType, long sellerId,
			RequestCallback requestCallback) {
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		if (userId != 0) {
			params.put(ReqUrls.USER_ID, userId);
		}
		params.put("content", content);
		params.put("type", type);
		params.put("contactor", contactor);
		params.put("contact", contact);
		params.put("ordernum", ordernum);
		params.put("productName", productName);
		params.put("sellerName", sellerName);
		if (infoType != 0) {
			params.put("infoType", infoType);
		}
		if (sellerId != 0) {
			params.put("sellerId", sellerId);
		}

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
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
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
			String password, double lon, double lat,
			RequestCallback requestCallback) {
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.PASSWORD, password);
		params.put(ReqUrls.MOBILE, mobile);
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
	public static void addressManager(Context context, int type, long userId,
			String curLocation, String detailAddress, String id,
			RequestCallback requestCallback) {
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put("type", String.valueOf(type));
		switch (type) {
		case 0:// 增加地址
			params.put(ReqUrls.USER_ID, userId);
			params.put("curLocation", curLocation);
			params.put("detailAddress", detailAddress);
			break;
		case 1:// 删除地址
			params.put(ReqUrls.ID, id);
			break;
		case 2:// 修改地址
			params.put(ReqUrls.USER_ID, userId);
			params.put("curLocation", curLocation);
			params.put("detailAddress", detailAddress);
			params.put(ReqUrls.ID, id);
			break;
		case 3:// 查询地址
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
	 * 
	 * @param context
	 * @param mobile
	 * @param code
	 * @param type
	 * @param name
	 * @param requestCallback
	 */
	public static void checkCode(Context context, String mobile, String code,
			String type, String name, RequestCallback requestCallback) {
		Map<String, Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.MOBILE, mobile);
		params.put("code", code);
		params.put("type", type);
		params.put(ReqUrls.NAME, name);
		ApiUtils.getParseModel(params, ReqUrls.CHECK_CODE, false,
				requestCallback, MethodType.UPDATE, context);
	}
	/**
	 * 获取我的档案
	 * @param context
	 * @param id
	 * @param limit
	 * @param dealDate
	 * @param requestCallBack
	 */
	public static void getMyFiles(Context context,long id ,int limit,String dealDate,RequestCallback requestCallBack){
//		/user/getArchives?id=21&dealDate=20141216&limit=3
		Map<String,Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put(ReqUrls.ID, id);
		params.put(ReqUrls.LIMIT, limit);
		params.put("dealDate", dealDate);
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_MY_FILES,false, requestCallBack, MethodType.UPDATE, context);
	}
	/**
	 * 获取验证码
	 * @param context
	 * @param mobile
	 * @param requestCallBack
	 */
	public static void getCode(Context context,String mobile,RequestCallback requestCallBack){
		Map<String,Object> params = HttpClientAddHeaders.getHeaders(context);
		params.put("mobile", mobile);
		ApiUtils.getParseModel(params, ReqUrls.GET_CODE,false, requestCallBack, MethodType.UPDATE, context);
	}

	/**
	 * 获取关于我们的地址、分享的各类文案等等常用信息
	 * @param context
	 * @param requestCallBack
	 */
	public static void getConfParams(Context context,RequestCallback requestCallBack){
		Map<String,Object> params = HttpClientAddHeaders.getHeaders(context);
		ApiUtils.getParseModel(params, ReqUrls.REQUEST_CONFPARAMS,false, requestCallBack, MethodType.UPDATE, context);
	}
	// /**
	// * 获取用户个人中心界面
	// *
	// * @param userId
	// * @return
	// */
	// public static void getUserInfo(Context context,
	// RequestCallback requestCallBack) {
	// Map<String,Object> params = HttpClientAddHeaders.getHeaders(context);
	// // ApiUtils.getParseModel(params, ReqUrls.REQUEST_USER_INFO, false,
	// // requestCallBack,MethodType.USER_PAGE_INFO,null);
	// }
	//
	// /**
	// * 修改用户信息
	// *
	// * @param enterId
	// * @return
	// */
	// public static void modifyUserInfo(Context context, String name,
	// String address, String mobile, RequestCallback requestCallBack) {
	// Map<String,Object> params = HttpClientAddHeaders.getHeaders(context);
	// params.put(ReqUrls.NAME, name);
	// params.put(ReqUrls.MOBILE, mobile);
	// params.put(ReqUrls.ADDRESS, address);
	// ApiUtils.getParseModel(params, ReqUrls.UPDATE_USER_INFO, false,
	// requestCallBack, MethodType.GET_MAINPAGE_AD, null);
	// }
	//
	// /**
	// * 修改用户信息
	// *
	// * @param
	// * @return
	// */
	// public static void downloadRepoInfo(Context context, long appId) {
	// Map<String,Object> params = HttpClientAddHeaders.getHeaders(context);
	// params.put(ReqUrls.APP_ID, String.valueOf(appId));
	// ApiUtils.repo(params, ReqUrls.DOWNLOAD_REPO_INFO, HttpMethod.GET);
	// // ApiUtils.getParseModel(params, ReqUrls.DOWNLOAD_REPO_INFO, false,
	// // null,MethodType.DOWNLOAD_REPO_INFO,null);
	// }
	//
	// //
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// public static String parseLong2Str(long userId) {
	// return String.valueOf(userId);
	// }
}

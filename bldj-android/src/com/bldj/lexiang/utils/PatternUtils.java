package com.bldj.lexiang.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则校验
 * 
 * @author handong
 * @email dong.han1@renren-inc.com
 */
public class PatternUtils {
	
	private static final String sRegistUser = "^[a-zA-Z0-9_\u4e00-\u9fa5]+$";
	private static final String sAllNumber = "^\\d+$";
	private static final String sHanZi = "[\\u4e00-\\u9fa5]";
	private static final String sMac = "[\\w|\\-|:]{1,100}";
	public static final String sFour_HanZi = "^[\u4e00-\u9fa5]{0,4}$"; // 姓氏最多4个汉字
	public static final String sPhone_Num = "^\\d{11}$"; // 电话号码11位数字
	public static final String sPhone_Pattern = "^(13|14|15|18)\\d{9}$"; // 电话号码
	public static final String sEmail_Pattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	public static final String sVefityCode_pattern = "^\\d{4}$";
	
	/**
	 * 汉字、数字、字母和下划线组成,非纯数字
	 * 
	 * @param aUserName
	 * @return
	 */
	public static boolean isRegistUserNameNotValue(String aUserName) {
		boolean mach1 = false;
		boolean mach2 = false;
		Pattern pattern = Pattern.compile(sRegistUser);
		Matcher matcher = pattern.matcher(aUserName);
		mach1 = matcher.matches();
		
		Pattern pattern2 = Pattern.compile(sAllNumber);
		Matcher matcher2 = pattern2.matcher(aUserName);
		mach2 = matcher2.matches();
		
		if (!mach1 || mach2) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isRegistPasswrodNotValue(String aPassword) {
		int pLen = aPassword.trim().length();
		if (pLen == 0) {
			return true;
		}
		Pattern pattern = Pattern.compile(sHanZi);
		Matcher matcher = pattern.matcher(aPassword);
		if (matcher.find()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isMacValue(String aMac) {
		int pLen = aMac.trim().length();
		if (pLen == 0) {
			return true;
		}
		Pattern pattern = Pattern.compile(sMac);
		Matcher matcher = pattern.matcher(aMac);
		if (matcher.find()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isMatch(String aPattern, String aStr) {
		Pattern pattern = Pattern.compile(aPattern);
		Matcher matcher = pattern.matcher(aStr);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean checkEmail(String email) {
		Pattern pattern = Pattern.compile(sEmail_Pattern);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
		
	}
	
	public static boolean checkPhoneNum(String num) {
		Pattern pattern = Pattern.compile(sPhone_Pattern);
		Matcher matcher = pattern.matcher(num);
		return matcher.matches();
	}
	
	// 检查验证码类型
	public static boolean checkCode(String code) {
		Pattern pattern = Pattern.compile(sVefityCode_pattern);
		Matcher matcher = pattern.matcher(code);
		return matcher.matches();
	}
	
}

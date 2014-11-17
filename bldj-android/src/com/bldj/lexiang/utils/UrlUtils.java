package com.bldj.lexiang.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class UrlUtils {
	
	public static boolean stringIsUrl(String aStr) {
		if (aStr == null || "".equals(aStr)) {
			return false;
		}
		if (aStr.startsWith("http") || aStr.startsWith("www")) {
			return true;
		} else {
			return false;
		}
		// return aStr.matches("http://([/w-]+/.)+[/w-]+(/[/w-./?%&=]*)?");
	}
	
	/**
	 * URL编码
	 * 
	 * @param str
	 * @return
	 */
	public static final String encodeURL(String str) {
		try {
			return URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * URL解码
	 * 
	 * @param str
	 * @return
	 */
	public static final String decodeURL(String str) {
		try {
			return URLDecoder.decode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
}

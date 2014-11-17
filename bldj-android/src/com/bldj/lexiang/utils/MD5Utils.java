package com.bldj.lexiang.utils;

import java.security.MessageDigest;

public class MD5Utils {
	
	public static String getMd5(String tag) {
		String md5 = "";
		try {
			String tempMd5 = "";
			md5 += tempMd5;
			byte[] btInput = md5.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			md5 = new String(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5;
	}
	
	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
}

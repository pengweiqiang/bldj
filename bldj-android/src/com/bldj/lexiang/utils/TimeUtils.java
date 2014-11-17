package com.bldj.lexiang.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class TimeUtils {
	
	public static String getCurrentDateTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String date = format.format(new Date());
		
		return date;
	}
	
	public static String getCurrentDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(new Date());
		
		return date;
	}
	
	public static String getDetailDateTime(long timeSec) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date(timeSec);
		return format.format(date);
	}
	
	public static String getDateDay(long timeSec) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(timeSec);
		return format.format(date);
	}
	
	public static String getTimeOfDay(long timeSec) {
		SimpleDateFormat format = new SimpleDateFormat("hh:mm");
		Date date = new Date(timeSec);
		return format.format(date);
	}
	
	public static String dateFormat(long date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
	
	/**
	 * 指定格式，返回当前的系统时间
	 * 
	 * @param format 显示日期的格式
	 * @return
	 */
	public static String dateFormat(String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(System.currentTimeMillis());
	}
	
	/**
	 * 将未指定格式的字符串转换成日期类型
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static long parseStringToDate(String date) throws ParseException {
		Date result = null;
		String parse = date;
		parse = parse.replaceFirst("^[0-9]{4}([^0-9]?)", "yyyy$1");
		parse = parse.replaceFirst("^[0-9]{2}([^0-9]?)", "yy$1");
		parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1MM$2");
		parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}( ?)", "$1dd$2");
		parse = parse.replaceFirst("( )[0-9]{1,2}([^0-9]?)", "$1HH$2");
		parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1mm$2");
		parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1ss$2");
		SimpleDateFormat format = new SimpleDateFormat(parse);
		result = format.parse(date);
		return result.getTime();
	}
	
}

package com.bldj.lexiang.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String CUSTOM_PATTERN = "yyyy-MM-dd HH-mm-ss";
	public static final String FULL_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";
	public static final String TRIM_PATTERN = "yyyyMMddHHmmss";
	public static final String SIMPLY_PATTERN = "HH:mm";
	public static final String SIMPLY_DD_PATTERN = "MM-dd HH:mm";
	public static final String DAY_PATTERN = "yyyy-MM-dd";
	public static final String SIMPLY_HH_PATTERN = "HH";
	public static final String CRITICISM_PATTERN = "yyyy-MM-dd HH:mm";
	public static final String CHINESE_PATTERN = "MM月dd日  HH:mm";
	/**
	 * 获得日期对象
	 * 
	 * @param dateStr 日期字符串，例如：2012-12-22 00:00:00
	 * @param pattern 日期格式
	 * @return dateStr所代表的Date对象
	 * @throws ParseException 
	 * @throws Exception
	 */
	public static Date getDate(String dateStr, String pattern) throws ParseException  {
		SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
		return df.parse(dateStr);
	}
	
	/**
	 *  返回指定样式的时间
	 * @param timeMillis  毫秒值
	 * @param pattern  时间样式
	 * @return
	 */
	public static String getDateStringByMill(long timeMillis , String pattern) {
		Date date = new Date(timeMillis);
		return getDateString(date, pattern);
	}

	/**
	 * 获得日期的格式化字符串
	 * 
	 * @param date 日期对象
	 * @param pattern 日期格式
	 * @return 日期字符串
	 * @throws Exception
	 */
	public static String getDateString(Date date, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
		return df.format(date);
	}

    /**
     * 判断两个时间是否是同一天
     *
     * @param date 日期对象
     * @return 是否是同一天
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if(null == date1 || null == date2) return false;

        SimpleDateFormat df = new SimpleDateFormat(DAY_PATTERN, Locale.getDefault());
        String dateString1 = df.format(date1);
        String dateString2 = df.format(date2);
        return dateString1.equalsIgnoreCase(dateString2);
    }

	/**
	 * 格式化时间字符串
	 * 
	 * @param date 日期对象
	 * @param pattern 日期格式
	 * @return 日期字符串
	 * @throws Exception
	 */
	public static String getDateString(String dateStr, String pattern){
		Date tempDate;
		try {
			tempDate = getDate(dateStr,FULL_PATTERN);
			return getDateString(tempDate,pattern);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 格式化时间字符串
	 * 
	 * @param date 日期对象
	 * @param pattern 日期格式
	 * @return 日期字符串
	 * @throws Exception
	 */
	public static String getDateString(String dateStr,String oldPattern, String pattern){
		if(null == dateStr || "".equals(dateStr)) return "";
		Date tempDate;
		try {
			tempDate = getDate(dateStr,oldPattern);
			return getDateString(tempDate,pattern);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

}

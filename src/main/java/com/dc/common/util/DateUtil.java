package com.dc.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;

/**
 * 
 * @ClassName: DateUtil
 * @Description: 日期处理工具类
 * @author: jinzhaopo
 * @date: 2015-5-18 下午12:52:30
 */
public class DateUtil {
	public static final String DATE_PATTERNS[] = { "yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd",
			"yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss" };

	/**
	 * 
	 * @Title: getDate
	 * @Description: 将传入的字符串变成date
	 * @param text
	 * @return
	 * @return: Date
	 */
	public static Date getDate(String text) {
		Date date = new Date();
		try {
			date = DateUtils.parseDate(text, DATE_PATTERNS);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 
	 * @Title: getString
	 * @Description: 将传入的string 变成date
	 * @param date
	 * @param pattern
	 * @return
	 * @return: String
	 */
	public static String getString(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 
	 * @Title: getString
	 * @Description: 以"yyyy-MM-dd"格式转换date
	 * @param date
	 * @return
	 * @return: String
	 */
	public static String getString(Date date) {
		return getString(date, "yyyy-MM-dd");
	}

	/**
	 * 
	 * @Title: getDateArray
	 * @Description: 给予当前的年日期
	 * @param y
	 * @return
	 * @return: String[]
	 */

	public static String[] getDateArray(int y) {
		String[] DateArrayStr = { ((y) + "-0" + 1 + "-0" + 1).toString(), (y + "-" + 12 + "-" + 31).toString() };
		return DateArrayStr;

	}

	/**
	 * 
	 * @Title: isGetTime
	 * @Description: 判断是否传入开始结束时间
	 * @return //空就返回true
	 * @return: Boolean
	 */
	public static Boolean isGetTimeNull(Map<String, Object> searchMap) {

		if (searchMap.get("startDate") != null && searchMap.get("endDate") != null) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 
	 * @Title: setDate
	 * @Description: 修改时间的操作
	 * @param date
	 * @param calendar
	 * @param param
	 * @return
	 * @return: Date
	 */
	public static Date setDate(Date date, int calendarField, int value) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendarField, calendar.get(calendarField) + value);
		return calendar.getTime();
	}

	/**
	 * 
	 * @Title: setHours
	 * @Description: 修改小时
	 * @param date
	 * @param hours
	 * @return
	 * @return: Date
	 */
	public static Date setAddHours(Date date, int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + hours);
		return calendar.getTime();
	}

	/**
	 * 
	 * @Title: setSubHours
	 * @Description: 减
	 * @param date
	 * @param hours
	 * @return
	 * @return: Date
	 */
	public static Date setSubHours(Date date, int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) - hours);
		return calendar.getTime();
	}

	public static void main(String[] args) {
		System.out.println(setAddHours(new Date(), -3));
	}

	/**
	 * 
	 * @Title: getDate_AddMonth
	 * @Description: 追加日期
	 * @param date
	 * @param month
	 * @return
	 * @return: Date
	 */
	public static Date getDate_AddMonth(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + month);
		return calendar.getTime();
	}

	/**
	 * 
	 * @Title: addYear
	 * @Description: 添加年
	 * @param date
	 * @param year
	 * @return
	 * @return: Date
	 */
	public static Date addYear(Date date, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year);
		return calendar.getTime();
	}

	/**
	 * 
	 * @Title: getDate_SubMonth
	 * @Description: 减日期
	 * @param date
	 * @param month
	 * @return
	 * @return: Date
	 */
	public static Date getDate_SubMonth(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - month);
		return calendar.getTime();
	}

	/**
	 * 
	 * @Title: getNowYearTime
	 * @Description: 得到当前的一年时间
	 * @param searchMap
	 * @return
	 * @return: String[]
	 */
	public static String[] getNowYearTime(Map<String, Object> searchMap) {

		Calendar cal = Calendar.getInstance();
		int y;
		y = cal.get(Calendar.YEAR); // 得到当前的年
		String[] aa = DateUtil.getDateArray(y);
		return aa;

	}

	/**
	 * 
	 * @Title: moveDateDay
	 * @Description: 去掉日期 yyyy-mm-dd 转换 yyyy-mm
	 * @param searchMap
	 * @return
	 * @return: String[]
	 */
	public static String[] moveDateDay(Map<String, Object> searchMap) {

		String startDate = searchMap.get("startDate").toString().substring(0, 7);
		String endDate = searchMap.get("endDate").toString().substring(0, 7);

		String[] aam = { startDate, endDate };

		return aam;

	}

	/**
	 * 毫秒值转化成时间
	 * @param millons
	 * @param pattern
	 * @return
	 */
	public static String parseLongToString(Long millons, String pattern) {
		Date date = new Date(millons);
		return getString(date, pattern);
	}
	
	/**
	 * 判断是否成年
	 * @param lincenceNumber
	 * @return
	 */
	public static boolean isChildren(String lincenceNumber){
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);  
		int month = (cal.get(Calendar.MONTH)) + 1;  
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		
		int birthYear=Integer.parseInt(lincenceNumber.substring(6, 10));
		int birthMonth=Integer.parseInt(lincenceNumber.substring(10, 12));
		int birthDay=Integer.parseInt(lincenceNumber.substring(12, 14));
		
		if(year-birthYear<18){	
			return true;
		}
		else if(year-birthYear==18){	
			if(month-birthMonth<0){	
				return true;
			}
			else if(month-birthMonth==0 && dayOfMonth-birthDay<=0){	
				return true;
			}
		}
		return false;
	}
}

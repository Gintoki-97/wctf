package cn.gin.wctf.common.util;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;


/**
 * 日期时间工具类，继承自commons lang3 下的DateUtils
 * @author Gintoki
 * @version 21017-10-03
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    /* ==============
     * unused
     * ==============
     * 
	 * private static String[] parsePatterns = {
	 * 			"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
	 *			"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
	 *			"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};
	 */
	
	public static final long SECONDS_PER_YEAR = 31536000L;
	
	/**
	 * 得到当前日期字符串的日期，格式 yyyy-MM-dd
	 */
	public static String getDate() {
		return formatDate(new Date());
	}
	
	/**
	 * 得到当前日期字符串的日期，格式为 pattern
	 * @param pattern 字符串的格式
	 */
	public static String getDate(Date date, String pattern) {
		return formatDate(new Date(), pattern);
	}
	
	/**
	 * 得到当前日期字符串的时间，格式为 HH:mm:ss
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}
	
	/**
	 * 得到当前日期字符串的日期时间，格式为 yyyy-MM-dd HH:mm:ss
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 得到当前日期字符串的年份，格式为  yyyy
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}
	
	/**
	 * 得到当前日期字符串的月份，格式为 MM
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}
	
	/**
	 * 得到当前日期字符串的日，格式为 dd
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}
	
	/**
	 * 得到当前日期字符串的周
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 得到当前日期字符串的日，格式为 dd
	 */
	public static String getHours() {
		return formatDate(new Date(), "HH");
	}
	
	/**
	 * 得到当前日期字符串的日，格式为 dd
	 */
	public static String getMinutes() {
		return formatDate(new Date(), "mm");
	}
	
	/**
	 * 得到当前日期字符串的日，格式为 dd
	 */
	public static String getSeconds() {
		return formatDate(new Date(), "ss");
	}
	
	/**
	 * 得到指定日期过去的年数，非准确时间
	 */
	public static long pastYears(Date date) {
		long t = new Date().getTime() - date.getTime();
		
		return t / (12 * 30 * MILLIS_PER_DAY);
	}
	
	/**
	 * 得到指定日期过去的月，非准确时间
	 */
	public static long pastMonths(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (30 * MILLIS_PER_DAY);
	}
	
	/**
	 * 得到指定日期过去的天数
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (MILLIS_PER_DAY);
	}
	
	/**
	 * 得到指定日期过去的小时
	 */
	public static long pastHours(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (MILLIS_PER_HOUR);
	}
	
	/**
	 * 得到指定日期过去的分钟
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (MILLIS_PER_MINUTE);
	}
	
	/**
	 * 得到指定日期过去的秒数
	 */
	public static long pastSeconds(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (MILLIS_PER_SECOND);
	}
	
	/**
	 * 得到指定日期字符串的指定格式
	 * @param pattern 字符串的格式，若为空则使用 yyyy-MM-dd
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * 将字符串按照指定格式转换为日期对象
	 * @param str 需要格式化的字符串
	 * @param pattern 指定的格式
	 */
	public static Date parseDate(String str, String pattern) {
		Date parseDate = null;
		try {
			if (pattern != null) {
				parseDate = org.apache.commons.lang3.time.DateUtils.parseDate(str, pattern);
			} else {
				parseDate = org.apache.commons.lang3.time.DateUtils.parseDate(str, "yyyy-MM-dd");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return parseDate;
	}
	
//	public static void main(String[] args) {
//		String formatDate = DateUtils.formatDate(new Date(), "yyyyMMddHHmmssSSS");
//		System.out.println(DateUtils.formatDate(new Date(1508510929000l), "yyyyMMddHHmmssSSS"));
//	}
}

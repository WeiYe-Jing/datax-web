package com.wugui.datax.admin.util;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 日期转换工具
 * @author lk
 * @date  2018/11/07
 */

public class DateUtil {

	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY_MM = "yyyy-MM";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss:SSS";
	public static final String YYYY_MM_DD_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	/**
	 * 日期格式，年月日，用横杠分开，例如：2006-12-25，2008-08-08
	 */
	public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	/**
	 * 日期格式，年月日，用横杠分开，例如：2006-12-25，2008-08-08
	 */
	public static final String PATTERN_DATE = "yyyy-MM-dd";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String YYYYMM = "yyyyMM";
	public static final String YYMM = "yyMM";
	public static final String YYMMDD = "yyMMdd";

	public static final String HHmmss = "HH:mm:ss";

	/**
	 * 日期格式，年月日时分秒，年月日用横杠分开，时分秒用冒号分开
	 * 例如：2005-05-10 23：20：00，2008-08-08 20:08:08
	 */
	public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS = "yyyy-MM-dd HH:mm:ss";

	public static boolean isValidDate(String str) {
		boolean convertSuccess=true;
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		try {
		    // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(str);
		} catch (ParseException e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess=false;
		}
		return convertSuccess;
	}

	/**
	 * 将字符串根据传入的指定格式的字符串转换为日期
	 *
	 * @author
	 * @date 2016年7月27日 上午10:28:24
	 * @param str 字符串
	 * @param format 转换的日期格式
	 * @return
	 */
	public static Date strToDate(String str, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (Exception e) {

			// System.out.println("输入日期格式错误，返回当前日期时间格式");
			return date;
		}
		return date;
	}
	/**
	 * 获取当前月所在的第一天
	 * @return
	 */
	public static String getMonthFirstDay(){
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
		String first = format.format(c.getTime());

		return first;
	}
    /**
     * 获取本月的第一天
     *
     * @param date
     * @return Date
     * @Title: getFirstDayOfMonth
     * @author wangqinghua
     * @date 2017-1-11 下午5:39:55
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = 1;
        cal.set(year, month, day, 0, 0, 0);
        return cal.getTime();
    }
	// 获取当前月第一天：
	public static String getFirstDayOfMonthTwo(String first) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			//跨年的情况会出现问题哦
			//如果时间为：2016-03-18 11:59:59 和 2016-03-19 00:00:01的话差值为 1
			Date bDate=sdf.parse(first);
			c.setTime(bDate);
		} catch (ParseException e) {
			logger.error("出现异常",e);
		}
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		first = format.format(c.getTime());
		return first;
	}// 获取当月最后一天
	public static String getLastDayOfMonthTwo(String last) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance();
		Calendar c = Calendar.getInstance();
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			//跨年的情况会出现问题哦
			//如果时间为：2016-03-18 11:59:59 和 2016-03-19 00:00:01的话差值为 1
			Date bDate=sdf.parse(last);
			c.setTime(bDate);
		} catch (ParseException e) {
			logger.error("出现异常",e);
		}
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		last = format.format(ca.getTime());
		return last;
	}

	public static long getIntegralTime(String str) {
		if(str.length()<11){
			Calendar cal = Calendar.getInstance();
			try {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				//跨年的情况会出现问题哦
				//如果时间为：2016-03-18 11:59:59 和 2016-03-19 00:00:01的话差值为 1
				Date bDate=sdf.parse(str);
				cal.setTime(bDate);
			} catch (ParseException e) {
				logger.error("出现异常",e);
			}
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.MILLISECOND, 0);
			return cal.getTime().getTime();
		}else{
			return	parseStrToDate(str).getTime();
		}


	}

	public static String getIntegralTimeToStr(String str) {
		if(str.length()<11){
			Calendar cal = Calendar.getInstance();
			try {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				//跨年的情况会出现问题哦
				//如果时间为：2016-03-18 11:59:59 和 2016-03-19 00:00:01的话差值为 1
				Date bDate=sdf.parse(str);
				cal.setTime(bDate);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.MILLISECOND, 0);
			} catch (ParseException e) {
				logger.error("出现异常",e);
			}
			return dateStr4(cal.getTime());
		}else{
			return	str;
		}
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 *
	 * @param date
	 * @return
	 */
	public static String dateStr4(Date date) {
		try{
			return dateStr(date, "yyyy-MM-dd HH:mm:ss");
		}catch (Exception e){
			logger.error("出现异常",e);
		}
		return "";
	}

	public static String getLastIntegralTimeToStr(String str) {
		if(str.length()<11){
			Calendar cal = Calendar.getInstance();
			try {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				//跨年的情况会出现问题哦
				//如果时间为：2016-03-18 11:59:59 和 2016-03-19 00:00:01的话差值为 1
				Date bDate=sdf.parse(str);
				cal.setTime(bDate);
			} catch (ParseException e) {
				logger.error("出现异常",e);
			}
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.MILLISECOND, 0);
			return dateStr4(cal.getTime());
		}else{
			return	str;
		}


	}
	public static long getLastIntegralTime(String str) {
		if(str.length()<11){
			Calendar cal = Calendar.getInstance();
			try {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				//跨年的情况会出现问题哦
				//如果时间为：2016-03-18 11:59:59 和 2016-03-19 00:00:01的话差值为 1
				Date bDate=sdf.parse(str);
				cal.setTime(bDate);
			} catch (ParseException e) {
				logger.error("出现异常",e);
			}
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.MILLISECOND, 0);
			return cal.getTime().getTime();
		}else{
			return	parseStrToDate(str).getTime();
		}


	}
	public static Date getIntegralTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date getLastIntegralTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date getLastSecIntegralTime(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(d.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * @param strDate
	 * @param pattern
	 * @return
	 * @author wangqinghua 2014-12-3
	 */
	public static Date string2Date(String strDate, String pattern) {
		if (StringUtils.isBlank(strDate)|| "null".equals(strDate.toLowerCase())) {
			return string2Date("1971-01-01 00:00:00",YYYY_MM_DD_HH_MM_SS);
		}
		if (pattern == null || pattern.equals("")) {
			pattern = PATTERN_DATE;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = null;

		try {
			date = sdf.parse(strDate);
		} catch (Exception e) {
			logger.error("转换时间出现问题",e);
			return string2Date("1971-01-01 00:00:00",YYYY_MM_DD_HH_MM_SS);
		}
		return date;
	}

	/**
	 * 字符串转为date
	 * @param datestr
	 * @param formatStr
	 * @return
	 */
	public static Date formatStrToDate(String datestr, String formatStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);

		Date date = null;
		try {
			date = sdf.parse(datestr);

		} catch (Exception e) {
			// System.out.println("输入日期格式错误，返回当前日期时间格式");
			return new Date();
		}
		return date;
	}

	/**
	 * 得到前一天转成字符串
	 * @return
	 */
	public static String getYestedayDateToString(){
		Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
		calendar.add(Calendar.DATE, -1);    //得到前一天
		String  yestedayDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		System.out.println(yestedayDate);
		return yestedayDate;
	}

	/**
	 * 得到第二天转成字符串
	 * @return
	 */
	public static String getNextDateToString(Date date){
		//此时打印它获取的是系统当前时间
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		//得到前一天
		calendar.add(Calendar.DATE, 1);
		String  yestedayDate = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(calendar.getTime());
		return yestedayDate;
	}

	/**
	 * 得到第二天
	 * @return
	 */
	public static Date getNextDate(Date date){
		//此时打印它获取的是系统当前时间
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		//得到前一天
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();
	}

	/**
	 * 获取当前时间，并且转换成字符串
	 * @return
	 */
	public static String getCurrentDateToString(){
		//此时打印它获取的是系统当前时间
		Calendar calendar = Calendar.getInstance();
		String  currentDateStr = new SimpleDateFormat("yyyy-MM-dd").format(calendar.get(Calendar.DATE));
		return currentDateStr;
	}


	/**
	 * 获取指定日期，并且转换成字符串
	 * @return
	 */
	public static String getDateToString(Date date){
		if(date==null){
			return null;
		}
		String  dateStr = new SimpleDateFormat(DateUtil.YYYY_MM_DD).format(date);
		return dateStr;
	}

	public static String date2String(Date date,String pattern){
		if(date==null){
			return null;
		}
		String  dateStr = new SimpleDateFormat(pattern).format(date);
		return dateStr;
	}

	/**
	 *根据年 月 获取对应的月份 天数
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDaysByYearMonth(int year, int month) {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		System.out.println("----------" + maxDate);
		return maxDate;
	}

	public static String getCurrentDayToString(int year, int month,int day){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, day);
		String dateStr = df.format(a.getTime());
		return dateStr;
	}


	public static Date getFirstDayOfMonth(int year,int month){
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR,year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		//获取某月最小天数
		int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		//设置日历中月份的最小天数
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);

		//格式化日期
		return cal.getTime();
	}

	/**
	 * 获得该月最后一天
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getLastDayOfMonth(int year,int month) {
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR, year);
		//设置月份
		cal.set(Calendar.MONTH, month - 1);
		//获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		//设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE,59);
		cal.set(Calendar.SECOND,59);
		//格式化日期
		return cal.getTime();
	}

	/***
	 * 传入2019, 1  返回  2019-01-01   2019-02-01 2019-01-31  2018-12-31
	 * @param startyear
	 * @param lastmonth
	 * @return
	 */
	public static Map<String, Object> getTime(int startyear, int lastmonth){
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> map = new HashMap<String, Object>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, startyear);
		calendar.set(Calendar.MONTH, lastmonth-1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date date = calendar.getTime();
		String firstDate = dateFormat2.format(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		date = calendar.getTime();
		String lastDate = dateFormat2.format(date);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, startyear);
		cal.set(Calendar.MONTH, lastmonth-1);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH,1);
		String endDay = dateFormat2.format(cal.getTime());

		Calendar cal2 = Calendar.getInstance();
		cal2.set(Calendar.YEAR, startyear);
		cal2.set(Calendar.MONTH, lastmonth-1);
		cal2.set(Calendar.DAY_OF_MONTH,0);
		String selectDay = dateFormat2.format(cal2.getTime());

		map.put("startTime", firstDate);
		map.put("endTime", endDay);

		map.put("thisMonth", lastDate);
		map.put("lastMonth", selectDay);

		return map;
	}



	public static Date parseDate(String nextDateToString, String pattern) throws ParseException {
		Date date = new SimpleDateFormat(pattern).parse(nextDateToString);
		return date;
	}

	/**
	 * 计算两个日期之间的天数
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
	public static int daysOfTwoDate(String beginDate,String endDate) throws ParseException {
		if (StringUtils.isBlank(beginDate)|| StringUtils.isBlank(endDate)){
			return 0;
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		//跨年的情况会出现问题哦
		//如果时间为：2016-03-18 11:59:59 和 2016-03-19 00:00:01的话差值为 1
		Date bDate=sdf.parse(beginDate);
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(bDate);
		int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
		Date eDate=sdf.parse(endDate);
		aCalendar.setTime(eDate);
		int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
		int num=new BigDecimal(eDate.getTime()).subtract(new BigDecimal(bDate.getTime())).
				divide(new BigDecimal("86400000")).intValue();
		return num;
	}

	public static String plusDay(int num,String nowDate) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date  currdate = format.parse(nowDate);
		System.out.println("现在的日期是：" + currdate);
		Calendar ca = Calendar.getInstance();
		ca.setTime(currdate);
		ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
		currdate = ca.getTime();
		String enddate = format.format(currdate);
		return enddate;
	}

	public static String getBeforeXDaysDate(int days){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -days); //得到前一天
		Date date = calendar.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}
	/**
	 * 日期转换为字符串 yyyy-MM-dd
	 *
	 * @param date
	 * @return
	 */
	public static String dateStr2(Date date) {
		try{
			return dateStr(date, "yyyy-MM-dd");
		}catch (Exception e){
			logger.error("出现异常",e);
		}
		return "";
	}
	/**
	 * 日期转换为字符串 格式自定义
	 *
	 * @param date
	 * @param f
	 * @return
	 */
	public static String dateStr(Date date, String f) {
		if(date == null) {
			return null;
		}
		try {
			SimpleDateFormat format = new SimpleDateFormat(f);
			String str = format.format(date);
			return str;
		} catch (Exception e){
			logger.error("出现异常",e);
		}
		return "";

	}

	/**
	 * 获取一个时间里的所有月份
	 * @param minDate
	 * @param maxDate
	 * @return
	 * @throws Exception
	 */
	public static List<String> getMonthBetween(String minDate, String maxDate) {
		ArrayList<String> result = new ArrayList<String>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");//格式化为年月

			Calendar min = Calendar.getInstance();
			Calendar max = Calendar.getInstance();

			min.setTime(sdf.parse(minDate));
			min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

			max.setTime(sdf.parse(maxDate));
			max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

			Calendar curr = min;
			while (curr.before(max)) {
				result.add(sdf.format(curr.getTime()));
				curr.add(Calendar.MONTH, 1);
			}
		} catch (ParseException e) {
			 logger.error("请求发生异常",e);
		}

		return result;
	}
	/**
	 * 解析两个日期段之间的所有日期
	 * @param beginDateStr 开始日期  ，至少精确到yyyy-MM-dd
	 * @param endDateStr 结束日期  ，至少精确到yyyy-MM-dd
	 * @return yyyy-MM-dd日期集合
	 */
	public static List<String> getDayListOfDate(String beginDateStr, String endDateStr) {
		// 指定要解析的时间格式
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

		// 定义一些变量
		Date beginDate = null;
		Date endDate = null;

		Calendar beginGC = null;
		Calendar endGC = null;
		List<String> list = new ArrayList<String>();

		try {
			// 将字符串parse成日期
			beginDate = f.parse(beginDateStr);
			endDate = f.parse(endDateStr);

			// 设置日历
			beginGC = Calendar.getInstance();
			beginGC.setTime(beginDate);

			endGC = Calendar.getInstance();
			endGC.setTime(endDate);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			// 直到两个时间相同
			while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {

				list.add(sdf.format(beginGC.getTime()));
				// 以日为单位，增加时间
				beginGC.add(Calendar.DAY_OF_MONTH, 1);
			}
			return list;
		} catch (Exception e) {
			logger.error("出现异常",e);
			return null;
		}
	}

	/**
	 * 格式化String时间
	 * @param time String类型时间
	 * @return 格式化后的Date日期
	 */
	public static Date parseStrToDate(String time) {
		String timeFromat="yyyy-MM-dd HH:mm:ss";
		if (time == null || time.equals("")) {
			return null;
		}
		if(time.length()==10){
			timeFromat="yyyy-MM-dd";
		}else if(time.length()==8){
			timeFromat="HH:mm:ss";
		}
		Date date=null;
		try{
			DateFormat dateFormat=new SimpleDateFormat(timeFromat);
			date=dateFormat.parse(time);
		}catch(Exception e){

		}
		return date;
	}
	/**
	 * 格式化String时间
	 * @param time String类型时间
	 * @param timeFromat String类型格式
	 * @return 格式化后的Date日期
	 */
	public static Date parseStrToDate(String time, String timeFromat) {
		if (time == null || time.equals("")) {
			return null;
		}

		Date date=null;
		try{
			DateFormat dateFormat=new SimpleDateFormat(timeFromat);
			date=dateFormat.parse(time);
		}catch(Exception e){

		}
		return date;
	}

	/**
	 * 比较两个日期大小相差毫秒数
	 * @param start
	 * @param end
	 * @return
	 */
	public  static  long compare(String start,String end,String pattern){
		Date startDate=string2Date(start,pattern);
		Date endDate=string2Date(end,pattern);
		return endDate.getTime()-startDate.getTime();
	}


	/**
	 * 根据当前时间，添加或减去指定的时间量。例如，要从当前日历时间减去 5 天，可以通过调用以下方法做到这一点：
	 * add(Calendar.DAY_OF_MONTH, -5)。
	 * @param date 指定时间
	 * @param num  为时间添加或减去的时间天数
	 * @return
	 */
	public static Date getBeforeOrAfterDate(Date date, int num) {
		Calendar calendar = Calendar.getInstance();//获取日历
		calendar.setTime(date);//当date的值是当前时间，则可以不用写这段代码。
		calendar.add(Calendar.DATE, num);
		Date d = calendar.getTime();//把日历转换为Date
		return d;
	}

	public static Integer getRandomNum() {
		Random random = new Random();
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		return Integer.parseInt(list.get(random.nextInt(list.size())));
	}

	/**
	 * 格式化Date时间
	 * @param time Date类型时间
	 * @param timeFromat String类型格式
	 * @return 格式化后的字符串
	 */
	public static String parseDateToStr(Date time, String timeFromat){
		DateFormat dateFormat=new SimpleDateFormat(timeFromat);
		return dateFormat.format(time);
	}


	/**
	 * 获取指定年月的第一天
	 * @param dateIndex
	 * @return
	 */
	public static String getFirstDayOfMonthYYMM(String dateIndex) {
		dateIndex =dateIndex.replace("-","");
		int year = Integer.parseInt(dateIndex.substring(0,4));
		int month = Integer.parseInt(dateIndex.substring(4,6));
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR, year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		//获取某月最小天数
		int firstDay = cal.getMinimum(Calendar.DATE);
		//设置日历中月份的最小天数
		cal.set(Calendar.DAY_OF_MONTH,firstDay);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime())+" 00:00:00";
	}
	/**
	 * 获取指定年月的最后一天
	 * @param dateIndex
	 * @return
	 */
	public static String getLastDayOfMonthYYMM(String dateIndex) {
		dateIndex =dateIndex.replace("-","");
		int year = Integer.parseInt(dateIndex.substring(0,4));
		int month = Integer.parseInt(dateIndex.substring(4,6));
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR, year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		//获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		//设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime())+ " 23:59:59";
	}


	/**
	 * 获取指定年月的第一天
	 * @param dateIndex
	 * @return
	 */
	public static String getFirstDayOfMonth(String dateIndex) {
		dateIndex =dateIndex.replace("-","");
		int year = Integer.parseInt(dateIndex.substring(0,4));
		int month = Integer.parseInt(dateIndex.substring(4,6));
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR, year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		//获取某月最小天数
		int firstDay = cal.getMinimum(Calendar.DATE);
		//设置日历中月份的最小天数
		cal.set(Calendar.DAY_OF_MONTH,firstDay);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}
	/**
	 * 获取指定年月的最后一天
	 * @param dateIndex
	 * @return
	 */
	public static String getLastDayOfMonth(String dateIndex) {
		dateIndex =dateIndex.replace("-","");
		int year = Integer.parseInt(dateIndex.substring(0,4));
		int month = Integer.parseInt(dateIndex.substring(4,6));
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR, year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		//获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		//设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}


	/**
	 * 获取指定时间的那天 23:59:59.999 的时间
	 * @param date
	 * @return
	 */
	public static Date getDayEndTime(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}


	public static void main(String[] args) {
		System.out.println(getMonthBetween("201902","201912"));

	}


}

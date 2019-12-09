package com.netty.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SimpleTimeZone;

//import org.springframework.util.Assert;

/**
 * 日期处理工具类
 * 
 */

public class DateUtil {
	// 默认日期格式
	public static final String DATE_DEFAULT_FORMAT = "yyyy-MM-dd";

	public static final String DATE_NO_HYPHEN = "yyyyMMdd";

	public static final String DATE_WITH_POINT = "yyyy.MM.dd";

	// 默认时间格式
	public static final String DATETIME_DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String TIME_DEFAULT_FORMAT = "HH:mm:ss";

	public static final String[] WEEK_ARRAY = new String[]{"星期一","星期二","星期三","星期四","星期五","星期六","星期日"}; 

	/**
	 * 日期格式化yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static Date formatDate(String date, String format) {
		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 日期格式化format
	 * UTF日期 差8个小时
	 * 
	 * @param date
	 * @return
	 */
	public static Date formatDateUtc(Date date, String format) {
		try {
			DateFormat dateTimeFormat = new SimpleDateFormat(DATETIME_DEFAULT_FORMAT);
			dateTimeFormat.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));//UTC时间
			String strDate =  dateTimeFormat.format(date);
			return new SimpleDateFormat(format).parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 日期格式化yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateFormat(Date date) {
		DateFormat dateFormat = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
		return dateFormat.format(date);
	}

	/**
	 * 日期格式化yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateTimeFormat(Date date) {
		DateFormat dateTimeFormat = new SimpleDateFormat(DATETIME_DEFAULT_FORMAT);
		return dateTimeFormat.format(date);
	}

	/**
	 * 时间格式化
	 * 
	 * @param date
	 * @return HH:mm:ss
	 */
	public static String getTimeFormat(Date date) {
		DateFormat timeFormat = new SimpleDateFormat(TIME_DEFAULT_FORMAT);
		return timeFormat.format(date);
	}

	/**
	 * 日期格式化
	 * 
	 * @param date
	 * @param 格式类型
	 * @return
	 */
	public static String getDateFormat(Date date, String formatStr) {
		if (formatStr.length()>0) {
			return new SimpleDateFormat(formatStr).format(date);
		}
		return null;
	}

	/**
	 * 日期格式化
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateFormat(String date) {
		try {
			DateFormat dateFormat = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
			dateFormat.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));//UTC时间
			return dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 时间格式化
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateTimeFormat(String date) {
		try {
			DateFormat dateTimeFormat = new SimpleDateFormat(DATETIME_DEFAULT_FORMAT);
			return dateTimeFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取当前日期(yyyy-MM-dd)
	 * 
	 * @param date
	 * @return
	 */
	public static Date getNowDate() {
		DateFormat dateFormat = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
		return DateUtil.getDateFormat(dateFormat.format(new Date()));
	}

	/**
	 * 获取当前日期星期一日期
	 * 
	 * @return date
	 */
	public static Date getFirstDayOfWeek() {
		Calendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
		gregorianCalendar.setTime(new Date());
		gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek()); // Monday
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取当前日期星期日日期
	 * 
	 * @return date
	 */
	public static Date getLastDayOfWeek() {
		Calendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
		gregorianCalendar.setTime(new Date());
		gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek() + 6); // Monday
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取日期星期一日期
	 * 
	 * @param 指定日期
	 * @return date
	 */
	public static Date getFirstDayOfWeek(Date date) {
		if (date == null) {
			return null;
		}
		Calendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
		gregorianCalendar.setTime(date);
		gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek()); // Monday
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取日期星期一日期
	 * 
	 * @param 指定日期
	 * @return date
	 */
	public static Date getLastDayOfWeek(Date date) {
		if (date == null) {
			return null;
		}
		Calendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
		gregorianCalendar.setTime(date);
		gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek() + 6); // Monday
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取当前月的第一天
	 * 
	 * @return date
	 */
	public static Date getFirstDayOfMonth() {
		Calendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(new Date());
		gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取当前月的最后一天
	 * 
	 * @return
	 */
	public static Date getLastDayOfMonth() {
		Calendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(new Date());
		gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
		gregorianCalendar.add(Calendar.MONTH, 1);
		gregorianCalendar.add(Calendar.DAY_OF_MONTH, -1);
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取指定月的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfMonth(Date date) {
		Calendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date);
		gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取指定月的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date);
		gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
		gregorianCalendar.add(Calendar.MONTH, 1);
		gregorianCalendar.add(Calendar.DAY_OF_MONTH, -1);
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取日期前一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDayBefore(Date date) {
		Calendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date);
		int day = gregorianCalendar.get(Calendar.DATE);
		gregorianCalendar.set(Calendar.DATE, day - 1);
		return gregorianCalendar.getTime();
	}

	/**
	 * 获取日期后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDayAfter(Date date) {
		Calendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date);
		int day = gregorianCalendar.get(Calendar.DATE);
		gregorianCalendar.set(Calendar.DATE, day + 1);
		return gregorianCalendar.getTime();
	}
	
	/**
	 * 获取当前日期的上周第一天
	 * 
	 * 
	 * @return
	 */
	public static String getBeforeFirstWeekdate(){
		 SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		 Calendar calendar1 = Calendar.getInstance();  
	     int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;  
	     int offset1 = 1 - dayOfWeek;  
	     calendar1.add(Calendar.DATE, offset1 - 7);  
	     String lastBeginDate = format.format(calendar1.getTime());  
	     return lastBeginDate;
	}
	
	/**
	 * 获取当前日期的上周的最后一天
	 * 
	 * 
	 * @return
	 */
	public static String getBeforeLastWeekdate(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
	     Calendar calendar2 = Calendar.getInstance();  
	     int dayOfWeek = calendar2.get(Calendar.DAY_OF_WEEK) - 1;  
	     int offset2 = 7 - dayOfWeek;  
	     calendar2.add(Calendar.DATE, offset2 - 7);  
	     String lastEndDate = format.format(calendar2.getTime());  
	     return lastEndDate;  
	}
	
	/**
	 * 获取当前日期的上个月第一天
	 * 
	 * 
	 * @return
	 */
	public static String getBeforeFirstMonthdate(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return format.format(calendar.getTime());
		//System.out.println("上个月第一天："+format.format(calendar.getTime()));
	}
	
	/**
	 * 获取当前日期的上个月的最后一天
	 * 
	 * 
	 * @return
	 */
	public static String getBeforeLastMonthdate(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		int month=calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return format.format(calendar.getTime());
		//System.out.println("上个月最后一天："+format.format(calendar.getTime()));
	}
	
	/**
	 * 获取当前日期的n个月前的第一天
	 * @param num 相差多少个月份
	 * 
	 * @return
	 */
	public static String getBeforeFirstNumMonthdate(int num){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, -num);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		//System.out.println("n个月第一天："+format.format(calendar.getTime()));
		return format.format(calendar.getTime());
	}
	
	/**
	 *获取当前日期的n个月前的最后一天
	 * @param num 相差多少个月份
	 * 
	 * @return
	 */
	public static String getBeforeLastNumMonthdate(int num){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		int month=calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month-num);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		//System.out.println("n个月最后一天："+format.format(calendar.getTime()));
		return format.format(calendar.getTime());
	}
	
	/**
	 * 获取当前日期的上季度前的第一天
	 * 
	 * 
	 * @return
	 */
	public static String getLastQuarterFirstDate(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();  
		calendar.set(Calendar.MONTH, ((int) calendar.get(Calendar.MONTH) / 3 - 1) * 3);  
		calendar.set(Calendar.DAY_OF_MONTH, 1);  
		return format.format(calendar.getTime());
	}
	
	/**
	 * 获取当前日期的上季度前的最后一天
	 * 
	 * 
	 * @return
	 */
	public static String getLastQuarterEndDate(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();  
		calendar.set(Calendar.MONTH, ((int) calendar.get(Calendar.MONTH) / 3 - 1) * 3 + 2);  
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return format.format(calendar.getTime());
	}
	
	/**
	 * 获取当前季度前的第一天
	 * 
	 * 
	 * @return
	 */
	public static String getQuarterFirstDate(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();  
		calendar.set(Calendar.MONTH, ((int) calendar.get(Calendar.MONTH) / 3) * 3);  
		calendar.set(Calendar.DAY_OF_MONTH, 1);  
		return format.format(calendar.getTime());
	}

	/**
	 * 获取当前年
	 * 
	 * @return
	 */
	public static int getNowYear() {
		Calendar d = Calendar.getInstance();
		return d.get(Calendar.YEAR);
	}

	/**
	 * 获取当前月份
	 * 
	 * @return
	 */
	public static int getNowMonth() {
		Calendar d = Calendar.getInstance();
		return d.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取当月天数
	 * 
	 * @return
	 */
	public static int getNowMonthDay() {
		Calendar d = Calendar.getInstance();
		return d.getActualMaximum(Calendar.DATE);
	}
		
	/**
	 * 得到一个时间延后或前移几天的时间,nowdate(yyyy-mm-dd)为时间,delay为前移或后延的天数
	 * @param compareDate 比较实际
	 * @param delay 移动的天数
	 * @return
	 */
	public static String getNextDay(Date compareDate, int delay) {
		Calendar calendar = new GregorianCalendar();  
		calendar.setTime(compareDate);  
		calendar.add(calendar.DATE,delay);//把日期往后增加delay天.整数往后推,负数往前移动  
		Date date = calendar.getTime(); //这个时间就是日期推移后的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date); 
		return dateString;
	}
	/**
	 * 得到一个时间延后或前移几天的时间,nowdate(yyyy-mm-dd)为时间,delay为前移或后延的天数
	 * @param compareDate 比较实际
	 * @param delay 移动的天数
	 * @return
	 */
	public static int getNextDayInt(Date compareDate, int delay) {
		Calendar calendar = new GregorianCalendar();  
		calendar.setTime(compareDate);  
		calendar.add(calendar.DATE,delay);//把日期往后增加delay天.整数往后推,负数往前移动  
		Date date = calendar.getTime(); //这个时间就是日期推移后的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(date); 
		return Integer.valueOf(dateString);
	}

	/**
	 * 得到一个时间延后或前移几天的时间,nowdate(yy-MM-dd HH:mm:ss)为时间,delay为前移或后延的天数
	 * @param compareDate 比较实际
	 * @param delay 移动的天数
	 * @return
	 */
	public static Date getDateTimeNextDay(Date compareDate, int delay) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(compareDate);
		calendar.add(calendar.DATE,delay);//把日期往后增加delay天.整数往后推,负数往前移动
		return calendar.getTime(); //这个时间就是日期推移后的结果
	}
	/**
	 * 得到一个时间延后或前移几个月的时间,nowdate(yyyy-mm-dd)为时间,delay为前移或后延的天数
	 * @param compareDate 比较实际
	 * @param delay 移动的天数
	 * @return
	 * @throws ParseException 
	 */
	public static Integer getNextMonthDay(int compareDate, int delay) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = new GregorianCalendar();  
		calendar.setTime(formatter.parse(String.valueOf(compareDate)));  
		calendar.add(calendar.MONTH,delay);//把日期往后增加delay天.整数往后推,负数往前移动  
		Date date = calendar.getTime(); //这个时间就是日期推移后的结果
		return Integer.valueOf(formatter.format(date));
	}
	
	/** 
	 * 获取指定时间对应的毫秒数 
	 * @param time "HH:mm:ss" 
	 * @return 
	 */  
	public static long getTimeMillis(String time) {  
	    try {  
	        DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");  
	        DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");  
	        Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);  
	        return curDate.getTime();  
	    } catch (ParseException e) {  
	        e.printStackTrace();  
	    }  
	    return 0;  
	}

	/**
	 * Date转换成LocalDateTime
	 * @param date
	 * @return
     */
	public static LocalDateTime transDateToLocalDateTime(Date date){
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	/**
	 * int转换成LocalDate
	 * @param date
	 * @return
	 */
	public static LocalDate transIntToLocalDate(int date){
		int dateYear = date/10000;
		int dateMonth = (date-dateYear*10000)/100;
		int dateDay = date - dateYear*10000 - dateMonth*100;
		return LocalDate.of(dateYear,dateMonth,dateDay);
	}

	/**
	 * Date转换成LocalTime
	 * @param date
	 * @return
	 */
	public static LocalTime transDateToLocalTime(Date date){
		return transDateToLocalDateTime(date).toLocalTime();
	}

	/**
	 * Date转换成LocalTime
	 * @param date
	 * @return
	 */
	public static LocalDate transDateToLocalDate(Date date){
		return transDateToLocalDateTime(date).toLocalDate();
	}

	/**
	 * 指定时间当天开始时间
	 * @param ars
	 * @throws Exception
	 */
	public static Date getDateStartDate(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	/**
	 * 指定时间结束时间
	 * @param ars
	 * @throws Exception
	 */
	public static Date getDateEndDate(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
	
	/**
	 * 获取当前日期往前前追溯1周，1月，3月，6月，1年
	 * @param dateFlag 日期标识 1-近一周，2-近1月，3-近3月，4-近6月，5-近一年，6-今年以来，7-其他
	 * @param beginDate 开始时间 yyyyMMdd
	 * @return Integer yyyyMMdd
	 * @throws Exception
	 */
	public static int getBeforDate(int beginDate,int dateFlag) throws Exception{
		//Assert.isTrue(beginDate>0,"start date must be >0");
		String dateStr = String.valueOf(beginDate);
		dateStr = dateStr.substring(0,4) + "-" + dateStr.substring(4,6) + "-" + dateStr.substring(6,8) + " 00:00:00";
		Date format = DateUtil.getDateTimeFormat(dateStr);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();
		c.setTime(format);
		switch (dateFlag) {
        case 1://近一周
            c.add(Calendar.DATE, - 7);
            break;
        case 2://近1月
        	c.add(Calendar.MONTH, -1);
            break;
        case 3://近3月
        	c.add(Calendar.MONTH, -3);
            break;
        case 4://近6月
        	c.add(Calendar.MONTH, -6);
            break;
        case 5://近一年
        	c.add(Calendar.YEAR, -1);
            break;
        case 6://今年以来
			Calendar currCal=Calendar.getInstance();
			int currentYear = currCal.get(Calendar.YEAR);
			c.clear();  
			c.set(Calendar.YEAR, currentYear);   
        	break;
        case 7://其他
        	c.add(Calendar.YEAR, -50);
        	break;
        default://参数错误
        	throw new Exception("参数错误");
        }
		return Integer.parseInt(sf.format(c.getTime()));
	}
	/**
	 * 获取当前日期往前前追溯1周，1月，3月，6月，1年
	 * @param dateFlag 日期标识 1-近一周，2-近1月，3-近3月，4-近6月，5-近一年，6-近三年，7-近五年，8-今年以来
	 * @param beginDate 开始时间 date
	 * @return String YYYY-MM-DD
	 * @throws Exception
	 */
	public static String beforDate(Date beginDate,int dateFlag) throws Exception{
		Calendar c = Calendar.getInstance();
		c.setTime(beginDate);
		switch (dateFlag) {
        case 1://近一周
            c.add(Calendar.DATE, - 7);
            break;
        case 2://近1月
        	c.add(Calendar.MONTH, -1);
            break;
        case 3://近3月
        	c.add(Calendar.MONTH, -3);
            break;
        case 4://近6月
        	c.add(Calendar.MONTH, -6);
            break;
        case 5://近一年
        	c.add(Calendar.YEAR, -1);
            break;
        case 6://近三年
        	c.add(Calendar.YEAR, -3);
            break;
        case 7://近五年
        	c.add(Calendar.YEAR, -5);
            break;
        case 8://今年以来
        	c.clear();
        	c.set(Calendar.YEAR,getNowYear());
            break;
        default://参数错误
        	throw new Exception("参数错误");
        }
		DateFormat dateFormat = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
		return dateFormat.format(c.getTime());
	}
	/**
	 * 获取时间毫秒值
	 * @param intDate yyyyMMdd
	 * @return
	 */
	public static Long getMiliTime(int intDate){
		String dateStr = String.valueOf(intDate);
		dateStr = dateStr.substring(0,4) + "-" + dateStr.substring(4,6) + "-" + dateStr.substring(6,8) + " 00:00:00";
		
		return DateUtil.getDateTimeFormat(dateStr).getTime();
	}
	/**
	 * 将int类型的date转成date类型
	 * @param intDate
	 * @return
	 */
	public static Date getFormatIntDate(int intDate){
		String dateStr = String.valueOf(intDate);
		dateStr = dateStr.substring(0,4) + "-" + dateStr.substring(4,6) + "-" + dateStr.substring(6,8) + " 00:00:00";
		return getDateFormat(dateStr);
	}
	/**
	 * 将int类型的date转成date类型
	 * @param intDate
	 * @return
	 */
	public static Date getFormatIntEndDate(int intDate){
		String dateStr = String.valueOf(intDate);
		dateStr = dateStr.substring(0,4) + "-" + dateStr.substring(4,6) + "-" + dateStr.substring(6,8) + " 23:59:59";
		return getDateFormat(dateStr);
	}
	
	/**
	 * 获取基金盘中交易分钟时间
	 * @param date
	 * @return
	 */
	public static List<Date> getDayMi(Date date) {
		List<Date> dateList = new ArrayList<Date>();
	    Calendar tt = Calendar.getInstance();
	    String dateStr = getDateFormat(date, "yyyyMMdd");
		String dateStr1 = dateStr.substring(0,4) + "-" + dateStr.substring(4,6) + "-" + dateStr.substring(6,8) + " 09:30:00";
		Date pz1 = getDateTimeFormat(dateStr1);
	    tt.setTime(pz1);
	    Calendar t2 = Calendar.getInstance();
	    Date zw1 = getDateTimeFormat((dateStr.substring(0,4) + "-" + dateStr.substring(4,6) + "-" + dateStr.substring(6,8) + " 11:30:00"));
	    Date zw2 = getDateTimeFormat((dateStr.substring(0,4) + "-" + dateStr.substring(4,6) + "-" + dateStr.substring(6,8) + " 13:00:00"));
	    //90：30~11：30
    	t2.setTime(zw1);
	    for (;tt.compareTo(t2)<=0; tt.add(Calendar.MINUTE, 1)) {
	    	dateList.add(tt.getTime());
	    }
	    //13：~15：00
	    tt.setTime(zw2);
	    t2.setTime(date);
	    for (;tt.compareTo(t2)<=0; tt.add(Calendar.MINUTE, 1)) {
	    	dateList.add(tt.getTime());
	    }
	    return dateList;
	}
	
	public static int secondDiffer(Date startDate,Date endDate) {
		  long startTime = startDate.getTime();
		  long endTime = endDate.getTime();
		  int c = (int)((endTime - startTime) / 1000);
		  return c;
    }
	
	public static String getCurrentTime() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String date=sdf.format(new Date(System.currentTimeMillis()));
		return date;
	}
	
	public static String getCurrentDate() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String date=sdf.format(new Date(System.currentTimeMillis()));
		return date;
	}
	
	/**
	 * 获取日期 月份偏移
	 * @param date
	 * @param offset
	 * @return
	 */
	public static Date getOffMonth(Date date,int offset){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH,offset);
		return c.getTime();
	
	}
	
	/**
	 * 获取日期 的零点
	 * @param date
	 * @param offset
	 * @return
	 */
	public static Date getStartTime(Date date){
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    return calendar.getTime();
	}
	
	/**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1,Date date2)
    {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }
	
    /**
     * 
     * 获取两个时间间隔天数
     * @param oldDate
     * @param newDate
     * @return
     */
    public static int getIntervalDays(Date oldDate,Date newDate){
        if(oldDate.after(newDate)){
            throw new  IllegalArgumentException("时间先后顺序不对!");
        }
        Calendar can1 = Calendar.getInstance();
        can1.setTime(oldDate);
        Calendar can2 = Calendar.getInstance();
        can2.setTime(newDate);
        int year1 = can1.get(Calendar.YEAR);
        int year2 = can2.get(Calendar.YEAR);
        int days = 0;
        Calendar can = null;
        days -= can1.get(Calendar.DAY_OF_YEAR);
        days += can2.get(Calendar.DAY_OF_YEAR);
        can = can1;
        for (int i = 0; i < Math.abs(year2-year1); i++) {
            days += can.getActualMaximum(Calendar.DAY_OF_YEAR);
            can.add(Calendar.YEAR, 1);
        }
        return days;
    }
    
    /**
     * 获取两个时间间隔的日期
     * @param dBegin
     * @param dEnd
     * @return
     */
    public static List<Date> getDates(Date dBegin, Date dEnd){
		List<Date> lDate = new ArrayList<>();
		lDate.add(dBegin);
		Calendar calBegin = Calendar.getInstance();
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(dEnd);
		while (dEnd.after(calBegin.getTime())){
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(calBegin.getTime());
		}
		return lDate;
	}

	public static void main(String[] args) throws ParseException {
		/*Date test = getDateTimeFormat(("20171208".substring(0,4) + "-" + "20171208".substring(4,6) + "-" + "20171208".substring(6,8) + " 10:30:00"));
		List<Date> dayMi = getDayMi(test);
		for (Date date : dayMi) {
			System.out.println(date);
		}*/
		Date lastDayOfWeek = formatDateUtc(getFormatIntDate(20180701),"yyyy-MM-dd hh:mm:ss");
		System.out.println(lastDayOfWeek);
		Date nowDate = formatDateUtc(getFormatIntDate(20180626),"yyyy-MM-dd hh:mm:ss");
		int intervalDays = getIntervalDays(nowDate,lastDayOfWeek);
		System.out.println(intervalDays);
		
		List<Date> findDates = getDates(nowDate, lastDayOfWeek);
		for (int i = 0; i < findDates.size(); i++) {
			System.out.println(findDates.get(i));
		}
		
	}
}
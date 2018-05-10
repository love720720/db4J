package com.db4J.util;

import org.apache.commons.lang3.StringUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author love720720@163.com
 * @date 2017年5月15日 下午3:35:09
 */
public class DateUtils {
	
	public static final String FORMAT_HMS ="HH:mm:ss";

	public static Calendar getCalendar(){
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
		return calendar;
	}

	public static Date getDate(){
		return getCalendar().getTime();
	}

	public static String formatDate(Date date, String format){
		if (date == null) {
			return StringUtils.EMPTY;
		}
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	
	public static String getCurrentDate() {
		return DateUtils.formatDate(DateUtils.getDate(), FORMAT_HMS);
	}
}

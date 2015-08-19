package com.ihsinformatics.tbr3fieldmonitoring.shared;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class DateTimeUtil
{
	public static final String SQL_DATE = "yyyy-MM-dd";
	public static final String SQL_DATETIME = "yyyy-MM-dd HH:mm:ss";

	public static String getFormattedDate(Date date, String format)
	{
		DateTimeFormat formatter = DateTimeFormat.getFormat(format);
		return formatter.format(date);
	}

	public static boolean isFutureDate(Date value)
	{
		return value.after(new Date());
	}

	public static boolean isPastDate(Date value)
	{
		return value.before(new Date());
	}

	@SuppressWarnings("deprecation")
	public static int compareDateOnly(Date date1, Date date2)
	{
		Date date1Only = new Date(date1.getYear(), date1.getMonth(),
				date1.getDate());
		Date date2Only = new Date(date2.getYear(), date2.getMonth(),
				date2.getDate());
		return date1Only.compareTo(date2Only);
	}

	@SuppressWarnings("deprecation")
	public static int compareTimeOnly(Date date1, Date date2)
	{
		Date time1 = new Date(date1.getYear(), date1.getMonth(),
				date1.getDate(), date1.getHours(), date1.getMinutes(),
				date1.getSeconds());
		Date time2 = new Date(date1.getYear(), date1.getMonth(),
				date1.getDate(), date2.getHours(), date2.getMinutes(),
				date2.getSeconds());
		return time1.compareTo(time2);
	}
}

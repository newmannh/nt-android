package com.syntropy.nationaltravelandroid.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class FormattingUtils {

	private final String DATETIME_FORMAT_STRING;
	private final DateTimeFormatter FORMATTER;
	
	public FormattingUtils(){
		DATETIME_FORMAT_STRING = "YYYY-MM-dd'T'HH:mm:ss'Z'";
		FORMATTER = DateTimeFormat.forPattern(DATETIME_FORMAT_STRING);
	}
	
	public static String getPeriodString(DateTime dt){
		return dt.getHourOfDay()>=12? "PM" : "AM";
	}
	
	public String getHrsMinsString(DateTime dt){
		int hrOfDay = dt.getHourOfDay()%12;
		int minOfHr = dt.getMinuteOfHour();
		return ( (hrOfDay<1? 12 : hrOfDay )+":"+(minOfHr<10? "0"+minOfHr : minOfHr) );
	}
	
	public DateTime parseDateTime(String input){
		return FORMATTER.parseDateTime(input);
	}
	
	public String dateTimeMillisToString(long millis){
		String output = new DateTime(millis).toString(FORMATTER);
		return output;
	}
	
}

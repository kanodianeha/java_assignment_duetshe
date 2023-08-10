package com.duetshe.store.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;  

public class DateUtil {

	private final static String FORMAT =  "dd/MM/yyyy";
	
	public static Date getDateFromString(String date) throws ParseException {
		try {
			return new SimpleDateFormat(FORMAT).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static String getStringFromDate(Date date) {
		DateFormat dateFormat = new SimpleDateFormat(FORMAT);  
		return dateFormat.format(date);  
	}
}

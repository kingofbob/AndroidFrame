package com.template.project.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormats {
	public static String dtFormat = "yyyy-MM-dd HH:mm:ss.SSS";

	public static SimpleDateFormat getServerDateTimeFormat(){
		return new SimpleDateFormat(dtFormat);
	}
	
	public static String getServerDateTimeString(){
		return new SimpleDateFormat(dtFormat).format(new Date());
	}
	
	public static long convertToMilliseconds(String datetime){
		Log.d(DateFormats.class.getSimpleName(), "Datetime: " + datetime);
		
		if (datetime == null) {
			return 0;
		}

		try {
			return new SimpleDateFormat(dtFormat).parse(datetime).getTime();
		} catch (ParseException e) {
			Log.e(DateFormats.class.getSimpleName(), "convertToMilliseconds", e );

		}
		
		return 0;
	}

	public static String convertDateStringToString(String from, String to, String stringDate){
		try {
			Date date = new SimpleDateFormat(from).parse(stringDate);
			return new SimpleDateFormat(to).format(date);
		}catch (ParseException e){
			Log.e(DateFormats.class.getSimpleName(), "convertDateStringToString",e);
		}catch (Exception e){
			Log.e(DateFormats.class.getSimpleName(), "convertDateStringToString",e);
		}

		return "";
	}

	public static String convertDateToString(String format, Date date){

		return new SimpleDateFormat(format).format(date);
	}

	public static Date convertDateStringToDate(String from, String stringDate){
		try {
			Date date = new SimpleDateFormat(from).parse(stringDate);
			return date;
		}catch (ParseException e){
			Log.e(DateFormats.class.getSimpleName(), "convertDateStringToString",e);
		}catch (Exception e){
			Log.e(DateFormats.class.getSimpleName(), "convertDateStringToString",e);
		}

		return new Date();
	}
	public static String printDifference(Date startDate, Date endDate) {

		//milliseconds
		long different = endDate.getTime() - startDate.getTime();

		System.out.println("startDate : " + startDate);
		System.out.println("endDate : " + endDate);
		System.out.println("different : " + different);

		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;

		long elapsedDays = different / daysInMilli;
		different = different % daysInMilli;

		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;

		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;

		long elapsedSeconds = different / secondsInMilli;

		System.out.printf(
				"%d days, %d hours, %d minutes, %d seconds%n",
				elapsedDays,
				elapsedHours, elapsedMinutes, elapsedSeconds);


		if (elapsedDays > 0) {
			String dayOrDays = " days";
			if (elapsedDays == 1){
				dayOrDays = " day";
			}
			return elapsedDays + dayOrDays;
		}

		if (elapsedHours > 0) {
			String name = " hours";
			if (elapsedHours == 1){
				name = " hour";
			}
			String value = elapsedHours + name;
//			if (elapsedMinutes > 0) {
//				value += " and " + elapsedMinutes + " mins";
//			}
			return value;
		}

		if (elapsedMinutes > 0) {
			String name = " mins";
			if (elapsedMinutes == 1){
				name = " min";
			}
			return elapsedMinutes + name;
		}

		return "";

	}


	public static String convertDateBetweenFormat(String inputFormats, String fromDateString, String toDateString){
		String value = "";
		try {
			Date fromDate = new SimpleDateFormat(inputFormats).parse(fromDateString);
			Date toDate = new SimpleDateFormat(inputFormats).parse(toDateString);

			Calendar fromCal = Calendar.getInstance();
			fromCal.setTime(fromDate);

			Calendar toCal = Calendar.getInstance();
			toCal.setTime(fromDate);

			if (fromCal.get(Calendar.YEAR) == toCal.get(Calendar.YEAR)){
				if (fromCal.get(Calendar.MONTH) == toCal.get(Calendar.MONTH)){
					if (fromCal.get(Calendar.DAY_OF_MONTH) == toCal.get(Calendar.DAY_OF_MONTH)){
						value = new SimpleDateFormat("dd MMM yyyy").format(toDate);
					}else{
						value = new SimpleDateFormat("dd").format(fromDate) + " - " + new SimpleDateFormat("dd MMM").format(toDate);
					}

				}else{
					value = new SimpleDateFormat("dd MMM").format(fromDate) + " - " + new SimpleDateFormat("dd MMM").format(toDate);
				}

			}else{
				value = new SimpleDateFormat("dd MMM yyyy").format(fromDate) + " - " + new SimpleDateFormat("dd MMM yyyy").format(toDate);
			}
		}catch (ParseException e){
			Log.e(DateFormats.class.getSimpleName(), "convertDateStringToString",e);
		}catch (Exception e){
			Log.e(DateFormats.class.getSimpleName(), "convertDateStringToString",e);
		}

		return value;
	}
	
}

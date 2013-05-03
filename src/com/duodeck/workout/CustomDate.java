package com.duodeck.workout;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CustomDate extends Date {
	private SimpleDateFormat dateFormatFull = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private SimpleDateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");
	private SimpleDateFormat dateFormatTimeOfDay = new SimpleDateFormat("HH:mm:ss");
	
	public CustomDate getCurrentDateAsCustomDate() 
	{
		return new CustomDate();
	}
	
	public String convertDateToStringFull() {
		return dateFormatFull.format(this);
	}
	public String convertDateToStringYMD() {
		return dateFormatYMD.format(this);
	}
	public String convertDateToStringTimeOfDay() {
		return dateFormatTimeOfDay.format(this);
	}
	
	public CustomDate getDateObjFromString(String dateString) throws java.text.ParseException
	{
		return (CustomDate) dateFormatFull.parse(dateString);
	}
	
	public CustomDate getEarlierDate(CustomDate otherDate) 
	{
		if (this.before(otherDate))
		{
			return this;
		} else {
			return otherDate;
		}
	}
	
	public CustomDate getLaterDate(CustomDate otherDate) 
	{
		if (this.after(otherDate))
		{
			return this;
		} else {
			return otherDate;
		}
	}
}

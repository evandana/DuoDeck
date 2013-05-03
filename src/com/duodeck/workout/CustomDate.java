package com.duodeck.workout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CustomDate extends Date {

	private static final String nullDateString = "0:00";
	private static CustomDate nullDate;

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


	public void setThisDateToDurationBasedOnString(String workoutString)
	{
		String pattern = "duration: (\\d+:\\d\\d)";
		Pattern r = Pattern.compile(pattern);

		String dateString = nullDateString;

		Matcher m = r.matcher(workoutString);
		if (m.find( )) {
			dateString = m.group(1);
		}

		try {
			long milliseconds = dateFormatTimeOfDay.parse(dateString).getTime();
			this.setTime(milliseconds);
		} catch (ParseException e) {
			// set to (CustomDate) 0:00
			this.setTime(0);
		}
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

	public boolean isNullDuration() {
		if (this.getTime() == 0)
		{
			return true;
		} else {
			return false;
		}
	}
}

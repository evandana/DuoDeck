package com.duodeck.workout;

public class Timer {
	private int[] timestamps = {}; // how can i add more items to an array easily?
	private int[] durations = {};  // how can i add more items to an array easily?
	
	
	public int[] getTimestamps() 
	{
		// TODO: convert to readable
		return timestamps;
	}
	public int[] getDurations() 
	{
		// TODO: convert to readable
		return durations;
	}
	
	public int[] addTimestamp() 
	{
		// TODO: get current time
		// TODO: log time into timestamp
		// TODO: calculate time difference
		return durations;
	}
	
	public String convertTimeToReadableFormat(int timeInt) {
		// TODO: format int as time
		return timeInt + "";
	}
}

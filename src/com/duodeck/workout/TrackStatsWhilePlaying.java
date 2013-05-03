package com.duodeck.workout;

public class TrackStatsWhilePlaying {
	// dateStarted:yyyy-mm-dd timeOfDayStarted:hh:mm deckName:string duration:hh:mm s:count,duration h:count,duration d:count,duration c:count,duration map:s=exercise,h=exercise,d=exercise,c=exercise"
	public String dateStarted = "";
	public String timeOfDayStarted = "";
	public String duration = "notFinished";
	
	public InGameStatsBySuit createStatsForExercise()
	{
		return new InGameStatsBySuit();
	}
	
	public class InGameStatsBySuit {
		private int sumCount = 0;
		private int sumDuration = 0;

		// count
		public int incrementCount(int val) 
		{
			this.sumCount += val;
			return this.sumCount; 
		}
		public int setCount(int val) 
		{
			this.sumCount = val;
			return this.sumCount;
		}
		public int getCount() 
		{
			return this.sumCount;
		}
		// duration
		// TODO: implement this
		//		public int incrementDuration(int val) 
		//		{
		//			this.sumDuration += val;
		//			return this.sumDuration; 
		//		}
		//		public int setDuration(int val) 
		//		{
		//			this.sumDuration = val;
		//			return this.sumDuration;
		//		}
		//		public int getDuration() 
		//		{
		//			return this.sumDuration;
		//		}
		// longest run
		// TODO: track the largest number of consecutive reps of the same exercise
		
		
		@Override
		public String toString() // "0,0:00"
		{
			return (this.sumCount + "," + this.sumDuration).toString();
		}
	}

}

package com.duodeck.workout;

/*
 * Heavily influenced by the post by crazyjugglerdrummer
 * http://www.dreamincode.net/forums/topic/110380-deck-of-cards-using-various-methods/
 */
public class Card
{
	private int suit, rank; // exercise is the same as suit

	private static String[] suits =		{ "spades", "clubs", "diamonds", "hearts" };
	private static String[] exercises = { "pushups", "pushups", "situps", "situps" }; //"spades", "hearts", "diamonds", "clubs" 
	private static String[] ranks  = 	{ "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14" };


	Card(int suit, int rank)
	{
		this.rank=rank;
		this.suit=suit;
	}

	public @Override String toString()
	{
		if (rank == -1 || suit == -1) 
		{
			return "The FINISHED CARD";
		} else {
			return ranks[rank] + " " + exercises[suit];
		}
	}

	public String getRank() {
		 return ranks[rank];
	}
	
	public String getExercise() {
		return exercises[suit];
	}
	public int getExerciseAsInt() {
		return suit;
	}
	
	public String getSuit() {
		return suits[suit];
	}
    
	public int getValue()
    {
    	switch(rank)
    	{
    		case -1: return -1;
    		case 0: return 2;
    		case 1: return 3;
    		case 2: return 4;
    		case 3: return 5;
    		case 4: return 6;
    		case 5: return 7;
    		case 6: return 8;
    		case 7: return 9;
    		case 8: return 10;
    		case 9: return 11;
    		case 10: return 12;
    		case 11: return 13;
    		case 12: return 14;
    		default: return 0;
    	}
    }

}


package com.duodeck.workout;

import java.util.ArrayList;
import java.util.Random;

import com.duodeck.workout.TrackStatsWhilePlaying.InGameStatsBySuit;

/*
 * Heavily influenced by the post by crazyjugglerdrummer
 * http://www.dreamincode.net/forums/topic/110380-deck-of-cards-using-various-methods/
 */
public class Deck {
	private ArrayList<Card> cards;
	private int deckSize = 52; // initialize it to expected value, then set dynamically after building the deck

	private Card finishedCard = new Card(-1, -1);

	public String name = "Deck1";
	
	// track stats
	public TrackStatsWhilePlaying inGameStats = new TrackStatsWhilePlaying();

	// TODO: make this dynamic instead of hard coded
	public InGameStatsBySuit pushups = inGameStats.createStatsForExercise();
	public InGameStatsBySuit situps = inGameStats.createStatsForExercise();
	
	Deck()
	{
		cards = new ArrayList<Card>();
		int index_1, index_2;
		Random generator = new Random();
		Card temp;

		// TODO: dynamically set the size of the suits based on the card's available suits
		for (int a=0; a<=3; a++)
		{
			//for (int b=0; b<=12; b++)
			// TODO: uncomment the above line for the full deck 
			for (int b=0; b<=3; b++)
			{
			   cards.add( new Card(a,b) );
			}
		}


		for (int i=0; i<100; i++)
		{
			index_1 = generator.nextInt( cards.size() - 1 );
			index_2 = generator.nextInt( cards.size() - 1 );

			temp = (Card) cards.get( index_2 );
			cards.set( index_2 , cards.get( index_1 ) );
			cards.set( index_1, temp );
		}
		
		deckSize = cards.size();
	}
	
	public String showDeck()
	{
		String s = "Cards left including this: " + getCardsRemaining() + "\n\n";
		for (int i=0; i<cards.size(); i++) 
		{
			Card card = cards.get(i);
			s += i + ": \t" + card.toString() + "\t\t\t\t\t\tvalue" + card.getValue() + "\t\trank: " + card.getRank() + "\n";
		}
		return s;
	}
	
	
	public Card drawFromDeck()
	{	   
		if (cards.size() > 0) 
		{
			return cards.remove( 0 );
		} else {
			return finishedCard;
		}
	}

	public int getCardsRemaining()
	{
		return cards.size();
	}
	
	public int getDeckSize()
	{
		return deckSize;
	}
}


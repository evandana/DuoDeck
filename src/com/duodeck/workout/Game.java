package com.duodeck.workout;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

import com.duodeck.workout.TrackStatsWhilePlaying.InGameStatsBySuit;
import com.example.duodeck.R;

public class Game extends Activity {

	private PersistentStorage ps = new PersistentStorage();
	
	private GameStates gameStates;
	public GameStates currentGameState = GameStates.Solo;
	
	public Deck deck = new Deck();
	public Card currentCard;
	
	// chronometer
	long chronometerTimeWhenStopped = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    
		setContentView(R.layout.solo_deck);

		startChronometer(null);
		
		TextView displayOfCurrentCard = (TextView) findViewById(R.id.display_current_card);
		currentCard = deck.drawFromDeck();
		displayOfCurrentCard.setText("this card: " + currentCard + "\t\t" + deck.getCardsRemaining() + "/" + deck.getDeckSize() );
		
		TextView deckInfo = (TextView) findViewById(R.id.display_deck_info);
		deckInfo.setText(deck.showDeck());
		
	}
	
	@Override 
	protected void onPause(){
        super.onPause();
        
//        SharedPreferences settings = getSharedPreferences(ps.STORAGE_STATS_DECK1,0);
//        SharedPreferences.Editor editor = settings.edit();
//        // Necessary to clear first if we save preferences onPause. 
//        editor.clear();
//        editor.putInt("Metric", mMetric);
//        editor.commit();
    }
	
	
	public void doneWithThisCard(View view) 
	{
		switch (currentGameState) 
		{
			case Solo:
				// record stats about card that was just completed
				switch (currentCard.getExerciseAsInt())
				{
					// TODO: make this dynamic instead of hard coded
					case 0:
						deck.pushups.incrementCount(currentCard.getValue());
						// TODO: add time duration summation
						break;
					// TODO: make this dynamic instead of hard coded
					case 1:
						deck.pushups.incrementCount(currentCard.getValue());
						// TODO: add time duration summation
						break;
					// TODO: make this dynamic instead of hard coded
					case 2:
						deck.situps.incrementCount(currentCard.getValue());
						// TODO: add time duration summation
						break;
					// TODO: make this dynamic instead of hard coded
					case 3:
						deck.situps.incrementCount(currentCard.getValue());
						// TODO: add time duration summation
						break;
				}
				
				// record the time that the card was finished
				
				
				TextView staticTimeValue = (TextView) findViewById(R.id.staticTimeValueDisplay);
				staticTimeValue.setText(((Chronometer) getChronometer()).getText());
				
				if (deck.getCardsRemaining() > 0) 
				{
					// get new card
					// remove the card from the deck of available options
					// display the next card
					// TODO: remove the following line
					System.out.println("done with this card");
		
					TextView deckInfo = (TextView) findViewById(R.id.display_deck_info);
					deckInfo.setText(deck.showDeck());
					
					TextView displayOfCurrentCard = (TextView) findViewById(R.id.display_current_card);
					currentCard = deck.drawFromDeck();
					displayOfCurrentCard.setText(currentCard + "\t\t" + deck.getCardsRemaining() + "/" + deck.getDeckSize() );
					

					deck.inGameStats.duration = ((Chronometer) getChronometer()).getText().toString();
					
//					String statsAsString = ps.makeWorkoutStringForStorage(deck);
//					System.out.println("... " + statsAsString);
					
				} else {
					stopChronometer(null);

//					System.out.println("finished deck");
					// TODO: record finished time
					deck.inGameStats.duration = ((Chronometer) getChronometer()).getText().toString();
					
					String statsAsString = ps.makeWorkoutStringForStorage(deck);
//					System.out.println("stats as string: " + statsAsString);
					ps.saveDataToSharedPrefs(Game.this, ps.KEY_PREVIOUS_DECK, statsAsString);
					
					// neuter the "next card" button
					View buttonNextCard = findViewById(R.id.solo_done_with_this_card);
					buttonNextCard.setOnClickListener(null);
					
					
					
					System.out.println("from STORAGE: " + ps.getDataFromSharedPrefs(Game.this, ps.KEY_PREVIOUS_DECK));
					
					
					// TODO: record deck stats in stats
					
					
					
					// TODO: show stats
					// TODO: show finished time
					// TODO: show summary of deck stats (sum of values per suit)
					// TODO: show summary of deck stats (sum of values per EXERCISE)
				}
	
				break;
				
			case MeWaitingBuddyWorkingOut:
				// TODO: record the time that this was finished
				// TODO: queue a new card (pull it from the deck), but don't show it yet
				// TODO: Notify partner that i'm done and send the card
				// TODO: show "wait" screen
				// TODO: put in "wait" state that can receive a push notification to move forward
				break;
				
			case MeWorkingOutBuddyWaiting:
				// TODO: request the card from buddy
				// TODO: remove this card from my deck
				// TODO: Notify partner that i'm done
				// TODO: start the next card
				break;
				
			default:
				break;
		}
	}
	
	public void saveData(View view) 
	{
//		System.out.println("saving data...");
		EditText inputText = (EditText) findViewById(R.id.editText1);
//		TextView responseText = (TextView) findViewById(R.id.textView1);
//		ps.saveDataToFile(inputText, responseText);
//		"
		ps.saveDataToSharedPrefs(Game.this, ps.KEY_PREVIOUS_DECK, inputText.getText().toString());

//		System.out.println("saved data.");
	}
	
	public void getData(View view)
	{
//		System.out.println("retrieving data...");
		
		EditText inputText = (EditText) findViewById(R.id.editText1);
		TextView responseText = (TextView) findViewById(R.id.textView1);
		String data = "";
//		System.out.println(ps.getDataFromInternalStorage(inputText, responseText, data));

		System.out.println(ps.getDataFromSharedPrefs(Game.this, ps.KEY_PREVIOUS_DECK));

//		System.out.println("retrieved data.");
	}
	
	
	/* 
	 * CHRONOMETER
	 */
	protected Chronometer getChronometer() 
	{
		return ((Chronometer) findViewById(R.id.chronometer1));
	}
	// start
	public void startChronometer(View view) 
	{
		Chronometer mChronometer = getChronometer();
		mChronometer.setBase(SystemClock.elapsedRealtime() + chronometerTimeWhenStopped);
		mChronometer.start();
	}
	// stop
	public void stopChronometer(View view) 
	{
	    getChronometer().stop();
    }
	// pause
	public void pauseChronometer(View view) 
	{
		Chronometer mChronometer = getChronometer();

		chronometerTimeWhenStopped = mChronometer.getBase() - SystemClock.elapsedRealtime();
		mChronometer.stop();
	}
	// resume
	public void resumeChronometer(View view) 
	{
		Chronometer mChronometer = getChronometer();
		
		mChronometer.setBase(chronometerTimeWhenStopped);
		mChronometer.start();

		mChronometer.setBase(SystemClock.elapsedRealtime() + chronometerTimeWhenStopped);
    }
	
	
	
	
	
	
}

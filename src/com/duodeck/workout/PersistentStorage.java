package com.duodeck.workout;

import android.content.Context;

public class PersistentStorage {
	
//	private String FILENAME = "./hello_file.txt";
//	private String string = "hello world!";

    // file for each deck
	public static final String STORAGE_STATS_DECK1 = "StatsDeck1";
	// TODO: add in decks 2 and 3
	//	  public static final String STORAGE_STATS_DECK2 = "StatsDeck2";	
	//    public static final String STORAGE_STATS_DECK3 = "StatsDeck3";
	// temp keys
	//    public static final String KEY_TEMP_SESSION_START_TIMESTAMP = "SessionStartTimestamp"; // allows us to calibrate in the case of interruptions
	//    public static final String KEY_TEMP_SESSION_START_TIMER_VALUE = "SessionStartTimerValue"; // allows us to calibrate in the case of interruptions
    // keys to use per deck

    // keys to use for summing counts per exercise
    // TODO: map suits to exercise
	//    public static final String KEY_SUM_VALUE_SPADES = "SumValueSpades";
	//    public static final String KEY_SUM_VALUE_HEARTS = "SumValueHearts";
	//    public static final String KEY_SUM_VALUE_DIAMONDS = "SumValueDiamonds";
	//    public static final String KEY_SUM_VALUE_CLUBSS = "SumValueClubs";
    // TODO: track time per suit
	//    public static final String KEY_SUM_VALUE_SPADES = "SumValueSpades";
	//    public static final String KEY_SUM_VALUE_HEARTS = "SumValueHearts";
	//    public static final String KEY_SUM_VALUE_DIAMONDS = "SumValueDiamonds";
	//    public static final String KEY_SUM_VALUE_CLUBSS = "SumValueClubs";
    
    // dateStarted:yyyy-mm-dd timeOfDayStarted:hh:mm deckName:string duration:hh:mm s:count,duration h:count,duration d:count,duration c:count,duration map:s=exercise,h=exercise,d=exercise,c=exercise"
    public String makeWorkoutStringForStorage(Deck deck) 
    {
    	TrackStatsWhilePlaying stats = deck.inGameStats;
    	
    	String s = "";
    	String sp = " "; String cm = ","; String cl = ":";
    	
    	s+= "dateStarted:" + stats.dateStarted.convertDateToStringYMD() + sp;
    	s+= "timeOfDayStarted:" + stats.dateStarted.convertDateToStringTimeOfDay() + sp;
    	s+= "deckName:" + deck.name + sp;
    	s+= "duration:" + stats.duration + sp;
    	s+= "pushups:" + deck.pushups.toString() + cm;
    	s+= "situps:" + deck.situps.toString() + sp;
    	
    	return s;
    }
    
    
    
    
    public void saveWorkoutDataToSharedPrefs(Context context, StatKeys key, String data) 
	{ 	// We need an Editor object to make preference changes. All objects are from android.context.Context
    	switch (key) 
		{
			case DecksCompleted:
				context.getSharedPreferences(STORAGE_STATS_DECK1, 0).edit().putString(key.toString(), data).commit();
				break;
			case FirstDeck:
				context.getSharedPreferences(STORAGE_STATS_DECK1, 0).edit().putString(key.toString(), data).commit();
				break;
			case DateSinceLastDeck:
				context.getSharedPreferences(STORAGE_STATS_DECK1, 0).edit().putString(key.toString(), data).commit();
				break;
			case PreviousDeck:
				context.getSharedPreferences(STORAGE_STATS_DECK1, 0).edit().putString(key.toString(), data).commit();
				break;
			case FastestDeck:
				context.getSharedPreferences(STORAGE_STATS_DECK1, 0).edit().putString(key.toString(), data).commit();
				break;
			case CumulativePushups:
				context.getSharedPreferences(STORAGE_STATS_DECK1, 0).edit().putString(key.toString(), data).commit();
				break;
			case CumulativeSitups:
				context.getSharedPreferences(STORAGE_STATS_DECK1, 0).edit().putString(key.toString(), data).commit();
				break;
			default:
				break;
		}
        context.getSharedPreferences(STORAGE_STATS_DECK1, 0).edit().putString(key.toString(), data).commit();
	}
	
	public String getWorkoutDataFromSharedPrefs(Context context, StatKeys key)
	{
		String s = "defaultString";
		switch (key) 
		{
			case DecksCompleted:
				s = context.getSharedPreferences(STORAGE_STATS_DECK1, 0).getString(StatKeys.DecksCompleted.toString(), "");
				break;
			case FirstDeck:
				s = context.getSharedPreferences(STORAGE_STATS_DECK1, 0).getString(StatKeys.FirstDeck.toString(), "");
				break;
			case DateSinceLastDeck:
				s = context.getSharedPreferences(STORAGE_STATS_DECK1, 0).getString(StatKeys.DateSinceLastDeck.toString(), "");
				break;
			case PreviousDeck:
				s = context.getSharedPreferences(STORAGE_STATS_DECK1, 0).getString(StatKeys.PreviousDeck.toString(), "");
				break;
			case FastestDeck:
				s = context.getSharedPreferences(STORAGE_STATS_DECK1, 0).getString(StatKeys.FastestDeck.toString(), "");
				break;
			case CumulativePushups:
				s = context.getSharedPreferences(STORAGE_STATS_DECK1, 0).getString(StatKeys.CumulativePushups.toString(), "");
				break;
			case CumulativeSitups:
				s = context.getSharedPreferences(STORAGE_STATS_DECK1, 0).getString(StatKeys.CumulativeSitups.toString(), "");
				break;
			default:
				break;
		}
		return s;
	}




	public void updateStatsWithNewDeck(Context context, Deck deck) {
		for (int i=0; i<StatKeys.values().length; i++)
		{
			StatKeys key = StatKeys.values()[i];
			String fromStorage = "";
			String data; // from current deck
			int val;
			switch (key) 
			{
				case DecksCompleted:
					fromStorage = "0" + context.getSharedPreferences(STORAGE_STATS_DECK1, 0).getString(StatKeys.DecksCompleted.toString(), "");
					val = Integer.parseInt(fromStorage) + 1;
					context.getSharedPreferences(STORAGE_STATS_DECK1, 0).edit().putString(key.toString(), val + "").commit();
					break;
				case FirstDeck:
					fromStorage = context.getSharedPreferences(STORAGE_STATS_DECK1, 0).getString(StatKeys.FirstDeck.toString(), "");
					if (fromStorage.equalsIgnoreCase("")) 
					{
						data = makeWorkoutStringForStorage(deck);
						context.getSharedPreferences(STORAGE_STATS_DECK1, 0).edit().putString(key.toString(), data).commit();
					}
					break;
				case DateSinceLastDeck: // TODO: implement this
					fromStorage = context.getSharedPreferences(STORAGE_STATS_DECK1, 0).getString(StatKeys.DateSinceLastDeck.toString(), "");
					context.getSharedPreferences(STORAGE_STATS_DECK1, 0).edit().putString(key.toString(), fromStorage).commit();
					break;
				case PreviousDeck:
					data = makeWorkoutStringForStorage(deck);
					context.getSharedPreferences(STORAGE_STATS_DECK1, 0).edit().putString(key.toString(), data).commit();
					break;
				case FastestDeck:
					fromStorage = context.getSharedPreferences(STORAGE_STATS_DECK1, 0).getString(StatKeys.FastestDeck.toString(), "");
					data = makeWorkoutStringForStorage(deck);
					// TODO: compare with previous deck
					context.getSharedPreferences(STORAGE_STATS_DECK1, 0).edit().putString(key.toString(), data).commit();
					break;
				case CumulativePushups:
					fromStorage = "0" + context.getSharedPreferences(STORAGE_STATS_DECK1, 0).getString(StatKeys.CumulativePushups.toString(), "");
					val = Integer.parseInt(fromStorage) + deck.pushups.getCount();
					context.getSharedPreferences(STORAGE_STATS_DECK1, 0).edit().putString(key.toString(), val + "").commit();
					break;
				case CumulativeSitups:
					fromStorage = "0" + context.getSharedPreferences(STORAGE_STATS_DECK1, 0).getString(StatKeys.CumulativeSitups.toString(), "");
					val = Integer.parseInt(fromStorage) + deck.situps.getCount();
					context.getSharedPreferences(STORAGE_STATS_DECK1, 0).edit().putString(key.toString(), val + "").commit();
					break;
				default:
					break;
			}
		}
	}
    
//    public void saveDataToFile(EditText inputText, TextView responseText) 
//	{
//
//		try {
//			FileOutputStream fos = new FileOutputStream(myInternalFile);
//			fos.write(inputText.getText().toString().getBytes());
//			fos.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		inputText.setText("");
//		responseText
//		.setText("MySampleFile.txt saved to Internal Storage...");
//	}
//	
//	public String getDataFromInternalStorage(EditText inputText, TextView responseText, String data)
//	{
//		try {
//			FileInputStream fis = new FileInputStream(myInternalFile);
//			DataInputStream in = new DataInputStream(fis);
//			BufferedReader br = 
//					new BufferedReader(new InputStreamReader(in));
//			String strLine;
//			while ((strLine = br.readLine()) != null) {
//				data = data + strLine;
//			}
//			in.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		inputText.setText(data);
//		responseText.setText("MySampleFile.txt data retrieved from Internal Storage..." + data);
//		return data;
//	}
}

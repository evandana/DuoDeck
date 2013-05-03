package com.duodeck.workout;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

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
    public static final String KEY_PREVIOUS_DECK = "DurationOfPreviousDeck";
	//    public static final String KEY_FIRST_DATE = "FirstWorkoutDate";
	//    public static final String KEY_FASTEST_TIME = "FastestTime";
	//    public static final String KEY_SLOWEST_TIME = "SlowestTime"; // use for showing improvement only
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
    	
    	s+= "dateStarted:" + stats.dateStarted + sp;
    	s+= "timeOfDayStarted:" + stats.timeOfDayStarted + sp;
    	s+= "deckName:" + deck.name + sp;
    	s+= "duration:" + stats.duration + sp;
    	s+= "pushups:" + deck.pushups.toString() + cm;
    	s+= "situps:" + deck.situps.toString() + sp;
    	
    	return s;
    }
    
    
    
    
    public void saveDataToSharedPrefs(Context context, String key, String data) 
	{ 	// We need an Editor object to make preference changes. All objects are from android.context.Context
        context.getSharedPreferences(STORAGE_STATS_DECK1, 0).edit().putString(key, data).commit();
	}
	
	public String getDataFromSharedPrefs(Context context, String key)
	{
		return context.getSharedPreferences(STORAGE_STATS_DECK1, 0).getString(key, "");
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

package com.duodeck.workout;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.duodeck.workout.R;


public class StatsActivity extends Activity {

    private ListView listViewStats;
    
    private DuoDeckApplication duoDeckApp;
    private PersistentStorage ps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    	duoDeckApp = (DuoDeckApplication) getApplication();
    	ps = duoDeckApp.getPersistentStorage();
        
        setContentView(R.layout.activity_stats);
        
        Stats stats_data[] = new Stats[]
        {
            new Stats(R.drawable.victory_icon_r1_c1, "Decks Completed", ps.getWorkoutDataFromSharedPrefs(StatsActivity.this, StatKeys.DecksCompleted)),
//            new Stats(R.drawable.victory_icon_r1_c5, "Date Since Last Deck", ps.getWorkoutDataFromSharedPrefs(StatsActivity.this, StatKeys.DateSinceLastDeck)),
            new Stats(R.drawable.victory_icon_r1_c7, "Previous Deck", ps.getWorkoutDataFromSharedPrefs(StatsActivity.this, StatKeys.PreviousDeck)),
            new Stats(R.drawable.victory_icon_r3_c1, "Fastest Deck", ps.getWorkoutDataFromSharedPrefs(StatsActivity.this, StatKeys.FastestDeck)),
            new Stats(R.drawable.victory_icon_r3_c3, "Cumulative Pushups", ps.getWorkoutDataFromSharedPrefs(StatsActivity.this, StatKeys.CumulativePushups)),
            new Stats(R.drawable.victory_icon_r3_c5, "Cumulative Situps", ps.getWorkoutDataFromSharedPrefs(StatsActivity.this, StatKeys.CumulativeSitups)),
            new Stats(R.drawable.victory_icon_r1_c3, "First Deck", ps.getWorkoutDataFromSharedPrefs(StatsActivity.this, StatKeys.FirstDeck))
        };
        
        StatsAdapter adapter = new StatsAdapter(this, 
                R.layout.activity_stats_row, stats_data);
        
        
        listViewStats = (ListView)findViewById(R.id.statsActivityListView);
         
        View header = (View)getLayoutInflater().inflate(R.layout.activity_stats_header, null);
        listViewStats.addHeaderView(header);
        
        listViewStats.setAdapter(adapter);
    }
}
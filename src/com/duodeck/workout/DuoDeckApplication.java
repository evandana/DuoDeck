package com.duodeck.workout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class DuoDeckApplication extends Application {

	public static final String ACCOUNT_NAME = "username";
	public static final String ACCOUNT_TOKEN = "oauth_token";
	public static final String USER_STATUS = "offline";
	public static final String FULL_JID = "full_jid";
	
	private String username;
	private String token;
	
	private boolean isServiceRunning = false;
	private boolean isAccountsetup = false;
	private boolean isConnected = false;
	private GameStates currentGameState = GameStates.Solo;
	private HashMap<String, String> contactList = new HashMap<String, String>();
	private SharedPreferences settings;
	
	/**
	 * Constructor
	 */
	public DuoDeckApplication(){
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		username = settings.getString(DuoDeckApplication.ACCOUNT_NAME, "");
		token = settings.getString(DuoDeckApplication.ACCOUNT_TOKEN, "");
		isAccountsetup = !TextUtils.isEmpty(username) && !TextUtils.isEmpty(token);
		startService(new Intent(DuoDeckApplication.this, DuoDeckService.class));
	}
	
	@Override
	public void onTerminate() {
		// Its ok even if this method is not called, since we could reuse the service
		stopService(new Intent(DuoDeckApplication.this, DuoDeckService.class));
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String uname) {
		username = uname;
		Editor edit = settings.edit();
		edit.putString(ACCOUNT_NAME, uname);
		edit.commit();
		isAccountsetup = !TextUtils.isEmpty(username) && !TextUtils.isEmpty(token);
		System.out.println("Got " + uname + " and " + isAccountsetup);
	}
	
	public String getAuthToken() {
		return token;
	}
	
	public void setAuthToken(String authToken) {
		token = authToken;
		Editor edit = settings.edit();
		edit.putString(ACCOUNT_TOKEN, authToken);
		edit.commit();
		isAccountsetup = !TextUtils.isEmpty(username) && !TextUtils.isEmpty(token);
		System.out.println("Got " + authToken + " and " + isAccountsetup);
	}

	public boolean isServiceRunning() {
		return isServiceRunning;
	}

	public void setServiceRunning(boolean isServiceRunning) {
		this.isServiceRunning = isServiceRunning;
	}

	public boolean isAccountsetup() {
		return isAccountsetup;
	}

	public void setAccountsetup(boolean isAccountsetup) {
		this.isAccountsetup = isAccountsetup;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public GameStates getCurrentGameState() {
		return currentGameState;
	}

	public void setCurrentGameState(GameStates currentState) {
		this.currentGameState = currentState;
	}

	public SharedPreferences getSettings() {
		return settings;
	}

	public void setSettings(SharedPreferences settings) {
		this.settings = settings;
	}

	public ArrayList<String> getContactList() {
		HashSet<String> cList = new HashSet<String>();
		for (String JID : this.contactList.keySet()) {
			cList.add(this.contactList.get(JID));
		}
		return new ArrayList<String>(cList);
	}

	public void updateContactList(String JID, String user) {
		this.contactList.put(JID, user);
	}
	
	
}

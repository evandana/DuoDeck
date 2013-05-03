package com.duodeck.workout.xmpp;

import java.io.IOException;
import java.util.ArrayList;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.duodeck.workout.DuoDeckApplication;
import com.duodeck.workout.DuoDeckService;
import com.example.duodeck.R;

public class WorkoutWithBuddyActivity extends Activity {

	private AccountManager accountManager;
	private Account[] accounts = null;
	private Account accSelected = null;
	private int authAttempt = 0;
	
	private Messenger mService = null;
	private DuoDeckApplication duoDeckApp;
	private TextView labelDisplay;
	private ListView listView;
	private ArrayList<String> contactList;
	
	final Messenger mMessenger = new Messenger(new HandleMessage());
	
	class HandleMessage extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case DuoDeckService.MSG_LOGIN:
				if (msg.arg1 == 0) {
					System.out.println("auth failed received at Activity");
					if (authAttempt <= 1) {
						getAuthToken(accSelected, true);
						authAttempt += 1;
					} else {
						labelDisplay.setText("Authentication Failed.");
						authAttempt = 0;
					}
				} else {
					sendMsgToService(DuoDeckService.MSG_GET_ROSTER);
				}
				break;
			case DuoDeckService.MSG_GET_ROSTER:
				displayRoster();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}
	
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = new Messenger(service);
			sendMsgToService(DuoDeckService.MSG_REGISTER);
		}
		
		@Override
		public void onServiceDisconnected(ComponentName className) {
			mService = null;
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bindService(new Intent(this, DuoDeckService.class), mConnection, Context.BIND_AUTO_CREATE);
		setContentView(R.layout.activity_workout_with_buddy);
		duoDeckApp = (DuoDeckApplication) getApplication();
		
		labelDisplay = (TextView) findViewById(R.id.wwb_displayLabel);
		listView = (ListView) findViewById(R.id.wwb_buddyList);
		this.contactList = duoDeckApp.getContactList();

		accountManager = AccountManager.get(getApplicationContext());
		accounts = accountManager.getAccountsByType("com.google");
		if (!duoDeckApp.isAccountsetup()) {
			this.chooseAccount(accounts);
		} else {
			for(Account a : accounts) {
				if (a.name.equals(duoDeckApp.getUsername())) {
					this.accSelected = a;
					break;
				}
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mService == null)
			bindService(new Intent(this, DuoDeckService.class), mConnection, Context.BIND_AUTO_CREATE);
		
		//System.out.println("Listview's bottom: " + listView.getBottom());
		listView.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_workout_with_buddy, R.id.wwb_displayLabel, contactList));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
				// TODO Auto-generated method stub
				System.out.println("Selected " + pos);
			}
			
		});
		
		this.getAuthToken(accSelected, false);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		sendMsgToService(DuoDeckService.MSG_UNREGISTER);
		unbindService(mConnection);
	}
	
	private void sendMsgToService(int type) {
		Message msg = Message.obtain(null, type);
		msg.replyTo = mMessenger;
		try {
			if (mService != null)
				mService.send(msg);
			else
				System.out.println("No service available");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void chooseAccount(final Account[] accounts) {
		String[] accNames = new String[accounts.length];
		for (int i = 0; i < accounts.length; i++) 
			accNames[i] = accounts[i].name;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.wwb_chooseAccount)
			   .setItems(accNames, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					duoDeckApp.setUsername(accounts[which].name);
					accSelected = accounts[which];
				}
			});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void getAuthToken(Account account, boolean invalidate) {
		Bundle options = new Bundle();
		String authTokenType = "oauth2:https://www.googleapis.com/auth/googletalk";
		if (invalidate) {
			accountManager.invalidateAuthToken("com.google", duoDeckApp.getAuthToken());
			System.out.println("Invalidating AuthToken");
		}
		accountManager.getAuthToken(account, authTokenType, options, this,
				new AccountManagerCallback<Bundle>() {
					@Override
					public void run(AccountManagerFuture<Bundle> future) {
						Bundle bundle;
						try {
							bundle = future.getResult();
							String authtoken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
							duoDeckApp.setAuthToken(authtoken);
							sendMsgToService(DuoDeckService.MSG_LOGIN);
							System.out.println("Done calling MSG_LOGIN");
						} catch (OperationCanceledException e) {
							System.out.println("User denied authorization");
						} catch (AuthenticatorException e) {
							System.out.println("Error when authorizing");
						} catch (IOException e) {
							System.out.println("Network error when trying to get authToken");
						}
						
					}
			
				}
				, null);
	}
	
	private void displayRoster() {
		contactList = duoDeckApp.getContactList();
		if (contactList.size() == 0)
			labelDisplay.setText("Currently no contacts are available online");
		else 
			labelDisplay.setText("Online Buddies");
	}
}

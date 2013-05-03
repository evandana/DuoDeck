package com.duodeck.workout;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.duodeck.workout.service.DuoDeckConnectionListener;
import com.duodeck.workout.service.DuoDeckConnectionManager;

public class DuoDeckService extends Service implements DuoDeckConnectionListener {

	public static final int MSG_REGISTER = 1;
	public static final int MSG_UNREGISTER = 2;
	public static final int MSG_LOGIN = 3;
	public static final int MSG_GET_ROSTER = 4;
	public static final int MSG_INVITE = 5;
	public static final int MSG_INVITE_RESPONSE = 6;
	public static final int MSG_SEND_SHUFFLED_DECK = 7;
	public static final int MSG_SHUFFLED_DECK_RESPONSE = 8;
	public static final int MSG_DONE_WITH_CARD_INDEX = 9;
		
	private DuoDeckApplication duoDeckApp;
	private Messenger sClient;
	private DuoDeckConnectionManager duoDeckConnection;
	final Messenger sMessenger = new Messenger(new HandleMessage());
	
	class HandleMessage extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_REGISTER:
				output("Register client");
				if (sClient == null)
					sClient = msg.replyTo;
				else
					System.out.println("There is already a client registered");
				break;
			case MSG_UNREGISTER:
				if (sClient != null)
					sClient = null;
				else
					System.out.println("No registered client present");
				output("Unregistering client");
				break;
			case MSG_LOGIN:
				connect();
				break;
			case MSG_GET_ROSTER:
				duoDeckConnection.getOnlineContacts();
				break;
			case MSG_INVITE:
				break;
			//case MSG_CANCEL_INVITE:
				//break;
			//case MSG_WORKOUT_SEND:
				//break;
			default:
				super.handleMessage(msg);
			}
		}
	}
	
	private void output(String val) {
		System.out.println(val);
	}
	
	
	
	private void connect() {
		if (duoDeckConnection == null || !duoDeckConnection.getUsername().equals(duoDeckApp.getUsername())) {
			System.out.println("Connecting to xmpp");
			duoDeckConnection = DuoDeckConnectionManager.initiate(getApplicationContext(), duoDeckApp.getUsername(), 
							duoDeckApp.getAuthToken(), this);
		} else {
			this.loginResponse(true);
		}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("Starting service..");
		duoDeckApp = (DuoDeckApplication) getApplication();
		duoDeckApp.setServiceRunning(true);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		duoDeckApp.setServiceRunning(true);
		return Service.START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return sMessenger.getBinder();
	}
	
	@Override
	public void onDestroy() {
		//disconnect xmpp stream
		duoDeckApp.setServiceRunning(false);
		super.onDestroy();
	}
	
	private void sendMsgToClient(int type, int arg1, int arg2) {
		try {
			sClient.send(Message.obtain(null, type, arg1, arg2));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void loginResponse(boolean success) {
		if (success) {
			this.sendMsgToClient(MSG_LOGIN, 1, 1);
		} else {
			System.out.println("Calling auth Failed");
			this.sendMsgToClient(MSG_LOGIN, 0, 0);
		}
	}
	
	@Override
	public void getRosterResponse() {
		this.sendMsgToClient(MSG_GET_ROSTER, 1, 1);
	}
	
	@Override
	public void errorReported(Exception e) {
		// TODO Auto-generated method stub
		e.printStackTrace();
	}

	@Override
	public void invite(String fromJID) {
		// TODO Auto-generated method stub
		System.out.println("Got Invite from :" + fromJID);
	}



	@Override
	public void inviteResponse(String fromJID, boolean accepted) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void processShuffledDeck(String fromJID) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void shuffledDeckResponse(String fromJID, boolean success) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void dockWithCardIndex(String fromJID, int seq, int ackSeq) {
		// TODO Auto-generated method stub
		
	}
	
}

package com.duodeck.workout.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import com.duodeck.workout.DuoDeckApplication;

public class DuoDeckConnectionManager implements MessageListener, ChatManagerListener {

	private static final String DEFAULT_XMPP_SERVER = "talk.google.com";
	private static final int DEFAULT_XMPP_PORT = 5222;
	private static final String APP_CHAT_RESOURCE = "duo-deck-xmpp";
	
	private String username;
	private String authToken;
	private Context appContext;
	private DuoDeckConnectionListener listener;
	private Account currentAccount;
	
	private static DuoDeckConnectionManager instance;
	private Connection xmppConnection;
	private ConnectionConfiguration xmppConf;
	
	private DuoDeckSession session;
	
	private DuoDeckConnectionManager(Context appContext, String username, String token, DuoDeckConnectionListener listener) {
		this.appContext = (DuoDeckApplication) appContext;
		this.username = username;
		this.authToken = token;
		this.listener = listener;
	}
	
	public static DuoDeckConnectionManager initiate(Context appContext, String username, String token, DuoDeckConnectionListener listener){
		System.out.println("Inside initate");
		try {
			if (instance != null) {
				instance.disconnect();
				instance = null;
			}
			instance = new DuoDeckConnectionManager(appContext, username, token, listener);
			instance.connect();
			
		} finally {//catch (XMPPException e) {
			//e.printStackTrace();
			//instance = null;
		}
		return instance;
	}
	
	public static DuoDeckConnectionManager getInstance() {
		return instance;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public DuoDeckSession.Buddy getBuddy() {
		return session.getBuddy();
	}
	
	protected void initConfig() {
		SASLAuthentication.registerSASLMechanism(SASLGoogleByOAuth.MECHANISM_NAME, SASLGoogleByOAuth.class);
		SASLAuthentication.supportSASLMechanism(SASLGoogleByOAuth.MECHANISM_NAME);
		xmppConf = new ConnectionConfiguration(DEFAULT_XMPP_SERVER, DEFAULT_XMPP_PORT);
		System.out.println("Service name:" + StringUtils.parseServer(username));
		System.out.println("Token :" + authToken);
		xmppConf.setServiceName(StringUtils.parseServer(this.username));
		xmppConf.setSASLAuthenticationEnabled(true);
	}
	
	protected void connect() {
		this.initConfig();
		AccountManager accountManager = AccountManager.get(this.appContext);
		for(Account a : accountManager.getAccountsByType("com.google")) {
			if (a.name.equals(this.username)) {
				this.currentAccount = a;
				break;
			}
		}
		if (this.currentAccount == null) return;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					xmppConnection = new XMPPConnection(xmppConf);
					xmppConnection.connect();
					xmppConnection.login(username, authToken, APP_CHAT_RESOURCE);
					xmppConnection.getChatManager().addChatListener(instance);
					System.out.println(xmppConnection.getUser());
					((DuoDeckApplication) appContext).setConnected(true);
					listener.loginResponse(true);
				} catch(XMPPException e) {
					e.printStackTrace();
					((DuoDeckApplication) appContext).setConnected(false);
					listener.loginResponse(false);
				}
			}
		}).start();
	}
	
	private void disconnect() {
		if (session != null) {
			session.close(this);
			session = null;
		}
		if (xmppConnection != null) {
			xmppConnection.disconnect();
			xmppConnection = null;
		}
		listener = null;
	}
	
	public void setDuoDeckConnectionListener(DuoDeckConnectionListener listener) {
		this.listener = listener;
	}
	
	public static boolean isInitiated() {
		return instance != null;
	}
	
	public boolean isConnected() {
		return xmppConnection != null && xmppConnection.isConnected();
	}
	
	public void getOnlineContacts() {
		ArrayList<String> contactList = new ArrayList<String>();
		if (xmppConnection != null) {
			Roster roster = xmppConnection.getRoster();
			roster.addRosterListener(new RosterListener() {

				@Override
				public void entriesAdded(Collection<String> arg0) { }

				@Override
				public void entriesDeleted(Collection<String> arg0) { }

				@Override
				public void entriesUpdated(Collection<String> arg0) { }

				@Override
				public void presenceChanged(Presence presence) {
					// TODO Auto-generated method stub
					System.out.println("Presence changed:" + presence.getFrom() + " " + presence);
					String JID = presence.getFrom();
					String user = StringUtils.parseName(JID);
					((DuoDeckApplication) appContext).updateContactList(JID, user);
					System.out.println("JID: " + JID + " , user: " + user);
					listener.getRosterResponse();
				}
				
			});
			/*for (RosterEntry entry : roster.getEntries()) {
				String user = entry.getUser();
				System.out.println("Contact:" + user + " presence: " + roster.getPresence(user).getType().toString());
				if (roster.getPresence(user).getType() == Presence.Type.available)
					contactList.add(user);	
			}*/
		}
	}
	
	public void acceptInvite() throws IOException, XMPPException {
		DuoDeckMessage.create(DuoDeckMessage.MessageType.InviteResponse, Boolean.TRUE.toString()).send(session);
	}
	
	public void declineInvite() throws IOException, XMPPException {
		DuoDeckMessage.create(DuoDeckMessage.MessageType.InviteResponse, Boolean.FALSE.toString()).send(session);
		if (session != null) {
			session.close(this);
			session = null;
		}
	}
	
	public void startSession(String user) throws IOException, XMPPException {
		Chat c = xmppConnection.getChatManager().createChat(user, this);
		session = new DuoDeckSession(c, this.username);
	}
	
	@Override
	public void chatCreated(Chat chat, boolean created) {
		// TODO Auto-generated method stub
		if (!created) {
			session = new DuoDeckSession(chat, username);
			chat.addMessageListener(this);
			System.out.println("Chat session created");
		}
	}

	@Override
	public synchronized void processMessage(Chat chat, Message message) {
		// TODO Auto-generated method stub
		if (listener != null) {
			try {
				DuoDeckMessage properties = DuoDeckMessage.fromMessageString(message.getBody());
				switch(properties.getType()) {
				case Invite:
					this.notifyInvite(properties);
					break;
				case InviteResponse:
					String user = properties.getProperty(DuoDeckMessage.MessageKey.User);
					boolean accepted = properties.getBooleanProperty(DuoDeckMessage.MessageKey.Response);
					if (!accepted) {
						session.buddyDeclined(user);
						session.close(this);
					} else {
						session.buddyAccepted(user);
					}
					break;
				case SendShuffledDeck:
					System.out.println("Received shuffled deck");
					break;
				case ShuffledDeckResponse:
					System.out.println("Received shuffled deck response");
					break;
				case DoneWithCardIndex:
					System.out.println("Done with Card index");
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
				listener.errorReported(e);
			}
		}
	}
	
	private void notifyInvite(DuoDeckMessage prop) {
		String fromJID = prop.getProperty(DuoDeckMessage.MessageKey.User);
		if (listener != null)
			listener.invite(fromJID);
	}
	
	public static void close() {
		if (isInitiated()) {
			instance.disconnect();
			instance.xmppConnection = null;
			instance = null;
		}
	}

}

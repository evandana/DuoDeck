package com.duodeck.workout.service;

import java.io.IOException;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

public class DuoDeckSession {

	private Chat chat;
	private String username;
	private Buddy buddy;
	
	public DuoDeckSession(Chat chat, String username) {
		this.chat = chat;
		this.username = username;
		this.buddy = new Buddy();
	}
	
	public void sendInvite() throws IOException, XMPPException {
		DuoDeckMessage.create(DuoDeckMessage.MessageType.Invite, username)
					  .send(this);
	}
	
	public void initBuddySession(String fromUser) {
		this.buddy.setUsername(fromUser);
		this.buddy.setAccepted();
	}
	
	public void sendMessage(DuoDeckMessage message) throws XMPPException, IOException {
		sendMessage(message.put(DuoDeckMessage.MessageKey.User, username).toMessageString());
	}
	
	public void sendMessage(String message) throws XMPPException {
		chat.sendMessage(message);
	}
	
	public Buddy getBuddy() {
		return buddy;
	}
	
	public void buddyAccepted(String user) {
		if (buddy.getUsername().equals(user))
			buddy.setAccepted();
	}
	
	public void buddyDeclined(String user) {
		if (buddy.getUsername().equals(user)) 
			buddy.setDeclined();
	}
	
	public void close(DuoDeckConnectionManager conn) {
		chat.removeMessageListener(conn);
	}
	
	public static class Buddy {
		private String username;
		private boolean accepted;
		
		public String getUsername() {
			return this.username;
		}
		
		public void setUsername(String uname) {
			this.username = uname;
		}

		public boolean isAccepted() {
			return accepted;
		}
		
		public void setAccepted() {
			this.accepted = true;
		}
		
		public void setDeclined() {
			this.accepted = false;
		}
	}
	
}

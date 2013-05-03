package com.duodeck.workout.service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

import org.jivesoftware.smack.XMPPException;

public class DuoDeckMessage {

	Properties properties = new Properties();
	
	public static DuoDeckMessage create(String name, String value) {
		DuoDeckMessage duoDeckMsg = new DuoDeckMessage();
		duoDeckMsg.properties.put(name, value);
		return duoDeckMsg;
	}
	
	public static DuoDeckMessage create(DuoDeckMessage.MessageType key, String value) {
		return create(key.name(), value);
	}
	
	public DuoDeckMessage put(String name, String value) {
		properties.setProperty(name, value);
		return this;
	}
	
	public DuoDeckMessage put(MessageKey key, String value) {
		return put(key.name(), value);
	}
	
	public String toMessageString() throws IOException {
		StringWriter out = new StringWriter();
		properties.store(out, "DuoDeck-Stanza");
		return out.toString();
	}
	
	public static DuoDeckMessage fromMessageString(String msg) throws IOException {
		DuoDeckMessage prop = new DuoDeckMessage();
		prop.properties.load(new StringReader(msg));
		return prop;
	}
	
	public void send(DuoDeckSession session) throws IOException, XMPPException {
		session.sendMessage(this);
	}
	
	public String getProperty(MessageKey key) {
		return properties.getProperty(key.name());
	}
	
	public MessageType getType() {
		String str = getProperty(MessageKey.MessageType);
		if (str != null) {
			try {
				return MessageType.valueOf(str);
			} catch (IllegalArgumentException e) {
				System.out.println("Not a leagl type: " + str + ": " + e.getMessage());
				return null;
			}
		} else 
			return null;
	}
	
	public boolean getBooleanProperty(MessageKey key) {
		String val = getProperty(key);
		return Boolean.TRUE.toString().equalsIgnoreCase(val);
	}
	
	public static enum MessageKey {
		MessageType, User, Response
	}
	
	public static enum MessageType {
		Invite, InviteResponse, SendShuffledDeck, ShuffledDeckResponse, DoneWithCardIndex
	}
	
}

package com.duodeck.workout.service;

public interface DuoDeckConnectionListener {

	void errorReported(Exception e);
	
	void loginResponse(boolean success);
	
	void getRosterResponse();
	
	void invite(String fromJID);
	
	void inviteResponse(String fromJID, boolean accepted);
	
	void processShuffledDeck(String fromJID);
	
	void shuffledDeckResponse(String fromJID, boolean success);
	
	void dockWithCardIndex(String fromJID, int seq, int ackSeq);
	
}

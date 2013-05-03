package com.duodeck.workout.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.harmony.javax.security.auth.callback.CallbackHandler;
import org.apache.harmony.javax.security.sasl.SaslException;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.util.Base64;

import de.measite.smack.Sasl;

public class SASLGoogleByOAuth extends SASLMechanism {

	public static final String MECHANISM_NAME = "X-OAUTH2";
	
	public SASLGoogleByOAuth(final SASLAuthentication saslAuth) {
		super(saslAuth);
	}
	
	@Override
	protected String getName() {
		// TODO Auto-generated method stub
		return MECHANISM_NAME;
	}
	
    @Override
    public void authenticate(String username, String host, String password) throws IOException, XMPPException {
      
        this.authenticationId = username;
        this.password = password;
        this.hostname = host;

        String[] mechanisms = {"PLAIN" };
        Map<String, String> props = new HashMap<String, String>();
        sc = Sasl.createSaslClient(mechanisms, username, "xmpp", host, props, this);
        authenticate();
    }

    @Override
    public void authenticate(String username, String host, CallbackHandler cbh) throws IOException, XMPPException {
        String[] mechanisms = {"PLAIN" };
        Map<String, String> props = new HashMap<String, String>();
        sc = Sasl.createSaslClient(mechanisms, username, "xmpp", host, props, cbh);
        authenticate();
    }

    @Override
    protected void authenticate() throws IOException, XMPPException {
        String authText = null;
        try {
            if (sc.hasInitialResponse()) {
                byte[] response = sc.evaluateChallenge(new byte[0]);
                authText = Base64.encodeBytes(response, Base64.DONT_BREAK_LINES);
            }
        } catch (SaslException e) {
            throw new XMPPException("SASL authentication failed", e);
        }

        getSASLAuthentication().send(new GoogleOAuth(authText));
    }

	
    public static class GoogleOAuth extends Packet {
        private final String authText;

        public GoogleOAuth(final String authText) {
            this.authText = authText;
        }

        @Override
        public String toXML() {
            StringBuilder stanza = new StringBuilder();
            stanza.append("<auth mechanism=\"").append(MECHANISM_NAME);
            stanza.append("\" xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\" "
                    + "auth:service=\"oauth2\" "
                    + "xmlns:auth=\"http://www.google.com/talk/protocol/auth\">");
            if (authText != null
                    && authText.trim().length() > 0) {
                stanza.append(authText);
            }
            stanza.append("</auth>");
            return stanza.toString();
        }
    }
	
	
}

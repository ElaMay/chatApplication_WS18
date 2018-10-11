package edu.hm.dako.lwtrt.listener;

import edu.hm.dako.lwtrt.pdu.LWTRTPdu;

public interface SessionListener {

	public void onDataEvent(LWTRTPdu lwtrtPdu);
	public void onConnectEvent(LWTRTPdu lwtrtPdu);
	public void onDisconnectEvent(LWTRTPdu lwtrtPdu);
}

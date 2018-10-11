package edu.hm.dako.lwtrt.statemachine;

import edu.hm.dako.lwtrt.pdu.LWTRTPdu;

public class Sending implements LWTRTState {

    private LWTRTStatemachine statemachine;
    
    public Sending(LWTRTStatemachine statemachine) {
        this.statemachine = statemachine;
    }

    @Override
    public LWTRTPdu connect() throws IncorrectTransitionException {
        throw new IncorrectTransitionException(this, "connect");
    }

    @Override
    public LWTRTPdu connectAcpt() throws IncorrectTransitionException {
        throw new IncorrectTransitionException(this, "connectAcpt");
    }

    @Override
    public LWTRTPdu connectReq() throws IncorrectTransitionException {
    	throw new IncorrectTransitionException(this, "connectReq");
    }

    @Override
    public LWTRTPdu connectRsp() throws IncorrectTransitionException {
    	throw new IncorrectTransitionException(this, "connectRsp");
    }

    @Override
    public LWTRTPdu disConnect() throws IncorrectTransitionException {
        throw new IncorrectTransitionException(this, "connectRsp");
    }

    @Override
    public LWTRTPdu disConnectAcpt() throws IncorrectTransitionException {
        throw new IncorrectTransitionException(this, "disConnectAcpt");
    }

    @Override
    public LWTRTPdu disConnectReq() throws IncorrectTransitionException {
        throw new IncorrectTransitionException(this, "connectRsp");
    }

    @Override
    public LWTRTPdu disConnectRsp() throws IncorrectTransitionException {
        throw new IncorrectTransitionException(this, "disConnectRsp");
    }

    @Override
    public LWTRTPdu timeout() throws IncorrectTransitionException {
    	//TODO: 2 Wiederholungen nach Timeout
    	return null;
    }

    @Override
    public LWTRTPdu data(Object chatPdu) throws IncorrectTransitionException {
        throw new IncorrectTransitionException(this, "data");
    }

    @Override
    public LWTRTPdu dataReq(LWTRTPdu lwtrtPdu) throws IncorrectTransitionException {
        return null;
    }

    @Override
    public LWTRTPdu dataRsp(LWTRTPdu lwtrtPdu) throws IncorrectTransitionException {
        //TODO: Wechseln in Zustand Established
        return null;
    }

    public String toString() {
        return "SENDING";
    }
}

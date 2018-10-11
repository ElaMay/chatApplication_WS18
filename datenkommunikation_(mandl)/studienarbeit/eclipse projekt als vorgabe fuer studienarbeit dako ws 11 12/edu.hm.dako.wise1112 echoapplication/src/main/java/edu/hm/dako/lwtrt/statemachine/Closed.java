package edu.hm.dako.lwtrt.statemachine;

import edu.hm.dako.lwtrt.pdu.LWTRTPdu;

public class Closed implements LWTRTState {

    private LWTRTStatemachine statemachine;

    public Closed(LWTRTStatemachine statemachine) {
        this.statemachine = statemachine;
    }

    @Override
    public LWTRTPdu connect() throws IncorrectTransitionException {
        statemachine.setZustand(statemachine.connecting);
        return new LWTRTPdu(LWTRTPdu.OPID_CONNECT_REQ,statemachine.getNextSendingSeqNumber());
    }

    @Override
    public LWTRTPdu connectAcpt() throws IncorrectTransitionException {
        statemachine.setZustand(statemachine.accepting);
        return null;
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
    	throw new IncorrectTransitionException(this, "disConnect");
    }

    @Override
    public LWTRTPdu disConnectAcpt() throws IncorrectTransitionException {
    	throw new IncorrectTransitionException(this, "disConnectAcpt");
    }

    @Override
    public LWTRTPdu disConnectReq() throws IncorrectTransitionException {
    	throw new IncorrectTransitionException(this, "disConnectReq()");
    }

    @Override
    public LWTRTPdu disConnectRsp() throws IncorrectTransitionException {
    	throw new IncorrectTransitionException(this, "disConnectRsp()");
    }
 
    @Override
    public LWTRTPdu timeout() throws IncorrectTransitionException {
        throw new IncorrectTransitionException(this, "timeout");
    }
    
    @Override
    public LWTRTPdu data(Object chatPdu) throws IncorrectTransitionException {
        throw new IncorrectTransitionException(this, "data");
    }

    @Override
    public LWTRTPdu dataReq(LWTRTPdu lwtrtPdu) throws IncorrectTransitionException {
    	throw new IncorrectTransitionException(this, "dataReq");
    }

    @Override
    public LWTRTPdu dataRsp(LWTRTPdu lwtrtPdu) throws IncorrectTransitionException {
        throw new IncorrectTransitionException(this, "dataRsp");
    }

    public String toString() {
        return "CLOSED";
    }
}

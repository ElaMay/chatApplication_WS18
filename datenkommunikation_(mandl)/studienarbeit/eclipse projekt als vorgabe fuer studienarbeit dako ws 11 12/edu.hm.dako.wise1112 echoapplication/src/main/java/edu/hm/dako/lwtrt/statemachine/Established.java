package edu.hm.dako.lwtrt.statemachine;

import edu.hm.dako.lwtrt.pdu.LWTRTPdu;

public class Established implements LWTRTState {

    private LWTRTStatemachine statemachine;

    public Established(LWTRTStatemachine statemachine) {
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
        statemachine.setZustand(statemachine.disconnecting);
        return new LWTRTPdu(LWTRTPdu.OPID_DISCONNECT_REQ, statemachine.getNextSendingSeqNumber());
    }

    @Override
    public LWTRTPdu disConnectAcpt() throws IncorrectTransitionException {
        throw new IncorrectTransitionException(this, "disConnectAcpt");
    }

    @Override
    public LWTRTPdu disConnectReq() throws IncorrectTransitionException {
        statemachine.setZustand(statemachine.wait1);
        return new LWTRTPdu(LWTRTPdu.OPID_DISCONNECT_RSP,statemachine.getNextSendingSeqNumber());
    }

    @Override
    public LWTRTPdu disConnectRsp() throws IncorrectTransitionException {
        throw new IncorrectTransitionException(this, "disConnectRsp");
    }

    @Override
    public LWTRTPdu timeout() throws IncorrectTransitionException {
        throw new IncorrectTransitionException(this, "timeout");
    }

    @Override
    public LWTRTPdu data(Object chatPdu) throws IncorrectTransitionException {
        //TODO: In den Zusand Sending wechseln
        LWTRTPdu lwtrtPdu = new LWTRTPdu(LWTRTPdu.OPID_DATA_REQ, statemachine.getNextSendingSeqNumber());
        lwtrtPdu.setUserData(chatPdu);
        return lwtrtPdu;
    }

    @Override
    public LWTRTPdu dataReq(LWTRTPdu lwtrtPdu) throws IncorrectTransitionException {
    	return new LWTRTPdu(LWTRTPdu.OPID_DATA_RSP, lwtrtPdu.getSequenceNumber());
    }

    @Override
    public LWTRTPdu dataRsp(LWTRTPdu lwtrtPdu) throws IncorrectTransitionException {
        throw new IncorrectTransitionException(this, "dataRsp");
    }

    public String toString() {
        return "ESTABLISHED";
    }
}

package edu.hm.dako.lwtrt.statemachine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.lwtrt.pdu.LWTRTPdu;

public class LWTRTStatemachine implements LWTRTState {

     private static Log log = LogFactory.getLog(LWTRTStatemachine.class);


    protected LWTRTState closed;
    protected LWTRTState connecting;
    protected LWTRTState accepting;
    protected LWTRTState established;
    protected LWTRTState disconnecting;
    protected LWTRTState wait1;
    protected LWTRTState wait2;
    protected LWTRTState sending;

    private LWTRTState actState;
    private boolean initialClosed;


    public LWTRTStatemachine() {
        closed = new Closed(this);
        connecting = new Connecting(this);
        accepting = new Accepting(this);
        established = new Established(this);
        disconnecting = new Disconnecting(this);
        wait1 = new Wait1(this);
        wait2 = new Wait2(this);
        sending = new Sending(this);

        actState = closed;
        initialClosed = true;

        log.debug("New Statemachine: " + actState.toString());
    }
    
    public String actStat() {
    	return actState.toString();
    }

    /**
     * Der aktuelle Zustand wird auf den uebergebenen Zustand gesetzt.
     * <p>
     * Das Zustands-Log wird entsprechend um ein Element erweitert.
     * 
     * @param zustand
     */
    protected synchronized void setZustand(LWTRTState state) {
        log.debug("StateTransition:   " + actState.toString() + "  =>  " + state.toString());
        actState = state;
        initialClosed = false;
    }


    /**
     * Der Zustandsautomat prueft die eingehende PDU (OpId und ResultCode) und leitet daraus entsprechende
     * Zustandsuebergaenge ab, die dann ausgefuehrt werden.
     * 
     * @param pdu zu pruefende PDU
     * @return Vorbereitet Antwort-PDU oder <code>null</code> wenn nicht geantwortet wird.
     * @throws IncorrectTransitionException wenn der Zustandsuebergang nicht vorgesehen ist
     */
    public LWTRTPdu processPDU(LWTRTPdu lwtrtPdu) throws IncorrectTransitionException {
        switch (lwtrtPdu.getOpId()) {
        case LWTRTPdu.OPID_CONNECT_REQ:
            return connectReq();
        case LWTRTPdu.OPID_CONNECT_RSP:
            return connectRsp();
        case LWTRTPdu.OPID_DISCONNECT_REQ:
            return disConnectReq();
        case LWTRTPdu.OPID_DISCONNECT_RSP:
            return disConnectRsp();
        case LWTRTPdu.OPID_DATA_REQ:
            return dataReq(lwtrtPdu);
        case LWTRTPdu.OPID_DATA_RSP:
            return dataRsp(lwtrtPdu);
        }
        return null;
    }

    @Override
    public LWTRTPdu connect() throws IncorrectTransitionException {
        return actState.connect();
    }

    @Override
    public LWTRTPdu connectAcpt() throws IncorrectTransitionException {
        return actState.connectAcpt();
    }

    @Override
    public LWTRTPdu connectReq() throws IncorrectTransitionException {
        return actState.connectReq();
    }

    @Override
    public LWTRTPdu connectRsp() throws IncorrectTransitionException {
        return actState.connectRsp();
    }

    @Override
    public LWTRTPdu disConnect() throws IncorrectTransitionException {
        return actState.disConnect();
    }

    @Override
    public LWTRTPdu disConnectAcpt() throws IncorrectTransitionException {
        return actState.disConnectAcpt();
    }

    @Override
    public LWTRTPdu disConnectReq() throws IncorrectTransitionException {
        return actState.disConnectReq();
    }

    @Override
    public LWTRTPdu disConnectRsp() throws IncorrectTransitionException {
        return actState.disConnectRsp();
    }

    @Override
    public LWTRTPdu timeout() throws IncorrectTransitionException {
        return actState.timeout();
    }

    @Override
    public synchronized LWTRTPdu data(Object chatPdu) throws IncorrectTransitionException {
        LWTRTPdu lwtrtPdu = actState.data(chatPdu);
        return lwtrtPdu;
    }

    @Override
    public LWTRTPdu dataReq(LWTRTPdu lwtrtPdu) throws IncorrectTransitionException {
    	return actState.dataReq(lwtrtPdu);
    }
    
    public synchronized long getNextSendingSeqNumber(){
    	//TODO: laufende Sequenznummer vergeben
    	return 1;
    }
    @Override
    public LWTRTPdu dataRsp(LWTRTPdu lwtrtPdu) throws IncorrectTransitionException {
    	return actState.dataRsp(lwtrtPdu);
    }

    public boolean isClosed() {
        return actState instanceof Closed;
    }

    public boolean isConnecting() {
        return actState instanceof Connecting;
    }

    public boolean isAccepting() {
        return actState instanceof Accepting;
    }

    public boolean isEstablished() {
        return actState instanceof Established;
    }

    public boolean isDisconnecting() {
        return actState instanceof Disconnecting;
    }

    public boolean isWait1() {
        return actState instanceof Wait1;
    }

    public boolean isWait2() {
        return actState instanceof Wait2;
    }

    public boolean isSending() {
        return actState instanceof Sending;
    }

    public boolean isInitialClosed(){
    	return initialClosed;
    }
}

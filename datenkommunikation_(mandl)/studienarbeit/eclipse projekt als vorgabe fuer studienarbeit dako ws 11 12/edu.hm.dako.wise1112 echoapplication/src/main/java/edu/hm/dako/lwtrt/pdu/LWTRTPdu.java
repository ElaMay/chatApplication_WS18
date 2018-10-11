package edu.hm.dako.lwtrt.pdu;

import java.io.Serializable;

/**
 * Klasse LWTRTPdu
 * 
 * @author dako.cs.hm.edu
 */
public class LWTRTPdu implements Serializable {

    private static final long serialVersionUID = -6172619032079227582L;

    public static final int OPID_CONNECT_REQ = 1;
    public static final int OPID_CONNECT_RSP = 2;
    public static final int OPID_DISCONNECT_REQ = 3;
    public static final int OPID_DISCONNECT_RSP = 4;
    public static final int OPID_DATA_REQ = 5;
    public static final int OPID_DATA_RSP = 6;
    public static final int OPID_PING_REQ = 7;
    public static final int OPID_PING_RSP = 8;

    private int opId;
    private int remotePort;
    private String remoteAddress;
    private long sequenceNumber;
    /** 16 Byte sind zur freien Verfuegung reserviert **/
    private Object userData;

    public LWTRTPdu(int opId, int sourcePort, String remoteAddress, long sequenceNumber, Object userData) {
        this.opId = opId;
        this.remotePort = sourcePort;
        this.remoteAddress = remoteAddress;
        this.sequenceNumber = sequenceNumber;
        this.userData = userData;
    }

    public LWTRTPdu(int opId, long sequenceNumber) {
        this(opId, 0, "0.0.0.0", sequenceNumber, null);
    }

    public LWTRTPdu() {};

    /**
     * @return the opId
     */
    public int getOpId() {
        return opId;
    }

    /**
     * @param opId the opId to set
     */
    public void setOpId(int opId) {
        this.opId = opId;
    }

    /**
     * @return the remotePort
     */
    public int getRemotePort() {
        return remotePort;
    }

    /**
     * @param remotePort the remotePort to set
     */
    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    /**
     * @return the remoteAddress
     */
    public String getRemoteAddress() {
        return remoteAddress;
    }

    /**
     * @param remoteAddress the remoteAddress to set
     */
    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    /**
     * @return the sequenceNumber
     */
    public long getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * @param sequenceNumber the sequenceNumber to set
     */
    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * @return the userData
     */
    public Object getUserData() {
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(Object userData) {
        this.userData = userData;
    }

    public void clone(LWTRTPdu lwtrtPdu) {
        this.opId = lwtrtPdu.getOpId();
        this.remoteAddress = lwtrtPdu.getRemoteAddress();
        this.remotePort = lwtrtPdu.getRemotePort();
        this.sequenceNumber = lwtrtPdu.getSequenceNumber();
        this.userData = lwtrtPdu.getUserData();
    }
}

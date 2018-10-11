package edu.hm.dako.lwtrt.impl;

import java.net.SocketException;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import edu.hm.dako.lwtrt.LWTRTAbortException;
import edu.hm.dako.lwtrt.LWTRTConnection;
import edu.hm.dako.lwtrt.LWTRTException;
import edu.hm.dako.lwtrt.pdu.LWTRTPdu;
import edu.hm.dako.lwtrt.statemachine.LWTRTStatemachine;
import edu.hm.dako.lwtrt.util.Timer;

/**
 * Klasse LWTRTConnectionImpl.
 * 
 * @author Bakomenko, Mandl
 * @version 2.0.0
 */
public class LWTRTConnectionImpl implements LWTRTConnection {

	private static Logger logger = Logger.getLogger(LWTRTConnectionImpl.class);
    
	// Konstante für die Länge eines Timeouts von 10000 ms = 10 sec 
	private static int timeout = 10000;
	private Timer timer = new Timer();
    
    // Lock für den Zugriff auf den Zustandsautomat
    protected ReentrantLock stateMachineLock = new ReentrantLock(true);
    // Lock um ein aktives Vorhaben mit Wiederholung vor einem neuen passiven Vorhaben abzuschließen
    // z.B. bis zum Abschluß eines aktiven Verbindungsaufbau kein Daten passiv entgegen nehmen
    private ReentrantLock retryLock = new ReentrantLock(true);

    // Für den Zugriff auf den zu verwendenden Port
    protected PortHandle portHandle;
    // Arbeitet PDUs ab, die auf den Empfangspuffer abgelegt wurden.
    private ReceivingThread rt;
    // Zur Verwaltung der Verbindungszustands
    private LWTRTStatemachine stateMachine;
    
    // IP-Adresse und Port des Verbindungspartners
    protected String remoteAddress;
    protected int remotePort;

    // Empfangspuffer für PDUs die vom Verbindungspartner gesendet wurden.
    private volatile Vector<LWTRTPdu> receiveBuffer = new Vector<LWTRTPdu>();
    // Puffer für 
    private volatile Vector<LWTRTPdu> pickupBuffer = new Vector<LWTRTPdu>();
	
    /**
     * Neue ConnectionImpl instanziieren
     * 
     * @param listenPort Port fuer das Warten auf ankommende Verbindungen
     * @throws SocketException
     */
    protected LWTRTConnectionImpl(PortHandle portHandle) throws LWTRTException {
        this.portHandle = portHandle;
        this.stateMachine = new LWTRTStatemachine();
        rt = new ReceivingThread(this);
        rt.setName("LWTRTReceiving-Thread-"+portHandle.getName());
        rt.start();
    }
   
    @Override
    public void disconnect() throws LWTRTException {
        timer.start("disconnect");
        retryLock.lock();
        try {

        // Aktiver Verbindungsabbau - Disconnect-Request-PDU wird versendet
        stateMachineLock.lock();
        try {
            LWTRTPdu lwtrtPdu = stateMachine.disConnect();
            if (lwtrtPdu != null) {
                dispatch(lwtrtPdu);
            }
        } finally {
        	stateMachineLock.unlock();
        }

        // Warten auf die Disconnect-Response
        // TODO: kein unendliches Warten, Timeout mit 2 Wiederholungen
        while (stateMachine.isDisconnecting()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
        timer.stop();
        } finally {
        	retryLock.unlock();
        }
    }

    @Override
    public void acceptDisconnection() throws LWTRTException {
        stateMachineLock.lock();
        try {
            LWTRTPdu lwtrtPdu = stateMachine.disConnectAcpt();
            if (lwtrtPdu != null) {
                dispatch(lwtrtPdu);
            }
        } finally {
        	stateMachineLock.unlock();
        }
    }

    @Override
    public void send(Object chatPdu) throws LWTRTException, LWTRTAbortException {
    	retryLock.lock();	
    	try {
	        timer.start("data");
	        stateMachineLock.lock();
	        try {
	        	//TODO: Zustand von Established nach Sending wechseln (siehe Established.class)
	            LWTRTPdu lwtrtPdu = stateMachine.data(chatPdu);
	            if (lwtrtPdu != null) {
	                dispatch(lwtrtPdu);
	            } else {
	            	// Wenn keine PDU vom Zustandautomaten übergeben wird, ist senden nicht möglich
	                throw new LWTRTException("Nicht bereit zum senden! Aktueller Zustand: "+stateMachine.actStat());
	            }
	        } finally {
	        	stateMachineLock.unlock();
	        }
	
	        // TODO: Warten auf DATA-RSP, Timeout mit zwei Wiederholungen dann Abbruch

    	} finally {
            timer.stop();
    		retryLock.unlock();
    	}
    }

    @Override
    public Object receive() throws LWTRTException {
        // Warten bis erste PDU im ReceiveBuffer eine DATA-REQ-PDU ist
        while (true) {
            if (!pickupBuffer.isEmpty()) {
                LWTRTPdu lwtrtPdu = pickupBuffer.firstElement();
                pickupBuffer.remove(lwtrtPdu);
                return lwtrtPdu.getUserData();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.debug("sleep in receive unterbrochen");
            }
        }
    }

    @Override
    public void ping() throws LWTRTException, LWTRTAbortException {
      
    }

    /**
     * Aktiver Verbindunsaufbau (Teil 1) wird durchgeführt.
     * Connect-Request-PDU versenden und auf Connect-Response-PDU warten
     * 
     * @param remoteAdress IP-Adresse des Verbindungspartners
     * @param remotePort Port-Nummer des Verbindungspartners
     * @throws LWTRTException Allegeiner Fehler beim Verbindungsaufbau
     * @throws LWTRTAbortException Abbruch des Verbindungsaufbaus
     */
    protected void activConnect1(String remoteAdress, Integer remotePort) throws LWTRTException, LWTRTAbortException {
        
    	retryLock.lock();
    	try {
	    	timer.start("connecting");
	
	        this.remoteAddress = remoteAdress;
	        this.remotePort = remotePort;
	
	        // Aktiver Verbindungsaufbau - Connect-Request-PDU wird versendet
	        stateMachineLock.lock();
	        try {
	            LWTRTPdu lwtrtPdu = stateMachine.connect();
	            if (lwtrtPdu != null) {
	                dispatch(lwtrtPdu);
	            }
	        } finally {
	        	stateMachineLock.unlock();
	        }
	        
	        // Warten auf die Connect-Response-PDU
	        // TODO: kein unendliches Warten, Timeout mit 2 Wiederholungen
	        while (stateMachine.isConnecting()) {
	            try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    logger.error(e);
                }
	        }        
    	} finally {
    		retryLock.unlock();
	        timer.stop();	
    	}
    }

    /**
     * Aktiver Verbindunsaufbau (Teil 2) wird durchgeführt.
     * Connect-Response-PDU wird verarbeitet
     * 
     * @param lwtrtPdu Connect-Response-PDU
     * @throws LWTRTException Allgemeiner Fehler bei der Verarbeitung der PDU
     */
    private void activConnect2(LWTRTPdu lwtrtPdu) throws LWTRTException {
        // PDU wird verarbeitet - Zustandsänderung und ggf. Antwort wird durchgeführt
    	processPdu(lwtrtPdu);
    }

    /**
     * Passiver Verbindungsaufbau wird akzeptiert (Teil 1)
     * Zustandsänderun auf Accepting
     * 
     * @param lwtrtPdu PDU mit einen Connection-Request vom Verbindungspartner
     * @throws LWTRTException Fehler beim Verbindungsaufbau
     */
    protected void accept(LWTRTPdu lwtrtPdu) throws LWTRTException {
        stateMachineLock.lock();
        try {
        	// accept als Ereignis für den Zustandsautomat
           	stateMachine.connectAcpt();
           	// das auslösende PDU normals empfangen um PDU zu verarbeiten
            receive(lwtrtPdu);
        } finally {
        	stateMachineLock.unlock();
        }
    }

    /**
     * Passiver Verbindungsaufbau (Teil 2)
     * Connect-Request wird verarbeitet
     * 
     * @param lwtrtPdu PDU mit einen Connection-Request vom Verbindungspartner
     * @throws LWTRTException Fehler beim Verbindungsaufbau
     */
    private void passivConnect(LWTRTPdu lwtrtPdu) throws LWTRTException {
        retryLock.lock();
        try {
        	// Wenn auf einen Connection-Request gewartet wird, werden die
        	// Verbindungsdaten des Partners übernommen
	        if (stateMachine.isAccepting()) {
	            this.remotePort = lwtrtPdu.getRemotePort();
	            this.remoteAddress = lwtrtPdu.getRemoteAddress();
	        }
	        // PDU wird verarbeitet - Zustandsänderung und ggf. Antwort wird durchgeführt
	        processPdu(lwtrtPdu);
        } finally {
        	retryLock.unlock();
        } 
    }

    /**
     * Aktiver Verbindungsabbau (Teil 2)
     * Disconnect-Response wird verarbeitet
     * 
     * @param lwtrtPdu PDU mit einer Disconnect-Response vom Verbindungspartner
     * @throws LWTRTException Fehler beim Verbindungsabbau
     */
    private void activeDisconnect2(LWTRTPdu lwtrtPdu) throws LWTRTException {
    	// PDU wird verarbeitet - Zustandsänderung und ggf. Antwort wird durchgeführt
    	processPdu(lwtrtPdu);
    }

    /**
     * Passiver Verbindungsabbau
     * Disconnect-Request wird verarbeitet. Thread zum Schließen der Verbindung wird gestartet
     * 
     * @param lwtrtPdu PDU mit einem Disconnect-Request vom Verbindungspartner
     * @throws LWTRTException Fehler beim Verbindungsabbau
     */
    private void passivDisconnect(LWTRTPdu lwtrtPdu) throws LWTRTException {
        retryLock.lock();
        try {
        	// PDU wird verarbeitet - Zustandsänderung und ggf. Antwort wird durchgeführt
	    	processPdu(lwtrtPdu);
	        
	    	// Thread für das Schließen der Verbindung wird gestart
	    	PassivDisconnectThread pdt = new PassivDisconnectThread(this);
	        pdt.setName("PassivDisconnectThread-"+portHandle.getName());
	        pdt.start();
        } finally {
        	retryLock.unlock();
        }
    }

    /**
     * Empfangende Daten werden verarbeitet
     * 
     * @param lwtrtPdu PDU mit einem DATA-Request vom Verbindungspartner
     * @throws LWTRTException Fehler beim Verarbeiten der Daten
     */
    private void passivData(LWTRTPdu lwtrtPdu) throws LWTRTException {
        retryLock.lock();
        try {
        	logger.trace("XXX: passivData");
            LWTRTPdu rsp = stateMachine.processPDU(lwtrtPdu);
            if (rsp != null) {
                dispatch(rsp);
    	    	pickupBuffer.add(lwtrtPdu);
            }
        } finally {
        	retryLock.unlock();
        }   
    }

    /**
     * Aktive senden von Daten
     * 
     * @param lwtrtPdu PDU das zum Verbindungspartner gesendet werden soll
     * @throws LWTRTException Fehler beim Senden
     */
    private void activData(LWTRTPdu lwtrtPdu) throws LWTRTException {
        LWTRTPdu rsp = stateMachine.dataRsp(lwtrtPdu);
        if (rsp != null) {
            dispatch(rsp);
        }
    }

    /**
     * Passiv Daten empfangen
     * Daten werden auf dem Empfangspuffer abgelegt um vom ReceivingThread verarbeitet zu werden
     * 
     * @param lwtrtPdu PDU das vom Verbindungspartner empfangen wurde
     */
    protected void receive(LWTRTPdu lwtrtPdu) {
        stateMachineLock.lock();
    	try {
            receiveBuffer.add(lwtrtPdu);
        } finally {
        	stateMachineLock.unlock();
        }
    }

    /**
     * PDU senden
     * 
     * @param lwtrtPdu PDU das an den Verbindungspartner gesende werden soll
     * @throws LWTRTException Fehler beim senden
     */
    private void dispatch(LWTRTPdu lwtrtPdu) throws LWTRTException {
        // IP-Adresse und Port des Verbindungspartner werden hinzugfügt
    	lwtrtPdu.setRemoteAddress(remoteAddress);
        lwtrtPdu.setRemotePort(remotePort);
        // PDU über den PortHandle versenden
        portHandle.send(lwtrtPdu);
    }

    /**
     * Empfangenes PDU verarbeiten
     * 
     * @param lwtrtPdu
     * @throws LWTRTException
     */
    private void processPdu(LWTRTPdu lwtrtPdu) throws LWTRTException {
        // PDU wird an den Zustandsautomaten übergeben um evtl. einen Zustandsänderung auszulösen
    	LWTRTPdu rsp = stateMachine.processPDU(lwtrtPdu);
        // Wird vom Zustandsautomat eine PDU zurückgegeben, ist eine Antwort
    	// (response) zu versenden
    	if (rsp != null) {
            dispatch(rsp);
        }
    }
    
    protected boolean isNotClosed() {
    	return !stateMachine.isClosed();
    }
    
    protected boolean isInitialClosed() {
    	return stateMachine.isInitialClosed();
    }

    /**
     * Thread, der fuer eine Transportverbindung Nachrichten entgegennimmt
     * 
     * @author Bakomenko, Mandl
     */
    private static class ReceivingThread extends Thread {

        private static Logger logger = Logger.getLogger(ReceivingThread.class);
        private LWTRTConnectionImpl con;

        private ReceivingThread(LWTRTConnectionImpl con) {
            this.con = con;
        }

        public void run() {
            // Solange die Verbindung nicht geschlossen ist, werden PDUs empfangen und verarbeitet
        	while (con.isNotClosed() || con.isInitialClosed()) {
                synchronized (con) {
                    if (!con.receiveBuffer.isEmpty()) {
                    	LWTRTPdu lwtrtPdu = con.receiveBuffer.firstElement();
                    	try {
                    		// Abhängig von der OPID wird das PDU verarbeitet
                            switch (lwtrtPdu.getOpId()) {
                            case LWTRTPdu.OPID_CONNECT_REQ:
                                con.passivConnect(lwtrtPdu);
                                break;
                            case LWTRTPdu.OPID_CONNECT_RSP:
                                con.activConnect2(lwtrtPdu);
                                break;
                            case LWTRTPdu.OPID_DISCONNECT_REQ:
                                con.passivDisconnect(lwtrtPdu);
                                break;
                            case LWTRTPdu.OPID_DISCONNECT_RSP:
                                con.activeDisconnect2(lwtrtPdu);
                                break;
                            case LWTRTPdu.OPID_DATA_REQ:
                                con.passivData(lwtrtPdu);
                                break;
                            case LWTRTPdu.OPID_DATA_RSP:
                                con.activData(lwtrtPdu);
                                break;
                            }
                        } catch (LWTRTException e) {
                            logger.error(e);
                        } 
                        con.receiveBuffer.remove(lwtrtPdu);
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        	// Nach dem Abbau der Verbindung wird diese auch im PortHandle beendet
            con.portHandle.close(con.remoteAddress, con.remotePort);
        }
    }

    /**
     * Thread fuer die Abwicklung eines passiven Verbindungsabbaus
     * 
     * @author Bakomenko, Mandl
     *
     */
    private static class PassivDisconnectThread extends Thread {

        private static Logger logger = Logger.getLogger(PassivDisconnectThread.class);
        private LWTRTConnectionImpl con;

        private PassivDisconnectThread(LWTRTConnectionImpl con) {
            this.con = con;
        }

        public void run() {
        	
        	logger.debug("Transportverbindung wird passiv abgebaut");
        
        	// zwei Sekunden im Zustand Wait1 warten
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                logger.error(e);
            }
            
            logger.debug("Wait1-Timer bei passiven Verbindungsabbau abgelaufen");
            
            try {
                if (con.stateMachine.isWait1()) {
                    LWTRTPdu lwtrtPdu = con.stateMachine.timeout();
                    if (lwtrtPdu != null) {
                        con.dispatch(lwtrtPdu);
                    }
                }
            } catch (LWTRTException e) {
                logger.error(e);
            }
        }
    }
}
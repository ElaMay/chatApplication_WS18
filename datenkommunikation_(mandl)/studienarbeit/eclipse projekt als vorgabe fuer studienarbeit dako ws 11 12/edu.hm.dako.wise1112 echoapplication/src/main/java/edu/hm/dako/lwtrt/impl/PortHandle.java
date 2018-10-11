package edu.hm.dako.lwtrt.impl;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.lwtrt.LWTRTAbortException;
import edu.hm.dako.lwtrt.LWTRTConnection;
import edu.hm.dako.lwtrt.LWTRTException;
import edu.hm.dako.lwtrt.pdu.LWTRTPdu;
import edu.hm.dako.lwtrt.udpWrapper.UdpSocketWrapper;

/**
 * Klasse PortHandle
 * 
 * Dient der Verwaltung aller logischen Transportverbindungen (LWTRT-Verbidnung, 
 * die ueber einen UDP-Port aktuell aufgebaut sind. 
 * Mehrere Verbindungen werden also ueber diese Klasse, deren 
 * Funktionalitaet im Wesentlichen in einen eigenen Thread ablaeuft, gemultiplext.
 * 
 * @author Bakomenko, Mandl
 */

public class PortHandle extends Thread 
{
	private static Log log = LogFactory.getLog(PortHandle.class);
    private volatile boolean stop = false;
    private UdpSocketWrapper socket;
    
    // Verbindungstabelle: Hier werden alle aktiven Verbindungen einer Transportinstanz verwaltet
    private volatile Map<String, LWTRTConnectionImpl> connections = new ConcurrentHashMap<String, LWTRTConnectionImpl>();
    protected ReentrantLock connectionsLock = new ReentrantLock(true);
    
    // Tabelle aller wartende Verbindungen, die darauf warten
    // akzeptiert zu werden
    private ArrayList<LWTRTConnectionImpl> waitingConnections = new ArrayList<LWTRTConnectionImpl>();

    public PortHandle(int localPort) throws LWTRTException {
        this.setName("LWTRTPortHandleThread-" + localPort);
        log.trace("Socket oeffnen, Port: " + localPort);
        try {
            socket = new UdpSocketWrapper(localPort);
        } catch (SocketException e) {
            throw new LWTRTException("Fehler beim Oeffnen des Sockets: " + e);
        }
        start();
    }

    public LWTRTConnection connect(String remoteAddress, int remotePort) throws LWTRTException, LWTRTAbortException {
        LWTRTConnectionImpl con = new LWTRTConnectionImpl(this);
        connectionsLock.lock();
        connections.put(generateKey(remoteAddress, remotePort), con);
        connectionsLock.unlock();
        con.activConnect1(remoteAddress, remotePort);
        return con;
    }
    
    public LWTRTConnection accept() {
        while (true) {
        	connectionsLock.lock();
            if (!waitingConnections.isEmpty()) {
                LWTRTConnection con = waitingConnections.get(0);
                waitingConnections.remove(con);
                connectionsLock.unlock();
                return con;
            }
            connectionsLock.unlock();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (!stop) {
            LWTRTPdu lwtrtPdu = new LWTRTPdu();
            try {
                socket.receive(lwtrtPdu);
            } catch (IOException e) {
            	log.error(e.getLocalizedMessage());
            }
            // Pruefen, ob zu dem Sender eine Transportverbindung besteht
            connectionsLock.lock();
            if (connections.containsKey(generateKey(lwtrtPdu.getRemoteAddress(), lwtrtPdu.getRemotePort()))) {
            	log.debug("Bestehende Verbindung gefunden: Remote-IP-Adresse = "
            		+ lwtrtPdu.getRemoteAddress() 
            		+ ", Remote-Port = "
            		+ lwtrtPdu.getRemotePort());
                connections.get(generateKey(lwtrtPdu.getRemoteAddress(), lwtrtPdu.getRemotePort())).receive(lwtrtPdu);
                
            } else {
                // Bei einem neuen Verbindungspartner wird ein passiver Verbindungsaufbau gestartet
                try {
                    LWTRTConnectionImpl con = new LWTRTConnectionImpl(this);
                    connections.put(generateKey(lwtrtPdu.getRemoteAddress(), lwtrtPdu.getRemotePort()), con);
                    con.accept(lwtrtPdu);
                    waitingConnections.add(con);
                } catch (LWTRTException e) {
                	log.error(e.getLocalizedMessage());
                }
            }
            connectionsLock.unlock();
        }
        socket.close();
        waitingConnections.clear();
        connections.clear();
    }

    public void requestStop() {
        stop = true;
    }

    protected void send(LWTRTPdu lwtrtPdu) throws LWTRTException {
        try {
            socket.send(lwtrtPdu);
        } catch (IOException e) {
            e.printStackTrace();
            throw new LWTRTException("LWTRT-PDU kann nicht ausgeliefert werden: " + e);
        }
    }
    
    protected void close(String remoteAddress, int remotePort) {
    	connectionsLock.lock();
    	if(connections.containsKey(generateKey(remoteAddress, remotePort))) {
    		connections.remove(generateKey(remoteAddress, remotePort));
    	} else {
    		log.error("PortHandle.close: Keine Connection für die "+remoteAddress+":"+remotePort+" bekannt!");
    	}
    	connectionsLock.unlock();
    }

    /**
     * Erzeugen eines eindeutigen Schluessels fuer die 
     * Identifikation einer Transportverbindung. Dieser besteht aus 
     * der IP-Adresse und dem Port des Partners.
     * TODO: Vorsicht bei erneutem Verbindungsaufbau kann der gleiche Port 
     * benutzt werden.
     * 
     * @param remoteAddress
     * @param remotePort
     * @return
     */
    private String generateKey(String remoteAddress, int remotePort) {
        return remoteAddress + ":" + String.valueOf(remotePort);
    }
}
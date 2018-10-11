package edu.hm.dako.lwtrt.udpWrapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.lwtrt.pdu.LWTRTPdu;

/**
 * The Class UdpSocketWrapper.
 * 
 * Diese Klasse kapselt die Datagram-Sockets und stellt
 * eine etwas komfortablere Schnittstelle zur Verfuegung,
 * die ausschliesslich fuer die LWTRT-Schicht dient und nicht
 * wiederverwendbar programmiert ist.
 * 
 * Der Mehrwert dieser Klasse im Vergleich zur Standard-DatagramSocket-Klasse
 * ist die Nutzung eines Objektstroms zur Kommunikation ueber UDP.
 * 
 * Die Klasse ist also Bestandteil der LWTRT-Schicht.
 * 
 * @author Pohl, Mandl
 * 
 * @version 2.0.0
 */
public class UdpSocketWrapper 
{
    private DatagramSocket socket;
    private static Log log = LogFactory.getLog(UdpSocketWrapper.class);

    private static  final int receiveBufferSize = 1000000;
    private static final int sendBufferSize = 50000;
    
    /**
     * Konstruktor
     * @param port UDP-Port, der lokal für das Datagramm-Socket verwendet werden soll
     */
    public UdpSocketWrapper(int port) throws SocketException {
        socket = new DatagramSocket(port);
       
        try {
        	socket.setReceiveBufferSize(receiveBufferSize);
        	socket.setSendBufferSize(sendBufferSize);
	    } catch (SocketException e){
	    	log.debug("Socketfehler: " + e);
	    }
    }

    /**
     * Empfangen einer LWTRT-PDU
     * 
     * @param lwtrtPdu: Nachricht, die empfangen wurde
     * @throws IOException
     */
    public synchronized void receive(LWTRTPdu lwtrtPdu) throws IOException 
    {
        byte[] bytes = new byte[65527];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        
        try {
        	socket.receive(packet);
        }
        catch (IOException e) {
        	log.error("SEND: " + "Fehler beim Senden einer LWTRT-Pdu");
        	throw e;
        }
        
        ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
        ObjectInputStream ois = new ObjectInputStream(bais);
        
        try {
            lwtrtPdu.clone((LWTRTPdu)ois.readObject());
            String remoteAddress = packet.getAddress().toString();
            remoteAddress = remoteAddress.substring(1, remoteAddress.length());
            lwtrtPdu.setRemoteAddress(remoteAddress);
            lwtrtPdu.setRemotePort(packet.getPort());
            
            log.debug("" + packet.getPort()
                      + "->"
                      + socket.getLocalPort()
                      + " "
                      + decodeOpid(lwtrtPdu)
                      + "-"
                      + lwtrtPdu.getSequenceNumber());

        } catch (ClassNotFoundException e) {
            log.error("ClassNotFoundException:", e);
        }
    }
    

	/**
	 * Senden einer LWTRT-PDU
	 * @param lwtrtPdu: Zu sendende PDU
	 * @throws IOException
	 */
    public void send(LWTRTPdu lwtrtPdu) throws IOException 
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(lwtrtPdu);
        byte[] bytes = out.toByteArray();
        
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(lwtrtPdu
            .getRemoteAddress()), lwtrtPdu.getRemotePort());
        
        log.debug("SEND: " + packet.getAddress()
                  + ":"
                  + packet.getPort()
                  + "   "
                  + decodeOpid(lwtrtPdu)
                  + "-"
                  + lwtrtPdu.getSequenceNumber());
        try {
        	socket.send(packet);
        }
        catch (IOException e) {
        	log.error("SEND: " + "Fehler beim Senden einer LWTRT-Pdu");
            throw e;
        }
    }

    /**
	 * Datagram-Socket schliessen
	 */
    public void close() {
        socket.close();
    }
    /**
     * @return Lokale Adresse
     */
    public String getLocalAddress() {
        return socket.getLocalAddress().getHostAddress();
    }
    /**
     * @return lokalen Port
     */
    public int getLocalPort() {
        return socket.getLocalPort();
    }
    
    /**
     * Dekodieren des LWTRT-Operationscodes fuer das Debugging
     */
	private String decodeOpid(LWTRTPdu lwtrtPdu) {
		switch( lwtrtPdu.getOpId()){
		case 1:
			return "OPID_CONNECT_REQ";
		case 2:
			return "OPID_CONNECT_RSP";
		case 3:
			return "OPID_DISCONNECT_REQ";
		case 4:
			return "OPID_DISCONNECT_RSP";
		case 5:
			return "OPID_DATA_REQ";
		case 6:
			return "OPID_DATA_RSP";
		case 7:
			return "OPID_PING_REQ";
		case 8:
			return "OPID_PING_RSP";
		default:
			return "UNKNOWN";
		}
	}
}
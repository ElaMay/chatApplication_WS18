package edu.hm.dako.chat.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;


public class AuditLogServerImpl extends AbstractAuditLogServer {


    private static Log log = LogFactory.getLog(AuditLogServerImpl.class);

    // Threadpool fuer Worker-Threads
    private ExecutorService executorService;

    // Socket fuer den Listener, der alle Verbindungsaufbauwuensche der Clients
    // entgegennimmt
    private DatagramSocket socket;


    public AuditLogServerImpl(ExecutorService executorService, DatagramSocket socket) throws SocketException {
        log.debug("AuditLogServerImpl konstruiert");
        this.executorService = executorService;
        this.socket = socket;
    }

    ///////////////////////////////////////////////////////////////////

    public AuditLogServerImpl (int port) throws IOException {
        socket = new DatagramSocket(port);
    }

    public void execute () throws IOException {
        while (true) {
            DatagramPacket packet = receivePacket();
            //sendMessage (packet.getAddress(), packet.getPort(), packet.getData(), packet.getLength());
        }
    }

    //Die Nachrichten, die versendet werden. (ist nicht nötig)
   // private void sendMessage(InetAddress address, int port, byte data[], int length) throws IOException {
       // DatagramPacket packet = new DatagramPacket(data, length, address, port);
       // socket.send(packet);
        //System.out.println(" Response sent ");
    //}

    //Die Nachrichten, die dann empfangen werden.
    private DatagramPacket receivePacket() throws IOException {
        byte buffer[] = new byte[65535];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        System.out.println("Received " + packet.getLength() + " bytes. ");
        System.out.println(packet);
        return packet;
    }

    //Die Main-Methode.
    public static void main (String args[]) throws IOException {
        if (args.length != 1)
            throw new RuntimeException("Syntax: AuditLogServerImpl <port>");
        AuditLogServerImpl server = new AuditLogServerImpl(Integer.parseInt(args[0]));
        server.execute();
    }

    ///////////////////////////////////////////////////////////////////

    public void start() {

    }


    public void stop() {

    }

    public void run() {

    }
}

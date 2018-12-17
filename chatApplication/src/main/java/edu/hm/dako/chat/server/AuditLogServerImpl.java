package edu.hm.dako.chat.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
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

    //BufferedWriter als Objektvariable.
    private BufferedWriter bufferedWriter;


    //Einen Konstruktor erstellen mit einem Executor und Socket.
    public AuditLogServerImpl(ExecutorService executorService, DatagramSocket socket) throws SocketException {
        log.debug("AuditLogServerImpl konstruiert");
        this.executorService = executorService;
        this.socket = socket;
    }


    //Datagramsocket erstellen mit einem Portnummer.
    public AuditLogServerImpl (int port) throws IOException {
        socket = new DatagramSocket(port);
    }


    //Zum Starten und in dem Fall öffnen der Dateien, damit wir reinschreiben können.
    public void start() {
        FileWriter fw = null;
        try {
            //Dateinamen selber erstellen, Klassenname als String und die Zeit und die log auch als String.
            fw = new FileWriter("SimpleChatServerImpl" + System.currentTimeMillis() + ".log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        bufferedWriter = new BufferedWriter(fw);
        System.currentTimeMillis();
    }


    //Eine Methode für die Packete, die wir dann erhalten.
    public void execute () throws IOException {
        while (true) {
            receivePacket();
            //sendMessage (packet.getAddress(), packet.getPort(), packet.getData(), packet.getLength());
        }
    }


    //Die Nachrichten, die versendet werden. (ist nicht nötig)
   // private void sendMessage(InetAddress address, int port, byte data[], int length) throws IOException {
       // DatagramPacket packet = new DatagramPacket(data, length, address, port);
       // socket.send(packet);
        //System.out.println(" Response sent ");
    //}


    //Die Nachrichten, die dann empfangen werden. Diese Methode besitzt keine Eingabe.
    private void receivePacket() throws IOException {
        byte buffer[] = new byte[65535];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        try {
            //Dieses Objekt wird gecastet.
            logWriter((AuditLogPDU) deserialize(packet.getData()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //System.out.println("Received " + packet.getLength() + " bytes. ");
        //System.out.println(packet);
    }


    //Methode, in einer Datei was reinschreiben.
    //Die AuditLogPDU Klasse zum Aufrufen eines Objektes in einem String.
    //Die Variable logNew gilt innerhalb dieser Methode nur. Verlässt er diese Methode, wird der reservierte Speicher frei gegeben.
    //Diese Methode besitzt keine Ausgabe.
    private void logWriter(AuditLogPDU logNew) throws IOException {
        bufferedWriter.write(logNew.toString());
    }


    //Von ByteArray in Object umwandeln und Object rausbekommen.
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }


    //Zum Schließen der Dateien, damit nichts mehr geschrieben wird.
    public void stop() {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Die Main-Methode, mit der wir den AuditLogServer testen.
    public static void main (String args[]) throws IOException {
        if (args.length != 1)
            throw new RuntimeException("Syntax: AuditLogServerImpl <port>");
        AuditLogServerImpl server = new AuditLogServerImpl(Integer.parseInt(args[0]));
        server.execute();
    }


    public void run() {

    }
}

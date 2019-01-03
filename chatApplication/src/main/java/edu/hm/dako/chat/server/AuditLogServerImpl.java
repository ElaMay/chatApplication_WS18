package edu.hm.dako.chat.server;

import com.sun.security.ntlm.Client;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;


public class AuditLogServerImpl extends AbstractAuditLogServer {

    /**
     * @author Diana Marjanovic
     *
     */

    /////////////////////////////////////
    private static final int Portnumber = 3000;

    private static Log log = LogFactory.getLog(AuditLogServerImpl.class);

    // Threadpool fuer Worker-Threads
    private ExecutorService executorService;


    // Socket fuer den Listener, der alle Verbindungsaufbauwuensche der Clients
    // entgegennimmt (UDP)
    private DatagramSocket socket;

    // Ein Socket für den TCP, der auch die Anfragen überprüft. ---------------------
    private ServerSocket serverSocket;
    private InetAddress inetAddress;


    //BufferedWriter als Objektvariable, den wir für unsere Datei brauchen.
    private BufferedWriter bufferedWriter;


    //Einen Konstruktor erstellen mit einem Executor und Socket bzw. die beiden Sockets. ---------
    public AuditLogServerImpl(ExecutorService executorService, DatagramSocket socket, ServerSocket serverSocket, InetAddress inetAddress) throws SocketException {
        log.debug("AuditLogServerImpl konstruiert");
        this.executorService = executorService;
        this.socket = socket;
        this.serverSocket = serverSocket;
        this.inetAddress = inetAddress;
    }


    //--------------------------------------------------------
    //Datagramsocket erstellen mit einem Portnummer und einen ServerSocket erstellen mit einem Portnummer..
    public AuditLogServerImpl (int port) throws IOException {
        socket = new DatagramSocket(port);
        serverSocket = new ServerSocket(port);
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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Java-Rumpf für einen TCP-Server
    /* Java-Rumpf für einen TCP-Server */
//    ServerSocket server = new ServerSocket(Portnummer);
    // Beispiel mit einem Thread, der auf einen Verbindungsaufbauwunschwartet,
    // den Client bedient, die Verbindung anschließend wieder beendet und
    // auf die nächste Anfrage wartet.
//            while (true) {
//            Socket incoming = server.accept();
//            ObjectInputStream in;
//            ObjectOutputStream out;
//                try {
//                    out = new ObjectOutputStream(incoming.getOutputStream());
//                    in = new ObjectInputStream(incoming.getInputStream());
//                    // Empfangen über Inputstream
//                    AuditLogPDU pdu = (AuditLogPDU) in.readObject();
//                    // Empfangene PDU verarbeiten
//
//                    // Senden über OutputStream
//                    // AuditLogPDU ist eine eigene Objektklasse
//                    out.writeObject(new AuditLogPDU());
//                    // Stream und Verbindung schließen
//                    incoming.close();
//                    }
//                    catch (Exception e) {
//                    }
//            }
}

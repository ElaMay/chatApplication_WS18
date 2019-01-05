package edu.hm.dako.chat.auditlog;

import edu.hm.dako.chat.common.PduType;

import java.io.*;
import java.net.*;


public class AuditLogServerImpl extends AbstractAuditLogServer {

    /**
     * @author Diana Marjanovic
     *
     */

    /////////////////////////////////////
    private static final int Portnumber = 3000;

    // Threadpool fuer Worker-Threads
    private boolean upd;

    private boolean running = true;


    // Socket fuer den Listener, der alle Verbindungsaufbauwuensche der Clients
    // entgegennimmt (UDP)
    //private DatagramSocket socket;
    private Socket tcpSocket;
    private DatagramSocket udpSocket;

    // Ein Socket für den TCP, der auch die Anfragen überprüft. ---------------------
    //private ServerSocket serverSocket;
    //private InetAddress inetAddress;
    //private Socket Client;


    //TODO: Fuer this.upd einen Setter schreiben und am Anfang von Execute den setzen.
    //BufferedWriter als Objektvariable, den wir für unsere Datei brauchen.
    private BufferedWriter bufferedWriter;

    //Einen Konstruktor erstellen mit einem Executor und Socket bzw. die beiden Sockets. ---------
    public AuditLogServerImpl(boolean isUpd, int port) throws SocketException, IOException {
        this.upd = isUpd;
        if(upd)
            udpSocket = new DatagramSocket(port);
        else
            tcpSocket = new ServerSocket(port).accept();

        System.out.println(port);
      //  this.serverSocket = serverSocket;
      //  this.inetAddress = inetAddress;
    }
    //, ServerSocket serverSocket, InetAddress inetAddress


    //--------------------------------------------------------
    //Datagramsocket erstellen mit einem Portnummer und einen ServerSocket erstellen mit einem Portnummer..
    public AuditLogServerImpl (int port) throws IOException {
        this(true,port);
       // serverSocket = new ServerSocket(port);
    }


    //Zum Starten und in dem Fall öffnen der Dateien, damit wir reinschreiben können.
    public void start() throws IOException {
        //Dateinamen selber erstellen, Klassenname als String und die Zeit und die log auch als String.
        FileWriter fw = new FileWriter("SimpleChatServerImpl" + System.currentTimeMillis() + ".log");
        bufferedWriter = new BufferedWriter(fw);
        System.currentTimeMillis();
        System.out.println("Logserver started");
    }


    //Eine Methode für die Packete, die wir dann erhalten.
    public void execute () {
        while (running) {
            try {
                receivePacket();
            }
            catch (IOException e){
                e.printStackTrace(); //TODO: besseres Errorhandling
            }
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
        AuditLogPDU receivedPDU = null;

        if(upd) {// Using UDP
            byte buffer[] = new byte[65535];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            udpSocket.receive(packet);
            try {
                receivedPDU = (AuditLogPDU) deserialize(packet.getData());
                this.logWriter(receivedPDU);
            } catch (ClassNotFoundException e) {
                e.printStackTrace(); //TODO: errorhandling
            }
            //TODO System outs entfernen
            System.out.println("Received " + packet.getLength() + " bytes. ");
            System.out.println(packet);


        }
        else { // Using TCP
            ObjectInputStream obj = new ObjectInputStream(tcpSocket.getInputStream());
            try {
                receivedPDU = (AuditLogPDU) obj.readObject();
                this.logWriter(receivedPDU);
            } catch (ClassNotFoundException e) {
                e.printStackTrace(); //TODO: errorhandling
            }
        }

        if(receivedPDU.getType() == PduType.SHUTDOWN_EVENT){
            this.stop();
        }

    }


    //Methode, in einer Datei was reinschreiben.
    //Die AuditLogPDU Klasse zum Aufrufen eines Objektes in einem String.
    //Die Variable logNew gilt innerhalb dieser Methode nur. Verlässt er diese Methode, wird der reservierte Speicher frei gegeben.
    //Diese Methode besitzt keine Ausgabe.
    private void logWriter(AuditLogPDU logNew) throws IOException {
        //System.out.println(logNew.toString());
        bufferedWriter.write(logNew.toString());
        bufferedWriter.flush();
    }


    //Von ByteArray in Object umwandeln und Object rausbekommen.
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }


    //Zum Schließen der Dateien, damit nichts mehr geschrieben wird.
    public void stop() throws IOException {
        if(upd) {

        }
        else {

        }
        bufferedWriter.close();
        running = false;
    }


    //Die Main-Methode, mit der wir den AuditLogServer testen.
    public static void main (String args[]) throws IOException {
        if (args.length != 1)
            throw new RuntimeException("Syntax: AuditLogServerImpl <port>");
        AuditLogServerImpl server = new AuditLogServerImpl(false, Integer.parseInt(args[0]));
        server.start();
        server.execute();
        server.stop();
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

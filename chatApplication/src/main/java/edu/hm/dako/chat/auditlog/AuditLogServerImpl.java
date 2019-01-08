package edu.hm.dako.chat.auditlog;

import edu.hm.dako.chat.common.PduType;

import java.io.*;
import java.net.*;

/**
 * @author Diana Marjanovic
 * Eine AuditLog-Server-Klasse, der für die Ausführung zuständig ist.
 */

public class AuditLogServerImpl extends AbstractAuditLogServer {

    /**
     * Eine Portnummer als Test zum Starten des Servers.
     */
    private static final int Portnumber = 3000;

    /**
     * Threadpool fuer die Worker-Threads
     */
    private boolean isUdp;

    /**
     * Zum Testen, ob der Server gerade läuft.
     */
    private boolean isRunning = true;

    /**
     * Socket fuer den Listener, der alle Verbindungsaufbauwuensche der Clients entgegennimmt (UDP).
     * Beide Sockets für jeweils tcp und udp.
     */
    private Socket tcpSocket;
    private DatagramSocket udpSocket;

    /**
     * BufferedWriter als Objektvariable, den wir für unsere Datei brauchen.
     */
    private BufferedWriter bufferedWriter;

    /**
     * Einen Konstruktor erstellen mit einem Executor und Socket bzw. die beiden Sockets.
     * @param isUpd
     * @param port
     * @throws SocketException
     * @throws IOException
     */
    public AuditLogServerImpl(boolean isUpd, int port) throws SocketException, IOException {
        this.isUdp = isUpd;
        if(isUdp)
            udpSocket = new DatagramSocket(port);
        else
            tcpSocket = new ServerSocket(port).accept();

        System.out.println(port);
    }

    /**
     * Datagramsocket erstellen mit einem Portnummer und einen ServerSocket erstellen mit einem Portnummer.
     * @param port
     * @throws IOException
     */
    public AuditLogServerImpl (int port) throws IOException {
        this(true,port);
    }

    /**
     * Zum Starten und in dem Fall öffnen der Dateien, damit wir reinschreiben können.
     * @throws IOException
     *
     */
    public void start() throws IOException {
        /**
         * Dateinamen selber erstellen, Klassenname als String und die Zeit und die log auch als String.
         */
        FileWriter fw = new FileWriter("SimpleChatServerImpl" + System.currentTimeMillis() + ".log");
        bufferedWriter = new BufferedWriter(fw);
        System.currentTimeMillis();
        System.out.println("Logserver started");
    }

    /**
     * Eine Methode für die Packete, die wir dann erhalten.
     */
    public void execute () {
        while (isRunning) {
            try {
                receivePacket();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Die Nachrichten, die dann empfangen werden. Diese Methode besitzt keine Eingabe.
     * @throws IOException
     */
    private void receivePacket() throws IOException {
        AuditLogPDU receivedPDU = null;

        if(isUdp) { // Using UDP
            byte buffer[] = new byte[65535];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            udpSocket.receive(packet);
            try {
                receivedPDU = (AuditLogPDU) deserialize(packet.getData());
                this.logWriter(receivedPDU);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("Received " + packet.getLength() + " bytes. ");
            System.out.println(packet);
        }
        else { // Using TCP
            ObjectInputStream obj = new ObjectInputStream(tcpSocket.getInputStream());
            try {
                receivedPDU = (AuditLogPDU) obj.readObject();
                this.logWriter(receivedPDU);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if(receivedPDU.getType() == PduType.SHUTDOWN_EVENT){
            this.stop();
        }

    }

    /**
     * Methode, in einer Datei was reinschreiben.
     * Die AuditLogPDU Klasse zum Aufrufen eines Objektes in einem String.
     * Die Variable logNew gilt innerhalb dieser Methode nur. Verlässt er diese Methode, wird der reservierte Speicher frei gegeben.
     * Diese Methode besitzt keine Ausgabe.
     * @param logNew
     * @throws IOException
     */
    private void logWriter(AuditLogPDU logNew) throws IOException {
        bufferedWriter.write(logNew.toString());
        bufferedWriter.flush();
    }

    /**
     * Von ByteArray in Object umwandeln und Object rausbekommen.
     * @param data
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    /**
     * Zum Schließen der Dateien, damit nichts mehr geschrieben wird.
     * @throws IOException
     */
    public void stop() throws IOException {
        if(isUdp) {

        }
        else {

        }
        bufferedWriter.close();
        isRunning = false;
    }

    /**
     * Die Main-Methode, mit der wir den AuditLogServer testen.
     * @param args
     * @throws IOException
     */
    public static void main (String args[]) throws IOException {
        AuditLogServerImpl server;
        if (args.length != 2)
            throw new RuntimeException("Syntax: AuditLogServerImpl <port>");
        String protocol = args[1];
        if(protocol.equalsIgnoreCase("udp")){
            server = new AuditLogServerImpl(true, Integer.parseInt(args[0]));
        }else if(protocol.equalsIgnoreCase("tcp")){
            server = new AuditLogServerImpl(false, Integer.parseInt(args[0]));
        }else{
            System.out.println("False Argument.");
            return;
        }
        server.start();
        server.execute();
        server.stop();
    }

    /**
     * Eine run-Methode für den Thread
     */
    public void run() {

    }

    /**
     * Ein Setter für unser UDP.
     * @param udp
     */
    public void setUdp(boolean udp){

        this.isUdp = udp;
    }

}

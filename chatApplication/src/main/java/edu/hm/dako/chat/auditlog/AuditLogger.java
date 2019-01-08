package edu.hm.dako.chat.auditlog;

import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.common.PduType;
import edu.hm.dako.chat.server.ChatServerGUI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

/**
 * @author Sophia Weißenberger
 *
 */
public class AuditLogger {

    public enum OutputType {
        SYSTEM,
        UDP,
        TCP;
    }


    private static Object obj = null;
    private InetAddress address;
    private int port;
    private OutputType outputType;
    private Socket tcpSocket;
    private DatagramSocket udpSocket;


    private AuditLogger() throws UnknownHostException{
        this.outputType = ChatServerGUI.auditUseUDP.isSelected() ? OutputType.UDP : OutputType.TCP;
        this.port = Integer.parseInt(ChatServerGUI.auditlogPort.getText());
        this.address = InetAddress.getByName(ChatServerGUI.auditlogIP.getText());
    }

    public static AuditLogger getInstance() throws UnknownHostException{
        if (obj == null)
            obj = new AuditLogger();
        return (AuditLogger) obj;
    }

    public void startAuditLog(){
        switch(outputType){
            case UDP:
                try {
                    //+++++++++++++++++
                    //hier socket öffnen
                    udpSocket = new DatagramSocket();
                }catch (Exception e){
                    outputType = OutputType.SYSTEM;
                }
                break;
            case TCP:
                try {
                    tcpSocket = new Socket(address, port);
                }
                catch (IOException e){
                    outputType = OutputType.SYSTEM;
                }
                break;
            default:
                outputType = OutputType.SYSTEM;
        }
    }

    public void stopAuditLog() throws IOException{

        String tmp = null;
        sendAudit(new ChatPDU(PduType.SHUTDOWN_EVENT, tmp));

        try{
            switch(outputType) {
                case UDP:
                    udpSocket.close();
                    break;

                case TCP:
                    tcpSocket.close();
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    //TODO: Synchronized für die recieved Methode
    public synchronized void  sendAudit(ChatPDU receivedPdu){
        AuditLogPDU auditLog = new AuditLogPDU(receivedPdu.getPduType(), receivedPdu.getUserName(), receivedPdu.getServerThreadName(), receivedPdu.getClientThreadName(), receivedPdu.getMessage());
        try {
            switch (outputType) {
                case UDP:
                    byte buffer[] = serialize(auditLog);
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
                    udpSocket.send(packet);
                    break;

                case TCP:
                    ObjectOutputStream oos = new ObjectOutputStream(tcpSocket.getOutputStream());
                    oos.writeObject(auditLog);
                    oos.flush();
                    break;

                case SYSTEM:
                    System.out.println(auditLog);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public OutputType getOutputType() {
        return outputType;
    }
}

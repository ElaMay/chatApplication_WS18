package edu.hm.dako.chat.server;

import com.sun.xml.internal.bind.v2.TODO;
import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.common.ClientConversationStatus;
import edu.hm.dako.chat.common.PduType;

import javax.print.attribute.standard.PDLOverrideSupported;
import java.io.Serializable;
import java.util.Date;
import java.util.Vector;
import java.util.function.ToDoubleBiFunction;

public class AuditLogPDU implements Serializable {

    /**
     * @author Sophia Weißenberger
     *
     * übertragen:
     *
     * - Typ der Nachricht
     * - Zeitstempel der Erzeugung der AuditLog-Nachricht
     * - Chat-Client-Name
     * - Identifikation des verarbeitenden WorkerThreads im Chat-Server
     * - Identifikation des verarbeitenden Threads im Chat-Client
     * - Inhalt der Chat-Nachricht (nur bei Chat Message-Request)
     * */

    private PduType type;
    private String userName;
    private String serverThread;
    private String clientThread;
    private String messageContent = "";
    private Date date;
    private ChatPDU receivedPdu;

    /**
     * Konstruktoren
     */


    public AuditLogPDU(){
        this.type = null;
        this.date = null;     //hier date erzeugen
        this.userName = null;
        this.serverThread = null;
        this.clientThread = null;
        this.messageContent = null;
    }

    public AuditLogPDU (PduType type, String userName, String serverThread, String clientThread, String messageContent ){
        this.type = type;
        this.date = new Date();     //hier date erzeugen
        this.userName = userName;
        this.serverThread = serverThread;
        this.clientThread = clientThread;
        this.messageContent = messageContent;
    }


    /**
     * toString() Methoden
     */

    public String toString() {

        //TODO: Workerthread null verbessern
       // switch (receivedPdu.getPduType()) {
        switch (this.getType()) {

            //Für ChatRequest wird Nachrichten Inhalt mitgeschickt
            case CHAT_MESSAGE_REQUEST:
                return "\n"
                        + "AuditLogPdu ****************************************************************************************************"
                        + "\n" + "PduType: " + this.type + ", " +   "\n" + "Date: " + this.date + ", " + "\n" + "userName: " + this.userName
                        + ", " + "\n" + "workerThreadServer: " + this.serverThread + ", " + "\n"
                        + "clientThread: " + this.clientThread + ", " + "\n" + "messageContent: " + this.messageContent + "\n"
                        + "**************************************************************************************************** AuditLogPdu"
                        + "\n";
            //Für alle anderen nicht.
            default:
                return "\n"
                        + "AuditLogPdu ****************************************************************************************************"
                        + "\n" + "PduType: " + this.type + ", " +   "\n" + "Date: " + this.date + ", " + "\n" + "userName: " + this.userName
                        + ", " + "\n" + "workerThreadServer: " + this.serverThread + ", " + "\n"
                        + "clientThread: " + this.clientThread + "\n"
                        + "**************************************************************************************************** AuditLogPdu"
                        + "\n";

        }
    }


    /**
     *setter aller variablen
     */
    public void setType(PduType type){
        this.type = type;
    }

    public void setDate (Date date){        //überhaupt nötig?
        this.date = date;
    }

    public void setUserName (String userName){
        this.userName = userName;
    }

    public void setServerThread (String serverThread){
        this.serverThread = serverThread;
    }

    public void setClientThread (String clientThread){
        this.clientThread = clientThread;
    }

    public void setMessageContent (String messageContent){
        this.messageContent = messageContent;
    }

    /**
     * getter aller variablen
     */
    public PduType getType(){
        return(type);
    }

    public Date getDate(){      //überhaupt nötig?
        return(date);
    }

    public String getUserName(){
        return(userName);
    }

    public String getServerThread(){
        return(serverThread);
    }

    public String getClientThread(){
        return(clientThread);
    }

    public String getMessageContent(){
        return(messageContent);
    }

    /**
     *Event PDU's
     */

    public static AuditLogPDU createLoginEventPdu(Date date,String userName, ChatPDU receivedPdu) {

        AuditLogPDU pdu = new AuditLogPDU();
        pdu.setType(PduType.LOGIN_EVENT);
        pdu.setDate(date);
        pdu.setUserName(userName);
        pdu.setServerThread(Thread.currentThread().getName());
        pdu.setClientThread(receivedPdu.getClientThreadName());

        return pdu;
    }

    public static AuditLogPDU createLogoutEventPdu(Date date, String userName, ChatPDU receivedPdu) {

        AuditLogPDU pdu = new AuditLogPDU();
        pdu.setType(PduType.LOGOUT_EVENT);
        pdu.setDate(date);
        pdu.setUserName(userName);
        pdu.setServerThread(Thread.currentThread().getName());
        pdu.setClientThread(receivedPdu.getClientThreadName());

        return pdu;
    }

    public static AuditLogPDU createChatMessageEventPdu(String userName, ChatPDU receivedPdu) {

        AuditLogPDU pdu = new AuditLogPDU();
        pdu.setType(PduType.CHAT_MESSAGE_EVENT);
        pdu.setServerThread(Thread.currentThread().getName());
        pdu.setClientThread(receivedPdu.getClientThreadName());
        pdu.setUserName(userName);
        pdu.setMessageContent(receivedPdu.getMessage());
        return pdu;
    }

}

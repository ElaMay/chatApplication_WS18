package edu.hm.dako.chat.auditlog;

import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.common.PduType;

import java.io.Serializable;
import java.util.Date;

public class AuditLogPDU implements Serializable {

    /**
     * @author Sophia Weißenberger
     *
     * übertragen:
     *
     * - Typ der Nachricht
     * - Zeitstempel der Erzeugung der auditlog-Nachricht
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
     * Konstruktor, Werte werden mit null vorbelegt.
     */
    public AuditLogPDU(){
        this.type = null;
        this.date = null;
        this.userName = null;
        this.serverThread = null;
        this.clientThread = null;
        this.messageContent = null;
    }

    /**
     * Konstruktor mit Übergabe von Werten.
     *
     * @param type  PduType
     * @param userName  Client Name
     * @param serverThread  Server Thread
     * @param clientThread client Thread
     * @param messageContent Nachrichten Inhalt
     */
    public AuditLogPDU (PduType type, String userName, String serverThread, String clientThread, String messageContent ){
        this.type = type;
        this.date = new Date();
        this.userName = userName;
        this.serverThread = serverThread;
        this.clientThread = clientThread;
        this.messageContent = messageContent;
    }


    /**
     * toString() Methode für die Ausgabe in der Log Datei.
     * @return String
     */

    public String toString() {

        switch (this.getType()) {

            //Für ChatRequest wird Nachrichten Inhalt mitgeschickt
            case CHAT_MESSAGE_REQUEST:
                return "\n"
                         + this.userName + "@@"  + this.type + "@@"  + this.date + "@@" + this.serverThread + "@@"
                         + this.clientThread + "@@" + this.messageContent + "\n";

            //Für alle anderen nicht.
            default:
                return "\n"
                        + this.userName + "@@" + this.type + "@@" + this.date  + "@@" + this.serverThread + "@@"
                        + this.clientThread + "\n";


//            //Für ChatRequest wird Nachrichten Inhalt mitgeschickt
//            case CHAT_MESSAGE_REQUEST:
//                return "\n"
//                        + "AuditLogPdu ****************************************************************************************************"
//                        + "\n" + "PduType: " + this.type + ", " +   "\n" + "Date: " + this.date + ", " + "\n" + "userName: " + this.userName
//                        + ", " + "\n" + "workerThreadServer: " + this.serverThread + ", " + "\n"
//                        + "clientThread: " + this.clientThread + ", " + "\n" + "messageContent: " + this.messageContent + "\n"
//                        + "**************************************************************************************************** AuditLogPdu"
//                        + "\n";
//            //Für alle anderen nicht.
//            default:
//                return "\n"
//                        + "AuditLogPdu ****************************************************************************************************"
//                        + "\n" + "PduType: " + this.type + ", " +   "\n" + "Date: " + this.date + ", " + "\n" + "userName: " + this.userName
//                        + ", " + "\n" + "workerThreadServer: " + this.serverThread + ", " + "\n"
//                        + "clientThread: " + this.clientThread + "\n"
//                        + "**************************************************************************************************** AuditLogPdu"
//                        + "\n";

        }
    }


    /**
     * Setter für den PduType.
     * @param type PduType
     */
    public void setType(PduType type){
        this.type = type;
    }

    /**
     * Setter für den Zeitstempel.
     * @param date Zeitstempel
     */
    public void setDate (Date date){        //überhaupt nötig?
        this.date = date;
    }

    /**
     * Setter für client Name.
     * @param userName Client Name
     */
    public void setUserName (String userName){
        this.userName = userName;
    }

    /**
     * Setter für Server Thread.
     * @param serverThread Server Thread
     */
    public void setServerThread (String serverThread){
        this.serverThread = serverThread;
    }

    /**
     * Setter für Client Thread.
     * @param clientThread Client Thread
     */
    public void setClientThread (String clientThread){
        this.clientThread = clientThread;
    }

    /**
     * Setter für Inhalt der Nachricht falls eine Message gesendet wurde.
     * @param messageContent Inhalt der Nachricht falls Message Request
     */
    public void setMessageContent (String messageContent){
        this.messageContent = messageContent;
    }

    /**
     * Getter für den PduType.
     * @return type von Pdu
     */
    public PduType getType(){
        return(type);
    }

    /**
     * Getter für den Zeitstempel.
     * @return
     */
    public Date getDate(){      //überhaupt nötig?
        return(date);
    }

    /**
     * Getter für Client Namen.
     * @return
     */
    public String getUserName(){
        return(userName);
    }

    /**
     * Getter für den ServerThread.
     * @return
     */
    public String getServerThread(){
        return(serverThread);
    }

    /**
     * Getter für den ClientThread.
     * @return
     */
    public String getClientThread(){
        return(clientThread);
    }

    /**
     * Getter für den Nachrichten Inhalt falls eine Message geschickt wurde.
     * @return
     */
    public String getMessageContent(){
        return(messageContent);
    }


    /**
     * (Wird nicht verwendet) Methode fürs erstellen von Login.
     * @param date
     * @param userName
     * @param receivedPdu
     * @return
     */
    public static AuditLogPDU createLoginEventPdu(Date date,String userName, ChatPDU receivedPdu) {

        AuditLogPDU pdu = new AuditLogPDU();
        pdu.setType(PduType.LOGIN_EVENT);
        pdu.setDate(date);
        pdu.setUserName(userName);
//        pdu.setServerThread(Thread.currentThread().getName());
        pdu.setServerThread(receivedPdu.getServerThreadName());
        pdu.setClientThread(receivedPdu.getClientThreadName());

        return pdu;
    }

    /**
     * (Wird nicht verwendet) Methode fürs erstellen von Logout.
     * @param date
     * @param userName
     * @param receivedPdu
     * @return
     */
    public static AuditLogPDU createLogoutEventPdu(Date date, String userName, ChatPDU receivedPdu) {

        AuditLogPDU pdu = new AuditLogPDU();
        pdu.setType(PduType.LOGOUT_EVENT);
        pdu.setDate(date);
        pdu.setUserName(userName);
//        pdu.setServerThread(Thread.currentThread().getName());
        pdu.setServerThread(receivedPdu.getServerThreadName());
        pdu.setClientThread(receivedPdu.getClientThreadName());

        return pdu;
    }

    /**
     * (Wird nicht verwendet) Methode fürs erstellen von ChatMessage.
     * @param userName
     * @param receivedPdu
     * @return
     */
    public static AuditLogPDU createChatMessageEventPdu(String userName, ChatPDU receivedPdu) {

        AuditLogPDU pdu = new AuditLogPDU();
        pdu.setType(PduType.CHAT_MESSAGE_EVENT);
//        pdu.setServerThread(Thread.currentThread().getName());
        pdu.setServerThread(receivedPdu.getServerThreadName());
        pdu.setClientThread(receivedPdu.getClientThreadName());
        pdu.setUserName(userName);
        pdu.setMessageContent(receivedPdu.getMessage());
        return pdu;
    }

}

package edu.hm.dako.chat.server;

import java.io.Serializable;

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
     * https://stackoverflow.com/questions/3997459/send-and-receive-serialize-object-on-udp
     * */


}

package edu.hm.dako.chat.auditlog;

import java.io.IOException;

/**
 * @author Diana Marjanovic
 * Eine Interface-Klasse für unsere abstrakte Klasse und somit auch für den AuditLog-Server-Klasse
 */

public interface AuditLogServerInterface {

    /**
     * Eine start-Methode für die Threads.
     * @throws IOException
     */
    public void start() throws IOException;

    /**
     * Eine stop-Methode für die Threads.
     * @throws IOException
     */
    public void stop() throws IOException;
}

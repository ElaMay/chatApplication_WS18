package edu.hm.dako.chat.auditlog;

import java.io.IOException;

/**
 * @author Diana Marjanovic
 * Eine abstrakte Klasse für unseren AuditLog-Server-Klasse.
 */

public abstract class AbstractAuditLogServer implements AuditLogServerInterface {

    /**
     * Eine start-Methode
     * @throws IOException
     */
    public abstract void start() throws IOException;

    /**
     * Eine stop-Methode
     * @throws IOException
     */
    public abstract void stop() throws IOException;

}

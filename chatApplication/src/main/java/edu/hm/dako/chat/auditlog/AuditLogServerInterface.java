package edu.hm.dako.chat.auditlog;

import java.io.IOException;

public interface AuditLogServerInterface {

    /**
     * @author Diana Marjanovic
     *
     */

    public void start() throws IOException;

    public void stop() throws IOException;
}

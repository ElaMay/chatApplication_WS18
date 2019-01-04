package edu.hm.dako.chat.auditlog;

import edu.hm.dako.chat.auditlog.AuditLogServerInterface;

import java.io.IOException;

public abstract class AbstractAuditLogServer implements AuditLogServerInterface {

    // hier ein udp socket auf machen
    // der nachrichten bekommt und in ein File schreibt.
    public abstract void start() throws IOException;

    public abstract void stop() throws IOException;

}

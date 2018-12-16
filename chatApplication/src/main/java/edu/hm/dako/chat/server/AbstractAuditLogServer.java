package edu.hm.dako.chat.server;

public abstract class AbstractAuditLogServer implements AuditLogServerInterface {

    // hier ein udp socket auf machen
    // der nachrichten bekommt und in ein File schreibt.
    public abstract void start();

    public abstract void stop();

}

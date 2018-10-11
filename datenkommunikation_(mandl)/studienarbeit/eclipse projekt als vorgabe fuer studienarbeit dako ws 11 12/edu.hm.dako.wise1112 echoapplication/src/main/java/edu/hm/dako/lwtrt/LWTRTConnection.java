package edu.hm.dako.lwtrt;

import edu.hm.dako.lwtrt.LWTRTException;

/**
 * Repraesentiert eine Transportverbindung, ueber die ein Dienstnehmer mit einem Verbindungspartner Daten austauschen kann.
 * <p>
 * Die {@link LWTRTConnection} stellt eine gesicherte Verbindung zur Verfuegung. Beim Senden von Daten wird der Empfang
 * der Daten nach verfolgt. Beim Empfang von Daten wird die Reihenfolge ueberwacht und mehrfach ausgelieferte Daten
 * erkannt. Ein Verlust einer Nachricht wird ebenfalls erkannt. Die Nachrichten werden ggf. erneut gesendet.
 * 
 * @author Bakomenko
 */
public interface LWTRTConnection {

    /**
     * Aufgebaute Verbindung wird aktiv abgebaut.
     * 
     * @throws LWTRTException Allgemeine Fehler die waehrend des aktiven Verbindungsabbaus auftreten
     */
    public void disconnect() throws LWTRTException;

    /**
     * Akzeptieren einer eingegangenen Nachricht ueber den Abbau der Verbindung beim passiven Verbindungsabbau.
     * 
     * @throws LWTRTException Fehler die waehrend des passiven Verbindungsabaus auftreten
     */
    public void acceptDisconnection() throws LWTRTException;

    /**
     * Daten an den Verbindungspartner uebertragen.
     * <p>
     * Nach dem Senden der Daten wird auf eine Bestaetigung durch den Empfaenger gewartet. Ist die Bestaetigung nicht vor
     * Ablauf eines Timer eingetroffen, wird der Sendeversuch wiederholt. Nach zwei erfolglosen Wiederholungen wird das
     * Senden abgebrochen und die Verbindung gilt als abgebaut.
     * 
     * @param pdu Daten, die zu uebertragen sind
     * @throws LWTRTException Allgemeiner Fehler die waehrend des Sendenbs auftreten
     * @throws LWTRTAbortException Zeigt den Abbruch des Sendeversuchts an (nach drei Versuchen)
     */
    public void send(Object pdu) throws LWTRTException, LWTRTAbortException;

    /**
     * Daten vom Verbindungspartner empfangen.
     * <p>
     * Es wird gewartet, bis Daten vom Verbindungspartner vorliegen. Wurden mehrere Pakete vom Verbindungspartner
     * gesendet, werden die Daten in der richtigen Reihenfolge (FIFO) an den Dienstnehmer uebergeben. Pakete die mehrmals
     * empfangen wurden, werden nur einmal an den Dienstnehmer uebergeben.
     * 
     * @return empfangenes Paket
     * @throws LWTRTException Fehler die waehrend des Empfangens auftreten
     */
    public Object receive() throws LWTRTException;

    /**
     * Durchfuehrung einer Lebendueberwachung eines Partners.
     * <p>
     * An den Verbindungspartner wird eine Anfrage (ping) gesendet, die umgehend beantwortet werden muss. Wird auf die
     * Anfrage nicht vor Ablauf eines Timers geantwortet wird zwei mal versucht die Anfrage zu wiederholen. Ist auch der
     * dritte Versuch ohne Erfolg (keine Antwort) gilt die Verbindung als abgebaut.
     * 
     * @throws LWTRTException Allgemeiner Fehler die waehrend der Lebendueberwachung auftreten
     * @throws LWTRTAbortException Zeigt den Abbruch des Pings an (nach drei Versuchen)
     */
    public void ping() throws LWTRTException, LWTRTAbortException;
}

package edu.hm.dako.lwtrt;

import edu.hm.dako.lwtrt.LWTRTException;
import edu.hm.dako.lwtrt.LWTRTAbortException;

/**
 * Mit dem {@link LWTRTService} wird dem Dienstnehmer ({@link BaseSessionService})
 * die Nutzung eines Ports ermoeglicht.<p>
 * Ein Dienstnehmer hat die folgenden drei Schritte vorzunehmen:<p>
 * 1.  Einen Port registrieren (UDP-Port)
 * 2a. Eine Verbindung aktiv aufbauen (connect)
 * 2b. Auf eine Verbindung passiv warten (listen)
 * 3.  Nach der Nutzung die Registrierung des Ports aufheben.
 * 
 * @author Bakomenko
 */
public interface LWTRTService {

	/**
	 * Fuer den Dienstnehmer wird ein Port registriert.<p>
	 * Nach der Registrierung ist der lokale Port exklusiv fuer den Dienstnehmer gebunden.
	 * Ab dem Zeitpunkt der Registrierung werden eingehende Verbindungswuensche entgegen
	 * genommen. Akzeptiert der Dienstnehmer den Verbindungswunsch nicht rechtzeitig,
	 * wird der Verbindungsaufbau abgebrochen. Nach der Registrierung kann der Dienstnehmer
	 * beginnen aktiv eine Verbindung aufzubauen.<p>
	 * Ein Port kann nur einmal (auch durch andere Prozesse) registriert werden.
	 * @param localPort Nummer des Ports (lokal) der registriert werden soll
	 * @throws LWTRTException Fehler beim Registrieren (zB Port wird schon genutzt)
	 */
    public void register(int localPort) throws LWTRTException;
    
    /**
     * Die Registrierung eines Ports wird aufgehoben.<p>
     * Die exklusive Nutzung des Ports fuer den Dienstnehmer wird aufgehoben. Ab dem
     * Zeitpunkt der Deregistierung werden keine Verbindungsw+nsch mehr beruecksichtigt.
     * Auch eingehende Verbindungswuensche in der Warteschlange werden abgebaut.
     * @param localPort Nummer des Ports (lokal) dessen Registrierung aufgehoben wird
     * @throws LWTRTException Fehler beim Aufheben der Registrierung (z.B. Port wurde nicht registriert)
     */
    public void unregister(int localPort) throws LWTRTException;
    
    /**
     * Aktiver Verbindungsaufbau.<p>
     * Ueber einen zuvor registrierten Port wird aktiv eine Verbindung aufgebaut. Beim
     * Verbindungsaufbau wird auf eine Bestaetigung des Verbindungspartners gewartet (Timeout).
     * Der Verbindungsaufbau kann somit nur erfolgreich abgeschlossen werden, wenn der
     * Verbindungspartner auf dem angegebnen Port auf eingehende Verbindungswuensche wartet.
     * @param localPort Nummer des zuvor registrieten Ports
     * @param remoteAddress Adresse des Verbindungspartners
     * @param remotePort Port-Nummer des Verbindungspartners
     * @return Verbindung (nach dem Verbindungsaufbau) die bereit zum Senden von Daten ist
     * @throws LWTRTException Allgemeiner Fehler beim Verbindungsaufbau
     * @throws LWTRTAbortException Abbruch des Verbindungsaufbaus nach drei Versuchen
     */
    public LWTRTConnection connect(int localPort, String remoteAddress, int remotePort) throws LWTRTException, LWTRTAbortException;

    /**
     * Passiver Verbindungsaufbau.<p>
     * Auf einem zuvor registrierten Port wird auf eingehende Verbindungswuensche passiv gewartet.
     * Geht von einem Verbindungspartner eine Anfrage ein, wird das Verbindungsobjekt dem
     * Dienstnehmer uebergeben. Innerhalb einer Timeout-Zeit muss der Dienstnehmer die Verbindung
     * akzeptieren. Erst nach dem expliziten Akzeptieren der Verbindung ist der Aufbau der Verbindung erfolgt.
     * Wird die Verbndung innerhalb der Timeout-Zeit nicht akzeptiert, wird die Verbindung nicht augebaut.
     * @see LWTRTConnection#acceptConnection()
     * @param localPort Nummer des zuvor registrierten Ports
     * @return Verbindung (vor dem Akzeptieren) - noch nicht bereit zum Senden von Daten
     * @throws LWTRTException Fehler beim Verbindungsaufbau
     */
    public LWTRTConnection accept(int localPort) throws LWTRTException;
}

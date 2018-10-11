package edu.hm.dako.lwtrt.statemachine;

import edu.hm.dako.lwtrt.pdu.LWTRTPdu;

/**
 * Beschreibt die zur Verfuegung stehenden Zustandsuebergaenge.
 * 
 * Jeder Zustand muss die Zustandsuebergaenge anbieten. Ist ein Zustanduebergang nicht vorgeshen wird
 * eine <code>IncorrectTransitionException</code> erzeugt.
 * 
 * Bei einem gueltigen Zustandsuebergang wird in den entsprechenden Zustand gewechselt. Ist eine
 * Antwort mittels eines PDUs an den Verbindungspartner vorgeshen wird die entsprechende PDU
 * vorbereitet. Dazu wird die OpID und die ResultCode mit den entsprechenden Werten gesetzt. Ist
 * keine Antwort vorgesehen wird <code>null</code> uebergeben.
 * 
 * @author bakomenko
 *
 */
public interface LWTRTState {
	
	/**
	 * Aktiver Verbindungsaufbau (1. Schritt)
	 * Dienstnehmer uebertraegt Wunsch zum Verbindungsaufbau.
	 * 
	 * @return Vorbereitete Antwort-PDU oder <code>null</code> wenn nicht geantwortet wird.
	 * @throws IncorrectTransitionException wenn der Zustandsuebergang nicht vorgesehen ist
	 */
	public LWTRTPdu connect() throws IncorrectTransitionException;
	
	/**
	 * Passiver Verbindungsaufbau (1. Schritt)
	 * Wunsch zum Verbindungsaufbau wird ueber eine PDU mitgeteilt
	 * 
	 * @return Vorbereitete Antwort-PDU oder <code>null</code> wenn nicht geantwortet wird.
	 * @throws IncorrectTransitionException wenn der Zustandsuebergang nicht vorgesehen ist
	 */
	public LWTRTPdu connectReq() throws IncorrectTransitionException;
	
	/**
	 * Aktiver Verbindungsaufbau (2. Schritt)
	 * Best+tigung des Verbindungsaufbaus wird +ber eine PDU mitgeteilt
	 * @return Vorbereitet Antwort-PDU oder <code>null</code> wenn nicht geantwortet wird.
	 * @throws IncorrectTransitionException wenn der Zustandsuebergang nicht vorgesehen ist
	 */
	public LWTRTPdu connectRsp() throws IncorrectTransitionException;

	/**
	 * Passiver Verbindungsaufbau (2. Schritt)
	 * Dienstnehmer bestaetigt den Wunsch zum Verbindungsaufbau
	 * 
	 * @return Vorbereitete Antwort-PDU oder <code>null</code> wenn nicht geantwortet wird.
	 * @throws IncorrectTransitionException wenn der Zustandsuebergang nicht vorgesehen ist
	 */
	public LWTRTPdu connectAcpt() throws IncorrectTransitionException;
	
	/**
	 * Aktiver Verbindungsabbau (1. Schritt)
	 * Dienstnehmer uebertraegt Wunsch zum Verbindungsabbau.
	 * 
	 * @return Vorbereitete Antwort-PDU oder <code>null</code> wenn nicht geantwortet wird.
	 * @throws IncorrectTransitionException wenn der Zustandsuebergang nicht vorgesehen ist
	 */
	public LWTRTPdu disConnect() throws IncorrectTransitionException;
	
	/**
	 * Passiver Verbindungsabbau (1. Schritt)
	 * Wunsch zum Verbindungsabbau des Verbindungspartners wird +ber eine PDU mitgeteilt
	 * 
	 * @return Vorbereitete Antwort-PDU oder <code>null</code> wenn nicht geantwortet wird.
	 * @throws IncorrectTransitionException wenn der Zustandsuebergang nicht vorgesehen ist
	 */
	public LWTRTPdu disConnectReq() throws IncorrectTransitionException;
	
	/**
	 * Aktiver Verbindungsabbau (2. Schritt)
	 * Best+tigung des Verbindungsaufbaus wird +ber eine PDU mitgeteilt
	 * 
	 * @return Vorbereitete Antwort-PDU oder <code>null</code> wenn nicht geantwortet wird.
	 * @throws IncorrectTransitionException wenn der Zustandsuebergang nicht vorgesehen ist
	 */
	public LWTRTPdu disConnectRsp() throws IncorrectTransitionException;

	/**
	 * Passiver Verbindungsabbau (2. Schritt)
	 * Dienstnehmer bestaetigt die abgebaute Verbindung
	 * 
	 * @return Vorbereitete Antwort-PDU oder <code>null</code> wenn nicht geantwortet wird.
	 * @throws IncorrectTransitionException wenn der Zustandsuebergang nicht vorgesehen ist
	 */
	public LWTRTPdu disConnectAcpt() throws IncorrectTransitionException;
	
	/**
	 * Timeout
	 * Das Auslaufen eines Timer wird angezeigt
	 * 
	 * @return Vorbereitete Antwort-PDU oder <code>null</code> wenn nicht geantwortet wird.
	 * @throws IncorrectTransitionException wenn der Zustandsuebergang nicht vorgesehen ist
	 */
	public LWTRTPdu timeout() throws IncorrectTransitionException;
	
	/**
	 * Data senden
	 * Wunsch Daten zu senden (aktiv)
	 * 
	 * @param chatPdu zu sendende PDU
	 * @return Vorbereitete Antwort-PDU oder <code>null</code> wenn nicht geantwortet wird.
	 * @throws IncorrectTransitionException wenn der Zustandsuebergang nicht vorgesehen ist
	 */
	public LWTRTPdu data(Object chatPdu) throws IncorrectTransitionException;
	
	/**
	 * Daten empfangen (passiv)
	 * Daten werden empfangen (DATA-REQ)
	 * 
	 * @param lwtrtPdu empfangenes PDU
	 * @return Vorbereitet Antwort-PDU oder <code>null</code> wenn nicht geantwortet wird.
	 * @throws IncorrectTransitionException wenn der Zustandsuebergang nicht vorgesehen ist
	 */
	public LWTRTPdu dataReq(LWTRTPdu lwtrtPdu) throws IncorrectTransitionException;
	
	/**
	 * Datenempfang wurde bestaetigt (akttiv)
	 * Eine Bestaetigung des Datenempfang wurde empfnagen (DATA-RSP)
	 * 
	 * @param lwtrtPdu empfangene Best+tigung
	 * @return Vorbereitete Antwort-PDU oder <code>null</code> wenn nicht geantwortet wird.
	 * @throws IncorrectTransitionException wenn der Zustandsuebergang nicht vorgesehen ist
	 */
	public LWTRTPdu dataRsp(LWTRTPdu lwtrtPdu) throws IncorrectTransitionException;
	
}

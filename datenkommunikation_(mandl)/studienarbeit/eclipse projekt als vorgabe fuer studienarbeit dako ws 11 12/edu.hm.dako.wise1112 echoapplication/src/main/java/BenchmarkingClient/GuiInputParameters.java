package BenchmarkingClient;


/**
 * Konfigurationsparameter fuer Lasttest
 * 
 * @author Mandl
 *
 */
public class GuiInputParameters {

	int numberOfClients; 	   	// Anzahl zu startender Client-Threads	
	int messageLength;		   	// Nachrichtenlaenge
	int clientThinkTime; 	   	// Denkzeit zwischen zwei Requests
	int numberOfMessages;	   	// Anzahl der Nachrichten pro Client-Thread
								// Typ der Implementierung 
	ImplementationType implementationType; 								
								// Typ der Messung fuer das Messprotokoll 
	MeasurementType measurementType;
	int remoteServerPort;	   	// UDP- oder TCP-Port des Servers, Default: 50000 
	String remoteServerAddress;	// Server-IP-Adresse, Default: "127.0.0.1"

	/**
	 * Konstruktor
	 * Belegung der Inputparameter mit Standardwerten
	 */
	public GuiInputParameters()
	{
		numberOfClients = 500; 
		clientThinkTime = 100;
		messageLength = 50;	
		numberOfMessages = 100;
		remoteServerPort = 50000; 
		remoteServerAddress = new String("127.0.0.1");
		implementationType = ImplementationType.TCPSingleThreaded;
		measurementType = MeasurementType.VarMsgLength;
	}
	
	
	/**
	 * Implementierungsvarianten des Lasttests mit verschiedenen Transportprotokollen
	 * 
	 * @author Mandl
	 */
	public enum ImplementationType {
		TCPSingleThreaded(0) {    
			public String toString() { return "Single-threaded TCP"; } },
		TCPMultiThreaded(1) {    
			public String toString() { return "Multi-threaded TCP"; } },
		UDPSingleThreaded(2) {    
			public String toString() { return "Single-threaded UDP"; } },
		UDPMultiThreaded(3) {    
			public String toString() { return "Multi-threaded UDP"; } },
		MyMultiThreaded(4) {    
			public String toString() { return "My Multi-threaded Implementation"; } },
		LwtrtMultiThreaded(5) {    
			public String toString() { return "Multi-threaded LWTRT"; } },
		RmiMultiThreaded(6) {    
			public String toString() { return "Multi-threaded RMI"; } };
		
		private int wert;  // Ordnung fuer Enum-Werte
		
		private ImplementationType(int wert) {
	        this.wert = wert;
	    }
		
		//TODO: mapping des Strings zum Wert fehlt 
		private ImplementationType() {
		
		}
	}
	

	/**
	 * Typen von unterstuetzten Messungen
	 * @author Mandl
	 *
	 */
	public enum MeasurementType {
		// Variation der Threadanzahl
		VarThreads {    
			public String toString() { return "Veraenderung der Threadanzahl"; } },		
		// Variation der Nachrichtenlaenge
		VarMsgLength {    
			public String toString() { return "Veraenderung der Nachrichtenlaenge"; } }
	}
	
	public int getNumberOfClients()
	{
		return numberOfClients;
	}
	
	public void setNumberOfClients(int numberOfClients)
	{
		this.numberOfClients = numberOfClients;
	}
	
	public int getMessageLength()
	{
		return messageLength;
	}
	
	public void setMessageLength(int messageLength)
	{
		this.messageLength = messageLength;
	}
	
	public void getMessageLength(int numberOfClients)
	{
		this.numberOfClients = numberOfClients;
	}
	
	public int getClientThinkTime()
	{
		return clientThinkTime;
	}
	
	public void setClientThinkTime(int clientThinkTime)
	{
		this.clientThinkTime = clientThinkTime;
	}
	
	public int getNumberOfMessages()
	{
		return numberOfMessages;
	}
	
	public void setNumberOfMessages(int numberOfMessages)
	{
		this.numberOfMessages = numberOfMessages;
	}
	
	public ImplementationType getImplementationType()
	{
		return implementationType;
	}
	
	public void setImplementationType(ImplementationType implementationType)
	{
		this.implementationType = implementationType;
	}
	
	public MeasurementType getMeasurementType()
	{
		return measurementType;
	}
	
	public void setMeasurementType(MeasurementType measurementType)
	{
		this.measurementType = measurementType;
	}
	
	public int getRemoteServerPort()
	{
		return remoteServerPort;
	}
	
	public void setRemoteServerPort(int remoteServerPort)
	{
		this.remoteServerPort = remoteServerPort;
	}
		
	public String getRemoteServerAddress()
	{
		return remoteServerAddress;
	}
	
	public void setRemoteServerAddress(String remoteServerAddress)
	{
		this.remoteServerAddress = remoteServerAddress;
	}
}
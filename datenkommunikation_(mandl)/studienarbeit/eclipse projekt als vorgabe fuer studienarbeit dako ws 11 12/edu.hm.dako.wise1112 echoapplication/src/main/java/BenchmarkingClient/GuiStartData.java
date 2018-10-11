package BenchmarkingClient;

/**
 * Klasse GuiStartData
 * 
 * Startparameter fuer Lasttest, die nach dem Start des Tests
 * auf Terminal (Bildschirm) ausgegeben werden sollen. 
 * 
 * @author Mandl
 *
 */
public class GuiStartData {

	long numberOfRequests;  	// Anzahl geplanter Requests
	String startTime;			// Zeit des Testbeginns
	
	
	public long getNumberOfRequests()
	{
		return numberOfRequests;
	}
	
	public void setNumberOfRequests(long numberOfRequests)
	{
		this.numberOfRequests = numberOfRequests;
	}
	
	public String getStartTime()
	{
		return startTime;
	}
	
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}
}
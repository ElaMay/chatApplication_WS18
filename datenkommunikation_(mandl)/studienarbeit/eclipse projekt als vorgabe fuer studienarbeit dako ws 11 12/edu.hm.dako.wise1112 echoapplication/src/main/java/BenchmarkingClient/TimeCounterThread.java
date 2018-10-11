package BenchmarkingClient;

/**
 * Klasse TimeCounterThread
 * 
 * Eigener Thread, der die Laufzeit eines Tests alle 2 Sekunden fuer
 * eine Ausgabe auf Terminal (Bildschirm) meldet.
 * 
 * @author Mandl
 *
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TimeCounterThread extends Thread {
	
	private static Log log = LogFactory.getLog(TimeCounterThread.class);
	
	private BenchmarkingClientGuiInterface out = null;
	
	private boolean running = true;
	
	private static int numberOfSeconds = 2;
	
	public TimeCounterThread(BenchmarkingClientGuiInterface clientGui)
	{
		setName("TimeCounterThread");
		this.out = clientGui;
	}
	
	/**
	 * Run-Methode fuer den Thread: 
	 * Erzeugt alle n Sekunden einen Zaehler und sendet ihn an die Ausgabe
	 */
	public void run() 
	{  
		log.debug(getName() + " gestartet");
		//System.out.println(getName() + " gestartet");	
		
		out.resetCurrentRunTime();
		
		while (running) {
			try {     
				Thread.sleep(1000*numberOfSeconds);
			}
			catch (InterruptedException e) {
				log.error("Sleep unterbrochen");
			}
			
			out.addCurrentRunTime(numberOfSeconds);
		}
	}
	
	/**
	 * Beenden des Threads
	 */
	public void stopThread()
	{
		running = false;
		log.debug(getName() + " gestoppt");		
		//System.out.println(getName() + " gestoppt");		
	}
}
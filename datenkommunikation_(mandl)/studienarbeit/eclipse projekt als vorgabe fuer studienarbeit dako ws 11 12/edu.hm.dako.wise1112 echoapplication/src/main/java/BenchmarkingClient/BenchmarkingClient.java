package BenchmarkingClient;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.EchoApplication.Basics.AbstractClientThread;
import edu.hm.dako.EchoApplication.Basics.SharedClientStatistics;
import edu.hm.dako.EchoApplication.Lwtrt.LWTRTMultiThreadedEchoClientThread;
import edu.hm.dako.EchoApplication.Rmi.RMIEchoClientThread;
import edu.hm.dako.EchoApplication.TCPMultiThreaded.TCPMultiThreadedEchoClientThread;
import edu.hm.dako.EchoApplication.TCPSingleThreaded.TCPSingleThreadedEchoClientThread;
import edu.hm.dako.EchoApplication.UDPMultiThreaded.UDPMultiThreadedEchoClientThread;
import edu.hm.dako.EchoApplication.UDPSingleThreaded.UDPSingleThreadedEchoClientThread;

import BenchmarkingClient.BenchmarkingStartInterface;
import BenchmarkingClient.GuiInputParameters;
import BenchmarkingClient.GuiInputParameters.ImplementationType;

/**
 * Klasse BenchmarkingClient 
 * 
 * Basisklasse zum Starten eines Benchmarks
 * 
 * @author Mandl
 */
public class BenchmarkingClient implements BenchmarkingStartInterface {
	private static Log log = LogFactory.getLog(BenchmarkingClient.class);

	// Daten aller Client-Threads zur Verwaltung der Statistik
	SharedClientStatistics sharedData;

	/**
	 * Methode liefert die aktuelle Zeit als String
	 * 
	 * @param cal Kalender
	 * @return Zeit als String
	 */
	private String getCurrentTime(Calendar cal) {
		return (cal.get(Calendar.DAY_OF_MONTH) + "."
				+ (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.YEAR)
				+ " " + cal.get(Calendar.HOUR_OF_DAY) + ":"
				+ cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
		// + ":" + cal.get(Calendar.MILLISECOND) );
	}

	@Override
	public void executeTest(GuiInputParameters parm,
			BenchmarkingClientGuiInterface clientGui) {
		clientGui.setMessageLine(parm.implementationType.toString()
				+ ": Benchmark gestartet");

		AbstractClientThread clientThreads[] = null;

		// Anzahl aller erwarteten Requests ermitteln
		long numberOfAllMessages = parm.getNumberOfClients()
				* parm.getNumberOfMessages();

		// Gemeinsamen Datenbereich fuer alle Threads anlegen
		sharedData = new SharedClientStatistics(parm.getNumberOfClients(),
				parm.getNumberOfMessages(), parm.getClientThinkTime());

		/**
		 * Startzeit ermitteln
		 */

		long startTime = 0;
		Calendar cal = Calendar.getInstance();
		startTime = cal.getTimeInMillis();
		String startTimeAsString = getCurrentTime(cal);

		/**
		 * Laufzeitzaehler-Thread erzeugen
		 */

		TimeCounterThread timeCounterThread = new TimeCounterThread(clientGui);
		timeCounterThread.start();

		/**
		 * Client-Threads in Abhaengigkeit des Implementierungstyps
		 * instanziieren und starten
		 */

		clientThreads = new AbstractClientThread[parm.getNumberOfClients()];

		for (int i = 0; i < parm.getNumberOfClients(); i++) {
			// Eine Instanz der ausgewählten EchoClientImplementierung holen
			clientThreads[i] = getClientThreadImplementation(parm
					.getImplementationType());

			// EchoClient initialisieren
			clientThreads[i].initialize(parm.getRemoteServerPort(),
					parm.getRemoteServerAddress(), i, parm.getMessageLength(),
					parm.getNumberOfMessages(), parm.getClientThinkTime(),
					sharedData);
			
			// Thread starten
			clientThreads[i].start();
		}

		/**
		 * Startwerte anzeigen
		 */

		GuiStartData startData = new GuiStartData();
		startData.setNumberOfRequests(numberOfAllMessages);
		startData.setStartTime(getCurrentTime(cal));
		clientGui.showStartData(startData);

		clientGui.setMessageLine("Alle " + parm.getNumberOfClients()
				+ " Clients-Threads gestartet");

		/**
		 * Auf das Ende aller Client-Threads warten
		 */

		for (int i = 0; i < parm.getNumberOfClients(); i++) {
			try {
				clientThreads[i].join();
				log.debug("Client " + i + " nach join, beendet");
			} catch (InterruptedException e) {
				log.error("join fuer " + clientThreads[i].getName()
						+ " wurde unterbrochen: " + e);
			}
		}

		/**
		 * Laufzeitzaehler-Thread beenden
		 */
		timeCounterThread.stopThread();

		/**
		 * Analyse der Ergebnisse durchfuehren, Statistikdaten berechnen und
		 * ausgeben
		 */
		// sharedData.printStatistic();

		/**
		 * Testergebnisse ausgeben
		 */

		clientGui.setMessageLine("Alle Clients-Threads beendet");

		GuiResultData resultData = new GuiResultData();

		resultData.setAvgRTT(sharedData.getAverageRTT() / 1000000);
		resultData.setMaxRTT(sharedData.getMaximumRTT() / 1000000);
		resultData.setMinRTT(sharedData.getMinimumRTT() / 1000000);
		resultData.setAvgServerTime(sharedData.getAverageServerTime()
				/ parm.getNumberOfClients() / 1000000);

		cal = Calendar.getInstance();
		resultData.setEndTime(getCurrentTime(cal));

		long elapsedTimeInSeconds = (cal.getTimeInMillis() - startTime) / 1000;
		resultData.setElapsedTime(elapsedTimeInSeconds);

		// TODO: CPU-Usage kann momentan noch nicht ermittelt werden
		resultData.setMaxCpuUsage(0);

		resultData.setMaxHeapSize(sharedData.getMaxHeapSize() / (1024 * 1024));

		resultData.setNumberOfResponses(sharedData
				.getSumOfAllReceivedMessages());
		resultData
				.setNumberOfSentRequests(sharedData.getNumberOfSentRequests());
		resultData.setNumberOfLostResponses(sharedData
				.getNumberOfLostResponses());

		clientGui.showResultData(resultData);
		clientGui.setMessageLine(parm.implementationType.toString()
				+ ": Benchmark beendet");

		/**
		 * Datensatz fuer Benchmark-Lauf auf Protokolldatei schreiben
		 */

		sharedData.writeStatisticSet("Benchmarking-EchoApp-Protokolldatei",
				parm.implementationType.toString(),
				parm.measurementType.toString(), startTimeAsString,
				resultData.getEndTime());

		System.exit(0);
	}

	private AbstractClientThread getClientThreadImplementation(
			ImplementationType implementationType) {

		switch (implementationType) {

		case TCPSingleThreaded:
			return new TCPSingleThreadedEchoClientThread();
		case TCPMultiThreaded:
			return new TCPMultiThreadedEchoClientThread();
		case UDPSingleThreaded:
			return new UDPSingleThreadedEchoClientThread();
		case UDPMultiThreaded:
			return new UDPMultiThreadedEchoClientThread();
		case RmiMultiThreaded:
			return new RMIEchoClientThread();
		case LwtrtMultiThreaded:
			return new LWTRTMultiThreadedEchoClientThread();
		default:
			throw new RuntimeException("unknown type: " + implementationType);
		}
	}
}
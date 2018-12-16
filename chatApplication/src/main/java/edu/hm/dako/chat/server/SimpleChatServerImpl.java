package edu.hm.dako.chat.server;

import java.net.DatagramSocket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.common.ClientListEntry;
import edu.hm.dako.chat.common.ExceptionHandler;
import edu.hm.dako.chat.connection.Connection;
import edu.hm.dako.chat.connection.ServerSocketInterface;
import javafx.concurrent.Task;

//import static edu.hm.dako.chat.server.ServerFactory.getDecoratedServerSocket;

/**
 * <p/>
 * Simple-Chat-Server-Implementierung
 *
 * @author Peter Mandl
 */
public class SimpleChatServerImpl extends AbstractChatServer {

	//Referenz zu dem AuditLogServer (in Bezug auf ChatServer)
	private AuditLogServerInterface auditServer;

	private static Log log = LogFactory.getLog(SimpleChatServerImpl.class);

	// Threadpool fuer Worker-Threads
	private final ExecutorService executorService;

	// Socket fuer den Listener, der alle Verbindungsaufbauwuensche der Clients
	// entgegennimmt
	private ServerSocketInterface socket;

	/**
	 * Konstruktor
	 *
	 * @param executorService
	 * @param socket
	 * @param serverGuiInterface
	 */
	public SimpleChatServerImpl(ExecutorService executorService,
								ServerSocketInterface socket, ChatServerGuiInterface serverGuiInterface) {
		log.debug("SimpleChatServerImpl konstruiert");
		this.executorService = executorService;
		this.socket = socket;
		this.serverGuiInterface = serverGuiInterface;
		counter = new SharedServerCounter();
		counter.logoutCounter = new AtomicInteger(0);
		counter.eventCounter = new AtomicInteger(0);
		counter.confirmCounter = new AtomicInteger(0);
		//AuditLog (UDP)
		try {
			auditServer = new AuditLogServerImpl(Executors.newCachedThreadPool(), new DatagramSocket(4445));
		} catch (Throwable e) {
			log.error("Could not create AuditLogServer!");
		}

	}

	@Override
	public void start() {
		//Start vom Server des AuditLogs
		auditServer.start();
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				// Clientliste erzeugen
				clients = SharedChatClientList.getInstance();

				while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
					try {
						// Auf ankommende Verbindungsaufbauwuensche warten
						System.out.println(
								"SimpleChatServer wartet auf Verbindungsanfragen von Clients...");

						Connection connection = socket.accept();
						log.debug("Neuer Verbindungsaufbauwunsch empfangen");
						// eine Nachricht an AuditLog Server, dass eine Connection kommt
						// Neuen Workerthread starten
						executorService.submit(new SimpleChatWorkerThreadImpl(connection, clients,
								counter, serverGuiInterface));
					} catch (Exception e) {
						if (socket.isClosed()) {
							log.debug("Socket wurde geschlossen");
						} else {
							log.error(
									"Exception beim Entgegennehmen von Verbindungsaufbauwuenschen: " + e);
							ExceptionHandler.logException(e);
						}
					}
				}
				return null;
			}
		};

		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}

	@Override
	public void stop() throws Exception {

		//Stoppen des Servers vom AuditLog
		auditServer.stop();
		// Alle Verbindungen zu aktiven Clients abbauen
		Vector<String> sendList = clients.getClientNameList();
		for (String s : new Vector<String>(sendList)) {
			ClientListEntry client = clients.getClient(s);
			try {
				if (client != null) {
					client.getConnection().close();
					log.error("Verbindung zu Client " + client.getUserName() + " geschlossen");
				}
			} catch (Exception e) {
				log.debug(
						"Fehler beim Schliessen der Verbindung zu Client " + client.getUserName());
				ExceptionHandler.logException(e);
			}
		}

		// Loeschen der Userliste
		clients.deleteAll();
		Thread.currentThread().interrupt();
		socket.close();
		log.debug("Listen-Socket geschlossen");
		executorService.shutdown();
		log.debug("Threadpool freigegeben");

		System.out.println("SimpleChatServer beendet sich");
	}
}
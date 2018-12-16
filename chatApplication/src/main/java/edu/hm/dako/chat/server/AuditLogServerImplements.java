package edu.hm.dako.chat.server;

import edu.hm.dako.chat.connection.ServerSocketInterface;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;


public class AuditLogServerImplements extends AbstractAuditLogServer {


    private static Log log = LogFactory.getLog(AuditLogServerImplements.class);

    // Threadpool fuer Worker-Threads
    private final ExecutorService executorService;

    // Socket fuer den Listener, der alle Verbindungsaufbauwuensche der Clients
    // entgegennimmt
    private DatagramSocket socket;


    public AuditLogServerImplements(ExecutorService executorService, DatagramSocket socket) {
        log.debug("AuditLogServerImplements konstruiert");
        this.executorService = executorService;
        this.socket = socket;
    }


   public void start() {
//        Task<Void> task = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                // Clientliste erzeugen
//                clients = SharedChatClientList.getInstance();
//
//                while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
//                    try {
//                        // Auf ankommende Verbindungsaufbauwuensche warten
//                        System.out.println(
//                                "SimpleChatServer wartet auf Verbindungsanfragen von Clients...");
//
//                        Connection connection = socket.accept();
//                        log.debug("Neuer Verbindungsaufbauwunsch empfangen");
//                        // eine Nachricht an AuditLog Server, dass eine Connection kommt
//                        // Neuen Workerthread starten
//                        executorService.submit(new SimpleChatWorkerThreadImpl(connection, clients,
//                                counter, serverGuiInterface));
//                    } catch (Exception e) {
//                        if (socket.isClosed()) {
//                            log.debug("Socket wurde geschlossen");
//                        } else {
//                            log.error(
//                                    "Exception beim Entgegennehmen von Verbindungsaufbauwuenschen: " + e);
//                            ExceptionHandler.logException(e);
//                        }
//                    }
//                }
//                return null;
//            }
//        };
//
//        Thread th = new Thread(task);
//        th.setDaemon(true);
//        th.start();
  }


  public void stop() {
        // Alle Verbindungen zu aktiven Clients abbauen
//        Vector<String> sendList = clients.getClientNameList();
//        for (String s : new Vector<String>(sendList)) {
//            ClientListEntry client = clients.getClient(s);
//            try {
//                if (client != null) {
//                    client.getConnection().close();
//                    log.error("Verbindung zu Client " + client.getUserName() + " geschlossen");
//                }
//            } catch (Exception e) {
//                log.debug(
//                        "Fehler beim Schliessen der Verbindung zu Client " + client.getUserName());
//                ExceptionHandler.logException(e);
//            }
//      }
//
//        // Loeschen der Userliste
//        clients.deleteAll();
//        Thread.currentThread().interrupt();
//        socket.close();
//        log.debug("Listen-Socket geschlossen");
//        executorService.shutdown();
//        log.debug("Threadpool freigegeben");
//
//        System.out.println("SimpleChatServer beendet sich");
    }
//    }

    public void run() {

    }
}

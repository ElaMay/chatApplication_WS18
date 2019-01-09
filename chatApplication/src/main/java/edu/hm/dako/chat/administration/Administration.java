package edu.hm.dako.chat.administration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Sophia Weißenberger
 * @author Diana Marjanovic
 *
 * Administrationsprogramm
 *
 * - auslesen des auditLogs -> Pfad angeben?
 *      -   Pfad als String einlesen von cmd line
 * - aufbereiten der Informationen -> Statistik nach clients aufdröseln
 * - ausgeben -> wo?
 */
public class Administration {

    /**
     * Das sind die benötigten Objektvariablen.
     */
    private BufferedReader bufferedReader;

    private ListOfClients clients = new ListOfClients();

    int messageCounter = 0;

    /**
     * Die Methode dient dazu, dass die Daten aus der Log-Datei in die Kommandozeile eingetragen wird.
     * @param fileName
     */
    private void loadFile(String fileName) {
        File file = new File(fileName);

        if (!file.canRead() || !file.isFile())
            System.exit(0);

        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(fileName));
            String line = null;
            while ((line = in.readLine()) != null) {
                //System.out.println("Gelesene Zeile: " + line);
                String[] s = line.split("/");

                //Für den Fall, falls wir es brauchen sollten.
                if(s.length == 6) {
                    String s1 = s[0]; //ClientName
                    String s2 = s[1]; //PDU-Type
                    String s3 = s[2]; //Time-Stamp
                    String s4 = s[3]; //Server-Thread
                    String s5 = s[4]; //Client-Thread
                    String s6 = s[5]; //Message

                    if (s1.equals(clients.getClient(s1).getClientName())) {
                        if (s2.equals("Login-Request")) {
                            clients.getClient(s1).setLoginTimestamp(s3);
                            //Wenn ein Login bereits stattfand, dann wird der TimeStamp überschrieben.
                        } else if (s2.equals("Logout-Request")) {
                            clients.getClient(s1).setLogoutTimestamp(s3);
                        } else if (s2.equals("Chat-Message-Request")) {
                            clients.getClient(s1).setMessageCounter(clients.getClient(s1).getMessageCounter()+1);
                            messageCounter = messageCounter + 1;
                        } else {

                        }
                    } else if (s1 != null) {
                        if (s2.equals("Login-Request")) {
                            ClientStatistic clientNew = new ClientStatistic();
                            clientNew.setClientName(s1);
                            clientNew.setLoginTimestamp(s3);
                            clients.addClients(clientNew);
                        } else {

                        }

                    } else {

                    }
                } else if (s.length == 5) {
                    String s1 = s[0]; //ClientName
                    String s2 = s[1]; //PDU-Type
                    String s3 = s[2]; //Time-Stamp
                    String s4 = s[3]; //Server-Thread
                    String s5 = s[4]; //Client-Thread

                    //if (s1.equals(clients.getClient(s1).getClientName())) {
                    if (clients.getClient(s1) != null && s1.equals(clients.getClient(s1).getClientName())) {
                        if (s2.equals("Login-Request")) {
                            clients.getClient(s1).setLoginTimestamp(s3);
                            //Wenn ein Login bereits stattfand, dann wird der TimeStamp überschrieben.
                        } else if (s2.equals("Logout-Request")) {
                            System.out.println("Lo");
                            clients.getClient(s1).setLogoutTimestamp(s3);
                        } else if (s2.equals("Chat-Message-Request")) {
                            System.out.println("M");
                            clients.getClient(s1).setMessageCounter(clients.getClient(s1).getMessageCounter()+1);
                            messageCounter = messageCounter + 1;
                        } else {

                        }
                    } else if (s1 != null) {
                        if (s2.equals("Login-Request")) {
                            ClientStatistic clientNew = new ClientStatistic();
                            clientNew.setClientName(s1);
                            clientNew.setLoginTimestamp(s3);
                            clients.addClients(clientNew);
                        } else {

                        }

                    } else {

                    }
                }
            }
            System.out.println("Anzahl der insgesamt angemeldeten Clients: "+ clients.getListSize());
            System.out.println("Anzahl der insgesamt gesendetetn Nachrichten: "+ messageCounter);

            for (int i = 0; i < clients.getListSize(); i++) {
               System.out.println(clients.getClientsByIndex(i).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * Eine main-Methode zum testen dieser Klasse.
     * @param args
     */
    public static void main(String[] args) {
        String fileName = args[0];
        Administration a = new Administration();
        a.loadFile(fileName);
    }

}

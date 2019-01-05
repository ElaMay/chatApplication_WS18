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

    private BufferedReader bufferedReader;


    private static void loadFile(String fileName) {
        File file = new File(fileName);

        if (!file.canRead() || !file.isFile())
            System.exit(0);

        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(fileName));
            String line = null;
            while ((line = in.readLine()) != null) {
                //System.out.println("Gelesene Zeile: " + line);
                //TODO: Magic
                if (line.startsWith("A")) {
                    //ToDo: do nothing.
                } else if (line.startsWith("P")) {
                    //ToDo: pdu-type checken.
                    //ToDo: Wenn es ein logIn ist, dann überprüfen, ob client existiert. Wenn nicht, in die Liste neu einfügen, LogIn setzen.
                    //ToDo: Wenn ChatMessageRequest -> Client finden, Massege counter erhöhen.
                    //ToDo: Wenn LogOut -> Client finden, LogOut setzen.
                }  else if (line.startsWith("D")) {

                }  else if (line.startsWith("u")) {

                }  else if (line.startsWith("w")) {

                }  else if (line.startsWith("c")) {

                }  else if (line.startsWith("m")) {

                }  else if (line.startsWith("*")) {
                    //ToDo: do nothing.
                } else {
                    //ToDo: do nothing.
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                }
        }
    }


    public static void main(String[] args) {
        String fileName = args[0];
        loadFile(fileName);
    }

}

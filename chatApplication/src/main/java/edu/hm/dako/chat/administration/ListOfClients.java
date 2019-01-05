package edu.hm.dako.chat.administration;

import java.util.ArrayList;


public class ListOfClients {

    //Namen der Clients, die dann aufgelistet werden.
    private String clientName;


    //Eine ArrayList wird für diese Klasse erzeugt.
    private ArrayList<ClientStatistic> clientList = new ArrayList<>();


    //Konstruktor für diese Klasse.
    public ListOfClients () {
        this.clientName = clientName;
    }


    /**
     * Liefert die Anzahl der Clients in ArrayList.
     */
    public int getListSize() {
        return clientList.size();
    }


    /**
     * Speichert alle Clients in einer Liste.
     */
    public void addClients(ClientStatistic newClient) {
        clientList.add(newClient);
        // String[] clients = new String[];
    }


    /**
     * Liefert den Namen aus der Liste.
     * @param clientName
     * @return
     */
    public ClientStatistic getClient (String clientName) {
        for (int i = 1; i < clientList.size(); i++) {
            if (clientList.get(i).getClientName().matches(clientName)) {
                return clientList.get(i);
            } else {
                //ToDo: do nothing.
            }
        }
        return null;
    }

}

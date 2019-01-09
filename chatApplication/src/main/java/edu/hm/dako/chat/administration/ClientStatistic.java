package edu.hm.dako.chat.administration;

/**
 * Klasse für den ClientStatistc Objekt.
 * @author Sophia Weißenberger
 */
public class ClientStatistic {

    /**
     * Die benötigten Objektvariablen.
     */
    private String clientName;
    private String loginTimestamp;
    private int messageCounter;
    private String logoutTimestamp;


    /**
     * Konstruktor für den ClientStatistic Objekt
     */
    public ClientStatistic(){
        this.clientName = null;
        this.loginTimestamp = null;
        this.messageCounter = 0;
        this.logoutTimestamp = null;
    }


    /**
     * toString() Methode für den ClientStatistic
     * @return String
     */
    public String toString() {
        return "\n"
                + "Statistic**********************************"
                + "\n" + "Client: " + this.clientName +   "\n" + "Login Timestamp: " + this.loginTimestamp + "\n" + "Messages: " + this.messageCounter
                + "\n" + "Logout Timestamp: " + this.logoutTimestamp+ "\n"
                + "**********************************Statistic"
                + "\n";
    }



    /**
     * Getter für den clientName
     * @return clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Getter für den loginTimestamp
     * @return loginTimestamp
     */
    public String getLoginTimestamp() {
        return loginTimestamp;
    }

    /**
     * Getter für den messageCounter
     * @return messageCounter
     */
    public int getMessageCounter() {
        return messageCounter;
    }

    /**
     * Getter für den logoutTimestamp
     * @return logoutTimestamp
     */
    public String getLogoutTimestamp() {
        return logoutTimestamp;
    }

    /**
     * Setter füe den clientName
     * @param clientName
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * Setter für den loginTimestamp
     * @param loginTimestamp
     */
    public void setLoginTimestamp(String loginTimestamp) {
        this.loginTimestamp = loginTimestamp;
    }

    /**
     * Setter für den messageCounter
     * @param messageCounter
     */
    public void setMessageCounter(int messageCounter) {
        this.messageCounter = messageCounter;
    }

    /**
     * Setter für den logoutTimestamp
     * @param logoutTimestamp
     */
    public void setLogoutTimestamp(String logoutTimestamp) {
        this.logoutTimestamp = logoutTimestamp;
    }


}

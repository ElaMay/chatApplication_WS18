package edu.hm.dako.chat.administration;

/**
 * Class for ClientStatistc Objekt.
 * @author Sophia Weißenberger
 */
public class ClientStatistic {

    private String clientName;
    private String loginTimestamp;
    private int messageCounter;
    private String logoutTimestamp;


    /**
     * Constructor for ClientStatistic Object
     */
    public ClientStatistic(){
        this.clientName = null;
        this.loginTimestamp = null;
        this.messageCounter = 0;
        this.logoutTimestamp = null;
    }


    /**
     * toString() Method for ClientStatistic
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
     * Getter for clientName
     * @return clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Getter for loginTimestamp
     * @return loginTimestamp
     */
    public String getLoginTimestamp() {
        return loginTimestamp;
    }

    /**
     * Getter for messageCounter
     * @return messageCounter
     */
    public int getMessageCounter() {
        return messageCounter;
    }

    /**
     * Getter for logoutTimestamp
     * @return logoutTimestamp
     */
    public String getLogoutTimestamp() {
        return logoutTimestamp;
    }

    /**
     * Setter for clientName
     * @param clientName
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * Setter for loginTimestamp
     * @param loginTimestamp
     */
    public void setLoginTimestamp(String loginTimestamp) {
        this.loginTimestamp = loginTimestamp;
    }

    /**
     * Setter for messageCounter
     * @param messageCounter
     */
    public void setMessageCounter(int messageCounter) {
        this.messageCounter = messageCounter;
    }

    /**
     * Setter for logoutTimestamp
     * @param logoutTimestamp
     */
    public void setLogoutTimestamp(String logoutTimestamp) {
        this.logoutTimestamp = logoutTimestamp;
    }


}

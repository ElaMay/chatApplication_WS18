package edu.hm.dako.lwtrt;

/**
 * The Class LWTRTException.
 * 
 * @author Hochschule Muenchen
 * @version 1.0.0
 */
public class LWTRTException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -46473630383065913L;

    /**
     * Instantiates a new LWTRT exception.
     * 
     * @param msg the msg
     */
    public LWTRTException(String msg) {
        super(msg);
    }

    /**
     * Instantiates a new LWTRT exception.
     * 
     * @param msg the msg
     * @param e the e
     */
    public LWTRTException(Throwable e) {
        super(e);
    }
}

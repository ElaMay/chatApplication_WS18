package edu.hm.dako.lwtrt;


/**
 * The Class LWTRTAbortException.
 * 
 * @author Bakomenko
 * @version 1.0.0
 */
public class LWTRTAbortException extends LWTRTException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -46473630383065913L;

    /**
     * Instantiates a new LWTRT Abort exception.
     * 
     * @param msg the msg
     */
    public LWTRTAbortException(String msg) {
        super(msg);
    }

    /**
     * Instantiates a new LWTRT Abort exception.
     * 
     * @param msg the msg
     * @param e the e
     */
    public LWTRTAbortException(Throwable e) {
        super(e);
    }
}

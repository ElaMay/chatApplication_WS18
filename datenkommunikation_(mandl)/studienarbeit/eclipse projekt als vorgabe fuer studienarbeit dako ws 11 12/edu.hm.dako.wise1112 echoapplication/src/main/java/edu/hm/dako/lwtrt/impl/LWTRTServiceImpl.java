package edu.hm.dako.lwtrt.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.hm.dako.lwtrt.LWTRTAbortException;
import edu.hm.dako.lwtrt.LWTRTConnection;
import edu.hm.dako.lwtrt.LWTRTService;
import edu.hm.dako.lwtrt.LWTRTException;

/**
 * Klasse LWTRTServiceImpl
 * 
 * @author Bakomenko, Mandl
 * @version 1.0.0
 */
public class LWTRTServiceImpl implements LWTRTService {

    /** Singleton-Instanz von LWTRTService */
    public static LWTRTService INSTANCE = new LWTRTServiceImpl();
 
    /** Liste aller aktiv genutzten Ports */
    private Map<Integer, PortHandle> portsInUse = new ConcurrentHashMap<Integer, PortHandle>();
    
    /**
     * Leerer privater Konstruktor. Es handelt sich hier um
	 * ein Singleton.
     */
    private LWTRTServiceImpl(){}
    
    /*
     * (non-Javadoc)
     * 
     * @see edu.hm.dako.wise0910.lwtrt.Instance#register(int, int[])
     */
    @Override
    public void register(int localPort) throws LWTRTException {  	
        if (portsInUse.containsKey(localPort)) {
            throw new LWTRTException("Port is already in use");
        }
        
        PortHandle porthandle = new PortHandle(localPort);
        portsInUse.put(localPort, porthandle);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.hm.dako.wise0910.lwtrt.Instance#unregister()
     */
    @Override
    public void unregister(int localPort) throws LWTRTException {
        if (!portsInUse.containsKey(localPort)) {
            throw new LWTRTException("There is no connection registered for port "+localPort);
        } else {
        	PortHandle portHandle = portsInUse.get(localPort);
        	portHandle.requestStop();
        	portsInUse.remove(localPort);
        }
    }
    
    @Override
    public LWTRTConnection connect(int localPort, String remoteAddress, int remotePort) throws LWTRTException, LWTRTAbortException {
    	return portsInUse.get(localPort).connect(remoteAddress, remotePort);
    }

    @Override
    public LWTRTConnection accept(int localPort) throws LWTRTException {
    	return portsInUse.get(localPort).accept();
    }    
}

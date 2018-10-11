package edu.hm.dako.EchoApplication.Rmi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import java.rmi.registry.Registry;

/**
 * Klasse RMIEchoServer
 * 
 * Startet das RMI-Registry lokal und den RMI-Echo-Server.
 * 
 * @author Mandl
 *
 */

public class RMIEchoServer
{
	private static Log log = LogFactory.getLog(RMIEchoServerImpl.class);
	/**
	 * Start des Echo-Servers
	 * @param args
	 */   	
	public static void main (String args[])
   	{
		PropertyConfigurator.configureAndWatch("log4j.server.properties", 60 * 1000);
		Registry rmiRegistry = null;
		
   	    // RMI Registry lokal starten
   	    try {
   	    	rmiRegistry = java.rmi.registry.LocateRegistry.createRegistry(1099);
   	    	System.out.println("RMI registry bereit");
   	    	log.debug("RMI registry bereit");
   	    } catch (Exception e) {
   	    	System.out.println("Exception beim Starten der RMI registry:");
   	    	log.error("Exception beim Starten der RMI registry: " + e);
   	    	System.exit(1);
   	    }
   	    	
   	    //TODO
   	}
}
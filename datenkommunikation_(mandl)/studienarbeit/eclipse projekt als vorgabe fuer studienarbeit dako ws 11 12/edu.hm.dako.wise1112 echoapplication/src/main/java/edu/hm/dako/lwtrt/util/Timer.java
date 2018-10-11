/*
 * $Date$
 * $Revision$
 * $Author$
 * $HeadURL$
 * $Id$
 */

package edu.hm.dako.lwtrt.util;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Timer
 * @author Hochschule Muenchen
 *
 */
public class Timer {
	  private static Log log = LogFactory.getLog(Timer.class);

	private String desc;
	private long nanos = 0;
	/**
	 * get nanoseconds
	 * @return
	 */
	public long getNanos() {
		return nanos;
	}

	/**
	 * Standardconstructor
	 * @param clazz 
	 */
	public Timer(){
			
	}
	
	/**
	 * start timer
	 * @param desc
	 */
	public void start(String desc){
		this.desc = desc;
		log.trace(MessageFormat.format("{0} monitoring started", desc));
		nanos = System.nanoTime();
	}
	
	/**
	 * stop timer
	 */
	public void stop(){
		if(nanos == 0){
			throw new IllegalStateException("Timer not started?");
		}
		nanos = System.nanoTime()-nanos;
		log.trace(MessageFormat.format("{0} timer stopped after {1} Nanoseconds",
				desc, nanos));
	}
}

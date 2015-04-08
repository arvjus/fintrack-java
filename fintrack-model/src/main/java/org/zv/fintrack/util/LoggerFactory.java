package org.zv.fintrack.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class LoggerFactory {
	/**
	 * C-tor for utility class.
	 */
	private LoggerFactory() {
	}
	
	/**
	 * Make logger for a caller class.
	 * 
	 * @return logger instance.
	 */
	public static Log make() {
		Throwable t = new Throwable();
		StackTraceElement directCaller = t.getStackTrace()[1];
		return LogFactory.getLog(directCaller.getClassName());
	}
}

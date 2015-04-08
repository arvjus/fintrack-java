package org.zv.common.mvc;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Create command instance for given action string.
 *  
 * @author arvid.juskaitis
 */
public interface CommandFactory {
	
	/**
	 * Do initialization here.
	 * @param application - to send init parameters.
	 */
	void init(ServletContext application) throws IOException;
	
	/**
	 * Create command instance for given action string.
	 * Method should create and set command context.
	 * 
	 * @param request - original request
	 * @return command instance.
	 */
	CommandSupport create(HttpServletRequest request) throws CommandException;
}

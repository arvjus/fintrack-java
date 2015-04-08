package org.zv.common.mvc;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Navigate view and dispatch request to the view.
 *  
 * @author arvid.juskaitis
 */
public interface ViewNavigator {

	/**
	 * Do initialization here.
	 * 
	 * @throws IOException 
	 * @param application - to send init parameters.
	 */
	void init(ServletContext application) throws IOException;
	
	/**
	 * Navigate view and dispatch request to the view.
	 * 
	 * @param view - logical view or view file.
	 * @param request
	 * @param response
	 */
	void dispatch(String view, HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException;
}

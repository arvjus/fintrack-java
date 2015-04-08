package org.zv.common.mvc.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;

import org.zv.common.mvc.ViewNavigator;

/**
 * Navigate view and dispatch request to the view.  
 * 
 * Default configuration of view navigator simply maps logical view to physical view file. These mappings 
 * are defined in a propery file. If no mapping exist then it is assumed that the locica view is the physical file. 
 *  
 * @author arvid.juskaitis
 */
public class DefaultViewNavigator implements ViewNavigator {

	/**
	 * The name of property file. Could be configured in servlet configuration 
	 * with <init-param>viewProperties</init-param>
	 */
	private String viewProperties = "/view.properties";
	
	/**
	 * Keep view navigation mappings here.
	 */
	private Properties properties = new Properties();
	
	/**
	 * {@inheritDoc}
	 */
	public void init(ServletContext application) throws IOException {
		String value = application.getInitParameter("viewProperties");
		if (value != null) {
			viewProperties = value;
		}

		InputStream inputStream = getClass().getResourceAsStream(viewProperties);
		if (inputStream == null) {
			throw new IOException("cannot load '" + viewProperties + "' property file");
		}
		properties.load(inputStream);
	}

	/**
	 * {@inheritDoc}
	 */
	public void dispatch(String view, HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		
		String mappedView = properties.getProperty(view);
		if (mappedView != null) {
			view = mappedView;
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher(view);
		dispatcher.forward(request, response);
	}
}

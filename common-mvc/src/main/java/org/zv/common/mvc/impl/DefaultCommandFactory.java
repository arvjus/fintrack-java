package org.zv.common.mvc.impl;

import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.zv.common.mvc.CommandContext;
import org.zv.common.mvc.CommandException;
import org.zv.common.mvc.CommandFactory;
import org.zv.common.mvc.CommandSupport;

/**
 * Create command instance for given action string.
 * 
 * Default implementation of command factory simply maps action to fully qualified command class name. These 
 * mappings exist in a propery file. An action string is extracted from request uri as following:
 * Remove context part and parameters if any, then remove extension. Remaining string is an action + optional method,
 * separated by '!' character. If no method specified, 'execute' is used. Eg.
 *   http://localhost:8080/fintrack/home!refresh.page?type=123
 *   action: '/home', method: 'refresh'
 * or 
 *   http://localhost:8080/fintrack/user/home.page?type=123
 *   action: '/user/home', method: 'execute'
 *  
 * @author arvid.juskaitis
 */
public class DefaultCommandFactory implements CommandFactory {

	/**
	 * The name of property file. Could be configured in servlet configuration 
	 * with <init-param>commandProperties</init-param>
	 */
	private String commandProperties = "/command.properties";

	/**
	 * Keep command mappings here.
	 */
	private Properties properties = new Properties();
	
	/**
	 * Application.
	 */
	private ServletContext servletContext;
	
	/**
	 * {@inheritDoc}
	 */
	public void init(ServletContext servletContext) throws IOException {
		this.servletContext = servletContext;
		
		// Load properties
		String value = servletContext.getInitParameter("commandProperties");
		if (value != null) {
			commandProperties = value;
		}
		InputStream inputStream = getClass().getResourceAsStream(commandProperties);
		if (inputStream == null) {
			throw new IOException("cannot load '" + commandProperties + "' property file");
		}
		properties.load(inputStream);
	}

	/**
	 * {@inheritDoc}
	 */
	public CommandSupport create(HttpServletRequest request) throws CommandException {

		// Create context
		CommandContext commandContext = createCommandContext(request);
		
		// Get mapping for action
		String className = properties.getProperty(commandContext.getAction());
		if (className == null) {
			throw new CommandException("no mapping for '" + commandContext.getAction() + "' action");
		}
		
		// Load intance
		CommandSupport command;
		try {
			command = (CommandSupport) Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new CommandException(e);
		}
	
		// Inject context as dependencies
		command.setCommandContext(commandContext);
		
		return command;
	}
	
	/**
	 * Parse request properties, extract action/method and put it into command context.
	 *  
	 * @param request
	 * @return new instance of CommandContext 
	 */
	protected CommandContext createCommandContext(HttpServletRequest request) throws CommandException {

		// Get action and method
		String action = request.getRequestURI();
		String method = "execute";

		// Remove context
		if (request.getContextPath().length() > 1) {
			action = action.substring(request.getContextPath().length());
		}
		
		// Remove parameters
		int pos = action.indexOf('?');
		if (pos != -1) {
			action = action.substring(0, pos);
		}
		
		// Remove extension
		pos = action.lastIndexOf('.');
		if (pos != -1) {
			action = action.substring(0, pos);
		} else {
			throw new CommandException("malformed url: " + request.getRequestURL());
		}
		
		// Split action path into action and method
		pos = action.lastIndexOf('!');
		if (pos != -1) {
			method = action.substring(pos + 1);
			action = action.substring(0, pos);
		}
		
		// Check if values are valid
		if (action.length() == 0 || method.length() == 0) {
			throw new CommandException("malformed url: " + request.getRequestURL());
		}
		
		return new CommandContext(action, method); 
	}
}

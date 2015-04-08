package org.zv.common.mvc;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Main front controller servlet.
 * 
 * @author arvid.juskaitis
 */
@SuppressWarnings("serial")
public class Controller extends HttpServlet {

	/**
	 * The name of command factory class. Could be configured in servlet configuration  
	 * with <init-param>commandFactoryClass</init-param>
	 */
	private String commandFactoryClass = "org.zv.common.mvc.impl.DefaultCommandFactory";

	/**
	 * Command factory.
	 */
	private CommandFactory commandFactory;
	
	/**
	 * The name of view navigator class. Could be configured in servlet configuration 
	 * with <init-param>viewNavigatorClass</init-param>
	 */
	private String viewNavigatorClass = "org.zv.common.mvc.impl.DefaultViewNavigator";

	/**
	 * View navigator.
	 */
	private ViewNavigator viewNavigator;
	
	/**
	 * Parameters types for invoked command.
	 */
	private Class[] parameterTypes = { HttpServletRequest.class, HttpServletResponse.class }; 

	/**
	 * Helper converter class.
	 */
	ValueConverter valueConverter = new ValueConverter();
	
	
	@Override
	public void init() throws ServletException {
		log("CommandBroker.init started");
		
		// get optional init parameters
		String value;
		value = getServletContext().getInitParameter("commandFactoryClass");
		if (value != null) {
			commandFactoryClass = value;
		}
		value = getServletContext().getInitParameter("viewNavigatorClass");
		if (value != null) {
			viewNavigatorClass = value;
		}
		
		// load command factory
		try {
			commandFactory = (CommandFactory) Class.forName(commandFactoryClass).newInstance();
			commandFactory.init(getServletContext());
		} catch (Exception e) {
			log("error during load of class " + commandFactoryClass);
			throw new ServletException(e);
		} 

		// load view navigator
		try {
			viewNavigator = (ViewNavigator) Class.forName(viewNavigatorClass).newInstance();
			viewNavigator.init(getServletContext());
		} catch (Exception e) {
			log("error during load of class " + commandFactoryClass);
			throw new ServletException(e);
		} 

		log("CommandBroker.init finished");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		doProcess(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		
		doProcess(request, response);
	}

	/**
	 * Process GET, POST requests.
	 * 
	 * @param request
	 * @param response
	 */
	private void doProcess(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		
		// Create a command
		CommandSupport command = commandFactory.create(request);
		
		// Inject parameters
		for (Method method : command.getClass().getMethods()) {
			String name = method.getName();
			if (!name.equals("setCommandContext") && name.length() > 3 && name.startsWith("set")) {
				// Resource DI
				Resource annotation = method.getAnnotation(Resource.class);
				if (annotation != null) {
					try {
						Context context = new InitialContext();
						String jndiName = "";
						if (!"".equals(annotation.name())) {
							jndiName = "java:comp/env/" + annotation.name();
						} else if (!"".equals(annotation.globalName())) {
							jndiName = annotation.globalName();
						} else {
							throw new Exception("annotation name is required");
						}
						Object obj = context.lookup(jndiName);
						if (obj != null) {
							method.invoke(command, new Object [] { obj });
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					continue;
				}
				
				// Request parameter
				Class<?> [] parameterTypes = method.getParameterTypes();
				if (parameterTypes != null && parameterTypes.length == 1) {
					try {
						String parameterName = Character.toLowerCase(name.charAt(3)) + name.substring(4);
						String [] parameterValues = request.getParameterValues(parameterName);
						if (parameterValues != null && parameterValues.length > 0) {
							Object value = valueConverter.valueOf(parameterValues, parameterTypes[0]);
							method.invoke(command, new Object [] { value });
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		// Invoke command
		String view;
		try {
			// Execute method
			String methodName = command.getCommandContext().getMethod(); 
			Method method = command.getClass().getDeclaredMethod(methodName, parameterTypes);
			view = (String) method.invoke(command, new Object [] { request, response });
		} catch (Exception e) {
			throw new CommandException(e);
		} 

		// Put properties into request scope attribtes
		for (Method method : command.getClass().getMethods()) {
			String name = method.getName();
			if (!name.equals("getCommandContext") && !name.equals("getApplicationContext") && !name.equals("getClass") &&
				name.length() > 3 && name.startsWith("get")) {
				try {
					String attributeName = Character.toLowerCase(name.charAt(3)) + name.substring(4);
					request.setAttribute(attributeName, method.invoke(command, (Object []) null));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		// Forward to the view
		if (view != null) {
			viewNavigator.dispatch(view, request, response);
		}
	}
}

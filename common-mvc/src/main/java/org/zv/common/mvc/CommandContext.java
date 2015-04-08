package org.zv.common.mvc;

/**
 * Keeps context info for command.
 * 
 * @author arvid.juskaitis
 */
public class CommandContext {

	/**
	 * Action string.
	 */
	private String action;

	/**
	 * Method to execute.
	 */
	private String method;

	/**
	 * Constructor.
	 * 
	 * @param action
	 * @param method
	 */
	public CommandContext(String action, String method) {
		this.action = action;
		this.method = method;
	}
	
	public String getAction() {
		return action;
	}

	public String getMethod() {
		return method;
	}
}

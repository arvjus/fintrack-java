package org.zv.common.mvc;

import javax.servlet.ServletException;

/**
 * Used in command related operations.  
 * 
 * @author arvid.juskaitis
 */
@SuppressWarnings("serial")
public class CommandException extends ServletException {
	public CommandException(String msg) {
		super(msg);
	}

	public CommandException(Throwable exc) {
		super(exc);
	}
}

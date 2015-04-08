package org.zv.common.mvc;

/**
 * Handle request from servlet.
 * 
 * @author arvid.juskaitis
 */
public abstract class CommandSupport {

	/**
	 * Command context.
	 */
	private CommandContext commandContext;

	public CommandContext getCommandContext() {
		return commandContext;
	}

	public void setCommandContext(CommandContext commandContext) {
		this.commandContext = commandContext;
	}
}

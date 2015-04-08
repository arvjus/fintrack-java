package org.zv.fintrack.command;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.zv.common.mvc.CommandSupport;

/**
 * Handle logout request and redirect to home page.
 * 
 * @author Arvid Juskaitis
 */
@SuppressWarnings("serial")
public class LogoutCommand extends CommandSupport {

	/**
	 * Invalidate session and redirect to home page.
	 *  
	 * @param request
	 * @param response
	 * @return view to forward
	 */
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		// Terminate session
		HttpSession session = request.getSession();
		if (session != null) {
			session.invalidate();
		}
		
		// Redirect to home
		try {
			response.sendRedirect(response.encodeURL(request.getContextPath() + "/user/home.page"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return null;
	}
}

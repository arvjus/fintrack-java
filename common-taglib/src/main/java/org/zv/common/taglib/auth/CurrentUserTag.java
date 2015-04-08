package org.zv.common.taglib.auth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Returns a name of currently inlogged user. If no users has logged in, the empty string
 * is returned or value of 'default' attribute if it set. 
 * 
 * @author Arvid Juskaitis
 */
@SuppressWarnings("unchecked")
public class CurrentUserTag extends TagSupport {

	/**
	 * Tag attribute - this value is returned if user is not logged in.
	 */
	private String defaultValue;
	
	public String getDefault() {
		return defaultValue;
	}

	public void setDefault(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String user = request.getRemoteUser();
		if (user == null && defaultValue != null) {
			user = defaultValue;
		}
		if (user != null) {
			try {
				pageContext.getOut().write(user);
			} catch (IOException e) {
				throw new JspException(e); 
			}
		}
		return SKIP_BODY;
	}
}

package org.zv.common.taglib.auth;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Test if user belongs to at least one of listed roles and if so, evaluate body, unless
 * attribute 'not' is set to true, which means reverse action. 
 * 
 * @author Arvid Juskaitis
 */
@SuppressWarnings("serial")
public class IsUserInRoleTag extends TagSupport {

	/**
	 * Tag attribute - comma separated roles to test.
	 */
	private String roles;
	
	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	/**
	 * Tag attribute - reverse action. If it is set, tag acts as if user is NOT logged in. 
	 */
	private boolean not;

	public boolean isNot() {
		return not;
	}

	public void setNot(boolean not) {
		this.not = not;
	}

	@Override
	public int doStartTag() throws JspException {
		// Check roles
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		boolean inRole = false;
		StringTokenizer st = new StringTokenizer(roles, ", ");
		while (st.hasMoreTokens()) {
			String role = st.nextToken();
			if (request.isUserInRole(role)) {
				inRole = true;
				break;
			}
		}
//inRole = true;	// remove this in prod!			
		// Perform action
		if ((inRole && !not) || (inRole && not)) {
			return EVAL_BODY_INCLUDE;
		} else {
			return SKIP_BODY;
		}
	}

	@Override
	public void release() {
		super.release();
		not = false;
	}
}

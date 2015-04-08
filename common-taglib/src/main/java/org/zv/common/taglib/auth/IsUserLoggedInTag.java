package org.zv.common.taglib.auth;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.HttpServletRequest;

/**
 * Test if user has logged in and if so, evaluate body, unless attribute 'not' is set to true, 
 * which means reverse action.
 *
 * @author Arvid Juskaitis
 */
@SuppressWarnings("serial")
public class IsUserLoggedInTag extends TagSupport {

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
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String user = request.getRemoteUser();
//user = "";	// remove this in prod!			
		if ((user != null && !not) || (user == null && not)) {
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

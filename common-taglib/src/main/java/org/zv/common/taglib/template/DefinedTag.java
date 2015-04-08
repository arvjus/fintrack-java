package org.zv.common.taglib.template;

import java.io.IOException;

import javax.servlet.jsp.JspException;

/**
 * Used to test definition of element.
 * 
 * @author Arvid Juskaitis
 */
public class DefinedTag extends ElementTagSupport {

	/**
	 * Tag attribute - reverse action. If it is set, tag acts as NOT DEFINED. 
	 */
	private boolean not;

	public boolean isNot() {
		return not;
	}

	public void setNot(boolean not) {
		this.not = not;
	}

	/**
	 * Just do it.
	 */
	public void doTag() throws JspException, IOException {
		ElementValue value = getValue(); 
		if ((not == true && value == null) || (not == false && value != null)) {
			getJspBody().invoke(null);
		}
	}
}

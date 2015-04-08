package org.zv.common.taglib.template;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;

/**
 * Used inside templates to insert defined elements.
 * 
 * @author Arvid Juskaitis
 */
public class InsertTag extends ElementTagSupport {

	/**
	 * Just do it.
	 */
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		ElementValue value = getValue(); 
		if (value != null) {
			// put a content here or include external resource
			if (value.getType() == ElementValue.ValueType.DIRECT_CONTENT) {
				pageContext.getOut().write(value.getValue());
			} else if (value.getType() == ElementValue.ValueType.PAGE_SOURCE) {
				try {
					pageContext.include(value.getValue());
				} catch (ServletException e) {
					throw new JspException(e);
				}
			} 
		} else {
			// if no element was defined, evaluate body
			JspFragment fragment = getJspBody();
			if (fragment != null) {
				fragment.invoke(null);
			}
		}
	}
}

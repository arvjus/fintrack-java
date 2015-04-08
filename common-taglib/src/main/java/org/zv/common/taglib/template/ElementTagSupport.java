package org.zv.common.taglib.template;

import java.util.Map;
import java.util.Stack;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * It is a base class for element tags.
 * 
 * @author Arvid Juskaitis
 */
@SuppressWarnings("unchecked")
public class ElementTagSupport extends SimpleTagSupport {

	/**
	 * The name of element. 
	 */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get stack and map of enclosed compose tag 
	 */
	protected ElementValue getValue() throws JspException {
		PageContext pageContext = (PageContext) getJspContext();
		Stack<Map<String, ElementValue>> stack = (Stack<Map<String, ElementValue>>) pageContext.getAttribute(
				CompositionTag.TEMPLATE_STACK_ATTRIBUTE, PageContext.REQUEST_SCOPE); 
		if (stack == null) {
			throw new JspException("parent's stack not found");
		}
		Map<String, ElementValue> map = (Map<String, ElementValue>) stack.peek(); 
		if (map == null) {
			throw new JspException("parent's map not found");
		}

		return map.get(name);
	}
}

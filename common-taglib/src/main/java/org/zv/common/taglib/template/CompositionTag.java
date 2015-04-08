package org.zv.common.taglib.template;

import java.util.Stack;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Main composing tag.
 * 
 * @author Arvid Juskaitis
 */
@SuppressWarnings("unchecked")
public class CompositionTag extends SimpleTagSupport {

	/**
	 * The name of attribute in request context.
	 */
	static String TEMPLATE_STACK_ATTRIBUTE = "template-stack";
	
	/**
	 * We keep element collection in stack because of templates could be nested.
	 */
	private Stack<Map<String, ElementValue>> stack;
	
	/**
	 * Template file. 
	 */
	private String template;
	
	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * Just do it.
	 */
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();

		// create a stack
		stack = getStack();
		stack.push(new HashMap<String, ElementValue>());
		
		// evaluate a body and createimplicit variable for optional result 
		JspFragment body = getJspBody();
		StringWriter writer = new StringWriter(); 
		body.invoke(writer);
		pageContext.setAttribute("compose-body", writer.toString(), PageContext.REQUEST_SCOPE);
		
		// include a template and reduce stack
		try {
			pageContext.include(template);
		} catch (ServletException e) {
			throw new JspException(e);
		} finally {
			stack.pop();
		}
	}
	
	/**
	 * Retrieves/creates stack.
	 */
	public Stack<Map<String, ElementValue>> getStack() {
		PageContext pageContext = (PageContext) getJspContext();
		Stack<Map<String, ElementValue>> stack = 
			(Stack<Map<String, ElementValue>> ) pageContext.getAttribute(TEMPLATE_STACK_ATTRIBUTE, PageContext.REQUEST_SCOPE);
		if (stack == null) {
			stack = new Stack<Map<String, ElementValue>>();
			pageContext.setAttribute(TEMPLATE_STACK_ATTRIBUTE, stack, PageContext.REQUEST_SCOPE);
		}
		return stack;
	}
}

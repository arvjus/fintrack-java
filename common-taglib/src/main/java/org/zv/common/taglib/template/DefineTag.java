package org.zv.common.taglib.template;

import java.util.Stack;
import java.util.Map;
import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Used to define include elements.
 * 
 * @author Arvid Juskaitis
 */
public class DefineTag extends SimpleTagSupport {

	/**
	 * Tag attribute - the name of element (optional). 
	 */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Tag attribute - the include page (optional). 
	 */
	private String src;
	
	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	/**
	 * Just do it.
	 */
	public void doTag() throws JspException, IOException {
		// Check attributes
		if (name == null && src == null) {
			throw new JspException("a 'name' or 'src' attribute must be set");
		}
		
		// Get parent's stack and map
		JspTag jspTag = getParent();
		if (jspTag == null || !(jspTag instanceof CompositionTag)) {
			throw new JspException("expected enclosing <tmpl:compose> tag not found");
		}
		Stack<Map<String, ElementValue>> stack = ((CompositionTag)jspTag).getStack();
		if (stack == null) {
			throw new JspException("parent's stack not found");
		}
		Map<String, ElementValue> map = (Map<String, ElementValue>) stack.peek(); 
		if (map == null) {
			throw new JspException("parent's map not found");
		}
		
		if (src == null) {
			// Evaluate body and add to the map
			JspFragment body = getJspBody();
			StringWriter writer = new StringWriter();
			if (body != null) {
				body.invoke(writer);
			}
			map.put(name, new ElementValue(writer.toString(), ElementValue.ValueType.DIRECT_CONTENT));
		} else {
			// Skip body and add reference to the map
			map.put(name, new ElementValue(src, ElementValue.ValueType.PAGE_SOURCE));
		}
	}
}

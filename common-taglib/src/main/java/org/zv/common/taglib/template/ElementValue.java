package org.zv.common.taglib.template;

/**
 * Class represents included element or reference.
 * 
 * @author Arvid Juskaitis
 */
public class ElementValue {

	enum ValueType {
		/**
		 * Evaluated jsp content.
		 */
		DIRECT_CONTENT,
		
		/**
		 * Page to include.
		 */
		PAGE_SOURCE
	}
	
	/**
	 * Keep value here.
	 */
	private String value;
	
	/**
	 * Kind of value.
	 */
	private ValueType type;
	
	/**
	 * Constructor. 
	 */
	public ElementValue(String value, ValueType type) {
		this.value = value;
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public ValueType getType() {
		return type;
	}
}

package org.zv.fintrack.model;

import java.io.Serializable;

public class Category implements Serializable {
	private static final long serialVersionUID = -5475118247338238299L;

	public	String	categoryId;
	public	String	name;
	public	String	nameShort;
	public	String	descr;

	public String toString() {
		return name;
		//return categoryId + "; " + name + "; " + nameShort + "; " + descr;
	}
}

package org.zv.fintrack.model;

import java.io.Serializable;

public class Income implements Serializable {
	private static final long serialVersionUID = -4920407114043902453L;

	public	Integer		incomeId;
	public	String		userId;
	public	String		createDate;
	public	float		amount;
	public	String		descr;
	
	public String toString() {
		return incomeId + "; " + userId + "; " + createDate + "; " + amount + "; " + descr;
	}
}

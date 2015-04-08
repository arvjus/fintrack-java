package org.zv.fintrack.model;

import java.io.Serializable;

public class Expense implements Serializable {
	private static final long serialVersionUID = 595041840422179084L;

	public	Integer		expenseId;
	public	String		category;
	public	String		userId;
	public	String		createDate;
	public	float		amount;
	public	String		descr;

	public String toString() {
		return expenseId + "; " + category + "; " + userId + "; " + createDate + "; " + amount + "; " + descr;
	}
}

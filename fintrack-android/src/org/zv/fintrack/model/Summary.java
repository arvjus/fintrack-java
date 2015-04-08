package org.zv.fintrack.model;

import java.io.Serializable;

public class Summary implements Serializable {
	private static final long serialVersionUID = 4941712175086083600L;
	public String	group;
	public long		count;
	public double	amount;

	public String toString() {
		return group + "; " + count + "; " + amount;
	}
}

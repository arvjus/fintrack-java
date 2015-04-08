package org.zv.fintrack.support;

import java.util.Calendar;
import java.util.Date;

/**
 * Some utilities for testing.
 * 
 * @author arju
 *
 */
public class Utils {

	private Utils() {
	}

	/**
	 * Create date object by yyyy, mm, dd
	 */
	public static Date makeDate(int yyyy, int mm, int dd) {
		Calendar date = Calendar.getInstance();
		date.set(yyyy, mm - 1, dd);
		return date.getTime();
	}
}

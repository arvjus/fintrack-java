package org.zv.fintrack.util;

import java.math.BigInteger;
import java.util.Map;
import java.util.HashMap;

public class FintrackUtils {

	private FintrackUtils() {
	}

	public static Map<?, ?> arrayToMap(Object [] array, Object value) {
		if (array == null) {
			return null;
		}
		HashMap<Object, Object> map = new HashMap<Object, Object>(); 
		for (Object object : array) {
			map.put(object, value);
		}
		return map;
	}

	public static long numericToLong(Object obj) {
		long value = 0;
		if (obj != null) {
			if (obj instanceof BigInteger) {
				value = ((BigInteger) obj).longValue();
			} else if (obj instanceof Double) {
				value = ((Double) obj).longValue();
			} else if (obj instanceof Float) {
				value = ((Float) obj).longValue();
			} else if (obj instanceof Long) {
				value = ((Long) obj).longValue();
			} else if (obj instanceof Integer) {
				value = ((Integer) obj).longValue();
			} else {
				throw new RuntimeException("unsupported numeric type: " + obj.getClass());
			}
		}
		return value;
	}

	public static double numericToDouble(Object obj) {
		double value = 0.0;
		if (obj != null) {
			if (obj instanceof BigInteger) {
				value = ((BigInteger) obj).doubleValue();
			} else if (obj instanceof Double) {
				value = ((Double) obj).doubleValue();
			} else if (obj instanceof Float) {
				value = ((Float) obj).doubleValue();
			} else if (obj instanceof Long) {
				value = ((Long) obj).doubleValue();
			} else if (obj instanceof Integer) {
				value = ((Integer) obj).doubleValue();
			} else {
				throw new RuntimeException("unsupported numeric type: " + obj.getClass());
			}
		}
		return value;
	}
	
	private static final String [][] mapping = new String [][] {{"01", "jan"}, {"02", "feb"}, {"03", "mar"}, {"04", "apr"}, {"05", "maj"}, 
		{"06", "jun"}, {"07", "jul"}, {"08", "aug"}, {"09", "sep"}, {"10", "okt"}, {"11", "nov"}, {"12", "dec"}};  
	public static String yyyymm2month(String yyyymm) {
		String month = yyyymm.substring(5);
		for (int i = 0; i < mapping.length; i++) {
			if (mapping[i][0].equals(month)) {
				return mapping[i][1];
			}
		}
		return yyyymm;
	}
}

package org.zv.common.mvc;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;

/**
 * Helper class for reading request parameters and set as command attributes.
 *  
 * @author Arvid Juskaitis
 */
public class ValueConverter {
	static HashMap<Class<?>, Class<?>> classMapping;
	
	static HashMap<Class<?>, Class<?>> getClassMapping() {
		if (classMapping == null) {
			classMapping = new HashMap<Class<?>, Class<?>>();
			
			classMapping.put(Date.class, java.sql.Date.class);
			classMapping.put(Integer.class, Integer.class);
			classMapping.put(int.class, Integer.class);
			classMapping.put(Long.class, Long.class);
			classMapping.put(long.class, Long.class); 
			classMapping.put(Float.class, Float.class);
			classMapping.put(float.class, Float.class);
			classMapping.put(Double.class, Double.class); 
			classMapping.put(double.class, Double.class); 
			classMapping.put(Boolean.class, Boolean.class); 
			classMapping.put(boolean.class, Boolean.class); 
/*
			classMapping.put(String[].class, String.class); 
			classMapping.put(Date[].class, java.sql.Date.class);
			classMapping.put(Integer[].class, Integer.class);
			classMapping.put(int[].class, Integer.class);
			classMapping.put(Long[].class, Long.class);
			classMapping.put(long[].class, Long.class); 
			classMapping.put(Float[].class, Float.class);
			classMapping.put(float[].class, Float.class);
			classMapping.put(Double[].class, Double.class); 
			classMapping.put(double[].class, Double.class); 
			classMapping.put(Boolean[].class, Boolean.class); 
			classMapping.put(boolean[].class, Boolean.class);
*/			
		}
		return classMapping;
	}
	
	/**
	 * Load value from String
	 * 
	 * @param clazz
	 * @param parameter
	 * @return instance of clazz
	 */
	Object valueOf(String [] parameterValues, Class<?> clazz) throws Exception {
		if (clazz.isArray()) {
			if (clazz == String[].class) {
				return parameterValues;
			}
			throw new Exception("no conversion from " + clazz + "class");
		} else {
			if (clazz == String.class) {
				return parameterValues[0];
			}
			Class<?> mappedClazz = getClassMapping().get(clazz);
			if (mappedClazz == null) {
				throw new Exception("no conversion from " + clazz + "class");
			}
			Method method = mappedClazz.getMethod("valueOf", new Class [] { String.class });
			return method.invoke(clazz, parameterValues[0].trim());
		}
	}
}

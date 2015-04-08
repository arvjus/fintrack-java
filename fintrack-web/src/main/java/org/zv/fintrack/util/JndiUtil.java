package org.zv.fintrack.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Performs JNDI lookups
 * 
 * @author arvid.juskaitis
 */
public class JndiUtil {
	public static Object lookup(String name) {
		try {
			Context context = new InitialContext();
			return context.lookup("java:comp/env/" + name);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null;
	}
}

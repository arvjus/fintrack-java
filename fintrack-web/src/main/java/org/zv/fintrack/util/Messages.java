package org.zv.fintrack.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Locale;

/**
 * Class provides access to message bundle.
 * 
 * @author arvid.juskaitis
 */
public class Messages {
	/**
	 * Class loader.
	 * 
	 * @param defaultObject
	 * @return
	 */
	protected static ClassLoader getCurrentClassLoader(Object defaultObject) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = defaultObject.getClass().getClassLoader();
		}
		return loader;
	}

	/**
	 * Retrieve and form a message.
	 * 
	 * @param bundleName
	 * @param locale
	 * @param key
	 * @param params
	 * @return
	 */
	public static String getMessageResourceString(String bundleName, Locale locale, String key, Object[] params) {
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, getCurrentClassLoader(params));

		String text = null;
		try {
			text = bundle.getString(key);
		} catch (MissingResourceException e) {
			text = key;
		}

		if (params != null) {
			MessageFormat mf = new MessageFormat(text, locale);
			text = mf.format(params, new StringBuffer(), null).toString();
		}
		return text;
	}
}

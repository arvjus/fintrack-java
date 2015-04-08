package org.zv.fintrack.pd;

/**
 * Group entity.
 * 
 * @author arvid.juskaitis
 */
public enum Role {
	/**
	 * Read-only operations.
	 */
	viewer,
	
	/**
	 * Data modification operations.
	 */
	reporter,
	
	/**
	 * Administrative tasks.
	 */
	admin;
}

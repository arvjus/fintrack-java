package org.zv.fintrack.support;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import junit.framework.TestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.IDatabaseTester;
import org.dbunit.JndiDatabaseTester;

/**
 * Base class for test cases, where data is loaded manually.
 *  
 * @author arju
 */
public abstract class LoadDataTestCaseSupport extends TestCase {

	/**
	 * Target database.
	 */
	private static final String JNDI_DATASET = "java:openejb/Resource/fintrackDB";
	
	/**
	 * Used to cleanup database etc.
	 */
	private IDatabaseTester databaseTester;
	
    /**
     * Call this first whenever need to access JNDI or logging.
     * 
     * @return initial context. 
     * @throws NamingException
     */
	protected Context getContext() throws NamingException {
		return new InitialContext();
	}

	/**
	 * Call this 1st to enable JNDI and logging
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
    	getContext(); 
	}
	
    /**
     * Loads data set from xml file. Performs CLEAR_INSERT operation on database. 
     * 
     * @param filename
     * @throws Exception
     */
	protected void loadData(String filename) throws Exception {
		IDataSet dataSet = new XmlDataSet(ClassLoader.getSystemClassLoader().getResourceAsStream(filename));
		databaseTester = new JndiDatabaseTester(JNDI_DATASET);
    	databaseTester.setDataSet(dataSet);
    	databaseTester.onSetup();
    }

	/**
     * Unload data. Performs CLEAR operation on loaded database.
     * 
     * @throws Exception
     */
	protected void unloadData() throws Exception {
		if (databaseTester != null) {
			databaseTester.onTearDown();
		}
    	databaseTester = null;
    }
}

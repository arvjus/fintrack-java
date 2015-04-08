package org.zv.fintrack.support;

/**
 * Base class for test cases, where data is loaded automatically on setup.
 *  
 * @author arju
 */
public abstract class SetUpDataTestCaseSupport extends LoadDataTestCaseSupport {

	/**
	 * The name of file to load.
	 * 
	 * @return filename of data file.
	 */
	protected abstract String getDataName();
	
	/**
	 * Performs CLEAR_INSERT operation on database.
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		loadData(getDataName());
	}

	/**
	 * Performs CLEAR operation on loaded database. 
	 * You can disable cleanup by overriding this method without calling super.tearDown().
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		unloadData();
	}
}

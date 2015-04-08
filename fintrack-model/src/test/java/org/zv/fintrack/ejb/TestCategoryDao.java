package org.zv.fintrack.ejb;

import java.util.List;

import javax.naming.Context;

import org.apache.commons.logging.Log;
import org.zv.fintrack.pd.Category;
import org.zv.fintrack.support.LoadDataTestCaseSupport;
import org.zv.fintrack.util.LoggerFactory;
import org.zv.fintrack.ejb.api.CategoryDao;

public class TestCategoryDao extends LoadDataTestCaseSupport {

	/**
	 * Class logger.
	 */
	private Log log = LoggerFactory.make();
	
	/**
	 * Load all categories.
	 * 
	 * @throws Exception
	 */
	public void testLoadAllCategories() throws Exception {
		Context context = getContext(); // call this 1st to enable logging
		log.info("testLoadAllCategories");
		loadData("testCategories.xml");
		CategoryDao categoryDao = (CategoryDao) context.lookup("CategoryDaoBeanLocal");
        List<Category> categories = categoryDao.getAll();
        assertEquals("invalid number of records", 3, categories.size());
    }
}

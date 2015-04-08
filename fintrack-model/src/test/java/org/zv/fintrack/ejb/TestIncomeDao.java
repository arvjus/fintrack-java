package org.zv.fintrack.ejb;

import java.util.Date;
import java.util.List;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;

import org.zv.fintrack.util.LoggerFactory;
import org.zv.fintrack.pd.Income;
import org.zv.fintrack.pd.bean.SummaryBean;
import org.zv.fintrack.ejb.api.IncomeDao;
import org.zv.fintrack.support.SetUpDataTestCaseSupport;
import org.zv.fintrack.support.Utils;

public class TestIncomeDao extends SetUpDataTestCaseSupport {
	
	/**
	 * Class logger.
	 */
	private Log log = LoggerFactory.make();

	@Override
	protected String getDataName() {
		return "testIncomes.xml";
	}

	@Override
	protected void tearDown() throws Exception {
		// we can skip cleanup operation
	}
	
	/**
	 * Test retrieving of records.
	 * 
	 * @throws Exception
	 */
	public void testFindIncomes() throws Exception {
		log.info("getAll unlimited");
        List<Income> incomes = getIncomeDao().getAll(1000);
        assertEquals("invalid number of records", 30, incomes.size());

		log.info("getAll unlimited");
        incomes = getIncomeDao().getAll(0);
        assertEquals("invalid number of records", 30, incomes.size());

		log.info("getAll limited to 15");
        incomes = getIncomeDao().getAll(15);
        assertEquals("invalid number of records", 15, incomes.size());

		log.info("getById");
        Income income = getIncomeDao().getById(10);
		assertNotNull("record was expected", income);
        assertEquals("wrong record", "Foraldrarpenning", income.getDescr());
        
		log.info("getById nonexisting");
        income = getIncomeDao().getById(100);
		assertNull("record was not expected", income);

		log.info("getPlain in range");
        incomes = getIncomeDao().getPlain(Utils.makeDate(2004, 1, 1), Utils.makeDate(2005, 1, 1), null);
        assertEquals("invalid number of records", 16, incomes.size());
		
		log.info("getPlain in range with user");
        incomes = getIncomeDao().getPlain(Utils.makeDate(2004, 1, 1), Utils.makeDate(2005, 1, 1), "user");
        assertEquals("invalid number of records", 5, incomes.size());
		
		log.info("getSummarySimple no results");
		List<SummaryBean> summary_incomes = getIncomeDao().getSummarySimple(Utils.makeDate(2010, 1, 1), Utils.makeDate(2010, 1, 1));
        assertEquals("invalid number of records", 1, summary_incomes.size());
		
		log.info("getSummarySimple in range");
		summary_incomes = getIncomeDao().getSummarySimple(Utils.makeDate(2004, 1, 1), Utils.makeDate(2005, 1, 1));
        assertEquals("invalid number of records", 1, summary_incomes.size());
        assertEquals("wrong amount", 55442.0, summary_incomes.get(0).getAmount());
		
        log.info("getSummaryByMonth in range");
		summary_incomes = getIncomeDao().getSummaryByMonth(Utils.makeDate(2004, 1, 1), Utils.makeDate(2005, 1, 1));
        assertEquals("invalid number of records", 8, summary_incomes.size());
        for (SummaryBean summary_income : summary_incomes) {
        	if ("2004-06".equals(summary_income.getGroup())) {
                assertEquals("wrong amount", 13333.0, summary_income.getAmount());
        	}
        }
    }
	
	/**
	 * Test updating, removing records.
	 * 
	 * @throws Exception
	 */
	public void testUpdateRemoveIncomes() throws Exception {
		log.info("persist");
        Income income = new Income();
        income.setDescr("some description");
        income.setAmount(12345.6f);
        income.setCreateDate(new Date());
        income.setUserId("test");
        income = getIncomeDao().save(income);
        List<Income> incomes = getIncomeDao().getAll(0);
        assertEquals("invalid number of records", 31, incomes.size());

		log.info("delete");
		getIncomeDao().delete(income.getIncomeId());
        incomes = getIncomeDao().getAll(0);
        assertEquals("invalid number of records", 30, incomes.size());
    }
	
	/**
	 * Get a new instance of IncomeDao 
	 * @throws NamingException
	 */
	private IncomeDao getIncomeDao() throws NamingException {
		return (IncomeDao)getContext().lookup("IncomeDaoBeanLocal");
	}
}

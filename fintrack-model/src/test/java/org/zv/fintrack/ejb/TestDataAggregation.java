package org.zv.fintrack.ejb;

import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;

import org.zv.fintrack.util.LoggerFactory;
import org.zv.fintrack.pd.bean.SummaryBean;
import org.zv.fintrack.ejb.api.IncomeDao;
import org.zv.fintrack.ejb.api.ExpenseDao;
import org.zv.fintrack.ejb.api.DataAggregation;
import org.zv.fintrack.support.SetUpDataTestCaseSupport;
import org.zv.fintrack.support.Utils;

public class TestDataAggregation extends SetUpDataTestCaseSupport {
	
	/**
	 * Class logger.
	 */
	private Log log = LoggerFactory.make();

	@Override
	protected String getDataName() {
		return "testDataAggregation.xml";
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
	public void testAggregation() throws Exception {
        log.info("incomeTotal aggregation");
        List<SummaryBean> summary_incomes = getIncomeDao().getSummaryByMonth(Utils.makeDate(2004, 1, 1), Utils.makeDate(2005, 1, 1));
        assertEquals("invalid number of records", 8, summary_incomes.size());
        SummaryBean summary_incomes_total = getDataAggregation().total(summary_incomes);
		assertNotNull("record was expected", summary_incomes_total);
        assertEquals("wrong amount", 55442.0, summary_incomes_total.getAmount());

        log.info("expenseTotal aggregation");
        log.info("getSummaryByMonth in range");
        List<SummaryBean> summary_expenses = getExpenseDao().getSummaryByMonth(Utils.makeDate(2007, 1, 1), Utils.makeDate(2008, 1, 1), null);
        assertEquals("invalid number of records", 4, summary_expenses.size());
        SummaryBean summary_expenses_total = getDataAggregation().total(summary_expenses); 
		assertNotNull("record was expected", summary_expenses_total);
        assertEquals("wrong amount", 72199.0, summary_expenses_total.getAmount());
        
        log.info("join incomes, expenses");
        List<Map<String, Object>> results = getDataAggregation().joinSummary(summary_incomes, summary_expenses);
        assertEquals("invalid number of records", 12, results.size());
        
	}
	
	/**
	 * Get a new instance of IncomeDao 
	 * @throws NamingException
	 */
	private IncomeDao getIncomeDao() throws NamingException {
		return (IncomeDao)getContext().lookup("IncomeDaoBeanLocal");
	}

	/**
	 * Get a new instance of ExpenseDao 
	 * @throws NamingException
	 */
	private ExpenseDao getExpenseDao() throws NamingException {
		return (ExpenseDao)getContext().lookup("ExpenseDaoBeanLocal");
	}

	/**
	 * Get a new instance of DataAggregation 
	 * @throws NamingException
	 */
	private DataAggregation getDataAggregation() throws NamingException {
		return (DataAggregation)getContext().lookup("DataAggregationBeanLocal");
	}
}

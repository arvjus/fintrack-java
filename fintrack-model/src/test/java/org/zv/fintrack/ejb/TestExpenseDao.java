package org.zv.fintrack.ejb;

import java.util.Date;
import java.util.List;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;

import org.zv.fintrack.pd.Expense;
import org.zv.fintrack.pd.bean.SummaryBean;
import org.zv.fintrack.util.LoggerFactory;
import org.zv.fintrack.ejb.api.ExpenseDao;
import org.zv.fintrack.support.SetUpDataTestCaseSupport;
import org.zv.fintrack.support.Utils;

public class TestExpenseDao extends SetUpDataTestCaseSupport {
	
	/**
	 * Class logger.
	 */
	private Log log = LoggerFactory.make();

	@Override
	protected String getDataName() {
		return "testExpenses.xml";
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
	public void testFindExpenses() throws Exception {
		String[] categories = new String[] {"ma", "iv"};
		
		log.info("getAll unlimited");
        List<Expense> expenses = getExpenseDao().getAll(1000);
        assertEquals("invalid number of records", 210, expenses.size());

		log.info("getAll unlimited");
        expenses = getExpenseDao().getAll(0);
        assertEquals("invalid number of records", 210, expenses.size());

		log.info("getAll limited to 50");
        expenses = getExpenseDao().getAll(50);
        assertEquals("invalid number of records", 50, expenses.size());

		log.info("getById");
        Expense expense = getExpenseDao().getById(10);
		assertNotNull("record was expected", expense);
        assertEquals("wrong record", "bredband", expense.getDescr());
        
		log.info("getById nonexisting");
        expense = getExpenseDao().getById(300);
		assertNull("record was not expected", expense);

		log.info("getPlain in range");
        expenses = getExpenseDao().getPlain(Utils.makeDate(2007, 1, 1), Utils.makeDate(2008, 1, 1), null, null);
        assertEquals("invalid number of records", 182, expenses.size());
		
		log.info("getPlain in range with cat");
        expenses = getExpenseDao().getPlain(Utils.makeDate(2007, 1, 1), Utils.makeDate(2008, 1, 1), null, categories);
        assertEquals("invalid number of records", 137, expenses.size());
		
		log.info("getPlain in range with user");
        expenses = getExpenseDao().getPlain(Utils.makeDate(2007, 1, 1), Utils.makeDate(2008, 1, 1), "user", null);
        assertEquals("invalid number of records", 5, expenses.size());
		
		log.info("getPlain in range with user, cat");
        expenses = getExpenseDao().getPlain(Utils.makeDate(2007, 1, 1), Utils.makeDate(2008, 1, 1), "user",  categories);
        assertEquals("invalid number of records", 3, expenses.size());
		
		log.info("getSummaryByCategory no results");
		List<SummaryBean> summary_expenses = getExpenseDao().getSummaryByCategory(Utils.makeDate(2010, 1, 1), Utils.makeDate(2010, 1, 1), null);
        assertEquals("invalid number of records", 0, summary_expenses.size());

        log.info("getSummaryByCategory in range");
		summary_expenses = getExpenseDao().getSummaryByCategory(Utils.makeDate(2007, 1, 1), Utils.makeDate(2008, 1, 1), null);
        assertEquals("invalid number of records", 8, summary_expenses.size());
        for (SummaryBean summary_expense : summary_expenses) {
        	if ("Maistas".equals(summary_expense.getGroup())) {
                assertEquals("wrong amount", 11100.0, summary_expense.getAmount());
        	}
        }
        
		log.info("getSummaryByCategory in range");
		summary_expenses = getExpenseDao().getSummaryByCategory(Utils.makeDate(2007, 1, 1), Utils.makeDate(2008, 1, 1), categories);
        assertEquals("invalid number of records", 2, summary_expenses.size());
        for (SummaryBean summary_expense : summary_expenses) {
        	if ("Ivairus".equals(summary_expense.getGroup())) {
                assertEquals("wrong amount", 20415.0, summary_expense.getAmount());
        	}
        }
		
        log.info("getSummaryByMonth in range");
		summary_expenses = getExpenseDao().getSummaryByMonth(Utils.makeDate(2007, 1, 1), Utils.makeDate(2008, 1, 1), null);
        assertEquals("invalid number of records", 4, summary_expenses.size());
        for (SummaryBean summary_expense : summary_expenses) {
        	if ("2007-02".equals(summary_expense.getGroup())) {
                assertEquals("wrong amount", 30168.0, summary_expense.getAmount());
        	}
        }
		
        log.info("getSummaryByMonth in range with cat");
		summary_expenses = getExpenseDao().getSummaryByMonth(Utils.makeDate(2007, 1, 1), Utils.makeDate(2008, 1, 1), categories);
        assertEquals("invalid number of records", 4, summary_expenses.size());
        for (SummaryBean summary_expense : summary_expenses) {
        	if ("2007-02".equals(summary_expense.getGroup())) {
                assertEquals("wrong amount", 20680.0, summary_expense.getAmount());
        	}
        }
    }
	
	/**
	 * Test updating, removing records.
	 * 
	 * @throws Exception
	 */
	public void testUpdateRemoveExpenses() throws Exception {
		log.info("persist");
        Expense expense = new Expense();
        expense.setCategoryId("iv");
        expense.setDescr("some description");
        expense.setAmount(12345.6f);
        expense.setCreateDate(new Date());
        expense.setUserId("test");
        expense = getExpenseDao().save(expense);
        List<Expense> expenses = getExpenseDao().getAll(0);
        assertEquals("invalid number of records", 211, expenses.size());

		log.info("delete");
		getExpenseDao().delete(expense.getExpenseId());
        expenses = getExpenseDao().getAll(0);
        assertEquals("invalid number of records", 210, expenses.size());
    }
	
	/**
	 * Get a new instance of ExpenseDao 
	 * @throws NamingException
	 */
	private ExpenseDao getExpenseDao() throws NamingException {
		return (ExpenseDao)getContext().lookup("ExpenseDaoBeanLocal");
	}
}
